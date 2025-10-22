document.addEventListener("DOMContentLoaded", () => {
  const reviewInput = document.querySelector("#reviewText");
  const submitBtn = document.querySelector("#submitBtn");
  const reviewList = document.querySelector("#reviewList");
  const writeLectureSelectBtn = document.querySelector("#writeLectureSelect .select-btn");
  const categorySelectBtn = document.querySelector("#categorySelect .select-btn");

  // 선택된 강좌 ID & 별점 저장용
  let selectedCourseId = null;
  let selectedRating = 0;
  let reviews = [];

  // ===== 후기 목록 불러오기 =====
  async function loadReviews() {
    const category = categorySelectBtn.dataset.selected || "전체";
    console.log("📥 후기 목록 요청 중... category =", category);

    try {
      const res = await fetch(`/review?category=${encodeURIComponent(category)}`);
      if (!res.ok) throw new Error("서버 응답 실패");
      reviews = await res.json();
      render();
    } catch (err) {
      console.error("후기 로드 실패:", err);
    }
  }

  // ===== 후기 렌더링 =====
  function render() {
    reviewList.innerHTML = "";
    const category = categorySelectBtn.dataset.selected || "전체";

    reviews
      .filter(r => category === "전체" || r.category === category)
      .forEach(r => {
        const stars = Array.from({ length: 5 }, (_, i) =>
          i < r.rating ? "★" : "☆"
        ).join("");

        const li = document.createElement("li");
        li.className = "review-item";
        li.innerHTML = `
        <div class="review-header">
          <strong>${r.studentName}</strong> | ${r.createdAt?.split("T")[0] || ""}<br>
          ${r.category} | ${r.courseTitle}
        </div>
        <div class="review-rating" style="color:#fbbf24; font-size:16px;">${stars}</div>
        <div class="review-content">${r.content}</div>
        ${r.isMine ? `<button class="delete-btn" data-id="${r.reviewId}">삭제</button>` : ""}
      `;
        reviewList.appendChild(li);
      });

    if (reviews.length === 0) {
      reviewList.innerHTML = "<li style='text-align:center; color:gray;'>등록된 후기가 없습니다.</li>";
    }
  }

  // ===== 별점 기능 =====
  const stars = document.querySelectorAll(".star");
  stars.forEach(star => {
    star.addEventListener("click", () => {
      selectedRating = parseInt(star.dataset.value);
      stars.forEach(s => s.classList.remove("selected"));
      for (let i = 0; i < selectedRating; i++) {
        stars[i].classList.add("selected");
      }
    });
  });

  // ===== select-box 로직 =====
  const selectBoxes = document.querySelectorAll(".select-box");
  selectBoxes.forEach(box => {
    const button = box.querySelector(".select-btn");
    const options = box.querySelector(".options");

    button.addEventListener("click", () => {
      box.classList.toggle("active");
    });

    options.querySelectorAll("li").forEach(opt => {
      opt.addEventListener("click", async () => {
        button.textContent = opt.textContent;
        button.dataset.selected = opt.dataset.value;
        box.classList.remove("active");

        // 강좌 선택 시
        if (box.id === "writeLectureSelect") {
          selectedCourseId = opt.dataset.id;
          console.log("선택된 강좌 ID:", selectedCourseId);
          return;
        }

        // 카테고리 선택 시
        if (box.id === "categorySelect") {
          const category = opt.dataset.value;
          try {
            const res = await fetch(`/review/filter?category=${encodeURIComponent(category)}`);
            const reviews = await res.json();

            const reviewList = document.querySelector("#reviewList");
            reviewList.innerHTML = "";

            if (reviews.length === 0) {
              reviewList.innerHTML = `<li style="text-align:center; color:gray;">해당 카테고리의 후기가 없습니다.</li>`;
              return;
            }

            reviews.forEach(r => {
              const li = document.createElement("li");
              li.className = "review-item";
              li.innerHTML = `
                <div class="review-header">
                  <strong>${r.studentName}</strong> | ${r.createdAt?.split("T")[0] || ""}<br>
                  ${r.category} | ${r.courseTitle}
                </div>
                <div class="review-content">${r.content}</div>
                ${r.isMine ? `<button class="delete-btn" data-id="${r.reviewId}">삭제</button>` : ""}
              `;
              reviewList.appendChild(li);
            });
          } catch (err) {
            console.error("필터링 중 오류 발생:", err);
          }
        }
      });
    });
  });

  // ===== 후기 등록 =====
  submitBtn.addEventListener("click", async () => {
    const userId = document.getElementById("currentUserId")?.value;
    if (!userId) {
      alert("로그인 후 이용해주세요.");
      location.href = "/common/loginForm";
      return;
    }

    const text = reviewInput.value.trim();
    if (!selectedCourseId) return alert("강좌를 선택해주세요.");
    if (!text) return alert("후기를 입력해주세요.");
    if (selectedRating === 0) return alert("별점을 선택해주세요.");

    const reviewData = {
      courseId: selectedCourseId,
      studentId: userId,
      content: text,
      rating: selectedRating
    };

    console.log("서버 전송 데이터:", reviewData);

    try {
      const res = await fetch("/review/regist", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(reviewData)
      });

      const msg = await res.text();
      if (msg === "success") {
        alert("후기가 등록되었습니다!");
        location.reload();
        
      } else if (msg == "duplicate") {
        alert("이미 해당 강좌의 후기를 작성하셨습니다.");
        return;
      }
      else if (msg === "unauthorized") {
        alert("로그인 후 이용해주세요.");
        location.href = "/common/loginForm";
      } else {
        alert("등록 실패");
      }
    } catch (err) {
      console.error("등록 에러:", err);
      alert("서버 통신 중 오류가 발생했습니다.");
    }
  });

  // ===== 후기 삭제 =====
  reviewList.addEventListener("click", async (e) => {
    if (e.target.classList.contains("delete-btn")) {
      const reviewId = e.target.dataset.id;
      if (!confirm("정말 삭제하시겠습니까?")) return;

      try {
        const res = await fetch(`/review/delete/${reviewId}`, { method: "POST" });
        const msg = await res.text();

        if (msg === "deleted") {
          alert("삭제 완료!");
          location.reload();
        } else if (msg === "forbidden") {
          alert("삭제 권한이 없습니다.");
        } else if (msg === "unauthorized") {
          alert("로그인 후 이용해주세요.");
          location.href = "/common/loginForm";
        } else if (msg === "not_found") {
          alert("해당 후기를 찾을 수 없습니다.");
        } else {
          alert("삭제 실패");
        }
      } catch (err) {
        console.error("삭제 에러:", err);
      }
    }
  });

  // 초기 로드
  loadReviews();
});
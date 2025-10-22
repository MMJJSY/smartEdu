document.addEventListener("DOMContentLoaded", () => {
  /* ========== 카테고리 탭 이동 ========== */
  const tabs = document.querySelectorAll(".category-tabs li a");

  tabs.forEach(tab => {
    tab.addEventListener("click", (e) => {
      e.preventDefault();
      const href = tab.getAttribute("href");

      // 탭 전환
      tabs.forEach(t => t.parentElement.classList.remove("active"));
      tab.parentElement.classList.add("active");

      // 페이지 이동
      window.location.href = href;
    });
  });


  /* ========== 썸네일 클릭 시 상세보기 이동 (임시) ========== */
  const courseCards = document.querySelectorAll(".course-card");
  courseCards.forEach(card => {
    card.addEventListener("click", () => {
      const title = card.querySelector(".title").innerText;
      console.log(`"${title}" 강좌 상세페이지로 이동합니다.`);
    });
  });


  /* ========== 페이지네이션 (임시로 넣은 기능) ========== */
  const paginationButtons = document.querySelectorAll(".pagination button");

  paginationButtons.forEach(button => {
    button.addEventListener("click", () => {
      const page = button.innerText;

      if (page === "<" || page === ">") {
        console.log("페이지 이동 (이전/다음)");
        return;
      }

      paginationButtons.forEach(btn => btn.classList.remove("active"));
      button.classList.add("active");

      console.log(`${page} 페이지로 이동`);
      // 실제 구현 시: (안쓰면 말고)
      // fetch(`/api/courses?page=${page}`)
      //   .then(res => res.json())
      //   .then(data => renderCourses(data));
    });
  });

  // 나중에 fetch로 불러올 때 사용 가능 (필요없으면 삭제,,,)
  // function renderCourses(data) {
  //   const grid = document.querySelector(".course-grid");
  //   grid.innerHTML = "";
  //   data.forEach(course => {
  //     const card = document.createElement("div");
  //     card.classList.add("course-card");
  //     card.innerHTML = `
  //       <div class="thumbnail">${course.thumbnail || "썸네일"}</div>
  //       <p class="title">${course.title}</p>
  //       <span class="category">${course.category}</span>
  //     `;
  //     grid.appendChild(card);
  //   });
  // }
});

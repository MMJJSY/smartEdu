// ====== 공통 알림 ======
function showAlert(message) {
  alert(message);
}

// ====== 질문 등록 페이지 ======
document.addEventListener("DOMContentLoaded", () => {
  const form = document.querySelector("#qnaForm");
  const fileInput = document.querySelector("#fileUpload");
  const fileList = document.querySelector("#fileList");

  // 파일 선택 시 목록 표시
  if (fileInput) {
    fileInput.addEventListener("change", () => {
      fileList.innerHTML = "";
      const files = fileInput.files;

      if (files.length === 0) {
        fileList.innerHTML = "<p class='no-file'>선택된 파일이 없습니다.</p>";
        return;
      }

      const ul = document.createElement("ul");
      [...files].forEach((file, i) => {
        const li = document.createElement("li");
        li.textContent = `${i + 1}. ${file.name}`;
        ul.appendChild(li);
      });
      fileList.appendChild(ul);
    });
  }

  // 폼 제출
  if (form) {
    form.addEventListener("submit", (e) => {
      e.preventDefault();
      showAlert("질문이 등록되었습니다.");
      location.href = "qna-view.html";
    });
  }
});

// ====== 상세보기 페이지 (질문 수정/삭제 + 댓글/답글/수정/삭제) ======
document.addEventListener("click", async (e) => {
  const target = e.target;

  // ========== 질문 수정 ==========
  if (target.classList.contains("modify-btn") && !target.dataset.commentId) {
    const questionId = new URLSearchParams(window.location.search).get("questionId");
    if (!questionId) return showAlert("잘못된 요청입니다.");

    // 수정 페이지로 이동
    location.href = `/question/modifyForm?questionId=${questionId}`;
  }

  // ========== 질문 삭제 ==========
  if (target.classList.contains("delete-btn") && !target.dataset.commentId) {
    const confirmDelete = confirm("정말 이 질문을 삭제하시겠습니까?");
    if (!confirmDelete) return;

    const questionId = new URLSearchParams(window.location.search).get("questionId");
    if (!questionId) return showAlert("잘못된 요청입니다.");

    const res = await fetch(`/question/delete/${questionId}`, { method: "POST" });

    if (res.ok) {
      showAlert("질문이 삭제되었습니다.");
      location.href = "/question/list";
    } else {
      showAlert("질문 삭제에 실패했습니다.");
    }
  }

  // ========== 답글쓰기 토글 ==========
  if (target.classList.contains("reply-toggle")) {
    const box = target.closest(".comment").querySelector(".reply-box");
    box.classList.toggle("hidden");
  }

  // ========== 댓글 등록 ==========
  if (target.classList.contains("comment-submit")) {
    e.preventDefault();
    const textarea = document.getElementById("new-comment");
    const content = textarea.value.trim();
    const questionId = target.dataset.questionId;

    if (!content) return showAlert("댓글을 입력해 주세요.");

    const res = await fetch("/comment/regist", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ questionId, content }),
    });

    if (res.ok) {
      showAlert("댓글이 등록되었습니다.");
      location.reload();
    } else {
      showAlert("댓글 등록에 실패하였습니다.");
    }
  }

  // ========== 답글 등록 ==========
  if (target.classList.contains("reply-submit")) {
    e.preventDefault();
    const textarea = target.previousElementSibling;
    const content = textarea.value.trim();
    const parentId = target.dataset.parentId;
    const questionId = document.querySelector(".comment-submit").dataset.questionId;

    if (!content) return showAlert("답글을 입력해주세요.");

    const res = await fetch("/comment/regist", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ questionId, content, parentId }),
    });

    if (res.ok) {
      showAlert("답글이 등록되었습니다.");
      location.reload();
    } else {
      showAlert("답글 등록에 실패하였습니다.");
    }
  }

  // ========== 댓글 삭제 ==========
  if (target.classList.contains("delete-btn") && target.dataset.commentId) {
    const confirmDelete = confirm("정말 삭제하시겠습니까?");
    if (!confirmDelete) return;

    const commentId = target.dataset.commentId;
    const commentEl = target.closest(".comment, .reply");

    const res = await fetch(`/comment/delete/${commentId}`, { method: "POST" });

    if (res.ok) {
      showAlert("삭제되었습니다.");


      const textEl = commentEl.querySelector(".comment-text");
      textEl.innerHTML = "<em class='deleted-comment'>삭제된 댓글입니다.</em>";


      const actions = commentEl.querySelector(".comment-actions");
      if (actions) actions.remove();
      const replyBox = commentEl.querySelector(".reply-box");
      if (replyBox) replyBox.remove();
    } else {
      showAlert("삭제 실패");
    }
  }
  // ========== 댓글 수정 ==========
  if (target.classList.contains("modify-btn") && target.dataset.commentId) {
    const commentDiv = target.closest(".comment, .reply");
    const textElem = commentDiv.querySelector(".comment-text");
    const oldContent = textElem.textContent.trim();

    // 기존 텍스트 숨기고 textarea 생성
    textElem.style.display = "none";
    const textarea = document.createElement("textarea");
    textarea.classList.add("modify-textarea");
    textarea.value = oldContent;
    commentDiv.insertBefore(textarea, textElem.nextSibling);

    // 저장 버튼 생성
    const saveBtn = document.createElement("button");
    saveBtn.textContent = "저장";
    saveBtn.classList.add("save-edit");
    saveBtn.dataset.commentId = target.dataset.commentId;
    commentDiv.querySelector(".comment-actions").appendChild(saveBtn);

    // 수정 버튼 비활성화
    target.disabled = true;
  }

  // ========== 댓글 수정 저장 ==========
  if (target.classList.contains("save-edit")) {
    const commentId = target.dataset.commentId;
    const commentDiv = target.closest(".comment, .reply");
    const textarea = commentDiv.querySelector(".modify-textarea");
    const newContent = textarea.value.trim();

    if (!newContent) return showAlert("내용을 입력하세요.");

    const res = await fetch("/comment/modify", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ commentId, content: newContent }),
    });

    if (res.ok) {
      showAlert("수정되었습니다.");
      location.reload();
    } else {
      showAlert("수정 실패");
    }
  }
});
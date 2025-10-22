document.addEventListener("DOMContentLoaded", () => {
  const form = document.getElementById("loginForm");
  const loginBtn = document.getElementById("loginBtn");
  const emailEl = document.getElementById("email");
  const passwordEl = document.getElementById("password");

  if (!form || !loginBtn) return;

  const submitHandler = (e) => {
    // 기본 제출 막고 프론트 검증만 수행
    e.preventDefault();

    const selectedRole = document.querySelector('input[name="role"]:checked');
    const email = emailEl.value.trim();
    const password = passwordEl.value.trim();

    if (!selectedRole) {
      alert("수강생 또는 강사를 선택해주세요.");
      return;
    }

    if (!email || !password) {
      alert("이메일과 비밀번호를 모두 입력해주세요.");
      return;
    }

    const emailPattern = /^[^@]+@[^@]+\.[a-zA-Z]{2,}$/;
    if (!emailPattern.test(email)) {
      alert("올바른 이메일 형식이 아닙니다.");
      return;
    }

    //폼 제출
    form.submit();
  };

  // 버튼 클릭과 Enter 제출 둘 다 동일 검증 흐름으로
  loginBtn.addEventListener("click", submitHandler);
  form.addEventListener("submit", submitHandler);
});

document.addEventListener("DOMContentLoaded", () => {
  const toast = document.getElementById("toast");
  if (!toast) return;

  // 타입별 색상
  const type = toast.dataset.type || "error";
  toast.classList.add(type);

  // 표시
  toast.classList.remove("hidden");
  // 초기 Y 살짝 올렸다가 자연스럽게 내려오게 하고 싶다면 아래 한 줄로 대체:
  // toast.style.transform = "translateX(-50%) translateY(-6px)";
  requestAnimationFrame(() => toast.classList.add("show"));

  // 3초 후 자동 숨김
  setTimeout(() => {
    toast.classList.remove("show");
    setTimeout(() => toast.classList.add("hidden"), 250);
  }, 3000);
});
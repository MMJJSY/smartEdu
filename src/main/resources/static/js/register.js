function limitInput(el, maxLength) {
    if (el.value.length > maxLength) {
        el.value = el.value.slice(0, maxLength);
    }
}

document.addEventListener("DOMContentLoaded", () => {
    // ✅ 추가: 이메일 중복확인 상태 플래그
    let isEmailChecked = false;

    // ✅ 학생 또는 강사 폼 가져오기
    const form = document.getElementById("studentForm") || document.getElementById("instructorForm");
    if (!form) return; // 폼이 없으면 실행 안함

    const checkEmailBtn = document.getElementById("checkEmail");
    const emailMsg = document.getElementById("emailMsg");
    const passwordMsg = document.getElementById("passwordMsg");
    const password = document.getElementById("password");
    const passwordCheck = document.getElementById("passwordCheck");
    const pwInfo = document.querySelector(".info");
    const emailInput = document.getElementById("email");

    // ✅ 강사 폼의 첨부파일
    const resumeInput = document.getElementById("resume");
    let resumeMsg = null;

    if (resumeInput) {
        resumeMsg = document.createElement("p");
        resumeMsg.id = "resumeMsg";
        resumeMsg.style.color = "#e63946";
        resumeInput.parentNode.appendChild(resumeMsg);

        // 파일 용량 및 확장자 검사
        resumeInput.addEventListener("change", () => {
            const file = resumeInput.files[0];
            if (!file) return;

            const maxSize = 10 * 1024 * 1024; // 10MB 제한
            const allowedExt = ["pdf", "hwp", "doc", "docx", "xls", "xlsx"];
            const ext = file.name.split(".").pop().toLowerCase();

            if (file.size > maxSize) {
                resumeMsg.textContent = "⚠️ 파일 용량이 너무 큽니다. 최대 10MB까지만 가능합니다.";
                resumeInput.value = "";
                return;
            }

            if (!allowedExt.includes(ext)) {
                resumeMsg.textContent = "⚠️ 허용되지 않는 파일 형식입니다.";
                resumeInput.value = "";
                return;
            }

            resumeMsg.textContent = "✅ 업로드 가능한 파일입니다.";
            resumeMsg.style.color = "green";
        });
    }

    // ✅ 이메일 입력 시 중복확인 초기화
    if (emailInput) {
        emailInput.addEventListener("input", () => {
            isEmailChecked = false;
            const email = emailInput.value.trim();
            const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

            if (!email) {
                emailMsg.textContent = "";
            } else if (!emailPattern.test(email)) {
                emailMsg.textContent = "이메일을 올바른 형식으로 작성해주세요.";
                emailMsg.style.color = "#e63946";
            } else {
                emailMsg.textContent = "";
            }
        });
    }

    // ✅ 이메일 중복확인 (서버 연동)
    if (checkEmailBtn && emailInput) {
        checkEmailBtn.addEventListener("click", async () => {
            const email = emailInput.value.trim();
            const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

            if (!email) {
                emailMsg.textContent = "이메일을 입력해주세요.";
                emailMsg.style.color = "#e63946";
                return;
            }
            if (!emailPattern.test(email)) {
                emailMsg.textContent = "이메일 형식이 올바르지 않습니다.";
                emailMsg.style.color = "#e63946";
                return;
            }

            try {
                const res = await fetch(`/member/emailCheck?email=${encodeURIComponent(email)}`, {
                    method: "GET",
                    headers: { "Accept": "text/plain;charset=UTF-8" }
                });

                const text = (await res.text()).trim(); // '' 또는 'duplicated'

                if (text === "duplicated") {
                    emailMsg.textContent = "이미 사용 중인 이메일입니다.";
                    emailMsg.style.color = "#e63946";
                    isEmailChecked = false;
                } else {
                    emailMsg.textContent = "사용 가능한 이메일입니다.";
                    emailMsg.style.color = "green";
                    isEmailChecked = true;
                }
            } catch (err) {
                emailMsg.textContent = "중복 확인 중 오류가 발생했습니다.";
                emailMsg.style.color = "#e63946";
                isEmailChecked = false;
            }
        });
    }

    // ✅ 비밀번호 유효성 검사
    if (password) {
        password.addEventListener("input", () => {
            const pw = password.value.trim();
            const pwPattern = /^(?=.*[a-zA-Z])(?=.*\d)(?=.*[!@#$%^&*]).{8,}$/;

            if (pw === "") {
                pwInfo.style.color = "#666";
            } else if (!pwPattern.test(pw)) {
                pwInfo.style.color = "#e63946";
            } else {
                pwInfo.style.color = "green";
            }
        });
    }

    // ✅ 비밀번호 일치 확인
    if (passwordCheck) {
        passwordCheck.addEventListener("input", () => {
            if (password.value && passwordCheck.value) {
                if (password.value === passwordCheck.value) {
                    passwordMsg.textContent = "비밀번호가 일치합니다.";
                    passwordMsg.style.color = "green";
                } else {
                    passwordMsg.textContent = "비밀번호가 일치하지 않습니다.";
                    passwordMsg.style.color = "#e63946";
                }
            } else {
                passwordMsg.textContent = "";
            }
        });
    }

    // ✅ 폼 제출
    form.addEventListener("submit", (e) => {
        e.preventDefault();

        // ✅ 이메일 중복확인 여부 검사
        if (!isEmailChecked) {
            alert("이메일 중복 확인을 먼저 해주세요.");
            return;
        }

        const email = document.getElementById("email").value.trim();
        const name = document.getElementById("name").value.trim();
        const pw = password.value.trim();
        const pwCheck = passwordCheck.value.trim();
        const phone1 = document.getElementById("phone1").value.trim();
        const phone2 = document.getElementById("phone2").value.trim();
        const phone3 = document.getElementById("phone3").value.trim();
        const pwPattern = /^(?=.*[a-zA-Z])(?=.*\d)(?=.*[!@#$%^&*]).{8,}$/;

        // ✅ 숫자 입력 길이 제한 적용
        limitInput(document.getElementById("phone1"), 3);
        limitInput(document.getElementById("phone2"), 4);
        limitInput(document.getElementById("phone3"), 4);

        if (!email || !name || !pw || !pwCheck || !phone1 || !phone2 || !phone3) {
            alert("모든 항목을 입력해주세요.");
            return;
        }

        if (!pwPattern.test(pw)) {
            pwInfo.style.color = "#e63946";
            return;
        }

        if (pw !== pwCheck) {
            alert("비밀번호가 일치하지 않습니다.");
            return;
        }

        if (!/^\d{3}$/.test(phone1) || !/^\d{4}$/.test(phone2) || !/^\d{4}$/.test(phone3)) {
            alert("전화번호 형식이 올바르지 않습니다.");
            return;
        }

        if (resumeInput && !resumeInput.value) {
            resumeMsg.textContent = "이력서는 반드시 첨부해야 합니다.";
            return;
        } else if (resumeMsg) {
            resumeMsg.textContent = "";
        }

        const phone = `${phone1}-${phone2}-${phone3}`;

        if (form.id === "instructorForm") {
            alert(`${name}님, 강사 승인 요청이 접수되었습니다.`);
        } else {
            alert(`${name}님, 회원가입이 완료되었습니다.`);
        }

        form.submit();
        form.reset();

        if (pwInfo) pwInfo.style.color = "#666";
        if (passwordMsg) passwordMsg.textContent = "";
        if (resumeMsg) resumeMsg.textContent = "";
    });

    // ✅ 전화번호 입력 시 길이 제한 이벤트 연결
    ["phone1", "phone2", "phone3"].forEach((id, i) => {
        const el = document.getElementById(id);
        if (!el) return;
        const maxLen = i === 0 ? 3 : 4;
        el.addEventListener("input", () => limitInput(el, maxLen));
    });
});
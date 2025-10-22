
        document.addEventListener("DOMContentLoaded", () => {
            const form = document.getElementById("pwdEditForm");
            const newPwd = document.getElementById("newPwd");
            const confirmPwd = document.getElementById("confirmPwd");
            const pwInfo = document.getElementById("pwInfo");
            const pwMsg = document.getElementById("pwMsg");

            const pwPattern = /^(?=.*[a-zA-Z])(?=.*\d)(?=.*[!@#$%^&*]).{8,}$/;

            // ✅ 비밀번호 유효성 검사
            newPwd.addEventListener("input", () => {
                const pw = newPwd.value.trim();
                if (!pw) {
                    pwInfo.style.color = "#666";
                } else if (!pwPattern.test(pw)) {
                    pwInfo.style.color = "#e63946"; // 빨강
                } else {
                    pwInfo.style.color = "green"; // 통과
                }
            });

            // ✅ 비밀번호 일치 확인
            confirmPwd.addEventListener("input", () => {
                if (confirmPwd.value && newPwd.value) {
                    if (confirmPwd.value === newPwd.value) {
                        pwMsg.textContent = "비밀번호가 일치합니다.";
                        pwMsg.style.color = "green";
                    } else {
                        pwMsg.textContent = "비밀번호가 일치하지 않습니다.";
                        pwMsg.style.color = "#e63946";
                    }
                } else {
                    pwMsg.textContent = "";
                }
            });

            // ✅ 제출 시 검증
            form.addEventListener("submit", (e) => {
                const pw = newPwd.value.trim();
                const pwCheck = confirmPwd.value.trim();

                if (!pwPattern.test(pw)) {
                    alert("비밀번호는 영문, 숫자, 특수문자 포함 8자 이상이어야 합니다.");
                    e.preventDefault();
                    return;
                }

                if (pw !== pwCheck) {
                    alert("비밀번호가 일치하지 않습니다.");
                    e.preventDefault();
                    return;
                }
            });
        });

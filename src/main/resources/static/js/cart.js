document.addEventListener("DOMContentLoaded", () => {
  const deleteButtons = document.querySelectorAll(".delete-btn");
  const totalBoxSpan = document.querySelector(".total-price span");
  const checkoutForm = document.getElementById("checkoutForm");

  // ====== 공통 유틸 ======
  const onlyDigits = (text) => (text || "").toString().replace(/[^0-9]/g, "");
  const parsePrice = (text) => {
    const n = onlyDigits(text);
    return n.length ? parseInt(n, 10) : 0;
  };

  // 합리적 상한 (원)
  const MAX_PRICE = 10_000_000;

  const isValidPriceNumber = (value) => {
    const num = typeof value === "number" ? value : parsePrice(value);
    return Number.isFinite(num) && Number.isInteger(num) && num >= 0 && num <= MAX_PRICE;
  };

  const formatKRW = (n) => Number(n || 0).toLocaleString("ko-KR") + "원";

  // ====== 총합 계산 (표시용) ======
  const updateTotal = () => {
    if (!totalBoxSpan) return;
    const prices = document.querySelectorAll(".cart-item .price");
    let total = 0;
    prices.forEach(p => total += parsePrice(p.textContent));
    totalBoxSpan.textContent = formatKRW(total);
  };

  // ====== 삭제 (폼 submit 기반이면 confirm만) ======
  deleteButtons.forEach(btn => {
    btn.addEventListener("click", (e) => {
      // 미구현 버튼(예제용) 막기
      if (btn.disabled) return;
      const ok = confirm("이 강좌를 찜목록에서 삭제하시겠습니까?");
      if (!ok) e.preventDefault();
      // 폼 submit이면 브라우저 기본 동작이 수행됨
    });
  });

  // ====== 전체 결제: 제출 전에 프론트 유효성 체크만 수행 ======
  if (checkoutForm) {
    checkoutForm.addEventListener("submit", (e) => {
      const items = document.querySelectorAll(".cart-item");
      for (const item of items) {
        const priceEl = item.querySelector(".price");
        const priceText = priceEl ? priceEl.textContent : "0";
        const valid = isValidPriceNumber(priceText);
        if (!valid) {
          e.preventDefault();
          alert(
            "가격 형식이 올바르지 않은 항목이 있습니다.\n(0 이상 정수, 최대 " +
            MAX_PRICE.toLocaleString("ko-KR") + "원)"
          );
          return;
        }
      }
      // ✅ 통과 시 서버로 제출 → 서버에서 flash(msg, msgType) 세팅 → 카트로 redirect → 여기서만 알림 표시
    });
  }

  updateTotal();

  // cart.js (맨 아래쪽에 추가)

  // ====== 결제 성공 후 이동 선택 알림 ======
  (function () {
    try {
      const params = new URLSearchParams(window.location.search);
      const paid = params.get("paid");
      if (paid === "1") {
        // URL 정리: 뒤로가기/새로고침 때 중복 표시 방지
        const url = new URL(window.location.href);
        url.searchParams.delete("paid");
        window.history.replaceState({}, "", url.toString());

        // 안내 + 이동 선택
        const go = confirm("결제가 완료되었습니다.\n결제 내역 페이지로 이동하시겠습니까?");
        if (go) {
          window.location.href = "/payment";
        }
        // 아니오 선택 시: 아무 것도 하지 않고 카트 페이지에 머무름
      }
    } catch (_) { /* no-op */ }
  })();

});

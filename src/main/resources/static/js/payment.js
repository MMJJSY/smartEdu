(function () {
  // yyyy-MM-dd → Date(UTC 00:00)
  function parseYmdToDate(ymd) {
    if (!ymd || typeof ymd !== 'string') return null;
    const [y, m, d] = ymd.trim().split('-').map(Number);
    if (!y || !m || !d) return null;
    return new Date(Date.UTC(y, m - 1, d, 0, 0, 0));
  }

  // 두 날짜 차이(일수, UTC 자정 기준)
  function diffDaysUTC(fromDate, toDate) {
    const MS = 24 * 60 * 60 * 1000;
    const fromUTC = Date.UTC(fromDate.getUTCFullYear(), fromDate.getUTCMonth(), fromDate.getUTCDate());
    const toUTC = Date.UTC(toDate.getUTCFullYear(), toDate.getUTCMonth(), toDate.getUTCDate());
    return Math.floor((toUTC - fromUTC) / MS);
  }

  function hideForm(form, reasonText) {
    if (!form) return;
    form.style.display = 'none';
    form.setAttribute('aria-hidden', 'true');
    const btn = form.querySelector('button');
    if (btn) {
      btn.setAttribute('disabled', 'true');
      btn.style.cursor = 'not-allowed';
      btn.title = reasonText || '환불 불가';
    }
  }

  function showForm(form) {
    if (!form) return;
    form.style.display = '';
    form.setAttribute('aria-hidden', 'false');
    const btn = form.querySelector('button');
    if (btn) {
      btn.removeAttribute('disabled');
      btn.style.cursor = '';
      btn.title = '';
    }
  }

  document.addEventListener('DOMContentLoaded', function () {
    // 환불 클릭 확인(폼 submit 전 confirm)
    document.body.addEventListener('click', function (e) {
      const btn = e.target.closest('.refund-btn');
      if (!btn) return;
      if (!confirm('해당 강좌를 정말 환불하시겠습니까?')) {
        e.preventDefault();
      }
    });

    const items = document.querySelectorAll('.payment-list .payment-item');

    const now = new Date();
    const todayUTC = new Date(Date.UTC(now.getUTCFullYear(), now.getUTCMonth(), now.getUTCDate()));

    items.forEach((item) => {
      // 서버가 NORMAL 아닐 때는 폼 자체가 렌더되지 않음
      const refundForm = item.querySelector('form[action*="/payment/refund"]');
      if (!refundForm) return; // 폼이 없으면 스킵

      // 결제일: data-created 우선, 없으면 .date 텍스트
      const createdAttr = item.getAttribute('data-created') || '';
      const dateText = createdAttr || (item.querySelector('.date')?.textContent || '');
      const createdAt = parseYmdToDate(dateText);

      // 기본: 표시
      showForm(refundForm);

      // 날짜가 유효하면 14일 초과 여부만 체크
      if (createdAt) {
        const passedDays = diffDaysUTC(createdAt, todayUTC);
        const within14Days = passedDays >= 0 && passedDays <= 14;

        if (!within14Days) {
          hideForm(refundForm, '환불 불가: 결제 후 14일 초과');
        }
      }
      // 날짜 파싱 실패 시에는 숨기지 않고 그대로 표시(서버 검증에 맡김)
    });
  });
})();

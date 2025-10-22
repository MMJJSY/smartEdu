  // 드롭다운 열기/닫기
  const select = document.querySelector('.mycourse .mc-select');
  const trigger = select?.querySelector('.mc-select-trigger');
  const menu = select?.querySelector('.mc-select-menu');
  const valueSpan = select?.querySelector('.mc-select-value');

  if (trigger && menu && valueSpan) {
    const toggle = (open) => {
      select.dataset.open = open ? "true" : "false";
      trigger.setAttribute('aria-expanded', open ? 'true' : 'false');
    };

    trigger.addEventListener('click', (e) => {
      e.stopPropagation();
      toggle(select.dataset.open !== "true");
    });

    menu.querySelectorAll('li').forEach(li => {
      li.addEventListener('click', (e) => {
        // 선택 표시 변경
        menu.querySelectorAll('li[aria-selected="true"]').forEach(n => n.setAttribute('aria-selected','false'));
        li.setAttribute('aria-selected','true');
        valueSpan.textContent = li.dataset.value || li.textContent.trim();
        toggle(false);

        // TODO: 필터 로직(선택값에 맞춰 리스트 필터링) — 나중에 DB/JS 연동
      });
    });

    // 외부 클릭 시 닫기
    document.addEventListener('click', () => toggle(false));
  }
  
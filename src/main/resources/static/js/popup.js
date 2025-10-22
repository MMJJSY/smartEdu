// 팝업 열기 유틸(이미 있으면 그대로 사용)
const POPUP_W = 960;
const POPUP_H = 540;

// 팝업 열기 함수
function openPlayerPopup(url, w = 960, h = 540) {
  const dualLeft = window.screenLeft ?? screen.left;
  const dualTop = window.screenTop ?? screen.top;
  const width = window.innerWidth || document.documentElement.clientWidth || screen.width;
  const height = window.innerHeight || document.documentElement.clientHeight || screen.height;
  const left = dualLeft + (width - w) / 2;
  const top = dualTop + (height - h) / 2;

  const features = [
    'toolbar=no', 'location=no', 'status=no', 'menubar=no',
    'scrollbars=yes', 'resizable=yes',
    `width=${w}`, `height=${h}`, `left=${left}`, `top=${top}`
  ].join(',');

  const win = window.open(url, 'SmartEduPlayer', features);
  if (!win) {
    // 팝업 차단 시 현재 탭 이동
    window.location.href = url;
  } else {
    win.focus();
  }
}

// ▶ 버튼(버튼 버전) 가로채기
document.addEventListener('DOMContentLoaded', () => {
  document.querySelectorAll('.play-btn').forEach(btn => {
    btn.addEventListener('click', () => {
      const src = btn.dataset.src;                 // 예: videos/html1.mp4
      const title = btn.dataset.title || '강의';
      const ep = btn.dataset.ep;

      // player.html로 이동할 URL 생성 (쿼리 파라미터 인코딩)
      const url = new URL('player.html', location.href);
      url.searchParams.set('src', src);
      url.searchParams.set('title', title);
      if (ep) url.searchParams.set('ep', ep);

      openPlayerPopup(url.toString());
    });
  });
});
// 쿼리 파라미터 파싱
const params = new URLSearchParams(location.search);
const src = params.get('src');
const title = params.get('title') || '강의';
const epStr = params.get('ep');
const ep = epStr ? parseInt(epStr, 10) : null;

// 요소
const video = document.getElementById('playerVideo');
const source = document.getElementById('playerSource');
const titleEl = document.getElementById('playerTitle');
const prev = document.getElementById('prevLink') || null;
const next = document.getElementById('nextLink') || null;
const closeBtn = document.getElementById('closeWin');

// 로드 (자동재생 X)
if (src) {
  source.src = src;
  video.load();
}

// 이전/다음 링크(간단 규칙: 파일명에 숫자 포함 가정)
function buildLink(baseSrc, targetEp, labelEl) {
  if (!labelEl) return; // 요소가 없으면 아무 것도 하지 않음
  if (!baseSrc || !ep) { labelEl.style.display = 'none'; return; }
  if (targetEp < 1) { labelEl.style.visibility = 'hidden'; return; }
  const match = baseSrc.match(/^(.*?)(\d+)(\.\w+)$/);
  if (!match) { labelEl.style.display = 'none'; return; }
  const [, prefix, , suffix] = match;
  const newSrc = `${prefix}${targetEp}${suffix}`;
  const url = new URL('player.html', location.href);
  url.searchParams.set('src', newSrc);
  url.searchParams.set('title', title);
  url.searchParams.set('ep', targetEp);
  labelEl.href = url.toString();
}
if (ep) {
  buildLink(src, ep - 1, prev);
  buildLink(src, ep + 1, next);
} else {
  if (prev) prev.style.display = 'none';
  if (next) next.style.display = 'none';
}

// 팝업에서 열렸는지 체크해서 닫기 처리
function inPopup() { return !!window.opener; }
if (!inPopup()) { closeBtn.style.display = 'none'; } // 새 탭일 때 닫기 버튼 숨김
closeBtn.addEventListener('click', () => {
  if (inPopup()) window.close(); else history.back();
});
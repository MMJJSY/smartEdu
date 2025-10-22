document.addEventListener('DOMContentLoaded', () => {
  console.log('관리자 대시보드 로드 완료');

  // 테이블 행 hover 효과
  const rows = document.querySelectorAll('.approval-table tbody tr');
  rows.forEach(row => {
    row.addEventListener('mouseenter', () => row.style.backgroundColor = '#f4f9ff');
    row.addEventListener('mouseleave', () => row.style.backgroundColor = 'white');
  });
});

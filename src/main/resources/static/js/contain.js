// js/course.js
document.getElementById("wishlistBtn").addEventListener("click", () => {
  alert("장바구니에 담겼습니다");

  // 나중에 DB 연동 시 아래 코드로 API 호출
  // fetch("/api/cart", {
  //   method: "POST",
  //   headers: { "Content-Type": "application/json" },
  //   body: JSON.stringify({ courseId: 1 })
  // })
  // .then(res => res.json())
  // .then(data => console.log("추가 완료:", data));
});
document.addEventListener("DOMContentLoaded", () => {
    const slides = document.querySelectorAll(".banner-slide");
    const banner = document.querySelector(".banner");
    let currentIndex = 0;
    const slideCount = slides.length;
    const intervalTime = 3000; // 3초마다 전환

    // 점 네비게이션 생성
    const dotsContainer = document.createElement("div");
    dotsContainer.classList.add("dots");
    slides.forEach((_, i) => {
        const dot = document.createElement("span");
        dot.classList.add("dot");
        if (i === 0) dot.classList.add("active");
        dot.addEventListener("click", () => goToSlide(i));
        dotsContainer.appendChild(dot);
    });
    banner.appendChild(dotsContainer);

    const dots = document.querySelectorAll(".dot");

    function goToSlide(index) {
        slides.forEach((slide, i) => {
            slide.style.transform = `translateX(${(i - index) * 100}%)`;
        });
        dots.forEach(dot => dot.classList.remove("active"));
        dots[index].classList.add("active");
        currentIndex = index;
    }

    function nextSlide() {
        const nextIndex = (currentIndex + 1) % slideCount;
        goToSlide(nextIndex);
    }

    // 초기 설정
    slides.forEach((slide, i) => {
        slide.style.transform = `translateX(${i * 100}%)`;
    });

    let autoSlide = setInterval(nextSlide, intervalTime);

    // 마우스 올리면 멈추고, 벗어나면 다시 시작
    banner.addEventListener("mouseenter", () => clearInterval(autoSlide));
    banner.addEventListener("mouseleave", () => autoSlide = setInterval(nextSlide, intervalTime));
});

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!-- header.jsp -->
<%@ include file="/WEB-INF/view/layout/header.jsp" %>
<!-- AOS CSS 추가 -->
<link href="https://cdn.jsdelivr.net/npm/aos@2.3.4/dist/aos.css" rel="stylesheet">
<!-- Font Awesome (아이콘용) -->
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">

<main class="main-container">
    <!-- 비디오 배경 섹션 -->
    <section class="video-section">
        <video autoplay loop muted playsinline class="background-video" preload="auto">
            <source src="/videos/main.mp4" type="video/mp4">
            Your browser does not support the video tag.
        </video>
        <div class="video-overlay">
            <div class="overlay-content" data-aos="fade-up">
                <h1>당신의 가게를 관리합니다</h1>
                <p id="animated-text">똑똑하게 가게를 관리해보세요!</p>
                <a href="/payment" class="btn btn-primary">UNI-ERP 서비스 시작하기</a>
            </div>
            <div class="explanation-container" data-aos="fade-right" data-aos-delay="300">
                <div class="explanation-content">
                    <h2>어떤 서비스가 있나요?</h2>
                    <p>UNI-ERP의 시스템은 소상공인 여러분을 위한 맞춤형 솔루션입니다.</p>
                    <p>매출 관리부터 상품, 재고, 인사까지 하나의 시스템으로 통합하여 운영을 간소화하고 시간과 비용을 절감할 수 있도록 돕습니다.</p>
                    <p>사업 운영이 어려우신가요? 간편하게 데이터를 관리하고 분석할 수 있는 ERP 시스템을 통해 비즈니스의 효율성을 높이고 새로운 성장 기회를 발견해 보세요.</p>
                </div>
            </div>
        </div>
    </section>

    <!-- 소개 섹션 -->
    <section class="introduce-section" data-aos="zoom-in">
        <h1>소상공인 대상 ERP 시스템</h1>
        <p>UNI-ERP는 기업의 효율적 운영과 성장을 지원하는 ERP(Enterprise Resource Planning) 시스템을 제공합니다.</p>
        <p>저희의 ERP 솔루션은 모든 비즈니스 프로세스를 통합하여 매출, 상품, 재고, 인사 등 다양한 업무를 하나의 시스템에서 관리할 수 있도록 도와줍니다.</p>
        <p>이를 통해 실시간으로 데이터를 공유하고, 빠르고 정확한 의사결정을 내릴 수 있는 환경을 제공합니다.</p>
    </section>

    <!-- 서비스 기능 섹션 -->
    <section class="services-section">
        <div class="services-container">
            <div class="service-card" data-aos="fade-up" data-aos-delay="200">
                <i class="fas fa-chart-line service-icon"></i>
                <h3>정확한 매출관리</h3>
                <p>판매 데이터를 자동으로 집계하고 분석하여, 매출 흐름을 한눈에 파악하고 전략적인 결정을 지원합니다.</p>
            </div>
            <div class="service-card" data-aos="fade-up" data-aos-delay="400">
                <i class="fas fa-tag service-icon"></i>
                <h3>체계적인 상품관리</h3>
                <p>제품의 등록과 분류를 간편하게 처리하여, 상품 관리를 더욱 효율적으로 수행할 수 있습니다.</p>
            </div>
            <div class="service-card" data-aos="fade-up" data-aos-delay="600">
                <i class="fas fa-box service-icon"></i>
                <h3>실시간 재고관리</h3>
                <p>재고 상태를 실시간으로 추적하여 부족한 재고를 자동 알림으로 확인할 수 있어, 재고 손실을 방지하고 원활한 운영을 유지할 수 있습니다.</p>
            </div>
            <div class="service-card" data-aos="fade-up" data-aos-delay="800">
                <i class="fas fa-users-cog service-icon"></i>
                <h3>직관적인 인사관리</h3>
                <p>직원 정보를 쉽고 빠르게 관리할 수 있어, 인사 업무의 효율을 높이고 팀 운영을 효과적으로 지원합니다.</p>
            </div>
            <div class="service-card" data-aos="fade-up" data-aos-delay="1000">
                <i class="fas fa-cash-register service-icon"></i>
                <h3>간편한 판매 및 결제 처리</h3>
                <p>POS 기능을 통해 매장에서의 판매 및 결제를 원활하게 처리할 수 있으며, 다양한 결제 수단을 지원하여 고객 편의성을 높입니다.</p>
            </div>
            <div class="service-card" data-aos="fade-up" data-aos-delay="1200">
                <i class="fas fa-chart-pie service-icon"></i>
                <h3>통계 및 현황자료 출력 기능</h3>
                <p>주요 데이터를 시각화하여, 매출, 재고, 인사 현황자료를 간편하게 생성하고 분석할 수 있습니다.</p>
            </div>
        </div>
    </section>

    <!-- 상담문의 섹션 -->
    <section class="contact-section" data-aos="fade-up">
        <h1>상담문의</h1>
        <div class="contact-container">
            <iframe src="https://www.google.com/maps/embed?pb=!1m18!1m12!1m3!1d3261.8798645106212!2d129.0576023117345!3d35.15961675833722!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x3568ebf31af65223%3A0xb3969a3821eb778e!2z67KU7Zal67mM65Sp!5e0!3m2!1sko!2skr!4v1730087022414!5m2!1sko!2skr" allowfullscreen="" loading="lazy" referrerpolicy="no-referrer-when-downgrade" class="google-map" data-aos="zoom-in" data-aos-delay="200"></iframe>
            <div class="contact-details">
                <div class="contact-info" data-aos="fade-right" data-aos-delay="400">
                    <h2>오시는 길</h2>
                    <p>부산광역시 부산진구 중앙대로 749</p>
                    <p>
                        <strong>Email:</strong> yena@abc.com<br>
                        <strong>Tel:</strong> 12-345-6789<br>
                        <strong>Fax:</strong> 12-345-6789
                    </p>
                </div>
                <div class="contact-form" data-aos="fade-left" data-aos-delay="600">
                    <h2>상담문의 하기</h2>
                    <a href="/support" class="btn btn-secondary">클릭&nbsp;<i class="fas fa-hand-point-up"></i></a>
                </div>
            </div>
        </div>
    </section>
</main>

<!-- AOS 라이브러리 초기화 및 기타 스크립트 -->
<!-- AOS JS 추가 -->
<script src="https://cdn.jsdelivr.net/npm/aos@2.3.4/dist/aos.js"></script>
<script>
    AOS.init({
        duration: 1000, // 애니메이션 지속 시간 (ms)
        once: true,     // 한 번만 애니메이션 실행
    });
</script>
<script>
    document.addEventListener("DOMContentLoaded", function () {

        // 오버레이 애니메이션 제어
        var overlay = document.querySelector('.video-overlay');
        var overlayContent = document.querySelector('.overlay-content');

        // 페이지 로드 시 오버레이 애니메이션 실행
        overlayContent.classList.add('visible');

        // TypeIt 애니메이션 (필요 시 유지 또는 제거)
        /*
        ['#explanation1', '#explanation2', '#explanation3', '#explanation4'].forEach(selector => {
            new TypeIt(selector, {
                speed: 40
            })
                .pause(50)
                .go();
        });
        */

        // 메인 소개 섹션 텍스트 애니메이션 (필요 시 유지 또는 제거)
        /*
        var introduceContainer = document.querySelector('.introduce-section');
        var textElements = introduceContainer.querySelectorAll('h1, p');

        textElements.forEach(function(element) {
            var text = element.textContent;
            element.innerHTML = '';

            // 각 글자를 span으로 감싸기
            for (var i = 0; i < text.length; i++) {
                var span = document.createElement('span');
                span.textContent = text[i];
                var randomDelay = Math.random() * 0.7;
                span.style.animationDelay = randomDelay.toFixed(2) + 's';
                element.appendChild(span);
            }
        });
        */

        // AOS 초기화는 CSS 파일에서 이미 포함되었으므로 추가 설정이 필요 없음
    });
</script>

<!-- footer.jsp -->
<%@ include file="/WEB-INF/view/layout/footer.jsp" %>

<%--
  Created by IntelliJ IDEA.
  User: namch
  Date: 24. 10. 10.
  Time: 오전 10:20
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!-- header.jsp -->
<%@ include file="/WEB-INF/view/layout/header.jsp" %>

<main class="main-container">
    <div class="video-container">
        <video autoplay loop muted width="100%">
            <source src="/videos/main.mp4">
        </video>
        <div class="overlay-content">
            <h1>당신의 가게를 관리합니다</h1>
            <p id="animated-text">똑똑하게 가게를 관리해보세요 !</p>
            <!-- 추가 버튼이나 링크를 여기에 배치할 수 있습니다 -->
            <a href="/payment" class="overlay-content-btn">UNI-ERP 서비스 시작하기</a>
        </div>
        <div class="explanation-container-right">
            <div class="explanation-container">
                <h2>어떤 서비스가 있나요?</h2>
                <h3>UNI-ERP의 시스템은 소상공인 여러분을 위한 맞춤형 솔루션입니다. 매출 관리부터 상품, 재고, 인사까지 하나의 시스템으로 통합하여 운영을 간소화하고 시간과 비용을 절감할 수 있도록 돕습니다. 사업 운영이 어려우신가요? 간편하게 데이터를 관리하고 분석할 수 있는 ERP 시스템을 통해 비즈니스의 효율성을 높이고 새로운 성장 기회를 발견해 보세요.</h3>
            </div>
        </div>
    </div>
    <div class="main-introduce-container">
        <h1>소상공인 대상 ERP 시스템</h1>
        <p>UNI-ERP는 기업의 효율적 운영과 성장을 지원하는 ERP(Enterprise Resource Planning) 시스템을 제공합니다.
            저희의 ERP 솔루션은 모든 비즈니스 프로세스를 통합하여 재무, 생산, 재고, 인사 등 다양한 업무를 하나의 시스템에서 관리할 수 있도록 도와줍니다.
            이를 통해 실시간으로 데이터를 공유하고, 빠르고 정확한 의사결정을 내릴 수 있는 환경을 제공합니다.
        </p>
    </div>
    <div class="main-section-container">
        <div class="main-section-line-container">
            <div class="section-content-box" style="border: none">
                <i class="fas fa-chart-line" title="매출 관리"></i>
                <h3>정확한 매출관리</h3>
                <p>판매 데이터를 자동으로 집계하고 분석하여, 매출 흐름을 한눈에 파악하고 전략적인 결정을 지원합니다.
                </p>
            </div>
            <div class="section-content-box">
                <i class="fas fa-tag"></i>
                <h3>체계적인 상품관리</h3>
                <p>제품의 등록과 분류를 간편하게 처리하여, 상품 관리를 더욱 효율적으로 수행할 수 있습니다.
                </p>
            </div>
            <div class="section-content-box">
                <i class="fas fa-box"></i>
                <h3>실시간 재고관리</h3>
                <p>재고 상태를 실시간으로 추적하여 부족한 재고를 자동 알림으로 확인할 수 있어, 재고 손실을 방지하고 원활한 운영을 유지할 수 있습니다.
                </p>
            </div>
        </div>
        <div class="main-section-line-container">
            <div class="section-content-box" style="border: none">
                <i class="fas fa-users-cog" title="인사 관리"></i>
                <h3>직관적인 인사관리</h3>
                <p>직원 정보를 쉽고 빠르게 관리할 수 있어, 인사 업무의 효율을 높이고 팀 운영을 효과적으로 지원합니다.
                </p>
            </div>
            <div class="section-content-box">
                <i class="fas fa-cash-register" title="POS"></i>
                <h3>간편한 판매 및 결제 처리</h3>
                <p>POS 기능을 통해 매장에서의 판매 및 결제를 원활하게 처리할 수 있으며, 다양한 결제 수단을 지원하여 고객 편의성을 높입니다.</p>
            </div>
            <div class="section-content-box">
                <i class="fas fa-chart-pie" title="통계 및 현황 자료 출력"></i>
                <h3>통계 및 현황자료 출력 기능</h3>
                <p>주요 데이터를 시각화하여, 매출, 재고, 인사 현황자료를 간편하게 생성하고 분석할 수 있습니다.</p>
            </div>
        </div>
    </div>
</main>
<div class="information-box">
    <h1>상담문의</h1>
    <iframe src="https://www.google.com/maps/embed?pb=!1m18!1m12!1m3!1d3261.8798645106212!2d129.0576023117345!3d35.15961675833722!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x3568ebf31af65223%3A0xb3969a3821eb778e!2z67KU7Zal67mM65Sp!5e0!3m2!1sko!2skr!4v1730087022414!5m2!1sko!2skr"
            allowfullscreen="" loading="lazy" referrerpolicy="no-referrer-when-downgrade" class="google-map"></iframe>
    <div class="information-content">
        <div class="information-text-box">
            <h2>오시는 길</h2>
            <p>부산광역시 부산진구 중앙대로 749</p>
            <br>
            <p>
                email : yena@abc.com<br>
                Tel : 12-345-6789<br>
                Fax : 12-345-6789
            </p>
            <h2>채용 문의</h2>
            <p>
                UNI-ERP에서 함께 일하고 싶은 분은<br>
                'yena@abc.com'로 연락주세요!<br>
                언제든지 환영합니다! (최예나 팬만)
            </p>
        </div>
        <div class="information-input-box">
            <p>
                UNI-ERP는 아무튼 어떤 서비스를 제공합니다<br>
                그냥 이용해주세요<br>
                이건 부탁이 아니라, 명령 입니다.
            </p>
            <br>
            <h2>상담문의 하기</h2>
            <a href="/support">!클릭!</a>
        </div>
    </div>
</div>


<script>
    document.addEventListener("DOMContentLoaded", function () {
        var overlay = document.querySelector('.overlay-content');
        var h1 = overlay.querySelector('h1');
        var animatedText = document.getElementById('animated-text');
        var animationRun = false; // 애니메이션 실행 여부를 추적하는 플래그

        window.addEventListener('scroll', function () {
            var currentScrollTop = window.scrollY || document.documentElement.scrollTop;

            if (!animationRun && currentScrollTop > 100) {
                overlay.classList.add('visible');
                h1.classList.add('animate-slideIn');
                animatedText.classList.add('animate-fadeIn'); // animated-text에 애니메이션 클래스 추가
                animationRun = true; // 플래그를 true로 설정하여 애니메이션이 다시 실행되지 않도록 함
            }
        });

        // 애니메이션이 끝난 후 클래스 제거를 원하지 않으면 아래 코드를 제거하거나 주석 처리
        /*
        h1.addEventListener('animationend', function () {
            h1.classList.remove('animate-slideIn');
        });

        animatedText.addEventListener('animationend', function () {
            animatedText.classList.remove('animate-fadeIn');
        });
        */
    });
</script>

<!-- footer.jsp -->
<%@ include file="/WEB-INF/view/layout/footer.jsp" %>
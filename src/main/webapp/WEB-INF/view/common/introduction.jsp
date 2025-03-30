<%--
  Created by IntelliJ IDEA.
  User: namch
  Date: 24. 10. 10.
  Time: 오후 7:53
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/view/layout/header.jsp"%>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/common/introduction.css">
<main class="main-container">
    <section class="introduce-container">
        <div class="introduce-content-img-box">
            <div class="image-box">
               <img src="${pageContext.request.contextPath}/images/introduce/pos.jpg" alt="소개 이미지" class="hidden fadeInLeft">
            </div>
        </div>
        <div class="introduce-content-text-box">
            <div class="introduce-text">
                <h1 class="introduce-text-title">정확한 상품, 재고 관리</h1>
                <p class="introduce-text-content">
                    실시간 재고 관리를 통해 부족한 상품의 재고를 알려주고 상품의 품절 상황을 줄입니다. 사용자가 원하는 상품을 항상 준비해 둘 수 있습니다.
                </p>
            </div>
        </div>
    </section>
    <section class="introduce-container">
        <div class="introduce-content-text-box">
            <div class="introduce-text">
                <h1 class="introduce-text-title">한눈에 볼수 있는 그래프</h1>
                <p class="introduce-text-content">
                    가게별로 년, 월, 일 비교를 통해 매출현황을 파악할 수 있고 상품별 판매 비율 등을 확인할 수 있습니다.
                </p>
            </div>
        </div>
        <div class="introduce-content-img-box">
            <div class="image-box">
                <img src="${pageContext.request.contextPath}/images/introduce/graph.jpg" alt="소개 이미지" class="hidden fadeInRight">
            </div>
        </div>
    </section>
    <section class="introduce-container">
        <div class="introduce-content-img-box">
            <div class="image-box">
                <img src="${pageContext.request.contextPath}/images/introduce/promise.jpg" alt="소개 이미지" class="hidden fadeInLeft">
            </div>
        </div>
        <div class="introduce-content-text-box">
            <div class="introduce-text">
                <h1 class="introduce-text-title">신뢰할 수 있는 서비스</h1>
                <p class="introduce-text-content">
                    분석, 결제, 직원관리 등 모든 과정이 체계적으로 관리되어 실수나 누락이 줄어듭니다. 이를 통해 사용자는 안심하고 믿을 수 있는 서비스를 이용할 수 있습니다.
                </p>
            </div>
        </div>
    </section>
    <section class="introduce-container">
        <div class="introduce-content-text-box">
            <div class="introduce-text">
                <h1 class="introduce-text-title">더 빠른 고객 지원</h1>
                <p class="introduce-text-content">
                    고객 문의에 사용자의 요청 사항이 모두 기록되어 있어, 문의나 문제 발생 시 더 빠르고 정확하게 도움을 드릴 수 있습니다. 사용자에게 필요한 정보를 신속하게 제공해 드립니다.
                </p>
            </div>
        </div>
        <div class="introduce-content-img-box">
            <div class="image-box">
                <img src="${pageContext.request.contextPath}/images/introduce/customer-support.jpg" alt="소개 이미지" class="hidden fadeInRight">
            </div>
        </div>
    </section>

</main>

<!-- footer.jsp 또는 별도의 JavaScript 파일에 추가 -->
<script>
    document.addEventListener('DOMContentLoaded', function () {
        const faders = document.querySelectorAll('.fadeInLeft, .fadeInRight');

        const options = {
            root: null, // 뷰포트
            rootMargin: '0px',
            threshold: 0.5 // 요소의 30%가 보이면 콜백 실행
        };

        const appearOnScroll = new IntersectionObserver(function(entries, appearOnScroll) {
            entries.forEach(entry => {
                if (entry.isIntersecting) {
                    entry.target.classList.add('show');
                    appearOnScroll.unobserve(entry.target); // 애니메이션이 한 번만 실행되도록 관찰 중단
                }
            });
        }, options);

        faders.forEach(fader => {
            appearOnScroll.observe(fader);
        });
    });
</script>

<%@include file="/WEB-INF/view/layout/footer.jsp"%>

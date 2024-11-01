<%--
  Created by IntelliJ IDEA.
  User: namch
  Date: 24. 10. 10.
  Time: 오후 7:53
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/view/layout/header.jsp"%>
<link rel="stylesheet" href="/css/common/introduction.css">
<main class="main-container">
    <section class="introduce-container">
        <div class="introduce-content-img-box">
            <div class="image-box">
               <img src="/images/introduce/pos.jpg" alt="소개 이미지" class="hidden fadeInLeft">
            </div>
        </div>
        <div class="introduce-content-text-box">
            <div class="introduce-text">
                <h1>빠릅니다.</h1>
                <p>
                    내용이 들어갈 자리 최예나 귀엽다<br>
                    내용이 들어갈 자리 최예나 예쁘다<br>
                    내용이 들어갈 자리 최예나 멋지다<br>
                </p>
            </div>
        </div>
    </section>
    <section class="introduce-container">
        <div class="introduce-content-text-box">
            <div class="introduce-text">
                <h1>빠릅니다.</h1>
                <p>
                    내용이 들어갈 자리 최예나 귀엽다<br>
                    내용이 들어갈 자리 최예나 예쁘다<br>
                    내용이 들어갈 자리 최예나 멋지다<br>
                </p>
            </div>
        </div>
        <div class="introduce-content-img-box">
            <div class="image-box">
                <img src="/images/introduce/contract.jpg" alt="소개 이미지" class="hidden fadeInRight">
            </div>
        </div>
    </section>
    <section class="introduce-container">
        <div class="introduce-content-img-box">
            <div class="image-box">
                <img src="/images/introduce/promise.jpg" alt="소개 이미지" class="hidden fadeInLeft">
            </div>
        </div>
        <div class="introduce-content-text-box">
            <div class="introduce-text">
                <h1>빠릅니다.</h1>
                <p>
                    내용이 들어갈 자리 최예나 귀엽다<br>
                    내용이 들어갈 자리 최예나 예쁘다<br>
                    내용이 들어갈 자리 최예나 멋지다<br>
                </p>
            </div>
        </div>
    </section>
    <section class="introduce-container">
        <div class="introduce-content-text-box">
            <div class="introduce-text">
                <h1>빠릅니다.</h1>
                <p>
                    내용이 들어갈 자리 최예나 귀엽다<br>
                    내용이 들어갈 자리 최예나 예쁘다<br>
                    내용이 들어갈 자리 최예나 멋지다<br>
                </p>
            </div>
        </div>
        <div class="introduce-content-img-box">
            <div class="image-box">
                <img src="/images/introduce/promise.jpg" alt="소개 이미지" class="hidden fadeInRight">
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

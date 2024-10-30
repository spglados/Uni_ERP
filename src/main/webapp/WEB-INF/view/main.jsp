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
                <h3>UNI-ERP는 매출, 상품, 재고, 인사를 관리하는 서비스를 제공해드리고 있습니다. 이하 설명 이하 설명 이하 설명 이하 설명 이하 설명 이하 설명 이하 설명 이하 설명 이하
                    설명 이하 설명 이하 설명 이하 설명</h3>
            </div>
        </div>
    </div>
    <div class="main-introduce-container">
        <p>회사 소개</p>
        <h1>소상공인 대상 ERP 시스템</h1>
        <p>UNI-ERP는 소상공인 대상 어쩌고 저쩌고 최예나 네모네모 많이 사랑해주세요<br>
            UNI-ERP는 소상공인 대상 어쩌고 저쩌고 최예나 스마일리 많이 사랑해주세요<br>
            UNI-ERP는 소상공인 대상 어쩌고 저쩌고 최예나 굿모닝 많이 사랑해주세요<br>
        </p>
    </div>
    <div class="main-section-container">
        <div class="main-section-line-container">
            <div class="section-content-box" style="border: none">
                <img src="/images/icon/graph.png" alt="그래프" class="icon">
                <h3>매출관리</h3>
                <p>매출관리설명. 아무튼 이러이러한 방식으로 손실을 줄이고 한눈에 보기쉽게 어쩌고 저쩌고 예나는 세계최고야</p>
            </div>
            <div class="section-content-box">
                <img src="/images/icon/graph.png" alt="그래프" class="icon">
                <h3>매출관리</h3>
                <p>매출관리설명. 아무튼 이러이러한 방식으로 손실을 줄이고 한눈에 보기쉽게 어쩌고 저쩌고 예나는 세계최고야</p>
            </div>
            <div class="section-content-box">
                <img src="/images/icon/graph.png" alt="그래프" class="icon">
                <h3>매출관리</h3>
                <p>매출관리설명. 아무튼 이러이러한 방식으로 손실을 줄이고 한눈에 보기쉽게 어쩌고 저쩌고 예나는 세계최고야</p>
            </div>
        </div>
        <div class="main-section-line-container">
            <div class="section-content-box" style="border: none">
                <img src="/images/icon/graph.png" alt="그래프" class="icon">
                <h3>매출관리</h3>
                <p>매출관리설명. 아무튼 이러이러한 방식으로 손실을 줄이고 한눈에 보기쉽게 어쩌고 저쩌고 예나는 세계최고야</p>
            </div>
            <div class="section-content-box">
                <img src="/images/icon/graph.png" alt="그래프" class="icon">
                <h3>매출관리</h3>
                <p>매출관리설명. 아무튼 이러이러한 방식으로 손실을 줄이고 한눈에 보기쉽게 어쩌고 저쩌고 예나는 세계최고야</p>
            </div>
            <div class="section-content-box">
                <img src="/images/icon/graph.png" alt="그래프" class="icon">
                <h3>매출관리</h3>
                <p>매출관리설명. 아무튼 이러이러한 방식으로 손실을 줄이고 한눈에 보기쉽게 어쩌고 저쩌고 예나는 세계최고야</p>
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
            <form class="custom-form">
                <label for="name">이름</label>
                <input type="text" id="name" name="name">
                <label for="email">이메일*</label>
                <input type="email" id="email" name="email" required>
                <label for="phone">전화번호</label>
                <input type="tel" id="phone" name="phone">
                <label for="message">메시지를 입력하세요.</label>
                <textarea id="message" name="message" rows="4"></textarea>
                <button type="submit">제출</button>
            </form>
        </div>
    </div>
</div>


<script>
    document.addEventListener("DOMContentLoaded", function () {
        var overlay = document.querySelector('.overlay-content');
        var h1 = overlay.querySelector('h1');
        var lastScrollTop = 0; // 이전 스크롤 위치 저장 변수

        window.addEventListener('scroll', function () {
            var currentScrollTop = window.scrollY || document.documentElement.scrollTop;

            if (currentScrollTop > lastScrollTop) {
                // 스크롤을 아래로 내리는 중
                if (currentScrollTop > 100) {
                    overlay.classList.add('visible');
                    // 애니메이션 클래스 추가
                    h1.classList.add('animate-slideIn');
                }
            } else {
                // 스크롤을 위로 올리는 중
                if (currentScrollTop <= 100) {
                    overlay.classList.remove('visible');
                    // 애니메이션 클래스 제거
                    h1.classList.remove('animate-slideIn');
                }
            }

            lastScrollTop = currentScrollTop <= 0 ? 0 : currentScrollTop; // 음수 값 방지
        });

        // 애니메이션 종료 시 이벤트 리스너 추가
        h1.addEventListener('animationend', function () {
            // 애니메이션 클래스 제거하여 다음에 다시 실행될 수 있도록 함
            h1.classList.remove('animate-slideIn');
        });
    });
</script>

<!-- footer.jsp -->
<%@ include file="/WEB-INF/view/layout/footer.jsp" %>
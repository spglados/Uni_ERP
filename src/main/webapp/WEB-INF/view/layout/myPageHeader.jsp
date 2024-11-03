<%--
  Created by IntelliJ IDEA.
  User: namch
  Date: 24. 10. 10.
  Time: 오전 10:19
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<head>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/animate.css/4.1.1/animate.min.css"/>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Gothic+A1:wght@400;800&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.6.0/css/all.min.css">
    <link href="https://cdn.jsdelivr.net/gh/orioncactus/pretendard/dist/web/static/pretendard.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn-uicons.flaticon.com/uicons-regular-rounded/css/uicons-regular-rounded.css">
    <title>Title</title>
    <link rel="stylesheet" href="/css/common.css">
    <link rel="stylesheet" href="/css/common/myPage.css">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
</head>
<body>

<header class="header">
    <div class="site__header">
        <nav>
            <a href="/main" class="none-a">
                <img src="/images/logo/logo_clear.png" class="animate__animated animate__fadeIn" alt="포스로고"
                     style="height: 100px; width: 125px;">
            </a>
            <ul class="menu">
                <li><a href="/introduction">소개</a></li>
                <li><a href="/notice">공지사항</a></li>
                <li><a href="/erp/main" target="_blank">ERP</a></li>
                <li><a href="/payment">결제</a></li>
                <li><a href="/support">고객지원</a></li>
                <div class="toggle">
                    <span class="toggle--switch"></span>
                </div>
                <div class="header-icons">
                <c:if test="${not empty sessionScope.userSession}">
                    <a href="/myPage" class="header-icon"><i class="fa-solid fa-user-gear icon-hover-text" style="color: #74C0FC;" id="icon1" data-text="마이페이지"></i></a>
                    <a href="/user/logout" class="header-icon"><i class="fa-solid fa-right-from-bracket icon-hover-text" style="color: #74C0FC;" id="icon2" data-text="로그아웃"></i></a>
                </c:if>
                <c:if test="${empty sessionScope.userSession}">
                    <a href="/user/login" class="header-icon"><i class="fa-solid fa-right-to-bracket icon-hover-text" style="color: #74C0FC;" id="icon3" data-text="로그인"></i></a>
                   <a href="/user/join" class="header-icon"><i class="fas fa-user-plus icon-hover-text" style="color: #74C0FC;" id="icon4" data-text="회원가입"></i></a>
                </c:if>
                </div>
            </ul>
        </nav>
    </div>
</header>
<div class="content-container">
<div class="sidebar">
    <h3>내 정보</h3>
    <a href="/myPage">회원 정보 및 수정</a>
    <a href="/myPage/storeList">가게 등록</a>
    <a href="/myPage/paymentHistory">결제 내역</a>
    <a href="/myPage/refundHistory">환불 내역</a>
    <a href="/myPage/contact">내 문의 내역</a>
</div>
<script>
    // 모든 아이콘 요소를 선택합니다.
    const icons = document.querySelectorAll('.icon-hover-text');

    icons.forEach(icon => {
        icon.addEventListener('mouseenter', () => {
            if (!icon.classList.contains('show-text')) {
                icon.classList.add('show-text');

                // 애니메이션이 완료된 후에 클래스 제거
                setTimeout(() => {
                    icon.classList.remove('show-text');
                }, 300);
            }
        });
    });
</script>

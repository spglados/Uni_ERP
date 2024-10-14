<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>주문 페이지</title>
    <link rel="stylesheet" href="/css/pos/posMain.css">
</head>
<body>
<div class="container">
    <!-- 상단 메뉴 -->
    <header class="header">
        <div class="kiosk-status">키오스크 OFF</div>
        <nav class="nav">
            <button class="active">주문 홈</button>
            <button>현황</button>
        </nav>
    </header>

    <!-- 본문 레이아웃 -->
    <div class="content">
        <!-- 메뉴 선택 섹션 -->
        <section class="menu-section">
            <div class="menu-header">
                <h2>즐겨찾는 메뉴</h2>
            </div>
            <div class="menu-items">
                <button>HOT 아메리카노 4,000</button>
                <button>ICE 아메리카노 4,500</button>
                <button>카페라떼 5,000</button>
                <button>카야 토스트 4,000</button>
                <!-- 추가 메뉴 아이템 -->
            </div>
            <div class="menu-options">
                <button>샷 추가 (500)</button>
                <button>사이즈 업 (500)</button>
                <button>시럽 추가 (500)</button>
            </div>
        </section>

        <!-- 주문 정보 섹션 -->
        <aside class="order-info">
            <h3>주문 목록</h3>
            <ul>
                <li>카페라떼 x1 <span>7,000원</span></li>
                <li>다크초콜릿 x2 <span>12,000원</span></li>
            </ul>
            <div class="discount">
                <p>직원 할인: -1,000원</p>
            </div>
            <div class="total">
                <h4>총 결제 금액</h4>
                <p>29,000원</p>
            </div>
            <button class="pay-button">결제</button>
        </aside>
    </div>
</div>
</body>
</html>
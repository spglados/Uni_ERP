<%--
  Created by IntelliJ IDEA.
  User: namch
  Date: 24. 10. 10.
  Time: 오후 3:30
  Description: ERP 대시보드 페이지 (사이드바 메뉴 추가)
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>ERP 시스템 대시보드</title>
    <link rel="stylesheet" href="/css/erp/erpMain.css">
</head>
<body>
<div class="erp-container">
    <!-- 사이드바 메뉴 -->
    <nav class="sidebar">
        <h2>ERP 시스템</h2>
        <ul class="sidebar-menu">
            <li><a href="/hr.jsp">인사 관리</a></li>
            <li><a href="/inventory.jsp">재고 관리</a></li>
            <li><a href="/sales.jsp">매출 관리</a></li>
        </ul>
    </nav>

    <!-- 메인 컨텐츠 -->
    <div class="content">
        <header>
            <h1>ERP 시스템 대시보드</h1>
        </header>
        <main class="erp-content">
            <div class="module-card">
                <h2>인사 관리</h2>
                <p>직원 관리 및 급여 정보를 확인하세요.</p>
                <a href="/hr.jsp" class="erp-button">인사 관리로 이동</a>
            </div>
            <div class="module-card">
                <h2>재고 관리</h2>
                <p>재고 현황과 제품 정보를 관리하세요.</p>
                <a href="/inventory.jsp" class="erp-button">재고 관리로 이동</a>
            </div>
            <div class="module-card">
                <h2>매출 관리</h2>
                <p>매출 현황과 판매 데이터를 확인하세요.</p>
                <a href="/sales.jsp" class="erp-button">매출 관리로 이동</a>
            </div>
        </main>
    </div>
</div>
</body>
</html>

<%--
  Created by IntelliJ IDEA.
  User: namch
  Date: 24. 10. 10.
  Time: 오후 3:30
  Description: ERP 대시보드 페이지 (사이드바 메뉴 및 하위 메뉴 추가)
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
            <li>
                <a href="javascript:void(0);" class="hr-toggle">인사 관리 ▼</a>
                <ul class="sub-menu">
                    <li><a href="/hr/employeeRegister.jsp">직원 등록</a></li>
                    <li><a href="/hr/employeeList.jsp">직원 목록</a></li>
                </ul>
            </li>
            <li>
                <a href="javascript:void(0);" class="inventory-toggle">재고 관리 ▼</a>
                <ul class="sub-menu">
                    <li><a href="/inventory/receiving.jsp">입고 관리</a></li>
                    <li><a href="/inventory/shipping.jsp">출고 관리</a></li>
                    <li><a href="/inventory/status.jsp">재고 현황</a></li>
                    <li><a href="/inventory/adjustment.jsp">재고 조정</a></li>
                    <li><a href="/inventory/suppliers.jsp">공급자 관리</a></li>
                </ul>
            </li>
            <li>
                <a href="javascript:void(0);" class="sales-toggle">매출 관리 ▼</a>
                <ul class="sub-menu">
                    <li><a href="/sales/record.jsp">매출 기록</a></li>
                    <li><a href="/sales/customers.jsp">고객 관리</a></li>
                </ul>
            </li>
            <li>
                <a href="javascript:void(0);" class="product-toggle">상품 관리 ▼</a>
                <ul class="sub-menu">
                    <li><a href="/product/register.jsp">상품 등록</a></li>
                    <li><a href="/product/list.jsp">상품 목록</a></li>
                </ul>
            </li>
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
                <p>직원 관리 및 관련 정보를 확인하세요.</p>
                <a href="#" class="erp-button hr-toggle">인사 관리 세부 메뉴 보기</a>
            </div>
            <div class="module-card">
                <h2>재고 관리</h2>
                <p>재고 현황과 제품 정보를 관리하세요.</p>
                <a href="#" class="erp-button inventory-toggle">재고 관리 세부 메뉴 보기</a>
            </div>
            <div class="module-card">
                <h2>매출 관리</h2>
                <p>매출 현황과 판매 데이터를 확인하세요.</p>
                <a href="#" class="erp-button sales-toggle">매출 관리 세부 메뉴 보기</a>
            </div>
            <div class="module-card">
                <h2>상품 관리</h2>
                <p>상품 정보를 등록하고 관리하세요.</p>
                <a href="#" class="erp-button product-toggle">상품 관리 세부 메뉴 보기</a>
            </div>
        </main>
    </div>
</div>

<!-- 하위 메뉴 토글 스크립트 (순수 JavaScript) -->
<script>
    // 하위 메뉴 토글
    document.addEventListener("DOMContentLoaded", function() {
        // 인사 관리 하위 메뉴 토글
        document.querySelectorAll('.hr-toggle').forEach(function(toggle) {
            toggle.addEventListener('click', function() {
                const subMenu = this.nextElementSibling;
                if (subMenu && subMenu.classList.contains('sub-menu')) {
                    subMenu.style.display = subMenu.style.display === 'block' ? 'none' : 'block';
                }
            });
        });

        // 재고 관리 하위 메뉴 토글
        document.querySelectorAll('.inventory-toggle').forEach(function(toggle) {
            toggle.addEventListener('click', function() {
                const subMenu = this.nextElementSibling;
                if (subMenu && subMenu.classList.contains('sub-menu')) {
                    subMenu.style.display = subMenu.style.display === 'block' ? 'none' : 'block';
                }
            });
        });

        // 매출 관리 하위 메뉴 토글
        document.querySelectorAll('.sales-toggle').forEach(function(toggle) {
            toggle.addEventListener('click', function() {
                const subMenu = this.nextElementSibling;
                if (subMenu && subMenu.classList.contains('sub-menu')) {
                    subMenu.style.display = subMenu.style.display === 'block' ? 'none' : 'block';
                }
            });
        });

        // 상품 관리 하위 메뉴 토글
        document.querySelectorAll('.product-toggle').forEach(function(toggle) {
            toggle.addEventListener('click', function() {
                const subMenu = this.nextElementSibling;
                if (subMenu && subMenu.classList.contains('sub-menu')) {
                    subMenu.style.display = subMenu.style.display === 'block' ? 'none' : 'block';
                }
            });
        });
    });
</script>
</body>
</html>

<%--
  Created by IntelliJ IDEA.
  User: geon
  Date: 24. 10. 28.
  Time: 오후 5:16
  Description: ERP 가게 선택 페이지
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/view/erp/layout/erpHeader.jsp" %>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/erp/storeChoice.css">

<!-- POS 가게 선택 섹션 -->
<main class="main-container">
    <section class="pos-selection-section">
        <h1>가게 선택</h1>
        <div class="pos-cards-container">
            <!-- POS 가게 카드 예시 -->
            <div class="pos-card">
                <h3>가게 이름 1</h3>
                <a href="#" class="pos-select-btn">선택하기</a>
            </div>
            <div class="pos-card">
                <h3>가게 이름 2</h3>
                <a href="#" class="pos-select-btn">선택하기</a>
            </div>
            <div class="pos-card">
                <h3>가게 이름 3</h3>
                <a href="#" class="pos-select-btn">선택하기</a>
            </div>
            <!-- 필요에 따라 추가 POS 가게 카드 삽입 -->
        </div>
    </section>
</main>








<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
<%@include file="/WEB-INF/view/erp/layout/erpFooter.jsp" %>
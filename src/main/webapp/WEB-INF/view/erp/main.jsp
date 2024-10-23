<%--
  Created by IntelliJ IDEA.
  User: namch
  Date: 24. 10. 10.
  Time: 오후 3:30
  Description: ERP 대시보드 페이지 (사이드바 메뉴 및 하위 메뉴 추가)
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/view/erp/layout/erpHeader.jsp"%>


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

<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
<script src="/js/statistics.js"></script>
<%@include file="/WEB-INF/view/erp/layout/erpFooter.jsp"%>

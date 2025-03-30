<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/view/erp/layout/erpHeader.jsp" %>

<div class="content container-fluid">
    <h1 id="currentDate"></h1>

    <div id="myGrid" style="height: 75%" class="ag-theme-alpine"></div>

</div>

<script src="${pageContext.request.contextPath}/js/erp/sales/statistics.js"></script>
    <script src="${pageContext.request.contextPath}/js/erp/sales/test.js"></script>
<%@include file="/WEB-INF/view/erp/layout/erpFooter.jsp" %>
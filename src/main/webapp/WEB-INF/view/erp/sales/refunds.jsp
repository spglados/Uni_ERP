<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/view/erp/layout/erpHeader.jsp"%>
<link rel="stylesheet" href="/css/sortTable.css">

<div class="content container-fluid">
    <h1>환?불</h1>

<canvas id="refund-count-chart" width="800" height="400"></canvas>

</div>

<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
<script src="/js/erp/sales/refunds.js"></script>
<%@include file="/WEB-INF/view/erp/layout/erpFooter.jsp"%>
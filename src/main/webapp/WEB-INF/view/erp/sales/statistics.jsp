<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/view/erp/layout/erpHeader.jsp"%>
<link rel="stylesheet" href="/css/sortTable.css">

<div class="content container-fluid">
    <h1 id="currentDate"> </h1>

    <div id="myGrid" style="height: 75%" class="ag-theme-quartz ag-custom-theme"></div>

</div>

<script src="https://cdn.jsdelivr.net/npm/ag-grid-community/dist/ag-grid-community.min.js"></script>
<script src="/js/erp/sales/statistics.js"></script>
<script src="/js/erp/sales/test.js"></script>
<script src="/js/erp/sales/sortTable.js"></script>
<%@include file="/WEB-INF/view/erp/layout/erpFooter.jsp"%>
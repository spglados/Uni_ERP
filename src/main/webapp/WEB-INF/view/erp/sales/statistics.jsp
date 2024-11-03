<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/view/erp/layout/erpHeader.jsp"%>
<link rel="stylesheet" href="/css/sortTable.css">

<div class="content">
    <h1 id="currentDate"> </h1>

    <div id="myGrid" style="height: 75%" class="ag-theme-quartz ag-custom-theme"></div>
    <div class="chart-grid">
        <div class="chart-row">
            <div class="chart-col">
                <canvas id="sales-chart" width="800" height="400"></canvas>
            </div>
            <div class="chart-col">
                <canvas id="item-count-chart" width="400" height="400"></canvas>
            </div>
        </div>
        <div class="chart-row">
            <div class="chart-col">
                <canvas id="item-profit-chart" width="800" height="400"></canvas>
            </div>
        </div>
    </div>

</div>

<script src="https://cdn.jsdelivr.net/npm/ag-grid-community/dist/ag-grid-community.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
<script src="/js/erp/sales/statistics.js"></script>
<script src="/js/erp/sales/test.js"></script>
<script src="/js/erp/sales/sortTable.js"></script>
<%@include file="/WEB-INF/view/erp/layout/erpFooter.jsp"%>
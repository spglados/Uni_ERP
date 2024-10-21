<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/view/erp/layout/erpHeader.jsp"%>
<link rel="stylesheet" href="/css/statistics.css">

<div class="content">
    <h1 id="currentDate"></h1>

    <table id="table">
    <th>
        <tr>
            <th>상품명</th>
            <th>매출(월)</th>
            <th>전월동기매출</th>
            <th>성장율(%)</th>
            <th>매출(연누계)</th>
            <th>전년동기매출</th>
            <th>성장율(%)</th>
            <th>매출이익(₩)</th>
            <th>비고</th>
        </tr>
    </th>
    <tb id="tableBody">
        <c:forEach var="item" items="${items}">
            <tr>
                <td>${item.name}</td>
                <td>${item.sales}</td>
                <td>${item.itemSales}</td>
                <td>${item.profitRate}</td>
                <td>${item.sales}</td>
                <td>${item.itemSales}</td>
                <td>${item.profitRate}</td>
                <td>${item.sales}</td>
                <td>${item.problems}</td>
            </tr>
        </c:forEach>
    </tb>

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

<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
<script src="/js/erp/sales/statistics.js"></script>
<%@include file="/WEB-INF/view/erp/layout/erpFooter.jsp"%>
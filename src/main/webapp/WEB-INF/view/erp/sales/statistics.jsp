<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/view/erp/layout/erpHeader.jsp"%>
<link rel="stylesheet" href="/css/statistics.css">

<div class="content">
    <h1 id="currentDate"></h1>

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
<script src="/js/statistics.js"></script>
<%@include file="/WEB-INF/view/erp/layout/erpFooter.jsp"%>
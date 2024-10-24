<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/view/erp/layout/erpHeader.jsp"%>
<link rel="stylesheet" href="/css/statistics.css">

<div class="content">
    <h1 id="currentDate"> </h1>

    <div class="table-responsive">
        <table class="table table-bordered">
        <tr>
            <th>시간</th>
            <th>전일목표금액</th>
            <th>전일매출</th>
            <th>전일객수</th>
            <th>금일목표금액</th>
            <th>금일매출</th>
            <th>금일객수</th>
            <th>전일대비</th>
            <th>전년도 대비</th>
            <th>비고</th>
            </tr>
        <tb>
            <c:forEach var="item" items="${salesComparison}">
                <tr>
                    <td>${item.hour}</td>
                    <td>${item.lastDayTargetProfit}</td>
                    <td>${item.lastDayTotalSales}</td>
                    <td>${item.lastDaySalesCount}</td>
                    <td>${item.todayTargetProfit}</td>
                    <td>${item.todayTotalSales}</td>
                    <td>${item.todaySalesCount}</td>
                    <td>
                        ${item.salesComparedToLastDay}
                        <span class="percentage">(${item.percentageComparedToLastDay != null ? item.percentageComparedToLastDay : 'N/A'}%)</span>
                    </td>
                    <td>
                        ${item.salesComparedToLastYear}
                        <span class="percentage">(${item.percentageComparedToLastYear != null ? item.percentageComparedToLastYear : 'N/A'}%)</span>
                    </td>
                    <td></td>
                </tr>
            </c:forEach>
        </tb>
    </table>
</div>

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
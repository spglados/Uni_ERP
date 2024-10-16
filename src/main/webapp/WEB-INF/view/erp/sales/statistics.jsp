<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/view/erp/layout/erpHeader.jsp"%>

<div class="content">
    <h1>매출 분석 통계화면 입니다.</h1>
    <div>
        <label for="startDate">시작일:</label>
        <input type="datetime-local" id="startDate" name="startDate" value="" min="2022-01-01T00:00" max="<%= new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm").format(new java.util.Date()) %>">
        <label for="endDate">종료일:</label>
        <input type="datetime-local" id="endDate" name="endDate" value="" min="2022-01-01T00:00" max="<%= new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm").format(new java.util.Date()) %>">
        <button id="confirm-button" class="btn btn-primary">확인</button>
    </div>

    <div>
        <canvas id="sales-chart" width="800" height="400"></canvas>
        <button id="hourly-button" class="btn btn-primary" onclick="updateCharts('hourly')">시간별</button>
        <button id="daily-button" class="btn btn-primary" onclick="updateCharts('daily')">일별</button>
        <button id="monthly-button" class="btn btn-primary" onclick="updateCharts('monthly')">월별</button>
        <button id="yearly-button" class="btn btn-primary" onclick="updateCharts('yearly')">년별</button>
    </div>

    <canvas id="item-count-chart" width="800" height="400"></canvas>
    <canvas id="item-profit-chart" width="800" height="400"></canvas>

</div>

<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
<script src="/js/statistics.js"></script>
<%@include file="/WEB-INF/view/erp/layout/erpFooter.jsp"%>
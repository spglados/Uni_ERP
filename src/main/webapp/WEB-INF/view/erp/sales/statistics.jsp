<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/view/erp/layout/erpHeader.jsp"%>
<link rel="stylesheet" href="/css/statistics.css">

<div class="content">
    <h1>매출 분석 통계화면 입니다.</h1>
    <div>
        <label for="startDate">시작일:</label>
        <input type="datetime-local" id="startDate" name="startDate" value="" min="2022-01-01T00:00" max="<%= new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm").format(new java.util.Date()) %>">
        <label for="endDate">종료일:</label>
        <input type="datetime-local" id="endDate" name="endDate" value="" min="2022-01-01T00:00" max="<%= new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm").format(new java.util.Date()) %>">
        <label for="storeId">가게:</label>
        <select id="storeId" name="storeId">
            <option value="">전체</option>
            <c:forEach var="storeId" items="${storeIdList}">
                <option value="${storeId}">${storeId}</option>
            </c:forEach>
        </select>
        <button id="confirm-button" class="btn btn-primary">확인</button>
    </div>

    <div class="chart-grid">
        <div class="chart-row">
            <div class="chart-col">
                <canvas id="sales-chart" width="400" height="400"></canvas>
                <button id="hourly-button" class="btn btn-primary" onclick="updateSalesChart('hourly')">시간별</button>
                <button id="daily-button" class="btn btn-primary" onclick="updateSalesChart('daily')">일별</button>
                <button id="monthly-button" class="btn btn-primary" onclick="updateSalesChart('monthly')">월별</button>
                <button id="yearly-button" class="btn btn-primary" onclick="updateSalesChart('yearly')">년별</button>
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
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/view/erp/layout/erpHeader.jsp"%>
<link rel="stylesheet" href="/css/sortTable.css">

<div class="content">
    <div class="table-responsive">
        <table class="table table-bordered">
        <tr>
            <th>직원</th>
            <th>주간 매출액</th>
            <th>주간 주문 수</th>
            <th>월간 매출액</th>
            <th>월간 주문 수</th>
            <th>연간 매출액</th>
            <th>연간 주문 수</th>
        </tr>
        <tb>
            <c:forEach var="employee" items="${employeeSalesData}">
                <tr>
                    <td>${employee.name}</td>
                    <td>${employee.weeklyCostPer}</td>
                    <td>${employee.weeklyTotalOrders}</td>
                    <td>${employee.monthlyCostPer}</td>
                    <td>${employee.monthlyTotalOrders}</td>
                    <td>${employee.yearlyCostPer}</td>
                    <td>${employee.yearlyTotalOrders}</td>
                </tr>
            </c:forEach>
        </tb>
        </table>
    </div>
</div>
<script src="/js/erp/sales/sortTable.js"></script>
<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
<%@include file="/WEB-INF/view/erp/layout/erpFooter.jsp"%>
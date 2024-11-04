<%--
  Created by IntelliJ IDEA.
  User: namch
  Date: 24. 10. 10.
  Time: 오후 6:42
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/view/erp/layout/erpHeader.jsp"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<link rel="stylesheet" href="/css/sortTable.css">


<style>
    .spinner-overlay {
        position: fixed;
        top: 0;
        left: 0;
        width: 100%;
        height: 100%;
        background: rgba(255, 255, 255, 0.8);
        display: flex;
        justify-content: center;
        align-items: center;
        z-index: 1000;
    }
    .spinner {
        border: 8px solid #f3f3f3;
        border-top: 8px solid #3498db;
        border-radius: 50%;
        width: 50px;
        height: 50px;
        animation: spin 1s linear infinite;
    }
    @keyframes spin {
        0% { transform: rotate(0deg); }
        100% { transform: rotate(360deg); }
    }

    .btn-main {
        background-color: #F8F399;
        border-color: #F8F399;
        color: #000;
    }
</style>
<%-- 로딩 스피너 --%>
<div id="loading-spinner" style="display: none;">
    <div class="spinner-overlay">
        <div class="spinner"></div>
    </div>
</div>
<div class="content container-fluid">
    <div class="d-flex flex-column mb-3 align-items-start">
        <label>기준일 선택</label>
        <div class="d-flex">
            <select id="selectYear" class="form-control" style="width: 100px;" required>
                <option value="${currentYear}">년</option>
                <c:forEach var="year" begin="2022" end="${currentYear}">
                    <option value="${year}">${year}</option>
                </c:forEach>
            </select>
            <select id="selectMonth" class="form-control" style="width: 100px; margin-left: 10px;" required>
                <option value="${currentMonth}">월</option>
                <c:forEach var="month" begin="1" end="12">
                    <option value="${month}">${month}</option>
                </c:forEach>
            </select>
            <button id="submit-btn" type="submit" class="btn btn-main" style="margin-left: 10px;">확인</button>
        </div>
    </div>
    <div class="d-flex justify-content-between mb-4" style="width: 100%; font-size: 18px;">
        <div class="text-center">
            <h5>기준일자</h5>
            <span id="dateSpan" style="font-size: 24px; font-weight: bold; color: #007bff;"></span>
        </div>

            <div class="text-center">
                <h5>매출(월)</h5>
                <div style="display: inline-flex; align-items: flex-end;">
                    <span class="sales-month-span" style="font-size: 30px; font-weight: bold; color: #007bff;"></span>
                    <div style="display: flex; flex-direction: column; align-items: flex-start; margin-left: 4px;">
                        <span class="sales-month-percent-span" style="font-size: 15px; color: green;"></span>
                        <span style="font-size: 15px; color: gray;">전월 대비</span>
                    </div>
                </div>
            </div>

            <div class="text-center">
                <h5>매출(연누계)</h5>
                <div style="display: inline-flex; align-items: flex-end;">
                    <span class="sales-year-span" style="font-size: 30px; font-weight: bold; color: #007bff;"></span>
                    <div style="display: flex; flex-direction: column; align-items: flex-start; margin-left: 4px;">
                        <span class="sales-year-percent-span" style="font-size: 15px; color: green;"></span>
                        <span style="font-size: 15px; color: gray;">전년 대비</span>
                    </div>
                </div>
            </div>

            <div class="text-center">
                <h5>매출이익(연누계)</h5>
                <div style="display: inline-flex; align-items: flex-end;">
                    <span class="sales-profit-span" style="font-size: 30px; font-weight: bold; color: #007bff;"></span>
                    <div style="display: flex; flex-direction: column; align-items: flex-start; margin-left: 4px;">
                        <span class="sales-profit-percent-span" style="font-size: 15px; color: green;"></span>
                        <span style="font-size: 15px; color: gray;">전년 대비</span>
                    </div>
                </div>
            </div>

            <div class="text-center">
                <h5>목표달성률(연)</h5>
                <div style="display: inline-flex; align-items: flex-end;">
                    <span id="targetAchievementRateSpan" class="target-achievement-rate-span" style="font-size: 30px; font-weight: bold; color: #007bff;"></span>
                </div>
            </div>
        </div>

    <div class="table-responsive">
        <table class="table table-bordered">
            <thead>
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
            </thead>
            <tbody>

            </tbody>
        </table>
    </div>
</div>

<script src="/js/erp/sales/detail.js"></script>
<script src="/js/erp/sales/sortTable.js"></script>
<%@ include file="/WEB-INF/view/erp/layout/erpFooter.jsp" %>

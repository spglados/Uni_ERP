<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/view/erp/layout/erpHeader.jsp" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div class="content">
    <h1>매출 기록</h1>

    <div class="d-flex flex-column mb-3 align-items-start">
        <label>기준일 선택</label>
        <div class="d-flex">
            <select id="selectYear" class="form-control" style="width: 100px;">
                <option value="">년</option>
                <c:forEach var="year" begin="2022" end="${currentYear}">
                    <option value="${year}">${year}</option>
                </c:forEach>
            </select>

            <select id="selectMonth" class="form-control" style="width: 100px; margin-left: 10px;">
                <option value="">월</option>
                <c:forEach var="month" begin="1" end="12">
                    <option value="${month}">${month}</option>
                </c:forEach>
            </select>
            <button id="submit-btn" type="submit" class="btn btn-primary" style="margin-left: 10px;">확인</button>
        </div>
    </div>

    <div class="d-flex justify-content-between mb-4" style="width: 100%; font-size: 18px;">
        <div class="text-center">
            <h5>기준일자</h5>
            <span style="font-size: 24px; font-weight: bold; color: #007bff;">2024-10-20</span>
        </div>

        <div class="text-center">
            <h5>매출(월)</h5>
            <div style="display: inline-flex; align-items: flex-end;">
                <span style="font-size: 30px; font-weight: bold; color: #007bff;">13,267</span>
                <div style="display: flex; flex-direction: column; align-items: flex-start; margin-left: 4px;">
                    <span style="font-size: 15px; color: green;">(+10%)</span>
                    <span style="font-size: 15px; color: gray;">전월 대비</span>
                </div>
            </div>
        </div>

        <div class="text-center">
            <h5>매출(연누계)</h5>
            <div style="display: inline-flex; align-items: flex-end;">
                <span style="font-size: 30px; font-weight: bold; color: #007bff;">71,884</span>
                <div style="display: flex; flex-direction: column; align-items: flex-start; margin-left: 4px;">
                    <span style="font-size: 15px; color: green;">(+17%)</span>
                    <span style="font-size: 15px; color: gray;">전년 대비</span>
                </div>
            </div>
        </div>

        <div class="text-center">
            <h5>매출이익(연누계)</h5>
            <div style="display: inline-flex; align-items: flex-end;">
                <span style="font-size: 30px; font-weight: bold; color: #007bff;">23,087</span>
                <div style="display: flex; flex-direction: column; align-items: flex-start; margin-left: 4px;">
                    <span style="font-size: 15px; color: green;">(+17%)</span>
                    <span style="font-size: 15px; color: gray;">전년 대비</span>
                </div>
            </div>
        </div>

        <div class="text-center">
            <h5>목표달성률(연)</h5>
            <div style="display: inline-flex; align-items: flex-end;">
                <span style="font-size: 30px; font-weight: bold; color: #007bff;">104.7%</span>
                <div style="display: flex; flex-direction: column; align-items: flex-start; margin-left: 4px;">
                </div>
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
                <tr>
                    <td>된장찌개</td>
                    <td>101</td>
                    <td>98</td>
                    <td>+3%</td>
                    <td>25%</td>
                    <td>24%</td>
                    <td>₩12,625</td>
                    <td></td>
                    <td></td>
                </tr>
                <tr>
                    <td>김치찌개</td>
                    <td>94</td>
                    <td>89</td>
                    <td>+5.6%</td>
                    <td>30%</td>
                    <td>28%</td>
                    <td>₩14,100</td>
                    <td></td>
                    <td></td>
                </tr>
                <tr>
                    <td>계란말이</td>
                    <td>106</td>
                    <td>100</td>
                    <td>+6%</td>
                    <td>20%</td>
                    <td>19%</td>
                    <td>₩10,600</td>
                    <td></td>
                    <td></td>
                </tr>
                <tr>
                    <td>비빔밥</td>
                    <td>94</td>
                    <td>90</td>
                    <td>+4.4%</td>
                    <td>35%</td>
                    <td>33%</td>
                    <td>₩16,450</td>
                    <td></td>
                    <td></td>
                </tr>
                <tr>
                    <td>김치전</td>
                    <td>122</td>
                    <td>115</td>
                    <td>+6.1%</td>
                    <td>40%</td>
                    <td>38%</td>
                    <td>₩24,400</td>
                    <td></td>
                    <td></td>
                </tr>
            </tbody>
        </table>
    </div>
</div>

<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script src="/js/erp/sales/detail.js"></script>
<%@ include file="/WEB-INF/view/erp/layout/erpFooter.jsp" %>

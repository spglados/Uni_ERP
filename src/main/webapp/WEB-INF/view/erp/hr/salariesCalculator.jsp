<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ include file="/WEB-INF/view/erp/layout/erpHeader.jsp" %>
<style>
    .results-section {
        display: none;
        margin-top: 20px;
    }

    .insurance-section, .results-section {
        margin-top: 20px;
    }

    /* Optional: Style for active tab */
    .nav-tabs .nav-item.show .nav-link, .nav-tabs .nav-link.active {
        background-color: #f8f9fa;
        border-color: #dee2e6 #dee2e6 #fff;
    }

    /* Hide Sunday allowance by default */
    #includeSundayWorkContainer {
        display: none;
    }

    /* Adjust employee selection grid */
    .employee-checkbox {
        margin-bottom: 10px;
    }

    .allowance-options .form-check {
        margin-bottom: 5px;
    }

    .tab-pane .row {
        margin-top: 20px;
    }

    .tab-pane .table {
        width: 100%;
    }

    .form-check-input:disabled + .form-check-label {
        color: #6c757d; /* 회색으로 변경 */
        cursor: not-allowed;
    }
</style>
<div class="content container-fluid">
<h1 class="mb-4">급여 계산 페이지</h1>
<c:choose>
    <c:when test="${employees.size() == 0}">
        <p>모든 직원의 급여가 확정되었습니다.</p>
    </c:when>
    <c:otherwise>
        <form id="payrollForm">
            <!-- Employee Selection -->
            <div class="card mb-4">
                <div class="card-header">
                    Employee 선택
                </div>
                <div class="card-body">
                    <div class="form-group">
                        <!-- 전체 선택 체크박스 추가 -->
                        <div class="form-check mb-3">
                            <input class="form-check-input" type="checkbox" id="selectAllEmployees">
                            <label class="form-check-label" for="selectAllEmployees">
                                전체 선택 / 해제
                            </label>
                        </div>
                        <div class="row">
                            <c:forEach var="employee" items="${employees}">
                                <div class="col-md-3">
                                    <div class="form-check employee-checkbox">
                                        <input class="form-check-input" type="checkbox" name="empNos" value="${employee.uniqueEmployeeNumber}"
                                               id="emp${employee.uniqueEmployeeNumber}">
                                        <label class="form-check-label" for="emp${employee.uniqueEmployeeNumber}">
                                                ${employee.name} (사번: ${employee.uniqueEmployeeNumber})
                                        </label>
                                    </div>
                                </div>
                            </c:forEach>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Allowance Options -->
            <div class="card mb-4 allowance-options">
                <div class="card-header">
                    수당 포함 옵션
                </div>
                <div class="card-body">
                    <div class="form-group">
                        <div class="form-check">
                            <input class="form-check-input" type="checkbox" id="includeOvertime" name="includeOvertime" checked>
                            <label class="form-check-label" for="includeOvertime">
                                초과 근무 수당 포함
                            </label>
                        </div>
                        <div class="form-check">
                            <input class="form-check-input" type="checkbox" id="includeHolidayWork" name="includeHolidayWork" checked>
                            <label class="form-check-label" for="includeHolidayWork">
                                휴일 근무 수당 포함
                            </label>
                        </div>
                        <div class="form-check" id="includeSundayWorkContainer">
                            <input class="form-check-input" type="checkbox" id="includeSundayWork" name="includeSundayWork">
                            <label class="form-check-label" for="includeSundayWork">
                                일요일 포함
                            </label>
                        </div>
                        <div class="form-check">
                            <input class="form-check-input" type="checkbox" id="includeNightWork" name="includeNightWork" checked>
                            <label class="form-check-label" for="includeNightWork">
                                야간 근무 수당 포함
                            </label>
                        </div>
                        <div class="form-check">
                            <input class="form-check-input" type="checkbox" id="includeWeeklyHoliday" name="includeWeeklyHoliday" checked>
                            <label class="form-check-label" for="includeWeeklyHoliday">
                                주휴 수당 포함
                            </label>
                        </div>
                        <div class="form-check">
                            <input class="form-check-input" type="checkbox" id="includeInsurance" name="includeInsurance" checked>
                            <label class="form-check-label" for="includeInsurance">
                                보험 포함
                            </label>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Buttons -->
            <div class="d-flex justify-content-between mb-4">
                <div>
                    <button type="button" class="btn btn-primary mr-2" id="calculateBtn">계산</button>
                    <button type="button" class="btn btn-secondary" id="refreshBtn" onclick="location.reload();">새로 고침</button>
                </div>
                <button type="button" class="btn btn-success" id="confirmBtn" disabled>확정</button>
            </div>
        </form>

        <!-- Results Section -->
        <div class="card results-section" id="resultsSection">
            <div class="card-header">
                급여 산출 결과
            </div>
            <div class="card-body">
                <!-- Nav tabs -->
                <ul class="nav nav-tabs" id="employeeTabs" role="tablist">
                    <!-- Tabs will be dynamically added here -->
                </ul>

                <!-- Tab panes -->
                <div class="tab-content" id="employeeTabContent">
                    <!-- Tab contents will be dynamically added here -->
                </div>
            </div>
        </div>
        </div>
    </c:otherwise>
</c:choose>
<script src="/js/erp/hr/scheduleCalculator.js"></script>


<%@ include file="/WEB-INF/view/erp/layout/erpFooter.jsp" %>
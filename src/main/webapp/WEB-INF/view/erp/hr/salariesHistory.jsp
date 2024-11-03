<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ include file="/WEB-INF/view/erp/layout/erpHeader.jsp" %>

<!-- Main Content -->
<div class="content container-fluid">
<h1 class="mt-4 mb-4">급여 관리</h1>
<hr>
<c:choose>
    <c:when test="${employeeList.isEmpty()}">
        <p>산출된 급여 내역이 없습니다.</p>
    </c:when>
    <c:otherwise>

        <div class="row" style="height: 600px;">
            <!-- 직원 목록 -->
            <div class="col-md-4 mb-4 d-flex">
                <div class="card shadow-sm flex-fill d-flex flex-column">
                    <div class="card-header d-flex justify-content-between align-items-center">
                        <span>직원 목록</span>
                        <button type="button" class="btn btn-secondary btn-sm" onclick="resetEmployeeFilters()" title="필터 초기화">
                            <i class="fas fa-sync-alt"></i>
                        </button>
                    </div>
                    <div class="card-body p-0 flex-grow-1" style="overflow: hidden;">
                        <div id="employeeGrid" class="ag-theme-quartz" style="height: 100%; width: 100%;"></div>
                    </div>
                </div>
            </div>

            <!-- 급여 명세서 -->
            <div class="col-md-8 d-flex">
                <div class="card shadow-sm flex-fill d-flex flex-column">
                    <div class="card-header d-flex justify-content-between align-items-center">
                        <h5 class="mb-0">급여 명세서</h5>
                        <form method="get" action="/erp/hr/salaries" class="form-inline mb-0">
                            <input type="hidden" name="employeeId" value="${selectedEmployee.empNo}"/>
                            <div class="form-group">
                                <label for="yearMonth" class="mr-2">조회 월:</label>
                                <input type="month" id="yearMonth" name="yearMonth" value="${currentYearMonth}" class="form-control" onchange="this.form.submit()">
                            </div>
                        </form>
                    </div>
                    <div class="card-body flex-grow-1 d-flex flex-column">
                        <!-- 급여 정보 카드 -->
                        <div class="card mb-4">
                            <div class="card-body">
                                <div class="row">
                                    <div class="col-md-6">
                                        <p><strong>년도/월:</strong> ${selectedEmployee.salaryDetails.year}년 ${selectedEmployee.salaryDetails.month}월</p>
                                        <p><strong>지급일자:</strong> ${selectedEmployee.salaryDetails.payDate}</p>
                                        <p><strong>직원명:</strong> ${selectedEmployee.name}</p>
                                        <p><strong>연락처:</strong> ${selectedEmployee.phone}</p>
                                    </div>
                                    <div class="col-md-6">
                                        <p><strong>급여계좌:</strong> ${selectedEmployee.accountNumber}</p>
                                        <p><strong>시급단가:</strong> <fmt:formatNumber value="${selectedEmployee.wage}" type="currency" currencySymbol="" groupingUsed="true"/>원</p>
                                        <p><strong>총 근무시간:</strong> ${selectedEmployee.salaryDetails.totalWorkHours}시간</p>
                                        <p><strong>총 근무일 수:</strong> ${selectedEmployee.salaryDetails.totalWorkDays}일</p>
                                    </div>
                                </div>
                                <hr>
                                <div class="row">
                                    <div class="col-md-6">
                                        <p><strong>총 급여액:</strong> <span class="text-success"><fmt:formatNumber value="${selectedEmployee.salaryDetails.totalSalary}"
                                                                                                                type="currency"
                                                                                                                currencySymbol="" groupingUsed="true"/>원</span></p>
                                    </div>
                                    <div class="col-md-6">
                                        <p><strong>공제액:</strong> <span class="text-danger"><fmt:formatNumber value="${selectedEmployee.salaryDetails.deductions}" type="currency"
                                                                                                             currencySymbol="" groupingUsed="true"/>원</span></p>
                                    </div>
                                    <div class="col-md-12">
                                        <p><strong>실지급액:</strong> <span class="text-primary h4"><fmt:formatNumber value="${selectedEmployee.salaryDetails.netPay}" type="currency"
                                                                                                                  currencySymbol="" groupingUsed="true"/>원</span></p>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <!-- 근무 상세 그리드 -->
                        <h5 class="mb-3">근무 상세</h5>
                        <div id="salaryDetailGrid" class="ag-theme-quartz" style="height: 300px; width: 100%;"></div>
                    </div>
                </div>
            </div>
        </div>
        </div>
    </c:otherwise>
</c:choose>
<script>
    // 직원 목록 ag-Grid 설정
    const employeeColumnDefs = [
        {headerName: "직원 ID", field: "id", sortable: true, filter: true, width: 90},
        {headerName: "이름", field: "name", sortable: true, filter: true, width: 150},
        {headerName: "연락처", field: "contact", sortable: true, filter: true, width: 150},
        {headerName: "급여 계좌", field: "salaryAccount", sortable: true, filter: true, width: 200},
        {headerName: "현시급단가", field: "hourlyRate", sortable: true, filter: 'agNumberColumnFilter', width: 120}
    ];

    const employeeRowData = [
        <c:forEach var="employee" items="${employeeList}" varStatus="status">
        {
            id: ${employee.empNo},
            name: "${employee.name}",
            contact: "${employee.phone}",
            salaryAccount: "${employee.accountNumber}",
            hourlyRate: ${employee.wage}
        }<c:if test="${!status.last}">, </c:if>
        </c:forEach>
    ];

    const employeeGridOptions = {
        columnDefs: employeeColumnDefs,
        rowData: employeeRowData,
        rowSelection: 'single',
        onRowClicked: function (event) {
            window.location.href = "/erp/hr/salaries-history?empNo=" + event.data.id + "&yearMonth=" + "${currentYearMonth}";
        },
        defaultColDef: {
            sortable: true,
            filter: true,
            resizable: true
        },
        pagination: true,
        paginationPageSize: 10,
        domLayout: 'autoHeight'
    };

    // 급여 상세 ag-Grid 설정
    const salaryDetailColumnDefs = [
        {headerName: "날짜", field: "date", sortable: true, filter: 'agDateColumnFilter', width: 120},
        {headerName: "출근 시간", field: "clockIn", sortable: true, filter: 'agTextColumnFilter', width: 120},
        {headerName: "퇴근 시간", field: "clockOut", sortable: true, filter: 'agTextColumnFilter', width: 120},
        {
            headerName: "휴식 여부",
            field: "hasBreak",
            sortable: true,
            filter: true,
            cellRenderer: function (params) {
                return params.value ? '<span class="badge badge-success">있음</span>' : '<span class="badge badge-secondary">없음</span>';
            },
            width: 100
        },
        {
            headerName: "출근 상태",
            field: "attendanceStatus",
            sortable: true,
            filter: true,
            cellRenderer: function (params) {
                const status = params.value;
                let badgeClass = 'badge-info';
                let statusText = '';

                if (status === '정상') {
                    badgeClass = 'badge-success';
                    statusText = '정상 출근';
                } else if (status === '지각' || status === '조퇴' || status === '지각&조퇴') {
                    badgeClass = 'badge-warning';
                    statusText = status;
                } else if (status === '무단결근') {
                    badgeClass = 'badge-danger';
                    statusText = '무단결근';
                } else {
                    statusText = status; // 기타 상태는 그대로 표시
                }

                return '<span class="badge ' + badgeClass + '">' + statusText + '</span>';
            },
            width: 150
        }
    ];

    const salaryDetailRowData = [
        <c:forEach var="attendance" items="${selectedEmployee.salaryDetails.dailyAttendances}" varStatus="status">
        {
            date: "${attendance.date}",
            clockIn: "${attendance.clockIn}",
            clockOut: "${attendance.clockOut}",
            hasBreak: ${attendance.hasBreak},
            attendanceStatus: "${attendance.attendanceStatus}"
        }<c:if test="${!status.last}">, </c:if>
        </c:forEach>
    ];

    const salaryDetailGridOptions = {
        columnDefs: salaryDetailColumnDefs,
        rowData: salaryDetailRowData,
        defaultColDef: {
            sortable: true,
            filter: true,
            resizable: true
        },
        pagination: true,
        paginationPageSize: 10,
        domLayout: 'autoHeight'
    };

    document.addEventListener('DOMContentLoaded', function () {
        // 직원 목록 그리드 초기화
        const employeeGridDiv = document.querySelector('#employeeGrid');
        new agGrid.Grid(employeeGridDiv, employeeGridOptions);
        if (employeeGridOptions.api) {
            employeeGridOptions.api.sizeColumnsToFit();
        }

        // 급여 상세 그리드 초기화
        const salaryDetailGridDiv = document.querySelector('#salaryDetailGrid');
        new agGrid.Grid(salaryDetailGridDiv, salaryDetailGridOptions);
        if (salaryDetailGridOptions.api) {
            salaryDetailGridOptions.api.sizeColumnsToFit();
        }
    });

    // 필터 초기화 함수
    function resetEmployeeFilters() {
        const employeeGrid = agGrid.Grid.getGridInstance(document.querySelector('#employeeGrid'));
        if (employeeGrid) {
            employeeGrid.api.setFilterModel(null);
            employeeGrid.api.onFilterChanged();
        }
    }
</script>

<%@ include file="/WEB-INF/view/erp/layout/erpFooter.jsp" %>

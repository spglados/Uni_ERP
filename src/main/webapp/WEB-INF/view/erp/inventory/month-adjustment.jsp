<%--
  Created by IntelliJ IDEA.
  User: namch
  Date: 24. 10. 10.
  Time: 오후 6:42
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/view/erp/layout/erpHeader.jsp" %>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/erp/material.css">
<!-- Font Awesome (필요 시 추가) -->
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css"
      crossorigin="anonymous" referrerpolicy="no-referrer"/>

<style>
    /* 아이템 텍스트 스타일 */
    .list-group-item span {
        font-weight: bold;
        color: #343a40;
    }

    /* 아이콘 색상 변경 */
    .list-group-item i {
        color: #007bff; /* 원하는 색상으로 변경 가능 */
    }

</style>

<!-- 재고 관리 콘텐츠 -->
<div class="content container-fluid">
    <h1>월 재고 현황</h1>
    <hr>
    <!-- Material List Grid -->
    <div class="shadow p-3 mb-5 bg-white rounded" style="height: 110%; margin-top: 26px;">
        <div class="refresh-btn-div">
            <button type="button" class="btn btn-secondary ml-2 btn-action " onclick="resetFilters()">
                <i class="fas fa-sync-alt"></i>
            </button>
        </div>
        <hr>
        <div id="myGrid" style="height: 500px; width:100%;" class="ag-theme-alpine"></div>
    </div>
</div>

<script>
    // 페이지 로드 시간 기록
    const pageLoadTime = new Date().getTime();

    // 재고 현황 데이터 배열 생성
    const materialManagementData = [
        <c:forEach var="material" items="${monthAdjustmentList}" varStatus="status">
        {
            materialCode: "${material.materialCode}",
            name: "${material.materialName}",
            monthReceiveAmount: "${material.monthReceiveAmount} ${material.unit}",
            useAmount: "${material.useAmount} ${material.unit}"
        }<c:if test="${!status.last}">, </c:if>
        </c:forEach>
    ];

    // ag-Grid 옵션 설정
    const gridOptions = {
        // 데이터 행
        rowData: materialManagementData,

        // 고유 Row ID 설정
        getRowId: function(params) {
            return params.data.materialCode;
        },

        // 컬럼 정의
        columnDefs: [
            {
                field: "materialCode",
                headerName: "자재코드",
                sortable: true,
                filter: 'agTextColumnFilter',
                resizable: true,
                suppressMovable: false,
                editable: false
            },
            {
                field: "name",
                headerName: "자재명",
                sortable: true,
                filter: 'agTextColumnFilter',
                resizable: true,
                suppressMovable: false,
                editable: false
            },
            {
                field: "monthReceiveAmount",
                headerName: "금월 입고량",
                sortable: true,
                filter: 'agNumberColumnFilter',
                resizable: true,
                suppressMovable: false,
                editable: false,
                valueFormatter: function(params) {
                    return params.value; // 필요 시 단위 추가
                }
            },
            {
                field: "useAmount",
                headerName: "금월 사용량",
                sortable: true,
                filter: 'agNumberColumnFilter',
                resizable: true,
                suppressMovable: false,
                editable: false,
                valueFormatter: function(params) {
                    return params.value; // 필요 시 단위 추가
                }
            }
        ],

        // 행 높이 설정
        rowHeight: 55,

        // 기본 컬럼 정의: 모든 컬럼에 공통으로 적용될 설정
        defaultColDef: {
            flex: 1,
            minWidth: 150,
            resizable: true,
            sortable: true,
            filter: true
        },

        // 페이징 설정
        pagination: true,
        paginationPageSize: 10,

        // 행 애니메이션
        animateRows: true,

        // 행 선택 모드
        rowSelection: 'single',

        // 기타 설정
        suppressMovableColumns: false,
        domLayout: 'autoHeight',

        // 그리드 준비 시 컬럼 사이즈 자동 조정
        onGridReady: function(params) {
            params.api.sizeColumnsToFit();
        }
    };

    // ag-Grid 초기화
    document.addEventListener('DOMContentLoaded', function () {
        const gridDiv = document.querySelector('#myGrid');
        new agGrid.Grid(gridDiv, gridOptions);

        // 컬럼 사이즈 자동 조정
        gridOptions.api.sizeColumnsToFit();
    });

    // 필터 초기화 함수
    function resetFilters() {
        // 모든 필터 초기화
        gridOptions.api.setFilterModel(null);
        gridOptions.api.setQuickFilter('');
    }
</script>

<%@ include file="/WEB-INF/view/erp/layout/erpFooter.jsp" %>

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
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/common/agGrid.css">
<style>
    .modal-dialog {
        max-width: 20%;
    }

    /* 리스트 아이템 호버 효과 */
    .list-group-item:hover {
        background-color: #f1f1f1;
        cursor: pointer;
    }

    /* 아이템 텍스트 스타일 */
    .list-group-item span {
        font-weight: bold;
        color: #343a40;
    }

    /* 아이콘 색상 변경 */
    .list-group-item i {
        color: #007bff; /* 원하는 색상으로 변경 가능 */
    }

    /* ag-Grid의 드롭다운 메뉴 스타일 조정 */
    .ag-menu {
        max-width: 200px; /* 필요한 최대 너비로 조정 */
        overflow: hidden;
    }

    .ag-date-picker {
        z-index: 1000; /* 다른 요소보다 위에 표시되도록 설정 */
    }

    /* 버튼 스타일링 */
    .btn-action {
        padding: 5px 10px;
        background-color: #F8F399;
        color: black;
        border: none;
        border-radius: 3px;
        cursor: pointer;
    }

    .btn-action:hover {
        background-color: #E5DC92;
    }

    /* Editable cell styling */
    .ag-theme-alpine .editable-cell {
        background-color: #FFF3DA; /* 연한 파란색 배경 */
        cursor: pointer; /* 커서를 포인터로 변경 */
        border-radius: 5px;
        border-left: 1px solid #FF2800; /* 왼쪽 테두리로 강조 */
        border-right: 1px solid #FF2800; /* 오른쪽 테두리로 강조 */
        border-top: 1px solid #FF2800; /* 위쪽 테두리로 강조 */
    }

    .ag-theme-alpine .editable-cell:hover {
        background-color: #bae7ff; /* 호버 시 배경색 변경 */
    }
</style>

<!-- 재고 관리 콘텐츠 -->
<div class="content container-fluid">
    <h1>일 재고 관리</h1>
    <hr>
    <!-- 저장 버튼 영역 -->
    <div class="shadow p-3 mb-5 bg-white rounded" style="height: 110%; margin-top: 26px;">
        <div class="d-flex justify-content-end mb-2">
            <!-- 저장 버튼 추가 -->
            <button id="saveButton" class="btn btn-secondary ml-2 btn-action" title="저장"><i class="fas fa-save"></i>
            </button>
            <div class="refresh-btn-div">
                <button type="button" class="btn btn-secondary ml-2 btn-action " onclick="resetFilters()"
                        title="필터 초기화">
                    <i class="fas fa-sync-alt"></i>
                </button>
            </div>
        </div>
        <hr>
        <!-- 자재 목록 그리드 -->
        <div id="myGrid" style="height: 500px; width:100%;" class="ag-theme-alpine"></div>
    </div>
</div>

<!-- 상품 내역 모달 -->
<div class="modal fade" id="ingredientModal" tabindex="-1" role="dialog" aria-labelledby="ingredientModalLabel"
     aria-hidden="true">
    <div class="modal-dialog modal-sm modal-dialog-centered" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="ingredientModalLabel">상품 내역</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="닫기">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <!-- Bootstrap List Group 적용 -->
                <ul class="list-group" id="product-list">
                    <!-- 상품 목록이 동적으로 삽입됩니다 -->
                </ul>
            </div>
        </div>
    </div>
</div>

<script>

    // 페이지 로드 시간 기록
    const pageLoadTime = new Date().getTime();

    // 재고 현황 데이터 배열 생성
    const materialManagementData = [
        <c:forEach var="material" items="${materialStatusList}" varStatus="status">
        {
            materialCode: "${material.materialCode}",
            name: "${material.name}",
            category: "${material.category}",
            theoreticalAmount: ${material.theoreticalAmount},
            unit: "${material.unit}",
            actualAmount: ${material.actualAmount},
            loss: ${material.loss}
        }<c:if test="${!status.last}">, </c:if>
        </c:forEach>
    ];

    // ag-Grid 옵션 설정
    const gridOptions = {
        // 데이터 행
        rowData: materialManagementData,

        // 고유 Row ID 설정
        getRowId: function (params) {
            return params.data.materialCode;
        },

        // 컬럼 정의
        columnDefs: [
            {
                field: "materialCode",
                headerName: "자재코드",
                sortable: true,
                filter: true,
                resizable: true,
                suppressMovable: false,
                editable: false
            },
            {
                field: "name",
                headerName: "자재명",
                sortable: true,
                filter: true,
                resizable: true,
                suppressMovable: false,
                editable: false
            },
            {
                field: "category",
                headerName: "분류",
                sortable: true,
                filter: true,
                resizable: true,
                suppressMovable: false,
                editable: false
            },
            {
                field: "theoreticalAmount",
                headerName: "이론 재고",
                sortable: true,
                filter: 'agNumberColumnFilter',
                resizable: true,
                suppressMovable: false,
                editable: false,
                valueFormatter: function (params) {
                    return params.value + ' ' + params.data.unit;
                }
            },
            {
                field: "actualAmount",
                headerName: "실 재고",
                sortable: true,
                filter: 'agNumberColumnFilter',
                resizable: true,
                suppressMovable: false,
                editable: true, // Editable
                valueFormatter: function (params) {
                    return params.value + ' ' + params.data.unit;
                },
                cellEditor: 'agNumberCellEditor', // Number input
                cellClass: 'editable-cell' // Add CSS class for styling
            },
            {
                field: "loss",
                headerName: "재고 손실",
                sortable: true,
                filter: 'agNumberColumnFilter',
                resizable: true,
                suppressMovable: false,
                editable: false,
                valueFormatter: function (params) {
                    if (params.value >= 0) {
                        return '+' + params.value + ' ' + params.data.unit;
                    } else {
                        return params.value + ' ' + params.data.unit;
                    }
                },
                cellStyle: function (params) {
                    if (params.value < 0) {
                        return {color: 'red'};
                    } else {
                        return {color: 'black'};
                    }
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
        onGridReady: function (params) {
            params.api.sizeColumnsToFit();
        },

        // 실재고 변경 시 유효성 검사
        onCellValueChanged: function (params) {
            if (params.colDef.field === 'actualAmount') {
                const newValue = parseFloat(params.data.actualAmount);
                if (isNaN(newValue) || newValue < 0) {
                    alert('실 재고는 0 이상이어야 합니다.');
                    params.data.actualAmount = params.oldValue;
                    params.api.refreshCells({rowNodes: [params.node], columns: ['actualAmount']});
                }
            }
        }
    };

    // ag-Grid 초기화
    document.addEventListener('DOMContentLoaded', function () {
        const gridDiv = document.querySelector('#myGrid');
        new agGrid.Grid(gridDiv, gridOptions);

        // 컬럼 사이즈 자동 조정
        gridOptions.api.sizeColumnsToFit();
    });

    // 저장 버튼 클릭 시 변경된 데이터를 서버로 전송
    document.getElementById('saveButton').addEventListener('click', function () {

        const currentTime = new Date().getTime();
        const elapsedTime = currentTime - pageLoadTime;

        if (elapsedTime > 60000) { // 60,000 ms = 1 minute
            alert('데이터 갱신이 필요합니다. 새로고침을 해주세요.');
            return;
        }

        // 변경된 데이터를 추출
        const updatedData = [];

        gridOptions.api.forEachNode(function (node) {
            updatedData.push({
                materialCode: node.data.materialCode,
                actualAmount: node.data.actualAmount
            });
        });

        // AJAX 요청을 통해 데이터 전송
        fetch('/erp/inventory/day-adjustment', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(updatedData)
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('네트워크 응답이 올바르지 않습니다.');
                }
                return response.text(); // 응답을 텍스트로 먼저 반환
            })
            .then(text => {
                if (text) {
                    return JSON.parse(text);
                } else {
                    throw new Error('서버 응답이 비어 있습니다.');
                }
            })
            .then(data => {
                if (data.success) {
                    alert("성공적으로 저장했습니다!");
                    window.location.href = '/erp/inventory/situation';
                } else if (!data.success) {
                    alert("변경된 값이 없습니다. \n\n\t 변경 후 Enter를 눌러주세요 !");
                } else {
                    alert("저장에 실패했습니다.");
                }
            })
            .catch(error => {
                // 오류 처리
                console.error('오류:', error);
                alert('저장 중 오류가 발생했습니다.');
            });
    });
</script>

<%@ include file="/WEB-INF/view/erp/layout/erpFooter.jsp" %>

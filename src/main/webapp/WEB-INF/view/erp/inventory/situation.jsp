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
</style>

<!-- 재고 관리 콘텐츠 -->
<div class="content container-fluid">
    <h1>현황 관리</h1>
    <hr>

    <!-- Material List Grid -->
    <div class="shadow p-3 mb-5 bg-white rounded" style="height: 110%; margin-top: 26px;">
        <div class="refresh-btn-div">
            <button type="button" class="btn btn-secondary ml-2 btn-action " onclick="resetFilters()" title="필터 초기화">
                <i class="fas fa-sync-alt"></i>
            </button>
        </div>
        <hr>
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
        // DOM 요소 가져오기
        const categoryFilter = document.getElementById('categoryFilter');
        const categoryTitle = document.getElementById('categoryTitle');
        const searchInput = document.getElementById('searchInput');

        // 자재 데이터 배열 생성
        var materialsData = [
            <c:forEach var="material" items="${materialDTOList}" varStatus="status">
            {
                id: ${material.id},
                name: "${material.name}",
                unit: "${material.unit}",
                subUnit: "${material.subUnit}"
            }<c:if test="${!status.last}">, </c:if>
            </c:forEach>
        ];

        // 재고 현황 데이터 배열 생성
        const materialStatusData = [
            <c:forEach var="material" items="${materialStatusList}" varStatus="status">
            {
                materialCode: "${material.materialCode}",
                name: "${material.name}",
                category: "${material.category}",
                theoreticalAmount: ${material.theoreticalAmount},
                actualAmount: ${material.actualAmount},
                loss: ${material.loss},
                unit: "${material.unit}",
                useProduct: [
                    <c:forEach var="product" items="${material.useProduct}" varStatus="productStatus">
                    { value: "${product.value}" }<c:if test="${!productStatus.last}">, </c:if>
                    </c:forEach>
                ]
            }<c:if test="${!status.last}">, </c:if>
            </c:forEach>
        ];

        // ag-Grid 옵션 설정
        const gridOptions = {
            // 데이터 행
            rowData: materialStatusData,

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
                    valueFormatter: function(params) {
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
                    editable: false,
                    valueFormatter: function(params) {
                        return params.value + ' ' + params.data.unit;
                    }
                },
                {
                    field: "loss",
                    headerName: "재고 손실",
                    sortable: true,
                    filter: 'agNumberColumnFilter',
                    resizable: true,
                    suppressMovable: false,
                    editable: false,
                    valueFormatter: function(params) {
                        if (params.value >= 0) {
                            return '+' + params.value + ' ' + params.data.unit;
                        } else {
                            return params.value + ' ' + params.data.unit;
                        }
                    },
                    cellStyle: function(params) {
                        if (params.value < 0) {
                            return { color: 'red' };
                        } else {
                            return { color: 'black' };
                        }
                    }
                },
                {
                    headerName: "사용 상품",
                    field: "useProduct",
                    cellRenderer: function(params) {
                        const materialCode = params.data.materialCode;
                        return '<button class="btn btn-secondary ml-2 btn-action" onclick="showProducts(\'' + materialCode + '\')"><i class="fas fa-box"></i></button>';
                    },
                    sortable: false,
                    filter: false,
                    resizable: true,
                    suppressMovable: false,
                    width: 120
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
            // 카테고리 필터와 검색 입력 초기화
            categoryFilter.value = '전체';
            categoryTitle.textContent = '전체';
            searchInput.value = '';
        }

        // 특정 행의 사용 상품 내역 표시 함수
        function showProducts(materialCode) {
            // 해당 자재의 행 노드를 찾습니다.
            const rowNode = gridOptions.api.getRowNode(materialCode);
            if (!rowNode) {
                alert('해당 자재의 상품 내역을 찾을 수 없습니다.');
                return;
            }

            const useProducts = rowNode.data.useProduct;
            const productList = document.getElementById('product-list');
            productList.innerHTML = '';

            if (useProducts && useProducts.length > 0) {
                useProducts.forEach(function(product) {
                    const listItem = document.createElement('li');
                    listItem.className = 'list-group-item list-group-item-action d-flex align-items-center';

                    const link = document.createElement('a');
                    link.href = '/erp/product/list/' + product.value;
                    link.className = 'text-decoration-none text-dark w-100 d-flex align-items-center';

                    const icon = document.createElement('i');
                    icon.className = 'fas fa-utensils mr-2'; // Font Awesome 아이콘

                    const span = document.createElement('span');
                    span.textContent = product.value;

                    link.appendChild(icon);
                    link.appendChild(span);
                    listItem.appendChild(link);
                    productList.appendChild(listItem);
                });
            } else {
                const listItem = document.createElement('li');
                listItem.className = 'list-group-item';
                listItem.textContent = '사용하고 있는 상품이 없습니다.';
                productList.appendChild(listItem);
            }

            // 모달을 표시합니다.
            $('#ingredientModal').modal('show');
        }
    </script>

<%@ include file="/WEB-INF/view/erp/layout/erpFooter.jsp" %>

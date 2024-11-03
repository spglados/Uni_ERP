<%--
  Created by IntelliJ IDEA.
  User: namch
  Date: 24. 10. 10.
  Time: 오후 6:42
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/view/erp/layout/erpHeader.jsp" %>
<link rel="stylesheet" href="/css/erp/material.css">

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
</style>

<!-- 재고 관리 콘텐츠 -->
<div class="content container-fluid">
    <h1>재고 관리</h1>
    <hr>
    <!-- Material List Grid -->
    <div class="shadow p-3 mb-5 bg-white rounded" style="height: 83%; margin-top: 26px;">
        <div class="refresh-btn-div">
            <button type="button" class="btn btn-secondary ml-2 btn-action " onclick="resetFilters()" title="필터 초기화">
                <i class="fas fa-sync-alt"></i>
            </button>
        </div>
        <hr>
        <div id="myGrid" style="height: 500px; width:100%;" class="ag-theme-quartz"></div>
    </div>
</div>

    <!-- 상품 내역 모달 -->
    <div class="modal fade" id="ingredientModal" tabindex="-1" role="dialog" aria-labelledby="ingredientModalLabel"
         aria-hidden="true">
        <div class="modal-dialog modal-sm modal-dialog-centered" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="ingredientModalLabel">상품 내역</h5>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
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

        // 재고 현황 데이터 배열 생성
        const materialManagementData = [
            <c:forEach var="material" items="${materialManagementList}" varStatus="status">
            {
                materialCode: "${material.materialCode}",
                name: "${material.name}",
                category: "${material.category}",
                unit: "${material.unit}",
                lastEnterDate: "${material.lastEnterDate != null ? material.lastEnterDate : '없음'}",
                imminent: ${material.imminent},
                expirationDate: "${material.expirationDate != null ? material.expirationDate : '없음'}",
                stockCycle: ${material.stockCycle > 0 ? material.stockCycle : '\"-\"'},
                alarmCycle: "${material.alarmCycle}",
                alarmUnit: "${material.alarmUnit}",
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
            rowData: materialManagementData,

            // 고유 Row ID 설정
            getRowId: function(params) {
                return params.data.materialCode;
            },

            // 컬럼 정의
            columnDefs: [
                {
                    field: "materialCode",
                    headerName: "자재 번호",
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
                    field: "unit",
                    headerName: "단위",
                    sortable: true,
                    filter: true,
                    resizable: true,
                    suppressMovable: false,
                    editable: false
                },
                {
                    field: "lastEnterDate",
                    headerName: "마지막 입고 날짜",
                    sortable: true,
                    filter: 'agDateColumnFilter',
                    resizable: true,
                    suppressMovable: false,
                    editable: false,
                    cellRenderer: function(params) {
                        return params.value;
                    }
                },
                {
                    field: "expirationDate",
                    headerName: "임박 유통기한",
                    sortable: true,
                    filter: 'agDateColumnFilter',
                    resizable: true,
                    suppressMovable: false,
                    editable: false,
                    cellRenderer: function(params) {
                        const imminent = params.data.imminent;
                        const expirationDate = params.value;
                        if (imminent) {
                            return '<span style="color: red;"><i class="fas fa-clock" style="color: black;"></i> ' + expirationDate + '</span>';
                        } else {
                            return '<i class="fas fa-calendar"></i> ' + expirationDate;
                        }
                    }
                },
                {
                    field: "stockCycle",
                    headerName: "입고 주기",
                    sortable: true,
                    filter: 'agNumberColumnFilter',
                    resizable: true,
                    suppressMovable: false,
                    editable: false,
                    valueFormatter: function(params) {
                        return params.value !== '-' ? params.value + '일' : '-일';
                    }
                },
                {
                    field: "alarmCycle",
                    headerName: "알림 주기",
                    sortable: true,
                    filter: 'agNumberColumnFilter',
                    resizable: true,
                    suppressMovable: false,
                    editable: false,
                    valueFormatter: function(params) {
                        return params.value + ' ' + params.data.alarmUnit;
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

        // 필터 적용 함수
        function filterProducts() {
            var selectedCategory = categoryFilter.value;
            var searchKeyword = searchInput.value.toLowerCase();

            // 카테고리 필터 적용
            var categoryFilterInstance = gridOptions.api.getFilterInstance('category');
            if (categoryFilterInstance) {
                if (selectedCategory === '전체') {
                    categoryFilterInstance.setModel(null); // 카테고리 필터 초기화
                } else {
                    categoryFilterInstance.setModel({
                        type: 'equals',
                        filter: selectedCategory
                    });
                }
                categoryFilterInstance.applyModel();
            }

            // 자재명 검색 필터 적용 (quick filter 사용)
            gridOptions.api.setQuickFilter(searchKeyword);
        }

        // 카테고리 필터 변경 시 필터링 및 검색어 비우기
        categoryFilter.addEventListener('change', function () {
            var selectedCategory = this.value;
            categoryTitle.textContent = selectedCategory;
            // 검색어 비우기
            searchInput.value = '';
            filterProducts();
        });

        // 검색 입력 시 필터링
        searchInput.addEventListener('input', function () {
            filterProducts();
        });

        // 엔터 키 입력 시 필터링
        searchInput.addEventListener('keyup', function (event) {
            if (event.key === 'Enter') {
                filterProducts();
            }
        });

        // 필터 초기화 함수
        function resetFilters() {
            // 모든 필터 초기화
            gridOptions.api.setFilterModel(null);
            gridOptions.api.setQuickFilter('');
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

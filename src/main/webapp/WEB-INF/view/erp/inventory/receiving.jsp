<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/view/erp/layout/erpHeader.jsp" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<link rel="stylesheet" href="/css/erp/material.css">

<!-- Main Content -->
<div class="content">
    <h1>입고 관리</h1>
    <hr>

    <!-- Material List Grid -->
    <div class="shadow p-3 mb-5 bg-white rounded" style="height: 83%; margin-top: 26px;">
        <div class="refresh-btn-div">
            <!-- 필터 초기화 버튼 추가 -->
            <button type="button" class="btn btn-secondary ml-2 btn-action " onclick="resetFilters()" title="필터 초기화">
                <i class="fas fa-sync-alt"></i>
            </button>
            <button type="button" class="btn btn-secondary ml-2 btn-action" data-toggle="modal" data-target="#registerModal" title="입고 내역 추가하기">
                <i class="fas fa-plus-circle"></i>
            </button>
        </div>
        <hr>
        <div id="myGrid" style="height: 500px; width:100%;" class="ag-theme-quartz"></div>
    </div>
</div>

<!-- Include Modal for Registration -->
<!-- 입고 등록 모달 -->
<div class="modal fade" id="registerModal" tabindex="-1" role="dialog" aria-labelledby="registerModalLabel"
     aria-hidden="true">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <!-- Form for Material Order Registration -->
            <form>
                <div class="modal-header">
                    <h5 class="modal-title" id="registerModalLabel">입고 등록</h5>
                    <button type="button" class="close" data-dismiss="modal" aria-label="닫기">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <div class="modal-body">
                    <!-- Form Fields -->
                    <div class="form-group">
                        <label for="materialName">상품명</label>
                        <input type="text" class="form-control" id="materialName" name="name" required>
                    </div>
                    <div class="form-group">
                        <label for="price">가격</label>
                        <input type="number" class="form-control" id="price" name="price" required>
                    </div>
                    <div class="form-group">
                        <label for="amount">입고량</label>
                        <input type="number" step="0.01" class="form-control" id="amount" name="amount" required>
                    </div>
                    <!-- 자재 선택 -->
                    <div class="form-group">
                        <label for="materialId">자재 선택</label>
                        <select class="form-control" id="materialId" name="materialId" required>
                            <option value="">자재 선택</option>
                            <c:forEach var="material" items="${materialDTOList}">
                                <option value="${material.id}">${material.name}</option>
                            </c:forEach>
                        </select>
                    </div>
                    <!-- 단위 선택 -->
                    <div class="form-group">
                        <label for="unit">단위</label>
                        <select class="form-control" id="unit" name="unit" required>
                        </select>
                    </div>
                    <div class="form-group">
                        <label for="supplier">공급처</label>
                        <input type="text" class="form-control" id="supplier" name="supplier" required>
                    </div>
                    <div class="form-group">
                        <label for="expirationDate">유통기한</label>
                        <input type="date" class="form-control" id="expirationDate" name="expirationDate" required>
                    </div>
                    <div class="form-group">
                        <label for="receiptDate">입고 날짜</label>
                        <input type="date" class="form-control" id="receiptDate" name="receiptDate" required>
                    </div>
                    <input type="hidden" name="isUse" value="true">
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-dismiss="modal">취소</button>
                    <button type="button" onclick="enterMaterialOrder()" class="btn btn-primary">등록</button>
                </div>
            </form>
        </div>
    </div>
</div>

<script>
    // DOM 요소 가져오기
    const expirationDate = document.getElementById('expirationDate');
    const receiveDate = document.getElementById('receiptDate');
    const materialName = document.getElementById('materialName');
    const price = document.getElementById('price');
    const amount = document.getElementById('amount');
    const materialId = document.getElementById('materialId');
    const unit = document.getElementById('unit');
    const supplier = document.getElementById('supplier');

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

    // 입고 내역 데이터 배열 생성
    const materialOrderData = [
        <c:forEach var="materialOrder" items="${materialOrderList}" varStatus="status">
        {
            name: "${materialOrder.name}",
            price: ${materialOrder.price},
            amount: ${materialOrder.amount},
            unit: "${materialOrder.unit}",
            supplier: "${materialOrder.supplier}",
            expirationDate: "${materialOrder.expirationDate}",
            receiptDate: "${materialOrder.receiptDate}",
            enterDate: "${materialOrder.enterDate}"
        }<c:if test="${!status.last}">, </c:if>
        </c:forEach>
    ];

    // ag-Grid 옵션 설정
    const gridOptions = {
        // 데이터 행
        rowData: materialOrderData,

        // 컬럼 정의
        columnDefs: [
            {
                field: "name",
                headerName: "상품명",
                sortable: true,
                filter: true,
                resizable: true,
                // 컬럼 이동 비활성화
                suppressMovable: false,
                // 편집 비활성화 (필요 시 true로 변경)
                editable: false
            },
            {
                field: "price",
                headerName: "가격",
                sortable: true,
                filter: 'agNumberColumnFilter',
                resizable: true,
                // 컬럼 이동 비활성화
                suppressMovable: false,
                // 편집 비활성화 (필요 시 true로 변경)
                editable: false,
                // 값 포매터: 가격에 '원' 추가
                valueFormatter: function(params) {
                    return params.value + '원';
                }
            },
            {
                field: "amount",
                headerName: "입고량",
                sortable: true,
                filter: 'agNumberColumnFilter',
                resizable: true,
                // 컬럼 이동 비활성화
                suppressMovable: false,
                // 편집 비활성화 (필요 시 true로 변경)
                editable: false,
                // 값 포매터: 입고량과 단위 결합
                valueFormatter: function(params) {
                    return params.value + ' ' + params.data.unit;
                }
            },
            {
                field: "supplier",
                headerName: "공급처",
                sortable: true,
                filter: true,
                resizable: true,
                // 컬럼 이동 비활성화
                suppressMovable: false,
                // 편집 비활성화 (필요 시 true로 변경)
                editable: false
            },
            {
                field: "expirationDate",
                headerName: "유통기한",
                sortable: true,
                filter: 'agDateColumnFilter',
                resizable: true,
                // 컬럼 이동 비활성화
                suppressMovable: false,
                // 편집 비활성화 (필요 시 true로 변경)
                editable: false
            },
            {
                field: "receiptDate",
                headerName: "입고 날짜",
                sortable: true,
                filter: 'agDateColumnFilter',
                resizable: true,
                // 컬럼 이동 비활성화
                suppressMovable: false,
                // 편집 비활성화 (필요 시 true로 변경)
                editable: false
            },
            {
                field: "enterDate",
                headerName: "등록 날짜",
                sortable: true,
                filter: 'agDateColumnFilter',
                resizable: true,
                // 컬럼 이동 비활성화
                suppressMovable: false,
                // 편집 비활성화 (필요 시 true로 변경)
                editable: false
            }
        ],

        // 행 높이 설정
        rowHeight: 55,

        // 기본 컬럼 정의: 모든 컬럼에 공통으로 적용될 설정
        defaultColDef: {
            flex: 1, // 가변 너비
            minWidth: 150, // 최소 너비
            resizable: true, // 컬럼 크기 조절 가능
            sortable: true, // 정렬 가능
            filter: true // 필터 가능
        },

        // 페이징 설정
        pagination: true, // 페이징 활성화
        paginationPageSizeSelector: [10, 20, 50],  // 원하는 페이지 수 나열
        paginationPageSize: 10, // 한 페이지당 표시할 행 수

        // 행 애니메이션
        animateRows: true,

        // 행 선택 모드
        rowSelection: 'single', // 단일 행 선택

        // 기타 설정
        suppressMovableColumns: false, // 모든 컬럼의 이동 비활성화
        domLayout: 'autoHeight' // 필요 시 활성화: 그리드가 콘텐츠에 맞춰 높이 조정
    };

    // ag-Grid 초기화
    document.addEventListener('DOMContentLoaded', function () {
        const gridDiv = document.querySelector('#myGrid');
        new agGrid.Grid(gridDiv, gridOptions);

        // 컬럼 사이즈 자동 조정
        gridOptions.api.sizeColumnsToFit();
    });

    // 단위 옵션 업데이트 함수
    function updateUnitOptions() {
        var materialSelect = document.getElementById('materialId');
        var unitSelect = document.getElementById('unit');

        var selectedMaterialId = materialSelect.value;

        unitSelect.innerHTML = '';

        // 입고 날짜를 오늘로 기본 설정
        receiveDate.value = getTodayDate();

        var defaultOption = document.createElement('option');
        defaultOption.value = '';
        defaultOption.text = '단위 선택';
        unitSelect.appendChild(defaultOption);

        if (selectedMaterialId) {
            var selectedMaterial = materialsData.find(function (material) {
                return material.id == selectedMaterialId;
            });

            if (selectedMaterial) {
                var unitOption = document.createElement('option');
                unitOption.value = selectedMaterial.unit;
                unitOption.text = selectedMaterial.unit;
                unitSelect.appendChild(unitOption);
            }
        }
    }

    // 자재 선택 변경 시 단위 옵션 업데이트
    document.getElementById('materialId').addEventListener('change', updateUnitOptions);

    // 페이지 로드 시 단위 옵션 업데이트
    window.addEventListener('load', function () {
        updateUnitOptions();
    });

    // 유효성 검사 함수
    function validateForm() {
        if (materialName.value.trim() === '') {
            alert('상품명을 입력해주세요.');
            materialName.focus();
            return false;
        }

        if (price.value === '' || price.value <= 0) {
            alert('유효한 가격을 입력해주세요.');
            price.focus();
            return false;
        }

        if (amount.value === '' || amount.value <= 0) {
            alert('유효한 입고량을 입력해주세요.');
            amount.focus();
            return false;
        }

        if (materialId.value === '') {
            alert('자재를 선택해주세요.');
            materialId.focus();
            return false;
        }

        if (unit.value === '') {
            alert('단위를 선택해주세요.');
            unit.focus();
            return false;
        }

        if (supplier.value.trim() === '') {
            alert('공급처를 입력해주세요.');
            supplier.focus();
            return false;
        }

        if (expirationDate.value === '') {
            alert('유통기한을 선택해주세요.');
            expirationDate.focus();
            return false;
        }

        if (!checkExpirationDate(expirationDate.value)) {
            expirationDate.focus();
            return false;
        }

        if (receiveDate.value === '') {
            alert('입고 날짜를 선택해주세요.');
            receiveDate.focus();
            return false;
        }

        if (!checkReceiveDate(receiveDate.value)) {
            receiveDate.focus();
            return false;
        }

        return true;
    }

    // 입고 등록 함수
    function enterMaterialOrder() {
        let form = document.querySelector('form');
        let formData = new FormData(form);

        // 유효성 검사 실행
        if (!validateForm()) {
            return;
        }

        fetch('/erp/inventory/receiving', {
            method: 'POST',
            body: formData
        })
            .then(response => {
                if (response.ok) {
                    alert('성공적으로 저장되었습니다!');
                    window.location.reload();
                } else {
                    alert('저장 도중 오류가 발생했습니다.\n\n입고 내역을 다시 확인해주세요!');
                }
            })
            .catch(error => {
                console.log('error', error);
                alert('저장에 실패했습니다.');
            });
    }

    // 오늘 날짜 반환 함수
    function getTodayDate() {
        const today = new Date();
        const year = today.getFullYear();
        const month = String(today.getMonth() + 1).padStart(2, '0');
        const day = String(today.getDate()).padStart(2, '0');
        return year + '-' + month + '-' + day;
    }

    // 유통기한 검사 함수
    function checkExpirationDate(inputDateValue) {
        const today = new Date();
        today.setHours(0, 0, 0, 0); // 오늘 날짜의 시간 초기화 (자정으로 설정)

        const targetDate = new Date(inputDateValue);
        targetDate.setHours(0, 0, 0, 0); // 입력 날짜의 시간 초기화

        if (targetDate <= today) {
            alert("유통기한은 오늘 이후 날짜여야 합니다.");
            return false;
        }
        return true;
    }

    // 입고 날짜 검사 함수
    function checkReceiveDate(inputDateValue) {
        const today = new Date();
        today.setHours(0, 0, 0, 0); // 오늘 날짜의 시간 초기화

        const targetDate = new Date(inputDateValue);
        targetDate.setHours(0, 0, 0, 0); // 입력 날짜의 시간 초기화

        if (targetDate > today) {
            alert("입고 날짜는 오늘 또는 이전 날짜여야 합니다.");
            return false;
        }
        return true;
    }

    // 필터 초기화 함수
    function resetFilters() {
        gridOptions.api.setFilterModel(null); // 모든 필터 초기화
        gridOptions.api.onFilterChanged(); // 필터 변경 사항 반영
    }

</script>

<%@ include file="/WEB-INF/view/erp/layout/erpFooter.jsp" %>

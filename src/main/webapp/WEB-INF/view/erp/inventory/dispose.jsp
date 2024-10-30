<%--
  Created by IntelliJ IDEA.
  User: namch
  Date: 24. 10. 10.
  Time: 오후 6:42
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/view/erp/layout/erpHeader.jsp" %>

<!-- 부트스트랩 CSS 및 Font Awesome 포함 -->
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
<link rel="stylesheet" href="/css/erp/material.css">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css"
      crossorigin="anonymous" referrerpolicy="no-referrer"/>

<!-- 부트스트랩 JS 및 jQuery 포함 (모달 및 탭 기능을 위해 필요) -->
<script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"
        integrity="sha384-DfXdJZilnjrKKtaAElcqSZ1twcLbU5lXrK4lUGnGk0R1nGic4s1jGKf1BvCDjH8D"
        crossorigin="anonymous"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@4.5.2/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-LtrjvnR4/J58gJSAJH05CdFKzFJDxYgC5r6dY6rXkz9O12FV1DlWQKIcXh8H7N9K"
        crossorigin="anonymous"></script>

<style>
    /* /css/erp/material.css 파일에 추가 */

    /* 모달 다이얼로그의 고정 크기 설정 */
    .fixed-size-modal .modal-dialog {
        max-width: 800px;  /* 원하는 너비로 조정 */
        width: 800px;
        height: 700px;     /* 원하는 높이로 조정 */
        margin: 30px auto; /* 모달의 위아래 여백 조정 */
    }

    /* 모달 콘텐츠의 고정 높이와 플렉스 레이아웃 설정 */
    .fixed-size-modal .modal-content {
        height: 700px;
        display: flex;
        flex-direction: column;
    }

    /* 모달 바디의 스크롤 가능 설정 */
    .fixed-size-modal .modal-body {
        flex: 1 1 auto;
        overflow-y: auto;
    }

    /* 테이블 컨테이너의 고정 높이와 스크롤 설정 */
    .fixed-size-modal .modal-body .table-responsive {
        max-height: 400px;  /* 원하는 최대 높이로 조정 */
        overflow-y: auto;
    }

    /* 로딩 스피너 스타일 */
    .spinner-overlay {
        position: fixed;
        top: 0;
        left: 0;
        width: 100%;
        height: 100%;
        background: rgba(255, 255, 255, 0.7);
        display: flex;
        align-items: center;
        justify-content: center;
        z-index: 1050; /* 부트스트랩 모달보다 높게 설정 */
        display: none; /* 초기에는 숨김 */
    }
</style>

<!-- 로딩 스피너 -->
<div class="spinner-overlay" id="loadingSpinner">
    <div class="spinner-border text-primary" role="status">
        <span class="sr-only">Loading...</span>
    </div>
</div>

<!-- 폐기 등록 콘텐츠 -->
<div class="content container mt-4">
    <h1 class="mb-4">폐기 등록</h1>
    <hr>

    <!-- 추가 및 저장 버튼 영역 -->
    <div class="d-flex justify-content-end mb-3">
        <!-- 추가 버튼 -->
        <button id="addButton" class="btn btn-success align-self-end">추가</button>
        <!-- 저장 버튼 -->
        <button id="saveButton" class="btn btn-primary ml-2 align-self-end">저장</button>
    </div>

    <!-- 폐기 목록 테이블 -->
    <div class="shadow p-3 mb-5 bg-white rounded" style="height: 83%; margin-top: 26px;">
        <div class="table-container">
            <table class="table table-bordered table-striped" id="disposalList">
                <thead class="thead-light">
                <tr>
                    <th>유형</th>
                    <th>번호</th>
                    <th>이름</th>
                    <th>분류</th>
                    <th>폐기 양</th>
                    <th>폐기 날짜</th>
                    <th>삭제</th>
                </tr>
                </thead>
                <tbody>
                <!-- 동적으로 추가될 행 -->
                </tbody>
            </table>
        </div>
    </div>

    <!-- 폐기 등록 모달 -->
    <div class="modal fade fixed-size-modal" id="disposalModal" tabindex="-1" role="dialog" aria-labelledby="disposalModalLabel"
         aria-hidden="true">
        <div class="modal-dialog modal-lg modal-dialog-centered" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">폐기할 자재/상품 선택</h5>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <div class="modal-body">
                    <!-- 검색 필드 -->
                    <input type="text" id="modalSearchInput" class="form-control mb-3" placeholder="검색어 입력">
                    <!-- 탭 메뉴 -->
                    <ul class="nav nav-tabs" id="disposalTab" role="tablist">
                        <li class="nav-item">
                            <a class="nav-link active" id="materials-tab" data-toggle="tab" href="#materials" role="tab"
                               aria-controls="materials" aria-selected="true">자재</a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" id="products-tab" data-toggle="tab" href="#products" role="tab"
                               aria-controls="products" aria-selected="false">상품</a>
                        </li>
                    </ul>
                    <div class="tab-content" id="disposalTabContent">
                        <!-- 자재 탭 -->
                        <div class="tab-pane fade show active" id="materials" role="tabpanel" aria-labelledby="materials-tab">
                            <div class="table-responsive">
                                <table class="table table-bordered table-striped mt-3">
                                    <thead class="thead-light">
                                    <tr>
                                        <th>자재코드</th>
                                        <th>이름</th>
                                        <th>분류</th>
                                        <th>단위</th>
                                        <th>선택</th>
                                    </tr>
                                    </thead>
                                    <tbody id="materialsTableBody">
                                    <!-- 자재 목록이 동적으로 삽입됩니다 -->
                                    </tbody>
                                </table>
                            </div>
                        </div>
                        <!-- 상품 탭 -->
                        <div class="tab-pane fade" id="products" role="tabpanel" aria-labelledby="products-tab">
                            <div class="table-responsive">
                                <table class="table table-bordered table-striped mt-3">
                                    <thead class="thead-light">
                                    <tr>
                                        <th>상품코드</th>
                                        <th>이름</th>
                                        <th>분류</th>
                                        <th>선택</th>
                                    </tr>
                                    </thead>
                                    <tbody id="productsTableBody">
                                    <!-- 상품 목록이 동적으로 삽입됩니다 -->
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" id="confirmAddButton" class="btn btn-primary">추가</button>
                    <button type="button" class="btn btn-secondary" data-dismiss="modal">취소</button>
                </div>
            </div>
        </div>
    </div>

    <!-- JavaScript 섹션 -->
    <script>
        // 자재 폐기 리스트와 상품 폐기 리스트를 JavaScript 배열로 정의
        const materialDisposalList = [
            <c:forEach var="material" items="${materialDisposalList}" varStatus="status">
            {
                materialCode: '${material.materialCode}',
                materialName: '${material.materialName}',
                category: '${material.category}',
                unit: '${material.unit}'
            }<c:if test="${!status.last}">,</c:if>
            </c:forEach>
        ];

        const productDisposalList = [
            <c:forEach var="product" items="${productDisposalList}" varStatus="status">
            {
                productCode: '${product.productCode}',
                productName: '${product.productName}',
                category: '${product.category}'
            }<c:if test="${!status.last}">,</c:if>
            </c:forEach>
        ];

        console.log('materialDisposalList', materialDisposalList);
        console.log('productDisposalList', productDisposalList);

        document.addEventListener('DOMContentLoaded', function () {
            // 필터 및 버튼 요소 선택
            const addButton = document.getElementById('addButton');
            const saveButton = document.getElementById('saveButton');
            const disposalModal = $('#disposalModal');
            const modalSearchInput = document.getElementById('modalSearchInput');
            const materialsTableBody = document.getElementById('materialsTableBody');
            const productsTableBody = document.getElementById('productsTableBody');
            const confirmAddButton = document.getElementById('confirmAddButton');
            const disposalList = document.getElementById('disposalList').getElementsByTagName('tbody')[0];
            const loadingSpinner = document.getElementById('loadingSpinner');

            // 자재와 상품 목록을 테이블에 삽입하는 함수
            function populateDisposalTables() {
                // 자재 테이블 채우기
                materialDisposalList.forEach(function (material) {
                    let row = '<tr>' +
                        '<td>' + material.materialCode + '</td>' +
                        '<td>' + material.materialName + '</td>' +
                        '<td>' + material.category + '</td>' +
                        '<td>' + material.unit + '</td>' +
                        '<td><input type="checkbox" class="selectDisposal" data-type="자재" data-code="' + material.materialCode + '" data-name="' + material.materialName + '" data-category="' + material.category + '" data-unit="' + material.unit + '"></td>' +
                        '</tr>';
                    materialsTableBody.innerHTML += row;
                });

                // 상품 테이블 채우기
                productDisposalList.forEach(function (product) {
                    let row = '<tr>' +
                        '<td>' + product.productCode + '</td>' +
                        '<td>' + product.productName + '</td>' +
                        '<td>' + product.category + '</td>' +
                        '<td><input type="checkbox" class="selectDisposal" data-type="상품" data-code="' + product.productCode + '" data-name="' + product.productName + '" data-category="' + product.category + '"></td>' +
                        '</tr>';
                    productsTableBody.innerHTML += row;
                });
            }

            // 폐기 목록 테이블에서 '삭제' 버튼 클릭 시 해당 행 삭제
            disposalList.addEventListener('click', function (event) {
                if (event.target && event.target.classList.contains('removeDisposalButton')) {
                    const row = event.target.closest('tr');
                    row.remove();
                }
            });

            // 폐기 목록에 행 추가 함수
            function addDisposalRow(disposal) {
                // 중복 추가 방지
                const rows = disposalList.getElementsByTagName('tr');
                let exists = false;
                for (let i = 0; i < rows.length; i++) {
                    const row = rows[i];
                    if (row.getAttribute('data-type') === disposal.type &&
                        row.getAttribute('data-code') === disposal.code) {
                        exists = true;
                        break;
                    }
                }

                if (exists) {
                    alert('이미 추가된 항목입니다.');
                    return;
                }

                // 오늘 날짜 가져오기
                const todayDate = getTodayDate();

                // 새로운 행 생성
                let newRow = '<tr data-type="' + disposal.type + '" data-code="' + disposal.code + '">' +
                    '<td>' + disposal.type + '</td>' +
                    '<td>' + disposal.code + '</td>' +
                    '<td>' + disposal.name + '</td>' +
                    '<td>' + disposal.category + '</td>' +
                    '<td>' +
                    '<input type="number" class="form-control disposal-amount" min="0" required>' +
                    (disposal.unit ? ' ' + disposal.unit : '') +
                    '</td>' +
                    '<td>' +
                    '<input type="date" class="form-control disposal-date" value="' + todayDate + '" required>' +
                    '</td>' +
                    '<td>' +
                    '<button class="btn btn-danger btn-sm removeDisposalButton">삭제</button>' +
                    '</td>' +
                    '</tr>';
                disposalList.innerHTML += newRow;
            }

            // 추가 버튼 클릭 시 모달 열기
            addButton.addEventListener('click', function () {
                // 모달 열기 전 모든 체크박스 해제
                const checkboxes = document.querySelectorAll('.selectDisposal');
                checkboxes.forEach(function (checkbox) {
                    checkbox.checked = false;
                });
                // 모달 검색 필드 초기화
                modalSearchInput.value = '';
                // 모든 행 보이기
                const materialRows = materialsTableBody.getElementsByTagName('tr');
                Array.from(materialRows).forEach(function (row) {
                    row.style.display = '';
                });
                const productRows = productsTableBody.getElementsByTagName('tr');
                Array.from(productRows).forEach(function (row) {
                    row.style.display = '';
                });
                // 모달 열기
                disposalModal.modal('show');
            });

            // 모달 내 '추가' 버튼 클릭 시 선택된 항목 추가
            confirmAddButton.addEventListener('click', function () {
                // 선택된 자재/상품 찾기
                const selectedCheckboxes = document.querySelectorAll('.selectDisposal:checked');
                selectedCheckboxes.forEach(function (checkbox) {
                    const type = checkbox.getAttribute('data-type');
                    const code = checkbox.getAttribute('data-code');
                    const name = checkbox.getAttribute('data-name');
                    const category = checkbox.getAttribute('data-category');
                    const unit = checkbox.getAttribute('data-unit') || '';

                    addDisposalRow({
                        type: type,
                        code: code,
                        name: name,
                        category: category,
                        unit: unit
                    });
                });
                // 모달 닫기
                disposalModal.modal('hide');
            });

            // 저장 버튼 클릭 시 데이터 서버로 전송
            saveButton.addEventListener('click', function () {
                // 로딩 스피너 표시
                loadingSpinner.style.display = 'flex';

                // 자재와 상품 폐기 리스트 초기화
                let materialsToSave = [];
                let productsToSave = [];

                // 폐기 목록 테이블의 모든 행을 순회
                const rows = disposalList.getElementsByTagName('tr');
                for (let i = 0; i < rows.length; i++) {
                    const row = rows[i];
                    const type = row.getAttribute('data-type');
                    const code = row.getAttribute('data-code');
                    const name = row.cells[2].textContent.trim();
                    const category = row.cells[3].textContent.trim();
                    const amount = parseFloat(row.querySelector('.disposal-amount').value);
                    const date = row.querySelector('.disposal-date').value;

                    // 폐기 양과 날짜 유효성 검사
                    if (isNaN(amount) || !date) {
                        alert('폐기 양과 폐기 날짜를 정확히 입력해주세요.');
                        loadingSpinner.style.display = 'none'; // 로딩 스피너 숨김
                        return;
                    }

                    if (type === '자재') {
                        materialsToSave.push({
                            materialCode: code,
                            materialName: name,
                            category: category,
                            disposalAmount: amount,
                            disposalDate: date
                        });
                    } else if (type === '상품') {
                        productsToSave.push({
                            productCode: code,
                            productName: name,
                            category: category,
                            disposalAmount: amount,
                            disposalDate: date
                        });
                    }
                }

                // 서버로 전송할 데이터 준비
                const dataToSend = {
                    materials: materialsToSave,
                    products: productsToSave
                };

                console.log('저장할 데이터:', dataToSend);

                // AJAX 요청을 통해 데이터 전송
                fetch('/erp/inventory/disposal', { // 실제 저장을 처리할 서버 엔드포인트로 변경 필요
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify(dataToSend)
                })
                    .then(response => {
                        if (response.ok) {
                            alert('폐기 등록이 완료되었습니다.');
                            disposalList.innerHTML = ''; // 테이블 초기화
                            // 로딩 스피너 숨김
                            loadingSpinner.style.display = 'none';
                        } else {
                            throw new Error('네트워크 응답이 올바르지 않습니다.');
                        }
                    })
                    .catch(error => {
                        // 오류 처리
                        console.error('오류:', error);
                        alert('폐기 등록 중 오류가 발생했습니다.');
                        // 로딩 스피너 숨김
                        loadingSpinner.style.display = 'none';
                    });
            });

            // 초기 데이터 삽입
            populateDisposalTables();
        });

        function getTodayDate() {
            const today = new Date();
            const year = today.getFullYear();
            const month = String(today.getMonth() + 1).padStart(2, '0'); // 월은 0부터 시작하므로 +1
            const day = String(today.getDate()).padStart(2, '0');
            return year + '-' + month + '-' + day;
        }
    </script>

</div>

<%@ include file="/WEB-INF/view/erp/layout/erpFooter.jsp" %>

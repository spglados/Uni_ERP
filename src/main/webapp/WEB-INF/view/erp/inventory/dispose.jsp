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

<!-- 재고 관리 콘텐츠 -->
<div class="content container mt-4">
    <h1 class="mb-4">폐기 관리</h1>
    <hr>
    <!-- 자재 목록 테이블 -->
    <div class="shadow p-3 mb-5 bg-white rounded" style="height: 83%; margin-top: 26px;">
        <div class="d-flex justify-content-between">
            <div>
                <h2 id="categoryTitle">전체</h2>
            </div>
            <div class="d-flex justify-content-between">
                <div class="form-group mr-3">
                    <!-- 자재 카테고리 선택 필터 -->
                    <select id="materialCategoryFilter" class="form-control">
                        <option value="">자재 카테고리</option>
                        <option value="냉동품">냉동품</option>
                        <option value="냉장품">냉장품</option>
                        <option value="상온품">상온품</option>
                    </select>
                </div>
                <!-- 상품 카테고리 선택 필터 -->
                <div class="form-group mr-3">
                    <select id="productCategoryFilter" class="form-control">
                        <option value="">상품 카테고리</option>
                        <option value="메인">메인</option>
                        <option value="사이드">사이드</option>
                        <option value="주류">주류</option>
                        <option value="음료">음료</option>
                    </select>
                </div>
                <input id="searchInput" placeholder="자재/상품명 검색" class="form-control mr-2" style="width: 200px;">
            </div>
        </div>
        <hr>
        <!-- 추가 및 저장 버튼을 테이블 내 hr 태그 위에 배치 -->
        <div class="d-flex justify-content-end mb-3">
            <!-- 저장 버튼 추가 -->
            <button id="saveButton" class="btn btn-primary mr-2 align-self-end">저장</button>
            <!-- 추가 버튼 추가 -->
            <button id="addButton" class="btn btn-success align-self-end">추가</button>
        </div>
        <div class="table-container">
            <!-- 폐기 목록 테이블 -->
            <table class="table table-bordered table-striped" id="disposalList">
                <thead class="thead-dark">
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
    <div class="modal fade" id="disposalModal" tabindex="-1" role="dialog" aria-labelledby="disposalModalLabel"
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
                        <!-- 상품 탭 -->
                        <div class="tab-pane fade" id="products" role="tabpanel" aria-labelledby="products-tab">
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
            const materialCategoryFilter = document.getElementById('materialCategoryFilter');
            const productCategoryFilter = document.getElementById('productCategoryFilter');
            const categoryTitle = document.getElementById('categoryTitle');
            const searchInput = document.getElementById('searchInput');
            const addButton = document.getElementById('addButton');
            const saveButton = document.getElementById('saveButton');
            const disposalModal = $('#disposalModal');
            const modalSearchInput = document.getElementById('modalSearchInput');
            const materialsTableBody = document.getElementById('materialsTableBody');
            const productsTableBody = document.getElementById('productsTableBody');
            const confirmAddButton = document.getElementById('confirmAddButton');
            const disposalList = document.getElementById('disposalList').getElementsByTagName('tbody')[0];

            // 선택된 폐기 항목을 저장할 배열
            let selectedDisposals = [];

            // 자재와 상품 목록을 테이블에 삽입하는 함수
            function populateDisposalTables() {
                // 자재 테이블 채우기
                materialDisposalList.forEach(function(material) {
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
                productDisposalList.forEach(function(product) {
                    let row = '<tr>' +
                        '<td>' + product.productCode + '</td>' +
                        '<td>' + product.productName + '</td>' +
                        '<td>' + product.category + '</td>' +
                        '<td><input type="checkbox" class="selectDisposal" data-type="상품" data-code="' + product.productCode + '" data-name="' + product.productName + '" data-category="' + product.category + '"></td>' +
                        '</tr>';
                    productsTableBody.innerHTML += row;
                });
            }

            // 필터링 함수
            function filterDisposals() {
                const materialCategory = materialCategoryFilter.value;
                const productCategory = productCategoryFilter.value;
                const searchKeyword = searchInput.value.toLowerCase();

                // 폐기 목록 테이블의 모든 행을 순회
                const rows = disposalList.getElementsByTagName('tr');
                Array.from(rows).forEach(function(row) {
                    const type = row.getAttribute('data-type');
                    const name = row.cells[2].textContent.trim().toLowerCase();
                    const category = row.cells[3].textContent.trim();

                    let categoryMatch = false;
                    if (type === '자재') {
                        categoryMatch = (materialCategory === '전체') || (category === materialCategory);
                    } else if (type === '상품') {
                        categoryMatch = (productCategory === '전체') || (category === productCategory);
                    }

                    const searchMatch = name.includes(searchKeyword);

                    if (categoryMatch && searchMatch) {
                        row.style.display = '';
                    } else {
                        row.style.display = 'none';
                    }
                });
            }

            // 모달 내 검색 필터링 함수
            function filterModalDisposals() {
                const searchKeyword = modalSearchInput.value.toLowerCase();
                const activeTab = $('#disposalTab .active').attr('id');

                if (activeTab === 'materials-tab') {
                    const rows = materialsTableBody.getElementsByTagName('tr');
                    Array.from(rows).forEach(function(row) {
                        const name = row.cells[1].textContent.trim().toLowerCase();
                        const category = row.cells[2].textContent.trim();
                        const materialCategory = materialCategoryFilter.value;

                        const categoryMatch = (materialCategory === '전체') || (category === materialCategory);
                        const searchMatch = name.includes(searchKeyword);

                        if (categoryMatch && searchMatch) {
                            row.style.display = '';
                        } else {
                            row.style.display = 'none';
                        }
                    });
                } else if (activeTab === 'products-tab') {
                    const rows = productsTableBody.getElementsByTagName('tr');
                    Array.from(rows).forEach(function(row) {
                        const name = row.cells[1].textContent.trim().toLowerCase();
                        const category = row.cells[2].textContent.trim();
                        const productCategory = productCategoryFilter.value;

                        const categoryMatch = (productCategory === '전체') || (category === productCategory);
                        const searchMatch = name.includes(searchKeyword);

                        if (categoryMatch && searchMatch) {
                            row.style.display = '';
                        } else {
                            row.style.display = 'none';
                        }
                    });
                }
            }

            // 모달 내 자재/상품 목록 필터링 이벤트 리스너
            modalSearchInput.addEventListener('input', filterModalDisposals);
            materialCategoryFilter.addEventListener('change', function() {
                // 자재 카테고리 변경 시 모달 자재 탭 필터링
                if ($('#materials-tab').hasClass('active')) {
                    filterModalDisposals();
                }
            });
            productCategoryFilter.addEventListener('change', function() {
                // 상품 카테고리 변경 시 모달 상품 탭 필터링
                if ($('#products-tab').hasClass('active')) {
                    filterModalDisposals();
                }
            });

            // 자재 및 상품 탭 전환 시 필터링 적용
            $('a[data-toggle="tab"]').on('shown.bs.tab', function (e) {
                filterModalDisposals();
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
                    '<input type="date" class="form-control disposal-date" required>' +
                    '</td>' +
                    '<td>' +
                    '<button class="btn btn-danger btn-sm removeDisposalButton">삭제</button>' +
                    '</td>' +
                    '</tr>';
                disposalList.innerHTML += newRow;
            }

            // 추가 버튼 클릭 시 모달 열기
            addButton.addEventListener('click', function() {
                // 모달 열기 전 모든 체크박스 해제
                const checkboxes = document.querySelectorAll('.selectDisposal');
                checkboxes.forEach(function(checkbox) {
                    checkbox.checked = false;
                });
                // 모달 검색 필드 초기화
                modalSearchInput.value = '';
                // 모든 행 보이기
                const materialRows = materialsTableBody.getElementsByTagName('tr');
                Array.from(materialRows).forEach(function(row) {
                    row.style.display = '';
                });
                const productRows = productsTableBody.getElementsByTagName('tr');
                Array.from(productRows).forEach(function(row) {
                    row.style.display = '';
                });
                // 모달 열기
                disposalModal.modal('show');
            });

            // 모달 내 '추가' 버튼 클릭 시 선택된 항목 추가
            confirmAddButton.addEventListener('click', function() {
                // 선택된 자재/상품 찾기
                const selectedCheckboxes = document.querySelectorAll('.selectDisposal:checked');
                selectedCheckboxes.forEach(function(checkbox) {
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

            // 폐기 목록 테이블에서 '삭제' 버튼 클릭 시 해당 행 삭제
            disposalList.addEventListener('click', function(event) {
                if (event.target && event.target.classList.contains('removeDisposalButton')) {
                    const row = event.target.closest('tr');
                    row.remove();
                }
            });

            // 저장 버튼 클릭 시 데이터 서버로 전송
            saveButton.addEventListener('click', function() {
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
                fetch('/erp/saveDisposals', { // 실제 저장을 처리할 서버 엔드포인트로 변경 필요
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify(dataToSend)
                })
                    .then(function(response) {
                        if (!response.ok) {
                            throw new Error('네트워크 응답이 올바르지 않습니다.');
                        }
                        return response.json();
                    })
                    .then(function(data) {
                        // 성공 처리 (예: 알림 표시 및 테이블 초기화)
                        alert('폐기 등록이 완료되었습니다.');
                        disposalList.innerHTML = ''; // 테이블 초기화
                    })
                    .catch(function(error) {
                        // 오류 처리
                        console.error('오류:', error);
                        alert('폐기 등록 중 오류가 발생했습니다.');
                    });
            });

            // 검색 입력 시 필터링 함수
            searchInput.addEventListener('input', filterDisposals);

            // 카테고리 필터 변경 시 필터링 함수
            materialCategoryFilter.addEventListener('change', filterDisposals);
            productCategoryFilter.addEventListener('change', filterDisposals);

            // 초기 데이터 삽입
            populateDisposalTables();
        });
    </script>

</div>

<%@ include file="/WEB-INF/view/erp/layout/erpFooter.jsp" %>

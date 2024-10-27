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
<!-- Font Awesome (integrity 속성 제거) -->
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css"
      crossorigin="anonymous" referrerpolicy="no-referrer"/>

<!-- 재고 관리 콘텐츠 -->
<div class="content">
    <h1>일 재고 관리</h1>
    <hr>
    <!-- 자재 필터 및 저장 버튼 영역 -->
    <div class="d-flex d-flex-row-reverse" style="flex-direction: row-reverse;">
        <!-- 카테고리 선택 필터 -->
        <select id="categoryFilter" class="form-control select" style="margin-right: 30px;">
            <option value="전체">전체</option>
            <option value="냉동품">냉동품</option>
            <option value="냉장품">냉장품</option>
            <option value="상온품">상온품</option>
        </select>
    </div>

    <!-- 자재 목록 테이블 -->
    <div class="shadow p-3 mb-5 bg-white rounded" style="height: 83%; margin-top: 26px;">
        <div class="d-flex justify-content-between">
            <div>
                <h2 id="categoryTitle">전체</h2>
            </div>
            <div class="d-flex justify-content-between">
                <input id="searchInput" placeholder="자재명 검색" class="form-control mr-2" style="width: 200px;">
                <!-- 저장 버튼 추가 -->
                <button id="saveButton" class="btn btn-primary" style="margin-right: 10px;">저장</button>
            </div>
        </div>
        <hr>
        <div class="table-container">
            <!-- 자재 목록 테이블 -->
            <table class="table table-bordered table-striped" id="materialList">
                <thead class="thead-dark">
                <tr>
                    <th>자재코드</th>
                    <th>자재명</th>
                    <th>분류</th>
                    <th>이론 재고</th>
                    <th>실 재고</th>
                    <th>재고 손실</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="material" items="${materialStatusList}" varStatus="status">
                    <tr data-index="${status.index}">
                        <td>${material.materialCode}</td>
                        <td>${material.name}</td>
                        <td>${material.category}</td>
                        <td>${material.theoreticalAmount}&nbsp;${material.unit}</td>
                        <td>
                            <input type="number" value="${material.actualAmount}" class="actualAmountInput">&nbsp;${material.unit}
                        </td>
                        <td>
                            <c:choose>
                                <c:when test="${material.loss >= 0}">
                                    +${material.loss}
                                </c:when>
                                <c:otherwise>
                                    <span style="color: red;">${material.loss}</span>
                                </c:otherwise>
                            </c:choose>
                            &nbsp;${material.unit}
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </div>

    <!-- JavaScript 섹션 -->
    <script>
        // materialStatusList를 JavaScript 배열로 정의
        const materialStatusList = [
            <c:forEach var="material" items="${materialStatusList}" varStatus="status">
                {
                    materialCode: '${material.materialCode}',
                    name: '${material.name}',
                    category: '${material.category}',
                    theoreticalAmount: ${material.theoreticalAmount},
                    unit: '${material.unit}',
                    actualAmount: ${material.actualAmount},
                    loss: ${material.loss}
                }<c:if test="${!status.last}">,</c:if>
            </c:forEach>
        ];

        document.addEventListener('DOMContentLoaded', function () {
            const categoryFilter = document.getElementById('categoryFilter');
            const categoryTitle = document.getElementById('categoryTitle');
            const searchInput = document.getElementById('searchInput');
            const saveButton = document.getElementById('saveButton');

            // 카테고리 선택 시 필터링 및 검색어 비우기
            categoryFilter.addEventListener('change', function () {
                var selectedCategory = categoryFilter.value;
                categoryTitle.textContent = selectedCategory;
                searchInput.value = ''; // 카테고리 변경 시 검색어 비우기
                filterProducts();
            });

            // 엔터 키 입력 시 필터링
            searchInput.addEventListener('keyup', function (event) {
                if (event.key === 'Enter') {
                    filterProducts();
                }
            });

            searchInput.addEventListener('input', function () {
                filterProducts();
            });

            // 상품 필터링 함수 (카테고리와 검색어 모두 적용)
            function filterProducts() {
                var selectedCategory = categoryFilter.value;
                var searchKeyword = searchInput.value.toLowerCase(); // 검색어를 소문자로 변환

                const productList = document.querySelectorAll('#materialList tbody tr');

                productList.forEach(function (row) {
                    var productCategoryCell = row.querySelector('td:nth-child(3)');
                    var productNameCell = row.querySelector('td:nth-child(2)'); // 상품명 있는 열 선택
                    if (productCategoryCell && productNameCell) {
                        var productCategory = productCategoryCell.textContent.trim();
                        var productName = productNameCell.textContent.trim().toLowerCase(); // 상품명 소문자로 변환

                        // 카테고리와 검색어가 모두 일치해야 보이게 함
                        var categoryMatch = selectedCategory === '전체' || productCategory === selectedCategory;
                        var searchMatch = productName.includes(searchKeyword);

                        if (categoryMatch && searchMatch) {
                            row.style.display = ''; // 카테고리와 검색어가 일치하는 경우 보이기
                        } else {
                            row.style.display = 'none'; // 일치하지 않는 경우 숨기기
                        }
                    }
                });
            }

            // actualAmount 입력 필드에 이벤트 리스너 추가하여 materialStatusList 업데이트
            const actualAmountInputs = document.querySelectorAll('.actualAmountInput');

            actualAmountInputs.forEach(function(input) {
                input.addEventListener('change', function() {
                    const row = this.closest('tr');
                    const index = row.getAttribute('data-index');
                    const newValue = parseFloat(this.value);
                    if (!isNaN(newValue)) {
                        materialStatusList[index].actualAmount = newValue;
                    }
                });
            });

            // 저장 버튼 클릭 시 변경된 데이터를 서버로 전송
            saveButton.addEventListener('click', function() {
                // 서버로 전송할 데이터 준비
                const updatedData = materialStatusList.map(material => ({
                    materialCode: material.materialCode,
                    actualAmount: material.actualAmount
                }));

                // AJAX 요청을 통해 데이터 전송
                fetch('/erp/inventory/day-adjustment', { // 실제 저장을 처리할 서버 엔드포인트로 변경 필요
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
                    alert("성공적으로 저장했습니다 !");
                })
                .catch(error => {
                    // 오류 처리
                    console.error('오류:', error);
                    alert('저장 중 오류가 발생했습니다.');
                });
            });
        });
    </script>

</div>

<%@ include file="/WEB-INF/view/erp/layout/erpFooter.jsp" %>

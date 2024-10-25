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
</style>

<!-- Font Awesome (integrity 속성 제거) -->
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css"
      crossorigin="anonymous" referrerpolicy="no-referrer"/>

<!-- 재고 관리 콘텐츠 -->
<div class="content">
    <h1>현황 관리</h1>
    <hr>
    <!-- 자재 필터 및 등록 버튼 영역 -->
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
            </div>
        </div>
        <hr>
        <div class="table-container">
            <!-- 자재 목록 테이블 -->
            <table class="table table-bordered table-striped" id="materialList">
                <thead class="thead-dark">
                <tr>
                    <th>자재명</th>
                    <th>분류</th>
                    <th>이론 재고</th>
                    <th>실 재고</th>
                    <th>재고 손실</th>
                    <th>임박 유통기한</th>
                    <th>마지막 입고</th>
                    <th>사용 상품</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="material" items="${materialStatusList}">
                    <tr>
                        <td>${material.name}</td>
                        <td>${material.category}</td>
                        <td>${material.theoreticalAmount}&nbsp;${material.unit}</td>
                        <td>${material.actualAmount}&nbsp;${material.unit}</td>
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
                        <td>0</td>
                        <td>0</td>
                        <td>0</td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
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
        document.addEventListener('DOMContentLoaded', function () {
            const categoryFilter = document.getElementById('categoryFilter');
            const categoryTitle = document.getElementById('categoryTitle');
            const searchInput = document.getElementById('searchInput');
            const searchButton = document.getElementById('searchButton');

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

                // 올바른 테이블 ID로 수정
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
        });

        function showProducts(materialCode) {
            // 해당 자재의 숨겨진 상품 목록 div를 찾습니다.
            const productDiv = document.getElementById('product-id-' + materialCode);
            if (productDiv) {
                // 상품 목록의 HTML을 가져옵니다.
                const productHTML = productDiv.innerHTML;
                // 모달 내의 product-list에 삽입합니다.
                document.getElementById('product-list').innerHTML = productHTML;
            } else {
                // 상품 목록을 찾지 못한 경우 메시지를 표시합니다.
                document.getElementById('product-list').innerHTML = '<li class="list-group-item">사용하고 있는 상품이 없습니다.</li>';
            }
        }
    </script>

</div>

<%@ include file="/WEB-INF/view/erp/layout/erpFooter.jsp" %>

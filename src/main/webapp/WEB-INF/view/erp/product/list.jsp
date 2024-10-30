<%--
  Created by IntelliJ IDEA.
  User: namch
  Date: 24. 10. 10.
  Time: 오후 6:42
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/view/erp/layout/erpHeader.jsp"%>
<link rel="stylesheet" href="/css/erp/product.css">

<!-- 메인 컨텐츠 -->
<div class="content">
    <h1>상품 목록</h1>
    <hr>
    <!-- 상품 목록 테이블 -->
    <div class="shadow p-3 mb-5 bg-white rounded" style="height: 83%; margin-top: 26px;">
            <div class="d-flex d-flex-row-reverse" style="flex-direction: row-reverse;">
                <!-- 새로운 등록 버튼 (별도의 페이지로 이동) -->
                <div class="refresh-btn-div">
                    <button type="button" class="btn btn-secondary ml-2 btn-action " onclick="resetFilters()">
                        <i class="fas fa-sync-alt"></i>
                    </button>
                </div>
                <a href="/erp/product/registration" class="btn btn-outline-info rounded-pill" style="margin-right: 20px;">등록하러 가기</a>
            </div>
        <hr>
        <div class="table-container">
            <!-- 상품 목록 테이블 -->
            <table class="table table-bordered table-striped"  id="productList">
                <thead class="thead-dark">
                <tr>
                    <th>상품 번호</th>
                    <th>상품명</th>
                    <th>분류</th>
                    <th>가격</th>
                    <th>금일 판매량</th>
                    <th>전일 판매량</th>
                    <th>금월 평균 일 판매량</th>
                    <th>전월 평균 일 판매량</th>
                    <th>금년 평균 일 판매량</th>
                    <th>재료</th>
                </tr>
                </thead>
                <tbody>
                    <c:forEach var = "product" items="${productList}">
                <tr>
                    <td>${product.productCode}</td>
                    <td>${product.name}</td>
                    <td>${product.category}</td>
                    <td>${product.formatToPrice()}원</td>
                    <td>${product.todaySales}개</td>
                    <td>${product.yesterdaySales}개</td>
                    <td>${product.monthSales}개</td>
                    <td>${product.previousMonthSales}개</td>
                    <td>${product.yearSales}개</td>
                    <td><button class="btn btn-sm btn-info" data-toggle="modal" id="modal-btn-${product.id}" data-target="#ingredientModal" onclick="showIngredients(${product.id})">재료 보기</button></td>
                </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </div>

    <!-- 재료 보기 모달 -->
    <div class="modal fade" id="ingredientModal" tabindex="-1" role="dialog" aria-labelledby="ingredientModalLabel" aria-hidden="true">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="ingredientModalLabel">재료 목록</h5>
                    <button type="button" class="close" onclick="saveIngredientModal()" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <div class="modal-body">
                    <!-- 재료 목록이 표시될 영역 -->
                    <ul id="ingredientList"></ul>
                </div>
                <div class="modal-footer">
                    <input type="hidden" id="modalProductId">
                    <button type="button" class="custom-btn" onclick="addIngredient()">추가</button>
                    <button type="button" class="btn btn-secondary close-btn" onclick="saveIngredientModal()">저장</button>
                </div>
            </div>
        </div>
    </div>

    <script>
        // 자재 리스트
        const materialList = ${materialList != null ? materialList : '[]'};
        const materialDTOList = ${materialDTOList != null ? materialDTOList : '[]'};
    </script>

    <c:if test="${not empty productName}">
    <script>
        window.productName = '<c:out value="${productName}" />';
    </script>
    </c:if>

    <script src="/js/erp/product/list.js"></script>

<%@include file="/WEB-INF/view/erp/layout/erpFooter.jsp"%>
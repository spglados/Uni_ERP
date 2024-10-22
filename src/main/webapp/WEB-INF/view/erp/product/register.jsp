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
    <h1>상품 등록</h1>
    <hr>
    <!-- 상품 필터 및 등록 버튼 영역 -->
    <div class="d-flex d-flex-row-reverse" style="flex-direction: row-reverse;">
        <!-- 등록 버튼 (모달 창을 통해 상품 등록) -->
        <button class="btn btn-outline-info rounded-pill" data-toggle="modal" data-target="#registerModal" style="margin-right: 20px;">등록</button>
        <!-- 카테고리 선택 필터 -->
        <select id="categoryFilter" class="form-control select" style="margin-right: 30px;">
            <option value="전체">전체</option>
            <option value="메인">메인</option>
            <option value="사이드">사이드</option>
            <option value="음료">음료</option>
            <option value="주류">주류</option>
        </select>

    </div>

    <!-- 상품 목록 테이블 -->
    <div class="shadow p-3 mb-5 bg-white rounded" style="height: 83%; margin-top: 26px;">
        <div class="d-flex justify-content-between">
            <div>
                <h2 id="categoryTitle">전체</h2>
            </div>
            <div class="d-flex justify-content-between">
                <input id="searchInput" placeholder="상품명 검색" style="margin-right: 10px">
                <h5>2024-10-13</h5>
            </div>
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
                    <th>일일 판매량</th>
                    <th>평균 일일 판매량</th>
                    <th>전월 판매량</th>
                    <th>금월 판매량</th>
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
                    <td>0</td>
                    <td>0</td>
                    <td>0</td>
                    <td>0</td>
                    <td><button class="btn btn-sm btn-info" data-toggle="modal" id="modal-btn-${product.id}" data-target="#ingredientModal" onclick="showIngredients(${product.id})">재료 보기</button></td>
                </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </div>

    <!-- 상품 등록 모달 -->
    <div class="modal fade" id="registerModal" tabindex="-1" role="dialog" aria-labelledby="registerModalLabel" aria-hidden="true">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="registerModalLabel">상품 등록</h5>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <div class="modal-body">
                    <!-- 상품 등록 폼 -->
                    <form id="registerForm" enctype="multipart/form-data">
                        <div class="form-group">
                            <label for="productName">상품명</label>
                            <input type="text" class="form-control" id="productName" name="name" required value="고추장찌개">
                        </div>
                        <div class="form-group">
                            <label for="category">카테고리</label>
                            <select class="form-control" id="category" name="category">
                                <option>메인</option>
                                <option>사이드</option>
                                <option>음료</option>
                                <option>주류</option>
                            </select>
                        </div>
                        <div class="form-group">
                            <label for="price">가격</label>
                            <input type="number" class="form-control" id="price" name="price" required value="8900">
                        </div>

                        <!-- 이미지 업로드 -->
                        <div class="form-group">
                            <label for="image">상품 이미지</label>
                            <input type="file" class="form-control" id="image" accept="image/*" onchange="previewImage(event)">
                        </div>

                        <!-- 이미지 미리보기 -->
                        <div class="form-group">
                            <label>미리보기</label>
                            <img id="imagePreview" style="max-width: 100%; height: auto;" />
                        </div>

                        <!-- 추가 정보 (예: 설명, 재고, 유통기한 등) -->
                        <div class="form-group">
                            <label for="description">상품 설명</label>
                            <textarea class="form-control" id="description" name="description" rows="3"></textarea>
                        </div>
                    </form>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-dismiss="modal">닫기</button>
                    <button type="button" class="btn btn-primary" onclick="registerProduct()">저장</button>
                </div>
            </div>
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

    <script src="/js/erp/product/register.js"></script>

<%@include file="/WEB-INF/view/erp/layout/erpFooter.jsp"%>
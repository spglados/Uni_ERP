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
        <select class="form-control select" style="margin-right: 30px;">
            <option value="">전체</option>
            <option value="">메인</option>
            <option value="">사이드</option>
            <option value="">주류</option>
        </select>
    </div>

    <!-- 상품 목록 테이블 -->
    <div class="shadow p-3 mb-5 bg-white rounded" style="height: 83%; margin-top: 26px;">
        <div class="d-flex justify-content-between">
            <div>
                <h2>전체</h2>
            </div>
            <div class="d-flex justify-content-between">
                <input value="검색" style="margin-right: 10px">
                <h5>2024-10-13</h5>
            </div>
        </div>
        <hr>
        <div class="table-container">
            <!-- 상품 목록 테이블 -->
            <table class="table table-bordered table-striped">
                <thead class="thead-dark">
                <tr>
                    <th>상품 코드</th>
                    <th>상품명</th>
                    <th>카테고리</th>
                    <th>가격</th>
                    <th>일일 판매량</th>
                    <th>평균 일일 판매량</th>
                    <th>전월 판매량</th>
                    <th>금월 판매량</th>
                    <th>재료</th>
                </tr>
                </thead>
                <tbody>
                <!-- TODO: 서버에서 상품 데이터를 받아와야 함 -->
                <tr>
                    <td>001</td>
                    <td>김치찌개</td>
                    <td>메인</td>
                    <td>₩9,900</td>
                    <td>50</td>
                    <td>35</td>
                    <td>300</td>
                    <td>157</td>
                    <td><button class="btn btn-sm btn-info" data-toggle="modal" data-target="#ingredientModal" onclick="showIngredients('김치찌개')">재료 보기</button></td>
                </tr>
                <tr>
                    <td>002</td>
                    <td>된장찌개</td>
                    <td>메인</td>
                    <td>₩8,500</td>
                    <td>40</td>
                    <td>30</td>
                    <td>280</td>
                    <td>145</td>
                    <td><button class="btn btn-sm btn-info" data-toggle="modal" data-target="#ingredientModal" onclick="showIngredients('된장찌개')">재료 보기</button></td>
                </tr>
                <tr>
                    <td>002</td>
                    <td>된장찌개</td>
                    <td>메인</td>
                    <td>₩8,500</td>
                    <td>40</td>
                    <td>30</td>
                    <td>280</td>
                    <td>145</td>
                    <td><button class="btn btn-sm btn-info" data-toggle="modal" data-target="#ingredientModal" onclick="showIngredients('된장찌개')">재료 보기</button></td>
                </tr>
                <tr>
                    <td>002</td>
                    <td>된장찌개</td>
                    <td>메인</td>
                    <td>₩8,500</td>
                    <td>40</td>
                    <td>30</td>
                    <td>280</td>
                    <td>145</td>
                    <td><button class="btn btn-sm btn-info" data-toggle="modal" data-target="#ingredientModal" onclick="showIngredients('된장찌개')">재료 보기</button></td>
                </tr>
                <tr>
                    <td>002</td>
                    <td>된장찌개</td>
                    <td>메인</td>
                    <td>₩8,500</td>
                    <td>40</td>
                    <td>30</td>
                    <td>280</td>
                    <td>145</td>
                    <td><button class="btn btn-sm btn-info" data-toggle="modal" data-target="#ingredientModal" onclick="showIngredients('된장찌개')">재료 보기</button></td>
                </tr>
                <tr>
                    <td>002</td>
                    <td>된장찌개</td>
                    <td>메인</td>
                    <td>₩8,500</td>
                    <td>40</td>
                    <td>30</td>
                    <td>280</td>
                    <td>145</td>
                    <td><button class="btn btn-sm btn-info" data-toggle="modal" data-target="#ingredientModal" onclick="showIngredients('된장찌개')">재료 보기</button></td>
                </tr>
                <tr>
                    <td>002</td>
                    <td>된장찌개</td>
                    <td>메인</td>
                    <td>₩8,500</td>
                    <td>40</td>
                    <td>30</td>
                    <td>280</td>
                    <td>145</td>
                    <td><button class="btn btn-sm btn-info" data-toggle="modal" data-target="#ingredientModal" onclick="showIngredients('된장찌개')">재료 보기</button></td>
                </tr>
                <tr>
                    <td>002</td>
                    <td>된장찌개</td>
                    <td>메인</td>
                    <td>₩8,500</td>
                    <td>40</td>
                    <td>30</td>
                    <td>280</td>
                    <td>145</td>
                    <td><button class="btn btn-sm btn-info" data-toggle="modal" data-target="#ingredientModal" onclick="showIngredients('된장찌개')">재료 보기</button></td>
                </tr>
                <tr>
                    <td>002</td>
                    <td>된장찌개</td>
                    <td>메인</td>
                    <td>₩8,500</td>
                    <td>40</td>
                    <td>30</td>
                    <td>280</td>
                    <td>145</td>
                    <td><button class="btn btn-sm btn-info" data-toggle="modal" data-target="#ingredientModal" onclick="showIngredients('된장찌개')">재료 보기</button></td>
                </tr>
                <tr>
                    <td>002</td>
                    <td>된장찌개</td>
                    <td>메인</td>
                    <td>₩8,500</td>
                    <td>40</td>
                    <td>30</td>
                    <td>280</td>
                    <td>145</td>
                    <td><button class="btn btn-sm btn-info" data-toggle="modal" data-target="#ingredientModal" onclick="showIngredients('된장찌개')">재료 보기</button></td>
                </tr>
                <!-- 추가적인 상품 행들 -->
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
                    <form id="registerForm">
                        <div class="form-group">
                            <label for="productCode">상품 코드</label>
                            <input type="text" class="form-control" id="productCode" required>
                        </div>
                        <div class="form-group">
                            <label for="productName">상품명</label>
                            <input type="text" class="form-control" id="productName" required>
                        </div>
                        <div class="form-group">
                            <label for="category">카테고리</label>
                            <select class="form-control" id="category">
                                <option>메인</option>
                                <option>사이드</option>
                                <option>주류</option>
                            </select>
                        </div>
                        <div class="form-group">
                            <label for="price">가격</label>
                            <input type="text" class="form-control" id="price" required>
                        </div>
                        <div class="form-group">
                            <label for="dailySales">일일 판매량</label>
                            <input type="number" class="form-control" id="dailySales">
                        </div>
                        <div class="form-group">
                            <label for="avgDailySales">평균 일일 판매량</label>
                            <input type="number" class="form-control" id="avgDailySales">
                        </div>
                        <div class="form-group">
                            <label for="lastMonthSales">전월 판매량</label>
                            <input type="number" class="form-control" id="lastMonthSales">
                        </div>
                        <div class="form-group">
                            <label for="currentMonthSales">금월 판매량</label>
                            <input type="number" class="form-control" id="currentMonthSales">
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
                    <button type="button" class="close" onclick="closeIngredientModal()" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <div class="modal-body">
                    <!-- 재료 목록이 표시될 영역 -->
                    <ul id="ingredientList">
                        <!-- TODO: 서버에서 선택된 상품의 재료 데이터를 받아와야 함 -->
                    </ul>
                </div>
                <div class="modal-footer">
                    <button type="button" class="custom-btn" onclick="showAddIngredientModal()">추가</button>
                    <button type="button" class="btn btn-secondary close-btn" onclick="closeIngredientModal()">닫기</button>
                </div>
            </div>
        </div>
    </div>

    <!-- 재료 추가 모달 -->
    <div class="modal fade" id="addIngredientModal" tabindex="-1" role="dialog" aria-labelledby="addIngredientModalLabel" aria-hidden="true">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="addIngredientModalLabel">재료 추가하기</h5>
                    <button type="button" class="close" onclick="closeAddIngredientModal()" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <div class="modal-body">
                    <!-- 재료 추가 폼 -->
                    <div class="form-group">
                        <label for="newIngredientName">재료명</label>
                        <input type="text" id="newIngredientName" class="form-control">
                    </div>
                    <div class="form-group">
                        <label for="newIngredientAmount">양</label>
                        <input type="number" id="newIngredientAmount" class="form-control">
                    </div>
                    <div class="form-group">
                        <label for="newIngredientUnit">단위</label>
                        <select id="newIngredientUnit" class="form-control">
                            <option value="g">g</option>
                            <option value="kg">kg</option>
                            <option value="ml">ml</option>
                            <option value="L">L</option>
                            <option value="EA">EA</option>
                            <option value="box">box</option>
                        </select>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="custom-btn" onclick="addIngredient()">추가</button>
                    <button type="button" class="btn btn-secondary close-btn" onclick="closeAddIngredientModal()">닫기</button>
                </div>
            </div>
        </div>
    </div>

    <script>
        // 재료 보기 모달을 열 때 선택된 상품의 재료 정보를 표시하는 함수
        function showIngredients(productName) {
            // TODO: 서버에서 선택된 상품의 재료 데이터를 받아와야 함
            const ingredients = {
                '김치찌개': [
                    { name: '김치', amount: '200', unit: 'g' },
                    { name: '돼지고기', amount: '150', unit: 'g' },
                    { name: '두부', amount: '100', unit: 'g' },
                    { name: '고춧가루', amount: '10', unit: 'g' },
                    { name: '마늘', amount: '5', unit: 'g' }
                ],
                '된장찌개': [
                    { name: '된장', amount: '50', unit: 'g' },
                    { name: '두부', amount: '100', unit: 'g' },
                    { name: '애호박', amount: '80', unit: 'g' },
                    { name: '양파', amount: '50', unit: 'g' },
                    { name: '버섯', amount: '70', unit: 'g' }
                ]
                // 추가적인 상품과 그 재료들
            };
            const ingredientList = document.getElementById('ingredientList');
            ingredientList.innerHTML = '';
            if (ingredients[productName]) {
                ingredients[productName].forEach(function(ingredient, index) {
                    const li = document.createElement('li');
                    li.innerHTML =
                        '<div class="ingredient-item" id="ingredient-' + index + '">' +
                        '<input type="text" class="ingredient-name form-control d-inline-block" value="' + ingredient.name + '" disabled>' +
                        '<input type="number" class="ingredient-amount form-control d-inline-block" value="' + ingredient.amount + '" disabled>' +
                        '<select class="ingredient-unit form-control d-inline-block" disabled>' +
                        '<option value="g" ' + (ingredient.unit == 'g' ? 'selected' : '') + '>g</option>' +
                        '<option value="kg" ' + (ingredient.unit == 'kg' ? 'selected' : '') + '>kg</option>' +
                        '<option value="ml" ' + (ingredient.unit == 'ml' ? 'selected' : '') + '>ml</option>' +
                        '<option value="L" ' + (ingredient.unit == 'L' ? 'selected' : '') + '>L</option>' +
                        '<option value="EA" ' + (ingredient.unit == 'EA' ? 'selected' : '') + '>EA</option>' +
                        '<option value="box" ' + (ingredient.unit == 'box' ? 'selected' : '') + '>box</option>' +
                        '</select>' +
                        '<button class="custom-btn edit-btn" onclick="editIngredient(' + index + ')">수정</button>' +
                        '<button class="custom-btn delete-btn" onclick="deleteIngredient(' + index + ')">삭제</button>' +
                        '</div>';
                    ingredientList.appendChild(li);
                });
            }
        }

        // 재료 추가 모달을 표시하는 함수
        function showAddIngredientModal() {
            document.getElementById('addIngredientModal').classList.add('show');
            document.getElementById('addIngredientModal').style.display = 'block';
        }

        // 재료 추가 모달을 닫는 함수
        function closeAddIngredientModal() {
            document.getElementById('addIngredientModal').classList.remove('show');
            document.getElementById('addIngredientModal').style.display = 'none';
        }

        // 재료 보기 모달을 닫는 함수
        function closeIngredientModal() {
            let unsavedChanges = false;
            document.querySelectorAll('.ingredient-item').forEach(function(item) {
                const editButton = item.querySelector('.edit-btn');
                if (editButton && editButton.textContent === '저장') {
                    unsavedChanges = true;
                }
            });

            if (unsavedChanges) {
                const confirmSave = confirm("수정된 정보가 있습니다. 저장하시겠습니까?");
                if (!confirmSave) {
                    document.querySelectorAll('.ingredient-item input, .ingredient-item select').forEach(function(input) {
                        input.disabled = true;
                    });
                    document.querySelectorAll('.ingredient-item .edit-btn').forEach(function(button) {
                        button.textContent = '수정';
                        button.classList.remove('custom-btn-success');
                        button.classList.add('custom-btn-warning');
                    });
                }
            }
            document.getElementById('ingredientModal').classList.remove('show');
            document.getElementById('ingredientModal').style.display = 'none';
            document.body.classList.remove('modal-open');
            const modalBackdrop = document.querySelector('.modal-backdrop');
            if (modalBackdrop) {
                modalBackdrop.parentNode.removeChild(modalBackdrop);
            }
        }

        // 재료를 추가하는 함수
        function addIngredient() {
            const name = document.getElementById('newIngredientName').value;
            const amount = document.getElementById('newIngredientAmount').value;
            const unit = document.getElementById('newIngredientUnit').value;

            const ingredientList = document.getElementById('ingredientList');
            const index = ingredientList.children.length;

            const li = document.createElement('li');
            li.innerHTML =
                '<div class="ingredient-item" id="ingredient-' + index + '">' +
                '<input type="text" class="ingredient-name form-control d-inline-block" value="' + name + '" disabled>' +
                '<input type="number" class="ingredient-amount form-control d-inline-block" value="' + amount + '" disabled>' +
                '<select class="ingredient-unit form-control d-inline-block" disabled>' +
                '<option value="g" ' + (unit == 'g' ? 'selected' : '') + '>g</option>' +
                '<option value="kg" ' + (unit == 'kg' ? 'selected' : '') + '>kg</option>' +
                '<option value="ml" ' + (unit == 'ml' ? 'selected' : '') + '>ml</option>' +
                '<option value="L" ' + (unit == 'L' ? 'selected' : '') + '>L</option>' +
                '<option value="EA" ' + (unit == 'EA' ? 'selected' : '') + '>EA</option>' +
                '<option value="box" ' + (unit == 'box' ? 'selected' : '') + '>box</option>' +
                '</select>' +
                '<button class="custom-btn edit-btn" onclick="editIngredient(' + index + ')">수정</button>' +
                '<button class="custom-btn delete-btn" onclick="deleteIngredient(' + index + ')">삭제</button>' +
                '</div>';
            ingredientList.appendChild(li);

            closeAddIngredientModal();
        }

        // 재료 정보를 수정하는 함수
        function editIngredient(index) {
            const ingredientItem = document.getElementById('ingredient-' + index);
            const inputs = ingredientItem.querySelectorAll('input, select');
            const editButton = ingredientItem.querySelector('.edit-btn');

            if (editButton.textContent === '수정') {
                inputs.forEach(function(input) { input.disabled = false; });
                editButton.textContent = '저장';
                editButton.classList.remove('custom-btn-warning');
                editButton.classList.add('custom-btn-success');
            } else {
                inputs.forEach(function(input) { input.disabled = true; });
                editButton.textContent = '수정';
                editButton.classList.remove('custom-btn-success');
                editButton.classList.add('custom-btn-warning');
            }
        }

        // 재료를 삭제하는 함수
        function deleteIngredient(index) {
            const confirmDelete = confirm("정말 삭제하시겠습니까?");
            if (confirmDelete) {
                const ingredientItem = document.getElementById('ingredient-' + index);
                ingredientItem.parentNode.parentNode.removeChild(ingredientItem.parentNode);
            }
        }
    </script>

<%@include file="/WEB-INF/view/erp/layout/erpFooter.jsp"%>
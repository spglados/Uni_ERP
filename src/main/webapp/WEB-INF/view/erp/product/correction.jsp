<%--
  Created by IntelliJ IDEA.
  User: namch
  Date: 24. 10. 10.
  Time: 오후 6:42
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/view/erp/layout/erpHeader.jsp" %>
<link rel="stylesheet" href="/css/erp/product.css">

<!-- 메인 컨텐츠 -->
<div class="content container-fluid">
    <h1>상품 수정</h1>
    <hr>

    <!-- 검색 섹션 -->
    <div class="search-section d-flex mb-4">
        <input type="text" id="searchProductCode" class="form-control mr-2" placeholder="상품 코드를 입력하세요" />
        <button class="btn btn-primary" onclick="searchProduct()">검색</button>
    </div>

    <!-- 상품 수정 폼 -->
    <div class="shadow p-3 mb-5 bg-white rounded">
        <form id="editForm" enctype="multipart/form-data">
            <div class="form-group">
                <label for="productCode">상품 코드</label>
                <input type="text" class="form-control" id="productCode" name="productCode" readonly />
            </div>
            <div class="form-group">
                <label for="productName">상품명</label>
                <input type="text" class="form-control" id="productName" name="name" required />
            </div>
            <div class="form-group">
                <label for="category">카테고리</label>
                <select class="form-control" id="category" name="category" required>
                    <option value="메인">메인</option>
                    <option value="사이드">사이드</option>
                    <option value="음료">음료</option>
                    <option value="주류">주류</option>
                </select>
            </div>
            <div class="form-group">
                <label for="price">가격</label>
                <input type="number" class="form-control" id="price" name="price" required />
            </div>

            <!-- 이미지 업로드 -->
            <div class="form-group">
                <label for="image">상품 이미지</label>
                <input type="file" class="form-control" id="image" name="image" accept="image/*" onchange="previewImage(event)" />
            </div>

            <!-- 이미지 미리보기 -->
            <div class="form-group">
                <label>미리보기</label>
                <img id="imagePreview" style="max-width: 100%; height: auto;" />
            </div>

            <!-- 추가 정보 (예: 설명) -->
            <div class="form-group">
                <label for="description">상품 설명</label>
                <textarea class="form-control" id="description" name="description" rows="3"></textarea>
            </div>

            <button type="button" class="btn btn-primary" onclick="updateProduct()">수정</button>
            <a href="/erp/product/list" class="btn btn-secondary">취소</a>
        </form>
    </div>
</div>

<script>
    // 이미지 미리보기 기능
    function previewImage(event) {
        const reader = new FileReader();
        reader.onload = function () {
            const output = document.getElementById('imagePreview');
            output.src = reader.result;
        };
        reader.readAsDataURL(event.target.files[0]);
    }

    // 상품 검색 함수
    function searchProduct() {
        const productCodeInput = document.getElementById('searchProductCode');
        const productCode = productCodeInput.value.trim();
        if (!productCode) {
            alert('상품 코드를 입력해주세요.');
            return;
        }

        fetch('/erp/product/products/' + productCode)
            .then(response => {
                if (response.status === 404) {
                    throw new Error('해당 상품을 찾을 수 없습니다.');
                }
                return response.json();
            })
            .then(data => {
                populateForm(data);
            })
            .catch(error => {
                alert(error.message);
                clearForm();
            });
    }

    // 폼에 데이터 채우기
    function populateForm(product) {
        document.getElementById('productCode').value = product.productCode;
        document.getElementById('productName').value = product.name;
        document.getElementById('category').value = product.category;
        document.getElementById('price').value = product.price;
        document.getElementById('description').value = product.description;

        if (product.image) {
            // 백틱 제거 및 문자열 결합 사용
            document.getElementById('imagePreview').src = 'data:image/jpeg;base64,' + product.image;
        } else {
            document.getElementById('imagePreview').src = '';
        }
    }

    // 폼 초기화
    function clearForm() {
        document.getElementById('editForm').reset();
        document.getElementById('imagePreview').src = '';
    }

    // 상품 수정 함수
    function updateProduct() {
        const form = document.getElementById('editForm');
        if (!form.checkValidity()) {
            form.reportValidity();
            return;
        }

        const formData = new FormData(form);
        const productCode = formData.get('productCode');

        fetch('/erp/product/products/' + productCode, {
            method: 'PUT',
            body: formData
        })
            .then(response => {
                if (response.ok) {
                    alert('상품이 성공적으로 수정되었습니다.');
                    window.location.href = '/erp/product/list';
                } else {
                    return response.text().then(text => { throw new Error(text || '상품 수정에 실패했습니다.'); });
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert('오류가 발생했습니다: ' + error.message); // 문자열 결합 사용
            });
    }
</script>

<%@ include file="/WEB-INF/view/erp/layout/erpFooter.jsp" %>

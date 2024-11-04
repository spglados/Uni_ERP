<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/view/erp/layout/erpHeader.jsp" %>
<link rel="stylesheet" href="/css/erp/product.css">
<style>
    .inner-search {
        margin-right: 10px;
    }
    .btn-main {
        background-color: #F8F399;
        border-color: #F8F399;
        color: #000;
    }
    .form-container {
        position: relative;
        padding: 20px;
    }
    .image-preview-container {
        position: absolute;
        top: 20px;
        right: 20px;
        width: 200px;
        height: 200px;
        border: 1px solid #ddd;
        display: flex;
        align-items: center;
        justify-content: center;
        overflow: hidden;
        background-color: #f9f9f9;
    }
    .image-preview-container img {
        max-width: 100%;
        max-height: 100%;
    }
    .form-section {
        display: flex;
        flex-wrap: wrap;
        gap: 20px;
        flex-direction: column;
    }
    .form-group {
        flex: 1 1 45%;
        min-width: 200px;
        display: flex;
        flex-direction: column;
    }
    .full-width {
        flex: 1 1 100%;
    }
    .button-group {
        display: flex;
        gap: 10px;
        margin-top: 20px;
    }
    @media (max-width: 768px) {
        .image-preview-container {
            position: static;
            margin-bottom: 20px;
        }
        .form-section {
            flex-direction: column;
        }
    }
    input, select {
        max-width: 70%;
    }
</style>

<!-- 메인 컨텐츠 -->
<div class="content container-fluid">
    <h1>상품 수정</h1>
    <hr>
    <!-- 검색 섹션 -->
    <div class="search-section d-flex mb-4 flex-row-reverse">
        <div class="inner-search">
            <button class="btn btn-main" onclick="searchProduct()">검색</button>
        </div>
        <div class="inner-search">
            <input type="text" id="searchProductCode" class="form-control" style="max-width: 100%" placeholder="상품 코드를 입력하세요" />
        </div>
    </div>

    <!-- 상품 수정 폼 -->
    <div class="shadow p-4 mb-5 bg-white rounded form-container">
        <!-- 이미지 미리보기 -->
        <div class="image-preview-container">
            <img id="imagePreview" src="" alt="이미지 미리보기" />
        </div>

        <form id="editForm" enctype="multipart/form-data">
            <div class="form-section">
                <!-- 상품 코드 -->
                <div class="form-group">
                    <label for="productCode">상품 코드</label>
                    <input type="text" class="form-control" id="productCode" name="productCode" readonly />
                </div>

                <!-- 상품명 -->
                <div class="form-group">
                    <label for="productName">상품명</label>
                    <input type="text" class="form-control" id="productName" name="name" required readonly/>
                </div>

                <!-- 카테고리 -->
                <div class="form-group">
                    <label for="category">카테고리</label>
                    <select class="form-control" id="category" name="category" required disabled>
                        <option value="메인">메인</option>
                        <option value="사이드">사이드</option>
                        <option value="음료">음료</option>
                        <option value="주류">주류</option>
                    </select>
                </div>

                <!-- 가격 -->
                <div class="form-group">
                    <label for="price">가격</label>
                    <input type="number" class="form-control" id="price" name="price" required readonly/>
                </div>

                <!-- 상품 설명 -->
                <div class="form-group full-width">
                    <label for="description">상품 설명</label>
                    <textarea class="form-control" id="description" name="description" rows="3" readonly></textarea>
                </div>

                <!-- 이미지 업로드 -->
                <div class="form-group full-width">
                    <label for="image">상품 이미지 (JPG만 가능)</label>
                    <input type="file" class="form-control-file" id="image" name="image" accept=".jpg, .jpeg" onchange="previewImage(event)" disabled />
                    <small class="form-text text-muted">이미지는 JPG 형식만 업로드할 수 있습니다.</small>
                </div>
            </div>

            <!-- 버튼 그룹 -->
            <div class="button-group">
                <button type="button" id="update-btn" class="btn btn-main" onclick="updateProduct()" disabled>수정</button>
                <a href="/erp/product/list" class="btn btn-secondary">취소</a>
            </div>
        </form>
    </div>
</div>

<script>
    // 이미지 미리보기 기능
    function previewImage(event) {
        const file = event.target.files[0];
        const allowedTypes = ['image/jpeg', 'image/jpg'];

        if (file) {
            if (!allowedTypes.includes(file.type)) {
                alert('JPG 파일만 업로드할 수 있습니다.');
                event.target.value = ''; // 파일 입력 초기화
                document.getElementById('imagePreview').src = '';
                return;
            }

            const reader = new FileReader();
            reader.onload = function () {
                const output = document.getElementById('imagePreview');
                output.src = reader.result;
            };
            reader.readAsDataURL(file);
        } else {
            document.getElementById('imagePreview').src = '';
        }
    }

    // Enter 키로 검색 트리거
    document.getElementById("searchProductCode").addEventListener("keyup", function(event) {
        if (event.key === "Enter") {
            searchProduct();
        }
    });

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
                editable();
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
            document.getElementById('imagePreview').src = 'data:image/jpeg;base64,' + product.image;
        } else {
            document.getElementById('imagePreview').src = '';
        }
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
        let productName = document.getElementById('productName').value;
        fetch('/erp/product/products/' + productCode, {
            method: 'PUT',
            body: formData
        })
            .then(response => {
                if (response.ok) {
                    alert('상품이 성공적으로 수정되었습니다.');
                    window.location.href = '/erp/product/list/' + productName;
                } else {
                    return response.text().then(text => { throw new Error(text || '상품 수정에 실패했습니다.'); });
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert('오류가 발생했습니다: ' + error.message);
            });
    }

    // 필드를 편집 가능하게 설정
    function editable() {
        // 필드들에 readonly 속성 해제
        document.getElementById("productName").readOnly = false;
        document.getElementById("price").readOnly = false;
        document.getElementById("description").readOnly = false;

        // disabled 속성 해제
        document.getElementById("category").disabled = false;
        document.getElementById("image").disabled = false;
        document.getElementById("update-btn").disabled = false;
    }

</script>

<%@ include file="/WEB-INF/view/erp/layout/erpFooter.jsp" %>

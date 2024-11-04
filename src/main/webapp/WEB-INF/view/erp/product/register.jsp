<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/view/erp/layout/erpHeader.jsp" %>
<link rel="stylesheet" href="/css/erp/product.css">
<style>
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
        width: 300px;
        height: 300px;
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
        flex-direction: column;
        gap: 20px;
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
    input, select, textarea {
        max-width: 60%;
    }
</style>

<!-- 메인 컨텐츠 -->
<div class="content container-fluid">
    <h1>상품 등록</h1>
    <hr>

    <!-- 상품 등록 폼 -->
    <div class="shadow p-4 mb-5 bg-white rounded form-container">
        <!-- 이미지 미리보기 -->
        <div class="image-preview-container">
            <img id="imagePreview" src="" alt="이미지 미리보기" />
        </div>

        <form id="registerForm" enctype="multipart/form-data">
            <div class="form-section">
                <!-- 상품명 -->
                <div class="form-group">
                    <label for="productName">상품명</label>
                    <input type="text" class="form-control" id="productName" name="name" required placeholder="상품명을 입력하세요" />
                </div>

                <!-- 카테고리 -->
                <div class="form-group">
                    <label for="category">카테고리</label>
                    <select class="form-control" id="category" name="category" required>
                        <option value="" disabled selected>카테고리를 선택하세요</option>
                        <option value="메인">메인</option>
                        <option value="사이드">사이드</option>
                        <option value="음료">음료</option>
                        <option value="주류">주류</option>
                    </select>
                </div>

                <!-- 가격 -->
                <div class="form-group">
                    <label for="price">가격</label>
                    <input type="number" class="form-control" id="price" name="price" required min="0" placeholder="가격을 입력하세요" />
                </div>

                <!-- 상품 설명 -->
                <div class="form-group">
                    <label for="description">상품 설명</label>
                    <textarea class="form-control" id="description" name="description" rows="3" placeholder="상품 설명을 입력하세요"></textarea>
                </div>

                <!-- 이미지 업로드 -->
                <div class="form-group full-width">
                    <label for="image">상품 이미지 (JPG만 가능)</label>
                    <input type="file" class="form-control-file" id="image" name="image" accept=".jpg, .jpeg" onchange="previewImage(event)" />
                    <small class="form-text text-muted">이미지는 JPG 형식만 업로드할 수 있습니다.</small>
                </div>
            </div>

            <!-- 버튼 그룹 -->
            <div class="button-group">
                <button type="button" class="btn btn-main" onclick="registerProduct()">저장</button>
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

    // 상품 등록 함수
    function registerProduct() {
        const form = document.getElementById('registerForm');
        if (!form.checkValidity()) {
            form.reportValidity();
            return;
        }

        const formData = new FormData(form);

        // 이미지 파일 유효성 검사
        const imageFile = document.getElementById('image').files[0];
        if (imageFile) {
            const allowedTypes = ['image/jpeg', 'image/jpg'];
            if (!allowedTypes.includes(imageFile.type)) {
                alert('JPG 파일만 업로드할 수 있습니다.');
                return;
            }
        }

        let productName = document.getElementById('productName').value;

        fetch('/erp/product/product', {
            method: 'POST',
            body: formData
        })
            .then(response => response.json()) // 서버 응답을 JSON으로 변환
            .then(data => {
                if (data.success) {
                    alert("상품이 등록되었습니다! \n\n\t 재료를 등록해야 재고가 관리됩니다!");
                    window.location.href = '/erp/product/list/' + productName; // 등록 후 상품 목록 페이지로 이동
                } else if (data.fail === false) {
                    alert("같은 이름의 상품이 존재합니다.");
                } else {
                    alert("상품 등록에 실패했습니다.");
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert("상품 등록 중 오류가 발생했습니다.\n" + error);
            });
    }
</script>

<%@ include file="/WEB-INF/view/erp/layout/erpFooter.jsp" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/view/erp/layout/erpHeader.jsp" %>
<link rel="stylesheet" href="/css/erp/product.css">

<!-- 메인 컨텐츠 -->
<div class="content">
    <h1>상품 등록</h1>
    <hr>

    <!-- 상품 등록 폼 -->
    <div class="shadow p-3 mb-5 bg-white rounded" style="margin-top: 26px;">
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
                <img id="imagePreview" style="max-width: 100%; height: auto;"/>
            </div>

            <!-- 추가 정보 (예: 설명, 재고, 유통기한 등) -->
            <div class="form-group">
                <label for="description">상품 설명</label>
                <textarea class="form-control" id="description" name="description" rows="3"></textarea>
            </div>

            <button type="button" class="btn btn-primary" onclick="registerProduct()">저장</button>
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

    // 상품 등록 함수
    function registerProduct() {
        const form = $('#registerForm')[0];
        let productName = document.getElementById('productName').value;
        if (form.checkValidity()) {
            const formData = new FormData(form);

            // Blob 이미지 데이터를 FormData에 추가
            const imageFile = $('#image')[0].files[0];

            if (imageFile) {
                formData.append('image', imageFile);
            }

            // 여기서 서버로 formData를 전송하는 로직을 구현
            fetch('/erp/product/product', {
                method: 'POST',
                body: formData
            }).then(response => response.json()) // JSON으로 변환
                .then(data => {
                    if (data.success) {
                        alert("상품이 등록되었습니다! \n\n\t 재료를 등록해야 재고가 관리됩니다 !");
                        window.location.href = '/erp/product/list/' + productName; // 등록 후 상품 목록 페이지로 이동
                    } else if (!data.fail) {
                        alert("같은 이름의 상품이 존재합니다.");
                    } else {
                        alert("상품 등록에 실패했습니다.");
                    }
                }).catch(error => {
                    console.error('Error:', error);
                    alert("상품 등록 중 오류가 발생했습니다." + error);
                });
        } else {
            // 유효성 검사가 실패한 경우 경고창을 띄우고, 유효성 검사를 강제로 실행
            form.reportValidity();
        }
    }
</script>

<%@ include file="/WEB-INF/view/erp/layout/erpFooter.jsp" %>

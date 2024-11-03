<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">

<main class="container mt-5">
    <h1 class="text-center">가게 등록</h1>
    <div class="profile-info">
        <form id="storeForm" onsubmit="registerStore(event)" class="needs-validation" novalidate>
            <div class="form-group">
                <label for="name">가게 이름:</label>
                <input type="text" id="name" name="name" class="form-control" required />
                <div class="invalid-feedback">가게 이름을 입력해주세요.</div>
            </div>

            <div class="form-group">
                <label for="is24Hours">24시간 운영:</label>
                <select id="is24Hours" name="is24Hours" class="form-control">
                    <option value="1">예</option>
                    <option value="0">아니오</option>
                </select>
            </div>

            <div class="form-group">
                <label for="isOpen">가게 상태:</label>
                <select id="isOpen" name="isOpen" class="form-control">
                    <option value="1">열림</option>
                    <option value="0">닫힘</option>
                </select>
            </div>

            <div class="form-group">
                <label for="address">가게 주소:</label>
                <input type="hidden" id="storeAddress">
                <input type="text" id="address" name="storeAddress" class="form-control" onclick="execDaumPostcode()" required />
                <input type="text" id="storeDetailAddress" class="form-control mt-2" placeholder="상세 주소"/>
            </div>

            <button type="submit" class="btn btn-primary btn-block">등록</button>
        </form>
    </div>
</main>

<script src="//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>
<script src="//dapi.kakao.com/v2/maps/sdk.js?appkey=3aea5e049cf7a27eae091e77ca0e1429&libraries=services"></script>
<script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.5.4/dist/umd/popper.min.js"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
<script>
    function registerStore(event) {
        event.preventDefault(); // 기본 폼 제출 방지

        const formData = new FormData(event.target);
        const storeData = {}; // 여기서 정의

        formData.forEach((value, key) => {
            storeData[key] = value.trim(); // 값의 앞뒤 공백 제거
        });

        // 주소 결합
        const address = storeData.storeAddress; // 선택한 주소
        const detailAddress = document.getElementById("storeDetailAddress").value.trim();
        storeData.storeAddress = address + ' , '+detailAddress ;

        storeData.is24Hours = parseInt(storeData.is24Hours, 10);
        storeData.isOpen = parseInt(storeData.isOpen, 10);

        console.log('JSON Data:', JSON.stringify(storeData)); // 전체 JSON 데이터 로그

        // 요청 보내기
        fetch('/my-page/stores', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(storeData),
        })
        .then(response => {
            if (!response.ok) {
                throw new Error('가게 등록에 실패했습니다.');
            }
            return response.text(); // 응답을 텍스트로 받기
        })
        .then(message => {
            alert(message); // 성공 메시지 표시
            window.opener.location.reload();
            window.close();
        })
        .catch(error => {
            alert(error.message); // 에러 메시지 출력
        });
    }

    function execDaumPostcode() {
        new daum.Postcode({
            oncomplete: function(data) {
                var addr = data.address;
                document.getElementById("address").value = addr; // 선택한 주소를 newAddress 필드에 넣기
            }
        }).open();
    }

    // 부트스트랩 유효성 검사
    (function () {
        'use strict';
        window.addEventListener('load', function () {
            var forms = document.getElementsByClassName('needs-validation');
            var validation = Array.prototype.filter.call(forms, function (form) {
                form.addEventListener('submit', function (event) {
                    if (form.checkValidity() === false) {
                        event.preventDefault();
                        event.stopPropagation();
                    }
                    form.classList.add('was-validated');
                }, false);
            });
        }, false);
    })();
</script>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <title>금고 보관</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.9.2/dist/umd/popper.min.js"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
</head>
<body>
<div class="container mt-4">
    <h1 class="mb-4">POS 현재 금액</h1>
    <p>현재 금액: <strong>${posNowAmount}</strong> 원</p>

    <h2 class="mt-4">입금할 금액</h2>
    <div class="form-group">
        <input type="text" id="amount" class="form-control" placeholder="입금할 금액을 입력하세요" />
    </div>
    <button class="btn btn-primary" onclick="submitAmount()">확인</button>
</div>

<script>
    function submitAmount() {
        const amount = document.getElementById("amount").value;
        const requestData = { amount: amount };

        fetch('/erp/pos/deposit', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(requestData)
        })
            .then(response => {
                if (response.ok) {
                    alert('입금이 완료되었습니다.');
                    window.close(); // 창 닫기
                } else {
                    alert('입금 실패.');
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert('입금 중 오류가 발생했습니다.');
            });
    }
</script>
</body>
</html>

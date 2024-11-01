<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>금고 보관</title>
</head>
<body>
    <h1>POS 현재 금액</h1>
    <p>현재 금액: ${posNowAmount}</p>

    <h2>입금할 금액</h2>
    <input type="text" id="amount" placeholder="입금할 금액을 입력하세요" />
    <button onclick="submitAmount()">확인</button>
</body>
</html>
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

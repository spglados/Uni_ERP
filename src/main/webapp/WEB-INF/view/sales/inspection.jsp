<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>시재 점검 페이지</title>
    <script>
        const posNowAmount = ${posNowAmount}; // JSP에서 전달받은 금액

        function checkAmount() {
            const inputAmount = parseInt(document.getElementById("amountInput").value);
            const closeButton = document.getElementById("closeButton");

            if (inputAmount === posNowAmount) {
                closeButton.disabled = false; // 버튼 활성화
            } else {
                closeButton.disabled = true; // 버튼 비활성화
            }
        }

        async function submitInspection() {
            const inputAmount = document.getElementById("amountInput").value;

            const response = await fetch('/inspection', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ amount: inputAmount }) // 전송할 데이터
            });

            const message = await response.text();

            if (response.ok) {
                alert(message); // 성공 메시지
                window.close(); // 창 닫기
            } else {
                alert(message); // 실패 메시지
            }
        }
    </script>
</head>
<body>
    <h1>시재 점검 페이지</h1>
    <p>포스 현재 금액: <span>${posNowAmount}</span> 원</p>
    <label for="amountInput">금액 입력:</label>
    <input id="amountInput" oninput="checkAmount()">

    <button id="closeButton" disabled onclick="submitInspection()">창 닫기</button>
</body>
</html>

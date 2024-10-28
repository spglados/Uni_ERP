<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>금고 관리 페이지</title>
    <script>
        const posNowAmount = ${posNowAmount}; // JSP에서 전달받은 금액

        function checkAmount() {
            const inputAmount = parseInt(document.getElementById("amountInput").value);
            const confirmButton = document.getElementById("confirmButton");

            if (inputAmount === posNowAmount) {
                confirmButton.disabled = false; // 버튼 활성화
            } else {
                confirmButton.disabled = true; // 버튼 비활성화
            }
        }

        async function submitInspection() {
            const inputAmount = document.getElementById("amountInput").value;

            const response = await fetch('/safe2', { // 기존 경로 유지
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ amount: inputAmount }) // 전송할 데이터
            });

            const message = await response.text();

            if (response.ok) {
                document.getElementById("withdrawSection").style.display = "block"; // 출금 입력란 표시
            } else {
                alert(message); // 실패 메시지
            }
        }

        async function withdrawAmount() {
            const withdrawInput = document.getElementById("withdrawInput").value;
            const response = await fetch('/withdraw2', { // 출금 처리 경로 설정
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ amount: withdrawInput }) // 출금 금액 전송
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
    <h1>금고 관리 페이지</h1>
    <p>포스 현재 금액: <span>${posNowAmount}</span> 원</p>
    <label for="amountInput">금액 입력:</label>
    <input id="amountInput" oninput="checkAmount()">

    <button id="confirmButton" disabled onclick="submitInspection()">확인</button>

    <div id="withdrawSection" style="display:none; margin-top: 20px;">
        <h2>중도 출금</h2>
        <label for="withdrawInput">출금할 금액:</label>
        <input id="withdrawInput" type="number" min="0">
        <button onclick="withdrawAmount()">출금하기</button>
    </div>
</body>
</html>

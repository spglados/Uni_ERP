<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>금고 관리 페이지</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.9.2/dist/umd/popper.min.js"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
    <script>
        const posNowAmount = ${posNowAmount}; // JSP에서 전달받은 금액

        function calculateTotal() {
            const amounts = [
                { value: 50000, id: 'count50000' },
                { value: 10000, id: 'count10000' },
                { value: 5000, id: 'count5000' },
                { value: 1000, id: 'count1000' },
                { value: 500, id: 'count500' },
                { value: 100, id: 'count100' },
                { value: 50, id: 'count50' },
                { value: 10, id: 'count10' }
            ];

            let total = 0;

            amounts.forEach(({ value, id }) => {
                const count = parseInt(document.getElementById(id).value) || 0;
                total += value * count;
            });

            document.getElementById("totalAmount").innerText = total;
            checkAmount(total);
        }

        function checkAmount(total) {
            const confirmButton = document.getElementById("confirmButton");
            confirmButton.disabled = total !== posNowAmount;
        }

        async function submitInspection() {
            const totalAmount = document.getElementById("totalAmount").innerText;
            const response = await fetch('/erp/pos/safe', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ amount: totalAmount })
            });

            const message = await response.text();

            if (response.ok) {
                document.getElementById("withdrawSection").style.display = "block";
            } else {
                alert(message);
            }
        }

        async function withdrawAmount() {
            const withdrawInput = document.getElementById("withdrawInput").value;
            const response = await fetch('/erp/pos/withdraw', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ amount: withdrawInput })
            });

            const message = await response.text();

            if (response.ok) {
                alert(message);
                window.close();
            } else {
                alert(message);
            }
        }
    </script>
</head>
<body>
<div class="container mt-4">
    <h1 class="mb-4">금고 관리 페이지</h1>
    <p>포스 현재 금액: <span>${posNowAmount}</span> 원</p>

    <div class="form-group">
        <label for="count50000">5만원:</label>
        <input id="count50000" type="number" min="0" value="0" class="form-control" oninput="calculateTotal()"><br>
        <label for="count10000">1만원:</label>
        <input id="count10000" type="number" min="0" value="0" class="form-control" oninput="calculateTotal()"><br>
        <label for="count5000">5천원:</label>
        <input id="count5000" type="number" min="0" value="0" class="form-control" oninput="calculateTotal()"><br>
        <label for="count1000">1천원:</label>
        <input id="count1000" type="number" min="0" value="0" class="form-control" oninput="calculateTotal()"><br>
        <label for="count500">500원:</label>
        <input id="count500" type="number" min="0" value="0" class="form-control" oninput="calculateTotal()"><br>
        <label for="count100">100원:</label>
        <input id="count100" type="number" min="0" value="0" class="form-control" oninput="calculateTotal()"><br>
        <label for="count50">50원:</label>
        <input id="count50" type="number" min="0" value="0" class="form-control" oninput="calculateTotal()"><br>
        <label for="count10">10원:</label>
        <input id="count10" type="number" min="0" value="0" class="form-control" oninput="calculateTotal()"><br>
    </div>

    <h2>총합 금액: <span id="totalAmount">0</span> 원</h2>

    <button id="confirmButton" disabled class="btn btn-primary" onclick="submitInspection()">확인</button>

    <div id="withdrawSection" style="display:none; margin-top: 20px;">
        <h2>중도 출금</h2>
        <div class="form-group">
            <label for="withdrawInput">출금할 금액:</label>
            <input id="withdrawInput" type="number" min="0" class="form-control">
        </div>
        <button class="btn btn-danger" onclick="withdrawAmount()">출금하기</button>
    </div>
</div>
</body>
</html>

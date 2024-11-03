<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>시재 점검 페이지</title>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
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
            const closeButton = document.getElementById("closeButton");
            closeButton.disabled = total !== posNowAmount;
        }

        async function submitInspection() {
            const totalAmount = document.getElementById("totalAmount").innerText;

            const response = await fetch('/erp/pos/inspection', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ totalAmount })
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
<body class="bg-light">
<div class="container mt-5">
    <h1 class="text-center">시재 점검 페이지</h1>
    <p class="text-center">포스 현재 금액: <span>${posNowAmount}</span> 원</p>

    <div class="form-group">
        <label for="count50000">5만원:</label>
        <input id="count50000" class="form-control" type="number" min="0" value="0" oninput="calculateTotal()">
    </div>
    <div class="form-group">
        <label for="count10000">1만원:</label>
        <input id="count10000" class="form-control" type="number" min="0" value="0" oninput="calculateTotal()">
    </div>
    <div class="form-group">
        <label for="count5000">5천원:</label>
        <input id="count5000" class="form-control" type="number" min="0" value="0" oninput="calculateTotal()">
    </div>
    <div class="form-group">
        <label for="count1000">1천원:</label>
        <input id="count1000" class="form-control" type="number" min="0" value="0" oninput="calculateTotal()">
    </div>
    <div class="form-group">
        <label for="count500">500원:</label>
        <input id="count500" class="form-control" type="number" min="0" value="0" oninput="calculateTotal()">
    </div>
    <div class="form-group">
        <label for="count100">100원:</label>
        <input id="count100" class="form-control" type="number" min="0" value="0" oninput="calculateTotal()">
    </div>
    <div class="form-group">
        <label for="count50">50원:</label>
        <input id="count50" class="form-control" type="number" min="0" value="0" oninput="calculateTotal()">
    </div>
    <div class="form-group">
        <label for="count10">10원:</label>
        <input id="count10" class="form-control" type="number" min="0" value="0" oninput="calculateTotal()">
    </div>

    <h2 class="text-center">총합 금액: <span id="totalAmount">0</span> 원</h2>
    <button id="closeButton" class="btn btn-primary btn-block" disabled onclick="submitInspection()">창 닫기</button>
</div>

<script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.9.3/dist/umd/popper.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
</body>
</html>

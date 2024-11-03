<!DOCTYPE html>
<html lang="en">
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<head>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.9.2/dist/umd/popper.min.js"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
</head>

<body>
<div class="container mt-4">
    <div id="inspectionSection" class="border p-4">
        <h3>포스 현재 금액: <span>${posNowAmount}</span> 원</h3>
        <div class="mb-3">
            <label for="count50000">5만원:</label>
            <input id="count50000" type="number" class="form-control" min="0" value="0" oninput="calculateTotal()">
        </div>
        <div class="mb-3">
            <label for="count10000">1만원:</label>
            <input id="count10000" type="number" class="form-control" min="0" value="0" oninput="calculateTotal()">
        </div>
        <div class="mb-3">
            <label for="count5000">5천원:</label>
            <input id="count5000" type="number" class="form-control" min="0" value="0" oninput="calculateTotal()">
        </div>
        <div class="mb-3">
            <label for="count1000">1천원:</label>
            <input id="count1000" type="number" class="form-control" min="0" value="0" oninput="calculateTotal()">
        </div>
        <div class="mb-3">
            <label for="count500">500원:</label>
            <input id="count500" type="number" class="form-control" min="0" value="0" oninput="calculateTotal()">
        </div>
        <div class="mb-3">
            <label for="count100">100원:</label>
            <input id="count100" type="number" class="form-control" min="0" value="0" oninput="calculateTotal()">
        </div>
        <div class="mb-3">
            <label for="count50">50원:</label>
            <input id="count50" type="number" class="form-control" min="0" value="0" oninput="calculateTotal()">
        </div>
        <div class="mb-3">
            <label for="count10">10원:</label>
            <input id="count10" type="number" class="form-control" min="0" value="0" oninput="calculateTotal()">
        </div>
        <h2>총합 금액: <span id="totalAmount">0</span> 원</h2>
        <button id="confirmButton" class="btn btn-primary mt-3" disabled onclick="submitInspection()">확인</button>
    </div>

    <div id="withdrawSection" class="border p-4 mt-4" style="display:none;">
        <h2>중도 출금</h2>
        <div class="mb-3">
            <label for="withdrawInput">출금할 금액:</label>
            <input id="withdrawInput" type="number" class="form-control" min="0">
        </div>
        <button class="btn btn-success" onclick="withdrawAmount()">출금하기</button>
    </div>
</div>

<script>
    const posNowAmount = ${posNowAmount}; // JSP에서 전달받은 금액
    function calculateTotal() {
        // 각 금액 단위와 그 갯수를 매핑
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
            const count = parseInt(document.getElementById(id).value) || 0; // 갯수 입력
            total += value * count; // 총합 계산
        });

        document.getElementById("totalAmount").innerText = total; // 총합을 화면에 표시
        checkAmount(total); // 금액 검증
    }

    function checkAmount(total) {
        const confirmButton = document.getElementById("confirmButton");

        // 현재 금액과 비교하여 버튼 활성화/비활성화
        confirmButton.disabled = total !== posNowAmount; // posNowAmount는 전역 변수로 설정되어 있어야 합니다.
    }

    async function submitInspection() {
        const totalAmount = document.getElementById("totalAmount").innerText; // 총합 금액
        const response = await fetch('/erp/pos/safe', { // 기존 경로 유지
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ amount: totalAmount }) // 전송할 데이터
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

        // First, send the withdrawal request
        const response = await fetch('/erp/pos/withdraw', { // 출금 처리 경로 설정
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ amount: withdrawInput }) // 출금 금액 전송
        });

        const message = await response.text();

        if (response.ok) {
            alert(message); // 성공 메시지
            // Now, send a request to close the business
            const closeResponse = await fetch('/erp/pos/close', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                }
            });

            const closeMessage = await closeResponse.text();

            if (closeResponse.ok) {
                alert(closeMessage); // Business closed successfully
                window.opener.location.reload();
                window.close(); // 창 닫기
            } else {
                alert(closeMessage); // 실패 메시지
                window.close(); // 창 닫기
            }
        } else {
            alert(message); // 실패 메시지
        }
    }
</script>
</body>
</html>

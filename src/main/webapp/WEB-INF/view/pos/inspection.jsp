<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>시재 점검 페이지</title>
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
           const closeButton = document.getElementById("closeButton");

           // 현재 금액과 비교하여 버튼 활성화/비활성화
           closeButton.disabled = total !== posNowAmount; // posNowAmount는 전역 변수로 설정되어 있어야 합니다.
       }

       async function submitInspection() {
           const totalAmount = document.getElementById("totalAmount").innerText; // 총합 금액
           const amounts = [50000, 10000, 5000, 1000, 500, 100, 50, 10];
           const counts = {};

           // 각 금액 단위의 입력 갯수를 counts 객체에 저장
           //amounts.forEach(amount => {
           //    counts[amount] = parseInt(document.getElementById(`count${amount}`).value) || 0;
           //});

           // POST 요청 보내기
           const response = await fetch('/erp/pos/inspection', {
               method: 'POST',
               headers: {
                   'Content-Type': 'application/json'
               },
               body: JSON.stringify({
                   totalAmount // 총합 금액

               })
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

    <div>
        <label for="count50000">5만원:</label>
        <input id="count50000" type="number" min="0" value="0" oninput="calculateTotal()"><br>
        <label for="count10000">1만원:</label>
        <input id="count10000" type="number" min="0" value="0" oninput="calculateTotal()"><br>
        <label for="count5000">5천원:</label>
        <input id="count5000" type="number" min="0" value="0" oninput="calculateTotal()"><br>
        <label for="count1000">1천원:</label>
        <input id="count1000" type="number" min="0" value="0" oninput="calculateTotal()"><br>
        <label for="count500">500원:</label>
        <input id="count500" type="number" min="0" value="0" oninput="calculateTotal()"><br>
        <label for="count100">100원:</label>
        <input id="count100" type="number" min="0" value="0" oninput="calculateTotal()"><br>
        <label for="count50">50원:</label>
        <input id="count50" type="number" min="0" value="0" oninput="calculateTotal()"><br>
        <label for="count10">10원:</label>
        <input id="count10" type="number" min="0" value="0" oninput="calculateTotal()"><br>
    </div>

    <h2>총합 금액: <span id="totalAmount">0</span> 원</h2>

    <button id="closeButton" disabled onclick="submitInspection()">시재 점검하기</button>
</body>
</html>

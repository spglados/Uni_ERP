<!DOCTYPE html>
<html lang="ko">
<head>
    <%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

    <title>UNI-POS</title>
    <style>
        body {
            font-family: 'Arial', sans-serif;
            margin: 0;
            padding: 0;
            background-color: #f8f9fa;
        }
        .header {
            background-color: #fff;
            padding: 20px;
            border-bottom: 1px solid #e0e0e0;
            display: flex;
            justify-content: space-between;
            align-items: center;
        }
        .header h1 {
            font-size: 24px;
            font-weight: bold;
        }
        .tabs {
            display: flex;
            gap: 10px;
        }
        .tab-button {
            background-color: #e0e0e0;
            padding: 10px 20px;
            border-radius: 5px;
            font-size: 18px;
            border: none;
            cursor: pointer;
        }
        .tab-button.active {
            background-color: #4a90e2;
            color: white;
        }

        .container {
            display: flex;
            padding: 20px;
            gap: 20px;
        }
        .menu-section {
            flex: 2;
            background-color: #fff;
            border: 1px solid #ddd;
            padding: 20px;
            border-radius: 10px;
        }
        .menu-grid {
            display: grid;
            grid-template-columns: repeat(4, 1fr);
            gap: 15px;
        }
        .menu-item button {
            background-color: #f5f5f5;
            padding: 20px;
            font-size: 16px;
            border: 1px solid #ccc;
            border-radius: 5px;
            cursor: pointer;
            width: 100%;
            height: 130px;
        }
        .menu-item button:active {
            transform: scale(0.95);
            background-color: #3a78d8; /* 클릭 시 색상 변경 */
            transition: transform 0.1s ease;
        }

        .menu-item button:hover {
            background-color: #629fe9; /* 호버 시 색상 변경 */
            box-shadow: 0px 4px 8px rgba(0, 0, 0, 0.2);
        }


        .order-section {
            flex: 1;
            background-color: #fff;
            border: 1px solid #ddd;
            padding: 20px;
            border-radius: 10px;
        }
        .order-summary {
            background-color: #f5f5f5;
            padding: 20px;
            margin-bottom: 20px;
            border-radius: 5px;
        }
        .total {
            font-size: 18px;
            font-weight: bold;
        }
        .payment-button {
            background-color: #4a90e2;
            color: white;
            padding: 15px;
            border: none;
            font-size: 18px;
            cursor: pointer;
            width: 100%;
            border-radius: 5px;
        }

        /* 옵션 버튼 */
        .order-options {
            display: flex;
            justify-content: space-between;
            margin-bottom: 20px;
        }
        .option-button {
            background-color: #ddd;
            padding: 10px;
            border-radius: 5px;
            border: none;
            cursor: pointer;
        }
        .option-button.active {
            background-color: #4a90e2;
            color: white;
        }

        /* 반응형 디자인 */
        @media screen and (max-width: 768px) {
            .container {
                flex-direction: column;
            }

            .menu-grid {
                grid-template-columns: repeat(2, 1fr);
            }

            .order-section {
                margin-top: 20px;
            }
        }

        @media screen and (max-width: 480px) {
            .menu-grid {
                grid-template-columns: repeat(1, 1fr);
            }

            .tab-button {
                padding: 10px;
                font-size: 14px;
            }

            .payment-button {
                font-size: 16px;
            }
        }

    </style>
</head>
<body>
<div class="header">
    <img src="../images/logo/logoPos.png" alt="포스로고" style="height: 100px; width: 125px;">
    <h1>UNI-POS SYSTEM(가상)</h1>
</div>

<div class="container">
    <div class="menu-section">
        <h2>주문 목록</h2>
        <div class="menu-grid">
            <div class="menu-item">
                <button class="add-to-order" data-item="HOT 아메리카노" data-price="4000">HOT 아메리카노<br>4,000원</button>
            </div>
            <div class="menu-item">
                <button class="add-to-order" data-item="ICE 아메리카노" data-price="4500">ICE 아메리카노<br>4,500원</button>
            </div>
            <div class="menu-item">
                <button class="add-to-order" data-item="카야 토스트" data-price="5000">카야 토스트<br>5,000원</button>
            </div>
            <div class="menu-item">
                <button class="add-to-order" data-item="바닐라라떼" data-price="5000">바닐라라떼<br>5,000원</button>
            </div>
        </div>
    </div>


    <div class="order-section">
            <form action="${pageContext.request.contextPath}/pos/payment" method="post">
            <h3>결제 목록</h3>
            <button type="button" id="clear-order">전체삭제</button>
            <div class="order-summary" id="orderList">
                <!-- 주문 항목들이 여기에 추가됨 -->
                <p class="total">총 결제금액: 0원</p>
            </div>
            <button type="submit" class="payment-button" >결제 버튼(총 금액: 0원)</button>
        </form>
    </div>
</div>
<script>
    let totalAmount = 0;

    // 메뉴 항목 추가
    $('.add-to-order').on('click', function() {
        const item = $(this).data('item');  // 메뉴 이름
        const price = parseInt($(this).data('price'));  // 가격

        // 주문 항목이 이미 있는지 확인
        const existingOrder = $('#orderList').find('p[data-item="' + item + '"]');
        if (existingOrder.length > 0) {
            // 기존 항목의 수량을 증가시킴
            const quantity = parseInt(existingOrder.data('quantity')) + 1;
            existingOrder.data('quantity', quantity);
            existingOrder.text(item + ' ×' + quantity + ' - ' + (price * quantity).toLocaleString() + '원');
        } else {
            // 새 항목 추가
            $('#orderList').append('<p data-item="' + item + '" data-quantity="1">' + item + ' ×1 - ' + price.toLocaleString() + '원</p>');
        }

        // 총합 계산
        totalAmount += price;
        $('.total').text('총 결제금액: ' + totalAmount.toLocaleString() + '원');
        $('.payment-button').text('결제 버튼(총 금액: ' + totalAmount.toLocaleString() + '원)');
    });

    // 전체 주문 삭제
    $('#clear-order').on('click', function() {
        $('#orderList').html('<p class="total">총 결제금액: 0원</p>');
        totalAmount = 0;
        $('.payment-button').text('결제 버튼(총 금액: 0원)');
    });
</script>
</body>
</html>
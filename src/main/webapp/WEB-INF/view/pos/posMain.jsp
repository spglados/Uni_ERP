<!DOCTYPE html>
<html lang="ko">
<head>
    <%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>카페 선불형 주문 시스템</title>
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
        }
        .menu-item.hot button {
            background-color: #4a90e2;
            color: white;
        }
        .menu-item.green button {
            background-color: #6abf69;
            color: white;
        }
        .menu-item.yellow button {
            background-color: #f8c471;
            color: white;
        }
        .menu-item.red button {
            background-color: #e74c3c;
            color: white;
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
    <h1>카페-선불형</h1>
    <div class="tabs">
        <button class="tab-button active">주문 홈</button>
        <button class="tab-button">현황</button>
    </div>
</div>

<div class="container">
    <div class="menu-section">
        <h2>즐겨찾는 메뉴</h2>
        <div class="menu-grid">
            <div class="menu-item hot">
                <button>HOT 아메리카노<br>4,000원</button>
            </div>
            <div class="menu-item">
                <button>ICE 아메리카노<br>4,500원</button>
            </div>
            <div class="menu-item green">
                <button>카야 토스트<br>5,000원</button>
            </div>
            <div class="menu-item">
                <button>바닐라라떼<br>5,000원</button>
            </div>
            <div class="menu-item">
                <button>헤이즐넛라떼<br>5,000원</button>
            </div>
            <div class="menu-item">
                <button>초코라떼<br>5,500원</button>
            </div>
            <div class="menu-item yellow">
                <button>카모마일티<br>5,000원</button>
            </div>
            <div class="menu-item red">
                <button>커피 믹스<br>6,000원</button>
            </div>
        </div>
    </div>

    <div class="order-section">
        <h3>포장</h3>
        <div class="order-options">
            <button class="option-button active">포장</button>
            <button class="option-button">매장</button>
        </div>
        <div class="order-summary">
            <p>카페라떼 ×1</p>
            <p>다크초콜릿 ×2</p>
            <p>사과청 ×2</p>
            <p class="total">총합계: 29,000원</p>
        </div>
        <button class="payment-button">결제 (29,000원)</button>
    </div>
</div>
</body>
</html>

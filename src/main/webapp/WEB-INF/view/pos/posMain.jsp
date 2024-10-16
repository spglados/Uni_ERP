<!DOCTYPE html>
<html lang="ko">
<head>
    <%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <style>
        /* 기존 CSS 그대로 유지 */
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
    <img src="/images/logo/logoPos.png" alt="포스로고" style="height: 100px; width: 125px;">
    <h1>UNI-POS SYSTEM(가상)</h1>
</div>

<div class="container">
    <div class="menu-section">
        <h2>주문 목록</h2>
        <div class="menu-grid">
            <!-- Product 리스트를 반복하여 동적으로 버튼 생성 -->
            <c:forEach var="product" items="${productList}">
                <div class="menu-item">
                    <button class="add-to-order" data-item="${product.name}" data-price="${product.price}" data-store-id="${storeId}">
                            ${product.name}<br>
                        <script>
                            // JavaScript로 가격에 천 단위 구분 기호 추가
                            document.write(new Intl.NumberFormat().format(${product.price}) + '원');
                        </script>
                    </button>
                </div>
            </c:forEach>
        </div>
    </div>

    <div class="order-section">
        <form id="product-submit">
            <h3>결제 목록</h3>
            <button type="button">전체삭제</button>
            <div class="order-summary" id="orderList">
                <p class="total">총 결제금액: 0원</p>
            </div>
            <button type="submit" class="payment-button">결제 버튼(총 금액: 0원)</button>
        </form>
    </div>
</div>

</body>
</html>
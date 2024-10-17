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
                    <button class="add-to-order" data-item="${product.name}" data-price="${product.price}" data-id="${product.id}">
                            ${product.name}<br>
                        <span class="product-price">${product.price}원</span>
                    </button>
                </div>
            </c:forEach>
        </div>

        <div class="pagination">
            <c:if test="${productList.hasPrevious()}">
                <a href="/erp/pos/main?page=${currentPage - 1}&size=${pageSize}">&laquo; 이전</a>
            </c:if>
            <c:forEach begin="1" end="${notices.totalPages}" var="i">
                <a href="/erp/pos/main?page=${i - 1}&size=${pageSize}" class="${i == currentPage ? 'active' : ''}">${i}</a>
            </c:forEach>
            <c:if test="${notices.hasNext()}">
                <a href="/erp/pos/main?page=${currentPage}&size=${pageSize}">다음 &raquo;</a>
            </c:if>
        </div>

    </div>

    <div class="order-section">
        <form id="product-submit">
            <h3>결제 목록</h3>
            <button type="button" id="clear-order">전체삭제</button>
            <div class="order-summary" id="orderList">
            </div>
            <button type="submit" class="payment-button">결제 버튼(총 금액: 0원)</button>
        </form>
    </div>
</div>

<script>
    let orderList = [];
    let totalAmount = 0;

    function updateOrderSummary() {
        const orderSummary = document.getElementById('orderList');
        const paymentButton = document.querySelector('.payment-button');  // 함수가 호출될 때마다 요소를 다시 찾기
        orderSummary.innerHTML = '';  // 기존 내용을 지움

        // 주문 목록을 화면에 추가
        orderList.forEach(function(item) {
            const orderItem = document.createElement('p');
            orderItem.textContent = item.name + ' - ' + item.price.toLocaleString() + '원 x ' + item.quantity;
            orderSummary.appendChild(orderItem);
        });


        // 결제 버튼의 텍스트 업데이트
        if (paymentButton) {
            paymentButton.textContent = '결제 버튼(총 금액: ' + totalAmount.toLocaleString() + '원)';
        } else {
            console.error('paymentButton이 존재하지 않습니다.');
        }

        console.log(totalAmount);
        console.log(orderList);
    }


    document.querySelectorAll('.add-to-order').forEach(function(button) {
        button.addEventListener('click', function() {
            const itemName = this.getAttribute('data-item');
            const itemId = this.getAttribute('data-id');
            const itemPrice = parseInt(this.getAttribute('data-price'));

            // 이미 주문 목록에 있는 항목인지 확인
            const existingItem = orderList.find(function(item) {
                return item.name === itemName;
            });

            if (existingItem) {
                // 이미 있는 항목이면 수량과 총 금액 증가
                existingItem.quantity += 1;
            } else {
                // 새로운 항목이면 목록에 추가
                orderList.push({ name: itemName, price: itemPrice, quantity: 1, productId: itemId});
            }

            totalAmount += itemPrice;  // 총 금액 업데이트
            updateOrderSummary();
        });
    });

    document.getElementById('clear-order').addEventListener('click', function() {
        orderList = [];
        totalAmount = 0;
        updateOrderSummary();
    });

    document.getElementById('product-submit').addEventListener('submit', function(event) {
        event.preventDefault();

        if (orderList.length === 0) {
            alert('주문 항목이 없습니다.');
            return;
        }

        const orderData = {
            items: orderList,
            totalAmount: totalAmount
        };
        console.log(orderList);
        console.log(totalAmount);

        fetch('/erp/pos/payment', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(orderData)
        })
            .then(response => {
                if (response.status === 200) {
                    alert('총 ' + totalAmount.toLocaleString() + '원이 결제되었습니다.');
                    orderList = [];
                    totalAmount = 0;
                    updateOrderSummary();
                    window.location.href = "/erp/pos/main"
                } else {
                    alert('결제에 실패했습니다. 다시 시도해주세요.');
                }
            })
            .catch(function(error) {
                alert('오류가 발생했습니다. 다시 시도해주세요.');
            });
    });
</script>

</body>
</html>

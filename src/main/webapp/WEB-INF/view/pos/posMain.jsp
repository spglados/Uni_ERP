<!DOCTYPE html>
<html lang="ko">
<head>
    <%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/animate.css/4.1.1/animate.min.css"/>
    <style>
        /* 기존 CSS 그대로 유지 */
        body {
            font-family: 'yg-jalnan', sans-serif;
            margin: 0;
            padding: 0;
            background-color: #f8f9fa;
        }

        @font-face {
            font-family: 'yg-jalnan';
            src: url('https://fastly.jsdelivr.net/gh/projectnoonnu/noonfonts_four@1.2/JalnanOTF00.woff') format('woff');
            font-weight: normal;
            font-style: normal;
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
        .add-to-order{
            font-family: 'yg-jalnan', sans-serif;
        }
        .total {
            font-size: 18px;
            font-weight: bold;
        }
        .payment-button {
            font-family: 'yg-jalnan', sans-serif;
            font-weight: 300;
            background-color: #4a90e2;
            color: white;
            padding: 15px;
            border: none;
            font-size: 18px;
            cursor: pointer;
            width: 100%;
            border-radius: 5px;
        }

        .payment-button:hover {
            animation: colorChange 1s infinite; /* 호버 시 애니메이션 추가 */
        }

        @keyframes colorChange {
            0% {
                color: white;
                font-size: 18px;
            }
            50% {
                color: orange; /* 중간색으로 변경 */
                font-size: 19px;
            }
            100% {
                color: white; /* 다시 원래 색으로 돌아옴 */
                font-size: 18px;
            }
        }

        .{
            font-family: 'yg-jalnan', sans-serif;
            font-weight: 100;
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

        /* 결제 목록 글자 정렬 */
        .order-section form h3{
            text-align: center;
        }

        .menu-section h1{
            text-align: center;
        }


        .pagination {
            display: flex;
            justify-content: center;
            gap: 10px;
        }

        .pagination a {
            padding: 10px 15px;
            text-decoration: none;
            color: #007bff;
            border: 1px solid #ddd;
            border-radius: 4px;
            background-color: #fff;
        }

        .pagination a.active {
            font-weight: bold;
            background-color: #007bff;
            color: #fff;
            border-color: #007bff;
        }

        .pagination a:hover {
            background-color: #f2f2f2;
        }

    </style>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<div class="header">
    <img src="/images/logo/logoPos.png" class="animate__animated animate__fadeIn" alt="포스로고" style="height: 100px; width: 125px;">
    <h1 class="animate__animated animate__fadeIn">UNI-POS SYSTEM ( 가상 )</h1>
</div>

<div class="container">
    <div class="menu-section">
        <h1>주문 목록</h1>
        <div class="menu-grid">
            <!-- Product 리스트를 반복하여 동적으로 버튼 생성 -->
            <c:forEach var="product" items="${productList.content}">
                <div class="menu-item">
                    <button class="add-to-order" data-item="${product.name}" data-price="${product.price}" data-code="${product.productCode}">
                            ${product.name}<br>
                        <span class="product-price">${product.price}원</span>
                    </button>
                </div>
            </c:forEach>
        </div>
        <br>

        <div class="pagination">
            <c:if test="${productList.hasPrevious()}">
                <a href="/erp/pos/main?category=${category}&page=${currentPage - 1}&size=${pageSize}">&laquo;</a>
            </c:if>
            <c:if test="${productList.hasNext()}">
                <a href="/erp/pos/main?category=${category}&page=${currentPage + 1}&size=${pageSize}">&raquo;</a>
            </c:if>
        </div>
    </div>

<div class="order-section">
    <form id="product-submit">
        <h3>결제 목록</h3>
        <button type="button" id="clear-order" class="clear-order-button">전체삭제</button>
        <button type="button" id="previous-order" class="previous-order-button btn btn-primary" data-bs-toggle="modal" data-bs-target="#previousOrderModal">
                주문 조회
        </button>
        <div class="order-summary" id="orderList">
            목록에 아무것도 들어있지 않습니다.
        </div>
        <div class="payment-method">
            <label for="payment-type">결제 방법:</label>
            <select id="payment-type" name="payment-type">
                <option value="card">카드</option>
                <option value="cash">현금</option>
            </select>
        </div>
        <button type="submit" class="payment-button">결제 버튼(총 금액: 0원)</button>
    </form>
</div>
<div class="modal fade" id="previousOrderModal" tabindex="-1" aria-labelledby="previousOrderModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="previousOrderModalLabel">주문 조회</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
                <div id="previousOrderList">
                    <!-- Dynamically populate this with previous orders -->
                    <c:if test="${not empty previousOrders}">
                        <c:forEach items="${previousOrders}" var="order">
                            <div>
                                <strong>주문 번호:</strong> ${order.orderNum}<br>
                                <strong>판매 일자:</strong> ${order.salesDate}<br>
                                <strong>총 가격:</strong> ${order.totalPrice} 원
                                <button type="button" class="btn btn-link" onclick="fetchOrderDetails(${order.orderNum})">
                                    상세 보기
                                </button>
                                <div id="order-detail-${order.orderNum}"></div>
                            </div>
                            <hr>
                        </c:forEach>
                    </c:if>
                    <c:if test="${empty previousOrders}">
                        <p id="noOrdersMessage">이전 주문이 없습니다.</p>
                    </c:if>
                </div>
            </div>
        </div>
    </div>
</div>

<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.11.6/dist/umd/popper.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script>
    let globalStatus = 1; // 1 = normal mode, 2 = refund mode
    let orderList = [];
    let totalAmount = 0;

    // add-to-order 버튼들을 저장할 변수
    const addToOrderButtons = document.querySelectorAll('.add-to-order');

    // add-to-order 버튼 비활성화 함수
    function disableAddToOrderButtons() {
        addToOrderButtons.forEach(function(button) {
            button.disabled = true;
            button.style.cursor = 'not-allowed';
            button.style.opacity = '0.6';
        });
    }

    // add-to-order 버튼 활성화 함수
    function enableAddToOrderButtons() {
        addToOrderButtons.forEach(function(button) {
            button.disabled = false;
            button.style.cursor = 'pointer';
            button.style.opacity = '1';
        });
    }

    function updateOrderSummary() {
        const orderSummary = document.getElementById('orderList');
        orderSummary.innerHTML = '';  // 기존 내용을 지움

        // 주문 목록을 화면에 추가
        orderList.forEach(function(item, index) {
            const orderItem = document.createElement('p');
            orderItem.textContent = item.name + ' - ' + item.price.toLocaleString() + '원 x ' + item.quantity;

            orderSummary.appendChild(orderItem);
        });

        // 결제 버튼의 텍스트 업데이트
        const paymentButton = document.querySelector('.payment-button');  // 함수가 호출될 때마다 요소를 다시 찾기
        if (paymentButton) {
            paymentButton.textContent = '결제 버튼(총 금액: ' + totalAmount.toLocaleString() + '원)';
        } else {
            console.error('paymentButton이 존재하지 않습니다.');
        }
        console.log(totalAmount);
        console.log(orderList);
    }

    // add-to-order 버튼 클릭 이벤트 리스너
addToOrderButtons.forEach(function(button) {
    button.addEventListener('click', function() {
        // clear-order 함수가 실행 중일 때는 작동하지 않도록 방지
        if (button.disabled) {
            return;
        }

        const itemName = this.getAttribute('data-item');
        const itemCode = this.getAttribute('data-code');
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
            orderList.push({ name: itemName, price: itemPrice, quantity: 1, productCode: itemCode});
        }

        totalAmount += itemPrice;  // 총 금액 업데이트
        updateOrderSummary();
    });
});


let selectedOrderNum;

function fetchOrderDetails(orderNum) {
    selectedOrderNum = orderNum; // store the order number in the global variable
    const orderDetailTable = document.getElementById('order-detail-' + orderNum);
    if (orderDetailTable && orderDetailTable.innerHTML !== '') {
        orderDetailTable.innerHTML = '';
    } else {
        fetch('/erp/pos/sales-detail?orderNum=' + encodeURIComponent(orderNum))
            .then(response => {
                if (!response.ok) {
                    throw new Error('asdf');
                }
                return response.json();
            })
            .then(data => {
                const tableHtml = generateTable(data);
                orderDetailTable.innerHTML = tableHtml;
            })
            .catch(error => {
                console.error('asdf', error);
            });
    }
}

let globalData;
let selectedOption = 'cancel';

function generateTable(data) {
  globalData = data;

  let tableHtml = '<table style="border-collapse: collapse; width: 100%;">';
  tableHtml += '<thead>';
  tableHtml += '<tr>';
  tableHtml += '<th style="border: 1px solid #ddd; padding: 10px; text-align: left;">상품명</th>';
  tableHtml += '<th style="border: 1px solid #ddd; padding: 10px; text-align: left; width: 20%;">수량</th>';
  tableHtml += '<th style="border: 1px solid #ddd; padding: 10px; text-align: left; width: 40%;">단가</th>';
  tableHtml += '</tr>';
  tableHtml += '</thead>';
  tableHtml += '<tbody>';
  data.forEach((item, index) => {
    tableHtml += '<tr>';
    tableHtml += '<td style="border: 1px solid #ddd; padding: 10px; text-align: left;">' + item.itemName + '</td>';
    tableHtml += '<td><input type="number" value="' + item.quantity + '" min="0" max="' + item.quantity + '" onchange="globalData[' + index + '].quantity = parseInt(this.value);"></td>';
    tableHtml += '<td style="border: 1px solid #ddd; padding: 10px; text-align: left;">' + item.unitPrice.toLocaleString() + '원</td>';
    tableHtml += '</tr>';
  });
  tableHtml += '</tbody>';
  tableHtml += '</table>';
  tableHtml += '<select style="margin-top: 10px;" onchange="updateSelectedOption(this.value)" value="cancel">';
  tableHtml += '<option value="cancel">취소</option>';
  tableHtml += '<option value="refund">환불</option>';
  tableHtml += '</select>';
  tableHtml += '<button onclick="handleButtonClick()" style="margin-top: 10px;">확정</button>';

  return tableHtml;
}

function updateOrderList() {
  orderList = globalData.map(function(item) {
    return {
      name: item.itemName,
      price: item.unitPrice,
      quantity: item.quantity,
      productCode: item.itemCode
    };
  });
  totalAmount = orderList.reduce((acc, item) => acc + (item.price * item.quantity), 0);
  updateOrderSummary();
}

function updateSelectedOption(value) {
  selectedOption = value;
}

function handleButtonClick() {
  globalStatus = 2;
  updateOrderList();

  if (globalStatus === 2) {
    addToOrderButtons.forEach(function(button) {
      button.disabled = true;
    });

    const paginationLinks = document.querySelectorAll('.pagination a');
    paginationLinks.forEach(function(link) {
      link.classList.add('disabled');
      link.style.cursor = 'not-allowed';
      link.style.pointerEvents = 'none';
    });

    const paymentMethodSelect = document.getElementById('payment-type');
    paymentMethodSelect.disabled = true;
  }

  $('#previousOrderModal').modal('hide');
}

    // clear-order 버튼 클릭 이벤트 리스너
    document.getElementById('clear-order').addEventListener('click', function() {
        const orderSummary = document.getElementById('orderList');

        // add-to-order 버튼 비활성화
        disableAddToOrderButtons();

        // 애니메이션 클래스를 추가
        orderSummary.classList.add('animate__animated', 'animate__zoomOutRight');

        // 애니메이션 종료 후 텍스트 삭제 및 버튼 활성화
        orderSummary.addEventListener('animationend', function() {
            orderSummary.innerHTML = ''; // 텍스트 삭제
            orderList = [];
            totalAmount = 0;

            // 애니메이션 후 클래스 제거 및 업데이트
            orderSummary.classList.remove('animate__animated', 'animate__zoomOutRight');
            updateOrderSummary();

            // add-to-order 버튼 활성화
            enableAddToOrderButtons();
        }, { once: true }); // 한 번만 실행하도록 설정
    });

    // product-submit 폼 제출 이벤트 리스너
    document.getElementById('product-submit').addEventListener('submit', function(event) {
        event.preventDefault();

        if (orderList.length === 0) {
            alert('주문 항목이 없습니다.');
            return;
        }

        const paymentMethod = document.getElementById('payment-type').value;

        let orderData;
        let url;

        if (globalStatus === 1) {
            orderData = {
                items: orderList,
                totalAmount: totalAmount,
                paymentMethod: paymentMethod
            };
            url = '/erp/pos/payment';
        } else if (globalStatus === 2) {
            orderData = {
                items: orderList,
                totalAmount: totalAmount,
                selectedOption: selectedOption,
                selectedOrderNum
            };
            url = '/erp/pos/refund';
        }

        fetch(url, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(orderData)
        })
            .then(response => {
                if (response.status === 200) {
                    let paymentMethodText;
                    if (paymentMethod === 'card') {
                        paymentMethodText = '카드';
                    } else if (paymentMethod === 'cash') {
                        paymentMethodText = '현금';
                    }

                    alert('총 ' + totalAmount.toLocaleString() + '원이 ' + paymentMethodText + '로 결제되었습니다.');
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

    const plusButton = document.createElement('button');
    plusButton.textContent = '+';
    plusButton.addEventListener('click', function() {
        if (item.quantity < item.originalQuantity) {
            item.quantity += 1;
            updateQuantityDisplay();
        }
    });

    const minusButton = document.createElement('button');
    minusButton.textContent = '-';
    minusButton.addEventListener('click', function() {
        if (item.quantity > 0) {
            item.quantity -= 1;
        }
    });

    function updateQuantityDisplay() {
        const orderItem = document.querySelector(`#order-item-${index}`);
        orderItem.id = `order-item-${index}`;
        orderItem.textContent = `${item.name} - ${item.price.toLocaleString()}원 x ${item.quantity}`;
    }
</script>

</body>
</html>

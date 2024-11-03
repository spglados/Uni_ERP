<!DOCTYPE html>
<html lang="ko">
<head>
    <%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
    <%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
    <%@ page import="com.uni.uni_erp.util.date.NumberFormatter" %>

    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/animate.css/4.1.1/animate.min.css"/>
    <link rel="stylesheet" href="/css/pos/posMain.css">
    <link rel="stylesheet"
          href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/css/bootstrap.min.css">
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.1/dist/umd/popper.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
</head>
<body>
<div class="header">
    <img src="/images/logo/logoPos.png" class="animate__animated animate__fadeIn" alt="포스로고"
         style="height: 100px; width: 125px;">
    <%@include file="/WEB-INF/view/pos/attendanceModal.jsp" %>
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
            <button type="button" id="clear-order" class="btn clear-order-button" style="background-color: #ffc107; color: #212529; border-color: #ffc107;">
                전체삭제
            </button>
            <button type="button" id="previous-order" class="previous-order-button btn btn-primary" data-toggle="modal" data-target="#previousOrderModal">
                주문 조회
            </button>
            <div class="order-summary" id="orderList">
                목록에 아무것도 들어있지 않습니다.
            </div>
            <div class="payment-method mb-3">
                <label for="payment-type">결제 방법:</label>
                <select id="payment-type" name="payment-type">
                    <option value="card">카드</option>
                    <option value="cash">현금</option>
                </select>
            </div>
            <button type="submit" class="payment-button mb-3" >결제 버튼(총 금액: 0원)</button>
        </form>
        <!-- 시재점검 버튼 -->
        <button class="btn btn-success mb-3" onclick="inspection()">시재점검</button>
        <!-- 금고관리 버튼 -->
        <button class="btn btn-success mb-3" onclick="safe()">금고관리</button>
        <!-- 시재 추가 버튼 -->
        <button class="btn btn-success mb-3" onclick="deposit()">시재 추가</button>
        <br>

        <!-- 오픈하기 버튼 -->
        <button id="openButton" class="btn btn-success mb-3" onclick="confirmOpen()"
                <c:choose>
                    <c:when test="${status == 1}">disabled</c:when>
                </c:choose>>오픈하기
        </button>

        <!-- 마감하기 버튼 -->
        <button id="closeButton" class="btn btn-danger mb-3" onclick="closeBusiness()"
                <c:choose>
                    <c:when test="${status == 0}">disabled</c:when>
                </c:choose>>마감하기
        </button>

        <div style="color: red;">
            <c:choose>
                <c:when test="${status24 == 1}">
                    해당 가게는 24시간 영업중이므로 오픈하기 버튼이 비활성화 됩니다.
                </c:when>
                <c:otherwise>
                    <!-- 상태가 0일 때 아무것도 표시하지 않음 -->
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</div>
<div class="modal fade" id="previousOrderModal" tabindex="-1" aria-labelledby="previousOrderModalLabel"
     aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="previousOrderModalLabel">주문 조회</h5>
                <button type="button" class="btn-close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span> <!-- X 아이콘 추가 -->
                </button>
            </div>
            <div class="modal-body">
                <div id="previousOrderList" style="max-height: 700px; overflow-y: auto;">
                    <!-- Dynamically populate this with previous orders -->
                    <c:if test="${not empty previousOrders}">
                        <c:forEach items="${previousOrders}" var="order">
                            <div>
                                <strong>주문 번호:</strong> ${order.orderNum}<br>
                                <strong>판매 일자:</strong> ${fn:replace(fn:substring(order.salesDate, 0, 16), 'T', ' ')}<br>
                                <strong>총 가격:</strong> ${NumberFormatter.formatToPrice(order.totalPrice)} 원
                                <button type="button" class="btn btn-link"
                                        onclick="fetchOrderDetails(${order.orderNum})">
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
<script>
    let globalStatus = 1; // 1 = normal mode, 2 = refund mode
    let orderList = [];
    let totalAmount = 0;

    // add-to-order 버튼들을 저장할 변수
    const addToOrderButtons = document.querySelectorAll('.add-to-order');

    // add-to-order 버튼 비활성화 함수
    function disableAddToOrderButtons() {
        addToOrderButtons.forEach(function (button) {
            button.disabled = true;
            button.style.cursor = 'not-allowed';
            button.style.opacity = '0.6';
        });
    }

    // add-to-order 버튼 활성화 함수
    function enableAddToOrderButtons() {
        addToOrderButtons.forEach(function (button) {
            button.disabled = false;
            button.style.cursor = 'pointer';
            button.style.opacity = '1';
        });
    }

    function updateOrderSummary() {
        const orderSummary = document.getElementById('orderList');
        orderSummary.innerHTML = '';  // 기존 내용을 지움

        // 주문 목록을 화면에 추가
        orderList.forEach(function (item, index) {
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
    addToOrderButtons.forEach(function (button) {
        button.addEventListener('click', function () {
            // clear-order 함수가 실행 중일 때는 작동하지 않도록 방지
            if (button.disabled) {
                return;
            }

            const itemName = this.getAttribute('data-item');
            const itemCode = this.getAttribute('data-code');
            const itemPrice = parseInt(this.getAttribute('data-price'));

            // 이미 주문 목록에 있는 항목인지 확인
            const existingItem = orderList.find(function (item) {
                return item.name === itemName;
            });

            if (existingItem) {
                // 이미 있는 항목이면 수량과 총 금액 증가
                existingItem.quantity += 1;
            } else {
                // 새로운 항목이면 목록에 추가
                orderList.push({name: itemName, price: itemPrice, quantity: 1, productCode: itemCode});
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

        let tableHtml = '<table class="table" style="border-collapse: collapse; width: 100%;">';
        tableHtml += '<thead class="thead-light">';
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
            tableHtml += '<td><input type="number" class="form-control" value="' + item.quantity + '" min="0" max="' + item.quantity + '" onchange="globalData[' + index + '].quantity = parseInt(this.value);"></td>';
            tableHtml += '<td style="border: 1px solid #ddd; padding: 10px; text-align: left;">' + item.unitPrice.toLocaleString() + '원</td>';
            tableHtml += '</tr>';
        });
         tableHtml += '</tbody>';
        tableHtml += '</table>';
        // 셀렉트 박스와 버튼을 나란히 배치
        tableHtml += '<div style="display: flex; align-items: center; margin-top: 10px;">';
        tableHtml += '<select class="form-select" style="margin-right: 10px; width: auto;" onchange="updateSelectedOption(this.value)" value="cancel">'; // Bootstrap 스타일 추가
        tableHtml += '<option value="cancel">취소</option>';
        tableHtml += '<option value="refund">환불</option>';
        tableHtml += '</select>';
        // Bootstrap 스타일을 추가한 버튼
        tableHtml += '<button class="btn btn-primary" onclick="handleButtonClick()" style="margin-left: 10px;">확정</button>';
        tableHtml += '</div>';

        return tableHtml;
    }

    function updateOrderList() {
        orderList = globalData.map(function (item) {
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
        console.log('handleButtonClick 함수 호출됨');
        globalStatus = 2;
        updateOrderList();

        if (globalStatus === 2) {
            addToOrderButtons.forEach(function (button) {
                button.disabled = true;
                console.log('버튼 비활성화:', button);
            });

            const paginationLinks = document.querySelectorAll('.pagination a');
            paginationLinks.forEach(function (link) {
                link.classList.add('disabled');
                link.style.cursor = 'not-allowed';
                link.style.pointerEvents = 'none';
                console.log('페이지네이션 링크 비활성화:', link);
            });

            const paymentMethodSelect = document.getElementById('payment-type');
            paymentMethodSelect.disabled = true;
            console.log('결제 방법 선택 비활성화:', paymentMethodSelect);
        }

        $('#previousOrderModal').modal('hide');
        console.log('previousOrderModal 숨김 처리');
    }


    // clear-order 버튼 클릭 이벤트 리스너
    document.getElementById('clear-order').addEventListener('click', function () {
        const orderSummary = document.getElementById('orderList');

        // add-to-order 버튼 비활성화
        disableAddToOrderButtons();

        // 애니메이션 클래스를 추가
        orderSummary.classList.add('animate__animated', 'animate__zoomOutRight');

        // 애니메이션 종료 후 텍스트 삭제 및 버튼 활성화
        orderSummary.addEventListener('animationend', function () {
            orderSummary.innerHTML = ''; // 텍스트 삭제
            orderList = [];
            totalAmount = 0;

            // 애니메이션 후 클래스 제거 및 업데이트
            orderSummary.classList.remove('animate__animated', 'animate__zoomOutRight');
            updateOrderSummary();

            // add-to-order 버튼 활성화
            enableAddToOrderButtons();
        }, {once: true}); // 한 번만 실행하도록 설정
    });

    // product-submit 폼 제출 이벤트 리스너
    document.getElementById('product-submit').addEventListener('submit', function (event) {
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
                } else if (response.status === 422) {
                    alert('재고가 부족합니다.');
                } else {
                    alert('결제에 실패했습니다. 다시 시도해주세요.');
                }
            })
            .catch(function (error) {
                alert('오류가 발생했습니다. 다시 시도해주세요.');
            });
    });

    const plusButton = document.createElement('button');
    plusButton.textContent = '+';
    plusButton.addEventListener('click', function () {
        if (item.quantity < item.originalQuantity) {
            item.quantity += 1;
            updateQuantityDisplay();
        }
    });

    const minusButton = document.createElement('button');
    minusButton.textContent = '-';
    minusButton.addEventListener('click', function () {
        if (item.quantity > 0) {
            item.quantity -= 1;
        }
    });

    function updateQuantityDisplay() {
        const orderItem = document.querySelector(`#order-item-${index}`);
        orderItem.id = `order-item-${index}`;
        orderItem.textContent = `${item.name} - ${item.price.toLocaleString()}원 x ${item.quantity}`;
    }

    function inspection() {
        window.open('http://localhost:8080/erp/pos/inspection', '_blank', 'width=800,height=600');
    }

    function safe() {
        window.open('http://localhost:8080/erp/pos/safe', '_blank', 'width=800,height=600');
    }

    function closeBusiness() {
        window.open('http://localhost:8080/erp/pos/close', '_blank', 'width=800,height=600');
    }

    function deposit() {
        window.open('http://localhost:8080/erp/pos/deposit', '_blank', 'width=800,height=600');
    }

    function confirmOpen() {
        if (confirm("오픈하시겠습니까?")) {
            fetch('/erp/pos/open', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({}) // 필요시 추가 데이터 전송
            })
                .then(response => {
                    if (!response.ok) {
                        throw new Error('Network response was not ok');
                    }
                    return response.text();
                })
                .then(data => {
                    alert(data); // 성공 메시지 알림
                    location.reload(); // 페이지 리로드하여 상태 업데이트
                })
                .catch(error => {
                    console.error('There was a problem with the fetch operation:', error);
                });
        }
    }
</script>
<script>
    // 엘리먼트 초기화
    const passwordElement = document.getElementById('password');
    let attendancesData = [];
    // 현재 활성화된 입력 필드를 추적
    let activeInputField = null;

    // 숫자 버튼 클릭 시 숫자 추가
    function addNumber(num) {
        if (activeInputField) {
            activeInputField.value += num;
        }
    }

    // ← 버튼 클릭 시 숫자 삭제
    function deleteNumber() {
        if (activeInputField) {
            activeInputField.value = activeInputField.value.slice(0, -1);
        }
    }

    // C 버튼 클릭 시 숫자 초기화
    function clearNumber() {
        if (activeInputField) {
            activeInputField.value = '';
        }
    }

    // 입력 필드에 포커스가 가면 해당 필드를 활성화
    document.getElementById('employeeNumber').addEventListener('focus', function () {
        activeInputField = this;
    });
    passwordElement.addEventListener('focus', function () {
        activeInputField = this;
    });

    // 모달이 열릴 때 초기화
    $('#clockInOutModal').on('shown.bs.modal', function () {
        resetModalFields();
    });

    // 조회 버튼 클릭 시 사번으로 직원 정보 조회
    function fetchEmployeeInfo() {
        const employeeNumber = document.getElementById('employeeNumber').value;

        // 사번이 입력되었는지 확인
        if (!employeeNumber) {
            alert('사번을 입력하세요.');
            return;
        }
        fetch("/erp/hr/attendance/" + employeeNumber, {
            method: "GET",
        })
            .then(response => {
                if (response.status === 200) {
                    return response.json();
                } else {
                    return response.json().then(errorData => {
                        throw new Error(errorData.message);
                    });
                }
            })
            .then(response => {
                attendancesData = response.dataList;
                console.log('attendancesData', attendancesData);
                const data = attendancesData[0];
                if (data.status === "UNPLANNED") {
                    attendancesData = [];
                    displayAttendanceOptions(attendancesData);
                    showToast("계획된 근무가 없습니다.");
                    document.getElementById('employeeName').value = data.name;
                    document.getElementById('clockInButton').style.display = 'flex';
                    document.getElementById('scheduledTime').value = '계획된 근무가 없습니다.';
                    return;
                }
                showToast("조회되었습니다.");
                displayAttendanceOptions(attendancesData);
            })
            .catch(error => {
                // 오류 발생 시 처리
                resetModalFields();
                console.error('오류:', error.message);
                showToast(error.message);
            });

    }

    // 근무가 2개 이상일 경우 select 옵션으로 적용
    function displayAttendanceOptions(attendances) {
        console.log('attendances', attendances);
        const attendanceList = document.getElementById('attendanceList');
        attendanceList.innerHTML = ''; // 기존 리스트 초기화

        // 기본 옵션 추가
        const defaultOption = document.createElement('option');
        defaultOption.textContent = '근무를 선택하세요';
        defaultOption.value = '';
        defaultOption.disabled = true;
        defaultOption.selected = true;
        attendanceList.appendChild(defaultOption);

        // 근무 항목을 select 옵션으로 추가
        let index = 0;
        attendances.forEach((attendance, curIndex) => {
            const option = document.createElement('option');
            if (!attendance.start || !attendance.end) {
                option.textContent = "근무 " + (curIndex + 1) + ": " + "예정에 없는 근무";
            } else {
                option.textContent = "근무 " + (curIndex + 1) + ": " + attendance.start + " - " + attendance.end;
            }
            if (attendance.leaveTime) {
                option.textContent += ' (완료)';
            } else if (attendance.attendanceTime) {
                option.textContent += ' (근무중)';
            }
            option.value = curIndex; // 인덱스를 값으로 사용
            option.setAttribute('data-id', attendance.id);
            // curIndex가 0일 경우 해당 옵션을 선택된 상태로 설정
            if (curIndex === 0) {
                option.selected = true;
            }
            index = curIndex + 1;
            console.log('index', index);
            attendanceList.appendChild(option);
        });
        // 반복문이 끝난 후 "예정에 없는 근무" 항목 추가
        const additionalOption = document.createElement('option');
        additionalOption.textContent = "근무 " + (index + 1) + ": " + "예정에 없는 근무";
        additionalOption.value = index; // 고유한 값을 설정
        attendanceList.appendChild(additionalOption);
        if (attendances.length === 0) {
            additionalOption.selected = true;
        }
        const additionalAttendance = {
            start: null,
            end: null,
            attendanceTime: null,
            leaveTime: null
        };
        attendances.push(additionalAttendance);
        // 기본적으로 첫 번째 근무를 선택
        if (attendances.length > 0) {
            selectAttendance(attendances[0]);
        }
    }

    // select 변경 시 선택한 근무에 따라 업데이트
    function handleAttendanceChange() {
        const attendanceList = document.getElementById('attendanceList');
        const selectedIndex = attendanceList.value;
        console.log('selectedIndex', selectedIndex);
        // 유효한 선택이었는지 확인
        if (selectedIndex !== '') {
            const selectedAttendance = attendancesData[selectedIndex]; // 이전에 불러온 데이터에서 가져옴
            selectAttendance(selectedAttendance);
        }
    }

    // 근무 선택시 값 변경
    function selectAttendance(attendance) {
        console.log('attendance', attendance);
        if (attendance.name) {
            document.getElementById('employeeName').value = attendance.name;
        }
        if (!attendance.status || attendance.status === 'NOT_EXECUTED') {
            // 출근하지 않은 상태라면 출근 버튼만 표시
            document.getElementById('clockInButton').style.display = 'flex';
            document.getElementById('clockOutButton').style.display = 'none';
        } else if (attendance.status === 'WORKING') {
            // 이미 출근한 상태라면 퇴근 버튼만 표시
            // 단, 출근 한지 30분이 지나지 않았다면 퇴근 버튼 활성화 되지않음
            const now = new Date();
            console.log('now', now);
            const [hours, minutes] = attendance.attendanceTime.split(":").map(Number);
            const nowHour = now.getHours();
            const nowMinute = now.getMinutes();
            let compareHour = hours;
            let compareMinute = minutes + 30;
            if (compareMinute >= 60) {
                compareHour += 1;
                compareMinute -= 60;
            }
            // 자정 넘어가는 경우 처리
            if (nowHour < hours || (nowHour === hours && nowMinute < minutes)) {
                // 자정 넘김을 의미 -> 출근 시간에 24시간을 더해 비교
                compareHour -= 24;
            }
            // 현재 시각을 24시간 형식으로 변환
            const currentTotalMinutes = nowHour * 60 + nowMinute;
            const compareTotalMinutes = compareHour * 60 + compareMinute;
            console.log(now, nowHour, nowMinute, compareHour, compareMinute, currentTotalMinutes, compareTotalMinutes);
            if (currentTotalMinutes >= compareTotalMinutes) {
                document.getElementById('clockOutButton').style.display = 'flex';
            } else {
                document.getElementById('clockOutButton').style.display = 'none';
            }
            document.getElementById('clockInButton').style.display = 'none';
        } else {
            // 이외 모든 상황에서 버튼 없앰
            document.getElementById('clockInButton').style.display = 'none';
            document.getElementById('clockOutButton').style.display = 'none';
        }
        if (attendance.start && attendance.end) {
            document.getElementById('scheduledTime').value = attendance.start + ' - ' + attendance.end;
        } else {
            document.getElementById('scheduledTime').value = "예정에 없는 근무";
        }
        document.getElementById('clockInTime').value = attendance.attendanceTime;
        document.getElementById('clockOutTime').value = attendance.leaveTime;
    }

    // 모든 필드를 초기화하는 함수
    function resetModalFields() {
        document.getElementById('employeeNumber').value = '';
        document.getElementById('employeeName').value = '';
        document.getElementById('password').value = '';
        document.getElementById('clockInTime').value = '';
        document.getElementById('clockOutTime').value = '';
        document.getElementById('scheduledTime').value = '';
        document.getElementById('attendanceList').innerHTML = ''; // 근무 목록 초기화

        // 출근/퇴근 버튼 숨기기
        document.getElementById('clockInButton').style.display = 'none';
        document.getElementById('clockOutButton').style.display = 'none';

        // 활성화된 입력 필드를 사번 입력 필드로 설정
        activeInputField = document.getElementById('employeeNumber');
    }

    // 현재 시간을 표시하는 함수
    function updateTime() {
        const now = new Date();
        const hours = String(now.getHours()).padStart(2, '0');
        const minutes = String(now.getMinutes()).padStart(2, '0');
        const seconds = String(now.getSeconds()).padStart(2, '0');
        const formattedTime = hours + ":" + minutes + ":" + seconds;
        document.getElementById('currentTime').textContent = formattedTime;
    }

    // 매초마다 시간을 업데이트
    setInterval(updateTime, 1000);
    // 페이지 로드 시 시간을 즉시 표시
    updateTime();

    // 출근 버튼 클릭 시
    function handleClockIn() {
        const employeeNumber = document.getElementById('employeeNumber').value;
        const password = passwordElement.value;
        const type = "attendance";
        const attendanceId = getSelectedAttendanceId();

        // 유효성 검사
        if (!validateForm(employeeNumber, password)) {
            return;
        }

        // 출근 요청 전송
        sendAttendanceRequest(employeeNumber, password, type, attendanceId);
    }

    // 퇴근 버튼 클릭 시
    function handleClockOut() {
        const employeeNumber = document.getElementById('employeeNumber').value;
        const password = passwordElement.value;
        const type = 'leave'; // 퇴근 타입 설정
        const attendanceId = getSelectedAttendanceId(); // 선택된 근무의 id 가져오기

        // 유효성 검사
        if (!validateForm(employeeNumber, password)) {
            return;
        }

        // 퇴근 요청 전송
        sendAttendanceRequest(employeeNumber, password, type, attendanceId);
    }

    // 선택된 근무의 id 가져오는 함수
    function getSelectedAttendanceId() {
        const attendanceList = document.getElementById('attendanceList');
        const selectedOption = attendanceList.options[attendanceList.selectedIndex];
        const attendanceId = selectedOption.getAttribute('data-id');

        // data-id가 없거나 빈 문자열일 경우 null 반환
        return attendanceId ? attendanceId : null;
    }

    // 유효성 검사 함수
    function validateForm(employeeNumber, password) {
        if (!employeeNumber) {
            showToast('사번을 입력하세요.');
            activeInputField = document.getElementById('employeeNumber');
            document.getElementById('employeeNumber').focus();
            return false;
        }
        if (!password) {
            showToast('비밀번호를 입력하세요.');
            activeInputField = passwordElement;
            passwordElement.focus();
            return false;
        }
        return true;
    }

    // 출근/퇴근 요청 전송 함수
    function sendAttendanceRequest(employeeNumber, password, type, attendanceId) {
        fetch("/erp/hr/attendance/" + employeeNumber, {
            method: "PUT",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                password: password,
                type: type, // 출근/퇴근 타입을 body에 포함
                id: attendanceId // 선택된 근무의 id를 body에 포함
            })
        })
            .then(response => {
                if (response.status === 200) {
                    return response.json();
                } else if (response.status === 401) {
                    return response.json().then(errorData => {
                        passwordElement.value = '';
                        passwordElement.focus();
                        showToast(errorData.message);
                    });
                } else {
                    return response.json().then(errorData => {
                        throw new Error(errorData.message);
                    });
                }
            })
            .then(response => {
                attendancesData = response.dataList;
                const data = attendancesData[0];
                showToast("조회되었습니다.");
                displayAttendanceOptions(attendancesData);
                selectAttendance(data);
            })
            .catch(error => {
                resetModalFields();
                console.error('오류:', error.message);
                showToast(error.message);
            });
    }
</script>
<!-- Toast 및 로딩 스피너를 위한 JavaScript 추가 -->
<script src="/js/toastHelper.js"></script>
</body>
</html>

<%--
  Created by IntelliJ IDEA.
  User: namch
  Date: 24. 10. 10.
  Time: 오후 3:30
  Description: ERP 대시보드 페이지 (사이드바 메뉴 및 하위 메뉴 추가)
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/view/erp/layout/erpHeader.jsp" %>
<style>
    .input-group {
        max-width: 300px;
        margin-top: 20px;
        margin-bottom: 20px;
    }

    .welcome {
        font-size: 32px;
        text-align: center;
    }

    /* 매출 정보 리스트 스타일링 */
    .sales-item {
        transition: background-color 0.3s, transform 0.3s;
        border-radius: 8px;
        padding: 15px;
        margin-bottom: 10px;
    }

    .sales-item:hover {
        background-color: #f0f8ff; /* 연한 하늘색 배경 */
        transform: translateY(-2px);
        box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
    }

    .sales-icon {
        color: #1E90FF; /* 도는 파란색 아이콘 */
        animation: pulse 2s infinite;
    }

    .sales-details {
        display: flex;
        flex-direction: row;
        justify-content: space-between;
        width: 100%;
    }

    .sales-time {
        font-size: 1.2rem;
        font-weight: 700;
        color: #333;
        margin-bottom: 5px;
        margin-left: 10px;
    }

    .sales-amount {
        font-size: 1rem;
        color: #555;
        font-weight: 600;
    }

    /* 아이콘 애니메이션 */
    @keyframes pulse {
        0% {
            transform: scale(1);
            opacity: 1;
        }
        50% {
            transform: scale(1.1);
            opacity: 0.7;
        }
        100% {
            transform: scale(1);
            opacity: 1;
        }
    }

    /* 반응형 디자인을 위한 추가 스타일 */
    @media (max-width: 767px) {
        .sales-item {
            flex-direction: column;
            align-items: flex-start;
        }

        .sales-icon {
            margin-bottom: 10px;
        }
    }

    @media (min-width: 768px) {
        .sales-item {
            flex-direction: row;
            align-items: center;
        }
    }

</style>

<!-- 메인 컨텐츠 -->
<div class="content container-fluid">
    <div class="erp-main-header">
        <!-- 사용자 환영 메시지 -->
        <div class="name-welcome">
            <h5 class="mb-0 welcome"><i class="fas fa-user"></i> [${username}]님, 환영합니다!</h5>
        </div>
        <!-- 가게 선택 드롭다운 -->
        <div class="store-selection">
            <div class="input-group">
                <div class="input-group-prepend">
                                <span class="input-group-text" id="store-select-icon"><i
                                        class="fas fa-store"></i></span>
                </div>
                <select name="store" id="storeId" class="form-control" aria-label="가게 선택"
                        aria-describedby="store-select-icon">
                    <option value="" disabled <c:if test="${empty storeId}">selected</c:if>>가게 선택</option>
                    <c:forEach var="store" items="${storeList}">
                        <option value="${store.id}" <c:if test="${storeId == store.id}">selected</c:if>>
                                ${store.name}
                        </option>
                    </c:forEach>
                </select>
            </div>
        </div>
    </div>
    <!-- 알림 Toast 추가 -->
<%--    <div class="toast-container position-fixed bottom-0 end-0 p-3">--%>
<%--        <div id="welcomeToast" class="toast align-items-center text-bg-primary" role="alert"--%>
<%--             aria-live="assertive" aria-atomic="true">--%>
<%--            <div class="d-flex">--%>
<%--                <div class="toast-body">--%>
<%--                    [${username}]님, 환영합니다.--%>
<%--                </div>--%>
<%--                <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast"--%>
<%--                        aria-label="Close"><i class="fas fa-window-close"></i></button>--%>
<%--            </div>--%>
<%--        </div>--%>
<%--    </div>--%>

    <!-- 4개의 섹션을 위한 그리드 -->
    <div class="row">
        <!-- 1. 현재 매출 정보 -->
        <div class="col-lg-6 col-md-6 mb-4">
            <div class="card h-100 shadow-sm">
                <div class="card-header bg-primary text-white">
                    <h5 class="card-title mb-0">현재 매출 정보</h5>
                </div>
                <div class="card-body">
                    <ul class="list-group list-group-flush">
                        <c:forEach var="sales" items="${salesInfoList}">
                            <li class="list-group-item d-flex align-items-center sales-item">
                                <i class="fas fa-clock fa-lg text-primary me-3 sales-icon" aria-hidden="true"></i>
                                <div class="sales-details">
                                    <!-- 오전/오후 구분 -->
                                    <h6 class="mb-1 sales-time">
                                        <c:choose>
                                            <c:when test="${sales.time < 12}">
                                                오전 ${sales.time}시
                                            </c:when>
                                            <c:when test="${sales.time == 12}">
                                                정오 ${sales.time}시
                                            </c:when>
                                            <c:otherwise>
                                                오후 ${sales.time - 12}시
                                            </c:otherwise>
                                        </c:choose>
                                    </h6>
                                    <p class="mb-0 sales-amount">${sales.amount}</p>
                                </div>
                            </li>
                        </c:forEach>
                    </ul>
                </div>
            </div>
        </div>

        <!-- 2. 현재 가장 많이 팔린 상품 -->
        <div class="col-lg-6 col-md-6 mb-4">
            <div class="card h-100 shadow-sm">
                <div class="card-header bg-success text-white">
                    <h5 class="card-title mb-0">현재 가장 많이 팔린 상품</h5>
                </div>
                <div class="card-body">
                    <!-- 리스트를 삽입할 영역 -->
                    <ul class="list-group" id="topSellingProducts">
                        <!-- 데이터 삽입 예시 -->
                        <c:forEach var="product" items="${saleQuantityList}">
                            <li class="list-group-item d-flex justify-content-between align-items-center">
                                <div class="d-flex align-items-center">
                                    <i class="fas fa-box-open me-3 text-primary"></i>&nbsp;
                                    <b>${product.productName}</b>
                                </div>
                                <span class="badge bg-success rounded-pill">${product.quantity}</span>
                            </li>
                        </c:forEach>

                    </ul>
                </div>
            </div>
        </div>

        <!-- 3. 임박 유통기한 자재 -->
        <div class="col-lg-6 col-md-6 mb-4">
            <div class="card h-100 shadow-sm">
                <div class="card-header bg-warning text-white">
                    <h5 class="card-title mb-0">임박 유통기한 자재</h5>
                </div>
                <div class="card-body">
                    <!-- 테이블을 삽입할 영역 -->
                    <table class="table table-striped" id="expiringMaterialsTable">
                        <thead>
                        <tr>
                            <th>자재코드</th>
                            <th>자재명</th>
                            <th>유통기한</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach var="expiration" items="${nearingExpirationDateList}">
                            <tr>
                                <td>${expiration.materialCode}</td>
                                <td>${expiration.materialName}</td>
                                <td><span style="color: red;">${expiration.expirationDate}</span></td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>

        <!-- 4. 현재 재고가 부족한 품목 -->
        <div class="col-lg-6 col-md-6 mb-4">
            <div class="card h-100 shadow-sm">
                <div class="card-header bg-danger text-white">
                    <h5 class="card-title mb-0">현재 재고가 부족한 품목</h5>
                </div>
                <div class="card-body">
                    <!-- 테이블을 삽입할 영역 -->
                    <table class="table table-striped" id="lowStockItemsTable">
                        <thead>
                        <tr>
                            <th>자재코드</th>
                            <th>자재명</th>
                            <th>이론재고</th>
                            <th>실재고</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach var="alarm" items="${alarmCycleList}">
                            <tr>
                                <td>${alarm.materialCode}</td>
                                <td>${alarm.materialName}</td>
                                <td>${alarm.theoreticalAmount}&nbsp;${alarm.unit}</td>
                                <td>${alarm.actualAmount}&nbsp;${alarm.unit}</td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>

        <!-- 5. 직원 근태 정보 -->
        <div class="col-lg-6 col-md-6 mb-4">
            <div class="card h-100 shadow-sm">
                <div class="card-header bg-info text-white">
                    <h5 class="card-title mb-0">직원 근태 정보</h5>
                </div>
                <div class="card-body">
                    <!-- 테이블을 삽입할 영역 -->
                    <table class="table table-striped" id="lowEmpStatusTable">
                        <thead>
                        <tr>
                            <th>${month}월</th>
                            <th>지각</th>
                            <th>조퇴</th>
                            <th>무단 결근</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach var="monthAtt" items="${attDTO.monthlyAttendanceList}">
                            <tr>
                                <td>${monthAtt.name}</td>
                                <td>${monthAtt.late}</td>
                                <td>${monthAtt.earlyLeave}</td>
                                <td>${monthAtt.unauthorizedAbsent}</td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>

        <!-- 6. 오늘 출근 예정자 -->
        <div class="col-lg-6 col-md-6 mb-4">
            <div class="card h-100 shadow-sm">
                <div class="card-header bg-dark text-white">
                    <h5 class="card-title mb-0">오늘 출근 예정자</h5>
                </div>
                <div class="card-body">
                    <!-- 테이블을 삽입할 영역 -->
                    <table class="table table-striped" id="lowTodayWorkTable">
                        <thead>
                        <tr>
                            <th>출근일</th>
                            <th>출근 예정 시간</th>
                            <th>출근 여부</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach var="todayAtt" items="${attDTO.todayAttendanceList}">
                            <tr>
                                <td>${todayAtt.name}</td>
                                <td>${todayAtt.startTime}</td>
                                <td>${todayAtt.status}</td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- 선택 사항: AJAX 요청을 위한 Axios 포함 (이미 헤더에 포함되어 있다면 생략 가능) -->
<script src="https://cdn.jsdelivr.net/npm/axios/dist/axios.min.js"></script>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script>
    // JSON 데이터를 Java 변수로 변환
    $(document).ready(function () {
        // Toast 설정 및 표시
        var toastElement = document.getElementById('welcomeToast');
        var toast = new bootstrap.Toast(toastElement, {autohide: false});
        toast.show();

        // 5초 후에 Toast 자동으로 닫기
        setTimeout(function () {
            toast.hide();
        }, 5000);
    });

    document.addEventListener('DOMContentLoaded', function () {
        const storeSelect = document.getElementById('storeId');

        storeSelect.addEventListener('change', function () {
            const selectedValue = storeSelect.value;

            // 서버로 데이터 전송 (예시: 가게 변경)
            fetch('/erp/store/' + selectedValue, {
                method: "PUT"
            })
                .then(function (response) {
                    if (response.ok) {
                        window.location.reload();
                    }
                })
                .catch(function (error) {
                    console.log('error', error);
                    alert('가게 변경에 실패했습니다.');
                });
        });

        // // 차트 초기화 (예시)
        // var ctx = document.getElementById('salesChart').getContext('2d');
        // var salesChart = new Chart(ctx, {
        //     type: 'bar', // 차트 유형 (bar, line, pie 등)
        //     data: {
        //         labels: [], // 날짜 또는 카테고리
        //         datasets: [{
        //             label: '매출액',
        //             data: [], // 매출 데이터
        //             backgroundColor: 'rgba(54, 162, 235, 0.6)',
        //             borderColor: 'rgba(54, 162, 235, 1)',
        //             borderWidth: 1
        //         }]
        //     },
        //     options: {
        //         responsive: true,
        //         maintainAspectRatio: false
        //     }
        // });

        // 데이터 삽입 로직 (statistics.js 또는 별도 스크립트 파일에서 처리)
        // 예시:
        /*
        axios.get('/erp/api/sales')
            .then(function(response) {
                salesChart.data.labels = response.data.labels;
                salesChart.data.datasets[0].data = response.data.sales;
                salesChart.update();
            })
            .catch(function(error) {
                console.log(error);
            });

        axios.get('/erp/api/top-selling-products')
            .then(function(response) {
                var list = document.getElementById('topSellingProducts');
                response.data.forEach(function(product) {
                    var li = document.createElement('li');
                    li.className = 'list-group-item';
                    li.textContent = product.name + ' - ' + product.sales + '개';
                    list.appendChild(li);
                });
            })
            .catch(function(error) {
                console.log(error);
            });

        axios.get('/erp/api/expiring-materials')
            .then(function(response) {
                var tableBody = document.getElementById('expiringMaterialsTable').getElementsByTagName('tbody')[0];
                response.data.forEach(function(material) {
                    var row = tableBody.insertRow();
                    var cell1 = row.insertCell(0);
                    var cell2 = row.insertCell(1);
                    var cell3 = row.insertCell(2);
                    cell1.textContent = material.name;
                    cell2.textContent = material.expirationDate;
                    cell3.textContent = material.quantity;
                });
            })
            .catch(function(error) {
                console.log(error);
            });

        axios.get('/erp/api/low-stock-items')
            .then(function(response) {
                var tableBody = document.getElementById('lowStockItemsTable').getElementsByTagName('tbody')[0];
                response.data.forEach(function(item) {
                    var row = tableBody.insertRow();
                    var cell1 = row.insertCell(0);
                    var cell2 = row.insertCell(1);
                    var cell3 = row.insertCell(2);
                    cell1.textContent = item.name;
                    cell2.textContent = item.currentStock;
                    cell3.textContent = item.requiredQuantity;
                });
            })
            .catch(function(error) {
                console.log(error);
            });
        */
    });
</script>
<%@ include file="/WEB-INF/view/erp/layout/erpFooter.jsp" %>

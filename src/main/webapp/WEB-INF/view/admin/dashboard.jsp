<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="description" content="">
    <meta name="author" content="">
    <title>Dashboard</title>

    <!-- Custom fonts for this template -->
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css" rel="stylesheet" type="text/css">
    <link href="https://fonts.googleapis.com/css?family=Nunito:200,300,400,600,700,800,900" rel="stylesheet">

    <!-- Custom styles for this template -->
    <link href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css" rel="stylesheet">
    <link href="/css/sb-admin-2.min.css" rel="stylesheet">
    <link href="/css/admin-page.css" rel="stylesheet">
    <link href="/img/favicon.ico" rel="icon">
</head>

<body id="page-top">
    <!-- Page Wrapper -->
    <div id="wrapper">
        <!-- Sidebar -->
        <ul class="navbar-nav bg-gradient-primary sidebar sidebar-dark accordion" id="accordionSidebar">
            <!-- Sidebar - Brand -->
            <a class="sidebar-brand d-flex align-items-center justify-content-center" href="/admin/main">
                <div class="sidebar-brand-icon rotate-n-15">
                    <i class="fas fa-laugh-wink"></i>
                </div>
                <div class="sidebar-brand-text mx-3">관리자 페이지</div>
            </a>
            <hr class="sidebar-divider my-0">

            <!-- Nav Items -->
            <li class="nav-item active"><a class="nav-link" href="/admin/main"><i class="fas fa-fw fa-tachometer-alt"></i> <span>대시보드</span></a></li>
            <li class="nav-item"><a class="nav-link" href="/admin/userManagement"><i class="fas fa-fw fa-table"></i> <span>유저 관리</span></a></li>
            <li class="nav-item"><a class="nav-link" href="/admin/storeManagement"><i class="fas fa-fw fa-table"></i> <span>가게 관리</span></a></li>
            <li class="nav-item"><a class="nav-link collapsed" href="#" data-toggle="collapse" data-target="#collapseTwo" aria-expanded="true" aria-controls="collapseTwo"><i class="fas fa-fw fa-cog"></i> <span>고객 지원</span></a>
                <div id="collapseTwo" class="collapse" aria-labelledby="headingTwo" data-parent="#accordionSidebar">
                    <div class="bg-white py-2 collapse-inner rounded">
                        <a class="collapse-item" href="/admin/noticeList">공지사항</a>
                        <a class="collapse-item" href="/admin/contactList">1:1 문의 관리</a>
                        <a class="collapse-item" href="/admin/refund">환불요청 처리</a>
                    </div>
                </div>
            </li>
            <hr class="sidebar-divider my-0">
            <li class="nav-item"><a class="nav-link" href="/admin/logout"><i class="fas fa-fw fa-table"></i> <span>로그아웃</span></a></li>
            <hr class="sidebar-divider">
            <div class="text-center d-none d-md-inline">
                <button class="rounded-circle border-0" id="sidebarToggle"></button>
            </div>
        </ul>
        <!-- End of Sidebar -->

        <!-- Content Wrapper -->
        <div id="content-wrapper" class="d-flex flex-column">
            <!-- Main Content -->
            <div id="content">
                <!-- Begin Page Content -->
                <div class="container-fluid">
                    <!-- Page Heading -->
                    <div class="d-sm-flex align-items-center justify-content-between mb-4">
                        <h1 class="h3 mb-0 text-gray-800">ㅎㄱㅁㅇ?</h1>
                    </div>

                    <div class="row">
                        <!-- Sales Chart -->
                        <div class="col-xl-6 col-lg-6">
                            <div class="card shadow mb-4">
                                <div class="card-header py-3 d-flex flex-row align-items-center justify-content-between">
                                    <h6 class="m-0 font-weight-bold text-primary">전체 가게 매출 평균</h6>
                                </div>
                                <div>
                                    <button onclick="updateChart('monthly')" onchange="filterStore()">월별</button>
                                    <button onclick="updateChart('yearly')" onchange="filterStore()">연별</button>
                                    * 오늘 날짜를 기준으로 해당하는 월, 연도별 전체 가게 매출을 보여줍니다
                                    <div id="storeSelect" onchange="filterStore()"></div>
                                    <canvas id="myChart"></canvas>
                                </div>
                            </div>
                        </div>
                        <div class="col-xl-6 col-lg-6">
                            <div class="container">
                                <table width="100%">
                                    <tr>
                                        <th colspan="4">사이트 운영현황</th>
                                    </tr>
                                    <tr>
                                        <th>운영 항목</th>
                                        <th>현황</th>
                                        <th>운영현황</th>
                                    </tr>
                                        <td>유저 수</td>
                                        <td>${totalUserCount}명</td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${totalUserCount >= 1000}"><span class="status-icon icon-sunny">☀️</span></c:when>
                                                <c:when test="${totalUserCount >= 750}"><span class="status-icon icon-cloud">☁️</span></c:when>
                                                <c:otherwise><span class="status-icon icon-thunder">⛈️</span></c:otherwise>
                                            </c:choose>
                                        </td>
                                    <tr>
                                    </tr>
                                        <td>구독자 수</td>
                                        <td>${subscribeUserCount}명</td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${subscribeUserCount >= 100}"><span class="status-icon icon-sunny">☀️</span></c:when>
                                                <c:when test="${subscribeUserCount >= 75}"><span class="status-icon icon-cloud">☁️</span></c:when>
                                                <c:otherwise><span class="status-icon icon-thunder">⛈️</span></c:otherwise>
                                            </c:choose>
                                        </td>
                                    <tr>
                                    </tr>
                                        <td>구독한 유저 비율</td>
                                        <td>${subscriptionRateDouble}%</td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${subscriptionRate >= 100}"><span class="status-icon icon-sunny">☀️</span></c:when>
                                                <c:when test="${subscriptionRate >= 75}"><span class="status-icon icon-cloud">☁️</span></c:when>
                                                <c:otherwise><span class="status-icon icon-thunder">⛈️</span></c:otherwise>
                                            </c:choose>
                                        </td>
                                    <tr>
                                    </tr>
                                        <td>전체 가게 수</td>
                                        <td>${storeCount}개</td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${storeCount >= 500}"><span class="status-icon icon-sunny">☀️</span></c:when>
                                                <c:when test="${storeCount >= 375}"><span class="status-icon icon-cloud">☁️</span></c:when>
                                                <c:otherwise><span class="status-icon icon-thunder">⛈️</span></c:otherwise>
                                            </c:choose>
                                        </td>
                                    <tr>
                                    </tr>
                                        <td>가게별 평균 유지기간</td>
                                        <td>${Placeholder}개월</td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${percentageOfSubscribeUser >= 100}"><span class="status-icon icon-sunny">☀️</span></c:when>
                                                <c:when test="${percentageOfSubscribeUser >= 75}"><span class="status-icon icon-cloud">☁️</span></c:when>
                                                <c:otherwise><span class="status-icon icon-thunder">⛈️</span></c:otherwise>
                                            </c:choose>
                                        </td>
                                    <tr>
                                    </tr>
                                        <td>사이트 총 이익</td>
                                        <td>${netProfitFormatted}만원</td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${percentageOfSubscribeUser >= 1000}"><span class="status-icon icon-sunny">☀️</span></c:when>
                                                <c:when test="${percentageOfSubscribeUser >= 750}"><span class="status-icon icon-cloud">☁️</span></c:when>
                                                <c:otherwise><span class="status-icon icon-thunder">⛈️</span></c:otherwise>
                                            </c:choose>
                                        </td>
                                    <tr>
                                </table>
                            </div>
                        </div>

                        <!-- Operational Status Table -->
                        <div class="container">
                            <table width="100%">
                                <tr>
                                    <th colspan="4">사이트 목표 달성률</th>
                                </tr>
                                <tr>
                                    <th>운영 항목</th>
                                    <th>달성 / 목표</th>
                                    <th>달성률</th>
                                    <th>달성현황</th>
                                </tr>
                                <tr>
                                    <td>구독자수</td>
                                    <td>${subscribeUserCount}명 / ${percentOfSubscribeUserCount}명</td>
                                    <td>${percentageOfSubscribeUser}%</td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${percentageOfSubscribeUser >= 100}"><span class="status-icon icon-sunny">☀️</span></c:when>
                                            <c:when test="${percentageOfSubscribeUser >= 75}"><span class="status-icon icon-cloud">☁️</span></c:when>
                                            <c:otherwise><span class="status-icon icon-thunder">⛈️</span></c:otherwise>
                                        </c:choose>
                                    </td>
                                </tr>
                                <tr>
                                    <td>매출 상승률(전체)</td>
                                    <td>${salesAmount}원 / ${percentOfSalesAmountForLastYear}원</td>
                                    <td>${percentageOfSalesAmount}%</td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${percentageOfSubscribeUser >= 100}"><span class="status-icon icon-sunny">☀️</span></c:when>
                                            <c:when test="${percentageOfSubscribeUser >= 75}"><span class="status-icon icon-cloud">☁️</span></c:when>
                                            <c:otherwise><span class="status-icon icon-thunder">⛈️</span></c:otherwise>
                                        </c:choose>
                                    </td>
                                </tr>
                                <tr>
                                    <td>가게 수</td>
                                    <td>${storeCount}개 / ${percentOfStoreCountForLastYear}개</td>
                                    <td>${percentageOfStoreCount}%</td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${percentageOfSubscribeUser >= 100}"><span class="status-icon icon-sunny">☀️</span></c:when>
                                            <c:when test="${percentageOfSubscribeUser >= 75}"><span class="status-icon icon-cloud">☁️</span></c:when>
                                            <c:otherwise><span class="status-icon icon-thunder">⛈️</span></c:otherwise>
                                        </c:choose>
                                    </td>
                                </tr>
                            </table>
                        </div>
                    </div>
                </div>
                <!-- End of Main Content -->
            </div>
            <!-- End of Content Wrapper -->
        </div>
    </div>
    <!-- End of Page Wrapper -->

    <!-- Scroll to Top Button -->
    <a class="scroll-to-top rounded" href="#page-top"><i class="fas fa-angle-up"></i></a>

    <!-- Bootstrap core JavaScript -->
    <script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.bundle.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery-easing/1.4.1/jquery.easing.min.js"></script>
    <script src="/js/sb-admin-2.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>

    <script>
        // Chart initialization
        // Chart.defaults.global.defaultFontFamily = 'Nunito', '-apple-system,system-ui,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif';
        // Chart.defaults.global.defaultFontColor = '#858796';

        var productLabels = [];
        <c:forEach var="category" items="${productList}">
            productLabels.push('${category}');
        </c:forEach>

        var percentageSales = [];
        <c:forEach var="percentage" items="${percentageSaleList}">
            percentageSales.push(${percentage});
        </c:forEach>

        var ctxLine = document.getElementById('myChart').getContext('2d');
        var myChart;
        var currentPeriod = 'monthly';

        var salesYear = /*[[${salesYear}]]*/ [];
        var salesYearTotalPrice = /*[[${salesYearTotalPrice}]]*/ [];

        <c:forEach var="year" items="${salesYear}">
            salesYear.push('${year}');
        </c:forEach>
        <c:forEach var="totalPrice" items="${salesYearTotalPrice}">
            salesYearTotalPrice.push('${totalPrice}');
        </c:forEach>

        var salesMonth = /*[[${salesMonth}]]*/ [];
        var salesMonthTotalPrice = /*[[${salesMonthTotalPrice}]]*/ [];

        <c:forEach var="month" items="${salesMonth}">
            salesMonth.push('${month}');
        </c:forEach>
        <c:forEach var="totalPrice" items="${salesMonthTotalPrice}">
            salesMonthTotalPrice.push('${totalPrice}');
        </c:forEach>

        var salesDays = /*[[${salesDays}]]*/ [];
        var salesTotalPrice = /*[[${salesTotalPrice}]]*/ [];

        <c:forEach var="day" items="${salesDays}">
            salesDays.push('${day}');
        </c:forEach>
        <c:forEach var="totalPrice" items="${salesTotalPrice}">
            salesTotalPrice.push('${totalPrice}');
        </c:forEach>

        function updateChart(period) {
            currentPeriod = period;
            var data = {};
            if (period === 'monthly') {
                data = { labels: salesMonth, values: salesMonthTotalPrice };
            } else if (period === 'yearly') {
                data = { labels: salesYear, values: salesYearTotalPrice };
            }

            var selectedStore = document.getElementById('storeSelect').value;

            if (myChart) {
                myChart.destroy();
            }

            myChart = new Chart(ctxLine, {
                type: 'line',
                data: {
                    labels: data.labels,
                    datasets: [{
                        label: '매출 평균',
                        data: data.values,
                        backgroundColor: 'rgba(75, 192, 192, 0.2)',
                        borderColor: 'rgba(75, 192, 192, 1)',
                        borderWidth: 1,
                    }]
                },
                options: {
                    scales: {
                        y: {
                            beginAtZero: false
                        }
                    }
                }
            });
        }

        function filterStore() {
            updateChart(currentPeriod);
        }

        updateChart('monthly');
    </script>
</body>

</html>

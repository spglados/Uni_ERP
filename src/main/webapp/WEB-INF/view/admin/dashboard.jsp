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
    <link href="/vendor/fontawesome-free/css/all.min.css" rel="stylesheet" type="text/css">
    <link href="https://fonts.googleapis.com/css?family=Nunito:200,300,400,600,700,800,900" rel="stylesheet">

    <!-- Custom styles for this template -->
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
            <li class="nav-item"><a class="nav-link" href="/admin/#"><i class="fas fa-fw fa-table"></i> <span>유저 관리</span></a></li>
            <li class="nav-item"><a class="nav-link" href="/admin/#"><i class="fas fa-fw fa-table"></i> <span>가게 관리</span></a></li>
            <li class="nav-item"><a class="nav-link" href="/admin/#"><i class="fas fa-fw fa-table"></i> <span>미정</span></a></li>
            <li class="nav-item"><a class="nav-link" href="/admin/#"><i class="fas fa-fw fa-table"></i> <span>미정</span></a></li>
            <li class="nav-item"><a class="nav-link" href="/admin/#"><i class="fas fa-fw fa-wrench"></i> <span>미정</span></a></li>
            <li class="nav-item"><a class="nav-link collapsed" href="#" data-toggle="collapse" data-target="#collapseTwo" aria-expanded="true" aria-controls="collapseTwo"><i class="fas fa-fw fa-cog"></i> <span>고객 지원</span></a>
                <div id="collapseTwo" class="collapse" aria-labelledby="headingTwo" data-parent="#accordionSidebar">
                    <div class="bg-white py-2 collapse-inner rounded">
                        <a class="collapse-item" href="/support/faq">FAQ 관리</a>
                        <a class="collapse-item" href="/support/qna">1:1 문의 관리</a>
                    </div>
                </div>
            </li>
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
                <!-- Topbar -->
                <nav class="navbar navbar-expand navbar-light bg-white topbar mb-4 static-top shadow">
                    <a class="navbar-brand" href="/admin/main">
                        <img class="img--logo" src="/images/logo/logo.png" style="height: 60px;">
                    </a>
                    <ul class="navbar-nav ml-auto">
                        <li class="nav-item"><a class="nav-link logout-btn" href="/user/logout"><i class="fas fa-sign-out-alt"></i> <span>로그아웃</span></a></li>
                    </ul>
                </nav>
                <!-- End of Topbar -->

                <!-- Begin Page Content -->
                <div class="container-fluid">
                    <!-- Page Heading -->
                    <div class="d-sm-flex align-items-center justify-content-between mb-4">
                        <h1 class="h3 mb-0 text-gray-800"></h1>
                    </div>

                    <!-- Content Row -->
                        <div class="col-xl-3 col-md-6 mb-4">
                            <div class="card border-left-primary shadow h-100 py-2">
                                <div class="card-body">
                                    <div class="row no-gutters align-items-center">
                                        <div class="col mr-2">
                                            <div class="text-xs font-weight-bold text-primary text-uppercase mb-1">추가</div>
                                            <div class="h5 mb-0 font-weight-bold text-gray-800"></div>
                                        </div>
                                        <div class="col-auto">
                                            <i class="fas fa-clipboard-list fa-2x text-gray-300"></i>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    <div class="row">

                            <div class="col-xl-4 col-lg-5">
                                <div class="card shadow mb-4">
                                    <div class="card-header py-3 d-flex flex-row align-items-center justify-content-between">
                                        <h6 class="m-0 font-weight-bold text-primary">상품 카테고리별 매출</h6>
                                    </div>
                                    <div class="card-body">
                                        <div class="chart-pie pt-4 pb-2">
                                            <canvas id="myPieChart"></canvas>
                                        </div>
                                        <div class="mt-4 text-center small">
                                            <c:forEach var="category" items="${productList}">
                                                <span class="mr-2"><i class="fas fa-circle text-success"></i>${category}</span>
                                            </c:forEach>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        <!-- Sales Chart -->
                        <div class="col-xl-8 col-lg-8">
                            <div class="card shadow mb-4">
                                <div class="card-header py-3 d-flex flex-row align-items-center justify-content-between">
                                    <h6 class="m-0 font-weight-bold text-primary">가게별 매출 평균</h6>
                                </div>
                                <div>
                                    <button onclick="updateChart('daily')">일별</button>
                                    <button onclick="updateChart('monthly')">월별</button>
                                    <button onclick="updateChart('yearly')">연별</button>
                                    <label for="storeSelect">가게 선택:</label>
                                    <select id="storeSelect" onchange="filterStore()">
                                        <option value="all">모든 가게</option>
                                        <option value="store1">가게 1</option>
                                        <option value="store2">가게 2</option>
                                        <option value="store3">가게 3</option>
                                    </select>
                                    <canvas id="myChart"></canvas>
                                </div>
                            </div>
                        </div>

                        <!-- Operational Status Table -->
                        <div class="container">
                            <table width="100%">
                                <tr>
                                    <th colspan="4">사이트 운영현황</th>
                                </tr>
                                <tr>
                                    <th>운영 항목</th>
                                    <th>달성 / 목표</th>
                                    <th>달성률</th>
                                    <th>운영현황</th>
                                </tr>
                                <tr>
                                    <td>구독자수</td>
                                    <td>${subscribeUserCount}명 / ${percentOfSubscribeUserCount}명</td>
                                    <td>${percentageOfSubscribeUser}%</td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${percentageOfSubscribeUser >= 100}"><span class="status-icon icon-sunny">☀️</span></c:when>
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
                                            <c:when test="${percentageOfSalesAmount >= 100}"><span class="status-icon icon-sunny">☀️</span></c:when>
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
                                            <c:when test="${percentageOfStoreCount >= 100}"><span class="status-icon icon-sunny">☀️</span></c:when>
                                            <c:otherwise><span class="status-icon icon-thunder">⛈️</span></c:otherwise>
                                        </c:choose>
                                    </td>
                                </tr>
                            </table>

                            <!-- Category Sales Chart -->
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
    <script src="/vendor/jquery/jquery.min.js"></script>
    <script src="/vendor/bootstrap/js/bootstrap.bundle.min.js"></script>
    <script src="/vendor/jquery-easing/jquery.easing.min.js"></script>
    <script src="/js/sb-admin-2.min.js"></script>
    <script src="/vendor/chart.js/Chart.min.js"></script>

    <script>
        // Chart initialization
        Chart.defaults.global.defaultFontFamily = 'Nunito', '-apple-system,system-ui,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif';
        Chart.defaults.global.defaultFontColor = '#858796';

        var productLabels = [];
        <c:forEach var="category" items="${productList}">
            productLabels.push('${category}');
        </c:forEach>

        var percentageSales = [];
        <c:forEach var="percentage" items="${percentageSaleList}">
            percentageSales.push(${percentage});
        </c:forEach>

        var ctx = document.getElementById("myPieChart");
        var myPieChart = new Chart(ctx, {
            type: 'doughnut',
            data: {
                labels: productLabels,
                datasets: [{
                    data: percentageSales,
                    backgroundColor: ['#4e73df', '#1cc88a', '#36b9cc', '#f6c23e', '#e74a3b'],
                    hoverBackgroundColor: ['#2e59d9', '#17a673', '#2c9faf', '#f6b93e', '#e74c3c'],
                    hoverBorderColor: "rgba(234, 236, 244, 1)",
                }],
            },
            options: {
                maintainAspectRatio: false,
                tooltips: {
                    backgroundColor: "rgb(255,255,255)",
                    bodyFontColor: "#858796",
                    borderColor: '#dddfeb',
                    borderWidth: 1,
                    xPadding: 15,
                    yPadding: 15,
                    displayColors: false,
                    caretPadding: 10,
                },
                legend: {
                    display: false
                },
                cutoutPercentage: 80,
            },
        });

        var ctxLine = document.getElementById('myChart').getContext('2d');
        var myChart;
        var currentPeriod = 'daily';

        function updateChart(period) {
            currentPeriod = period;
            var data = {};
            if (period === 'daily') {
                data = { labels: ['2024-10-01', '2024-10-02', '2024-10-03', '2024-10-04', '2024-10-05'], values: [120, 150, 180, 90, 220] };
            } else if (period === 'monthly') {
                data = { labels: ['2024-10', '2024-11', '2024-12'], values: [3000, 4000, 3500] };
            } else if (period === 'yearly') {
                data = { labels: ['2022', '2023', '2024'], values: [25000, 30000, 28000] };
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
                            beginAtZero: true
                        }
                    }
                }
            });
        }

        function filterStore() {
            updateChart(currentPeriod);
        }

        updateChart('daily');
    </script>
</body>

</html>

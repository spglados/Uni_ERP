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
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css" rel="stylesheet" type="text/css">
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
            <li class="nav-item"><a class="nav-link" href="/admin/main"><i class="fas fa-fw fa-tachometer-alt"></i> <span>대시보드</span></a></li>
            <li class="nav-item"><a class="nav-link" href="/admin/userManagement"><i class="fas fa-fw fa-table"></i> <span>유저 관리</span></a></li>
            <li class="nav-item"><a class="nav-link" href="/admin/storeManagement"><i class="fas fa-fw fa-table"></i> <span>가게 관리</span></a></li>
            <li class="nav-item active"><a class="nav-link" href="/admin/salesManagement"><i class="fas fa-fw fa-table"></i> <span>매출 관리</span></a></li>
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
                    <div class="col-xl-10 col-lg-8">
                        <div class="card shadow mb-4">
                            <div class="card-header py-3 d-flex flex-row align-items-center justify-content-between">
                                <h6 class="m-0 font-weight-bold text-primary">전체 가게 매출 평균</h6>
                            </div>
                            <div>
                                <button onclick="updateChart('daily')">일별</button>
                                <button onclick="updateChart('monthly')">월별</button>
                                <button onclick="updateChart('yearly')">연별</button>
                                <label for="storeSelect">가게 선택:</label>
                                <select id="storeSelect" onchange="filterStore()">
                                    <option value="">모든 가게</option>
                                    <c:forEach var="store" items="${storeList}">
                                        <option value="${store.id}">${store.name}</option>
                                    </c:forEach>
                                </select>
                                <canvas id="myChart"></canvas>
                            </div>
                        </div>
                    </div>
                </div>
                <!-- End of Page Content -->
            </div>
        </div>
        <!-- End of Content Wrapper -->

        <!-- Scroll to Top Button -->
        <a class="scroll-to-top rounded" href="#page-top"><i class="fas fa-angle-up"></i></a>

        <!-- Bootstrap core JavaScript -->
        <script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>
        <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.bundle.min.js"></script>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery-easing/1.4.1/jquery.easing.min.js"></script>
        <script src="/js/sb-admin-2.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
    </div>
    <script>
        function updateChart(period) {
            currentPeriod = period;
            var selectedStore = document.getElementById('storeSelect').value;

            // AJAX 요청을 통해 매출 데이터 가져오기
            $.ajax({
                url: '/api/salesData', // 매출 데이터를 가져올 API 엔드포인트
                method: 'GET',
                data: {
                    period: currentPeriod,
                    storeId: selectedStore
                },
                success: function(data) {
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
            });
        }

        function filterStore() {
            updateChart(currentPeriod);
        }
        updateChart('daily');
    </script>
</body>

</html>

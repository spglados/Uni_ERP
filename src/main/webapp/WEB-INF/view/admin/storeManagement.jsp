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
                        <h1 class="h3 mb-0 text-gray-800">가게 관리</h1>
                    </div>

                    <!-- DataTales Example -->
                    <div class="card shadow mb-4">
                        <div class="card-body">
                            <div class="table-responsive">
                                <table class="table table-bordered" id="dataTable" width="100%" cellspacing="0">
                                    <thead>
                                        <tr>
                                            <th>번호</th>
                                            <th>가게이름</th>
                                            <th>사장님</th>
                                            <th>24시간</th>
                                            <th>상태</th>
                                            <th>등록일</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach var="store" items="${storeList}">
                                            <tr>
                                                <td>${store.id}</td>
                                                <td><a href="#" onclick="openStoreDetails(${store.id})">${store.name}</a></td>
                                                <td>${store.userName}</td>
                                                <td>${store.is24Hours == 1 ? 'O' : store.is24Hours == 0 ? 'X' : '비활성화'}</td>
                                                <td>${store.is24Hours == 1 ? 'Open' : store.is24Hours == 0 ? 'Close' : '비활성화'}</td>
                                                <td>${store.createdAt}</td>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>
                <!-- End of Page Content -->
            </div>
        </div>
        <!-- End of Content Wrapper -->
        <!-- Modal -->
        <div class="modal fade" id="storeDetailsModal" tabindex="-1" role="dialog" aria-labelledby="storeDetailsModalLabel" aria-hidden="true">
            <div class="modal-dialog modal-lg" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="storeDetailsModalLabel">Store Details</h5>
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <div class="modal-body" id="storeDetailsModalBody">
                        <table class="table table-striped">
                            <thead>
                                <tr>
                                    <th>Field</th>
                                    <th>Value</th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr>
                                    <th>ID</th>
                                    <td id="storeId"></td>
                                </tr>
                                <tr>
                                    <th>Name</th>
                                    <td id="storeName"></td>
                                </tr>
                                <tr>
                                    <th>24 Hours</th>
                                    <td id="storeIs24Hours"></td>
                                </tr>
                                <tr>
                                    <th>Open</th>
                                    <td id="storeIsOpen"></td>
                                </tr>
                                <tr>
                                    <th>Created At</th>
                                    <td id="storeCreatedAt"></td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
                    </div>
                </div>
            </div>
        </div>
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
        function openStoreDetails(storeId) {
            fetch('/admin/store/details/' + storeId)
                .then(response => response.json())
                .then(data => {
                    $('#storeId').text(data.id);
                    $('#storeName').text(data.name);
                    $('#storeIs24Hours').text(data.is24Hours === 1 ? 'Yes' : 'No');
                    $('#storeIsOpen').text(data.isOpen === 1 ? 'Yes' : 'No');
                    $('#storeCreatedAt').text(data.createdAt);
                    $('#storeDetailsModal').modal('show');
                })
                .catch(error => console.error('Error:', error));
        }
    </script>
</body>

</html>

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
    <link href="${pageContext.request.contextPath}/css/sb-admin-2.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/admin-page.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/img/favicon.ico" rel="icon">
</head>

<body id="page-top">
    <!-- Page Wrapper -->
    <div id="wrapper">
        <!-- Sidebar -->
        <ul class="navbar-nav bg-gradient-primary sidebar sidebar-dark accordion" id="accordionSidebar">
            <!-- Sidebar - Brand -->
            <a class="sidebar-brand d-flex align-items-center justify-content-center" href="${pageContext.request.contextPath}/admin/main">
                <div class="sidebar-brand-icon rotate-n-15">
                    <i class="fas fa-laugh-wink"></i>
                </div>
                <div class="sidebar-brand-text mx-3">관리자 페이지</div>
            </a>
            <hr class="sidebar-divider my-0">

            <!-- Nav Items -->
            <li class="nav-item active"><a class="nav-link" href="${pageContext.request.contextPath}/admin/main"><i class="fas fa-fw fa-tachometer-alt"></i> <span>대시보드</span></a></li>
            <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/admin/userManagement"><i class="fas fa-fw fa-table"></i> <span>유저 관리</span></a></li>
            <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/admin/storeManagement"><i class="fas fa-fw fa-table"></i> <span>가게 관리</span></a></li>
            <li class="nav-item"><a class="nav-link collapsed" href="#" data-toggle="collapse" data-target="#collapseTwo" aria-expanded="true" aria-controls="collapseTwo"><i class="fas fa-fw fa-cog"></i> <span>고객 지원</span></a>
                <div id="collapseTwo" class="collapse" aria-labelledby="headingTwo" data-parent="#accordionSidebar">
                    <div class="bg-white py-2 collapse-inner rounded">
                        <a class="collapse-item" href="${pageContext.request.contextPath}/admin/noticeList">공지사항</a>
                        <a class="collapse-item" href="${pageContext.request.contextPath}/admin/contactList">1:1 문의 관리</a>
                        <a class="collapse-item" href="${pageContext.request.contextPath}/admin/refund">환불요청 처리</a>
                    </div>
                </div>
            </li>
            <hr class="sidebar-divider my-0">
            <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/admin/logout"><i class="fas fa-fw fa-table"></i> <span>로그아웃</span></a></li>
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
                                                <td>${store.isOpen == 1 ? 'Open' : store.isOpen == 0 ? 'Close' : '비활성화'}</td>
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
                        <h5 class="modal-title" id="storeDetailsModalLabel">세부 정보</h5>
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <div class="modal-body" id="storeDetailsModalBody">
                        <table class="table table-striped">
                            <thead>
                                <tr>
                                    <th>필드</th>
                                    <th>내용</th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr>
                                    <th>ID</th>
                                    <td id="storeId"></td>
                                </tr>
                                <tr>
                                    <th>이름</th>
                                    <td id="storeName"></td>
                                </tr>
                                <tr>
                                    <th>24 시간</th>
                                    <td id="storeIs24Hours"></td>
                                </tr>
                                <tr>
                                    <th>상태</th>
                                    <td id="storeIsOpen"></td>
                                </tr>
                                <tr>
                                    <th>생성일</th>
                                    <td id="storeCreatedAt"></td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-warning" id="editButton">수정</button>
                        <button type="button" class="btn btn-danger" id="deleteButton">삭제</button>
                        <button type="button" class="btn btn-secondary" data-dismiss="modal">닫기</button>
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
        <script src="${pageContext.request.contextPath}/js/sb-admin-2.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
    </div>

    <script>
        function openStoreDetails(storeId) {
            fetch('/admin/store/details/' + storeId)
                .then(response => response.json())
                .then(data => {
                    $('#storeId').text(data.id);
                    $('#storeName').text(data.name);
                    $('#storeIs24Hours').text(data.is24Hours === 1 ? 'O' : 'X');
                    $('#storeIsOpen').text(data.isOpen === 1 ? 'Open' : 'Closed');
                    $('#storeCreatedAt').text(data.createdAt);
                    $('#storeDetailsModal').modal('show');
                })
                .catch(error => console.error('Error:', error));
        }

    let storeDetailsModalBody = document.getElementById('storeDetailsModalBody');
    let editButton = document.getElementById('editButton');
    let storeName = document.getElementById('storeName');
    let is24Hours = document.getElementById('storeIs24Hours');
    let isOpen = document.getElementById('storeIsOpen');

    editButton.addEventListener('click', function() {
      if (editButton.textContent === '수정') {
        storeName.contentEditable = 'true';
        is24Hours.contentEditable = 'true';
        isOpen.contentEditable = 'true';
        editButton.textContent = '저장';

        const isOpenSelect = document.createElement('select');
        isOpenSelect.id = 'isOpenSelect';
        isOpenSelect.innerHTML = "<option value=\"1\">Open</option><option value=\"0\">Closed</option>";
        isOpen.parentNode.replaceChild(isOpenSelect, isOpen);

        const is24HoursSelect = document.createElement('select');
        is24HoursSelect.id = 'is24HoursSelect';
        is24HoursSelect.innerHTML = "<option value=\"1\">O</option><option value=\"0\">X</option>";
        is24Hours.parentNode.replaceChild(is24HoursSelect, is24Hours);

        isOpenSelect.addEventListener('change', function() {
          isOpen.textContent = isOpenSelect.value === '1' ? 'Open' : 'Closed';
        });

        is24HoursSelect.addEventListener('change', function() {
          is24Hours.textContent = is24HoursSelect.value === '1' ? 'O' : 'X';
        });

      } else {
        if (confirm('정말로 저장하시겠습니까?')) {
          storeName.contentEditable = 'false';
          is24Hours.contentEditable = 'false';
          editButton.textContent = '수정';

          const isOpenSelect = document.getElementById('isOpenSelect'); // Get the isOpenSelect element
          const isOpenDropdown = document.createElement('div');
          isOpenDropdown.id = 'storeIsOpen';
          isOpenDropdown.textContent = isOpenSelect.value === '1' ? 'Open' : 'Closed';
          isOpenSelect.parentNode.replaceChild(isOpenDropdown, isOpenSelect); // Replace isOpenSelect with isOpenDropdown
          isOpen = isOpenDropdown; // Update the isOpen variable to reference the new element

          const is24HoursSelect = document.getElementById('is24HoursSelect'); // Get the is24HoursSelect element
          const is24HoursDropdown = document.createElement('div');
          is24HoursDropdown.id = 'storeIs24Hours';
          is24HoursDropdown.textContent = is24HoursSelect.value === '1' ? 'O' : 'X';
          is24HoursSelect.parentNode.replaceChild(is24HoursDropdown, is24HoursSelect); // Replace is24HoursSelect with is24HoursDropdown
          is24Hours = is24HoursDropdown; // Update the is24Hours variable to reference the new element

          const updatedData = {
            name: storeName.textContent,
            is24Hours: is24Hours.textContent === '예' ? 1 : 0,
            isOpen: isOpen.textContent === '열림' ? 1 : 0
          };

          const storeCell = document.querySelector('td[id="storeId"]');
          const storeId = storeCell.textContent.trim();
          fetch('/admin/store/update/' + storeId, {
            method: 'PUT',
            headers: {
              'Content-Type': 'application/json'
            },
            body: JSON.stringify(updatedData)
          })
          .then(response => response.json())
          .then(data => alert(data.message))
          .catch(error => alert('Error: ' + error))
          .finally(() => window.location.reload());
        }
      }
    });

    const deleteButton = document.getElementById('deleteButton');
    deleteButton.addEventListener('click', function() {
      const storeId = $('#storeId').text();
      fetch('/admin/store/delete/' + storeId, {
        method: 'DELETE'
      })
      .then(response => response.text())
      .then(data => {
        alert(data);
        location.reload();
      })
      .catch(error => console.error('Error:', error));
    });
    </script>
</body>

</html>

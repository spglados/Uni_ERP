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
                        <h1 class="h3 mb-0 text-gray-800">유저 관리</h1>
                    </div>

                    <!-- DataTales Example -->
                    <div class="card shadow mb-4">
                        <div class="card-body">
                            <div class="table-responsive">
                                <table class="table table-bordered" id="dataTable" width="100%" cellspacing="0">
                                    <thead>
                                        <tr>
                                            <th>번호</th>
                                            <th>이름</th>
                                            <th>이메일</th>
                                            <th>멤버십</th>
                                            <th>주소</th>
                                            <th>가입일</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach var="user" items="${userList}">
                                            <tr>
                                                <td>${user.id}</td>
                                                <td><a href="#" onclick="openUserDetails(${user.id})">${user.name}</a></td>
                                                <td>${user.email}</td>
                                                <td>${user.membership == 'COMMON' ? '일반회원' : user.membership == 'PREMIUM' ? '프리미엄회원' : '비활성화'}</td>
                                                <td>${user.address}</td>
                                                <td>${user.createdAt}</td>
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
        <div class="modal fade" id="userDetailsModal" tabindex="-1" role="dialog" aria-labelledby="userDetailsModalLabel" aria-hidden="true">
            <div class="modal-dialog modal-lg" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="userDetailsModalLabel">세부 정보</h5>
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <div class="modal-body" id="userDetailsModalBody">
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
                                    <td id="userId"></td>
                                </tr>
                                <tr>
                                    <th>이름</th>
                                    <td id="userName"></td>
                                </tr>
                                <tr>
                                    <th>이메일</th>
                                    <td id="userEmail"></td>
                                </tr>
                                <tr>
                                    <th>연락처</th>
                                    <td id="userPhone"></td>
                                </tr>
                                <tr>
                                    <th>주소</th>
                                    <td id="userAddress"></td>
                                </tr>
                                <tr>
                                    <th>멤버십</th>
                                    <td id="userMembership"></td>
                                </tr>
                                <tr>
                                    <th>생성일</th>
                                    <td id="userCreatedAt"></td>
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
        <script>
        function openUserDetails(userId) {
            $('#userDetailsModal').modal('show');

            fetch('/admin/user/details/' + userId)
                .then(response => response.json())
                .then(data => {
                    $('#userId').text(data.id);
                    $('#userName').text(data.name);
                    $('#userEmail').text(data.email);
                    $('#userPhone').text(data.phone);
                    $('#userAddress').text(data.address);
                    $('#userMembership').text(data.membership);
                    $('#userCreatedAt').text(data.createdAt);
                })
                .catch(error => console.error('Error:', error));
        }

        const userDetailsModal = document.getElementById('userDetailsModal');
        const editButton = document.getElementById('editButton');
        let saveButton = document.getElementById('saveButton');

        editButton.addEventListener('click', () => {
          const fields = userDetailsModal.querySelectorAll('td');
          fields.forEach((field, index) => {
            if (index !== 0 && field.id !== 'userId' && field.id !== 'userCreatedAt') {
              if (field.id === 'userMembership') {
                const select = document.createElement('select');
                select.id = 'userMembership';
                const options = [
                  { value: 'COMMON', text: 'COMMON' },
                  { value: 'PREMIUM', text: 'PREMIUM' },
                ];
                options.forEach((option) => {
                  const optionElement = document.createElement('option');
                  optionElement.value = option.value;
                  optionElement.text = option.text;
                  if (field.textContent === option.value) {
                    optionElement.selected = true;
                  }
                  select.appendChild(optionElement);
                });
                field.innerHTML = ''; // Remove the text content of the td element
                field.appendChild(select); // Append the select element to the td element
              } else {
                field.contentEditable = 'true';
              }
            }
          });

          saveButton = document.createElement('button');
          saveButton.textContent = '저장';
          saveButton.className = 'btn btn-primary';
          saveButton.id = 'saveButton';
          editButton.parentNode.replaceChild(saveButton, editButton);

          saveButton.addEventListener('click', () => {
            const updatedFields = userDetailsModal.querySelectorAll('td');
            const updatedData = {};
            updatedFields.forEach((field, index) => {
              if (index !== 0 && field.id !== 'userId') {
                if (field.querySelector('select')) {
                  const selectElement = field.querySelector('select');
                  const selectedOption = selectElement.querySelector('option:checked');
                  updatedData[field.id.replace('user', '').toLowerCase()] = selectedOption.value;
                } else {
                  updatedData[field.id.replace('user', '').toLowerCase()] = field.textContent;
                }
              }
            });

            alert('정말로 수정하시겠습니까?');
            const storeCell = document.querySelector('td[id="userId"]');
            const userId = storeCell.textContent.trim();
            fetch('/admin/user/update/' + userId, {
              method: 'PUT',
              headers: { 'Content-Type': 'application/json' },
              body: JSON.stringify(updatedData),
            })
              .then((response) => response.json())
              .then((data) => alert(data.message))
              .catch((error) => console.error(error))
              .finally(() => {
                location.reload();
              });
          });
        });

        const deleteButton = document.getElementById('deleteButton');
            deleteButton.addEventListener('click', function() {
              const userId = $('#userId').text();
              fetch('/admin/user/delete/' + userId, {
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
    </div>
</body>

</html>

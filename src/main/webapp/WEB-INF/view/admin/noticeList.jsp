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
                        <h1 class="h3 mb-0 text-gray-800">ㅎㄱㅁㅇ</h1>
                    </div>

                    <div class="col-xl-10 col-lg-8">
                        <div class="card shadow mb-4">
                            <div class="card-header py-3 d-flex flex-row align-items-center justify-content-between">
                                <h6 class="m-0 font-weight-bold text-primary">공지사항 목록</h6>
                            </div>
                            <div>
                                <section>
                                    <table class="table table-striped">
                                        <thead>
                                            <tr>
                                                <th>번호</th>
                                                <th>제목</th>
                                                <th>작성일</th>
                                                <th>조회수</th>
                                                <th>작업</th> <!-- New column for delete button -->
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <c:forEach var="notice" items="${notices.content}" varStatus="status">
                                                <tr>
                                                    <td>${status.index + 1 + (currentPage - 1) * pageSize}</td>
                                                    <td><a href="${pageContext.request.contextPath}/notice/detail?id=${notice.id}">${notice.title}</a></td>
                                                    <td>${notice.dateFormatter()}</td>
                                                    <td>${notice.views}</td>
                                                    <td>
                                                        <button class="btn btn-danger btn-sm" onclick="deleteNotice(${notice.id})">삭제</button>
                                                    </td>
                                                </tr>
                                            </c:forEach>
                                        </tbody>
                                    </table>

                                    <!-- Pagination and Write Button, Centered -->
                                    <div class="d-flex flex-column align-items-center mt-4">
                                        <!-- Pagination -->
                                        <div class="pagination mb-3">
                                            <c:if test="${notices.hasPrevious()}">
                                                <a href="${pageContext.request.contextPath}/admin/noticeList?page=${currentPage - 2}&size=${pageSize}" class="btn btn-primary">&laquo; 이전</a>
                                            </c:if>
                                            <c:forEach begin="1" end="${notices.totalPages}" var="i">
                                                <a href="${pageContext.request.contextPath}/admin/noticeList?page=${i - 1}&size=${pageSize}" class="btn ${i == currentPage ? 'btn-secondary active' : 'btn-light'}">${i}</a>
                                            </c:forEach>
                                            <c:if test="${notices.hasNext()}">
                                                <a href="${pageContext.request.contextPath}/admin/noticeList?page=${currentPage}&size=${pageSize}" class="btn btn-primary">다음 &raquo;</a>
                                            </c:if>
                                        </div>

                                        <!-- Write Button -->
                                        <button onclick="navigateToWrite()" class="btn btn-success">작성하기</button>
                                    </div>
                                </section>
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
<script src="${pageContext.request.contextPath}/js/sb-admin-2.min.js"></script>
<script>
    function deleteNotice(id) {
        if (confirm("정말로 삭제하시겠습니까?")) {
            fetch("/admin/notice/delete/" + id, {
                method: 'DELETE'
            })
            .then(response => {
                if (response.ok) {
                    alert('공지사항이 삭제되었습니다.');
                    location.reload();
                } else {
                    alert('삭제에 실패하였습니다. 다시 시도해 주세요.');
                }
            })
            .catch(error => {
                console.error('Error deleting notice:', error);
                alert('삭제 중 오류가 발생했습니다.');
            });
        }
    }

    function navigateToWrite() {
        window.location.href = '/admin/notice';
    }
</script>
</div>

</body>
</html>

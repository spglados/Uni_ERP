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
            <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/admin/main"><i class="fas fa-fw fa-tachometer-alt"></i> <span>대시보드</span></a></li>
            <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/admin/userManagement"><i class="fas fa-fw fa-table"></i> <span>유저 관리</span></a></li>
            <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/admin/storeManagement"><i class="fas fa-fw fa-table"></i> <span>가게 관리</span></a></li>
            <li class="nav-item"><a class="nav-link collapsed" href="#" data-toggle="collapse" data-target="#collapseTwo" aria-expanded="true" aria-controls="collapseTwo"><i class="fas fa-fw fa-cog"></i> <span>고객 지원</span></a>
                <div id="collapseTwo" class="collapse" aria-labelledby="headingTwo" data-parent="#accordionSidebar">
                    <div class="bg-white py-2 collapse-inner rounded">
                        <a class="collapse-item" href="${pageContext.request.contextPath}/admin/noticeList">공지사항</a>
                        <a class="collapse-item" href="${pageContext.request.contextPath}/admin/contactList">1:1 문의 관리</a>
                        <a class="collapse-item active" href="${pageContext.request.contextPath}/admin/refund">환불요청 처리</a>
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
                        <h1 class="h3 mb-0 text-gray-800">환불 요청 처리</h1>
                    </div>

                    <!-- DataTales Example -->
                    <div class="card shadow mb-4">
                        <div class="card-body">
                            <div class="table-responsive">
                                <table class="table table-bordered" id="dataTable" width="100%" cellspacing="0">
                                    <thead>
                                        <tr>
                                                        <th></th>
                                                        <th>상품 이름</th>
                                                        <th>상품 원가</th>
                                                        <th>결제된 금액</th>
                                                        <th>다음 달 결제 예정 금액</th>
                                                        <th>결제된 날짜</th>
                                                        <th>결제 수단</th>
                                                        <th>취소 사유</th>
                                                        <th></th>
                                                    </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach var="payment" items="${payments}">
                                                        <c:if test="${not empty payment.cancelReason}">
                                                            <tr>
                                                                <td><input type="radio" class="payment-radio" name="selectedPayment" value="${payment.id}" /></td>
                                                                <td>${payment.orderName}</td>
                                                                <td>${payment.amount}</td>
                                                                <td>${payment.nowPayAmount}</td>
                                                                <td>${payment.nextPayAmount}</td>
                                                                <td>${payment.date}</td>
                                                                <td>${payment.method}</td>
                                                                <td>
                                                                    <c:choose>
                                                                        <c:when test="${refund.cancelReason == 'simple'}">단순변심</c:when>
                                                                        <c:when test="${refund.cancelReason == 'cancelSubscribe'}">가게폐점</c:when>
                                                                        <c:when test="${refund.cancelReason == 'doublePay'}">중복결제</c:when>
                                                                        <c:otherwise>기타</c:otherwise>
                                                                    </c:choose>
                                                                </td>
                                                                <td>
                                                                    <c:if test="${payment.cancel != 'Y'}">
                                                                        <button onclick="cancelPayments('${payment.cancelReason}', '${payment.id}')">환불 승인</button>
                                                                    </c:if>
                                                                    <c:if test="${payment.cancel == 'Y'}">
                                                                        <button disabled>승인 완료</button>
                                                                    </c:if>
                                                                    <input type="hidden" class="payment-key" value="${payment.paymentKey}" />
                                                                </td>
                                                            </tr>
                                                        </c:if>
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
     function cancelPayments(cancelReason) {
             const selectedPayment = document.querySelector('.payment-radio:checked');

             if (!selectedPayment) {
                 alert('환불할 결제를 선택해주세요.');
                 return;
             }

             const paymentKey = selectedPayment.closest('tr').querySelector('.payment-key').value;
             const paymentRequest = {
                 paymentKey: paymentKey,
                 cancelReason: cancelReason,
                 payPk: selectedPayment.value // payment.id를 payPk로 사용
             };

             // 요청이 배열 형식으로 전송되도록 만듭니다.
             const paymentRequests = [paymentRequest];

             fetch('/payment/refund', {
                 method: 'POST',
                 headers: {
                     'Content-Type': 'application/json'
                 },
                 body: JSON.stringify(paymentRequests) // 배열로 전송
             })
             .then(response => {
                 if (response.ok) {
                     alert('승인 완료');
                     location.reload(); // 페이지를 리로드합니다.
                 } else {
                     console.error('Error canceling payments');
                 }
             })
             .catch(error => console.error('Fetch error:', error));
         }
    </script>
</body>

</html>

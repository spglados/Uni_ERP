<!DOCTYPE html>
<html lang="ko">
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/view/layout/header.jsp" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>마이페이지</title>
    <style>
        .sidebar {
            width: 200px;
            float: left;
            margin-right: 20px;
            border-right: 1px solid #ccc;
            padding: 10px;
        }
        .sidebar a {
            display: block;
            margin: 10px 0;
            text-decoration: none;
            color: #333;
        }
        .sidebar a:hover {
            color: #007bff;
        }
        .profile-info {
            overflow: hidden;
        }
        .selected {
            background-color: #f0f8ff; /* 선택된 항목 강조 */
        }
        .no-refund {
            color: red; /* 메시지 색상 */
        }
    </style>
</head>
<body>
    <div class="sidebar">
        <h3>내 정보</h3>
        <a href="/myPage">회원 정보 및 수정</a>
        <a href="#">가게 등록</a>
        <a href="/myPage/paymentHistory">결제 내역</a>
        <a href="/myPage/refundHistory">환불 내역</a>
        <a href="#">내 문의 내역</a>
    </div>

    <h1>환불 내역</h1>

    <c:if test="${empty refund}">
        <p class="no-refund">환불 내역이 없습니다.</p>
    </c:if>

    <c:if test="${not empty refund}">
        <table>
            <thead>
                <tr>
                    <th>환불 요청 ID</th>
                    <th>환불 금액</th>
                    <th>환불 사유</th>
                    <th>환불 승인 날짜</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="refund" items="${refund}">
                    <tr>
                        <td>${refund.id}</td> <!-- refund 객체의 ID -->
                        <td>${refund.cancelAmount}</td> <!-- refund 객체의 ID -->
                        <td>${refund.cancelReason}</td>
                        <td>${refund.approvedAt}</td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </c:if>

<%@ include file="/WEB-INF/view/layout/footer.jsp" %>
</body>
</html>

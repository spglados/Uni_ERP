<!DOCTYPE html>
<html lang="ko">
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/view/layout/header.jsp" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>가게 리스트</title>
    <style>
        .sidebar {
            height: 500px;
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
        <a href="/myPage/storeList">가게 등록</a>
        <a href="/myPage/paymentHistory">결제 내역</a>
        <a href="/myPage/refundHistory">환불 내역</a>
        <a href="/myPage/contact">내 문의 내역</a>
    </div>

    <h1>가게 등록</h1>

    <c:if test="${empty store and count == 0}">
        <p class="no-refund">보유중인 가게 없습니다.</p>
        <p>* 가게를 등록하시려면 결제를 먼저 진행해주세요. *</p>
        <button onclick="payment()">결제하러가기</button>
    </c:if>


    <c:if test="${not empty store}">
    <p>현재 보유중인 가게 수: ${storeCount} &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp 등록가능한 가게 수: ${count - storeCount}</p>

    <c:if test="${count - storeCount == 0}">
        <p class="no-refund">더 이상 가게를 등록할 수 없습니다.</p>
        <p>가게를 등록하시려면 결제를 진행해주세요. <button onclick="payment()">결제하러가기</button></p>
    </c:if>
    <table>
        <thead>
            <tr>
                <th>ID</th>
                <th>이름</th>
                <th>24시간 운영</th>
                <th>운영 상태</th>
                <th>주소</th>
                <th>등록일</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="store" items="${store}">
                <tr>
                    <td>${store.id}</td>
                    <td>${store.name}</td>
                    <td>${store.is24Hours == 1 ? '예' : '아니오'}</td>
                    <td>${store.isOpen == 1 ? '열림' : '닫힘'}</td>
                    <td>${store.storeAddress}</td>
                    <td><fmt:formatDate value="${store.createdAt}" pattern="yyyy-MM-dd HH:mm" /></td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
    </c:if>
    <c:if test="${storeCount < count}">
            <br><button onclick="saveStore()">가게 등록</button>
        </c:if>

<%@ include file="/WEB-INF/view/layout/footer.jsp" %>
</body>
</html>
<script>
    function saveStore() {
        window.open('http://localhost:8080/myPage/saveStore', '_blank', 'width=800,height=600');
    }
    function payment() {
        window.location.href = '/payment';
    }
</script>


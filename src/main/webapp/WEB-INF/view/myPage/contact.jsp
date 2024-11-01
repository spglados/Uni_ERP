<!DOCTYPE html>
<html lang="ko">
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/view/layout/header.jsp" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>내 문의 내역</title>
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
        <a href="/myPage/storeList">가게 등록</a>
        <a href="/myPage/paymentHistory">결제 내역</a>
        <a href="/myPage/refundHistory">환불 내역</a>
        <a href="/myPage/contact">내 문의 내역</a>
    </div>

    <h1>내 문의 내역</h1>

 <c:if test="${not empty contact}">
     <table>
         <thead>
             <tr>
                 <th>문의 제목</th>
                 <th>상태</th>
             </tr>
         </thead>
         <tbody>
             <c:forEach var="contact" items="${contact}">
                 <tr>
                     <td>
                         <a href="javascript:void(0);" onclick="openContactDetail(${contact.id})">${contact.title}</a>
                     </td>
                     <td>${contact.status.name() == 'OPEN' ? '답변 미완료' : '답변 완료'}</td>
                 </tr>
             </c:forEach>
         </tbody>
     </table>
 </c:if>

 <c:if test="${empty contact}">
     <div class="no-refund">문의하신 사항이 없습니다.</div>
 </c:if>


 <script>
 function openContactDetail(id) {
     const url = '/myPage/contactDetail/'+ id;
         console.log(url); // 생성된 URL 확인
     window.open(url, '_blank', 'width=800,height=600');
 }
 </script>


<%@ include file="/WEB-INF/view/layout/footer.jsp" %>
</body>
</html>

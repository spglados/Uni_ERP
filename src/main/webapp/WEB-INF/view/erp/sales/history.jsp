<%--
  Created by IntelliJ IDEA.
  User: namch
  Date: 24. 10. 10.
  Time: 오후 6:42
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/view/erp/layout/erpHeader.jsp"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>


<!-- 메인 컨텐츠 -->
<div class="content">

    <h1>매출 기록 입니다.</h1>

    <table border="1">
        <thead>
            <tr>
                <th>ID</th>
                <th>상품명</th>
                <th>수량</th>
                <th>판매일</th>
                <th>총 가격</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="record" items="${salesHistory}">
                <tr>
                    <td>${record.id}</td>
                    <td>${record.itemName}</td>
                    <td>${record.quantity}</td>
                    <td>${record.salesDate}</td>
                    <td>${record.totalPrice}</td>
                </tr>
            </c:forEach>
        </tbody>
    </table>

    <c:if test="${empty salesHistory}">
        <p>매출 기록이 없습니다.</p>
    </c:if>

</div>

<%@include file="/WEB-INF/view/erp/layout/erpFooter.jsp"%>

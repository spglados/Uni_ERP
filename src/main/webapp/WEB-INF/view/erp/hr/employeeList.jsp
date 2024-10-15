<%--
  Created by IntelliJ IDEA.
  User: namch
  Date: 24. 10. 10.
  Time: 오후 6:42
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@include file="/WEB-INF/view/erp/layout/erpHeader.jsp" %>

<!-- 메인 컨텐츠 -->
<div class="content">

    <h1>직원 목록</h1>
    <c:if test="${not empty employees}">
        <table>
            <thead>
            <tr>
                <th>사원번호</th>
                <th>이름</th>
                <th>생년월일</th>
                <th>성별</th>
                <th>이메일</th>
                <th>연락처</th>
                <th>직책</th>
                <th>상태</th>
                <th>은행</th>
                <th>게장번호</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="employee" items="${employees}">
                <tr>
                    <td>${employee.uniqueEmployeeNumber}</td>
                    <td>${employee.name}</td>
                    <td>${employee.birthday}</td>
                    <td>
                        <c:choose>
                            <c:when test="${employee.gender == 'M'}">남성</c:when>
                            <c:when test="${employee.gender == 'F'}">여성</c:when>
                            <c:otherwise>정보 없음</c:otherwise>
                        </c:choose>
                    </td>
                    <td>${employee.email}</td>
                    <td>${employee.phone}</td>
                    <td>${employee.position}</td>
                    <td>
                        <c:choose>
                            <c:when test="${employee.employmentStatus == 'ACTIVE'}">재직 중</c:when>
                            <c:when test="${employee.employmentStatus == 'INACTIVE'}">퇴사</c:when>
                            <c:when test="${employee.employmentStatus == 'ONLEAVE'}">휴직</c:when>
                        </c:choose>
                    </td>
                    <td>${employee.bank.name}</td>
                    <td>${employee.accountNumber}</td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </c:if>
    <c:if test="${empty employees}">
        <p>등록된 직원이 없습니다.</p>
    </c:if>

</div>

<%@include file="/WEB-INF/view/erp/layout/erpFooter.jsp" %>
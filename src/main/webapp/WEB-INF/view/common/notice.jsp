<%--
  Created by IntelliJ IDEA.
  User: namch
  Date: 24. 10. 10.
  Time: 오후 7:53
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@include file="/WEB-INF/view/layout/header.jsp"%>
<link rel="stylesheet" href="/css/common/notice.css">
<main class="main-container">

    <section>
        <h2>공지사항 목록</h2>
        <table>
            <thead>
                <tr>
                    <th>번호</th>
                    <th>제목</th>
                    <th>작성일</th>
                    <th>조회수</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="notice" items="${notices.content}" varStatus="status">
                    <tr>
                        <td>${status.index + 1 + (currentPage - 1) * pageSize}</td>
                        <td><a href="/notice/detail?id=${notice.id}">${notice.title}</a></td>
                        <td>${notice.dateFormatter()}</td>
                        <td>${notice.views}</td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>

        <div class="pagination">
            <c:if test="${notices.hasPrevious()}">
                <a href="/notice?page=${currentPage - 1}&size=${pageSize}">&laquo; 이전</a>
            </c:if>
            <c:forEach begin="1" end="${notices.totalPages}" var="i">
                <a href="/notice?page=${i - 1}&size=${pageSize}" class="${i == currentPage ? 'active' : ''}">${i}</a>
            </c:forEach>
            <c:if test="${notices.hasNext()}">
                <a href="/notice?page=${currentPage}&size=${pageSize}">다음 &raquo;</a>
            </c:if>
        </div>
    </section>

</main>

<%@include file="/WEB-INF/view/layout/footer.jsp"%>
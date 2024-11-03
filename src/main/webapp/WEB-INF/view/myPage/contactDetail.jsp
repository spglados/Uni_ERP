<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/view/layout/myPageHeader.jsp" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<main class="main-container">
    <div class="contact-detail">
        <h2>${contact.title}</h2>
        <p><strong>작성자 ID:</strong> ${contact.userId}&nbsp&nbsp&nbsp<strong>상태:</strong> ${contact.status.name() == 'OPEN' ? '답변 미완료' : '답변 완료'}</p>
        <p><strong>내용:</strong></p>
        <p>${contact.content}</p>

        <div class="response">
            <h3>답변</h3>
            <c:choose>
                    <c:when test="${not empty response}">
                        <p><strong>답변자:</strong> ${response.author}</p>
                        <p><strong>답변 내용:</strong> ${response.content}</p>
                    </c:when>
                    <c:otherwise>
                        <p>답변 준비중입니다.</p>
                    </c:otherwise>
                </c:choose>
        </div>

    </div>

</main>
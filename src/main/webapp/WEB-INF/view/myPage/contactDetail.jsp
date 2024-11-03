<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">

<main class="container mt-5">
    <div class="card mb-4">
        <div class="card-header">
            <h2>${contact.title}</h2>
        </div>
        <div class="card-body">
            <p><strong>상태:</strong> ${contact.status.name() == 'OPEN' ? '답변 미완료' : '답변 완료'}</p>
            <p><strong>내용:</strong></p>
            <p>${contact.content}</p>
        </div>
    </div>

    <div class="card">
        <div class="card-header">
            <h5>답변</h5>
        </div>
        <div class="card-body">
            <c:choose>
                <c:when test="${not empty response}">
                    <p><strong>답변자:</strong> ${response.author}</p>
                    <p><strong>답변 내용:</strong> ${response.content}</p>
                </c:when>
                <c:otherwise>
                    <p class="text-muted">답변 준비중입니다.</p>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</main>

<script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.9.2/dist/umd/popper.min.js"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>

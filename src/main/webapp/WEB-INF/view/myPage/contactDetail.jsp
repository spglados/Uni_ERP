<!DOCTYPE html>
<html lang="ko">
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>문의 상세 내역</title>
    <style>
        .contact-detail {
            max-width: 600px;
            margin: 20px auto;
            padding: 20px;
            border: 1px solid #ccc;
            border-radius: 5px;
        }
        .contact-detail h2 {
            margin-bottom: 20px;
        }
        .contact-detail p {
            margin: 10px 0;
        }
        .response {
            margin-top: 20px;
            border-top: 1px solid #ccc;
            padding-top: 10px;
        }
    </style>
</head>
<body>
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

</body>
</html>

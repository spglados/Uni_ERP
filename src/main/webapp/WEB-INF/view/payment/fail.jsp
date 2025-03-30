<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
</head>
<body>
<div class="container">
    <div class="success-icon">
        <svg class="crossmark" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 52 52" aria-label="결제 실패 아이콘" role="img">
            <circle class="crossmark__circle" cx="26" cy="26" r="24" fill="none"/>
            <path class="crossmark__cross" d="M16 16L36 36M36 16L16 36" fill="none"/>
        </svg>
    </div>
    <h1>
        죄송합니다 결제에 실패하였습니다!<br>
        다시 결제 시도 해주세요
    </h1>
    <br>
    <a href="${pageContext.request.contextPath}/payment">다시 시도하기</a>
</div>

</body>
</html>

<!-- /WEB-INF/views/header.jsp -->
<!DOCTYPE html>
<html lang="ko">
<head>
    <%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/pos/posMain.css">
    <title>POS 시스템</title>
</head>
<body>
<header>
    <h1>POS 시스템</h1>
    <nav>
        <ul>
            <li><a href="${pageContext.request.contextPath}/">홈</a></li>
            <li><a href="${pageContext.request.contextPath}/sales">판매</a></li>
            <li><a href="${pageContext.request.contextPath}/inventory">재고 관리</a></li>
        </ul>
    </nav>
</header>

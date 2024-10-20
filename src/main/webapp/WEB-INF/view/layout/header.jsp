<%--
  Created by IntelliJ IDEA.
  User: namch
  Date: 24. 10. 10.
  Time: 오전 10:19
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<html>
<head>
    <title>Title</title>
    <link rel="stylesheet" href="/css/common.css">
</head>
<body>

<header class="header">
    <div class="inner--header">
        <p class="h--logo"><a href="/main" class="none-a">Uni ERP</a></p>
        <ul class="nav">
            <li class="nav-item"><a href="/introduction">소개</a></li>
            <li class="nav-item"><a href="/notice">공지사항</a></li>
            <li class="nav-item"><a href="/erp/main" target="_blank">ERP</a></li>
            <li class="nav-item"><a href="/payment">결제</a></li>
            <li class="nav-item"><a href="/support">고객지원</a></li>
        </ul>
        <div class="header-icons">
            <a href="/user/login" class="none-a"><i class="fa-solid fa-sign-in-alt">로그인</i></a>
            <a href="#"><i class="fa-solid fa-clipboard"></i></a>
        </div>
    </div>
</header>


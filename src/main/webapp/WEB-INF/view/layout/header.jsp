<%--
  Created by IntelliJ IDEA.
  User: namch
  Date: 24. 10. 10.
  Time: 오전 10:19
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<html>
<head>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/animate.css/4.1.1/animate.min.css"/>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Gothic+A1:wght@400;800&display=swap" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/gh/orioncactus/pretendard/dist/web/static/pretendard.css" rel="stylesheet">
    <title>Title</title>
    <link rel="stylesheet" href="/css/common.css">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
</head>
<body>

<header class="header">
    <div class="site__header">
        <nav>
            <a href="/main" class="none-a">
                <img src="/images/logo/logo_clear.png" class="animate__animated animate__fadeIn" alt="포스로고"
                     style="height: 100px; width: 125px;">
            </a>
            <ul class="menu">
                <li><a href="/introduction">소개</a></li>
                <li><a href="/notice">공지사항</a></li>
                <li><a href="/erp/main" target="_blank">ERP</a></li>
                <li><a href="/payment">결제</a></li>
                <li><a href="/support">고객지원</a></li>
                <div class="toggle">
                    <span class="toggle--switch"></span>
                </div>
                <div class="header-icons">
                    <a href="/user/login" class="header-icon">로그인&nbsp;&nbsp;</a>
                    <i class="header-icon">/</i>
                    <a href="/user/join" class="header-icon">&nbsp;&nbsp;회원가입</a>
                </div>
            </ul>
        </nav>
    </div>
</header>


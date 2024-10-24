<%--
  Created by IntelliJ IDEA.
  User: namch
  Date: 24. 10. 10.
  Time: 오후 2:02
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!-- header.jsp  -->
<%@ include file="/WEB-INF/view/layout/header.jsp"%>
<link rel="stylesheet" href="/css/signIn.css">

<main class="main-container">

<div class="login-container" id="login-container">
    <img src="/images/logo/logo_clear.png" alt="로고">
       <h6>로그인 후 이용 해 주세요</h6>
    <h2>로그인</h2>
  <form action="/user/login" method="post">
    <input type="text" name="email" placeholder="Email" value="asd@asd.com" required>
    <input type="password" name="password" placeholder="Password" value="1234" required>
    <button type="submit">로그인</button>
  </form>
  <div class="signup-link">
    <p><a href="/user/join">회원가입</a></p>
  </div>
</div>

</main>


<!-- footer.jsp  -->
<%@ include file="/WEB-INF/view/layout/footer.jsp"%>
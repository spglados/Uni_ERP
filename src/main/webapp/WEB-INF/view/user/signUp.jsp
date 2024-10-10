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
<link rel="stylesheet" href="/css/signUp.css">

<main class="main-container">

  <div class="signup-container">
    <h2>회원가입</h2>
    <form action="/signup" method="post">
      <input type="text" name="fullname" placeholder="이름" required>
      <input type="email" name="email" placeholder="이메일 주소" required>
      <input type="text" name="username" placeholder="사용자 이름" required>
      <input type="password" name="password" placeholder="비밀번호" required>
      <input type="password" name="confirm_password" placeholder="비밀번호 확인" required>
      <button type="submit">회원가입</button>
    </form>
    <div class="login-link">
      <p>이미 계정이 있으신가요? <a href="/user/login">로그인하기</a></p>
    </div>
  </div>

</main>

<!-- footer.jsp  -->
<%@ include file="/WEB-INF/view/layout/footer.jsp"%>
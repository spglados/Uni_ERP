<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<link rel="stylesheet" href="/css/signIn.css">

<main class="main-container">

<div class="login-container" id="login-container">
    <img src="/images/logo/logo_clear.png" alt="로고">
    <h2>관리자 로그인</h2>
  <form action="/admin/login" method="post">
    <input type="text" name="username" placeholder="Username" value="testAdminName1" required maxlength="15" pattern="[a-zA-Z0-9]+">
    <input type="password" name="password" placeholder="Password" value="1234" required autocomplete="off">
    <button type="submit">로그인</button>
  </form>
</div>

</main>

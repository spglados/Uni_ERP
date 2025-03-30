<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<link rel="stylesheet" href="${pageContext.request.contextPath}/css/signIn.css">

<main class="main-container">

<div class="login-container" id="login-container">
    <img src="${pageContext.request.contextPath}/images/logo/logo_clear.png" alt="로고">
    <h2>관리자 로그인</h2>
  <form id="login-form">
    <input type="text" name="username" placeholder="Username" value="testAdminName1" required maxlength="15" pattern="[a-zA-Z0-9]+">
    <input type="password" name="password" placeholder="Password" value="1234" required autocomplete="off">
    <button type="submit">로그인</button>
  </form>
</div>

<script>
const loginForm = document.getElementById('login-form');

loginForm.addEventListener('submit', (e) => {
  e.preventDefault();

  const username = loginForm.username.value;
  const password = loginForm.password.value;

  fetch('/admin/login', {
      method: 'POST',
      headers: {
          'Content-Type': 'application/json',
      },
      body: JSON.stringify({ username, password }),
  })
  .then((response) => response.text())
  .then((data) => {
      if (data === 'success') {
          window.location.href = '/admin/main';
      } else {
          alert('아이디 / 비밀번호가 다릅니다');
      }
  })
  .catch((error) => console.error(error));
});
</script>

</main>

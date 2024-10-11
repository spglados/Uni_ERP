<%--
  Created by IntelliJ IDEA.
  User: namch
  Date: 24. 10. 11.
  Time: 오전 9:40
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>404 - 페이지를 찾을 수 없습니다</title>
  <link href="https://fonts.googleapis.com/css2?family=Roboto:wght@400;700&display=swap" rel="stylesheet">
  <link rel="stylesheet" href="styles.css">
  <style>
    body {
      background: linear-gradient(135deg, #f0f0f0, #e0e0e0);
      color: #343a40;
      font-family: 'Roboto', sans-serif;
      display: flex;
      justify-content: center;
      align-items: center;
      height: 100vh;
      margin: 0;
    }
    .container {
      text-align: center;
      max-width: 600px;
      padding: 40px;
      background: #ffffff;
      box-shadow: 0 10px 20px rgba(0, 0, 0, 0.15);
      border-radius: 20px;
      transition: transform 0.3s;
    }
    .container:hover {
      transform: translateY(-10px);
    }
    .container h1 {
      font-size: 8rem;
      font-weight: 700;
      margin: 0;
      color: #e63946;
    }
    .container p {
      font-size: 1.25rem;
      margin: 20px 0;
    }
    .home-link {
      display: inline-block;
      margin-top: 20px;
      padding: 15px 40px;
      font-size: 1rem;
      font-weight: 700;
      color: #ffffff;
      background: linear-gradient(90deg, #007bff, #0056b3);
      text-decoration: none;
      border-radius: 50px;
      box-shadow: 0 6px 12px rgba(0, 0, 0, 0.1);
      transition: background 0.3s, transform 0.3s;
    }
    .home-link:hover {
      background: linear-gradient(90deg, #0056b3, #003f8a);
      transform: scale(1.1);
    }
    .container img {
      max-width: 70%;
      height: auto;
      margin-bottom: 30px;
      border-radius: 15px;
      box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);
    }
  </style>
</head>
<body>
<div class="container">
  <img src="/images/404.jpg" alt="404 이미지">
  <h2><b>페이지를 찾을 수 없습니다.</b></h2>
  <p><br><br>요청하신 페이지가 삭제되었거나, <br><br>주소가 잘못 입력되었을 수 있습니다.</p>
  <a href="/main" class="home-link">홈으로 돌아가기</a>
</div>
</body>
</html>
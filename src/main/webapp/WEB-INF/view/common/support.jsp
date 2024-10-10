<%--
  Created by IntelliJ IDEA.
  User: namch
  Date: 24. 10. 10.
  Time: 오후 8:29
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/view/layout/header.jsp"%>
<link rel="stylesheet" href="/css/common/support.css">
<main class="main-container">

  <section>
    <h2>고객지원</h2>
    <p>고객님의 만족을 최우선으로 생각합니다. 궁금한 사항이나 도움이 필요하시면 아래의 연락처로 언제든지 문의해 주세요.</p>
  </section>

  <section>
    <h3>연락처 정보</h3>
    <ul>
      <li><strong>전화:</strong> 1234-5678</li>
      <li><strong>이메일:</strong> support@uni-erp.com</li>
      <li><strong>운영 시간:</strong> 월~금, 오전 9시 ~ 오후 6시</li>
    </ul>
  </section>

  <section>
    <h3>문의하기</h3>
    <form action="/support/contact" method="post">
      <div class="form-group">
        <label for="name">이름:</label>
        <input type="text" id="name" name="name" required>
      </div>
      <div class="form-group">
        <label for="email">이메일:</label>
        <input type="email" id="email" name="email" required>
      </div>
      <div class="form-group">
        <label for="message">문의 내용:</label>
        <textarea id="message" name="message" rows="5" required></textarea>
      </div>
      <button type="submit">문의하기</button>
    </form>
  </section>

</main>

<%@include file="/WEB-INF/view/layout/footer.jsp"%>

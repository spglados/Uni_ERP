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

  <div class="support-img-box">
    <img src="/images/support/support.jpg" alt="고객 지원 사진사진">
  </div>
  <section class="customer-support">
    <h2>고객지원</h2>
    <p>문의 사항이 있으신가요?</p>
    <p>고객님의 만족을 최우선으로 생각합니다. 궁금한 사항이나 도움이 필요하시면 아래의 연락처로 언제든지 문의해 주세요.</p>

    <div class="contact-info">
      <h3>연락처 정보</h3>
      <ul>
        <li><strong>전화:</strong> 12-345-6789</li>
        <li><strong>이메일:</strong> yena@abc.com</li>
        <li><strong>운영 시간:</strong> 월~금, 오전 9시 ~ 오후 6시</li>
      </ul>
    </div>

    <div class="contact-form">
      <h3>문의하기</h3>
      <form id="contact-container">
        <div class="form-group">
          <label for="name">이름:</label>
          <input type="text" id="name" name="name" required>
        </div>
        <div class="form-group">
          <label for="tel">전화번호:</label>
          <input type="tel" id="tel" name="tel" required placeholder="하이픈(-)을 제외하고 써주세요">
        </div>
        <div class="form-group">
          <label for="email">이메일:</label>
          <input type="email" id="email" name="email" required>
        </div>
        <div class="form-group">
          <label for="content">문의 내용:</label>
          <textarea id="content" name="content" rows="5" required style="resize: none;"></textarea>
        </div>
        <button type="submit" id="submit-btn">문의하기</button>
      </form>
    </div>
  </section>

</main>
<script src="/js/common/support.js"></script>
<%@include file="/WEB-INF/view/layout/footer.jsp"%>

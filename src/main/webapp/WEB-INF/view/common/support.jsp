<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/view/layout/header.jsp"%>

<!-- Font Awesome CDN 추가 -->
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">

<!-- Select2 CSS 추가 -->
<link href="https://cdn.jsdelivr.net/npm/select2@4.1.0-rc.0/dist/css/select2.min.css" rel="stylesheet"/>

<link rel="stylesheet" href="${pageContext.request.contextPath}/css/common/support.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/payment/subscribe.css"> <!-- 이미 고급스러운 스타일을 포함 -->

<main class="main-container">

  <section class="customer-support-section">
    <!-- 상단 영역: 소개 섹션과 이미지 -->
    <div class="top-section">
      <div class="intro-section">
        <i class="fas fa-headset intro-icon"></i>
        <h2 class="title--heading">고객지원</h2>
        <p>
          언제나 고객님의 만족을 최우선으로 생각합니다. 궁금한 사항이나 도움이 필요하시면 아래의 연락처로 언제든지 문의해 주세요.
        </p>
      </div>

      <div class="support-img-box">
        <img src="${pageContext.request.contextPath}/images/support/support.jpg" alt="고객 지원 이미지">
      </div>
    </div>

    <!-- 하단 영역: 연락처 정보와 문의하기 폼 -->
    <div class="bottom-section">
      <div class="support-content">
        <!-- 연락처 정보 -->
        <div class="contact-info">
          <h3>연락처 정보</h3>
          <ul>
            <li><i class="fas fa-phone-alt"></i> <strong>전화 </strong></li>
            <li>+82 051-123-4567</li>
            <br>
            <li><i class="fas fa-envelope"></i> <strong>이메일</strong></li>
            <li>unierp@gmail.com</li>
            <br>
            <li><i class="fas fa-clock"></i> <strong>운영 시간 </strong></li>
            <li>월~금, 오전 9시 ~ 오후 6시</li>
          </ul>
        </div>

        <!-- 문의하기 폼 -->
        <div class="contact-form">
          <h3>문의하기</h3>
          <form id="support-form" action="/submitSupport" method="post" style="width: 100%;">
            <div class="form-group">
              <label for="title"><i class="fas fa-heading"></i> 제목</label>
              <input type="text" id="title" name="title" required placeholder="문의 제목을 입력하세요">
            </div>
            <div class="form-group">
              <label for="content"><i class="fas fa-comment-dots"></i> 문의 내용</label>
              <textarea id="content" name="content" rows="5" required placeholder="문의 내용을 입력하세요"></textarea>
            </div>
            <button type="submit" id="submit-btn">문의하기</button>
          </form>
        </div>
      </div>
    </div>
  </section>

</main>

<script src="${pageContext.request.contextPath}/js/common/support.js"></script>
<%@ include file="/WEB-INF/view/layout/footer.jsp"%>

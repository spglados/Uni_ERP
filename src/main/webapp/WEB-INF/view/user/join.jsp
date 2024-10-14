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
<script src="https://cdn.jsdelivr.net/npm/sockjs-client/dist/sockjs.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/stompjs/lib/stomp.min.js"></script>
<link rel="stylesheet" href="/css/signUp.css">

<main class="main-container">

  <div class="signup-container">
    <h2>회원가입</h2>
    <form action="/user/join" method="post" onsubmit="return validateForm();">
      <input type="text" name="name" placeholder="이름" required>

      <!-- 비밀번호 입력 -->
      <input type="password" name="password" id="password" placeholder="비밀번호" required>
      <input type="password" name="confirm_password" id="confirm_password" placeholder="비밀번호 확인" required>
      <span id="passwordMessage" class="password-message"></span>

      <!-- 이메일 인증 -->
      <div>
      <input type="email" id="email" name="email" placeholder="이메일 주소" required>
      <p id="email--verification--status"></p>
      <button type="button" id="send--email--verification" class="btn btn-primary">이메일 인증하기</button>
      </div>

      <!-- 휴대폰 문자인증 -->
      <td>
      		<p>
      			<input id="phone" type="tel" name="phone" placeholder="핸드폰 번호" required>
      			<button type="button" id="phoneChk" class="doubleChk">인증번호 보내기</button><br/>

      			<input id="phone2" type="text" name="phone2" title="인증번호 입력" disabled required/>
      			<button type="button" id="phoneChk2" class="doubleChk">인증번호 확인</button>
      			<input type="hidden" id="phoneDoubleChk"/>
      		</p>
      	</td>

      <!-- 주소 입력 필드 -->
      <input type="text" id="basicAddress" placeholder="기본 주소" readonly>
      <input type="text" id="detailAddress" placeholder="상세 주소">
      <input type="button" class="check--btn" onclick="execDaumPostcode()" value="주소 검색"><br>

      <!-- 숨겨진 address 필드 -->
      <input type="hidden" id="fullAddress" name="address">

      <!-- 회원가입 버튼 -->
      <button type="submit">회원가입</button>
    </form>

    <div class="login-link">
      <p>이미 계정이 있으신가요? <a href="/user/login">로그인하기</a></p>
    </div>
  </div>

</main>

<!-- Daum 우편번호 및 Kakao Maps API 불러오기 -->
<script src="//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>
<script src="//dapi.kakao.com/v2/maps/sdk.js?appkey=3aea5e049cf7a27eae091e77ca0e1429&libraries=services"></script>

<script>
  // Daum 우편번호 API 실행
  function execDaumPostcode() {
    new daum.Postcode({
      oncomplete: function(data) {
        // 주소 검색 후 처리
        var addr = data.address; // 최종 주소 변수

        // 주소 정보를 해당 필드에 넣는다.
        document.getElementById("basicAddress").value = addr;

      }
    }).open(); // 주소 검색 창 열기
  }

  // 폼 검증
  function validateForm() {
    // 비밀번호 확인
    if (!validatePassword()) {
      return false; // 비밀번호 일치하지 않으면 폼 제출 중단
    }

    // 기본 주소 및 상세 주소 합치기
    var basicAddress = document.getElementById("basicAddress").value;
    var detailAddress = document.getElementById("detailAddress").value;

    if (!basicAddress) {
      alert("기본 주소를 입력해 주세요.");
      return false;
    }

    if (!detailAddress) {
      alert("상세 주소를 입력해 주세요.");
      return false;
    }

    // 합쳐진 주소를 숨겨진 필드에 저장
    var fullAddress = basicAddress + " " + detailAddress;
    document.getElementById("fullAddress").value = fullAddress;

    return true; // 모든 검증을 통과하면 폼 제출
  }

  // 비밀번호 확인 기능
  function validatePassword() {
    var password = document.getElementById("password").value;
    var confirmPassword = document.getElementById("confirm_password").value;
    var message = document.getElementById("passwordMessage");

    if (password !== confirmPassword) {
      message.textContent = "비밀번호가 일치하지 않습니다.";
      message.style.color = "red";
      return false; // 폼 제출 방지
    } else {
      message.textContent = "비밀번호가 일치합니다.";
      message.style.color = "green";
      return true; // 폼 제출 허용
    }
  }

  // 비밀번호 입력 시 실시간 확인
  document.getElementById('confirm_password').addEventListener('input', function() {
    var password = document.getElementById("password").value;
    var confirmPassword = this.value;
    var message = document.getElementById("passwordMessage");

    if (password !== confirmPassword) {
      message.textContent = "비밀번호가 일치하지 않습니다.";
      message.style.color = "red";
    } else {
      message.textContent = "비밀번호가 일치합니다.";
      message.style.color = "green";
    }
  });
</script>

<script src="/js/user/sendEmail.js"></script>
<script src="/js/user/sendSMS.js"></script>
<!-- footer.jsp  -->
<%@ include file="/WEB-INF/view/layout/footer.jsp"%>

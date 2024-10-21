<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/view/layout/header.jsp"%>
<script src="https://cdn.jsdelivr.net/npm/sockjs-client/dist/sockjs.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/stompjs/lib/stomp.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/jquery@3.7.1/dist/jquery.slim.min.js"></script>
<link rel="stylesheet" href="/css/signUp.css">

<main class="main-container">
  <div class="signup-container">
    <h2>회원가입</h2>

      <input type="text" name="name" id="name" placeholder="이름" required>
      <span id="nameMessage" class="name-Message"></span>

      <input type="password" name="password" id="password" placeholder="비밀번호" required>
      <span id="passwordMessage" class="password-message"></span>
      <input type="password" name="confirm_password" id="confirm_password" placeholder="비밀번호 확인" required>
      <span id="passwordCheckMessage" class="password-message"></span>

      <div>
        <input type="email" id="email" name="email" placeholder="이메일 주소" required>
        <span id="emailMessage" class="email-message"></span>
        <p id="email--verification--status"></p>
        <button type="button" class="check--btn" id="checkId">이메일 중복 확인</button>
        <button type="button" id="send--email--verification" class="btn btn-primary">이메일 인증하기</button>
      </div>

      <p>
        <input id="phone" type="tel" name="phone" placeholder="핸드폰 번호" required>
        <span id="phoneMessage" class="phone-message"></span>
        <button type="button" id="checkPhone" class="check--btn">휴대폰 번호 확인</button>
        <button type="button" id="phoneChk" class="doubleChk">인증번호 보내기</button><br/>
        <input id="phone2" type="text" name="phone2" title="인증번호 입력" disabled required/>
        <span class="point successPhoneChk">휴대폰 번호 입력후 인증번호 보내기를 해주십시오.</span>
        <button type="button" id="phoneChk2" class="doubleChk">인증번호 확인</button>
        <input type="hidden" id="phoneDoubleChk"/>
      </p>

      <input type="text" id="basicAddress" placeholder="기본 주소" readonly>
      <input type="text" id="detailAddress" placeholder="상세 주소">
      <input type="button" class="check--btn" onclick="execDaumPostcode()" value="주소 검색"><br>
      <input type="hidden" id="fullAddress" name="address">
      <form id="signupForm">
      <button type="submit" id="signupButton">회원가입</button>
      </form>


    <div class="login-link">
      <p>이미 계정이 있으신가요? <a href="/user/login">로그인하기</a></p>
    </div>
  </div>
</main>

<script src="//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>
<script src="//dapi.kakao.com/v2/maps/sdk.js?appkey=3aea5e049cf7a27eae091e77ca0e1429&libraries=services"></script>

<script>
    let isEmailValid = false;
    let isPhoneValid = false;
    let isPasswordValid = false;

  function execDaumPostcode() {
    new daum.Postcode({
      oncomplete: function(data) {
        var addr = data.address;
        document.getElementById("basicAddress").value = addr;
      }
    }).open();
  }

  function validateForm() {
      // 각 유효성 검사 결과를 콘솔에 출력
      console.log("Password Valid:", isPasswordValid);
      console.log("Email Valid:", isEmailValid);
      console.log("Phone Valid:", isPhoneValid);
      console.log("validateName",validateName());
      console.log("validateAddress",validateAddress());

      if (!isPasswordValid || !isEmailValid || !isPhoneValid || validateName() || validateAddress()) {
          return false; // 모든 유효성 검사를 통과해야 폼 제출
      }

      var basicAddress = document.getElementById("basicAddress").value;
      var detailAddress = document.getElementById("detailAddress").value;

      var fullAddress = basicAddress + " " + detailAddress;
      document.getElementById("fullAddress").value = fullAddress;

      return true;
  }

function validateAddress() {
    var basicAddress = document.getElementById("basicAddress").value;


    if (!basicAddress) {
        alert("기본 주소를 입력해 주세요.");
        return false;
    }

    return true;
}


  function validateEmail() {
    var email = document.getElementById("email").value;
    let emailRegex = /^[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,}$/i;
    if (!emailRegex.test(email)) {
      document.getElementById("emailMessage").textContent = "올바른 이메일 주소가 아닙니다.";
      document.getElementById("emailMessage").style.color = "red";
      return false;
    }
    return true;
  }

  function validatePhone() {
    var phone = document.getElementById("phone").value;
    const phoneRegex = /^01([0|1|6|7|8|9])([0-9]{4})([0-9]{4})$/;
    if (!phoneRegex.test(phone)) {
      document.getElementById("phoneMessage").textContent = "올바른 휴대폰 번호가 아닙니다.";
      document.getElementById("phoneMessage").style.color = "red";

    }

  }

  function validatePassword() {
    var password = document.getElementById("password").value;
    var confirmPassword = document.getElementById("confirm_password").value;
    var message = document.getElementById("passwordCheckMessage");

    if (password !== confirmPassword) {
      message.textContent = "비밀번호가 일치하지 않습니다.";
      message.style.color = "red";

      return false;
    } else {
      message.textContent = "비밀번호가 일치합니다.";
      message.style.color = "green";

      return true;
    }
  }

function validateName() {
    var name = document.getElementById("name").value;
    const nameRegex = /^[가-힣]+$/; // 한글 이름 확인
    if (!nameRegex.test(name)) {
        document.getElementById("nameMessage").textContent = "이름은 한글로 입력하세요.";
        document.getElementById("nameMessage").style.color = "red";
        return false;
    } else {
        document.getElementById("nameMessage").textContent = "";
        return true;
    }
}

  document.getElementById('name').addEventListener('input', function() {
    var name = document.getElementById("name").value;
    var message = document.getElementById("nameMessage");
    const nameRegex = /^[가-힣]+$/;
    if (!nameRegex.test(name)) {
      message.textContent = "이름은 한글로 입력하세요";
      message.style.color = "red";
    } else {
      message.textContent = "";
    }
  });

  document.getElementById('phone').addEventListener('input', function() {
    var phone = document.getElementById("phone").value;
    var message = document.getElementById("phoneMessage");
    const phoneRegex = /^01([0|1|6|7|8|9])([0-9]{4})([0-9]{4})$/;
    if (!phoneRegex.test(phone)) {
      message.textContent = "올바른 휴대폰 번호가 아닙니다.";
      message.style.color = "red";
    } else {
      message.textContent = "";
    }
  });

  document.getElementById('email').addEventListener('input', function() {
    var email = document.getElementById("email").value;
    var message = document.getElementById("emailMessage");
    let emailRegex = /^[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,}$/i;
    if (!emailRegex.test(email)) {
      message.textContent = "올바른 이메일 주소가 아닙니다.";
      message.style.color = "red";
    } else {
      message.textContent = "";
    }
  });

  document.getElementById('password').addEventListener('input', function() {
    var password = document.getElementById("password").value;
    const message = document.getElementById("passwordMessage");
    var passwordRegex = /^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[!@#$%^&*?_]).{8,16}$/;

    if (/\s/.test(password)) {
      message.textContent = "비밀번호에 공백이 포함될 수 없습니다.";
      message.style.color = "red";
      return;
    }

    if (!passwordRegex.test(password)) {
      message.textContent = "비밀번호는 최소 8자에서 16자까지, 영문자, 숫자 및 특수 문자를 포함해야 합니다.";
      message.style.color = "red";
    } else {
      message.textContent = "비밀번호가 유효합니다.";
      message.style.color = "green";
    }
  });

  document.getElementById('confirm_password').addEventListener('input', function() {
    var password = document.getElementById("password").value;
    var confirmPassword = this.value;
    const message = document.getElementById("passwordCheckMessage");

    if (password !== confirmPassword) {
      message.textContent = "비밀번호가 일치하지 않습니다.";
      message.style.color = "red";
      isPasswordValid = false;
    } else {
      message.textContent = "비밀번호가 일치합니다.";
      message.style.color = "green";
      isPasswordValid = true;
    }
  });

  $("#checkPhone").on('click', function() {
    const phone = document.getElementById('phone').value;
    const message = document.getElementById("phoneMessage");
    const phoneRegex = /^01([0|1|6|7|8|9])([0-9]{4})([0-9]{4})$/;

    if (!phoneRegex.test(phone)) {
      message.textContent = "올바른 휴대폰 번호가 아닙니다.";
      message.style.color = "red";
      return;
    }

    fetch("http://localhost:8080/user/checkPhone?phone=" + phone)
      .then(response => {
        if (!response.ok) {
          return response.json().then(data => {
            throw new Error(data.message || '알 수 없는 에러가 발생했습니다.');
          });
        }
        return response.json();
      })
      .then(data => {
        message.textContent = (data.message);
        if (data.message === "이미 사용 중인 번호입니다.") {
          message.style.color = "red";
          document.getElementById('phone').readOnly = false;
        } else {
          message.style.color = "green";
          document.getElementById('phone').readOnly = true;
        }
      })
      .catch(error => {
        message.style.color = "red";
        message.textContent = (error.message);
      });
  });

  $("#checkId").on('click', function() {
    const email = document.getElementById('email').value;
    const message = document.getElementById("emailMessage");
    let emailRegex = /^[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,}$/i;

    if (!emailRegex.test(email)) {
      message.textContent = "올바른 이메일 주소가 아닙니다.";
      message.style.color = "red";
      return;
    }

    fetch("http://localhost:8080/user/checkId?email=" + email)
      .then(response => {
        if (!response.ok) {
          return response.json().then(data => {
            throw new Error(data.message || '알 수 없는 에러가 발생했습니다.');
          });
        }
        return response.json();
      })
      .then(data => {
        message.style.color = "green";
        message.textContent = (data.message);
        if (data.message === "이미 사용 중인 이메일입니다.") {
          message.style.color = "red";
          document.getElementById('email').readOnly = false;
        } else {
          message.style.color = "green";
          document.getElementById('email').readOnly = true;
        }
      })
      .catch(error => {
        message.style.color = "red";
        message.textContent = (error.message);
      });
  });

  document.getElementById('signupButton').addEventListener('click', function(event) {
    event.preventDefault(); // 기본 폼 제출 방지

    const basicAddress = document.getElementById("basicAddress").value;
        const detailAddress = document.getElementById("detailAddress").value;
        const fullAddress = basicAddress + " " + detailAddress;
        document.getElementById("fullAddress").value = fullAddress;

    //if (validateForm() === false) {
    //  return; // 유효성 검사 실패 시 종료
    //}

    // 폼 데이터 수집
    const formData = {
      name: document.getElementById("name").value,
      password: document.getElementById("password").value,
      email: document.getElementById("email").value,
      phone: document.getElementById("phone").value,
      address: fullAddress,
    };

    console.log("formData",formData);

    // fetch 요청 보내기
    fetch('/user/join', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(formData),
    })
    .then(response => {
      if (!response.ok) {
        return response.json().then(data => {
          throw new Error(data.message || '회원가입 중 오류가 발생했습니다.');
        });
      }
      return response.json(); // JSON 응답으로 변환
    })
    .then(data => {
      alert('회원가입이 완료되었습니다!'); // 성공 메시지 표시
      window.location.href = '/user/login'; // 로그인 페이지로 이동
    })
    .catch(error => {
      alert(error.message); // 오류 메시지 표시
    });
  });

</script>

<script src="/js/user/sendEmail.js"></script>
<script src="/js/user/sendSMS.js"></script>
<%@ include file="/WEB-INF/view/layout/footer.jsp"%>
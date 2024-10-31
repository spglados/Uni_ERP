// 휴대폰 인증 요청
  document.getElementById("phoneChk").addEventListener("click", function() {
    alert("인증번호 발송이 완료되었습니다.\n휴대폰에서 인증번호 확인을 해주십시오.");
    const phone = document.getElementById("phone").value;

    fetch(`/user/sendPhoneVerification?phone=${encodeURIComponent(phone)}`, { method: "GET" })
      .then(response => response.text())
      .then(data => {
        if (data === "error") {
          alert("휴대폰 번호가 올바르지 않습니다.");
          document.querySelector(".successPhoneChk").textContent = "유효한 번호를 입력해주세요.";
          document.querySelector(".successPhoneChk").style.color = "red";
          document.getElementById("phone").focus();
        } else {
          document.getElementById("phone2").disabled = false;
          document.getElementById("phoneChk2").style.display = "inline-block";
          document.querySelector(".successPhoneChk").textContent = "인증번호를 입력한 뒤 본인인증을 눌러주십시오.";
          document.querySelector(".successPhoneChk").style.color = "green";
          document.getElementById("phone").readOnly = true;
          // code2 = data; // 필요시 추가
        }
      })
      .catch(error => console.error('Error:', error));
  });

  // 휴대폰 인증번호 대조
  document.getElementById("phoneChk2").addEventListener("click", function() {
    const phone2 = document.getElementById("phone2").value;

    fetch(`/user/verifyPhone?code=${encodeURIComponent(phone2)}`, { method: "POST" })
      .then(response => response.json())
      .then(data => {
        if (data.success) {
          document.querySelector(".successPhoneChk").textContent = "인증번호가 일치합니다.";
          document.querySelector(".successPhoneChk").style.color = "green";
          document.getElementById("phoneDoubleChk").value = "true";
          document.getElementById("phone2").disabled = true;
          isPhoneValid = true;
        } else {
          document.querySelector(".successPhoneChk").textContent = "인증번호가 일치하지 않습니다. 확인해주시기 바랍니다.";
          document.querySelector(".successPhoneChk").style.color = "red";
          document.getElementById("phoneDoubleChk").value = "false";
          document.getElementById("").focus();
          isPhoneValid = false
        }
      })
      .catch(error => console.error('Error:', error));
  });
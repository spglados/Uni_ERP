<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ include file="/WEB-INF/view/erp/layout/erpHeader.jsp" %>

<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">

<!-- 메인 컨텐츠 -->
<div class="content container-fluid">
    <h1 class="text-center mb-4">직원 등록</h1>

    <c:if test="${not empty errorMessage}">
        <div class="alert alert-danger">
                ${errorMessage}
        </div>
    </c:if>

    <form action="${pageContext.request.contextPath}/erp/hr/registerEmployee" method="post" id="employeeRegisterForm"
          onsubmit="return validateForm()" class="border rounded p-4 shadow-sm">
        <div class="mb-3">
            <label for="name" class="form-label">이름 (최대 10자, 한글만):</label>
            <input type="text" id="name" name="name" required maxlength="10"
                   pattern="^[가-힣]{1,10}$" class="form-control"
                   title="한글로 최대 10자 입력하세요" value="${employeeDTO.name}">
        </div>
        <div class="mb-3">
            <label for="birthday" class="form-label">생년월일:</label>
            <input type="date" id="birthday" name="birthday" required class="form-control"
                   min="1900-01-01"
                   max="<%= new java.text.SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date()) %>"
                   title="1900년 1월 1일부터 오늘까지의 날짜를 선택하세요" value="${employeeDTO.birthday}">
        </div>
        <div class="mb-3">
            <label for="gender" class="form-label">성별:</label>
            <select id="gender" name="gender" required class="form-select">
                <option value="M" <c:if test="${employeeDTO.gender == 'M'}">selected</c:if>>남자</option>
                <option value="F" <c:if test="${employeeDTO.gender == 'F'}">selected</c:if>>여자</option>
            </select>
        </div>
        <div class="mb-3">
            <label for="email" class="form-label">이메일:</label>
            <div class="input-group">
                <input type="text" id="email" name="email" required
                       pattern="^[A-Za-z0-9._%+-]+$" class="form-control"
                       title="유효한 이메일 형식으로 입력하세요" value="${employeeDTO.email.split('@')[0]}">
                <span class="input-group-text">@</span>
                <input type="text" id="emailDomain" name="emailDomain" required class="form-control"
                       title="도메인을 입력하세요" value="${employeeDTO.email.split('@')[1]}">
                <select id="domainSelect" onchange="updateDomain()" class="form-select">
                    <option value="">직접 입력</option>
                    <option value="naver.com" <c:if test="${employeeDTO.emailDomain == 'naver.com'}">selected</c:if>>
                        naver.com
                    </option>
                    <option value="daum.net" <c:if test="${employeeDTO.emailDomain == 'daum.net'}">selected</c:if>>
                        daum.net
                    </option>
                    <option value="gmail.com" <c:if test="${employeeDTO.emailDomain == 'gmail.com'}">selected</c:if>>
                        gmail.com
                    </option>
                    <option value="nate.com" <c:if test="${employeeDTO.emailDomain == 'nate.com'}">selected</c:if>>
                        nate.com
                    </option>
                </select>
                <button type="button" onclick="checkEmail()" class="btn btn-outline-secondary">중복 확인</button>
            </div>
            <small id="emailCheckResult" class="form-text text-danger"></small>
        </div>

        <div class="mb-3">
            <label for="password" class="form-label">비밀번호:</label>
            <input type="password" id="password" name="password" required class="form-control"
                   minlength="4" maxlength="8" title="4자 이상, 8자 이하로 입력하세요">
        </div>
        <div class="mb-3">
            <label for="confirmPassword" class="form-label">비밀번호 확인:</label>
            <input type="password" id="confirmPassword" name="confirmPassword" required class="form-control"
                   oninput="checkPasswordMatch()" title="비밀번호를 확인 입력하세요">
            <small id="passwordCheckResult" class="form-text text-danger"></small>
        </div>

        <div class="mb-3">
            <label for="phone" class="form-label">전화번호 (형식: 000-0000-0000):</label>
            <div class="input-group">
                <input type="text" id="phone" name="phone" required class="form-control"
                       oninput="formatPhoneNumber(this)" maxlength="13"
                       title="전화번호를 000-0000-0000 형식으로 입력하세요" placeholder="000-0000-0000" value="${employeeDTO.phone}">
                <button type="button" onclick="checkPhone()" class="btn btn-outline-secondary">중복 확인</button>
            </div>
            <small id="phoneCheckResult" class="form-text text-danger"></small>
        </div>
        <div class="mb-3">
            <label for="address" class="form-label">주소:</label>
            <input type="hidden" id="fullAddress" name="address">
            <input type="text" id="address" required class="form-control" value="${employeeDTO.address}" onclick = "execDaumPostcode()">
            <input type="text" id="detailAddress" required class="form-control" placeholder="상세 주소">
        </div>
        <div class="mb-3">
            <label for="accountNumber" class="form-label">계좌번호 (최대 16자리, 숫자만):</label>
            <input type="text" id="accountNumber" name="accountNumber" required maxlength="16" class="form-control"
                   pattern="^\d{1,16}$" title="계좌번호를 1자리 이상 16자리 이하의 숫자로 입력하세요" value="${employeeDTO.accountNumber}">
        </div>
        <div class="mb-3">
            <label for="position" class="form-label">직책:</label>
            <select id="position" name="empPosition.id" required class="form-select">
                <c:forEach var="position" items="${positionsList}">
                    <option value="${position.id}"
                            <c:if test="${position.id == employeeDTO.empPosition.id}">selected</c:if>>${position.name}</option>
                </c:forEach>
            </select>
        </div>
        <div class="mb-3">
            <label for="bankId" class="form-label">은행:</label>
            <select id="bankId" name="bankId" required class="form-select">
                <c:forEach var="bank" items="${bankList}">
                    <option value="${bank.id}"
                            <c:if test="${bank.id == employeeDTO.bankId}">selected</c:if>>${bank.name}</option>
                </c:forEach>
            </select>
        </div>

        <h3 class="mt-4">문서 관련 정보</h3>
        <p>문서 보관 여부를 선택해주세요.</p>
        <div class="form-check mb-2">
            <input type="checkbox" id="employmentContract" name="empDocumentDTO.employmentContract"
                   class="form-check-input"
                   <c:if test="${employeeDTO.empDocumentDTO.employmentContract}">checked</c:if>>
            <label for="employmentContract" class="form-check-label">고용 계약서</label>
        </div>
        <div class="form-check mb-2">
            <input type="checkbox" id="healthCertificate" name="empDocumentDTO.healthCertificate"
                   class="form-check-input" <c:if test="${employeeDTO.empDocumentDTO.healthCertificate}">checked</c:if>>
            <label for="healthCertificate" class="form-check-label">건강증명서</label>
        </div>
        <div class="form-check mb-2">
            <input type="checkbox" id="identificationCopy" name="empDocumentDTO.identificationCopy"
                   class="form-check-input"
                   <c:if test="${employeeDTO.empDocumentDTO.identificationCopy}">checked</c:if>>
            <label for="identificationCopy" class="form-check-label">신분증 사본</label>
        </div>
        <div class="form-check mb-2">
            <input type="checkbox" id="bankAccountCopy" name="empDocumentDTO.bankAccountCopy" class="form-check-input"
                   <c:if test="${employeeDTO.empDocumentDTO.bankAccountCopy}">checked</c:if>>
            <label for="bankAccountCopy" class="form-check-label">계좌 사본</label>
        </div>
        <div class="form-check mb-2">
            <input type="checkbox" id="residentRegistration" name="empDocumentDTO.residentRegistration"
                   class="form-check-input"
                   <c:if test="${employeeDTO.empDocumentDTO.residentRegistration}">checked</c:if>>
            <label for="residentRegistration" class="form-check-label">주민등록증</label>
        </div>

        <input type="hidden" name="storeId" value="${storeId}">
        <div class="text-center mt-4">
            <button type="submit" id="submitButton" class="btn btn-primary" disabled>직원 등록</button>
        </div>
    </form>
</div>


<script src="//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>
<script src="//dapi.kakao.com/v2/maps/sdk.js?appkey=3aea5e049cf7a27eae091e77ca0e1429&libraries=services"></script>
<script>
    let isEmailChecked = false;
    let isPhoneChecked = false;

    function checkPasswordMatch() {
        const password = document.getElementById("password").value;
        const confirmPassword = document.getElementById("confirmPassword").value;
        const resultSpan = document.getElementById("passwordCheckResult");

        if (password !== confirmPassword) {
            resultSpan.innerText = "비밀번호가 일치하지 않습니다.";
            resultSpan.classList.add("text-danger");
            resultSpan.classList.remove("text-success");
        } else {
            resultSpan.innerText = "비밀번호가 일치합니다.";
            resultSpan.classList.add("text-success");
            resultSpan.classList.remove("text-danger");
        }
    }

    function updateDomain() {
        const domainSelect = document.getElementById("domainSelect");
        const emailDomainInput = document.getElementById("emailDomain");
        emailDomainInput.value = domainSelect.value;
    }

    function execDaumPostcode() {
        new daum.Postcode({
          oncomplete: function(data) {
            var addr = data.address;
            document.getElementById("address").value = addr;
          }
        }).open();
    }

    function validateForm() {
            const address = document.getElementById("address").value;
            const detailAddress = document.getElementById("detailAddress").value;
            document.getElementById("fullAddress").value = address + ' , ' + detailAddress;
            return true;
        }

    function formatPhoneNumber(input) {
        const value = input.value.replace(/\D/g, '');
        if (value.length < 4) {
            input.value = value;
        } else if (value.length < 8) {
            input.value = value.slice(0, 3) + '-' + value.slice(3);
        } else {
            input.value = value.slice(0, 3) + '-' + value.slice(3, 7) + '-' + value.slice(7, 11);
        }
    }

    // 이메일 중복 확인
    function checkEmail() {
        const email = document.getElementById("email").value;
        const emailDomain = document.getElementById("emailDomain").value;
        const fullEmail = email + "@" + emailDomain;
        console.log(fullEmail);
        const resultSpan = document.getElementById("emailCheckResult");

        // 이메일 입력이 비어 있는지 확인
        if (!email || !emailDomain) {
            resultSpan.innerText = "이메일을 입력하세요.";
            resultSpan.classList.remove("text-success", "text-danger");
            return; // 입력이 없으면 함수 종료
        }

        if (fullEmail) {
            fetch("/erp/hr/check-email?email=" + encodeURIComponent(fullEmail))
                .then(response => response.json())
                .then(data => {
                    if (data.isDuplicated) {
                        resultSpan.innerText = "이미 사용 중인 이메일입니다.";
                        resultSpan.classList.remove("text-success");
                        resultSpan.classList.add("text-danger");
                    } else {
                        resultSpan.innerText = "사용 가능한 이메일입니다.";
                        resultSpan.classList.remove("text-danger");
                        resultSpan.classList.add("text-success");
                    }
                    isEmailChecked = !data.isDuplicated; // 중복된 경우 false, 아닌 경우 true
                    updateSubmitButtonState(); // 제출 버튼 상태 업데이트
                });
        } else {
            resultSpan.innerText = "이메일을 입력하세요.";
            resultSpan.classList.remove("text-success", "text-danger");
        }
    }

    // 전화번호 중복 확인
    function checkPhone() {
        const phone = document.getElementById("phone").value;
        const resultSpan = document.getElementById("phoneCheckResult");

        if (!/^\d{3}-\d{3,4}-\d{4}$/.test(phone)) {
            alert("유효한 전화번호 형식을 입력해주세요.");
            return;
        }

        fetch('${pageContext.request.contextPath}/erp/hr/check-phone?phone=' + encodeURIComponent(phone))
            .then(response => response.json())
            .then(data => {
                if (data.isDuplicated) {
                    resultSpan.innerText = "이미 등록된 전화번호입니다.";
                    resultSpan.classList.remove("text-success");
                    resultSpan.classList.add("text-danger");
                    isPhoneChecked = false;
                } else {
                    resultSpan.innerText = "사용 가능한 전화번호입니다.";
                    resultSpan.classList.remove("text-danger");
                    resultSpan.classList.add("text-success");
                    isPhoneChecked = true;
                }
                updateSubmitButtonState();
            })
            .catch(error => {
                console.error('Error:', error);
            });
    }

    function updateSubmitButtonState() {
        const submitButton = document.getElementById("submitButton");
        submitButton.disabled = !(isEmailChecked && isPhoneChecked);
    }

</script>

<%@ include file="/WEB-INF/view/erp/layout/erpFooter.jsp" %>
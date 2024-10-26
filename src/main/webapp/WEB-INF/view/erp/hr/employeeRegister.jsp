<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ include file="/WEB-INF/view/erp/layout/erpHeader.jsp" %>

<!-- 메인 컨텐츠 -->
<div class="content">
    <h1>직원 등록입니다.</h1>

    <c:if test="${not empty errorMessage}">
        <div style="color: red;">
            ${errorMessage}
        </div>
    </c:if>

    <form action="${pageContext.request.contextPath}/erp/hr/registerEmployee" method="post" id="employeeRegisterForm"
          onsubmit="return validateForm()">
        <div>
            <label for="name">이름 (최대 10자, 한글만):</label>
            <input type="text" id="name" name="name" required maxlength="10"
                   pattern="^[가-힣]{1,10}$"
                   title="한글로 최대 10자 입력하세요" value="${employeeDTO.name}">
        </div>
        <div>
            <label for="birthday">생년월일:</label>
            <input type="date" id="birthday" name="birthday" required
                   min="1900-01-01"
                   max="<%= new java.text.SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date()) %>"
                   title="1900년 1월 1일부터 오늘까지의 날짜를 선택하세요" value="${employeeDTO.birthday}">
        </div>
        <div>
            <label for="gender">성별:</label>
            <select id="gender" name="gender" required>
                <option value="M" <c:if test="${employeeDTO.gender == 'M'}">selected</c:if>>남자</option>
                <option value="F" <c:if test="${employeeDTO.gender == 'F'}">selected</c:if>>여자</option>
            </select>
        </div>
        <div>
            <label for="email">이메일:</label>
            <input type="text" id="email" name="email" required
                   pattern="^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$"
                   title="유효한 이메일 형식으로 입력하세요" value="${employeeDTO.email}">
            <select name="emailDomain" id="emailDomain" onchange="updateEmail()">
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
            <button type="button" onclick="checkEmail()">중복 확인</button>
            <span id="emailCheckResult" style="color: red;"></span> <!-- 이메일 중복 검사 결과 표시 -->
        </div>
        <div>
            <label for="phone">전화번호 (형식: 000-0000-0000):</label>
            <input type="text" id="phone" name="phone" required
                   oninput="formatPhoneNumber(this)" maxlength="13"
                   title="전화번호를 000-0000-0000 형식으로 입력하세요" placeholder="000-0000-0000" value="${employeeDTO.phone}">
            <button type="button" onclick="checkPhone()">중복 확인</button>
            <span id="phoneCheckResult" style="color: red;"></span> <!-- 전화번호 중복 검사 결과 표시 -->
        </div>
        <div>
            <label for="address">주소:</label>
            <input type="text" id="address" name="address" required value="${employeeDTO.address}">
        </div>
        <div>
            <label for="accountNumber">계좌번호 (최대 16자리, 숫자만):</label>
            <input type="text" id="accountNumber" name="accountNumber" required maxlength="16"
                   pattern="^\d{1,16}$"
                   title="계좌번호를 1자리 이상 16자리 이하의 숫자로 입력하세요" value="${employeeDTO.accountNumber}">
        </div>
        <div>
            <label for="position">직책:</label>
            <select id="position" name="empPosition.id" required>
                <c:forEach var="position" items="${positionsList}">
                    <option value="${position.id}"
                            <c:if test="${position.id == employeeDTO.empPosition.id}">selected</c:if>>${position.name}</option>
                </c:forEach>
            </select>
        </div>
        <div>
            <label for="bankId">은행:</label>
            <select id="bankId" name="bankId" required>
                <c:forEach var="bank" items="${bankList}">
                    <option value="${bank.id}"
                            <c:if test="${bank.id == employeeDTO.bankId}">selected</c:if>>${bank.name}</option>
                </c:forEach>
            </select>
        </div>
        <h3>문서 관련 정보</h3>
        <p>문서 보관 여부를 선택해주세요.</p>
        <br>
        <div>
            <label for="employmentContract">고용 계약서:</label>
            <input type="checkbox" id="employmentContract" name="empDocumentDTO.employmentContract"
                   <c:if test="${employeeDTO.empDocumentDTO.employmentContract}">checked</c:if>>
        </div>
        <div>
            <label for="healthCertificate">건강증명서:</label>
            <input type="checkbox" id="healthCertificate" name="empDocumentDTO.healthCertificate"
                   <c:if test="${employeeDTO.empDocumentDTO.healthCertificate}">checked</c:if>>
        </div>
        <div>
            <label for="identificationCopy">신분증 사본:</label>
            <input type="checkbox" id="identificationCopy" name="empDocumentDTO.identificationCopy"
                   <c:if test="${employeeDTO.empDocumentDTO.identificationCopy}">checked</c:if>>
        </div>
        <div>
            <label for="bankAccountCopy">계좌 사본:</label>
            <input type="checkbox" id="bankAccountCopy" name="empDocumentDTO.bankAccountCopy"
                   <c:if test="${employeeDTO.empDocumentDTO.bankAccountCopy}">checked</c:if>>
        </div>
        <div>
            <label for="residentRegistration">주민등록증:</label>
            <input type="checkbox" id="residentRegistration" name="empDocumentDTO.residentRegistration"
                   <c:if test="${employeeDTO.empDocumentDTO.residentRegistration}">checked</c:if>>
        </div>
        <div>
            <input type="hidden" name="storeId" value="${storeId}">
            <button type="submit" id="submitButton" disabled>직원 등록</button>
        </div>
    </form>
</div>

<script>
    let isEmailChecked = false;
    let isPhoneChecked = false;

    function updateEmail() {
        const emailInput = document.getElementById("email");
        const domainSelect = document.getElementById("emailDomain");
        const selectedDomain = domainSelect.options[domainSelect.selectedIndex].value;
        if (selectedDomain) {
            emailInput.value = emailInput.value.split('@')[0] + '@' + selectedDomain;
        }
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
        const resultSpan = document.getElementById("emailCheckResult");

        if (!/^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$/.test(email)) {
            alert("유효한 이메일을 입력해주세요.");
            return;
        }

        fetch('${pageContext.request.contextPath}/erp/hr/check-email?email=' + encodeURIComponent(email))
            .then(response => response.json())
            .then(data => {
                if (data.isDuplicated) {
                    resultSpan.innerText = "이미 등록된 이메일입니다.";
                    isEmailChecked = false;
                } else {
                    resultSpan.innerText = "사용 가능한 이메일입니다.";
                    isEmailChecked = true;
                }
                updateSubmitButtonState();
            })
            .catch(error => {
                console.error('Error:', error);
            });
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
                    isPhoneChecked = false;
                } else {
                    resultSpan.innerText = "사용 가능한 전화번호입니다.";
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
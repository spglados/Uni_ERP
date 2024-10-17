<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ include file="/WEB-INF/view/erp/layout/erpHeader.jsp" %>

<!-- 메인 컨텐츠 -->
<div class="content">
    <h1>직원 등록입니다.</h1>

    <form action="${pageContext.request.contextPath}/erp/hr/employee-register" method="post" id="employeeRegisterForm" onsubmit="return validateForm()">
        <div>
            <label for="name">이름 (최대 10자, 한글만):</label>
            <input type="text" id="name" name="name" required maxlength="10"
                   pattern="^[가-힣]{1,10}$"
                   title="한글로 최대 10자 입력하세요">
        </div>
        <div>
            <label for="birthday">생년월일:</label>
            <input type="date" id="birthday" name="birthday" required
                   min="1900-01-01" max="<%= new java.text.SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date()) %>"
                   title="1900년 1월 1일부터 오늘까지의 날짜를 선택하세요">
        </div>
        <div>
            <label for="gender">성별:</label>
            <select id="gender" name="gender" required>
                <option value="M">남자</option>
                <option value="F">여자</option>
            </select>
        </div>
        <div>
            <label for="email">이메일:</label>
            <input type="text" id="email" name="email" required
                   pattern="^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$"
                   title="유효한 이메일 형식으로 입력하세요">
            <select name="emailDomain" id="emailDomain" onchange="updateEmail()">
                <option value="">직접 입력</option>
                <option value="naver.com">naver.com</option>
                <option value="daum.net">daum.net</option>
                <option value="gmail.com">gmail.com</option>
                <option value="nate.com">nate.com</option>
            </select>
        </div>
        <div>
            <label for="phone">전화번호 (형식: 000-0000-0000):</label>
            <input type="text" id="phone" name="phone" required
                   oninput="formatPhoneNumber(this)" maxlength="13"
                   title="전화번호를 000-0000-0000 형식으로 입력하세요" placeholder="000-0000-0000">
        </div>
        <div>
            <label for="address">주소:</label>
            <input type="text" id="address" name="address" required>
        </div>
        <div>
            <label for="accountNumber">계좌번호 (최대 16자리, 숫자만):</label>
            <input type="text" id="accountNumber" name="accountNumber" required maxlength="16"
                   pattern="^\d{1,16}$"
                   title="계좌번호를 1자리 이상 16자리 이하의 숫자로 입력하세요">
        </div>
        <div>
            <label for="position">직책 (최대 10자, 한글 및 숫자만):</label>
            <input type="text" id="position" name="position" required maxlength="10"
                   pattern="^[가-힣0-9]{1,10}$"
                   title="직책을 한글 또는 숫자로 최대 10자 입력하세요">
        </div>
        <div>
            <label for="bankId">은행:</label>
            <select id="bankId" name="bankId" required>
                <c:forEach var="bank" items="${bankList}">
                    <option value="${bank.id}">${bank.name}</option>
                </c:forEach>
            </select>
        </div>
        <h3>문서 관련 정보</h3>
            <p>문서 보관 여부를 선택해주세요.</p>
        <br>
        <div>
            <label for="employmentContract">고용 계약서:</label>
            <input type="checkbox" id="employmentContract" name="empDocumentDTO.employmentContract">
        </div>
        <div>
            <label for="healthCertificate">건강증명서:</label>
            <input type="checkbox" id="healthCertificate" name="empDocumentDTO.healthCertificate">
        </div>
        <div>
            <label for="identificationCopy">신분증 사본:</label>
            <input type="checkbox" id="identificationCopy" name="empDocumentDTO.identificationCopy">
        </div>
        <div>
            <label for="bankAccountCopy">계좌 사본:</label>
            <input type="checkbox" id="bankAccountCopy" name="empDocumentDTO.bankAccountCopy">
        </div>
        <div>
            <label for="residentRegistration">주민등록증:</label>
            <input type="checkbox" id="residentRegistration" name="empDocumentDTO.residentRegistration">
        </div>
        <div>
            <input type="hidden" name="storeId" value="${storeId}">
            <button type="submit">직원 등록</button>
        </div>
    </form>
</div>

<script>
    function updateEmail() {
        var emailInput = document.getElementById("email");
        var domainSelect = document.getElementById("emailDomain");
        var selectedDomain = domainSelect.options[domainSelect.selectedIndex].value;
        if (selectedDomain) {
            emailInput.value = emailInput.value.split('@')[0] + '@' + selectedDomain;
        }
    }

    function formatPhoneNumber(input) {
        // 입력된 값에서 숫자만 남기기
        var value = input.value.replace(/\D/g, '');

        // 하이픈을 추가하기 위해 숫자 길이에 따라 조건문 작성
        if (value.length < 4) {
            input.value = value; // 3자리 이하는 그대로
        } else if (value.length < 8) {
            input.value = value.slice(0, 3) + '-' + value.slice(3); // 3자리-4자리
        } else {
            input.value = value.slice(0, 3) + '-' + value.slice(3, 7) + '-' + value.slice(7, 11); // 3자리-4자리-4자리
        }
    }

    function validateForm() {
        var nameInput = document.getElementById("name");
        var phoneInput = document.getElementById("phone");
        var accountNumberInput = document.getElementById("accountNumber");
        var positionInput = document.getElementById("position");

        // 이름 검증
        if (!/^[가-힣]{1,5}$/.test(nameInput.value)) {
            alert("이름은 한글로 최대 5자여야 합니다.");
            return false;
        }

        // 전화번호 검증
        if (!/^\d{3}-\d{4}-\d{4}$/.test(phoneInput.value)) {
            alert("전화번호는 000-0000-0000 형식이어야 합니다.");
            return false;
        }

        // 계좌번호 검증
        if (!/^\d{1,16}$/.test(accountNumberInput.value)) {
            alert("계좌번호는 1자리 이상 16자리 이하의 숫자여야 합니다.");
            return false;
        }

        // 직책 검증
        if (!/^[가-힣0-9]{1,10}$/.test(positionInput.value)) {
            alert("직책은 한글 또는 숫자로 최대 10자여야 합니다.");
            return false;
        }

        return true; // 모든 검증 통과 시 폼 제출
    }
</script>

<%@ include file="/WEB-INF/view/erp/layout/erpFooter.jsp" %>

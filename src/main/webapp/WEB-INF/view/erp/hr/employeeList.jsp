<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ include file="/WEB-INF/view/erp/layout/erpHeader.jsp" %>

<div class="container">
    <div class="left-panel">
        <h1>직원 목록</h1>
        <c:if test="${not empty employees}">
            <table>
                <thead>
                <tr>
                    <th>사원번호</th>
                    <th>이름</th>
                    <th>직책</th>
                    <th>상태</th>
                    <th>전화번호</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="employee" items="${employees}">
                    <tr class="employee-row"
                        data-id="${employee.uniqueEmployeeNumber}"
                        data-name="${employee.name}"
                        data-birthday="${employee.birthday}"
                        data-gender="${employee.gender}"
                        data-address="${employee.address}"
                        data-email="${employee.email.split('@')[0]}@${employee.email.split('@')[1]}"
                        data-phone="${employee.phone}"
                        data-status="${employee.employmentStatus}"
                        data-bank="${employee.bankName != null ? employee.bankName : '정보 없음'}"
                        data-position="${employee.empPosition.id != null ? employee.empPosition.id : '0'}"
                        data-account="${employee.accountNumber}"
                        data-healthcertificatedate="${employee.healthCertificateDate}"
                        data-employmentcontract="${employee.empDocumentDTO.employmentContract}"
                        data-healthcertificate="${employee.empDocumentDTO.healthCertificate}"
                        data-identificationcopy="${employee.empDocumentDTO.identificationCopy}"
                        data-bankaccountcopy="${employee.empDocumentDTO.bankAccountCopy}"
                        data-residentregistration="${employee.empDocumentDTO.residentRegistration}">
                        <td>${employee.uniqueEmployeeNumber}</td>
                        <td>${employee.name}</td>
                        <td>${employee.empPosition.name != null ? employee.empPosition.name : '정보 없음'}</td>
                        <td>
                            <c:choose>
                                <c:when test="${employee.employmentStatus == 'ACTIVE'}">재직</c:when>
                                <c:when test="${employee.employmentStatus == 'INACTIVE'}">퇴사</c:when>
                                <c:when test="${employee.employmentStatus == 'ONLEAVE'}">휴직</c:when>
                            </c:choose>
                        </td>
                        <td>${employee.phone}</td>
                    </tr>
                </c:forEach>
                <button onclick="location.href='/erp/hr/download/excel'">엑셀 다운로드</button>
                </tbody>
            </table>
        </c:if>
        <c:if test="${empty employees}">
            <p>등록된 직원이 없습니다.</p>
        </c:if>
    </div>
    <div class="right-panel" id="employee-details">
        <h2>직원 상세 정보</h2>
        <p>상세 정보를 클릭하여 확인하세요.</p>
    </div>
</div>

<!-- 수정 팝업 모달 -->
<div id="editEmployeeModal" style="display: none;">
    <h2>직원 수정</h2>
    <form id="editEmployeeForm" onsubmit="return false;">
        <input type="hidden" id="editEmployeeId" name="id"/>

        <label for="editEmployeeName">이름:</label>
        <input type="text" id="editEmployeeName" name="name" maxlength="10" required pattern="^[가-힣]{2,10}$"
               title="이름은 한글 2~10자로 입력해야 합니다."/>

        <label for="editEmployeeBirthday">생년월일:</label>
        <input type="date" id="editEmployeeBirthday" name="birthday" required min="1900-01-01"
               max="<%= new java.text.SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date()) %>"/>

        <label for="editEmployeeGender">성별:</label>
        <select id="editEmployeeGender" name="gender" required>
            <option value="M">남성</option>
            <option value="F">여성</option>
        </select>

        <label for="editEmployeeAddress">주소:</label>
        <input type="text" id="editEmployeeAddress" name="address" required>

        <div>
            <label for="editEmployeeEmail">이메일 아이디:</label>
            <input type="text" id="editEmployeeEmail" name="email" required
                   pattern="^[A-Za-z0-9._%+-]+$"
                   title="유효한 이메일 형식으로 입력하세요" value=""/>
            @
            <input type="text" id="emailDomain" name="emailDomain" required
                   title="도메인을 입력하세요" value="">
            <select id="domainSelect" onchange="updateEmail()">
                <option value="">직접 입력</option>
                <option value="naver.com">naver.com</option>
                <option value="daum.net">daum.net</option>
                <option value="gmail.com">gmail.com</option>
                <option value="nate.com">nate.com</option>
            </select>
        </div>

        <label for="editEmployeePhone">전화번호:</label>
        <input type="text" id="editEmployeePhone" name="phone" required
               oninput="formatPhoneNumber(this)" maxlength="13"
               placeholder="000-0000-0000"
               title="유효한 전화번호 형식이 아닙니다."/>

        <!-- 은행 정보 추가 -->
        <label for="bankSelect">은행:</label>
        <select id="bankSelect" name="bank">
            <c:forEach items="${banks}" var="bank">
                <option value="${bank.id}">${bank.name}</option>
            </c:forEach>
        </select>

        <label for="editEmployeeAccountNumber">계좌번호:</label>
        <input type="text" id="editEmployeeAccountNumber" name="accountNumber" required
               maxlength="16"
               pattern="^\d{1,16}$"
               title="계좌번호를 1자리 이상 16자리 이하의 숫자로 입력하세요"/>

        <!-- 직책 정보 추가 -->
        <label for="positionSelect">직책:</label>
        <select id="positionSelect" name="positionId">
            <c:forEach items="${positions}" var="position">
                <option value="${position.id}">${position.name}</option>
            </c:forEach>
        </select>

        <label for="editEmployeeStatus">상태:</label>
        <select id="editEmployeeStatus" name="employmentStatus">
            <option value="ACTIVE">재직</option>
            <option value="INACTIVE">퇴사</option>
            <option value="ONLEAVE">휴직</option>
        </select>

        <!-- 문서 정보 추가 -->
        <h3>문서 제출 상태</h3>
        <p>문서 보관 여부를 선택해주세요.</p>
        <br>
        <div>
            <label for="editEmploymentContract">고용 계약서:</label>
            <input type="checkbox" id="editEmploymentContract" value="true" name="employmentContract">
        </div>
        <div>
            <label for="editHealthCertificate">건강증명서:</label>
            <input type="checkbox" id="editHealthCertificate" value="true" name="healthCertificate">
        </div>
        <div>
            <label for="editIdentificationCopy">신분증 사본:</label>
            <input type="checkbox" id="editIdentificationCopy" value="true" name="identificationCopy">
        </div>
        <div>
            <label for="editBankAccountCopy">계좌 사본:</label>
            <input type="checkbox" id="editBankAccountCopy" value="true" name="bankAccountCopy">
        </div>
        <div>
            <label for="editResidentRegistration">주민등록증:</label>
            <input type="checkbox" id="editResidentRegistration" value="true" name="residentRegistration">
        </div>
        <div>
            <label for="editHealthCertificateDate">보건증 발급일:</label>
            <input type="date" id="editHealthCertificateDate" name="healthCertificateDate"/>
        </div>

        <button type="submit">수정하기</button>
        <button type="button" onclick="closeModal()">취소</button>
    </form>
</div>

<script>

    const bankMapping = [
        <c:forEach items="${banks}" var="bank" varStatus="status">
        {id: "${bank.id}", name: "${bank.name}"}<c:if test="${!status.last}">, </c:if>
        </c:forEach>
    ];

    function openModal() {
        document.getElementById('editEmployeeModal').style.display = 'block';
    }

    function closeModal() {
        document.getElementById('editEmployeeModal').style.display = 'none';
    }

    const employeesJson = '${employeesJson}'; // JSON 문자열을 올바르게 설정

    // JSON 문자열 유효성 검사
    let employeesData = [];
    if (employeesJson && employeesJson !== 'null' && employeesJson !== '') {
        try {
            employeesData = JSON.parse(employeesJson); // JSON 문자열을 객체로 파싱
        } catch (error) {
            console.error("JSON 파싱 오류:", error);
        }
    }

    document.addEventListener('DOMContentLoaded', function () {
        // 직원 목록 클릭 시 상세 정보 표시

        document.querySelector('.left-panel table tbody').addEventListener('click', function (event) {
            const row = event.target.closest('.employee-row');
            if (row) {
                const uniqueId = row.dataset.id;
                const name = row.dataset.name;
                const birthday = row.dataset.birthday;
                const gender = row.dataset.gender;
                const address = row.dataset.address;
                const email = row.dataset.email;
                const phone = row.dataset.phone;
                const status = row.dataset.status;
                const bank = row.dataset.bank;
                const account = row.dataset.account;
                const position = row.dataset.position;

                // 문서 정보 추가
                const employmentContract = row.dataset.employmentcontract === 'true';
                const healthCertificate = row.dataset.healthcertificate === 'true';
                const identificationCopy = row.dataset.identificationcopy === 'true';
                const bankAccountCopy = row.dataset.bankaccountcopy === 'true';
                const residentRegistration = row.dataset.residentregistration === 'true';
                const healthCertificateDate = row.dataset.healthcertificatedate;

                // 직원 상세 정보를 표시
                const detailsDiv = document.getElementById('employee-details');
                detailsDiv.innerHTML =
                    '<h2>' + name + '의 상세 정보</h2>' +
                    '<p>사원번호: ' + uniqueId + '</p>' +
                    '<p>생년월일: ' + birthday + '</p>' +
                    '<p>성별: ' + gender + '</p>' +
                    '<p>주소: ' + address + '</p>' +
                    '<p>이메일: ' + email + '</p>' +
                    '<p>연락처: ' + phone + '</p>' +
                    '<p>상태: ' + status + '</p>' +
                    '<p>은행: ' + bank + '</p>' +
                    '<p>계좌번호: ' + account + '</p>' +
                    '<h3>문서 제출 여부</h3>' +
                    '<ul>' +
                    '<li>근로계약서: ' + (employmentContract ? '제출' : '미제출') + '</li>' +
                    '<li>건강증명서: ' + (healthCertificate ? '제출' : '미제출') + '</li>' +
                    '<li>신분증 사본: ' + (identificationCopy ? '제출' : '미제출') + '</li>' +
                    '<li>은행 계좌 사본: ' + (bankAccountCopy ? '제출' : '미제출') + '</li>' +
                    '<li>주민등록증: ' + (residentRegistration ? '제출' : '미제출') + '</li>' +
                    '<li>건강증명서 발급일: ' + healthCertificateDate + '</li>' +
                    '</ul>' +
                    '<button id="edit-button" data-id="' + uniqueId + '" class="edit-btn">수정</button>'; // 수정 버튼 추가


                // 수정 버튼 클릭 이벤트 리스너
                document.getElementById('edit-button').addEventListener('click', function () {
                    // 콘솔 로그로 변수 값 확인
                    openEditModal(uniqueId, name, birthday, gender, address, email, phone, bank, account, status, position, employmentContract, healthCertificate, identificationCopy, bankAccountCopy, residentRegistration, healthCertificateDate);
                });
            }
        });

        // TODO 우리형 공부 !! ^^

        /**
         * 밑에 함수들은 은행 아이디로 이름을 반환하거나
         * 이름으로 아이디를 반환하는 함수이다 !
         * 하지만 설계자의 문제로 bankId와 bankName 이라는 key 값으로 Json 데이터를 보내야 하나
         * bank 라는 key 값으로 은행 아이디를 value로 보냈다 !
         * 그럼 bankId=7, bankName=농협은행 이라는 데이터를 보내야되는 상황에서
         * bank=7 이라는 데이터를 보내고 있으니 이게 맞을까????
         * 그러니 반복문을 돌릴 때 만약 key 값이 bank 일 때 !!!!! 필터를 거는 것이다.
         * 왜? 다른 key 들은 올바르게 세팅되어 있어서 값을 넣고 있는 상황을 확인했기 때문이다 !
         * 그럼 bank=7 이라는 값을 가공을 해야되는데 그걸 밑에 2개의 함수가 처리를 해준다
         * 왜? 우리가 가지고 있는 건 은행 아이디거든
         * 그럼 bank=7이라는 데 k, v로 만들어서 보내주면 된다 !!! 그 코드가
         *
         * jsonData['bankId'] = value;
         * jsonData['bankName'] = getBankNameById(value);
         *
         * 이 코드이다 !
         */

        function getBankIdByName(bankName) {
            // bankMapping 배열에서 id가 일치하는 name 찾기
            const bank = bankMapping.find(b => b.name === bankName);
            return bank ? bank.id : null; // 해당하는 이름 반환
        }


        function getBankNameById(bankId) {
            // bankMapping 배열에서 id가 일치하는 name 찾기
            const bank = bankMapping.find(b => b.id === bankId);
            return bank ? bank.name : null; // 해당하는 이름 반환
        }


        function openEditModal(uniqueId, name, birthday, gender, address, email, phone, bank, account, status, position, employmentContract, healthCertificate, identificationCopy, bankAccountCopy, residentRegistration, healthCertificateDate) {
            // 모달 입력 필드에 데이터 세팅
            document.getElementById('editEmployeeId').value = uniqueId;
            document.getElementById('editEmployeeName').value = name;
            document.getElementById('editEmployeeBirthday').value = birthday;
            document.getElementById('editEmployeeGender').value = gender;
            document.getElementById('editEmployeeAddress').value = address;
            document.getElementById('editEmployeeEmail').value = email;
            document.getElementById('editEmployeePhone').value = phone;



            // 은행 및 직책 선택 설정
            const bankSelect = document.getElementById('bankSelect');
            for (let option of bankSelect.options) {
                if (option.value === getBankIdByName(bank)) {
                    option.selected = true; // 은행 ID와 일치하면 선택
                    break;
                }
            }

            const positionSelect = document.getElementById('positionSelect');
            for (let option of positionSelect.options) {
                if (option.value === position) {
                    option.selected = true; // 직책 ID와 일치하면 선택
                    break;
                }
            }

            document.getElementById('editEmployeeAccountNumber').value = account;
            document.getElementById('editEmployeeStatus').value = status;

            // 문서 제출 상태 체크박스 설정
            document.getElementById('editEmploymentContract').checked = employmentContract;
            document.getElementById('editHealthCertificate').checked = healthCertificate;
            document.getElementById('editIdentificationCopy').checked = identificationCopy;
            document.getElementById('editBankAccountCopy').checked = bankAccountCopy;
            document.getElementById('editResidentRegistration').checked = residentRegistration;
            document.getElementById('editHealthCertificateDate').value = healthCertificateDate;

            // 모달 열기
            openModal(); // openModal 함수는 기존에 정의한 모달 여는 함수
        }

        document.getElementById('editEmployeeForm').addEventListener('submit', function (event) {
            event.preventDefault();
            // 수정 요청 처리 로직 추가
            const formData = new FormData(event.target);
            console.log('event.target', event.target);
            console.log('formData', formData);
            // const formData = new FormData(this); // 폼 데이터 가져오기
            const jsonData = {}; // JSON 객체 초기화

            // FormData를 JSON 객체로 변환
            formData.forEach((value, key) => {

                if (key === 'bank') {
                    jsonData['bankId'] = value;
                    jsonData['bankName'] = getBankNameById(value);
                } else {
                    jsonData[key] = value;
                }
            });
            console.log('jsonData', jsonData);
            const employeeId = jsonData.id;

            // 예시: 수정된 직원 정보를 서버에 전송
            fetch('/erp/hr/employees/' + employeeId, {
                method: 'PUT',
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify(jsonData)
            })
                .then(response => {
                    if (response.ok) {
                        alert('직원 정보가 수정되었습니다.');
                        // 필요 시 직원 목록 새로 고침 또는 수정된 직원 정보 업데이트 로직 추가
                        closeModal();
                        location.reload();
                    } else {
                        alert('수정 실패: ' + response.statusText);
                    }
                })
                .catch(error => {
                    console.error('수정 요청 오류:', error);
                });
        });
    });
    function updateEmail() {
        const emailInput = document.getElementById("editEmployeeEmail");
        const domainInput = document.getElementById("emailDomain");
        const selectedDomain = document.getElementById("domainSelect").value;

        // 선택된 도메인이 있으면 도메인 입력란의 값으로 업데이트
        if (selectedDomain) {
            domainInput.value = selectedDomain; // 도메인 선택 시 입력란에 도메인 업데이트
        } else {
            domainInput.value = ''; // 직접 입력으로 전환 시 도메인 입력란 비우기
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
</script>


<style>
    .container {
        display: flex;
    }

    .left-panel {
        flex: 1; /* 왼쪽 패널의 너비 */
        padding: 20px; /* 패딩 추가 */
        border-right: 1px solid #ccc; /* 오른쪽 경계선 */
    }

    .right-panel {
        flex: 2; /* 오른쪽 패널의 너비 */
        padding: 20px; /* 패딩 추가 */
    }

    #editEmployeeModal {
        position: fixed;
        top: 50%;
        left: 50%;
        transform: translate(-50%, -50%);
        background-color: white;
        padding: 20px;
        border: 1px solid #ccc;
        z-index: 1000;
    }

    /* 모달 배경 스타일 */
    #modalBackground {
        position: fixed;
        top: 0;
        left: 0;
        width: 100%;
        height: 100%;
        background-color: rgba(0, 0, 0, 0.5);
        display: none; /* 처음에는 보이지 않음 */
    }
</style>

<!-- 모달 배경 -->
<div id="modalBackground" onclick="closeModal()"></div>

<%@ include file="/WEB-INF/view/erp/layout/erpFooter.jsp" %>

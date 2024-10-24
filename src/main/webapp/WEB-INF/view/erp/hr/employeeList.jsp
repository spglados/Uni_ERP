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
                        data-email="${employee.email}"
                        data-phone="${employee.phone}"
                        data-status="${employee.employmentStatus}"
                        data-bank="${employee.bankName != null ? employee.bankName : '정보 없음'}"
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
    <form id="editEmployeeForm">
        <input type="hidden" id="editEmployeeId" name="id" />

        <label for="editEmployeeName">이름:</label>
        <input type="text" id="editEmployeeName" name="name" required />

        <label for="editEmployeeBirthday">생년월일:</label>
        <input type="date" id="editEmployeeBirthday" name="birthday" required />

        <label for="editEmployeeEmail">이메일:</label>
        <input type="email" id="editEmployeeEmail" name="email" required />

        <label for="editEmployeePhone">전화번호:</label>
        <input type="tel" id="editEmployeePhone" name="phone" required />

        <!-- 은행 정보 추가 -->
        <label for="editEmployeeBank">은행:</label>
        <select id="editEmployeeBank" name="bankId" required>
            <c:forEach var="bank" items="${banks}">
                <option value="${bank.id}">${bank.name}</option>
            </c:forEach>
        </select>

        <label for="editEmployeeAccountNumber">계좌번호:</label>
        <input type="text" id="editEmployeeAccountNumber" name="accountNumber" required />

        <!-- 직책 정보 추가 -->
        <label for="editEmployeePosition">직책:</label>
        <select id="editEmployeePosition" name="positionId" required>
            <c:forEach var="position" items="${positions}">
                <option value="${position.id}">${position.name}</option>
            </c:forEach>
        </select>

        <!-- 문서 정보 추가 -->
        <h3>문서 제출 상태</h3>
        <p>문서 보관 여부를 선택해주세요.</p>
        <br>
        <div>
            <label for="editEmploymentContract">고용 계약서:</label>
            <input type="checkbox" id="editEmploymentContract" name="empDocumentDTO.employmentContract">
        </div>
        <div>
            <label for="editHealthCertificate">건강증명서:</label>
            <input type="checkbox" id="editHealthCertificate" name="empDocumentDTO.healthCertificate">
        </div>
        <div>
            <label for="editIdentificationCopy">신분증 사본:</label>
            <input type="checkbox" id="editIdentificationCopy" name="empDocumentDTO.identificationCopy">
        </div>
        <div>
            <label for="editBankAccountCopy">계좌 사본:</label>
            <input type="checkbox" id="editBankAccountCopy" name="empDocumentDTO.bankAccountCopy">
        </div>
        <div>
            <label for="editResidentRegistration">주민등록증:</label>
            <input type="checkbox" id="editResidentRegistration" name="empDocumentDTO.residentRegistration">
        </div>
        <div>
            <label for="editHealthCertificateDate">보건증 발급일:</label>
            <input type="date" id="editHealthCertificateDate" name="healthCertificateDate" />
        </div>

        <button type="submit">수정하기</button>
        <button type="button" onclick="closeModal()">취소</button>
    </form>
</div>

<script>
    function openModal() {
        document.getElementById('editEmployeeModal').style.display = 'block';
    }

    function closeModal() {
        document.getElementById('editEmployeeModal').style.display = 'none';
    }

    const employeesJson = '${employeesJson}'; // JSON 문자열을 올바르게 설정
    console.log(employeesJson); // JSON 문자열 출력 확인

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
                const gender = row.dataset.gender === 'M' ? '남성' : '여성';
                const email = row.dataset.email;
                const phone = row.dataset.phone;
                const status = row.dataset.status === 'ACTIVE' ? '재직' :
                    row.dataset.status === 'INACTIVE' ? '퇴사' : row.dataset.status === 'ONLEAVE' ? '휴직' : '정보 없음';
                const bank = row.dataset.bank;
                const account = row.dataset.account;

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
                    openEditModal(uniqueId, name, birthday, email, phone, bank, account, employmentContract, healthCertificate, identificationCopy, bankAccountCopy, residentRegistration, healthCertificateDate);
                });
            }
        });



        function openEditModal(uniqueId, name, birthday, email, phone, bank, account, employmentContract, healthCertificate, identificationCopy, bankAccountCopy, residentRegistration, healthCertificateDate) {
            // 모달 입력 필드에 데이터 세팅
            document.getElementById('editEmployeeId').value = uniqueId;
            document.getElementById('editEmployeeName').value = name;
            document.getElementById('editEmployeeBirthday').value = birthday;
            document.getElementById('editEmployeeEmail').value = email;
            document.getElementById('editEmployeePhone').value = phone;
            document.getElementById('editEmployeeBank').value = bank; // 선택된 은행 ID로 설정
            document.getElementById('editEmployeeAccountNumber').value = account;

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
            // 예시: 수정된 직원 정보를 서버에 전송
            fetch('/api/employees/' + formData.get('id'), {
                method: 'PUT',
                body: formData
            })
            .then(response => {
                if (response.ok) {
                    alert('직원 정보가 수정되었습니다.');
                    // 필요 시 직원 목록 새로 고침 또는 수정된 직원 정보 업데이트 로직 추가
                    closeModal();
                } else {
                    alert('수정 실패: ' + response.statusText);
                }
            })
            .catch(error => {
                console.error('수정 요청 오류:', error);
            });
        });
    });
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

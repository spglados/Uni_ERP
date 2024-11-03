<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ include file="/WEB-INF/view/erp/layout/erpHeader.jsp" %>
<link rel="stylesheet" href="/css/erp/hr/employeeList.css">
<div class="content" style="display: flex; justify-content: center; align-content: center; ">
    <div class="left-panel">
        <h1 class="mb-4">직원 목록</h1>
        <c:if test="${not empty employees}">
            <div class="mb-3">
                <label for="employmentStatusFilter" class="form-label">상태</label>
                <select id="employmentStatusFilter" class="form-select" onchange="filterEmployees()">
                    <option value="" <c:if test="${empty param.status}">selected</c:if>>전체</option>
                    <option value="ACTIVE" <c:if test="${param.status == 'ACTIVE'}">selected</c:if>>재직</option>
                    <option value="INACTIVE" <c:if test="${param.status == 'INACTIVE'}">selected</c:if>>퇴사</option>
                    <option value="ONLEAVE" <c:if test="${param.status == 'ONLEAVE'}">selected</c:if>>휴직</option>
                </select>
            </div>
            <table class="table table-striped">
                <thead>
                <tr>
                    <th scope="col">사원번호</th>
                    <th scope="col">이름</th>
                    <th scope="col">직책</th>
                    <th scope="col">상태</th>
                    <th scope="col">전화번호</th>
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
                        data-password="${employee.password}"
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
            <form id="excelDownloadForm" action="/erp/hr/download/excel" method="get" class="mt-3">
                <input type="hidden" name="employeeStatus" id="employeeStatus" value="${param.status}"/>
                <button type="submit" class="btn btn-primary">엑셀 다운로드</button>
            </form>
        </c:if>
        <c:if test="${empty employees}">
            <p>등록된 직원이 없습니다.</p>
        </c:if>
    </div>
    <div class="right-panel" id="employee-details">
        <h2 class="mb-0">직원 상세 정보</h2>
        <div class="card-body">
            <p>직원 목록을 클릭하여 확인하세요.</p>
            <!-- 상세 정보가 여기에 추가됩니다 -->
        </div>
    </div>
</div>
</div>

<!-- 수정 팝업 모달 -->
<div id="modalBackground" onclick="closeModal()"></div>
<div id="editEmployeeModal" class="modal" tabindex="-1" role="dialog" aria-labelledby="editEmployeeModalLabel"
     aria-hidden="true" style="display: none;">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="editEmployeeModalLabel">직원 수정</h5>
                <button type="button" class="close" onclick="closeModal()" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <form id="editEmployeeForm" onsubmit="return false;">
                    <input type="hidden" id="editEmployeeId" name="id"/>

                    <div class="mb-3">
                        <label for="editEmployeeName" class="form-label">이름</label>
                        <input type="text" class="form-control" id="editEmployeeName" name="name" maxlength="10"
                               required pattern="^[가-힣]{2,10}$"
                               title="이름은 한글 2~10자로 입력해야 합니다."/>
                    </div>

                    <div class="mb-3">
                        <label for="editEmployeeBirthday" class="form-label">생년월일</label>
                        <input type="date" class="form-control" id="editEmployeeBirthday" name="birthday" required
                               min="1900-01-01"
                               max="<%= new java.text.SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date()) %>"/>
                    </div>
                    <div class="mb-3">
                        <label for="editEmpPassword">비밀번호(숫자 4자리)</label>
                        <input type="password" id="editEmpPassword" name="password" pattern="\d{4}" maxlength="4"
                               title="숫자 4자리로 입력하세요"
                               required>
                    </div>
                    <div class="mb-3">
                        <label for="editEmployeeGender">성별</label>
                        <select id="editEmployeeGender" name="gender" required>
                            <option value="M">남성</option>
                            <option value="F">여성</option>
                        </select>
                    </div>
                    <div class="mb-3">
                        <label for="editEmployeeAddress" class="form-label">주소</label>
                        <input type="hidden" id="fullAddress" name="address">
                        <input type="text" class="form-control" id="editEmployeeAddress" onclick="execDaumPostcode()"
                               required>
                        <input type="text" class="form-control" id="editEmployeeDetailAddress">
                    </div>

                    <div class="mb-3">
                        <label for="editEmployeeEmail" class="form-label">이메일 아이디</label>
                        <div class="input-group">
                            <input type="text" class="form-control" id="editEmployeeEmail" name="email" required
                                   pattern="^[A-Za-z0-9._%+-]+$"
                                   title="유효한 이메일 형식으로 입력하세요" value=""/>
                            <span class="input-group-text">@</span>
                            <input type="text" class="form-control" id="emailDomain" name="emailDomain" required
                                   title="도메인을 입력하세요" value="">
                            <select id="domainSelect" class="form-select" onchange="updateEmailDomain()">
                                <option value="">직접 입력</option>
                                <option value="naver.com">naver.com</option>
                                <option value="daum.net">daum.net</option>
                                <option value="gmail.com">gmail.com</option>
                                <option value="nate.com">nate.com</option>
                            </select>
                        </div>
                        <button type="button" class="btn btn-secondary mt-2" onclick="checkEmailDuplicate()">중복 확인</button>
                        <!-- 여기의 ID를 emailDuplicateMessage로 수정 -->
                        <div id="duplicateEmailMessage" class="mt-1"></div>
                    </div>

                    <div class="mb-3">
                        <label for="editEmployeePhone" class="form-label">전화번호</label>
                        <input type="text" class="form-control" id="editEmployeePhone" name="phone" required
                               oninput="formatPhoneNumber(this)" maxlength="13"
                               placeholder="000-0000-0000"
                               title="유효한 전화번호 형식이 아닙니다."/>
                        <button type="button" class="btn btn-secondary mt-2" onclick="checkPhoneDuplicate()">중복 확인</button>
                        <!-- 여기의 ID를 phoneDuplicateMessage로 수정 -->
                        <div id="duplicatePhoneMessage" class="mt-1"></div>
                    </div>

                    <!-- 은행 정보 추가 -->
                    <div class="mb-3">
                        <label for="bankSelect" class="form-label">은행</label>
                        <select id="bankSelect" name="bank" class="form-select">
                            <c:forEach items="${banks}" var="bank">
                                <option value="${bank.id}">${bank.name}</option>
                            </c:forEach>
                        </select>
                    </div>

                    <div class="mb-3">
                        <label for="editEmployeeAccountNumber" class="form-label">계좌번호</label>
                        <input type="text" class="form-control" id="editEmployeeAccountNumber" name="accountNumber"
                               required
                               maxlength="16"
                               pattern="^\d{1,16}$"
                               title="계좌번호를 1자리 이상 16자리 이하의 숫자로 입력하세요"/>
                        <div id="accountDuplicateMessage" class="mt-1"></div>
                    </div>

                    <!-- 직책 정보 추가 -->
                    <div class="mb-3">
                        <label for="positionSelect" class="form-label">직책</label>
                        <select id="positionSelect" name="positionId" class="form-select">
                            <c:forEach items="${positions}" var="position">
                                <option value="${position.id}">${position.name}</option>
                            </c:forEach>
                        </select>
                    </div>

                    <div class="mb-3">
                        <label for="editEmployeeStatus" class="form-label">상태</label>
                        <select id="editEmployeeStatus" name="employmentStatus" class="form-select">
                            <option value="ACTIVE">재직</option>
                            <option value="INACTIVE">퇴사</option>
                            <option value="ONLEAVE">휴직</option>
                        </select>
                    </div>

                    <!-- 문서 정보 추가 -->
                    <h3>문서 제출 상태</h3>
                    <p>문서 보관 여부를 선택해주세요.</p>
                    <div class="mb-3 form-check">
                        <input type="checkbox" class="form-check-input" id="editEmploymentContract" value="true"
                               name="employmentContract">
                        <label class="form-check-label" for="editEmploymentContract">고용 계약서</label>
                    </div>
                    <div class="mb-3 form-check">
                        <input type="checkbox" class="form-check-input" id="editHealthCertificate" value="true"
                               name="healthCertificate">
                        <label class="form-check-label" for="editHealthCertificate">건강증명서</label>
                    </div>
                    <div class="mb-3 form-check">
                        <input type="checkbox" class="form-check-input" id="editIdentificationCopy" value="true"
                               name="identificationCopy">
                        <label class="form-check-label" for="editIdentificationCopy">신분증 사본</label>
                    </div>
                    <div class="mb-3 form-check">
                        <input type="checkbox" class="form-check-input" id="editBankAccountCopy" value="true"
                               name="bankAccountCopy">
                        <label class="form-check-label" for="editBankAccountCopy">계좌 사본</label>
                    </div>
                    <div class="mb-3 form-check">
                        <input type="checkbox" class="form-check-input" id="editResidentRegistration" value="true"
                               name="residentRegistration">
                        <label class="form-check-label" for="editResidentRegistration">주민등록증</label>
                    </div>
                    <div class="mb-3">
                        <label for="editHealthCertificateDate" class="form-label">보건증 발급일:</label>
                        <input type="date" class="form-control" id="editHealthCertificateDate"
                               name="healthCertificateDate"/>
                    </div>

                    <button type="submit" class="btn btn-primary" onclick="updateEmployee()">수정하기</button>
                    <button type="button" class="btn btn-secondary" onclick="closeModal()">취소</button>
                </form>
            </div>
        </div>
    </div>
</div>


<!-- Bootstrap Bundle with Popper.js (jsDelivr) -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-pwhx3de1z5koq2k9n7zn8XBc/4eKD48Wn5sbzIS5QJgEN5hYhDDK1e+FvY86G/Zg"
        crossorigin="anonymous"></script>
<script src="//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>
<script src="//dapi.kakao.com/v2/maps/sdk.js?appkey=3aea5e049cf7a27eae091e77ca0e1429&libraries=services"></script>
<script>

    function execDaumPostcode() {
        new daum.Postcode({
            oncomplete: function (data) {
                var addr = data.address;
                document.getElementById("editEmployeeAddress").value = addr;
            }
        }).open();
    }

    function filterEmployees() {
        // 선택된 필터 값 가져오기
        const selectedStatus = document.getElementById("employmentStatusFilter").value;

        // 모든 직원 행 가져오기
        const employeeRows = document.querySelectorAll(".employee-row");

        // 각 행을 필터링
        employeeRows.forEach(row => {
            // 각 행의 상태 값 가져오기
            const employeeStatus = row.getAttribute("data-status");

            // 선택된 상태가 없거나(전체) 직원 상태와 일치하는 경우 행 표시, 그렇지 않으면 숨김
            if (selectedStatus === "" || employeeStatus === selectedStatus) {
                row.style.display = ""; // 행 표시
            } else {
                row.style.display = "none"; // 행 숨기기
            }
        });
    }

    document.addEventListener("DOMContentLoaded", function () {
        const employeesData = JSON.parse('${employeesJson}'); // JSON 데이터를 파싱하여 사용
        console.log("Employees Data:", employeesData); // 콘솔에 출력하여 데이터 확인
    });


    const bankMapping = [
        <c:forEach items="${banks}" var="bank" varStatus="status">
        {id: "${bank.id}", name: "${bank.name}"}<c:if test="${!status.last}">, </c:if>
        </c:forEach>
    ];

    function openModal() {
        const modalBackground = document.getElementById('modalBackground');
        const editEmployeeModal = document.getElementById('editEmployeeModal');

        // 배경과 모달 보이기
        modalBackground.style.display = 'block'; // 배경 보이게 하기
        editEmployeeModal.style.display = 'block'; // 모달 보이게 하기

        // z-index 조정
        modalBackground.style.zIndex = '1000'; // 배경을 모달보다 위로
        editEmployeeModal.style.zIndex = '1001'; // 모달이 최상단
    }

    function closeModal() {
        const modalBackground = document.getElementById('modalBackground');
        const editEmployeeModal = document.getElementById('editEmployeeModal');

        // 배경과 모달 숨기기
        modalBackground.style.display = 'none'; // 배경 숨기기
        editEmployeeModal.style.display = 'none'; // 모달 숨기기

        // z-index 원래대로
        modalBackground.style.zIndex = '-1'; // 배경을 다시 아래로
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
                const addressParts = address.split(',');
                const email = row.dataset.email;
                const phone = row.dataset.phone;
                const status = row.dataset.status;
                const bank = row.dataset.bank;
                const account = row.dataset.account;
                const password = row.dataset.password;
                const position = row.dataset.position;

                // 문서 정보 추가
                const employmentContract = row.dataset.employmentcontract === 'true';
                const healthCertificate = row.dataset.healthcertificate === 'true';
                const identificationCopy = row.dataset.identificationcopy === 'true';
                const bankAccountCopy = row.dataset.bankaccountcopy === 'true';
                const residentRegistration = row.dataset.residentregistration === 'true';
                const healthCertificateDate = row.dataset.healthcertificatedate;

                // 직원 상세 정보를 표시
                const detailsDiv = document.getElementById('employee-details').querySelector('.card-body');
                detailsDiv.innerHTML =
                    '<div class="card">' +
                    '<div class="card-header">' +
                    '<h2>' + name + '의 상세 정보</h2>' +
                    '</div>' +
                    '<div class="card-body">' +
                    '<p><strong>사원번호:</strong> ' + uniqueId + '</p>' +
                    '<p><strong>생년월일:</strong> ' + birthday + '</p>' +
                    '<p><strong>성별:</strong> ' + (gender === 'F' ? '여자' : '남자') + '</p>' +
                    '<p><strong>주소:</strong> ' + addressParts[0] + ' ' + addressParts[1] + '</p>' +
                    '<p><strong>이메일:</strong> ' + email + '</p>' +
                    '<p><strong>비밀번호:</strong> ' + password + '</p>' +
                    '<p><strong>연락처:</strong> ' + phone + '</p>' +
                    '<p><strong>상태:</strong> ' + (status === 'ACTIVE' ? '재직' : (status === 'INACTIVE' ? '퇴사' : '휴직')) + '</p>' +
                    '<p><strong>은행:</strong> ' + bank + '</p>' +
                    '<p><strong>계좌번호:</strong> ' + account + '</p>' +
                    '<h3>문서 제출 여부</h3>' +
                    '<ul class="list-group mb-3">' +
                    '<li class="list-group-item">근로계약서: ' + (employmentContract ? '제출' : '미제출') + '</li>' +
                    '<li class="list-group-item">건강증명서: ' + (healthCertificate ? '제출' : '미제출') + '</li>' +
                    '<li class="list-group-item">신분증 사본: ' + (identificationCopy ? '제출' : '미제출') + '</li>' +
                    '<li class="list-group-item">은행 계좌 사본: ' + (bankAccountCopy ? '제출' : '미제출') + '</li>' +
                    '<li class="list-group-item">주민등록증: ' + (residentRegistration ? '제출' : '미제출') + '</li>' +
                    '<li class="list-group-item">건강증명서 발급일: ' + healthCertificateDate + '</li>' +
                    '</ul>' +
                    '<button id="edit-button" data-id="' + uniqueId + '" class="btn btn-primary">수정</button>'; // 수정 버튼


                // 수정 버튼 클릭 이벤트 리스너
                document.getElementById('edit-button').addEventListener('click', function () {
                    // 콘솔 로그로 변수 값 확인
                    openEditModal(uniqueId, name, birthday, gender, address, email, phone, bank, account, password, status, position, employmentContract, healthCertificate, identificationCopy, bankAccountCopy, residentRegistration, healthCertificateDate);
                });
            }
        });


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

        function openEditModal(uniqueId, name, birthday, gender, address, email, phone, bank, account, password, status, position, employmentContract, healthCertificate, identificationCopy, bankAccountCopy, residentRegistration, healthCertificateDate) {
            // 모달 입력 필드에 데이터 세팅
            const addressParts = address.split(',');
            document.getElementById('editEmployeeId').value = uniqueId;
            document.getElementById('editEmployeeName').value = name;
            document.getElementById('editEmployeeBirthday').value = birthday;
            document.getElementById('editEmployeeGender').value = gender;
            document.getElementById('editEmpPassword').value = password;
            document.getElementById('editEmployeeAddress').value = addressParts[0];
            document.getElementById('editEmployeeDetailAddress').value = addressParts[1];
            document.getElementById('editEmployeeEmail').value = email.split('@')[0];
            document.getElementById('emailDomain').value = email.split('@')[1];
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

                if (key === 'address') {
                    const mainAddress = document.getElementById('editEmployeeAddress').value;
                    const detailAddress = document.getElementById('editEmployeeDetailAddress').value;
                    jsonData[key] = mainAddress + " , " + detailAddress;
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
                .then(response => response.json())
                .then(data => {
                    if (data.success) {
                        alert('직원 정보가 수정되었습니다.');
                        // 필요 시 직원 목록 새로 고침 또는 수정된 직원 정보 업데이트 로직 추가
                        closeModal();
                        location.reload();
                    } else if (data.fail) {
                        alert('수정 실패: ' + data.fail);
                    }
                })
                .catch(error => {
                    console.error('수정 요청 오류:', error);
                });
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

    });

    function updateEmailDomain() {
        const domainSelect = document.getElementById("domainSelect");
        const emailDomainInput = document.getElementById("emailDomain");


        // 선택된 도메인 값을 가져옴
        const selectedDomain = domainSelect.value;

        // 선택된 도메인이 있을 경우 emailDomain 입력란 업데이트
        if (selectedDomain) {
            emailDomainInput.value = selectedDomain; // 선택한 도메인으로 도메인 입력란 업데이트
        } else {
            emailDomainInput.value = ""; // 도메인이 선택되지 않은 경우 초기화
        }

        // 이메일 ID를 업데이트하는 함수 호출
        updateFullEmail();
    }

    function updateFullEmail() {
        const emailIdInput = document.getElementById("editEmployeeEmail");
        const emailDomainInput = document.getElementById("emailDomain");

        const emailId = emailIdInput.value.split('@')[0]; // '@' 이전의 이메일 ID
        const domainValue = emailDomainInput.value; // 도메인 입력란의 값

        // 도메인이 존재할 경우 이메일 ID는 그대로 두고, 도메인 선택 시 사용
        if (domainValue) {
            emailIdInput.value = emailId; // 이메일 ID를 그대로 유지
        }
    }


    function checkEmailDuplicate() {
        const emailId = document.getElementById('editEmployeeEmail').value;
        const emailDomain = document.getElementById('emailDomain').value;
        const fullEmail = emailId.toLowerCase() + '@' + emailDomain.toLowerCase();

        fetch('/erp/hr/check-email?email=' + fullEmail)
            .then(response => response.json())
            .then(data => {
                const emailMessageDiv = document.getElementById('duplicateEmailMessage');
                if (data.isDuplicated) {
                    emailMessageDiv.textContent = '이메일이 중복되었습니다. 다른 이메일을 입력하세요.';
                    emailMessageDiv.style.color = 'red';
                } else {
                    emailMessageDiv.textContent = '사용 가능한 이메일입니다.';
                    emailMessageDiv.style.color = 'green';
                }
            })
            .catch(error => console.error('이메일 중복 확인 오류:', error));
    }

    function checkPhoneDuplicate() {
        const phone = document.getElementById('editEmployeePhone').value;

        fetch('/erp/hr/check-phone?phone=' + phone)
            .then(response => response.json())
            .then(data => {
                const phoneMessageDiv = document.getElementById('duplicatePhoneMessage');
                if (data.isDuplicated) {
                    phoneMessageDiv.textContent = '전화번호가 중복되었습니다. 다른 전화번호를 입력하세요.';
                    phoneMessageDiv.style.color = 'red';
                } else {
                    phoneMessageDiv.textContent = '사용 가능한 전화번호입니다.';
                    phoneMessageDiv.style.color = 'green';
                }
            })
            .catch(error => console.error('전화번호 중복 확인 오류:', error));
    }



    // 선택된 도메인이 있으면 도메인 입력란의 값으로 업데이트
    if (selectedDomain) {
        domainInput.value = selectedDomain; // 도메인 선택 시 입력란에 도메인 업데이트
    } else {
        domainInput.value = ''; // 직접 입력으로 전환 시 도메인 입력란 비우기
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

    function updateEmployee() {
        const emailIdInput = document.getElementById("editEmployeeEmail");
        const emailDomainInput = document.getElementById("emailDomain");

        const fullEmail = emailIdInput.value + "@" + emailDomainInput.value; // 전체 이메일
        const existingEmails = ["existing1@example.com", "existing2@example.com"]; // DB에서 가져온 기존 이메일 목록

        // 기존 이메일과 비교
        if (existingEmails.includes(fullEmail)) {
            alert("중복된 이메일입니다."); // 중복 경고 메시지
            return; // 수정 막기
        }

    }
</script>


<!-- 모달 배경 -->


<%@ include file="/WEB-INF/view/erp/layout/erpFooter.jsp" %>

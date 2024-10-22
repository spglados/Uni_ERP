<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
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
                        data-healthcertificatedate="${employee.healthCertificateDate}">
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

<script>
    const employeesJson = '${fn:escapeXml(employeesJson)}'; // JSON 문자열을 올바르게 설정
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
                let documents = {};

                // 현재 선택한 row의 employee_id를 uniqueId로 사용하여 employeesData에서 해당 employee의 문서 정보 찾기
                const employeeDocument = employeesData.find(emp => emp.uniqueEmployeeNumber === uniqueId);
                console.log('employeeDocument', employeeDocument);

                if (employeeDocument) {
                    documents = {
                        employmentContract: employeeDocument.employmentContract,
                        healthCertificate: employeeDocument.healthCertificate,
                        healthCertificateDate: employeeDocument.healthCertificateDate,
                        identificationCopy: employeeDocument.identificationCopy,
                        bankAccountCopy: employeeDocument.bankAccountCopy,
                        residentRegistration: employeeDocument.residentRegistration
                    };
                } else {
                    console.error("해당 employee_id에 맞는 문서 정보를 찾을 수 없습니다.");
                }

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
                    '<li>근로계약서: ' + (documents.employmentContract ? '제출됨' : '미제출') + '</li>' +
                    '<li>보건증: ' + (documents.healthCertificate ? '제출됨' : '미제출') + '</li>' +
                    '<li>보건증 발급일: ' + (documents.healthCertificateDate ? documents.healthCertificateDate : '정보 없음') + '</li>' +
                    '<li>신분증 사본: ' + (documents.identificationCopy ? '제출됨' : '미제출') + '</li>' +
                    '<li>통장 사본: ' + (documents.bankAccountCopy ? '제출됨' : '미제출') + '</li>' +
                    '<li>주민등록등본: ' + (documents.residentRegistration ? '제출됨' : '미제출') + '</li>' +
                    '</ul>';
            }
        });
    });
</script>

<style>
    .container {
        display: flex;
    }

    .left-panel {
        flex: 1; /* 왼쪽 패널의 너비 */
        padding: 20px;
        border-right: 1px solid #ccc; /* 구분선 */
    }

    .right-panel {
        flex: 2; /* 오른쪽 패널의 너비 */
        padding: 20px;
    }

    .employee-row.selected {
        background-color: #e0e0e0; /* 선택된 행의 배경색 */
    }
</style>

<%@ include file="/WEB-INF/view/erp/layout/erpFooter.jsp" %>

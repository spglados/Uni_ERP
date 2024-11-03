<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ include file="/WEB-INF/view/erp/layout/erpHeader.jsp" %>

<!-- Bootstrap CSS -->
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">

<!-- Custom CSS -->
<style>
    :root {
        --main-color: #F8F399;
        --main-color-dark: #e0d67a;
        --main-color-light: #fff8e6;
    }

    .btn-main {
        background-color: var(--main-color);
        border-color: var(--main-color);
        color: #000;
    }

    .btn-main-size {
        width: 100%;
    }

    .btn-main:hover {
        background-color: var(--main-color-dark);
        border-color: var(--main-color-dark);
        color: #000;
    }

    .card-custom {
        border: none;
        border-radius: 10px;
        box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
        background-color: #fff;
        padding: 20px;
        margin: 10px;
        flex: 1 1 100%;
    }

    .table-custom th {
        background-color: var(--main-color);
        color: #000;
    }

    .form-control:disabled, .form-control[readonly] {
        background-color: #f5f5f5;
    }

    /* Flexbox 컨테이너 */
    .cards-container {
        display: flex;
        flex-wrap: wrap;
        justify-content: space-between;
        gap: 20px; /* 카드 간의 간격 */
    }

    /* Left and Right Containers */
    .left-container {
        display: flex;
        flex-direction: column;
        gap: 20px;
        flex: 1;
        min-width: 300px;
    }

    .right-container {
        flex: 2;
        min-width: 300px;
    }

    /* Flexbox for form elements */
    .form-group {
        display: flex;
        align-items: center;
        margin-bottom: 15px;
    }

    .form-group label {
        flex: 0 0 150px;
        margin-bottom: 0;
    }

    .form-group .form-control {
        flex: 1;
    }

    /* Flexbox for buttons */
    .button-group {
        display: flex;
        justify-content: center;
        gap: 10px;
        margin-top: 20px;
    }

    /* Flexbox for position list forms */
    .position-form {
        display: flex;
        align-items: center;
        gap: 10px;
        flex-wrap: wrap;
    }

    .position-form .form-control {
        flex: 1 1 150px;
    }

    /* Flexbox for position registration */
    .position-registration {
        display: flex;
        align-items: center;
        gap: 10px;
        flex-wrap: wrap;
    }

    .position-registration .form-control {
        flex: 1 1 200px;
    }

    @media (max-width: 992px) {
        .cards-container {
            flex-direction: column;
        }

        .left-container, .right-container {
            flex: 1 1 100%;
        }
    }
</style>

<div class="container mt-5">
    <!-- Flexbox 컨테이너 시작 -->
    <div class="cards-container">
        <!-- Left Container -->
        <div class="left-container">
            <!-- Store Update Form -->
            <div class="card card-custom">
                <h1 class="text-center mb-4">가게 수정</h1>
                <form method="post" action="${pageContext.request.contextPath}/erp/store/update"
                      id="storeForm" class="needs-validation" novalidate>

                    <input type="hidden" name="id" value="${storeId}"/>

                    <div class="form-group">
                        <label for="storeName">가게 이름</label>
                        <input type="text" name="name" readonly id="storeName" class="form-control"
                               value="${store.name}" required>
                        <div class="invalid-feedback">
                            가게 이름을 입력해주세요.
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="storeAddress">가게 주소</label>
                        <input type="text" name="storeAddress" readonly id="storeAddress" class="form-control"
                               value="${store.storeAddress}" required>
                        <div class="invalid-feedback">
                            가게 주소를 입력해주세요.
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="is24Hours">24시간 운영 여부</label>
                        <select name="is24Hours" id="is24Hours" class="form-select" disabled required>
                            <c:choose>
                                <c:when test="${store.is24Hours == 0}">
                                    <option value="0" selected>아니요</option>
                                    <option value="1">네</option>
                                </c:when>
                                <c:otherwise>
                                    <option value="0">아니요</option>
                                    <option value="1" selected>네</option>
                                </c:otherwise>
                            </c:choose>
                        </select>
                        <div class="invalid-feedback">
                            운영 여부를 선택해주세요.
                        </div>
                    </div>
                    <div class="button-group">
                        <button type="button" id="editButton" onclick="enableEdit()" class="btn btn-main btn-main-size">수정</button>
                        <input type="submit" id="submitButton" value="수정 완료" class="btn btn-main btn-main-size d-none"/>
                        <button type="button" id="cancelButton" style="width: 100%" onclick="cancelEdit()" class="btn btn-secondary d-none">
                            취소
                        </button>
                    </div>
                </form>
            </div>

            <!-- Position Registration -->
            <div class="card card-custom">
                <h2 class="text-center mb-4">포지션 등록</h2>
                <form id="positionRegistrationForm" class="position-registration needs-validation" novalidate>

                    <input type="hidden" name="storeId" value="${storeId}"/>

                    <div class="d-flex flex-md-row align-items-center gap-3">
                        <div>
                            <input type="text" id="newPosition" name="name" class="form-control" placeholder="직책 이름"
                                   style="margin: 10px" required>
                            <input type="number" id="minRequire" name="minRequiredNum" class="form-control"
                                   placeholder="최소 요구 인원 수" style="margin: 10px" required>
                            <button type="button" onclick="createPosition()" class="btn btn-main btn-main-size">등록</button>
                        </div>
                    </div>
                    <div class="invalid-feedback">
                        모든 필드를 올바르게 입력해주세요.
                    </div>
                </form>
            </div>
        </div>

        <!-- Right Container -->
        <div class="right-container">
            <!-- Position List -->
            <div class="card card-custom">
                <h2 class="mb-4">포지션 목록</h2>
                <table class="table table-hover table-bordered table-custom">
                    <thead>
                    <tr>
                        <th>포지션 이름</th>
                        <th>최소 요구 인원 수</th>
                        <th>작업</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="position" items="${positions}">
                        <c:if test="${position.name != '미정'}">
                            <tr>
                                <td>${position.name}</td>
                                <td>${position.minRequiredNum}</td>
                                <td>
                                    <form method="post"
                                          action="${pageContext.request.contextPath}/erp/store/position/update"
                                          class="position-form needs-validation" novalidate>
                                        <input type="hidden" name="id" value="${position.id}"/>
                                        <input type="text" name="name" class="form-control" style="max-width: 200px;"
                                               value="${position.name}" required>
                                        <input type="number" name="minRequiredNum" class="form-control"
                                               style="max-width: 150px;"
                                               value="${position.minRequiredNum}" required>
                                        <input type="submit" value="수정" class="btn btn-main btn-sm"/>
                                        <button type="button" onclick="deletePosition(${position.id})"
                                                class="btn btn-secondary btn-sm">삭제
                                        </button>
                                        <div class="invalid-feedback">
                                            모든 필드를 올바르게 입력해주세요.
                                        </div>
                                    </form>
                                </td>
                            </tr>
                        </c:if>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
    <!-- Flexbox 컨테이너 끝 -->
</div>

<!-- jQuery and Bootstrap JS -->
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>

<!-- Custom JS -->
<script>
    $(document).ready(function () {
        // Form validation
        (function () {
            'use strict'
            var forms = document.querySelectorAll('.needs-validation')
            Array.prototype.slice.call(forms)
                .forEach(function (form) {
                    form.addEventListener('submit', function (event) {
                        if (!form.checkValidity()) {
                            event.preventDefault()
                            event.stopPropagation()
                        }
                        form.classList.add('was-validated')
                    }, false)
                })
        })()

        $("#editButton").on("click", function () {
            if (confirm("가게 정보를 수정하시겠습니까?")) {
                enableEdit();
            }
        });

        $("#cancelButton").on("click", function () {
            cancelEdit();
        });

        $("#submitButton").on("click", function (e) {
            if (!confirm("수정된 내용을 저장하시겠습니까?")) {
                e.preventDefault();
            }
        });
    });

    function enableEdit() {
        $("#storeName, #storeAddress").prop("readonly", false);
        $("#is24Hours").prop("disabled", false); // 24시간 운영 여부 활성화
        $("#editButton").addClass("d-none");
        $("#cancelButton, #submitButton").removeClass("d-none").prop("disabled", false);
    }

    function cancelEdit() {
        $("#storeName, #storeAddress").prop("readonly", true);
        $("#is24Hours").prop("disabled", true);
        $("#editButton").removeClass("d-none");
        $("#cancelButton, #submitButton").addClass("d-none").prop("disabled", true);
    }

    function deletePosition(positionId) {
        if (!confirm('정말 삭제하시겠습니까?')) return;
        fetch('${pageContext.request.contextPath}/erp/store/position/' + positionId, {
            method: 'DELETE',
            headers: {
                'X-CSRF-TOKEN': "${_csrf.token}"
            }
        })
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    alert('삭제되었습니다.');
                    window.location.reload();
                } else {
                    alert('삭제 중 오류가 발생했습니다.');
                }
            })
            .catch(error => console.log('error', error));
    }

    function createPosition() {
        let newPosition = document.getElementById('newPosition').value.trim();
        let minRequire = document.getElementById('minRequire').value.trim();

        if (newPosition === '' || minRequire === '') {
            alert('값이 비어있습니다.');
            return;
        }

        const requestBody = JSON.stringify({
            name: newPosition,
            minRequiredNum: parseInt(minRequire),
            storeId: "${store.id}"
        });

        fetch('${pageContext.request.contextPath}/erp/store/position/create', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'X-CSRF-TOKEN': "${_csrf.token}"
            },
            body: requestBody
        })
            .then(response => {
                if (!response.ok) throw new Error('Network response was not ok');
                return response.json();
            })
            .then(data => {
                console.log('data', data);
                alert('등록 성공');
                window.location.reload();
            })
            .catch(error => {
                console.log('error', error);
                alert('등록 실패: ' + error.message);
            });
    }
</script>

<%@ include file="/WEB-INF/view/erp/layout/erpFooter.jsp" %>

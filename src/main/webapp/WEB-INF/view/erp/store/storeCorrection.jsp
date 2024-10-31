<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ include file="/WEB-INF/view/erp/layout/erpHeader.jsp" %>

<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">

<div class="container mt-5">
    <h1 class="text-center">가게 수정</h1>

    <!-- Store Update Form -->
    <form:form method="post" action="${pageContext.request.contextPath}/erp/store/update"
               modelAttribute="store" id="storeForm" class="border rounded p-4 shadow-sm">
        <form:hidden path="id"/>
        <div class="mb-3 row">
            <label for="storeName" class="col-sm-3 col-form-label">가게 이름</label>
            <div class="col-sm-9">
                <form:input path="name" readonly="true" id="storeName" class="form-control"/>
            </div>
        </div>
        <div class="mb-3 row">
            <label for="storeAddress" class="col-sm-3 col-form-label">가게 주소</label>
            <div class="col-sm-9">
                <form:input path="storeAddress" readonly="true" id="storeAddress" class="form-control"/>
            </div>
        </div>
        <div class="text-center">
            <button type="button" id="editButton" onclick="enableEdit()" class="btn btn-primary">수정</button>
            <input type="submit" id="submitButton" value="수정 완료" class="btn btn-success d-none"/>
            <button type="button" id="cancelButton" onclick="cancelEdit()" class="btn btn-secondary d-none">취소</button>
        </div>
    </form:form>

    <!-- Position List -->
    <h2 class="mt-5">포지션 목록</h2>
    <table class="table table-hover table-bordered">
        <thead class="table-light">
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
                            <form:form method="post" action="${pageContext.request.contextPath}/erp/store/position/update"
                                       modelAttribute="storePositionDTO" class="d-inline">
                                <form:hidden path="id" value="${position.id}"/>
                                <form:input path="name" value="${position.name}" class="form-control"/>
                                <form:input path="minRequiredNum" value="${position.minRequiredNum}" class="form-control"/>
                                <input type="submit" value="수정" class="btn btn-warning btn-sm"/>
                            </form:form>
                            <button onclick="deletePosition(${position.id})" class="btn btn-danger btn-sm">삭제</button>
                        </td>
                    </tr>
                </c:if>
            </c:forEach>
        </tbody>
    </table>

    <!-- Position Registration -->
    <h2 class="mt-5">포지션 등록</h2>
    <div class="input-group mb-3">
        <input type="text" id="newPosition" class="form-control" placeholder="직책 이름" required>
        <input type="number" id="minRequire" class="form-control" placeholder="최소 요구 인원 수" required>
        <button onclick="createPosition()" class="btn btn-success">등록</button>
    </div>
</div>

<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script>
    $(document).ready(function () {
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
        $("#editButton").addClass("d-none");
        $("#cancelButton, #submitButton").removeClass("d-none");
    }

    function cancelEdit() {
        $("#storeName, #storeAddress").prop("readonly", true);
        $("#editButton").removeClass("d-none");
        $("#cancelButton, #submitButton").addClass("d-none");
    }

    function deletePosition(positionId) {
        if (!confirm('정말 삭제하시겠습니까?')) return;
        fetch('/erp/store/position/' + positionId, { method: 'DELETE' })
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
        let newPosition = document.getElementById('newPosition').value;
        let minRequire = document.getElementById('minRequire').value;

        if (newPosition === '' || minRequire === '') {
            alert('값이 비어있습니다.');
            return;
        }

        const requestBody = JSON.stringify({
            name: newPosition,
            minRequiredNum: parseInt(minRequire)
        });

        fetch('/erp/store/position/create', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
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

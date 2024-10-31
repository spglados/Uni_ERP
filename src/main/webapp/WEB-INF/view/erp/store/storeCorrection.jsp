<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ include file="/WEB-INF/view/erp/layout/erpHeader.jsp" %>

<title>가게 수정</title>
<h1>가게 수정</h1>

<form:form method="post" action="${pageContext.request.contextPath}/erp/store/update" modelAttribute="store"
           id="storeForm">
    <form:hidden path="id"/>
    <table>
        <tr>
            <th>가게 이름</th>
            <td>
                <form:input path="name" readonly="true" id="storeName"/>
            </td>
        </tr>
        <tr>
            <th>가게 주소</th>
            <td>
                <form:input path="storeAddress" readonly="true" id="storeAddress"/>
            </td>
        </tr>
        <tr>
            <td colspan="2">
                <button type="button" id="editButton" onclick="enableEdit()">수정</button>
                <input type="submit" id="submitButton" value="수정 완료" style="display:none;"/>
                <button type="button" id="cancelButton" onclick="cancelEdit()" style="display:none;">취소</button>
            </td>
        </tr>
    </table>
</form:form>

<h2>포지션 목록</h2>
<table>
    <thead>
    <tr>
        <th>포지션 ID</th>
        <th>포지션 이름</th>
        <th>최소 요구 인원 수</th>
        <th>작업</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="position" items="${positions}">
        <tr>
            <td>${position.id}</td>
            <td>${position.name}</td>
            <td>${position.minRequiredNum}</td>
             <td>
            <c:if test="${position.name != '미정'}">
                <!-- 일반 직책에 대해서만 수정과 삭제 버튼을 보여줍니다. -->
                <form:form method="post" action="${pageContext.request.contextPath}/erp/store/position/update" modelAttribute="storePositionDTO">
                    <form:hidden path="id" value="${position.id}"/>
                    <form:input path="name" value="${position.name}"/>
                    <form:input path="minRequiredNum" value="${position.minRequiredNum}"/>
                    <input type="submit" value="수정"/>
                </form:form>
                   <button onclick="deletePosition(${position.id})">삭제</button>
            </c:if>
            <c:if test="${position.name == '미정'}">
                <!-- "미정" 직책은 수정 및 삭제 버튼이 비활성화 됩니다. -->
                <span>미정 (수정 및 삭제 불가)</span>
            </c:if>
        </td>
        </tr>
    </c:forEach>
    </tbody>
</table>

<h2>포지션 등록</h2>
<table>
<thead>
<tr>
<th>직책 이름</th>
<th>최소 요구인원 수</th>
</tr>
</thead>
<tbody>
<tr>
<td>
<input type="text" id="newPosition" placeholder="직책 이름" required>
<input type="number" id="minRequire" placeholder="최소요구 인원 수" required>
</td>
<td><button onclick="createPosition()">등록</button></td>
</tr>
</tbody>
</table>

</table>

<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
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
                e.preventDefault(); // 사용자가 취소할 경우 폼 제출을 막음
            }
        });
    });

    function enableEdit() {
        $("#storeName").prop("readonly", false);
        $("#storeAddress").prop("readonly", false);
        $("#editButton").hide();
        $("#cancelButton").show();
        $("#submitButton").show();
    }

    function cancelEdit() {
        $("#storeName").prop("readonly", true);
        $("#storeAddress").prop("readonly", true);
        $("#editButton").show();
        $("#cancelButton").hide();
        $("#submitButton").hide();
    }

    function deletePosition(positionId) {

        if(!confirm('정말 삭제하시겠습니까?')) {
            return;
        }

        fetch('/erp/store/position/' + positionId, {
            method: 'DELETE'
        })
        .then(response => response.json())
        .then(data => {
            if(data.success) {
                alert('삭제되었습니다.');
                window.location.reload();
            } else if(!data.fail) {
                alert('삭제 중 오류가 발생했습니다.');
            }
        })
        .catch(error => {
            console.log('error', error);
        });

    }

  function createPosition() {
    let newPosition = document.getElementById('newPosition').value;
    let minRequire = document.getElementById('minRequire').value;

    if (newPosition === '' || minRequire === '') {
        alert('값이 비어있습니다.');
        return;
    }

    // 요청 본문을 JSON 문자열로 변환
    const requestBody = JSON.stringify({
        name: newPosition,
        minRequiredNum: parseInt(minRequire) // 숫자로 변환
    });

    fetch('/erp/store/position/create', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: requestBody // 수정된 부분
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('Network response was not ok');
        }
        return response.json();
    })
    .then(data => {
        console.log('data', data);
        alert('등록 성공');
        window.location.reload(); // 페이지 새로 고침 추가
    })
    .catch(error => {
        console.log('error', error);
        alert('등록 실패: ' + error.message);
    });
}
</script>

<%@ include file="/WEB-INF/view/erp/layout/erpFooter.jsp" %>

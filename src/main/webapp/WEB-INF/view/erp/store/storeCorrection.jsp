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
                <form:form method="post" action="${pageContext.request.contextPath}/erp/store/position/update"
                           modelAttribute="storePositionDTO">
                    <form:hidden path="id" value="${position.id}"/>
                    <form:input path="name" value="${position.name}"/>
                    <form:input path="minRequiredNum" value="${position.minRequiredNum}"/>
                    <input type="submit" value="수정"/>
                </form:form>
                <form:form method="post"
                           action="${pageContext.request.contextPath}/erp/store/position/delete/${position.id}"
                           onsubmit="console.log('Deleting position with ID:', ${position.id});">
                    <input type="submit" value="삭제" onclick="return confirm('정말 삭제하시겠습니까?');"/>
                </form:form>
            </td>
        </tr>
    </c:forEach>
    </tbody>
</table>

<h2>포지션 등록</h2>
<form:form method="post" action="${pageContext.request.contextPath}/erp/store/position/create"
           modelAttribute="storePositionDTO">
    <table>
        <tr>
            <th>포지션 이름</th>
            <td>
                <form:input path="name"/>
            </td>
        </tr>
        <tr>
            <th>최소 요구 인원 수</th>
            <td>
                <form:input path="minRequiredNum"/>
            </td>
        </tr>
        <tr>
            <td colspan="2">
                <input type="submit" value="포지션 등록"/>
            </td>
        </tr>
    </table>
</form:form>

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
</script>

<%@ include file="/WEB-INF/view/erp/layout/erpFooter.jsp" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ include file="/WEB-INF/view/erp/layout/erpHeader.jsp" %>

<!-- 메인 컨텐츠 -->
<div class="content">
    <h1>직원 등록 입니다.</h1>

    <form action="/employee-register" method="post" id="employeeRegisterForm">
        <div>
            <label for="name">이름:</label>
            <input type="text" id="name" name="name" required>
        </div>
        <div>
            <label for="birthday">생년월일:</label>
            <input type="date" id="birthday" name="birthday" required>
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
            <input type="email" id="email" name="email" required>
        </div>
        <div>
            <label for="phone">전화번호:</label>
            <input type="text" id="phone" name="phone" required>
        </div>
        <div>
            <label for="accountNumber">계좌번호:</label>
            <input type="text" id="accountNumber" name="accountNumber" required>
        </div>
        <div>
            <label for="position">직책:</label>
            <input type="text" id="position" name="position" required placeholder="직책을 입력하세요">
        </div>
        <div>
            <button type="submit">직원 등록</button>
        </div>
    </form>
</div>

<%@ include file="/WEB-INF/view/erp/layout/erpFooter.jsp" %>

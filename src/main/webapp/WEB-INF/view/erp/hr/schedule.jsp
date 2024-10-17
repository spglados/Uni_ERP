<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@include file="/WEB-INF/view/erp/layout/erpHeader.jsp" %>
<link rel="stylesheet" href="/css/erp/hr/calendar.css">
<link rel="stylesheet" href="/css/erp/hr/scheduleModal.css">
<link href='https://cdn.jsdelivr.net/npm/@fortawesome/fontawesome-free@5.13.1/css/all.css' rel='stylesheet'>
<div class="p-3 w-100" id='calendar'></div>
<div id="addEventModal" class="modal">
    <div class="modal-content">
        <span class="close" id="closeAddEventButton">&times;</span>
        <h2>일정 추가</h2>
        <form id="addEventForm">
            <label for="employeeSelect">근무자 선택 :</label>
            <select id="employeeSelect" name="employeeSelect" required>
                <c:forEach var="employee" items="${employees}">
                    <option value="${employee.id}">${employee.name}</option>
                </c:forEach>
            </select><br>

            <label for="eventStart">날짜:</label>
            <input type="date" id="eventStart" name="eventStart" required>
            <label for="eventStartTime">시작 시간:</label>
            <select id="eventStartTime" name="eventStartTime" required></select><br><br>

            <div class="d-flex justify-content-between">
                <label for="eventEndTime">종료 시간:</label>
                <div>
                    <label for="isNextDay">다음 날 종료</label>
                    <input type="checkbox" id="isNextDay" name="isNextDay">
                </div>
            </div>
            <select id="eventEndTime" name="eventEndTime" required></select><br><br>

            <button type="submit">추가</button>
        </form>
    </div>
</div>

<%-- 일정 데이터 초기화 --%>
<script type="application/javascript">
    const scheduleList = JSON.parse('${schedules}');
    const employeeList = JSON.parse('${employees}');
</script>
<%-- 캘린더 초기 랜더링 --%>
<script type="module">
    import {initializeCalendar} from '/js/erp/hr/calendarModule.js';

    document.addEventListener('DOMContentLoaded', function () {
        const calendarEl = document.getElementById('calendar');
        initializeCalendar(calendarEl, scheduleList);
    });

</script>
<%@include file="/WEB-INF/view/erp/layout/erpFooter.jsp" %>
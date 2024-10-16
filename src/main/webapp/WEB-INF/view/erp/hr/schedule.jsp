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
            <label for="eventTitle">근무자 이름 :</label>
            <input type="text" id="eventTitle" name="eventTitle" required><br><br>

            <label for="eventStart">날짜:</label>
            <input type="date" id="eventStart" name="eventStart" required>
            <label for="eventStartTime">시작 시간:</label>
            <select id="eventStartTime" name="eventStartTime" required></select><br><br>

            <label for="eventEndTime">종료 시간:</label>
            <select id="eventEndTime" name="eventEndTime" required></select><br><br>

            <button type="submit">추가</button>
        </form>
    </div>
</div>

<%-- 일정 데이터 초기화 --%>
<script type="application/javascript">
    const schedules = "${schedules}";
    console.log(schedules);
</script>
<%-- 캘린더 초기 랜더링 --%>
<script type="module">
    import { initializeCalendar } from '/js/erp/hr/calendarModule.js';

    document.addEventListener('DOMContentLoaded', function() {
        const calendarEl = document.getElementById('calendar');
        initializeCalendar(calendarEl);
    });

</script>
<%@include file="/WEB-INF/view/erp/layout/erpFooter.jsp" %>
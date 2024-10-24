<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@include file="/WEB-INF/view/erp/layout/erpHeader.jsp" %>

<%-- FullCalendar 4.4.2 CSS (jsDelivr) --%>
<link href='https://cdn.jsdelivr.net/npm/@fullcalendar/core@4.4.2/main.min.css' rel='stylesheet'/>
<link href='https://cdn.jsdelivr.net/npm/@fullcalendar/daygrid@4.4.2/main.min.css' rel='stylesheet'/>
<link href='https://cdn.jsdelivr.net/npm/@fullcalendar/timegrid@4.4.2/main.min.css' rel='stylesheet'/>
<link href='https://cdn.jsdelivr.net/npm/@fullcalendar/bootstrap@4.4.2/main.min.css' rel='stylesheet'/>
<link href='https://cdn.jsdelivr.net/npm/@fortawesome/fontawesome-free@5.13.1/css/all.css' rel='stylesheet'>
<!-- Custom CSS -->
<link rel="stylesheet" href="/css/erp/hr/calendar.css">

<div class="container mt-5 d-flex">
    <!-- 월간 캘린더, 주간 캘린더의 헤더만 -->
    <div id="calendarEmployeeName" class="d-none"></div>
    <div>
        <div id="calendar"></div>
        <!-- 직원별 캘린더를 담을 컨테이너 -->
        <div id="calendarsContainer" class="d-none"></div>
    </div>
</div>

<!-- 일정 추가 모달 -->
<div class="modal fade" id="eventModal" tabindex="-1" role="dialog" aria-labelledby="eventModalLabel" aria-hidden="true">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="eventModalLabel">일정 추가</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <form id="eventForm">
                    <input type="hidden" name="scheduleId" id="scheduleId">
                    <div class="form-group">
                        <label for="employeeSelect">근무자 선택 :</label>
                        <select id="employeeSelect" name="employeeSelect" required class="form-control">
                            <c:forEach var="employee" items="${employees}">
                                <option value="${employee.id}">${employee.name}</option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="form-group">
                        <label for="eventDate">날짜:</label>
                        <input type="date" id="eventDate" name="eventDate" required class="form-control">
                    </div>
                    <div class="form-group">
                        <label for="eventStartTime">시작 시간:</label>
                        <select id="eventStartTime" name="eventStartTime" required class="form-control"></select>
                    </div>

                    <div class="form-group">
                        <div class="d-flex justify-content-between">
                            <label for="eventEndTime">종료 시간:</label>
                            <div class="form-group form-check">
                                <input type="checkbox" class="form-check-input" id="isNextDay" name="isNextDay">
                                <label class="form-check-label" for="isNextDay">다음 날 종료</label>
                            </div>
                        </div>
                        <select id="eventEndTime" name="eventEndTime" required class="form-control"></select>
                    </div>
                    <button type="submit" id="eventSubmitBtn" class="btn btn-primary">추가</button>
                </form>
            </div>
        </div>
    </div>
</div>

<div id="toastContainer" class="position-fixed bottom-0 right-0 p-3" style="z-index: 1060;">
    <!-- JavaScript를 통해 토스트가 여기에 동적으로 추가됩니다 -->
</div>

<%-- FullCalendar 4.4.2 JS (jsDelivr) --%>
<script src='https://cdn.jsdelivr.net/npm/@fullcalendar/core@4.4.2/main.min.js'></script>
<script src='https://cdn.jsdelivr.net/npm/@fullcalendar/interaction@4.4.2/main.min.js'></script>
<script src='https://cdn.jsdelivr.net/npm/@fullcalendar/daygrid@4.4.2/main.min.js'></script>
<script src='https://cdn.jsdelivr.net/npm/@fullcalendar/timegrid@4.4.2/main.min.js'></script>
<script src='https://cdn.jsdelivr.net/npm/@fullcalendar/bootstrap@4.4.2/main.min.js'></script>
<%-- scheduleModal.js 포함 --%>
<script src="/js/erp/hr/scheduleModal.js"></script>

<%-- calendar.js 포함 --%>
<script src="/js/erp/hr/calendar.js"></script>

<%-- 일정 데이터 초기화 --%>
<script type="application/javascript">
    const scheduleList = JSON.parse('${schedulesJson}');
    const employeeList = JSON.parse('${employeesJson}');
</script>

<%-- 캘린더 초기 랜더링 --%>
<script>
    document.addEventListener('DOMContentLoaded', function () {
        const calendarEl = document.getElementById('calendar');
        const calendarsContainer = document.getElementById('calendarsContainer');
        initializeMainCalendar(calendarEl, scheduleList);
        initializeEmployeeCalendars(calendarsContainer, scheduleList, employeeList);
    });
</script>
<!-- Toast 및 로딩 스피너를 위한 JavaScript 추가 -->
<script src="/js/toastHelper.js"></script>

<%@include file="/WEB-INF/view/erp/layout/erpFooter.jsp" %>
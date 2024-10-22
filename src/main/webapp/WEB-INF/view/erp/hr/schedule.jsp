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

<div class="container mt-5">
    <div id='calendar'></div>
</div>

<!-- 일정 추가 모달 -->
<div class="modal fade" id="addEventModal" tabindex="-1" role="dialog" aria-labelledby="addEventModalLabel" aria-hidden="true">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="addEventModalLabel">일정 추가</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <form id="addEventForm">
                    <div class="form-group">
                        <label for="employeeSelect">근무자 선택 :</label>
                        <select id="employeeSelect" name="employeeSelect" required class="form-control">
                            <c:forEach var="employee" items="${employees}">
                                <option value="${employee.id}">${employee.name}</option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="form-group">
                        <label for="eventStart">날짜:</label>
                        <input type="date" id="eventStart" name="eventStart" required class="form-control">
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
                    <button type="submit" class="btn btn-primary">추가</button>
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

<%--&lt;%&ndash; scheduleModal.js 포함 &ndash;%&gt;--%>
<%--<script src="/js/erp/hr/scheduleModal.js"></script>--%>

<%--&lt;%&ndash; calendarModule.js 포함 &ndash;%&gt;--%>
<%--<script src="/js/erp/hr/calendarModule.js"></script>--%>

<%--&lt;%&ndash; 일정 데이터 초기화 &ndash;%&gt;--%>
<%--<script type="application/javascript">--%>
<%--    const scheduleList = JSON.parse('${schedules}');--%>
<%--</script>--%>

<%--&lt;%&ndash; 캘린더 초기 랜더링 &ndash;%&gt;--%>
<%--<script>--%>
<%--    document.addEventListener('DOMContentLoaded', function () {--%>
<%--        const calendarEl = document.getElementById('calendar');--%>
<%--        initializeCalendar(calendarEl, scheduleList);--%>
<%--    });--%>
<%--</script>--%>
<%--<!-- Toast 및 로딩 스피너를 위한 JavaScript 추가 -->--%>
<%--<script src="/js/toastHelper.js"></script>--%>

<script>
    // jQuery가 로드된 후에 실행되도록 대기
    function onJQueryReady(callback) {
        if (window.jQuery && jQuery.fn.modal) {
            console.log("jQuery와 Bootstrap 모달이 준비되었습니다.");
            callback();
        } else {
            setTimeout(function() { onJQueryReady(callback); }, 100);
        }
    }

    onJQueryReady(function() {
        // FullCalendar 및 커스텀 스크립트 로드
        console.log("커스텀 스크립트 로드 시작");
        const scripts = [
            '/js/erp/hr/scheduleModal.js',
            '/js/erp/hr/calendarModule.js',
            '/js/toastHelper.js'
        ];

        function loadScripts(scripts, index) {
            if (index < scripts.length) {
                const script = document.createElement('script');
                script.src = scripts[index];
                script.onload = function() {
                    console.log(scripts[index] + " 로드 완료");
                    loadScripts(scripts, index + 1);
                };
                document.body.appendChild(script);
            } else {
                // 모든 스크립트가 로드된 후 초기화
                if (typeof initializeCalendar === 'function') {
                    const scheduleList = JSON.parse('${schedules}');
                    const calendarEl = document.getElementById('calendar');
                    console.log("캘린더 초기화 시작");
                    initializeCalendar(calendarEl, scheduleList);
                    console.log("캘린더 초기화 완료");
                } else {
                    console.error('initializeCalendar 함수가 정의되지 않았습니다.');
                }
            }
        }

        loadScripts(scripts, 0);
    });
</script>
<%@include file="/WEB-INF/view/erp/layout/erpFooter.jsp" %>
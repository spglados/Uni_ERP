/**
 * FullCalendar 4.4.2를 초기화하는 함수
 * @param {HTMLElement} element - FullCalendar가 렌더링될 DOM 요소
 * @param {Array} schedules - 초기 이벤트 목록
 */
function initializeCalendar(element, schedules) {
    if (typeof FullCalendar === 'undefined') {
        console.error('FullCalendar 라이브러리가 로드되지 않았습니다.');
        return;
    }
    // 클릭 타이머 변수와 클릭 간격 설정
    let clickTimer = null;
    const clickDelay = 300; // 밀리초

    const calendar = new FullCalendar.Calendar(element, {
        plugins: ['interaction', 'dayGrid', 'timeGrid', 'bootstrap'],
        header: {
            left: 'prevYear,prev,next,nextYear today addEventButton',
            center: 'title',
            right: 'dayGridMonth,timeGridWeek,timeGridDay'
        },
        eventLimit: true,
        nextDayThreshold: '06:00:00',
        buttonText: {
            today: '오늘',
            month: '월간',
            week: '주간',
            day: '일간'
        },
        editable: true,
        selectable: true,
        themeSystem: 'bootstrap',
        customButtons: {
            addEventButton: {
                text: '일정 추가',
                click: function () {
                    openEventModal('add');
                }
            }
        },
        dateClick: info => {
            if (clickTimer) {
                // 두 번째 클릭: 더블클릭으로 인식
                clearTimeout(clickTimer);
                clickTimer = null;
                handleDateDoubleClick(info);
            } else {
                // 첫 번째 클릭: 단일 클릭으로 인식하지 않음
                clickTimer = setTimeout(function () {
                    // 단일 클릭 시 아무런 동작도 하지 않음
                    clickTimer = null;
                }, clickDelay);
            }
        },
        eventClick: info => {
            openEventModal('edit', info.event);
        },
        eventDrop: info => {
            if (info.view.type === "dayGridMonth") {
                const startDate = info.event.start.toLocaleDateString('en-CA').substring(0, 10); // yyyy-MM-dd
                const endDate = info.event.end.toLocaleDateString('en-CA').substring(0, 10);
                const startTime = info.oldEvent.start.toTimeString().substring(0, 8); // HH:mm:ss
                const endTime = info.oldEvent.end.toTimeString().substring(0, 8);
                const start = startDate + "T" + startTime;
                const end = endDate + "T" + endTime;
                console.log('가공', start);
                console.log('가공', end);
                info.event.setStart(start);
                info.event.setEnd(end);
            }
            const id = info.event.id;
            const startDate = info.event.start.toLocaleDateString('en-CA').substring(0, 10); // yyyy-MM-dd
            const endDate = info.event.end.toLocaleDateString('en-CA').substring(0, 10); // yyyy-MM-dd
            const startTime = info.event.start.toTimeString().substring(0, 8); // HH:mm:ss
            const endTime = info.event.end.toTimeString().substring(0, 8);
            const start = startDate + "T" + startTime; // yyyy-MM-ddTHH:mm:ss
            console.log(start);
            const end = endDate + "T" + endTime;
            console.log(end);
            updateSchedule(id, start, end, calendar, info);
        },
        eventResize: info => {
            const id = info.event.id;
            const startDate = info.event.start.toLocaleDateString('en-CA').substring(0, 10); // yyyy-MM-dd
            const endDate = info.event.end.toLocaleDateString('en-CA').substring(0, 10); // yyyy-MM-dd
            const startTime = info.event.start.toTimeString().substring(0, 8); // HH:mm:ss
            const endTime = info.event.end.toTimeString().substring(0, 8);
            const start = startDate + "T" + startTime; // yyyy-MM-ddTHH:mm:ss
            console.log(start);
            const end = endDate + "T" + endTime;
            console.log(end);
            updateSchedule(id, start, end, calendar, info);
        },
        eventMouseEnter: info => {
            if (info.view.type === "dayGridMonth") {
                // Tooltip 내용 설정
                const startTime = info.event.start.toTimeString().substring(0,5); // HH:mm
                const endTime = info.event.end.toTimeString().substring(0,5); // HH:mm
                const tooltipContent = `
                <strong>${info.event.title}</strong><br/>
                시작 시간 : ${startTime}<br/>
                종료 시간 : ${endTime}`;

                // Bootstrap Tooltip 초기화
                $(info.el).tooltip({
                    title: tooltipContent,
                    html: true,
                    placement: 'top',
                    trigger: 'hover',
                    container: 'body'
                }).tooltip('show');
            }
        },
        eventMouseLeave: info => {
            $(info.el).tooltip('hide');
        },
        locale: 'ko',
        events: schedules
    });
    calendar.render();

    // 모달 폼의 일정 추가 버튼 이벤트 리스너 등록
    document.getElementById('eventForm').addEventListener('submit', function (e) {
        e.preventDefault();
        submitEvent(calendar);
    });

    // 더블클릭 처리 함수
    function handleDateDoubleClick(info) {
        console.log('더블클릭:', info.dateStr);
        calendar.changeView('timeGridDay', info.dateStr);
    }

    // 전역 객체로 접근할 수 있도록 설정 (필요 시)
    window.calendar = calendar;
}

// 전역 객체로 함수 노출
window.initializeCalendar = initializeCalendar;
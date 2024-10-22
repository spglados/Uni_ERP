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
    const calendar = new FullCalendar.Calendar(element, {
        plugins: ['interaction', 'dayGrid', 'timeGrid', 'bootstrap'],
        header: {
            left: 'prevYear,prev,next,nextYear today addEventButton',
            center: 'title',
            right: 'dayGridMonth,timeGridWeek,timeGridDay'
        },
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
                    openAddEventModal();
                }
            }
        },
        dateClick: function (info) {
            calendar.changeView('timeGridDay', info.dateStr);
        },
        locale: 'ko',
        events: schedules
    })
    calendar.render();

    // 모달 폼의 일정 추가 버튼 이벤트 리스너 등록
    document.getElementById('addEventForm').addEventListener('submit', function (e) {
        e.preventDefault();
        submitAddEvent(calendar);
    });

    // 전역 객체로 접근할 수 있도록 설정 (필요 시)
    window.calendar = calendar;
}
// 전역 객체로 함수 노출
window.initializeCalendar = initializeCalendar;
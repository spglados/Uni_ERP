import {Calendar} from 'https://cdn.skypack.dev/@fullcalendar/core@6.1.15'
import interactionPlugin from 'https://cdn.skypack.dev/@fullcalendar/interaction@6.1.15'
import dayGridPlugin from 'https://cdn.skypack.dev/@fullcalendar/daygrid@6.1.15'
import timeGridPlugin from 'https://cdn.skypack.dev/@fullcalendar/timegrid@6.1.15'
import bootstrapPlugin from 'https://cdn.skypack.dev/@fullcalendar/bootstrap@6.1.15'
import {openAddEventModal, submitAddEvent} from './scheduleModal.js';

export function initializeCalendar(element, schedules) {
    const calendar = new Calendar(element, {
        plugins: [interactionPlugin, dayGridPlugin, timeGridPlugin, bootstrapPlugin],
        headerToolbar: {
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
        dayMaxEventRows: 5,
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
        events: schedules
    })
    calendar.setOption('locale', 'ko');
    calendar.render();

    // 모달 폼의 일정 추가 버튼 이벤트 리스너 등록
    document.getElementById('addEventForm').addEventListener('submit', function (e) {
        e.preventDefault();
        submitAddEvent(calendar);
    });
}
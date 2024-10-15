import {Calendar} from 'https://cdn.skypack.dev/@fullcalendar/core@6.1.15'
import interactionPlugin from 'https://cdn.skypack.dev/@fullcalendar/interaction@6.1.15'
import dayGridPlugin from 'https://cdn.skypack.dev/@fullcalendar/daygrid@6.1.15'
import timeGridPlugin from 'https://cdn.skypack.dev/@fullcalendar/timegrid@6.1.15'
import bootstrapPlugin from 'https://cdn.skypack.dev/@fullcalendar/bootstrap@6.1.15'

export function initializeCalendar(element) {
    const calendar = new Calendar(element, {
        plugins: [interactionPlugin, dayGridPlugin, timeGridPlugin, bootstrapPlugin],
        headerToolbar: {
            left: 'prevYear,prev,next,nextYear today',
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
        events: [
            {title: 'Meeting', start: new Date()}
        ]
    })
    calendar.setOption('locale', 'ko');
    calendar.render()
}
const empNameSpace = '50px';
const empHeight = 50;

/**
 * FullCalendar 4.4.2를 초기화하는 함수
 * @param {HTMLElement} element - FullCalendar가 렌더링될 DOM 요소
 * @param {Array} schedules - 초기 이벤트 목록
 */
function initializeMainCalendar(element, schedules) {
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
            right: 'dayGridMonth,dayGridWeek,timeGridDay'
        },
        eventLimit: true,
        timeGridEventMinHeight: 10,
        nextDayThreshold: '06:00:00',
        buttonText: {
            day: '일간'
        },
        slotEventOverlap: false,
        editable: true,
        selectable: true,
        themeSystem: 'bootstrap',
        customButtons: {
            addEventButton: {
                text: '일정 추가',
                click: function () {
                    openEventModal('add');
                }
            },
            dayGridWeek: {
                text: '주간',
                click: () => {
                    calendar.setOption('height', 90);
                    calendar.changeView('dayGridWeek');
                    calendar.getEvents().forEach(event => {
                        event.remove();
                    });
                    $('#calendarsContainer').removeClass('d-none').addClass('d-block');
                    $('#calendarEmployeeName').removeClass('d-none').addClass('d-block');
                }
            },
            dayGridMonth: {
                text: '월간',
                click: () => {
                    calendar.setOption('height', 800);
                    schedules.forEach(event => {
                        calendar.addEvent(event);
                    })
                    console.log(schedules);
                    calendar.changeView('dayGridMonth');
                    $('#calendarsContainer').removeClass('d-block').addClass('d-none');
                    $('#calendarEmployeeName').removeClass('d-block').addClass('d-none');
                }
            },
            prev: {
                click: () => {
                    if (calendar.view.type === 'dayGridWeek') {
                        window.employeeCalendars.forEach(empCal => empCal.prev());
                    }
                    calendar.prev();
                }
            },
            next: {
                click: () => {
                    if (calendar.view.type === 'dayGridWeek') {
                        window.employeeCalendars.forEach(empCal => empCal.next());
                    }
                    calendar.next();
                }
            },
            today: {
                text: '오늘',
                click: () => {
                    if (calendar.view.type === 'dayGridWeek') {
                        window.employeeCalendars.forEach(empCal => empCal.today());
                    }
                    calendar.today();
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
            if (info.view.type === "dayGridWeek" || info.view.type === "dayGridMonth") {
                // Tooltip 내용 설정
                const startTime = info.event.start.toTimeString().substring(0, 5); // HH:mm
                const endTime = info.event.end.toTimeString().substring(0, 5); // HH:mm
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
        events: schedules,
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
    // 내비게이션 동기화를 위한 전역 배열
    window.employeeCalendars = [];

    // 메인 캘린더 내비게이션 버튼을 동기화
    setupNavigationSync(calendar);

}

/**
 * 직원별 캘린더를 초기화하는 함수
 * @param {HTMLElement} container - 캘린더들이 추가될 컨테이너 요소
 * @param {Array} schedules - 모든 일정 목록
 * @param {Array} employees - 직원 목록
 */
function initializeEmployeeCalendars(container, schedules, employees) {
    if (typeof FullCalendar === 'undefined') {
        console.error('FullCalendar 라이브러리가 로드되지 않았습니다.');
        return;
    }

    employees.forEach(employee => {
        // 직원별 캘린더를 담을 섹션 생성
        const employeeSection = document.createElement('div');
        employeeSection.classList.add('employee-calendar-section');

        // 직원 이름 표시 (왼쪽)
        const employeeName = document.createElement('div');
        employeeName.classList.add('employee-name');
        employeeName.textContent = `${employee.empName}`;
        employeeName.style.minWidth = empNameSpace;
        employeeName.style.minHeight = empHeight + "px";
        employeeName.style.overflowY = 'hidden';
        const employeeNameContainer = document.getElementById('calendarEmployeeName');
        employeeNameContainer.appendChild(employeeName);

        // 캘린더를 담을 div 생성
        const calendarEl = document.createElement('div');
        const calendarId = `employee-calendar-${employee.empId}`;
        calendarEl.id = calendarId;
        employeeSection.appendChild(calendarEl);

        // 컨테이너에 직원 섹션 추가
        container.appendChild(employeeSection);

        // 직원별 일정을 필터링
        const employeeSchedules = schedules.filter(schedule => schedule.extendedProps.empId === employee.empId);

        // 캘린더 초기화
        const employeeCalendar = new FullCalendar.Calendar(calendarEl, {
            plugins: ['interaction', 'dayGrid', 'timeGrid', 'bootstrap'],
            header: false, // 헤더 숨김
            defaultView: 'dayGridWeek',
            eventLimit: true,
            columnHeader: false, // 컬럼 헤더 없음
            height: empHeight, // 적절한 높이 설정
            nextDayThreshold: '06:00:00',
            selectable: true,
            themeSystem: 'bootstrap',
            dateClick: info => {
                openEventModal('add', employee.id);
            },
            eventClick: info => {
                openEventModal('edit', info.event, employee.id);
            },
            eventRender: info => {
                console.log('el', info.el);
                const startTime = info.event.start.toTimeString().substring(0, 5); // HH:MM
                const endTime = info.event.end.toTimeString().substring(0, 5); // HH:MM
                $(info.el).find('.fc-title').remove();
                $(info.el).find('.fc-time').text(`${startTime} - ${endTime}`);
                $(info.el).find('.fc-time').css('width', '100%');
            },
            locale: 'ko',
            events: employeeSchedules,
        });
        employeeCalendar.render();
        console.log('employeeCalendar.view.type', employeeCalendar.view.type);

        // 캘린더 내비게이션을 메인 캘린더와 동기화하기 위해 배열에 추가
        window.employeeCalendars.push(employeeCalendar);
    });
}

/**
 * 내비게이션 버튼 동기화 설정
 * @param {FullCalendar.Calendar} mainCalendar - 메인 캘린더 인스턴스
 */
function setupNavigationSync(mainCalendar) {
    // 메인 캘린더의 내비게이션 버튼을 식별
    const mainPrevBtn = document.querySelector('#calendar .fc-prev-button');
    const mainNextBtn = document.querySelector('#calendar .fc-next-button');
    const mainTodayBtn = document.querySelector('#calendar .fc-today-button');

    if (mainPrevBtn && mainNextBtn && mainTodayBtn) {
        mainPrevBtn.addEventListener('click', () => {
            window.employeeCalendars.forEach(empCal => empCal.prev());
        });
        mainNextBtn.addEventListener('click', () => {
            window.employeeCalendars.forEach(empCal => empCal.next());
        });
        mainTodayBtn.addEventListener('click', () => {
            window.employeeCalendars.forEach(empCal => empCal.today());
        });
    }
}

// 전역 객체로 함수 노출
window.initializeMainCalendar = initializeMainCalendar;
window.initializeEmployeeCalendars = initializeEmployeeCalendars;
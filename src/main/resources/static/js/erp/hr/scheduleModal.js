/**
 * 일정 추가 모달 오픈 이벤트
 */
function openAddEventModal() {
    setupTimeOptions();
    $('#addEventModal').modal('show');
}


/**
 * 모달 닫기 이벤트
 * 다시 열렸을때를 대비해 벨류 초기화 진행
 */
function closeAddEventModal() {
    $('#addEventModal').modal('hide');
    // 모달 필드 초기화
    $('#addEventForm')[0].reset();
}

/**
 * 일정 추가 버튼 입력시 호출 되는 이벤트
 * @param {FullCalendar.Calendar} calendar - FullCalendar 인스턴스 캘린더에 연동시키기 위해 받아온다
 */
function submitAddEvent(calendar) {
    // 근무자 관련 요소
    const employeeSelect = document.getElementById('employeeSelect');
    const title = employeeSelect.options[employeeSelect.selectedIndex].text; // 근무자 이름
    const empId = employeeSelect.value;

    // 시간 관련 요소
    const startDate = document.getElementById('eventStart').value;
    const startTime = document.getElementById('eventStartTime').value;
    const endTime = document.getElementById('eventEndTime').value;
    const isNextDay = document.getElementById('isNextDay').checked;

    if (!isValidTime(startTime) || !isValidTime(endTime)) {
        showToast('시간은 10분 단위로 입력해야 합니다.');
        return;
    }

    let endDate = new Date(startDate);
    if (isNextDay) {
        endDate = new Date(endDate.getTime() + 24 * 60 * 60 * 1000);
    }
    const start = startDate + 'T' + startTime + ":00";
    const end = endDate.toISOString().split('T')[0] + 'T' + endTime + ":00";

    if (!validateEventDates(start, end)) {
        showToast('날짜를 확인해주세요');
        return;
    }

    if (title && startDate && start && end) {
        createSchedule(title, empId, start, end, calendar);
        closeAddEventModal();
    } else {
        showToast('모든 필드를 채워주세요.');
    }
}

/**
 * 비동기로 일정 등록 처리 요청
 * 등록 성공시 캘린더에 동기화
 * @param title
 * @param empId
 * @param start
 * @param end
 * @param calendar
 */
function createSchedule(title, empId, start, end, calendar) {
    fetch("/erp/hr/schedule", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify({
            title: title,
            empId: empId,
            startTime: start,
            endTime: end,
        }),
    })
        .then(response => {
            if (response.status === 201) {
                return response.json();
            } else if (response.status === 400) {
                throw new Error('잘못된 요청입니다.');
            } else {
                throw new Error('알 수 없는 오류가 발생했습니다.');
            }
        })
        .then(data => {
            showToast("일정이 등록되었습니다.");
            console.log(data.schedule);
            calendar.addEvent(data.schedule);
        })
        .catch(error => {
            // 오류 발생 시 처리
            console.error('오류:', error.message);
            showToast(error.message);
        });
}

// 일정 날짜 유효성 검사
function validateEventDates(start, end) {
    const startDate = new Date(start);
    const endDate = new Date(end);
    return startDate < endDate;
}

// 시간 유효성 검사
function isValidTime(time) {
    const [hours, minutes] = time.split(':').map(Number);
    return minutes % 10 === 0; // 10분 단위 확인
}

// 모달 오픈시 필요한 값 설정
function setupTimeOptions() {
    const startTimeSelect = document.getElementById('eventStartTime');
    const endTimeSelect = document.getElementById('eventEndTime');

    if (startTimeSelect.options.length === 0) {
        addTimeOptions(startTimeSelect);
    }
    if (endTimeSelect.options.length === 0) {
        addTimeOptions(endTimeSelect);
    }
}

// 시간 선택 옵션 추가 함수 (10분 단위)
function addTimeOptions(timeSelect) {
    if (timeSelect.options.length > 0) return;
    for (let hour = 0; hour < 24; hour++) {
        for (let minute = 0; minute < 60; minute += 10) {
            const formattedHour = String(hour).padStart(2, '0');
            const formattedMinute = String(minute).padStart(2, '0');
            const time = `${formattedHour}:${formattedMinute}`;
            const option = document.createElement('option');
            option.value = time;
            option.textContent = time;
            timeSelect.appendChild(option);
        }
    }
}

// 전역 객체로 함수 노출
window.openAddEventModal = openAddEventModal;
window.submitAddEvent = submitAddEvent;
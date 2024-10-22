/**
 * 일정 추가 모달 오픈 이벤트
 */
function openEventModal(mode, event) {
    setupTimeOptions();
    // 일정 추가 모달일시
    if (mode === 'add') {
        // 출력 문구 수정
        document.getElementById('eventModalLabel').textContent = '일정 추가';
        document.getElementById('eventSubmitBtn').textContent = '추가';

        // 근무자 설정
        const employeeSelect = document.getElementById('employeeSelect');

        // 기존에 선택된 옵션의 selected 속성 해제
        const options = employeeSelect.options;
        for (let i = 0; i < options.length; i++) {
            options[i].selected = false; // 모든 옵션의 selected 속성 초기화
        }
        employeeSelect.disabled = false; // 변경 가능

        // 시간 설정
        document.getElementById('eventDate').value = '';
        document.getElementById('eventStartTime').value = '';
        document.getElementById('eventEndTime').value = '';
        document.getElementById('isNextDay').checked = false;
    }
    // 일정 수정 모달일시
    else if (mode === 'edit') {
        // 스케줄 아이디 지정
        document.getElementById("scheduleId").value = event.id;

        // 출력 문구 수정
        document.getElementById('eventModalLabel').textContent = '일정 수정';
        document.getElementById('eventSubmitBtn').textContent = '수정';

        // 근무자 설정
        const empId = event.extendedProps.empId; // 선택된 근무자의 ID
        const employeeSelect = document.getElementById('employeeSelect');

        // 기존에 선택된 옵션의 selected 속성 해제
        const options = employeeSelect.options;
        for (let i = 0; i < options.length; i++) {
            options[i].selected = false; // 모든 옵션의 selected 속성 초기화
        }

        // 해당 ID를 가진 option에 selected 속성 설정
        const selectedOption = employeeSelect.querySelector(`option[value="${empId}"]`);
        selectedOption.selected = true; // 선택된 근무자 설정
        employeeSelect.disabled = true; // 변경 불가

        // 시간 설정
        const startDate = event.start.toLocaleDateString('en-CA').substring(0, 10); // YYYY-MM-DD
        const startTime = event.start.toTimeString().substring(0, 5); // HH:MM
        const endDate = event.end.toLocaleDateString('en-CA').substring(0, 10);
        const endTime = event.end.toTimeString().substring(0, 5);

        document.getElementById('eventDate').value = startDate;
        document.getElementById('eventStartTime').value = startTime;
        document.getElementById('eventEndTime').value = endTime;

        // 다음 날인지 확인하여 체크박스 설정
        if (endDate && endDate !== startDate) {
            document.getElementById('isNextDay').checked = true; // 다음 날 체크
        } else {
            document.getElementById('isNextDay').checked = false; // 같은 날일 경우 체크 해제
        }
    }
    $('#eventModal').modal('show');
}

/**
 * 모달 닫기 이벤트
 * 다시 열렸을때를 대비해 벨류 초기화 진행
 */
function closeEventModal() {
    $('#eventModal').modal('hide');
    // 모달 필드 초기화
    $('#eventForm')[0].reset();
}

/**
 * 일정 추가 or 수정 버튼 입력시 호출 되는 이벤트
 * @param {FullCalendar.Calendar} calendar - FullCalendar 인스턴스 캘린더에 연동시키기 위해 받아온다
 */
function submitEvent(calendar) {
    // 근무자 관련 요소
    const employeeSelect = document.getElementById('employeeSelect');
    const title = employeeSelect.options[employeeSelect.selectedIndex].text; // 근무자 이름
    const empId = employeeSelect.value;

    // 시간 관련 요소
    const startDate = document.getElementById('eventDate').value;
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
    const end = endDate.toLocaleDateString('en-CA').split('T')[0] + 'T' + endTime + ":00";

    if (!validateEventDates(start, end)) {
        showToast('날짜를 확인해주세요');
        return;
    }

    // 추가, 수정 핸들링
    const btnText = document.getElementById('eventSubmitBtn').textContent.trim();

    if (title && startDate && start && end) {
        if (btnText === '추가') createSchedule(empId, start, end, calendar);
        else if (btnText === '수정') {
            const id = document.getElementById("scheduleId").value;
            updateSchedule(id, start, end, calendar);
        }
        closeEventModal();
    } else {
        showToast('모든 필드를 채워주세요.');
    }
}

/**
 * 비동기로 일정 등록 처리 요청
 * 등록 성공시 캘린더에 동기화
 * @param empId 근무자 pk
 * @param start 시작 시간
 * @param end 종료 시간
 * @param calendar 캘린더 동기화를 위해 받아옴
 */
function createSchedule(empId, start, end, calendar) {
    fetch("/erp/hr/schedule", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify({
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

/**
 * 비동기로 일정 수정 처리 요청
 * 수정 성공시 캘린더에 동기화
 * @param id 일정 pk
 * @param start 시작 시간
 * @param end 종료 시간
 * @param calendar 캘린더 동기화를 위해 받아옴
 */
function updateSchedule(id, start, end, calendar, info) {
    fetch("/erp/hr/schedule", {
        method: "PUT",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify({
            id: id,
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
            showToast("일정이 수정되었습니다.");
            console.log(data.schedule);
            const event = calendar.getEventById(data.schedule.id);
            event.setStart(data.schedule.start);
            event.setEnd(data.schedule.end);
        })
        .catch(error => {
            // 오류 발생 시 처리
            console.error('오류:', error.message);
            showToast(error.message);
            // 콜백 메서드의 경우 해당 이동 무효화
            if (info) info.revert();
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
window.openEventModal = openEventModal;
window.submitEvent = submitEvent;
// 일정 추가 모달 오픈
export function openAddEventModal() {
    const modal = document.getElementById('addEventModal');
    if (modal) {
        modal.style.display = 'block';
        setupEventListeners();
        setupElements();
    }
}

// 모달 닫기
export function closeAddEventModal() {
    const modal = document.getElementById('addEventModal');
    if (modal) {
        modal.style.display = 'none';
        // 모달 필드 초기화
        document.getElementById('eventTitle').value = '';
        document.getElementById('eventStart').value = '';
        document.getElementById('eventStartTime').value = '';
        document.getElementById('eventEndTime').value = '';
    }
}

// 일정 추가 제출
export function submitAddEvent(calendar) {
    const title = document.getElementById('eventTitle').value;
    const startDate = document.getElementById('eventStart').value;
    const startTime = document.getElementById('eventStartTime').value;
    const endTime = document.getElementById('eventEndTime').value;
    const isNextDay = document.getElementById('isNextDay').checked;
    console.log('isNextDay', isNextDay);
    if (!isValidTime(startTime) || !isValidTime(endTime)) {
        alert('시간은 10분 단위로 입력해야 합니다.');
        return;
    }

    let endDate = new Date(startDate);
    if (isNextDay) {
        endDate = new Date(endDate.getTime() + 24 * 60 * 60 * 1000);
    }
    const start = startDate + 'T' + startTime;
    const end =  endDate.toISOString().split('T')[0] + 'T' + endTime;

    if (!validateEventDates(start, end)) {
        alert('날짜를 확인해주세요');
        return;
    }

    if (title && startDate && start && end) {
        calendar.addEvent({
            title: title,
            start: start,
            end: end,
            allDay: false
        });
        closeAddEventModal();
    } else {
        alert('모든 필드를 채워주세요.');
    }
}

// 일정 날짜 유효성 검사
function validateEventDates(start, end) {
    const startDate = new Date(start);
    const endDate = new Date(end);
    return  startDate < endDate;
}

// 시간 유효성 검사
function isValidTime(time) {
    const [hours, minutes] = time.split(':').map(Number);
    return minutes % 10 === 0; // 10분 단위 확인
}

// 시간 선택 옵션 추가 함수 (10분 단위)
function addTimeOptions(timeSelect) {
    const times = [];
    for (let hour = 0; hour < 24; hour++) {
        for (let minute = 0; minute < 60; minute += 10) {
            const formattedHour = String(hour).padStart(2, '0');
            const formattedMinute = String(minute).padStart(2, '0');
            times.push(`${formattedHour}:${formattedMinute}`);
        }
    }
    times.forEach(time => {
        const option = document.createElement('option');
        option.value = time;
        option.textContent = time;
        timeSelect.appendChild(option);
    });
}

// 이벤트 리스너 설정
function setupEventListeners() {
    const closeModalButton = document.getElementById('closeAddEventButton');

    if (closeModalButton) {
        closeModalButton.addEventListener('click', closeAddEventModal);
    }
}

// 모달 오픈시 필요한 값 설정
function setupElements() {
    const startTimeInput = document.getElementById('eventStartTime');
    addTimeOptions(startTimeInput);
    const endTimeInput = document.getElementById('eventEndTime');
    addTimeOptions(endTimeInput);
}
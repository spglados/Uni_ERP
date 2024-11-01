<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!-- 모달 버튼 (테스트용) -->
<button type="button" class="btn btn-primary" data-toggle="modal" data-target="#clockInOutModal">
    출퇴근 모달 열기
</button>

<!-- 모달 구조 -->
<div class="modal fade" id="clockInOutModal" tabindex="-1" role="dialog" aria-labelledby="clockInOutModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-lg" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="clockInOutModalLabel">출퇴근 관리</h5>
                <div class="current-time ml-auto" id="currentTime" style="font-size: 1rem;"></div>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <div class="container-fluid">
                    <div class="row">
                        <!-- 왼쪽: 입력 필드 영역 -->
                        <div class="col-md-6">
                            <form>
                                <div class="form-group">
                                    <label for="employeeNumber">사번</label>
                                    <div class="input-group">
                                        <input type="text" class="form-control" id="employeeNumber" placeholder="사번 입력">
                                        <div class="input-group-append">
                                            <button type="button" class="btn btn-secondary" onclick="fetchEmployeeInfo()">조회</button>
                                        </div>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label for="employeeName">이름</label>
                                    <input type="text" class="form-control" id="employeeName" placeholder="자동 입력" disabled>
                                </div>
                                <div class="form-group">
                                    <label for="password">비밀번호</label>
                                    <input type="password" class="form-control" id="password" placeholder="비밀번호 입력">
                                </div>
                            </form>
                        </div>
                        <!-- 오른쪽: 숫자 패드 영역 -->
                        <div class="col-md-6">
                            <div class="number-pad">
                                <div class="row">
                                    <button class="btn btn-secondary col-4" onclick="addNumber(1)">1</button>
                                    <button class="btn btn-secondary col-4" onclick="addNumber(2)">2</button>
                                    <button class="btn btn-secondary col-4" onclick="addNumber(3)">3</button>
                                </div>
                                <div class="row mt-2">
                                    <button class="btn btn-secondary col-4" onclick="addNumber(4)">4</button>
                                    <button class="btn btn-secondary col-4" onclick="addNumber(5)">5</button>
                                    <button class="btn btn-secondary col-4" onclick="addNumber(6)">6</button>
                                </div>
                                <div class="row mt-2">
                                    <button class="btn btn-secondary col-4" onclick="addNumber(7)">7</button>
                                    <button class="btn btn-secondary col-4" onclick="addNumber(8)">8</button>
                                    <button class="btn btn-secondary col-4" onclick="addNumber(9)">9</button>
                                </div>
                                <div class="row mt-2">
                                    <button class="btn btn-secondary col-4" onclick="deleteNumber()">←</button>
                                    <button class="btn btn-secondary col-4" onclick="addNumber(0)">0</button>
                                    <button class="btn btn-secondary col-4" onclick="clearNumber()">C</button>
                                </div>
                            </div>
                        </div>
                    </div>
                    <!-- 아래: 근무 목록, 출퇴근 시간, 버튼 -->
                    <div class="row mt-4">
                        <div class="col-12">
                            <div class="form-group">
                                <label for="attendanceList">근무 목록</label>
                                <select id="attendanceList" class="form-control mb-3" onchange="handleAttendanceChange()">
                                    <!-- 근무 항목이 동적으로 추가됩니다 -->
                                </select>
                            </div>
                            <div class="form-group">
                                <label for="scheduledTime">계획된 근무 시간</label>
                                <input type="text" class="form-control" id="scheduledTime" placeholder="자동 입력" disabled>
                            </div>
                            <div class="row">
                                <div class="col-md-4">
                                    <div class="form-group">
                                        <label for="clockInTime">출근 시간</label>
                                        <input type="text" class="form-control" id="clockInTime" placeholder="자동 입력" disabled>
                                    </div>
                                </div>
                                <div class="col-md-4">
                                    <div class="form-group">
                                        <label for="clockOutTime">퇴근 시간</label>
                                        <input type="text" class="form-control" id="clockOutTime" placeholder="자동 입력" disabled>
                                    </div>
                                </div>
                                <div class="col-md-4 d-flex align-items-end">
                                    <button type="button" class="btn btn-primary btn-block" id="clockInButton" style="display: none;" onclick="handleClockIn()">출근</button>
                                    <button type="button" class="btn btn-primary btn-block" id="clockOutButton" style="display: none;" onclick="handleClockOut()">퇴근</button>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<div id="toastContainer" class="position-fixed bottom-0 right-0 p-3" style="z-index: 1060;">
    <!-- JavaScript를 통해 토스트가 여기에 동적으로 추가됩니다 -->
</div>
<!-- JavaScript -->
<script>
    // 엘리먼트 초기화
    const passwordElement = document.getElementById('password');
    let attendancesData = [];
    // 현재 활성화된 입력 필드를 추적
    let activeInputField = null;

    // 숫자 버튼 클릭 시 숫자 추가
    function addNumber(num) {
        if (activeInputField) {
            activeInputField.value += num;
        }
    }

    // ← 버튼 클릭 시 숫자 삭제
    function deleteNumber() {
        if (activeInputField) {
            activeInputField.value = activeInputField.value.slice(0, -1);
        }
    }

    // C 버튼 클릭 시 숫자 초기화
    function clearNumber() {
        if (activeInputField) {
            activeInputField.value = '';
        }
    }

    // 입력 필드에 포커스가 가면 해당 필드를 활성화
    document.getElementById('employeeNumber').addEventListener('focus', function () {
        activeInputField = this;
    });
    passwordElement.addEventListener('focus', function () {
        activeInputField = this;
    });

    // 모달이 열릴 때 초기화
    $('#clockInOutModal').on('shown.bs.modal', function () {
        resetModalFields();
    });

    // 조회 버튼 클릭 시 사번으로 직원 정보 조회
    function fetchEmployeeInfo() {
        const employeeNumber = document.getElementById('employeeNumber').value;

        // 사번이 입력되었는지 확인
        if (!employeeNumber) {
            alert('사번을 입력하세요.');
            return;
        }
        fetch("/erp/hr/attendance/" + employeeNumber, {
            method: "GET",
        })
            .then(response => {
                if (response.status === 200) {
                    return response.json();
                } else {
                    return response.json().then(errorData => {
                        throw new Error(errorData.message);
                    });
                }
            })
            .then(response => {
                attendancesData = response.dataList;
                console.log('attendancesData', attendancesData);
                const data = attendancesData[0];
                if (data.status === "UNPLANNED") {
                    attendancesData = [];
                    displayAttendanceOptions(attendancesData);
                    showToast("계획된 근무가 없습니다.");
                    document.getElementById('employeeName').value = data.name;
                    document.getElementById('clockInButton').style.display = 'flex';
                    document.getElementById('scheduledTime').value = '계획된 근무가 없습니다.';
                    return;
                }
                showToast("조회되었습니다.");
                displayAttendanceOptions(attendancesData);
            })
            .catch(error => {
                // 오류 발생 시 처리
                resetModalFields();
                console.error('오류:', error.message);
                showToast(error.message);
            });

    }

    // 근무가 2개 이상일 경우 select 옵션으로 적용
    function displayAttendanceOptions(attendances) {
        console.log('attendances', attendances);
        const attendanceList = document.getElementById('attendanceList');
        attendanceList.innerHTML = ''; // 기존 리스트 초기화

        // 기본 옵션 추가
        const defaultOption = document.createElement('option');
        defaultOption.textContent = '근무를 선택하세요';
        defaultOption.value = '';
        defaultOption.disabled = true;
        defaultOption.selected = true;
        attendanceList.appendChild(defaultOption);

        // 근무 항목을 select 옵션으로 추가
        let index = 0;
        attendances.forEach((attendance, curIndex) => {
            const option = document.createElement('option');
            if (!attendance.start || !attendance.end) {
                option.textContent = "근무 " + (curIndex + 1) + ": " + "예정에 없는 근무";
            } else {
                option.textContent = "근무 " + (curIndex + 1) + ": " + attendance.start + " - " + attendance.end;
            }
            if (attendance.leaveTime) {
                option.textContent += ' (완료)';
            } else if (attendance.attendanceTime) {
                option.textContent += ' (근무중)';
            }
            option.value = curIndex; // 인덱스를 값으로 사용
            option.setAttribute('data-id', attendance.id);
            // curIndex가 0일 경우 해당 옵션을 선택된 상태로 설정
            if (curIndex === 0) {
                option.selected = true;
            }
            index = curIndex + 1;
            console.log('index', index);
            attendanceList.appendChild(option);
        });
        // 반복문이 끝난 후 "예정에 없는 근무" 항목 추가
        const additionalOption = document.createElement('option');
        additionalOption.textContent = "근무 " + (index + 1) + ": " + "예정에 없는 근무";
        additionalOption.value = index; // 고유한 값을 설정
        attendanceList.appendChild(additionalOption);
        if (attendances.length === 0) {
            additionalOption.selected = true;
        }
        const additionalAttendance = {
            start: null,
            end: null,
            attendanceTime: null,
            leaveTime: null
        };
        attendances.push(additionalAttendance);
        // 기본적으로 첫 번째 근무를 선택
        if (attendances.length > 0) {
            selectAttendance(attendances[0]);
        }
    }

    // select 변경 시 선택한 근무에 따라 업데이트
    function handleAttendanceChange() {
        const attendanceList = document.getElementById('attendanceList');
        const selectedIndex = attendanceList.value;
        console.log('selectedIndex', selectedIndex);
        // 유효한 선택이었는지 확인
        if (selectedIndex !== '') {
            const selectedAttendance = attendancesData[selectedIndex]; // 이전에 불러온 데이터에서 가져옴
            selectAttendance(selectedAttendance);
        }
    }

    // 근무 선택시 값 변경
    function selectAttendance(attendance) {
        console.log('attendance', attendance);
        if (attendance.name) {
            document.getElementById('employeeName').value = attendance.name;
        }
        if (!attendance.status || attendance.status === 'NOT_EXECUTED') {
            // 출근하지 않은 상태라면 출근 버튼만 표시
            document.getElementById('clockInButton').style.display = 'flex';
            document.getElementById('clockOutButton').style.display = 'none';
        } else if (attendance.status === 'WORKING') {
            // 이미 출근한 상태라면 퇴근 버튼만 표시
            // 단, 출근 한지 30분이 지나지 않았다면 퇴근 버튼 활성화 되지않음
            const now = new Date();
            console.log('now', now);
            const [hours, minutes] = attendance.attendanceTime.split(":").map(Number);
            const nowHour = now.getHours();
            const nowMinute = now.getMinutes();
            let compareHour = hours;
            let compareMinute = minutes + 30;
            if (compareMinute >= 60) {
                compareHour += 1;
                compareMinute -= 60;
            }
            // 자정 넘어가는 경우 처리
            if (nowHour < hours || (nowHour === hours && nowMinute < minutes)) {
                // 자정 넘김을 의미 -> 출근 시간에 24시간을 더해 비교
                compareHour -= 24;
            }
            // 현재 시각을 24시간 형식으로 변환
            const currentTotalMinutes = nowHour * 60 + nowMinute;
            const compareTotalMinutes = compareHour * 60 + compareMinute;
            console.log(now, nowHour, nowMinute, compareHour, compareMinute, currentTotalMinutes, compareTotalMinutes);
            if (currentTotalMinutes >= compareTotalMinutes) {
                document.getElementById('clockOutButton').style.display = 'flex';
            } else {
                document.getElementById('clockOutButton').style.display = 'none';
            }
            document.getElementById('clockInButton').style.display = 'none';
        } else {
            // 이외 모든 상황에서 버튼 없앰
            document.getElementById('clockInButton').style.display = 'none';
            document.getElementById('clockOutButton').style.display = 'none';
        }
        if (attendance.start && attendance.end) {
            document.getElementById('scheduledTime').value = attendance.start + ' - ' + attendance.end;
        } else {
            document.getElementById('scheduledTime').value = "예정에 없는 근무";
        }
        document.getElementById('clockInTime').value = attendance.attendanceTime;
        document.getElementById('clockOutTime').value = attendance.leaveTime;
    }

    // 모든 필드를 초기화하는 함수
    function resetModalFields() {
        document.getElementById('employeeNumber').value = '';
        document.getElementById('employeeName').value = '';
        document.getElementById('password').value = '';
        document.getElementById('clockInTime').value = '';
        document.getElementById('clockOutTime').value = '';
        document.getElementById('scheduledTime').value = '';
        document.getElementById('attendanceList').innerHTML = ''; // 근무 목록 초기화

        // 출근/퇴근 버튼 숨기기
        document.getElementById('clockInButton').style.display = 'none';
        document.getElementById('clockOutButton').style.display = 'none';

        // 활성화된 입력 필드를 사번 입력 필드로 설정
        activeInputField = document.getElementById('employeeNumber');
    }

    // 현재 시간을 표시하는 함수
    function updateTime() {
        const now = new Date();
        const hours = String(now.getHours()).padStart(2, '0');
        const minutes = String(now.getMinutes()).padStart(2, '0');
        const seconds = String(now.getSeconds()).padStart(2, '0');
        const formattedTime = hours + ":" + minutes + ":" + seconds;
        document.getElementById('currentTime').textContent = formattedTime;
    }

    // 매초마다 시간을 업데이트
    setInterval(updateTime, 1000);
    // 페이지 로드 시 시간을 즉시 표시
    updateTime();

    // 출근 버튼 클릭 시
    function handleClockIn() {
        const employeeNumber = document.getElementById('employeeNumber').value;
        const password = passwordElement.value;
        const type = "attendance";
        const attendanceId = getSelectedAttendanceId();

        // 유효성 검사
        if (!validateForm(employeeNumber, password)) {
            return;
        }

        // 출근 요청 전송
        sendAttendanceRequest(employeeNumber, password, type, attendanceId);
    }

    // 퇴근 버튼 클릭 시
    function handleClockOut() {
        const employeeNumber = document.getElementById('employeeNumber').value;
        const password = passwordElement.value;
        const type = 'leave'; // 퇴근 타입 설정
        const attendanceId = getSelectedAttendanceId(); // 선택된 근무의 id 가져오기

        // 유효성 검사
        if (!validateForm(employeeNumber, password)) {
            return;
        }

        // 퇴근 요청 전송
        sendAttendanceRequest(employeeNumber, password, type, attendanceId);
    }

    // 선택된 근무의 id 가져오는 함수
    function getSelectedAttendanceId() {
        const attendanceList = document.getElementById('attendanceList');
        const selectedOption = attendanceList.options[attendanceList.selectedIndex];
        const attendanceId = selectedOption.getAttribute('data-id');

        // data-id가 없거나 빈 문자열일 경우 null 반환
        return attendanceId ? attendanceId : null;
    }

    // 유효성 검사 함수
    function validateForm(employeeNumber, password) {
        if (!employeeNumber) {
            showToast('사번을 입력하세요.');
            activeInputField = document.getElementById('employeeNumber');
            document.getElementById('employeeNumber').focus();
            return false;
        }
        if (!password) {
            showToast('비밀번호를 입력하세요.');
            activeInputField = passwordElement;
            passwordElement.focus();
            return false;
        }
        return true;
    }

    // 출근/퇴근 요청 전송 함수
    function sendAttendanceRequest(employeeNumber, password, type, attendanceId) {
        fetch("/erp/hr/attendance/" + employeeNumber, {
            method: "PUT",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                password: password,
                type: type, // 출근/퇴근 타입을 body에 포함
                id: attendanceId // 선택된 근무의 id를 body에 포함
            })
        })
            .then(response => {
                if (response.status === 200) {
                    return response.json();
                } else if (response.status === 401) {
                    return response.json().then(errorData => {
                        passwordElement.value = '';
                        passwordElement.focus();
                        showToast(errorData.message);
                    });
                } else {
                    return response.json().then(errorData => {
                        throw new Error(errorData.message);
                    });
                }
            })
            .then(response => {
                attendancesData = response.dataList;
                const data = attendancesData[0];
                showToast("조회되었습니다.");
                displayAttendanceOptions(attendancesData);
                selectAttendance(data);
            })
            .catch(error => {
                resetModalFields();
                console.error('오류:', error.message);
                showToast(error.message);
            });
    }
</script>
<!-- Toast 및 로딩 스피너를 위한 JavaScript 추가 -->
<script src="/js/toastHelper.js"></script>

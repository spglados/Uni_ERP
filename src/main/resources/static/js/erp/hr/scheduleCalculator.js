document.addEventListener('DOMContentLoaded', () => {
    const calculateBtn = document.getElementById('calculateBtn');
    const confirmBtn = document.getElementById('confirmBtn');
    const refreshBtn = document.getElementById('refreshBtn');
    const resultsSection = document.getElementById('resultsSection');
    const employeeTabs = document.getElementById('employeeTabs');
    const employeeTabContent = document.getElementById('employeeTabContent');
    const includeHolidayWorkCheckbox = document.getElementById('includeHolidayWork');
    const includeSundayWorkContainer = document.getElementById('includeSundayWorkContainer');
    const includeSundayWorkCheckbox = document.getElementById('includeSundayWork');
    const includeWeeklyHolidayCheckbox = document.getElementById('includeWeeklyHoliday');
    const selectAllEmployeesCheckbox = document.getElementById('selectAllEmployees');

    // 일요일 수당 표시 제어
    includeHolidayWorkCheckbox.addEventListener('change', () => {
        if (includeHolidayWorkCheckbox.checked) {
            includeSundayWorkContainer.style.display = 'block';
        } else {
            includeSundayWorkContainer.style.display = 'none';
            includeSundayWorkCheckbox.checked = false;
        }
    });

    // 초기 로드 시 일요일 수당 옵션 상태 설정
    if (includeHolidayWorkCheckbox.checked) {
        includeSundayWorkContainer.style.display = 'block';
    } else {
        includeSundayWorkContainer.style.display = 'none';
    }

    // '전체 선택' 체크박스 이벤트 리스너
    selectAllEmployeesCheckbox.addEventListener('change', () => {
        const isChecked = selectAllEmployeesCheckbox.checked;
        document.querySelectorAll('input[name="empNos"]').forEach(cb => {
            cb.checked = isChecked;
        });
    });

    calculateBtn.addEventListener('click', async () => {
        // 선택된 Employee 번호 수집
        const empNos = Array.from(document.querySelectorAll('input[name="empNos"]:checked'))
            .map(cb => cb.value);

        if (empNos.length === 0) {
            alert('적어도 하나의 Employee를 선택해주세요.');
            return;
        }

        // 수당 포함 옵션 수집
        const includeOvertime = document.getElementById('includeOvertime').checked;
        const includeHolidayWork = includeHolidayWorkCheckbox.checked;
        const includeNightWork = document.getElementById('includeNightWork').checked;
        const includeSundayWork = includeSundayWorkCheckbox.checked;
        const includeWeeklyHoliday = includeWeeklyHolidayCheckbox.checked;

        // GET 요청 URL 구성
        const params = new URLSearchParams();
        empNos.forEach(empNo => params.append('empNos', empNo));
        params.append('includeOvertime', includeOvertime);
        params.append('includeHolidayWork', includeHolidayWork);
        params.append('includeNightWork', includeNightWork);
        params.append('includeSundayWork', includeSundayWork);
        params.append('includeWeeklyHoliday', includeWeeklyHoliday);

        const url = `/erp/hr/salaries-calculator/employee?${params.toString()}`;

        try {
            const response = await fetch(url, {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json'
                }
            });

            if (!response.ok) {
                const errorText = await response.text();
                throw new Error(errorText || '서버 오류 발생');
            }

            const data = await response.json();
            console.log('data', data);

            // 데이터가 리스트인지 확인
            if (!Array.isArray(data)) {
                throw new Error('서버에서 예상치 못한 데이터 형식을 반환했습니다.');
            }

            // 기존 탭과 탭 콘텐츠 초기화
            employeeTabs.innerHTML = '';
            employeeTabContent.innerHTML = '';

            // 선택되지 않은 직원 숨기기
            document.querySelectorAll('input[name="empNos"]').forEach(cb => {
                if (!cb.checked) {
                    cb.closest('.col-md-4').style.display = 'none';
                }
            });

            // 각 Employee의 PayrollResult를 순회하며 탭과 탭 콘텐츠 생성
            data.forEach((result, index) => {
                // 탭 생성
                const tabItem = document.createElement('li');
                tabItem.classList.add('nav-item');

                const tabLink = document.createElement('a');
                tabLink.classList.add('nav-link');
                if (index === 0) tabLink.classList.add('active');
                tabLink.id = `tab-${result.empNo}`;
                tabLink.setAttribute('data-toggle', 'tab');
                tabLink.href = `#content-${result.empNo}`;
                tabLink.role = 'tab';
                tabLink.setAttribute('aria-controls', `content-${result.empNo}`);
                tabLink.setAttribute('aria-selected', index === 0 ? 'true' : 'false');
                tabLink.textContent = `${result.name} (사번: ${result.empNo})`;

                tabItem.appendChild(tabLink);
                employeeTabs.appendChild(tabItem);

                // 탭 콘텐츠 생성
                const tabPane = document.createElement('div');
                tabPane.classList.add('tab-pane', 'fade');
                if (index === 0) tabPane.classList.add('show', 'active');
                tabPane.id = `content-${result.empNo}`;
                tabPane.role = 'tabpanel';
                tabPane.setAttribute('aria-labelledby', `tab-${result.empNo}`);

                // 급여 정보 테이블 생성
                const table = document.createElement('table');
                table.classList.add('table', 'table-bordered', 'mt-3');

                const tbody = document.createElement('tbody');

                const fields = [
                    {label: '총 합계 급여', value: result.grossSalary + "원"},
                    {label: '기본 급여', value: result.workSalary + "원"},
                    {label: '초과 근무 수당', value: result.overWorkAllowance + "원"},
                    {label: '휴일 근무 수당', value: result.holidayWorkAllowance + "원"},
                    {label: '야간 근무 수당', value: result.nightWorkAllowance + "원"},
                    {label: '주휴 수당', value: result.weeklyHolidayAllowance + "원"},
                    {label: '총 근무 시간', value: result.totalWorkTime + "시간"},
                    {label: '국민연금 (업주)', value: `${result.nationalPension}원 (${result.nationalPension}원)`},
                    {label: '건강보험 (업주)', value: `${result.healthInsurance}원 (${result.healthInsurance}원)`},
                    {label: '고용보험 (업주)', value: `${result.employmentInsurance}원 (${result.employmentInsuranceEmployer}원)`},
                    {label: '산재보험-업주', value: `${result.industrialAccidentCompensationInsurance}원`}
                ];

                fields.forEach(field => {
                    const row = document.createElement('tr');

                    const labelCell = document.createElement('th');
                    labelCell.scope = 'row';
                    labelCell.textContent = field.label;
                    row.appendChild(labelCell);

                    const valueCell = document.createElement('td');
                    valueCell.textContent = field.value;
                    row.appendChild(valueCell);

                    tbody.appendChild(row);
                });

                table.appendChild(tbody);
                tabPane.appendChild(table);
                employeeTabContent.appendChild(tabPane);
            });

            // 결과 섹션 표시
            resultsSection.style.display = 'block';

            // 확정 버튼 활성화
            confirmBtn.disabled = false;
        } catch (error) {
            console.error('Error:', error);
            alert(`급여 계산 중 오류가 발생했습니다: ${error.message}`);
        }
    });

    confirmBtn.addEventListener('click', async () => {
        // 선택된 Employee 번호 수집
        const empNos = Array.from(document.querySelectorAll('input[name="empNos"]:checked'))
            .map(cb => cb.value);

        if (empNos.length === 0) {
            alert('적어도 하나의 Employee를 선택해주세요.');
            return;
        }

        // 수당 포함 옵션 수집
        const includeOvertime = document.getElementById('includeOvertime').checked;
        const includeHolidayWork = includeHolidayWorkCheckbox.checked;
        const includeNightWork = document.getElementById('includeNightWork').checked;
        const includeSundayWork = includeSundayWorkCheckbox.checked;
        const includeWeeklyHoliday = includeWeeklyHolidayCheckbox.checked;

        // GET 요청 URL 구성 (저장용)
        const params = new URLSearchParams();
        empNos.forEach(empNo => params.append('empNos', empNo));
        params.append('includeOvertime', includeOvertime);
        params.append('includeHolidayWork', includeHolidayWork);
        params.append('includeNightWork', includeNightWork);
        params.append('includeSundayWork', includeSundayWork);
        params.append('includeWeeklyHoliday', includeWeeklyHoliday);

        const url = `/erp/hr/salaries-calculator/save?${params.toString()}`;

        try {
            const response = await fetch(url, {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json'
                }
            });

            if (!response.ok) {
                const errorText = await response.text();
                throw new Error(errorText || '서버 오류 발생');
            }

            const data = await response.json();

            if (data.success) {
                alert('급여 데이터가 성공적으로 저장되었습니다.');
                // 결과 섹션 숨기기 및 버튼 초기화
                resultsSection.style.display = 'none';
                confirmBtn.disabled = true;
                // 체크박스 초기화 및 선택되지 않은 항목 다시 표시
                document.querySelectorAll('input[name="empNos"]').forEach(cb => {
                    cb.checked = false;
                    cb.closest('.col-md-4').style.display = 'block'; // col-md-4
                });
                // 수당 포함 옵션 초기화
                document.getElementById('includeOvertime').checked = true;
                includeHolidayWorkCheckbox.checked = true;
                document.getElementById('includeNightWork').checked = true;
                includeSundayWorkCheckbox.checked = false;
                includeWeeklyHolidayCheckbox.checked = true;
                includeSundayWorkContainer.style.display = 'block'; // "휴일 근무 수당 포함"이 체크된 상태이므로 "일요일 수당 포함"을 표시
            } else {
                alert('급여 데이터 저장에 실패했습니다.');
            }
        } catch (error) {
            console.error('Error:', error);
            alert(`급여 저장 중 오류가 발생했습니다: ${error.message}`);
        }
    });

    refreshBtn.addEventListener('click', () => {
        // 폼 초기화
        document.getElementById('payrollForm').reset();

        // 일요일 수당 옵션 숨기기
        includeSundayWorkContainer.style.display = 'none';
        includeSundayWorkCheckbox.checked = false;

        // 직원 선택 섹션 다시 표시
        document.querySelectorAll('input[name="empNos"]').forEach(cb => {
            cb.closest('.col-md-4').style.display = 'block'; // col-md-4
        });

        // 결과 섹션 숨기기 및 버튼 비활성화
        resultsSection.style.display = 'none';
        confirmBtn.disabled = true;

        // 탭과 탭 콘텐츠 초기화
        employeeTabs.innerHTML = '';
        employeeTabContent.innerHTML = '';
    });
});

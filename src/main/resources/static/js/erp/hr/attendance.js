// datetime-local cell editor 구현
class DateTimePickerCellEditor {
    // Cell editor 초기화
    init(params) {
        this.eInput = document.createElement("input");
        this.eInput.type = "datetime-local";
        this.eInput.value = params.value ? this.formatDateToLocal(params.value) : "";

        // 초기 포커스 설정
        this.eInput.focus();
    }

    // 렌더링할 HTML 요소 반환
    getGui() {
        return this.eInput;
    }

    // 입력된 값 반환
    getValue() {
        return this.eInput.value ? new Date(this.eInput.value).toISOString() : null;
    }

    // datetime-local 형식으로 변환
    formatDateToLocal(dateString) {
        const date = new Date(dateString);
        const year = date.getFullYear();
        const month = (date.getMonth() + 1).toString().padStart(2, '0');
        const day = date.getDate().toString().padStart(2, '0');
        const hours = date.getHours().toString().padStart(2, '0');
        const minutes = date.getMinutes().toString().padStart(2, '0');

        return year + '-' + month + '-' + day + 'T' + hours + ':' + minutes;
    }
}

// Sample column and row data
const columnDefs = [
    {field: "empNo", headerName: "사번", editable: false, minWidth: 80 },
    {field: "name", headerName: "이름", editable: false, minWidth: 80 },
    {field: "date", headerName: "날짜", editable: true, minWidth: 120 },
    {
        field: "attendanceTime", headerName: "출근", editable: true, minWidth: 150,
        cellEditor: DateTimePickerCellEditor,
        valueFormatter: (params) => {
            const date = new Date(params.value);
            const hours = date.getHours().toString().padStart(2, '0');
            const minutes = date.getMinutes().toString().padStart(2, '0');
            return hours + ':' + minutes;
        }
    },
    {
        field: "leaveTime", headerName: "퇴근", editable: true, minWidth: 150,
        cellEditor: DateTimePickerCellEditor,
        valueFormatter: (params) => {
            const date = new Date(params.value);
            const hours = date.getHours().toString().padStart(2, '0');
            const minutes = date.getMinutes().toString().padStart(2, '0');
            return hours + ':' + minutes;
        }
    },
    {
        field: "breakTime", headerName: "휴식", editable: true, minWidth: 120,
        valueFormatter: (params) => formatMinutes(params.value)
    },
    {
        field: "workTime", headerName: "실 근무", editable: true, minWidth: 120,
        valueFormatter: (params) => formatMinutes(params.value)
    },
    {
        field: "overedTime", headerName: "초과", editable: true, minWidth: 120,
        valueFormatter: (params) => formatMinutes(params.value)
    },
    {
        field: "missedTime", headerName: "지각•조퇴", editable: true, minWidth: 150,
        valueFormatter: (params) => formatMinutes(params.value)
    },
    {field: "wage", headerName: "시급", editable: true},
    {field: "status", headerName: "상태", editable: true},
    {field: "schedule", headerName: "근무일정보기", editable: false}
];

const rowData = dataList;

// ag-Grid options
const gridOptions = {
    columnDefs: columnDefs,
    rowData: rowData,
    onGridReady: (params) => {
        params.api.sizeColumnsToFit();
    },
    pagination: true, // Enable pagination
    defaultColDef: {
        sortable: true, // Enable sorting
        filter: true,    // Enable filtering
        resizable: true,
        editable: true
    }
};

// 분을 시간과 분으로 변환하는 함수
function formatMinutes(minutes) {
    if (minutes) {
        const hours = Math.floor(minutes / 60); // 전체 시간
        const mins = minutes % 60; // 남은 분
        return hours + "시간 " + mins + "분"; // 형식화된 문자열 반환
    } else {
        return '';
    }
}

document.addEventListener("DOMContentLoaded", function () {
    const gridDiv = document.getElementById("myGrid");
    if (gridDiv) {
        new agGrid.Grid(gridDiv, gridOptions);
    } else {
        console.error("그리드 컨테이너가 초기화되지 않았습니다.");
    }
});


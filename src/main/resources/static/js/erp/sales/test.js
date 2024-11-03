columnDefs = [
  { headerName: '판매 시간대', field: 'hour', },
  { headerName: '전일 목표 매출', field: 'lastDayTargetProfit', valueFormatter: currencyFormatter},
  { headerName: '전일 총 매출', field: 'lastDayTotalSales', valueFormatter: currencyFormatter},
  { headerName: '전일 매출 건수', field: 'lastDaySalesCount' },
  { headerName: '오늘 목표 매출', field: 'todayTargetProfit', valueFormatter: currencyFormatter},
  { headerName: '오늘 총 매출', field: 'todayTotalSales', valueFormatter: currencyFormatter},
  { headerName: '오늘 매출 건수', field: 'todaySalesCount' },
  { headerName: '전일 대비 매출', field: 'salesComparedToLastDay', valueFormatter: currencyFormatter},
  { headerName: '전일 대비 비율', field: 'percentageComparedToLastDay', valueFormatter: percentageFormatter},
  { headerName: '전년 대비 매출', field: 'salesComparedToLastYear', valueFormatter: currencyFormatter},
  { headerName: '전년 대비 비율', field: 'percentageComparedToLastYear', valueFormatter: percentageFormatter}
];

let gridApi;

function percentageFormatter(params) {
  return params.value + "%";
}

function currencyFormatter(params) {
  return formatNumber(params.value) + "원";
}

function formatNumber(number) {
  return Math.floor(number).toLocaleString();
}

const gridOptions = {
  rowData: null,
  columnDefs: columnDefs,
  defaultColDef: {
    editable: true,
    filter: true,
  },
};

// setup the grid after the page has finished loading
document.addEventListener("DOMContentLoaded", function () {
  let gridDiv = document.querySelector("#myGrid");
  gridApi = agGrid.createGrid(gridDiv, gridOptions);
  fetch('/erp/sales/test')
    .then((response) => response.json())
    .then((data) => gridApi.setGridOption("rowData", data));
});
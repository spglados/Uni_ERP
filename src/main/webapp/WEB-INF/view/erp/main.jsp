<%--
  Created by IntelliJ IDEA.
  User: namch
  Date: 24. 10. 10.
  Time: 오후 3:30
  Description: ERP 대시보드 페이지 (사이드바 메뉴 및 하위 메뉴 추가)
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/view/erp/layout/erpHeader.jsp" %>
<link rel="stylesheet" href="https://unpkg.com/ag-grid-community/styles/ag-grid.css">
<link rel="stylesheet" href="https://unpkg.com/ag-grid-community/styles/ag-theme-alpine.css">
<!-- 선택 사항: 부트스트랩 포함 (스타일링용) -->
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">

<!-- 메인 컨텐츠 -->
<div class="content">
    <header>
        <h1>ERP 시스템 대시보드</h1>
    </header>
    <div class="container mt-5">
        <h1>User Management</h1>
        <div id="myGrid" class="ag-theme-alpine"></div>
    </div>
</div>

<!-- AG Grid JavaScript -->
<script src="https://unpkg.com/ag-grid-community/dist/ag-grid-community.noStyle.js"></script>

<!-- 선택 사항: AJAX 요청을 위한 Axios 포함 -->
<script src="https://cdn.jsdelivr.net/npm/axios/dist/axios.min.js"></script>

<script>
    document.addEventListener('DOMContentLoaded', function () {
        const columnDefs = [
            {headerName: "ID", field: "id", sortable: true, filter: true},
            {headerName: "Name", field: "name", sortable: true, filter: true, editable: true},
            // {headerName: "Email", field: "email", sortable: true, filter: true, editable: true},
            // {headerName: "Phone", field: "phone", sortable: true, filter: true, editable: true},
            // {headerName: "Address", field: "address", sortable: true, filter: true, editable: true},
            // {headerName: "Membership", field: "membership", sortable: true, filter: true, editable: true},
            // {headerName: "Created At", field: "createdAt", sortable: true, filter: true},
            // {headerName: "Actions", field: "actions", cellRenderer: actionCellRenderer, editable: false}
        ];

        // const gridOptions = {
        //     columnDefs: columnDefs,
        //     rowData: null,
        //     pagination: true,
        //     paginationPageSize: 10,
        //     defaultColDef: {
        //         flex: 1,
        //         minWidth: 100,
        //         resizable: true,
        //     }
        // };

        // 그리드 초기화
        // const eGridDiv = document.querySelector('#myGrid');
        // new agGrid.Grid(eGridDiv, gridOptions);

        // Row Data Interface

// Grid API: Access to Grid API methods
        let gridApi;
        const test = ${test != null ? test : []};
        console.log('test', test);

// Grid Options: Contains all of the grid configurations
        const gridOptions = {
            // Data to be displayed
            rowData: test,
            // Columns to be displayed (Should match rowData properties)
            columnDefs: [
                { field: "id" },
                { field: "name" }
                // { field: "make" },
                // { field: "model" },
                // { field: "price" },
                // { field: "electric" },
            ],
        };
// Create Grid: Create new grid within the #myGrid div, using the Grid Options object
        gridApi = agGrid.createGrid(document.querySelector("#myGrid"), gridOptions);

        // REST API에서 데이터 가져오기
        //axios.get('/erp/api/users')
        // fetch('/erp/api/users')
        //     .then(function (response) {
        //         console.log('response.data', response.data);
        //         gridOptions.api.setRowData(response.data);
        //     })
        //     .catch(function (error) {
        //         console.error('Error fetching users:', error);
        //     });

        // fetch('/erp/api/users')
        //     .then(response => {
        //         console.log('response123', response);
        //         if (response.ok) {
        //             return response.json();
        //         }
        //     })
        //     .then(data => {
        //         // gridOptions.api.setRowData(data);
        //         console.log('data', data);
        //         gridApi.setGridOption('rowData', data)
        //     })
        //     .catch(error => {
        //        console.log('error', error);
        //     });


    });
</script>


<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
<script src="/js/statistics.js"></script>
<%@include file="/WEB-INF/view/erp/layout/erpFooter.jsp" %>

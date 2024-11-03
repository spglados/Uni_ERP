<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/view/erp/layout/erpHeader.jsp" %>

<!-- CSS Links -->
<link rel="stylesheet" href="/css/erp/product.css">

<!-- 메인 컨텐츠 -->
<div class="content">
    <h1>상품 목록</h1>
    <hr>
    <!-- 상품 목록 테이블 -->
    <div class="shadow p-3 mb-5 bg-white rounded" style="height: 83%; margin-top: 26px;">
        <div class="d-flex d-flex-row-reverse" style="flex-direction: row-reverse;">
            <!-- 새로운 등록 버튼 (별도의 페이지로 이동) -->
            <div class="refresh-btn-div">
                <button type="button" class="btn btn-secondary ml-2 btn-action" onclick="resetFilters()" title="필터 초기화">
                    <i class="fas fa-sync-alt"></i>
                </button>
            </div>
            <a href="/erp/product/registration" class="btn btn-secondary ml-2 btn-action" style="margin-right: 5px;"
               title="등록하러 가기">
                <i class="fas fa-plus-circle"></i> <!-- 등록 아이콘 -->
            </a>
        </div>
        <hr>
        <!-- ag-Grid 컨테이너 -->
        <div id="myGrid" class="ag-theme-quartz" style="height: 500px; width:100%;"></div> <!-- 표준 테마 사용 -->
    </div>
</div>

<!-- 재료 보기 모달 -->
<div class="modal fade" id="ingredientModal" tabindex="-1" role="dialog" aria-labelledby="ingredientModalLabel"
     aria-hidden="true">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">재료 목록</h5>
                <button type="button" class="close" onclick="saveIngredientModal()" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <!-- 재료 목록이 표시될 영역 -->
                <ul id="ingredientList" class="list-unstyled"></ul>
            </div>
            <div class="modal-footer">
                <input type="hidden" id="modalProductId">
                <button type="button" class="btn btn-success" onclick="addIngredient()">추가</button>
            </div>
        </div>
</div>

<!-- JavaScript Code -->
<script>
    // 서버에서 전달된 materialList와 materialDTOList를 JavaScript 변수로 정의
    const materialList = ${materialList != null ? materialList : '[]'};
    const materialDTOList = ${materialDTOList != null ? materialDTOList : '[]'};

    // Define materialsData before gridOptions
    const materialsData = [
        <c:forEach var="product" items="${productList}" varStatus="status">
        {
            id: '${product.id}', // 고유 식별자 추가
            productCode: '${product.productCode}',
            name: '${product.name}',
            category: '${product.category}',
            price: '${product.formatToPrice()}',
            todaySales: '${product.todaySales}',
            yesterdaySales: '${product.yesterdaySales}',
            monthSales: '${product.monthSales}',
            previousMonthSales: '${product.previousMonthSales}',
            yearSales: '${product.yearSales}'
        }<c:if test="${!status.last}">, </c:if>
        </c:forEach>
    ];

    // Initialize gridOptions
    const gridOptions = {
        // 데이터 행: materialsData 배열을 그리드에 전달
        rowData: materialsData,

        // 컬럼 정의: materialsData의 각 필드에 맞게 컬럼 설정
        columnDefs: [
            {
                field: "productCode",
                headerName: "상품 번호",
                sortable: true,
                filter: 'agNumberColumnFilter',
                resizable: true,
                suppressMovable: false,
                editable: false
            },
            {
                field: "name",
                headerName: "상품명",
                sortable: true,
                filter: true,
                resizable: true,
                suppressMovable: false,
                editable: false
            },
            {
                field: "category",
                headerName: "분류",
                sortable: true,
                filter: true,
                resizable: true,
                suppressMovable: false,
                editable: false
            },
            {
                field: "price",
                headerName: "가격",
                sortable: true,
                filter: 'agNumberColumnFilter',
                resizable: true,
                suppressMovable: false,
                editable: false,
                // 값 포매터: 가격에 '원' 추가
                valueFormatter: function (params) {
                    return params.value + '원';
                }
            },
            {
                field: "todaySales",
                headerName: "금일 판매량",
                sortable: true,
                filter: 'agNumberColumnFilter',
                resizable: true,
                suppressMovable: false,
                editable: false,
                // 값 포매터: 판매량에 '개' 추가
                valueFormatter: function (params) {
                    return params.value + '개';
                }
            },
            {
                field: "yesterdaySales",
                headerName: "전일 판매량",
                sortable: true,
                filter: 'agNumberColumnFilter',
                resizable: true,
                suppressMovable: false,
                editable: false,
                valueFormatter: function (params) {
                    return params.value + '개';
                }
            },
            {
                field: "monthSales",
                headerName: "금월 평균 일 판매량",
                sortable: true,
                filter: 'agNumberColumnFilter',
                resizable: true,
                suppressMovable: false,
                editable: false,
                valueFormatter: function (params) {
                    return params.value + '개';
                }
            },
            {
                field: "previousMonthSales",
                headerName: "전월 평균 일 판매량",
                sortable: true,
                filter: 'agNumberColumnFilter',
                resizable: true,
                suppressMovable: false,
                editable: false,
                valueFormatter: function (params) {
                    return params.value + '개';
                }
            },
            {
                field: "yearSales",
                headerName: "금년 평균 일 판매량",
                sortable: true,
                filter: 'agNumberColumnFilter',
                resizable: true,
                suppressMovable: false,
                editable: false,
                valueFormatter: function (params) {
                    return params.value + '개';
                }
            },
            {
                headerName: "재료",
                // 'actions' 필드는 데이터에 포함되지 않으므로 field는 지정하지 않습니다.
                sortable: false,
                filter: false,
                resizable: true,
                suppressMovable: false,
                editable: false,
                // 버튼을 동적으로 생성하는 cellRenderer 사용
                cellRenderer: function (params) {
                    const button = document.createElement('button');
                    button.className = 'btn btn-secondary ml-2 btn-action';
                    button.innerHTML = '<i class="fas fa-utensils"></i>';
                    // 클릭 이벤트 리스너 추가
                    button.addEventListener('click', function () {
                        showIngredients(params.data.id); // 'id' 필드 사용
                    });
                    return button;
                }
            }
        ],

        // 기본 컬럼 정의: 모든 컬럼에 공통으로 적용될 설정
        defaultColDef: {
            flex: 1, // 가변 너비
            minWidth: 150, // 최소 너비
            resizable: true, // 컬럼 크기 조절 가능
            sortable: true, // 정렬 가능
            filter: true // 필터 가능
        },

        // 행 높이 설정
        rowHeight: 55,

        // 페이징 설정
        pagination: true, // 페이징 활성화
        paginationPageSize: 10, // 한 페이지당 표시할 행 수

        // 행 애니메이션
        animateRows: true,

        // 행 선택 모드
        rowSelection: 'single', // 단일 행 선택

        // 기타 설정
        suppressMovableColumns: false, // 모든 컬럼의 이동 비활성화
        domLayout: 'autoHeight' // 그리드가 콘텐츠에 맞춰 높이 조정
    };

    // Initialize ag-Grid and setup event listeners on DOMContentLoaded
    document.addEventListener('DOMContentLoaded', function () {
        // Initialize ag-Grid
        const gridDiv = document.querySelector('#myGrid'); // 그리드가 렌더링될 DOM 요소 선택
        new agGrid.Grid(gridDiv, gridOptions); // 그리드 생성

        // 컬럼 사이즈 자동 조정
        gridOptions.api.sizeColumnsToFit();

        // 상품명이 전달되었을 경우 필터 설정
        if (window.productName && window.productName.trim() !== '') {
            // 'name' 컬럼에 'contains' 필터 적용
            gridOptions.api.setFilterModel({
                name: {
                    type: 'contains',
                    filter: window.productName
                }
            });

            // 필터 변경 사항 반영
            gridOptions.api.onFilterChanged();
        }


    });

    // 재료 보기 함수
    function showIngredients(productId) {
        document.getElementById('modalProductId').value = productId;
        fetch('/erp/product/ingredient/' + productId)
            .then(response => {
                if (response.status === 404) {
                    alert('재료 정보를 조회할 수 없습니다.');
                    throw new Error('재료 정보 조회 실패');
                }
                return response.json();
            })
            .then(data => {
                const ingredients = data;
                let ingredientList = document.getElementById('ingredientList');
                ingredientList.innerHTML = '';
                ingredients.forEach(function (ingredient, index) {
                    let li = document.createElement('li');
                    li.innerHTML =
                        '<div class="ingredient-item" id="ingredient-' + ingredient.id + '">' +
                        '<input type="hidden" name="productId" value="' + productId + '">' +
                        '<input type="text" class="ingredient-name form-control d-inline-block" value="' + ingredient.name + '" name="name" disabled>' +
                        '<input type="number" class="ingredient-amount form-control d-inline-block" value="' + ingredient.amount + '" name="amount" disabled>' +
                        '<select class="ingredient-unit form-control d-inline-block" name="unit" disabled>' +
                        '<option value="g" ' + (ingredient.unit.toUpperCase() === 'G' ? 'selected' : '') + '>g</option>' +
                        '<option value="kg" ' + (ingredient.unit.toUpperCase() === 'KG' ? 'selected' : '') + '>kg</option>' +
                        '<option value="ml" ' + (ingredient.unit.toUpperCase() === 'ML' ? 'selected' : '') + '>ml</option>' +
                        '<option value="L" ' + (ingredient.unit.toUpperCase() === 'L' ? 'selected' : '') + '>L</option>' +
                        '<option value="EA" ' + (ingredient.unit.toUpperCase() === 'EA' ? 'selected' : '') + '>EA</option>' +
                        '<option value="box" ' + (ingredient.unit.toUpperCase() === 'BOX' ? 'selected' : '') + '>box</option>' +
                        '</select>' +
                        '<button class="btn btn-warning btn-sm edit-btn" onclick="editIngredient(' + ingredient.id + ')">수정</button>' +
                        '<button class="btn btn-danger btn-sm delete-btn" onclick="deleteIngredient(' + ingredient.id + ')">삭제</button>' +
                        '</div>';
                    ingredientList.appendChild(li);
                });
                // 모달 열기
                $('#ingredientModal').modal('show');
            })
            .catch(error => {
                console.error('Error:', error);
            });
    }

    // 재료 보기 모달을 닫는 함수
    function saveIngredientModal() {
        let unsavedChanges = false;
        document.querySelectorAll('.ingredient-item').forEach(function (item) {
            const editButton = item.querySelector('.edit-btn');
            if (editButton && (editButton.textContent === '저장' || editButton.classList.contains('btn-warning'))) {
                unsavedChanges = true;
            }
        });
        // 재료 목록 초기화
        document.getElementById('ingredientList').innerHTML = '';

        // 모달 닫기
        $('#ingredientModal').modal('hide');
    }

    // 재료를 추가하는 함수
    function addIngredient() {
        const ingredientList = document.getElementById('ingredientList');
        const productId = document.getElementById('modalProductId').value;
        const index = Date.now(); // 고유한 index 값 (타임스탬프 사용)
        const li = document.createElement('li');

        li.innerHTML =
            '<div class="ingredient-item" id="ingredient-' + index + '">' +
            '<input type="text" id="ingredientInput-' + index + '" class="ingredient-name form-control d-inline-block" name="name" required oninput="filterMaterials(this, ' + index + ')" onclick="showDropdown(' + index + ')" autocomplete="off" style="width: 150px; display: inline-block; margin-right: 5px;">' +
            '<ul id="dropdown-' + index + '" class="dropdown-menu"></ul>' +
            '<input type="number" class="ingredient-amount form-control d-inline-block" name="amount" required style="width: 80px; display: inline-block; margin-right: 5px;">' +
            '<select class="ingredient-unit form-control d-inline-block" name="unit" style="width: 80px; display: inline-block; margin-right: 5px;">' +
            '<option value="G">g</option>' +
            '<option value="KG">kg</option>' +
            '<option value="ML">ml</option>' +
            '<option value="L">L</option>' +
            '<option value="EA">EA</option>' +
            '<option value="BOX">box</option>' +
            '</select>' +
            '<button class="btn btn-success btn-sm" style="margin-right: 10px;" name="add-btn" onclick="registerIngredient(' + index + ', ' + productId + ')">등록</button>' +
            '<button class="btn btn-danger btn-sm" style="margin-right: 10px;" name="delete-btn" onclick="cancelIngredient(' + index + ')">취소</button>' +
            '<button class="btn btn-secondary btn-sm" id="add-new-material-btn-' + index + '" style="display:none;" onclick="goToAddMaterialPage()">추가</button>' +
            '</div>';

        ingredientList.appendChild(li);
    }

    // 추가 재료 등록
    function registerIngredient(index, productId) {
        const ingredientItem = document.getElementById('ingredient-' + index);
        const name = ingredientItem.querySelector('input[name="name"]').value.trim();
        const amount = ingredientItem.querySelector('input[name="amount"]').value.trim();
        const unit = ingredientItem.querySelector('select[name="unit"]').value;

        // 이미 등록된 자재가 있는지 확인하는 로직
        let isDuplicate = false;
        $('#ingredientList .ingredient-item').each(function () {
            const existingName = $(this).find('input[name="name"]').val().trim();
            // 현재 등록 중인 자재를 제외하고 중복을 검사
            if (existingName === name && $(this).attr('id') !== 'ingredient-' + index) {
                isDuplicate = true;  // 중복된 자재가 발견되면 플래그 설정
            }
        });
        if (isDuplicate) {
            alert('이미 등록되어 있습니다.');
            return;
        }

        // materialList에 있는지 확인하는 로직
        if (!materialList.includes(name)) {
            alert('해당 자재는 목록에 없습니다.\n\t 자재를 먼저 등록해주세요 !');
            return;
        }

        if (!checkUnit(name, unit)) {
            return;
        }

        if (!name) {
            alert('재료명을 기입해주세요.');
            return;
        }

        if (!amount) {
            alert('재료양을 기입해주세요.');
            return;
        }

        // 숫자만 입력하도록 유효성 검사 추가
        if (isNaN(amount)) {
            alert('재료양은 숫자만 입력 가능합니다.');
            return;
        }

        const data = {
            name: name,
            amount: amount,
            unit: unit,
            productId: productId
        };

        if (!canSubmit) {
            alert('잠시 후 다시 시도해 주세요.');  // 일정 시간 내 중복 요청 방지
            return;
        }

        if (confirm("등록하시겠습니까?")) {
            canSubmit = false;  // 요청이 시작되면 플래그를 false로 설정
            fetch('/erp/product/ingredient/' + productId, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(data)
            })
                .then(response => {
                    console.log('response.status', response.status);
                    if (response.status === 200) {
                        alert('등록되었습니다.');

                        // 모달 닫기
                        $('#ingredientModal').modal('hide');

                        // 모달이 완전히 닫혔을 때 실행
                        $('#ingredientModal').on('hidden.bs.modal', function () {
                            // modal-backdrop 요소 제거
                            $('.modal-backdrop').remove();

                            // 약간의 지연을 두고 재료 정보를 다시 불러오기
                            setTimeout(function () {
                                showIngredients(productId);
                            }, 100);  // 100ms 지연 후 재료 보기
                        });
                    }
                    return response.text();
                })
                .catch(error => {
                    console.log('error', error);
                })
                .finally(() => {
                    // 일정 시간(2초)이 지나면 다시 요청할 수 있도록 설정
                    setTimeout(() => {
                        canSubmit = true;  // 지정된 시간 이후에 다시 요청 가능
                    }, submissionTerm);
                });
        }
    }

    // 재료 등록을 취소하는 함수 (추가한 재료를 삭제)
    function cancelIngredient(index) {
        const ingredientItem = document.getElementById('ingredient-' + index);
        if (ingredientItem && ingredientItem.parentNode && ingredientItem.parentNode.tagName === 'DIV') {
            ingredientItem.parentNode.removeChild(ingredientItem);
        } else {
            console.error("재료 항목을 찾을 수 없거나 이미 삭제되었습니다.");
        }
    }

    // 재료 정보를 수정하는 함수
    function editIngredient(ingredientId) {
        const ingredientItem = document.getElementById('ingredient-' + ingredientId);
        if (!ingredientItem) {
            console.error("재료 항목을 찾을 수 없습니다.");
            return;
        }
        const inputs = ingredientItem.querySelectorAll('input, select');
        const editButton = ingredientItem.querySelector('.edit-btn');
        const name = ingredientItem.querySelector('input[name="name"]').value.trim();
        const amount = ingredientItem.querySelector('input[name="amount"]').value.trim();
        const unit = ingredientItem.querySelector('select[name="unit"]').value;
        const productId = document.getElementById('modalProductId').value;

        if (!checkUnit(name, unit)) {
            return;
        }

        if (editButton.textContent === '수정') {
            inputs.forEach(function (input) {
                input.disabled = false;
            });
            editButton.textContent = '저장';
            editButton.classList.remove('btn-warning');
            editButton.classList.add('btn-success');
        } else {
            // 저장하는 fetch
            const data = {
                id: ingredientId,
                name: name,
                amount: amount,
                unit: unit,
                productId: productId
            };
            fetch('/erp/product/ingredient', {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(data)
            })
                .then(response => {
                    if (response.status === 200) {
                        alert('수정되었습니다.');
                        return response.json();
                    } else {
                        throw new Error('수정 실패');
                    }
                })
                .then(data => {
                    console.log('수정된 데이터:', data);
                })
                .catch(error => {
                    alert('수정 중에 오류가 발생했습니다.');
                    console.error('Error:', error);
                });
            inputs.forEach(function (input) {
                input.disabled = true;
            });
            editButton.textContent = '수정';
            editButton.classList.remove('btn-success');
            editButton.classList.add('btn-warning');
        }
    }

    // 재료를 삭제하는 함수
    function deleteIngredient(ingredientId) {
        const confirmDelete = confirm("정말 삭제하시겠습니까?");
        if (confirmDelete) {
            fetch('/erp/product/ingredient/' + ingredientId, {
                method: 'DELETE'
            })
                .then(response => {
                    if (response.status === 200) {
                        alert('삭제되었습니다.');
                        const ingredientItem = document.getElementById('ingredient-' + ingredientId);
                        if (ingredientItem && ingredientItem.parentNode && ingredientItem.parentNode.tagName === 'DIV') {
                            ingredientItem.parentNode.removeChild(ingredientItem);
                        } else {
                            console.error("재료 항목을 찾을 수 없거나 이미 삭제되었습니다.");
                        }
                    } else {
                        throw new Error('삭제 실패');
                    }
                })
                .catch(error => {
                    alert('삭제 중에 오류가 발생했습니다.');
                    console.error('Error:', error);
                });
        }
    }

    // 드롭다운을 업데이트하는 함수
    function updateDropdown(input, index) {
        const dropdown = document.getElementById('dropdown-' + index);
        const addNewMaterialBtn = document.getElementById('add-new-material-btn-' + index);  // "추가" 버튼
        const query = input.value.toLowerCase();

        // 필터링된 결과가 없을 경우 드롭다운 숨김
        if (!query) {
            dropdown.style.display = 'none';
            addNewMaterialBtn.style.display = 'none';  // 입력이 없을 때 추가 버튼도 숨기기
            return;
        }

        const filteredMaterials = materialList.filter(material => material.toLowerCase().includes(query));

        dropdown.innerHTML = ''; // 기존 리스트 초기화

        // 필터링된 리스트를 드롭다운에 추가
        filteredMaterials.forEach(material => {
            const li = document.createElement('li');
            li.textContent = material;
            li.classList.add('dropdown-item'); // Bootstrap 클래스 추가
            li.onclick = function () {
                input.value = material; // 선택한 값을 input에 넣음
                dropdown.style.display = 'none'; // 선택 후 드롭다운 숨기기
                addNewMaterialBtn.style.display = 'none';  // "추가" 버튼 숨기기
            };
            dropdown.appendChild(li);
        });

        // 드롭다운 보이기 및 "추가" 버튼 표시 여부 설정
        if (filteredMaterials.length > 0) {
            dropdown.style.display = 'block';
            addNewMaterialBtn.style.display = 'none';  // 일치하는 재료가 있을 때는 "추가" 버튼 숨기기
        } else {
            dropdown.style.display = 'none';
            addNewMaterialBtn.style.display = 'block';  // 일치하는 재료가 없을 때 "추가" 버튼 표시
        }
    }

    // input 필드를 클릭하면 전체 materialList를 보여줌
    function showDropdown(index) {
        const input = document.getElementById('ingredientInput-' + index);
        updateDropdown(input, index);
    }

    // "추가" 버튼 클릭 시 다른 페이지로 이동하는 함수
    function goToAddMaterialPage() {
        window.location.href = '/erp/inventory/registration'; // 추가 페이지로 이동
    }

    // 상품 등록 함수
    function registerProduct() {
        const form = $('#registerForm')[0];
        if (form.checkValidity()) {
            const formData = new FormData(form);

            // Blob 이미지 데이터를 FormData에 추가
            const imageFile = $('#image')[0].files[0];

            if (imageFile) {
                formData.append('image', imageFile);
            }

            // 서버로 formData를 전송하는 로직
            fetch('/erp/product/product', {
                method: 'POST',
                body: formData
            }).then(response => {
                if (response.ok) {
                    alert("상품이 등록되었습니다! \n\n\t 재료를 등록해야 재고가 관리됩니다 !");
                    location.reload();
                } else {
                    alert("상품 등록에 실패했습니다.");
                }
            }).catch(error => {
                console.error('Error:', error);
                alert("상품 등록 중 오류가 발생했습니다." + error);
            });
        } else {
            // 유효성 검사가 실패한 경우 경고창을 띄우고, 유효성 검사를 강제로 실행
            form.reportValidity();
        }
    }

    // 특정 자재의 unit을 찾는 함수
    function getUnitByName(materialName) {
        const material = materialDTOList.find(item => item.name === materialName);
        return material ? material.unit : null; // 자재가 존재하면 unit 반환, 없으면 null 반환
    }

    function getSubUnitByName(materialName) {
        const material = materialDTOList.find(item => item.name === materialName);
        return material ? material.subUnit : null; // 자재가 존재하면 subUnit 반환, 없으면 null 반환
    }

    function checkUnit(name, unit) {
        const materialUnit = getUnitByName(name);
        const materialSubUnit = getSubUnitByName(name);

        if (materialUnit && (materialUnit.toUpperCase() === unit.toUpperCase() || (materialSubUnit && materialSubUnit.toUpperCase() === unit.toUpperCase()))) {
            return true;
        } else {
            alert(`${name}의 단위는 [${materialUnit}] 또는 [${materialSubUnit}] 만 사용 가능합니다.`);
            return false;
        }
    }

    // 필터 초기화 함수
    function resetFilters() {
        gridOptions.api.setFilterModel(null); // 모든 필터 초기화
        gridOptions.api.onFilterChanged(); // 필터 변경 사항 반영
    }

</script>

<c:if test="${not empty productName}">
    <script>
        window.productName = '<c:out value="${productName}" />';
    </script>
</c:if>

<%@ include file="/WEB-INF/view/erp/layout/erpFooter.jsp" %>

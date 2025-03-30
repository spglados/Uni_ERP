<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ include file="/WEB-INF/view/erp/layout/erpHeader.jsp" %>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/erp/material.css">
<style>
    .inner-search {
        margin-right: 10px;
    }

    .btn-main {
        background-color: #F8F399;
        border-color: #F8F399;
        color: #000;
    }

    .form-container {
        position: relative;
        padding: 20px;
    }

    .image-preview-container img {
        max-width: 100%;
        max-height: 100%;
    }

    .form-section {
        display: flex;
        flex-wrap: wrap;
        gap: 20px;
    }

    .form-group {
        flex: 1 1 45%;
        min-width: 200px;
        display: flex;
        flex-direction: column;
    }

    .full-width {
        flex: 1 1 100%;
    }

    .button-group {
        display: flex;
        gap: 10px;
        margin-top: 20px;
    }

    @media (max-width: 768px) {
        .image-preview-container {
            position: static;
            margin-bottom: 20px;
        }

        .form-section {
            flex-direction: column;
        }
    }

    input, select {
        max-width: 80%;
    }
</style>

<!-- JSP에서 unitCategories를 JSON 배열로 변환하여 JavaScript 변수에 저장 -->
<script type="text/javascript">
    var unitCategories = [
        <c:forEach var="unit" items="${unitCategories}" varStatus="status">
        "${unit}"<c:if test="${!status.last}">, </c:if>
        </c:forEach>
    ];

    // 단위와 서브 단위의 관계를 정의하는 매핑 객체 (올바른 논리적 관계로 수정)
    var unitSubUnitMap = {
        "KG": ["G", "BOX", "EA"],
        "G": ["BOX", "EA"],
        "L": ["ML", "BOX", "EA"],
        "ML": ["BOX", "EA"],
        "BOX": ["EA", "KG", "G", "L", "ML"],
        "EA": ["KG", "G", "L", "ML"]
    };
</script>

<!-- 자재 수정 컨텐츠 -->
<div class="content container-fluid">
    <h1>자재 수정</h1>
    <hr>

    <!-- 검색 섹션 -->
    <div class="search-section d-flex mb-4 flex-row-reverse">
        <div class="inner-search">
            <button class="btn btn-main" onclick="searchMaterial()">검색</button>
        </div>
        <div class="inner-search">
            <input type="text" id="searchMaterialCode" class="form-control" placeholder="자재 코드를 입력하세요" style="max-width: 100%;"/>
        </div>
    </div>

    <!-- 자재 수정 폼 -->
    <div class="shadow p-4 mb-5 bg-white rounded form-container">
        <form id="editMaterialForm">
            <div class="form-section">
                    <!-- 자재 코드 -->
                    <div class="form-group">
                        <label for="materialCode">자재 코드 <span class="text-danger">*</span></label>
                        <input type="text" class="form-control" id="materialCode" name="materialCode" required
                               readonly/>
                        <label for="name">이름 <span class="text-danger">*</span></label>
                        <input type="text" class="form-control" id="name" name="name" required readOnly>
                    </div>
                <!-- 카테고리 -->
                <div class="form-group">
                    <label for="category">카테고리 <span class="text-danger">*</span></label>
                    <select class="form-control" id="category" name="category" required disabled>
                        <option value="">선택하세요</option>
                        <option value="냉동품">냉동품</option>
                        <option value="냉장품">냉장품</option>
                        <option value="상온품">상온품</option>
                    </select>
                <!-- 단위 -->
                    <label for="unit">단위 <span class="text-danger">*</span></label>
                    <select class="form-control" id="unit" name="unit" required disabled>
                        <option value="">선택하세요</option>
                        <!-- 옵션은 JavaScript로 동적으로 추가 -->
                    </select>
                </div>

                <!-- 서브 수량 -->
                <div class="form-group">
                    <label for="subAmount">서브 수량</label>
                    <input type="number" step="0.01" class="form-control" id="subAmount" name="subAmount"
                           disabled>
                <!-- 서브 단위 -->
                    <label for="subUnit">서브 단위 <span class="text-danger">*</span></label>
                    <select class="form-control" id="subUnit" name="subUnit" required disabled>
                        <option value="">선택하세요</option>
                        <!-- 옵션은 JavaScript로 동적으로 추가 -->
                    </select>
                </div>
                <!-- 알람 주기 -->
                <div class="form-group">
                    <label for="alarmCycle">알람 주기</label>
                    <input type="number" step="0.01" class="form-control" id="alarmCycle" name="alarmCycle" readOnly>
                    <label for="alarmUnit">알람 단위 <span class="text-danger">*</span></label>
                    <select class="form-control" id="alarmUnit" name="alarmUnit" required disabled>
                        <option value="">선택하세요</option>
                        <!-- 옵션은 JavaScript로 동적으로 추가 -->
                    </select>
                </div>
            </div>
            <!-- 버튼 그룹 -->
            <div class="button-group">
                <!-- 제출 버튼 추가 -->
                <button type="button" class="btn btn-main" onclick="updateMaterial()">수정</button>
                <a href="${pageContext.request.contextPath}/erp/inventory/status" class="btn btn-secondary">취소</a>
            </div>
        </form>
    </div>
</div>

<!-- JavaScript 코드 추가 -->
<script>
    // 단위, 서브 단위, 알람 단위 <select> 요소 가져오기
    var unitSelect = document.getElementById('unit');
    var subAmountInput = document.getElementById('subAmount');
    var subUnitSelect = document.getElementById('subUnit');
    var alarmUnitSelect = document.getElementById('alarmUnit');
    var editMaterialForm = document.getElementById('editMaterialForm');

    /**
     * <select> 요소에 옵션을 동적으로 추가하는 함수
     * @param {HTMLElement} selectElement - 옵션을 추가할 <select> 요소
     * @param {Array} options - 추가할 옵션 값들의 배열
     * @param {boolean} excludeSelectedUnit - 선택된 단위를 제외할지 여부
     * @param {string} selectedUnit - 선택된 단위
     */
    function populateSelectOptions(selectElement, options, excludeSelectedUnit, selectedUnit) {
        // 기존 옵션 제거 (첫 번째 '선택하세요' 옵션만 남김)
        var firstOption = selectElement.querySelector('option[value=""]');
        selectElement.innerHTML = '';
        if (firstOption) {
            selectElement.appendChild(firstOption);
        }

        for (var i = 0; i < options.length; i++) {
            var optionValue = options[i].toUpperCase(); // Ensure consistency
            if (excludeSelectedUnit && optionValue === selectedUnit) {
                continue; // 선택된 단위는 제외
            }
            var option = document.createElement('option');
            option.value = optionValue;
            option.text = optionValue;
            selectElement.appendChild(option);
        }
    }

    /**
     * 알람 단위 옵션을 필터링하여 선택된 단위와 서브 단위만 추가하는 함수
     * @param {string} selectedUnit - 선택된 단위
     * @param {string} selectedSubUnit - 선택된 서브 단위
     */
    function filterAlarmUnitOptions(selectedUnit, selectedSubUnit) {
        // 기존 알람 단위 옵션 제거
        alarmUnitSelect.innerHTML = '<option value="">선택하세요</option>';

        // 선택된 단위와 서브 단위만 알람 단위로 추가
        var optionsToAdd = [];
        if (selectedUnit) {
            optionsToAdd.push(selectedUnit);
        }
        if (selectedSubUnit && selectedSubUnit !== selectedUnit) {
            optionsToAdd.push(selectedSubUnit);
        }

        // 중복 제거
        var uniqueOptions = [];
        for (var i = 0; i < optionsToAdd.length; i++) {
            if (uniqueOptions.indexOf(optionsToAdd[i]) === -1) {
                uniqueOptions.push(optionsToAdd[i]);
            }
        }

        // 알람 단위 옵션에 추가
        populateSelectOptions(alarmUnitSelect, uniqueOptions, false, '');
    }

    // 단위, 서브 단위, 알람 단위 <select>에 초기 옵션 추가
    populateSelectOptions(unitSelect, unitCategories, false, '');
    populateSelectOptions(subUnitSelect, unitCategories, false, '');
    populateSelectOptions(alarmUnitSelect, unitCategories, false, '');

    // 단위 선택 시 서브 수량 활성화/비활성화 및 서브 단위 필터링
    unitSelect.addEventListener('change', function () {
        var selectedUnit = this.value.toUpperCase();

        // 서브 수량 활성화/비활성화
        if (selectedUnit === 'BOX' || selectedUnit === 'EA') {
            subAmountInput.disabled = false;
        } else {
            subAmountInput.disabled = true;
            subAmountInput.value = ''; // 값 초기화
        }

        // 서브 단위 옵션 필터링 (선택된 단위를 제외)
        populateSelectOptions(subUnitSelect, unitCategories, true, selectedUnit);

        // 알람 단위 옵션 업데이트
        var selectedSubUnit = subUnitSelect.value.toUpperCase();
        filterAlarmUnitOptions(selectedUnit, selectedSubUnit);
    });

    // 서브 단위 선택 시 알람 단위 옵션 업데이트
    subUnitSelect.addEventListener('change', function () {
        var selectedSubUnit = this.value.toUpperCase();
        var selectedUnit = unitSelect.value.toUpperCase();

        // 알람 단위 옵션 업데이트
        filterAlarmUnitOptions(selectedUnit, selectedSubUnit);
    });

    /**
     * 클라이언트 측 유효성 검사 함수
     * @returns {boolean} - 유효성 검사 통과 여부
     */
    function validateForm() {
        var isValid = true;
        var errorMessage = '';

        // 필수 입력값 확인
        var requiredFields = [
            {id: 'materialCode', label: '자재 코드'},
            {id: 'name', label: '이름'},
            {id: 'category', label: '카테고리'},
            {id: 'unit', label: '단위'},
            {id: 'subUnit', label: '서브 단위'},
            {id: 'alarmUnit', label: '알람 단위'}
        ];

        for (var i = 0; i < requiredFields.length; i++) {
            var field = requiredFields[i];
            var element = document.getElementById(field.id);
            if (!element.value.trim()) {
                isValid = false;
                errorMessage += field.label + '을(를) 입력해주세요.\n';
            }
        }

        // 서브 수량이 활성화된 경우 유효성 검사
        if (!subAmountInput.disabled) {
            var subAmount = parseFloat(subAmountInput.value);
            if (isNaN(subAmount) || subAmount <= 0) {
                isValid = false;
                errorMessage += '서브 수량은 0보다 큰 숫자여야 합니다.\n';
            }
        }

        // 알람 주기 유효성 검사 (필요 시)
        var alarmCycle = document.getElementById('alarmCycle');
        if (alarmCycle.value) {
            var alarmCycleValue = parseFloat(alarmCycle.value);
            if (isNaN(alarmCycleValue) || alarmCycleValue <= 0) {
                isValid = false;
                errorMessage += '알람 주기는 0보다 큰 숫자여야 합니다.\n';
            }
        }

        // 단위와 서브 단위의 유효성 검사
        var unit = document.getElementById('unit').value.toUpperCase();
        var subUnit = document.getElementById('subUnit').value.toUpperCase();
        if (unit && subUnit) {
            var allowedSubUnits = unitSubUnitMap[unit] || [];
            if (allowedSubUnits.indexOf(subUnit) === -1) {
                isValid = false;
                errorMessage += '단위와 서브 단위의 조합이 유효하지 않습니다.\n';
            }
        }

        if (!isValid) {
            alert(errorMessage);
        }

        return isValid;
    }

    /**
     * 자재 수정 함수
     */
    function updateMaterial() {
        if (!validateForm()) {
            return; // 유효성 검사 실패 시 전송 중단
        }

        // 폼 데이터 추출
        var materialCode = document.getElementById('materialCode').value.trim();
        var name = document.getElementById('name').value.trim();
        var category = document.getElementById('category').value;
        var unit = document.getElementById('unit').value.toUpperCase();
        var subAmount = document.getElementById('subAmount').value ? parseFloat(document.getElementById('subAmount').value) : null;
        var subUnit = document.getElementById('subUnit').value.toUpperCase();
        var alarmCycle = document.getElementById('alarmCycle').value ? parseFloat(document.getElementById('alarmCycle').value) : null;
        var alarmUnit = document.getElementById('alarmUnit').value.toUpperCase();

        // DTO 형식으로 데이터 구성
        var materialSaveDTO = {
            materialCode: parseLong(materialCode),
            name: name,
            category: category,
            unit: unit,
            subAmount: subAmount,
            subUnit: subUnit,
            alarmCycle: alarmCycle,
            alarmUnit: alarmUnit
        };

        if (confirm('자재 단위를 변경하시면 재고 계산 시스템에 문제가 발생할 수도 있으며,' +
            '\n\n\t 과실은 사용자에게 있습니다' +
            '\n\n\t 위 내용을 확인하셨나요?')) {

            if (confirm('정말 변경하시겠습니까?')) {
            } else {
                return;
            }

        } else {
            return;
        }


        fetch('/erp/inventory/materials', { // Path Variable 없이 PUT 요청
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(materialSaveDTO)
        })
            .then(function (response) {
                if (response.ok) {
                    return response.text().then(function (text) {
                        alert(text || '자재가 성공적으로 수정되었습니다.');
                        window.location.href = '/erp/inventory/status';
                    });
                } else {
                    return response.json().then(function (data) {
                        throw new Error(data.error || '자재 수정에 실패했습니다.');
                    });
                }
            })
            .catch(function (error) {
                console.error('Error:', error);
                alert('오류가 발생했습니다: ' + error.message);
            });
    }

    /**
     * materialCode를 Long으로 변환하는 헬퍼 함수
     * @param {string} value
     * @returns {number}
     */
    function parseLong(value) {
        var parsed = parseInt(value, 10);
        return isNaN(parsed) ? null : parsed;
    }

    document.getElementById("searchMaterialCode").addEventListener("keyup", function (event) {
        if (event.key === "Enter") {
            searchMaterial();
        }
    });

    /**
     * 자재 검색 함수
     */
    function searchMaterial() {
        var materialCodeInput = document.getElementById('searchMaterialCode');
        var materialCode = materialCodeInput.value.trim();
        if (!materialCode) {
            alert('자재 코드를 입력해주세요.');
            return;
        }

        fetch('/erp/inventory/materials/' + materialCode)
            .then(function (response) {
                if (response.status === 404) {
                    throw new Error('해당 자재를 찾을 수 없습니다.');
                }
                if (!response.ok) {
                    throw new Error('자재 정보를 가져오는 데 실패했습니다.');
                }
                return response.json();
            })
            .then(function (data) {
                editable();
                populateForm(data);
            })
            .catch(function (error) {
                alert(error.message);
                clearForm();
            });
    }

    /**
     * 폼에 데이터 채우기
     */
    function populateForm(material) {
        document.getElementById('materialCode').value = material.materialCode || '';
        document.getElementById('name').value = material.name || '';
        document.getElementById('category').value = material.category || '';
        document.getElementById('unit').value = material.unit ? material.unit.toUpperCase() : '';
        document.getElementById('subAmount').value = material.subAmount || '';
        document.getElementById('subUnit').value = material.subUnit ? material.subUnit.toUpperCase() : '';
        document.getElementById('alarmCycle').value = material.alarmCycle || '';
        document.getElementById('alarmUnit').value = material.alarmUnit ? material.alarmUnit.toUpperCase() : '';

        // 단위 선택에 따른 서브 단위 옵션 업데이트
        populateSelectOptions(unitSelect, unitCategories, false, material.unit ? material.unit.toUpperCase() : '');

        // 단위 변경 이벤트 트리거 (서브 단위 옵션 업데이트)
        var event = new Event('change');
        unitSelect.dispatchEvent(event);


        unitSelect.value = material.unit ? material.unit.toUpperCase() : '';

        // 서브 단위 설정 (서브 단위 옵션이 업데이트된 후)
        subUnitSelect.value = material.subUnit ? material.subUnit.toUpperCase() : '';

        // 알람 단위 옵션 업데이트 및 설정
        filterAlarmUnitOptions(material.unit ? material.unit.toUpperCase() : '', material.subUnit ? material.subUnit.toUpperCase() : '');
        alarmUnitSelect.value = material.alarmUnit ? material.alarmUnit.toUpperCase() : '';

        // 서브 수량 활성화 여부 업데이트
        if (material.unit && (material.unit.toUpperCase() === 'BOX' || material.unit.toUpperCase() === 'EA')) {
            subAmountInput.disabled = false;
        } else {
            subAmountInput.disabled = true;
            subAmountInput.value = '';
        }
    }

    /**
     * 초기 페이지 로드 시 단위, 서브 단위, 알람 단위 옵션 설정
     */
    window.onload = function () {
        // 단위 옵션 설정
        populateSelectOptions(unitSelect, unitCategories, false, '');
        // 서브 단위 옵션 설정 (기본적으로 모든 옵션)
        populateSelectOptions(subUnitSelect, unitCategories, false, '');
        // 알람 단위 옵션 설정 (기본적으로 모든 옵션)
        populateSelectOptions(alarmUnitSelect, unitCategories, false, '');
    };

    function editable() {
        // 필드들에 readonly 속성 해제
        document.getElementById("name").readOnly = false;
        document.getElementById("alarmCycle").readOnly = false;

        // disabled 속성 해제
        document.getElementById("category").disabled = false;
        document.getElementById("unit").disabled = false;
        document.getElementById("subUnit").disabled = false;
        document.getElementById("alarmUnit").disabled = false;
    }
</script>

<%@ include file="/WEB-INF/view/erp/layout/erpFooter.jsp" %>

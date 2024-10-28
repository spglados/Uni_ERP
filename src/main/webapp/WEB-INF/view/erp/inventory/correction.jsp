<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ include file="/WEB-INF/view/erp/layout/erpHeader.jsp" %>
<link rel="stylesheet" href="/css/erp/material.css">
<style>
    .edit-container {
        display: flex;
        width: 100%;
        height: 100%;
    }
</style>

<!-- JSP에서 unitCategories를 JSON 배열로 변환하여 JavaScript 변수에 저장 -->
<script type="text/javascript">
    var unitCategories = [
        <c:forEach var="unit" items="${unitCategories}" varStatus="status">
        "${unit}"<c:if test="${!status.last}">, </c:if>
        </c:forEach>
    ];
</script>

<!-- 자재 수정 컨텐츠 -->
<div class="content">
    <h1>자재 수정</h1>
    <hr>

    <!-- 검색 섹션 -->
    <div class="search-section d-flex mb-4">
        <input type="text" id="searchMaterialCode" class="form-control mr-2" placeholder="자재 코드를 입력하세요" />
        <button class="btn btn-primary" onclick="searchMaterial()">검색</button>
    </div>

    <!-- 자재 수정 폼 -->
    <div class="shadow p-3 mb-5 bg-white rounded">
        <form id="editMaterialForm">
            <div class="edit-container">
                <!-- 기본 정보 섹션 -->
                <div class="form-section">
                    <div class="form-row">
                        <!-- 자재 코드 -->
                        <div class="form-group col-md-6">
                            <label for="materialCode">자재 코드 <span class="text-danger">*</span></label>
                            <input type="text" class="form-control" id="materialCode" name="materialCode" required />
                        </div>
                        <!-- 이름 -->
                        <div class="form-group col-md-6">
                            <label for="name">이름 <span class="text-danger">*</span></label>
                            <input type="text" class="form-control" id="name" name="name" required>
                        </div>
                    </div>
                    <div class="form-row">
                        <!-- 카테고리 -->
                        <div class="form-group col-md-6">
                            <label for="category">카테고리 <span class="text-danger">*</span></label>
                            <select class="form-control" id="category" name="category" required>
                                <option value="">선택하세요</option>
                                <option value="냉동품">냉동품</option>
                                <option value="냉장품">냉장품</option>
                                <option value="상온품">상온품</option>
                            </select>
                        </div>
                        <!-- 단위 -->
                        <div class="form-group col-md-6">
                            <label for="unit">단위 <span class="text-danger">*</span></label>
                            <select class="form-control" id="unit" name="unit" required>
                                <option value="">선택하세요</option>
                                <!-- 옵션은 JavaScript로 동적으로 추가 -->
                            </select>
                        </div>
                    </div>

                    <div class="form-row">
                        <!-- 서브 수량 -->
                        <div class="form-group col-md-6">
                            <label for="subAmount">서브 수량</label>
                            <input type="number" step="0.01" class="form-control" id="subAmount" name="subAmount" disabled>
                        </div>
                        <!-- 서브 단위 -->
                        <div class="form-group col-md-6">
                            <label for="subUnit">서브 단위 <span class="text-danger">*</span></label>
                            <select class="form-control" id="subUnit" name="subUnit" required>
                                <option value="">선택하세요</option>
                                <!-- 옵션은 JavaScript로 동적으로 추가 -->
                            </select>
                        </div>
                    </div>
                    <div class="form-row">
                        <!-- 알람 주기 -->
                        <div class="form-group col-md-6">
                            <label for="alarmCycle">알람 주기</label>
                            <input type="number" step="0.01" class="form-control" id="alarmCycle" name="alarmCycle">
                        </div>

                        <div class="form-group col-md-6">
                            <label for="alarmUnit">알람 단위 <span class="text-danger">*</span></label>
                            <select class="form-control" id="alarmUnit" name="alarmUnit" required>
                                <option value="">선택하세요</option>
                                <!-- 옵션은 JavaScript로 동적으로 추가 -->
                            </select>
                        </div>
                    </div>
                    <!-- 제출 버튼 추가 -->
                    <button type="button" class="btn btn-primary" onclick="updateMaterial()">수정</button>
                    <button type="button" class="btn btn-secondary" onclick="clearForm()">초기화</button>
                </div>
            </div>
        </form>
    </div>
</div>

<!-- JavaScript 코드 추가 -->
<script>
    document.addEventListener('DOMContentLoaded', function () {
        const unitSelect = document.getElementById('unit');
        const subAmountInput = document.getElementById('subAmount');
        const subUnitSelect = document.getElementById('subUnit');
        const alarmUnitSelect = document.getElementById('alarmUnit');
        const editMaterialForm = document.getElementById('editMaterialForm');

        /**
         * <select> 요소에 옵션을 동적으로 추가하는 함수
         * @param {HTMLElement} selectElement - 옵션을 추가할 <select> 요소
         * @param {Array} options - 추가할 옵션 값들의 배열
         * @param {boolean} excludeSelectedUnit - 선택된 단위를 제외할지 여부
         */
        function populateSelectOptions(selectElement, options, excludeSelectedUnit = false, selectedUnit = '') {
            // 기존 옵션 제거 (첫 번째 '선택하세요' 옵션만 남김)
            const firstOption = selectElement.querySelector('option[value=""]');
            selectElement.innerHTML = '';
            selectElement.appendChild(firstOption);

            options.forEach(optionValue => {
                if (excludeSelectedUnit && optionValue === selectedUnit) {
                    return; // 선택된 단위는 제외
                }
                const option = document.createElement('option');
                option.value = optionValue;
                option.text = optionValue;
                selectElement.appendChild(option);
            });
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
            const optionsToAdd = [];
            if (selectedUnit) {
                optionsToAdd.push(selectedUnit);
            }
            if (selectedSubUnit && selectedSubUnit !== selectedUnit) {
                optionsToAdd.push(selectedSubUnit);
            }

            // 중복 제거
            const uniqueOptions = [...new Set(optionsToAdd)];

            // 알람 단위 옵션에 추가
            populateSelectOptions(alarmUnitSelect, uniqueOptions);
        }

        // 단위, 서브 단위, 알람 단위 <select>에 초기 옵션 추가
        populateSelectOptions(unitSelect, unitCategories);
        populateSelectOptions(subUnitSelect, unitCategories);
        populateSelectOptions(alarmUnitSelect, unitCategories);

        // 단위 선택 시 서브 수량 활성화/비활성화 및 서브 단위 필터링
        unitSelect.addEventListener('change', function () {
            const selectedUnit = this.value;

            // 서브 수량 활성화/비활성화
            if (selectedUnit.toLowerCase() === 'box' || selectedUnit.toLowerCase() === 'ea') {
                subAmountInput.disabled = false;
            } else {
                subAmountInput.disabled = true;
                subAmountInput.value = ''; // 값 초기화
            }

            // 서브 단위 옵션 필터링 (선택된 단위를 제외)
            populateSelectOptions(subUnitSelect, unitCategories, true, selectedUnit);

            // 알람 단위 옵션 업데이트
            const selectedSubUnit = subUnitSelect.value;
            filterAlarmUnitOptions(selectedUnit, selectedSubUnit);
        });

        // 서브 단위 선택 시 알람 단위 옵션 업데이트
        subUnitSelect.addEventListener('change', function () {
            const selectedSubUnit = this.value;
            const selectedUnit = unitSelect.value;

            // 알람 단위 옵션 업데이트
            filterAlarmUnitOptions(selectedUnit, selectedSubUnit);
        });

        /**
         * 클라이언트 측 유효성 검사 함수
         * @returns {boolean} - 유효성 검사 통과 여부
         */
        function validateForm() {
            let isValid = true;
            let errorMessage = '';

            // 필수 입력값 확인
            const requiredFields = [
                { id: 'materialCode', label: '자재 코드' },
                { id: 'name', label: '이름' },
                { id: 'category', label: '카테고리' },
                { id: 'unit', label: '단위' },
                { id: 'subUnit', label: '서브 단위' },
                { id: 'alarmUnit', label: '알람 단위' }
            ];

            requiredFields.forEach(field => {
                const element = document.getElementById(field.id);
                if (!element.value.trim()) {
                    isValid = false;
                    errorMessage += `${field.label}을(를) 입력해주세요.\n`;
                }
            });

            // 서브 수량이 활성화된 경우 유효성 검사
            if (!subAmountInput.disabled) {
                const subAmount = parseFloat(subAmountInput.value);
                if (isNaN(subAmount) || subAmount <= 0) {
                    isValid = false;
                    errorMessage += '서브 수량은 0보다 큰 숫자여야 합니다.\n';
                }
            }

            // 알람 주기 유효성 검사 (필요 시)
            const alarmCycle = document.getElementById('alarmCycle');
            if (alarmCycle.value) {
                const alarmCycleValue = parseFloat(alarmCycle.value);
                if (isNaN(alarmCycleValue) || alarmCycleValue <= 0) {
                    isValid = false;
                    errorMessage += '알람 주기는 0보다 큰 숫자여야 합니다.\n';
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
            const materialCode = document.getElementById('materialCode').value.trim();
            const name = document.getElementById('name').value.trim();
            const category = document.getElementById('category').value;
            const unit = document.getElementById('unit').value;
            const subAmount = document.getElementById('subAmount').value ? parseFloat(document.getElementById('subAmount').value) : null;
            const subUnit = document.getElementById('subUnit').value;
            const alarmCycle = document.getElementById('alarmCycle').value ? parseFloat(document.getElementById('alarmCycle').value) : null;
            const alarmUnit = document.getElementById('alarmUnit').value;

            // DTO 형식으로 데이터 구성
            const materialSaveDTO = {
                materialCode: parseLong(materialCode),
                name: name,
                category: category,
                unit: unit,
                subAmount: subAmount,
                subUnit: subUnit,
                alarmCycle: alarmCycle,
                alarmUnit: alarmUnit
            };

            fetch('/erp/inventory/materials', { // Path Variable 없이 PUT 요청
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(materialSaveDTO)
            })
                .then(response => {
                    if (response.ok) {
                        return response.text().then(text => {
                            alert(text || '자재가 성공적으로 수정되었습니다.');
                            window.location.href = '/erp/inventory/list';
                        });
                    } else {
                        return response.text().then(text => { throw new Error(text || '자재 수정에 실패했습니다.'); });
                    }
                })
                .catch(error => {
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
            const parsed = parseInt(value, 10);
            return isNaN(parsed) ? null : parsed;
        }

        /**
         * 자재 검색 함수
         */
        function searchMaterial() {
            const materialCodeInput = document.getElementById('searchMaterialCode');
            const materialCode = materialCodeInput.value.trim();
            if (!materialCode) {
                alert('자재 코드를 입력해주세요.');
                return;
            }

            fetch('/erp/inventory/materials/' + encodeURIComponent(materialCode))
                .then(response => {
                    if (response.status === 404) {
                        throw new Error('해당 자재를 찾을 수 없습니다.');
                    }
                    return response.json();
                })
                .then(data => {
                    populateForm(data);
                })
                .catch(error => {
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
            document.getElementById('unit').value = material.unit || '';
            document.getElementById('subAmount').value = material.subAmount || '';
            document.getElementById('subUnit').value = material.subUnit || '';
            document.getElementById('alarmCycle').value = material.alarmCycle || '';
            document.getElementById('alarmUnit').value = material.alarmUnit || '';
        }

        /**
         * 폼 초기화
         */
        function clearForm() {
            document.getElementById('editMaterialForm').reset();
        }

        // 단위, 서브 단위, 알람 단위 <select>에 초기 옵션 추가
        populateSelectOptions(unitSelect, unitCategories);
        populateSelectOptions(subUnitSelect, unitCategories);
        populateSelectOptions(alarmUnitSelect, unitCategories);

</script>

<%@ include file="/WEB-INF/view/erp/layout/erpFooter.jsp" %>

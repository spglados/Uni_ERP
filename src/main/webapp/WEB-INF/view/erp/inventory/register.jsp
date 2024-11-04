<%--
  Created by IntelliJ IDEA.
  User: namch
  Date: 24. 10. 10.
  Time: 오후 6:42
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ include file="/WEB-INF/view/erp/layout/erpHeader.jsp" %>
<link rel="stylesheet" href="/css/erp/material.css">
<style>
    .btn-main {
        background-color: #F8F399;
        border-color: #F8F399;
        color: #000;
    }
    .register-container {
        display: flex;
        flex-direction: column;
        padding: 20px;
        position: relative;
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
        .form-section {
            flex-direction: column;
        }
    }
    /* 추가적인 스타일링 */
    label {
        margin-bottom: 5px;
        font-weight: bold;
    }
    input, select, textarea {
        max-width: 100%;
    }
</style>

<!-- JSP에서 unitCategories를 JSON 배열로 변환하여 JavaScript 변수에 저장 -->
<script type="text/javascript">
    var unitCategories = [
        <c:forEach var="unit" items="${unitCategories}" varStatus="status">
        "${unit}"<c:if test="${!status.last}">, </c:if>
        </c:forEach>
    ];

    // 단위와 서브 단위의 관계를 정의하는 매핑 객체 (사용자 제공 매핑)
    var unitSubUnitMap = {
        "KG": ["G", "BOX", "EA"],
        "G": ["BOX", "EA"],
        "L": ["ML", "BOX", "EA"],
        "ML": ["BOX", "EA"],
        "BOX": ["EA", "KG", "G", "L", "ML"],
        "EA": ["KG", "G", "L", "ML"]
    };
</script>

<!-- 자재 등록 컨텐츠 -->
<div class="content container-fluid">
    <h1>재고 등록</h1>
    <hr>

    <!-- 자재 등록 폼 -->
    <div class="shadow p-4 mb-5 bg-white rounded register-container">
        <form id="materialForm">
            <div class="form-section">
                <!-- 이름 -->
                <div class="form-group">
                    <label for="name">이름 <span class="text-danger">*</span></label>
                    <input type="text" class="form-control" id="name" name="name" required placeholder="자재 이름을 입력하세요">
                </div>

                <!-- 카테고리 -->
                <div class="form-group">
                    <label for="category">카테고리 <span class="text-danger">*</span></label>
                    <select class="form-control" id="category" name="category" required>
                        <option value="">선택하세요</option>
                        <option value="냉동품">냉동품</option>
                        <option value="냉장품">냉장품</option>
                        <option value="상온품">상온품</option>
                    </select>
                </div>

                <!-- 단위 -->
                <div class="form-group">
                    <label for="unit">단위 <span class="text-danger">*</span></label>
                    <select class="form-control" id="unit" name="unit" required>
                        <option value="">선택하세요</option>
                        <!-- 옵션은 JavaScript로 동적으로 추가 -->
                    </select>
                </div>

                <!-- 서브 수량 -->
                <div class="form-group">
                    <label for="subAmount">서브 수량</label>
                    <input type="number" step="0.01" class="form-control" id="subAmount" name="subAmount" disabled placeholder="서브 수량을 입력하세요">
                </div>

                <!-- 서브 단위 -->
                <div class="form-group">
                    <label for="subUnit">서브 단위 <span class="text-danger">*</span></label>
                    <select class="form-control" id="subUnit" name="subUnit" required>
                        <option value="">선택하세요</option>
                        <!-- 옵션은 JavaScript로 동적으로 추가 -->
                    </select>
                </div>

                <!-- 알람 주기 -->
                <div class="form-group">
                    <label for="alarmCycle">알람 주기</label>
                    <input type="number" step="0.01" class="form-control" id="alarmCycle" name="alarmCycle" placeholder="알람 주기를 입력하세요">
                </div>

                <!-- 알람 단위 -->
                <div class="form-group">
                    <label for="alarmUnit">알람 단위 <span class="text-danger">*</span></label>
                    <select class="form-control" id="alarmUnit" name="alarmUnit" style="max-width: 55%;" required>
                        <option value="">선택하세요</option>
                        <!-- 옵션은 JavaScript로 동적으로 추가 -->
                    </select>
                </div>
                <!-- 버튼 그룹 -->
                <div class="button-group">
                    <button type="submit" class="btn btn-main">등록</button>
                </div>
            </div>

        </form>
    </div>

    <!-- JavaScript 코드 추가 -->
    <script>
        document.addEventListener('DOMContentLoaded', function () {
            const unitSelect = document.getElementById('unit');
            const subAmountInput = document.getElementById('subAmount');
            const subUnitSelect = document.getElementById('subUnit');
            const alarmUnitSelect = document.getElementById('alarmUnit');
            const materialForm = document.getElementById('materialForm');

            /**
             * <select> 요소에 옵션을 동적으로 추가하는 함수
             * @param {HTMLElement} selectElement - 옵션을 추가할 <select> 요소
             * @param {Array} options - 추가할 옵션 값들의 배열
             * @param {boolean} excludeSelectedUnit - 선택된 단위를 제외할지 여부
             * @param {string} selectedUnit - 선택된 단위
             */
            function populateSelectOptions(selectElement, options, excludeSelectedUnit = false, selectedUnit = '') {
                // 기존 옵션 제거 (첫 번째 '선택하세요' 옵션만 남김)
                const firstOption = selectElement.querySelector('option[value=""]');
                selectElement.innerHTML = '';
                if (firstOption) {
                    selectElement.appendChild(firstOption);
                }

                options.forEach(function (optionValue) {
                    if (excludeSelectedUnit && optionValue.toUpperCase() === selectedUnit.toUpperCase()) {
                        return; // 선택된 단위는 제외
                    }
                    const option = document.createElement('option');
                    option.value = optionValue.toUpperCase();
                    option.text = optionValue.toUpperCase();
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

            /**
             * 클라이언트 측 유효성 검사 함수
             * @returns {boolean} - 유효성 검사 통과 여부
             */
            function validateForm() {
                var isValid = true;
                var errorMessage = '';

                // 필수 입력값 확인
                var requiredFields = [
                    {id: 'name', label: '이름'},
                    {id: 'category', label: '카테고리'},
                    {id: 'unit', label: '단위'},
                    {id: 'subUnit', label: '서브 단위'},
                    {id: 'alarmUnit', label: '알람 단위'}
                ];

                requiredFields.forEach(function (field) {
                    var element = document.getElementById(field.id);
                    if (!element.value.trim()) {
                        isValid = false;
                        errorMessage += field.label + '을(를) 입력해주세요.\n';
                    }
                });

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
             * 폼 초기화 함수
             */
            function clearForm() {
                materialForm.reset();
                // 서브 수량 비활성화 및 초기화
                subAmountInput.disabled = true;
                subAmountInput.value = '';
                // 서브 단위 및 알람 단위 옵션 초기화
                populateSelectOptions(subUnitSelect, unitCategories);
                populateSelectOptions(alarmUnitSelect, unitCategories);
            }

            /**
             * 자재 등록 함수
             * @param {Event} event - 폼 제출 이벤트
             */
            function saveMaterial(event) {
                event.preventDefault(); // 기본 폼 제출 방지

                if (!validateForm()) {
                    return; // 유효성 검사 실패 시 전송 중단
                }

                var formData = new FormData(materialForm);

                // FormData를 JSON 객체로 변환
                var data = {};
                formData.forEach(function (value, key) {
                    if (key === 'subAmount' || key === 'alarmCycle') {
                        data[key] = value ? parseFloat(value) : null;
                    } else {
                        data[key] = value.trim();
                    }
                });

                let materialName = document.getElementById('name').value;

                // 서버로 데이터 전송
                fetch('/erp/inventory/registration', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json' // JSON 형식
                    },
                    body: JSON.stringify(data)
                })
                    .then(response => response.json())
                    .then(data => {
                        if (data.success) {
                            alert('등록 성공!');
                            clearForm();
                            window.location.href = '/erp/inventory/status/' + materialName;
                        } else if(!data.fail) {
                            alert('중복된 이름의 자재가 존재합니다.');
                        }
                    })
                    .catch(function (error) {
                        // 오류 처리
                        console.error('오류:', error);
                        alert('등록 실패: ' + error.message);
                    });
            }

            /**
             * 초기 페이지 로드 시 단위, 서브 단위, 알람 단위 옵션 설정
             */
            function initializeForm() {
                // 단위 옵션 설정
                populateSelectOptions(unitSelect, unitCategories);
                // 서브 단위 옵션 설정 (기본적으로 모든 옵션)
                populateSelectOptions(subUnitSelect, unitCategories);
                // 알람 단위 옵션 설정 (기본적으로 모든 옵션)
                populateSelectOptions(alarmUnitSelect, unitCategories);
            }

            // 폼의 submit 이벤트에 saveMaterial 함수 연결
            materialForm.addEventListener('submit', saveMaterial);

            // 초기화 함수 호출
            initializeForm();

            /**
             * 단위 변경 시 서브 단위 옵션 업데이트 및 서브 수량 활성화/비활성화
             */
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

            /**
             * 서브 단위 선택 시 알람 단위 옵션 업데이트
             */
            subUnitSelect.addEventListener('change', function () {
                var selectedSubUnit = this.value.toUpperCase();
                var selectedUnit = unitSelect.value.toUpperCase();

                // 알람 단위 옵션 업데이트
                filterAlarmUnitOptions(selectedUnit, selectedSubUnit);
            });
        });
    </script>
</div>

<%@ include file="/WEB-INF/view/erp/layout/erpFooter.jsp" %>

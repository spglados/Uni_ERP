<%--
  Created by IntelliJ IDEA.
  User: namch
  Date: 24. 10. 10.
  Time: 오후 6:42
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/view/erp/layout/erpHeader.jsp" %>
<link rel="stylesheet" href="/css/erp/material.css">
<style>
    .register-container {
        display: flex;
        width: 100%;
        height: 100%;
    }
</style>

<script type="text/javascript">
    // JSP에서 unitCategories를 JSON 배열로 변환하여 JavaScript 변수에 저장
    var unitCategories = [
        <c:forEach var="unit" items="${unitCategories}" varStatus="status">
        "${unit}"<c:if test="${!status.last}">, </c:if>
        </c:forEach>
    ];
</script>

<!-- 냉동품 컨텐츠 -->
<div class="content">
    <h1>재고 등록</h1>
    <hr>

    <!-- 자재 목록 테이블 -->
    <div class="shadow p-3 mb-5 bg-white rounded" style="height: 83%; margin-top: 26px;">
        <!-- 자재 등록 폼 -->
        <form id="materialForm" action="/erp/inventory/registration" method="post">
            <div class="register-container">
                <!-- 기본 정보 섹션 -->
                <div class="form-section">
                    <div class="form-row">
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
                            <select class="form-control" id="subUnit" name="subUnit">
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
                            <label for="alarmUnit">알람 단위</label>
                            <select class="form-control" id="alarmUnit" name="alarmUnit">
                                <option value="">선택하세요</option>
                                <!-- 옵션은 JavaScript로 동적으로 추가 -->
                            </select>
                        </div>
                    </div>
                    <!-- 제출 버튼 추가 -->
                    <button type="submit" class="btn btn-primary">등록</button>
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
                    { id: 'name', label: '이름' },
                    { id: 'category', label: '카테고리' },
                    { id: 'unit', label: '단위' }
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
             * 자재 등록 함수
             * @param {Event} event - 폼 제출 이벤트
             */
            function saveMaterial(event) {
                event.preventDefault(); // 기본 폼 제출 방지

                if (!validateForm()) {
                    return; // 유효성 검사 실패 시 전송 중단
                }

                const formData = new FormData(materialForm);

                // FormData를 JSON 객체로 변환
                const data = {};
                formData.forEach((value, key) => {
                    data[key] = value;
                });

                // 서버로 데이터 전송
                fetch(materialForm.action, {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json' // JSON 형식
                    },
                    body: JSON.stringify(data)
                })
                    .then(response => {
                        if (response.ok) {
                            return response.json(); // 서버가 JSON 응답을 반환한다고 가정
                        } else {
                            throw new Error('등록 실패!');
                        }
                    })
                    .then(data => {
                        // 성공 처리
                        console.log('성공:', data);
                        alert('등록 성공!');
                        materialForm.reset();

                        // 서브 수량과 서브 단위 초기화
                        subAmountInput.disabled = true;
                        populateSelectOptions(subUnitSelect, unitCategories);

                        // 알람 단위 초기화
                        populateSelectOptions(alarmUnitSelect, unitCategories);
                    })
                    .catch(error => {
                        // 오류 처리
                        console.error('오류:', error);
                        alert('등록 실패!');
                    });
            }

            // 폼의 submit 이벤트에 saveMaterial 함수 연결
            materialForm.addEventListener('submit', saveMaterial);
        });
    </script>

    <%@ include file="/WEB-INF/view/erp/layout/erpFooter.jsp" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/view/erp/layout/erpHeader.jsp" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<link rel="stylesheet" href="/css/erp/material.css">

<!-- Main Content -->
<div class="content">
    <h1>입고 관리</h1>
    <hr>

    <!-- Material List Table -->
    <div class="shadow p-3 mb-5 bg-white rounded" style="height: 83%; margin-top: 26px;">
        <div>
            <button type="button" class="btn btn-primary" data-toggle="modal" data-target="#registerModal">입고 등록
            </button>
        </div>
        <hr>
        <div class="table-container">
            <table class="table table-bordered table-striped" id="materialList">
                <thead class="thead-dark">
                <tr>
                    <th>상품명</th>
                    <th>가격</th>
                    <th>입고량</th>
                    <th>공급처</th>
                    <th>유통기한</th>
                    <th>입고 날짜</th>
                    <th>등록 날짜</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="materialOrder" items="${materialOrderList}">
                    <tr>
                        <td>${materialOrder.name}</td>
                        <td>${materialOrder.price}원</td>
                        <td>${materialOrder.amount}&nbsp;${materialOrder.unit}</td>
                        <td>${materialOrder.supplier}</td>
                        <td>${materialOrder.expirationDate}</td>
                        <td>${materialOrder.receiptDate}</td>
                        <td>${materialOrder.enterDate}</td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </div>

</div>

<!-- Include Modal for Registration -->
<!-- 입고 등록 모달 -->
<div class="modal fade" id="registerModal" tabindex="-1" role="dialog" aria-labelledby="registerModalLabel"
     aria-hidden="true">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <!-- Form for Material Order Registration -->
            <form>
                <div class="modal-header">
                    <h5 class="modal-title" id="registerModalLabel">입고 등록</h5>
                    <button type="button" class="close" data-dismiss="modal" aria-label="닫기">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <div class="modal-body">
                    <!-- Form Fields -->
                    <div class="form-group">
                        <label for="materialName">상품명</label>
                        <input type="text" class="form-control" id="materialName" name="name" required>
                    </div>
                    <div class="form-group">
                        <label for="price">가격</label>
                        <input type="number" class="form-control" id="price" name="price" required>
                    </div>
                    <div class="form-group">
                        <label for="amount">입고량</label>
                        <input type="number" step="0.01" class="form-control" id="amount" name="amount" required>
                    </div>
                    <!-- 자재 선택 -->
                    <div class="form-group">
                        <label for="materialId">자재 선택</label>
                        <select class="form-control" id="materialId" name="materialId" required>
                            <option value="">자재 선택</option>
                            <c:forEach var="material" items="${materialDTOList}">
                                <option value="${material.id}">${material.name}</option>
                            </c:forEach>
                        </select>
                    </div>
                    <!-- 단위 선택 -->
                    <div class="form-group">
                        <label for="unit">단위</label>
                        <select class="form-control" id="unit" name="unit" required>
                        </select>
                    </div>
                    <div class="form-group">
                        <label for="supplier">공급처</label>
                        <input type="text" class="form-control" id="supplier" name="supplier" required>
                    </div>
                    <div class="form-group">
                        <label for="expirationDate">유통기한</label>
                        <input type="date" class="form-control" id="expirationDate" name="expirationDate" required>
                    </div>
                    <div class="form-group">
                        <label for="receiptDate">입고 날짜</label>
                        <input type="date" class="form-control" id="receiptDate" name="receiptDate" required>
                    </div>
                    <input type="hidden" name="isUse" value="true">
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-dismiss="modal">취소</button>
                    <button type="button" onclick="enterMaterialOrder()" class="btn btn-primary">등록</button>
                </div>
            </form>
        </div>
    </div>
</div>

<script>

    const expirationDate = document.getElementById('expirationDate');
    const receiveDate = document.getElementById('receiptDate');
    const materialName = document.getElementById('materialName');
    const price = document.getElementById('price');
    const amount = document.getElementById('amount');
    const materialId = document.getElementById('materialId');
    const unit = document.getElementById('unit');
    const supplier = document.getElementById('supplier');

    var materialsData = [
        <c:forEach var="material" items="${materialDTOList}" varStatus="status">
        {
            id: ${material.id},
            name: "${material.name}",
            unit: "${material.unit}",
            subUnit: "${material.subUnit}"
        }<c:if test="${!status.last}">, </c:if>
        </c:forEach>
    ];

    function updateUnitOptions() {
        var materialSelect = document.getElementById('materialId');
        var unitSelect = document.getElementById('unit');

        var selectedMaterialId = materialSelect.value;

        unitSelect.innerHTML = '';

        // 입고 날짜 오늘로 기본 세팅
        receiveDate.value = getTodayDate();

        var defaultOption = document.createElement('option');
        defaultOption.value = '';
        defaultOption.text = '단위 선택';
        unitSelect.appendChild(defaultOption);

        if (selectedMaterialId) {
            var selectedMaterial = materialsData.find(function (material) {
                return material.id == selectedMaterialId;
            });

            if (selectedMaterial) {
                var unitOption = document.createElement('option');
                unitOption.value = selectedMaterial.unit;
                unitOption.text = selectedMaterial.unit;
                unitSelect.appendChild(unitOption);

            }
        }
    }

    document.getElementById('materialId').addEventListener('change', updateUnitOptions);

    window.addEventListener('load', function () {
        updateUnitOptions();
    });

    // 유효성 검사 함수 추가
    function validateForm() {
        // 상품명 확인
        if (materialName.value.trim() === '') {
            alert('상품명을 입력해주세요.');
            materialName.focus();
            return false;
        }

        // 가격 확인
        if (price.value === '' || price.value <= 0) {
            alert('유효한 가격을 입력해주세요.');
            price.focus();
            return false;
        }

        // 입고량 확인
        if (amount.value === '' || amount.value <= 0) {
            alert('유효한 입고량을 입력해주세요.');
            amount.focus();
            return false;
        }

        // 자재 선택 확인
        if (materialId.value === '') {
            alert('자재를 선택해주세요.');
            materialId.focus();
            return false;
        }

        // 단위 선택 확인
        if (unit.value === '') {
            alert('단위를 선택해주세요.');
            unit.focus();
            return false;
        }

        // 공급처 확인
        if (supplier.value.trim() === '') {
            alert('공급처를 입력해주세요.');
            supplier.focus();
            return false;
        }

        // 유통기한 확인
        if (expirationDate.value === '') {
            alert('유통기한을 선택해주세요.');
            expirationDate.focus();
            return false;
        }

        // 유통기한이 오늘 이후인지 확인
        if (!checkExpirationDate(expirationDate.value)) {
            expirationDate.focus();
            return false;
        }

        // 입고 날짜 확인
        if (receiveDate.value === '') {
            alert('입고 날짜를 선택해주세요.');
            receiveDate.focus();
            return false;
        }

        // 입고 날짜가 미래인지 확인
        if (!checkReceiveDate(receiveDate.value)) {
            receiveDate.focus();
            return false;
        }

        return true; // 모든 검사를 통과하면 true 반환
    }

    function enterMaterialOrder() {
        let form = document.querySelector('form');
        let formData = new FormData(form);

        // 유효성 검사 실행
        if (!validateForm()) {
            return; // 유효성 검사 실패 시 함수 종료
        }

        fetch('/erp/inventory/receiving', {
            method: 'POST',
            body: formData
        })
            .then(response => {
                if (response.ok) {
                    alert('성공적으로 저장되었습니다 !');
                    window.location.reload();
                } else {
                    alert('저장 도중 오류가 발생했습니다. \n\n\t 입고 내역을 다시 확인해주세요 !');
                }
            })
            .catch(error => {
                console.log('error', error);
                alert('저장에 실패했습니다.');
            });

    }

    function getTodayDate() {
        const today = new Date();
        const year = today.getFullYear();
        const month = String(today.getMonth() + 1).padStart(2, '0'); // 월은 0부터 시작하므로 +1
        const day = String(today.getDate()).padStart(2, '0');
        return year + '-' + month + '-' + day;
    }

    // 유통기한 검사 함수 수정
    function checkExpirationDate(inputDateValue) {
        const today = new Date();
        today.setHours(0, 0, 0, 0); // 오늘 날짜의 시간 초기화 (자정으로 설정)

        const targetDate = new Date(inputDateValue);
        targetDate.setHours(0, 0, 0, 0); // 입력 날짜의 시간 초기화

        if (targetDate <= today) {
            alert("유통기한은 오늘 이후 날짜여야 합니다.");
            return false;
        }
        return true;
    }

    // 입고 날짜 검사 함수 추가
    function checkReceiveDate(inputDateValue) {
        const today = new Date();
        today.setHours(0, 0, 0, 0); // 오늘 날짜의 시간 초기화

        const targetDate = new Date(inputDateValue);
        targetDate.setHours(0, 0, 0, 0); // 입력 날짜의 시간 초기화

        if (targetDate > today) {
            alert("입고 날짜는 오늘 또는 이전 날짜여야 합니다.");
            return false;
        }
        return true;
    }

</script>


<%@ include file="/WEB-INF/view/erp/layout/erpFooter.jsp" %>

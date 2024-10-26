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
        <div class="d-flex justify-content-between">
            <div class="d-flex justify-content-between">
                <select id="categoryFilter" class="form-control select" style="margin-right: 30px;">
                    <option value="전체">전체</option>
                    <option value="상품명">상품명</option>
                    <option value="가격">가격</option>
                    <option value="입고량">입고량</option>
                    <option value="공급처">공급처</option>
                    <option value="유통기한">유통기한</option>
                    <option value="입고 날짜">입고 날짜</option>
                    <option value="등록 날짜">등록 날짜</option>
                    <option value="상태">상태</option>
                </select>
                <input id="searchInput" placeholder="내역 검색" class="form-control mr-2" style="width: 200px;">
            </div>
            <div>
                <!-- Button to trigger modal -->
                <button type="button" class="btn btn-primary" data-toggle="modal" data-target="#registerModal">입고 등록
                </button>
            </div>
        </div>
        <hr>
        <div class="table-container">
            <!-- Material List Table -->
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
                        <input type="date" class="form-control" id="receiptDate" name="receiptDate"
                               value="<fmt:formatDate value='${today}' pattern='yyyy-MM-dd'/>" required>
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

    function enterMaterialOrder() {
        let form = document.querySelector('form');
        let formData = new FormData(form);

        fetch('/erp/inventory/receiving', {
            method: 'POST',
            body: formData
        })
            .then(response => {
                if(response.ok) {
                    alert('성공적으로 저장되었습니다 !');
                    window.location.reload();
                }
            })
            .catch(error => {
               console.log('error', error);
               alert('저장에 실패했습니다.');
            });

    }
</script>

<%@ include file="/WEB-INF/view/erp/layout/erpFooter.jsp" %>

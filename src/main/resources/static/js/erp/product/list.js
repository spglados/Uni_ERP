// 재료 보기 모달을 열 때 선택된 상품의 재료 정보를 표시하는 함수
let ingredients = null;
let canSubmit = true;  // 요청이 가능한지 여부를 확인하는 플래그
const submissionTerm = 2000;  // 2초(2000ms) 동안 재요청 차단

// 재료 보기 모달을 닫는 함수
// 재료를 추가하는 함수
function addIngredient() {
    const ingredientList = document.getElementById('ingredientList');
    const productId = document.getElementById('modalProductId').value;
    const index = 999_999_999; // 임시 index 값
    const li = document.createElement('li');

    li.innerHTML =
        '<div class="ingredient-item" id="ingredient-' + index + '">' +
        '<input type="text" id="ingredientInput-' + index + '" class="ingredient-name form-control d-inline-block" name="name" required oninput="filterMaterials(this, ' + index + ')" onclick="showDropdown(' + index + ')">' +
        '<ul id="dropdown-' + index + '" class="dropdown-menu"></ul>' +
        '<input type="number" class="ingredient-amount form-control d-inline-block" name="amount" required>' +
        '<select class="ingredient-unit form-control d-inline-block" name="unit">' +
        '<option value="G">g</option>' +
        '<option value="KG">kg</option>' +
        '<option value="ML">ml</option>' +
        '<option value="L">L</option>' +
        '<option value="EA">EA</option>' +
        '<option value="BOX">box</option>' +
        '</select>' +
        '<button class="custom-btn register-btn" style="margin-right: 10px;" name="add-btn" onclick="registerIngredient(' + index + ', ' + productId + ')">등록</button>' +
        '<button class="custom-btn cancel-btn" style="margin-right: 10px;" name="delete-btn" onclick="cancelIngredient(' + index + ')">취소</button>' +
        '<button class="custom-btn" id="add-new-material-btn-' + index + '" style="display:none;" onclick="goToAddMaterialPage()">추가</button>' +
        '</div>';

    ingredientList.appendChild(li);
}



// 추가 재료 등록
function registerIngredient(index, productId) {
    const ingredientList = document.getElementById('ingredientList');
    const ingredientItem = document.getElementById('ingredient-' + index);
    // 해당 input 필드에서 값을 가져오기
    const name = ingredientItem.querySelector('input[name="name"]').value;
    const amount = ingredientItem.querySelector('input[name="amount"]').value;
    const unit = ingredientItem.querySelector('select[name="unit"]').value;

    // 이미 등록된 자재가 있는지 확인하는 로직
    let isDuplicate = false;
    $('#ingredientList .ingredient-item').each(function() {
        const existingName = $(this).find('input[name="name"]').val();
        // 현재 등록 중인 자재를 제외하고 중복을 검사
        if (existingName === name && $(this).attr('id') !== 'ingredient-' + index) {
            isDuplicate = true;  // 중복된 자재가 발견되면 플래그 설정
        }
    });
    if (isDuplicate === true) {
        alert('이미 등록되어 있습니다.');
        isDuplicate = false;
        return;
    }


    // materialList에 있는지 확인하는 로직
    if (!materialList.includes(name)) {
        alert('해당 자재는 목록에 없습니다.\n\t 자재를 먼저 등록해주세요 !');
        return;
    }

    if(!checkUnit(name, unit)) {
        return;
    }

    if (!name || name.trim() === '') {
        alert('재료명을 기입해주세요.');
        return;
    }

    if (!amount || amount.trim() === '') {
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

    if(confirm("등록하시겠습니까?")) {
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
                if(response.status === 200) {
                    alert('등록되었습니다.');

                    // 모달 닫기
                    $('#ingredientModal').modal('hide');

                    // 모달이 완전히 닫혔을 때 실행
                    $('#ingredientModal').on('hidden.bs.modal', function () {
                        // modal-backdrop 요소 제거
                        $('.modal-backdrop').remove();

                        // 약간의 지연을 두고 모달을 다시 열기 위해 모달 여는 버튼을 클릭
                        setTimeout(function() {
                            document.querySelector('#modal-btn-' + productId).click();
                        }, 100);  // 300ms 지연 후 모달 여는 버튼 클릭

                        // 이벤트 리스너 제거 (다음 모달 닫기 시 중복 방지)
                        $('#ingredientModal').off('hidden.bs.modal');
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
    const li = ingredientItem.parentNode; // li 태그
    li.parentNode.removeChild(li); // li 태그 삭제
}


// 재료 정보를 수정하는 함수
function editIngredient(index) {
    const ingredientItem = document.getElementById('ingredient-' + index);
    const inputs = ingredientItem.querySelectorAll('input, select');
    const editButton = ingredientItem.querySelector('.edit-btn');
    const name = ingredientItem.querySelector('input[name="name"]').value;
    const amount = ingredientItem.querySelector('input[name="amount"]').value;
    const unit = ingredientItem.querySelector('select[name="unit"]').value;
    const data = {
        name: name,
        amount: amount,
        unit: unit,
        productId: index
    };

    if(!checkUnit(name, unit)) {
        return;
    }

    if (editButton.textContent === '수정') {
        inputs.forEach(function(input) { input.disabled = false; });
        editButton.textContent = '저장';
        editButton.classList.remove('custom-btn-warning');
        editButton.classList.add('custom-btn-success');
    } else {
        // 저장하는 fetch
        fetch('/erp/product/ingredient', {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(data)
        })
            .then(response => {
                if(response.status === 200) {
                    return response.json();
                }
            })
            .then(data => {
                console.log('data', data);
            })
            .catch(error => {
                throw new Error('수정 중에 오류가 발생했습니다.');
            });
        inputs.forEach(function(input) { input.disabled = true; });
        editButton.textContent = '수정';
        editButton.classList.remove('custom-btn-success');
        editButton.classList.add('custom-btn-warning');
    }

}

// 재료를 삭제하는 함수
function deleteIngredient(index) {
    const confirmDelete = confirm("정말 삭제하시겠습니까?");
    if (confirmDelete) {
        fetch('/erp/product/ingredient/' + index, {
            method: 'DELETE'
        })
            .then(response => {
                if(response.status === 200) {
                    alert('삭제되었습니다.');
                }
            })
            .catch(error => {
                throw new Error('알 수 없는 오류 발생');
            });
        const ingredientItem = document.getElementById('ingredient-' + index);
        if (ingredientItem && ingredientItem.parentNode && ingredientItem.parentNode.tagName === 'LI') {
            const li = ingredientItem.parentNode; // li 태그
            li.parentNode.removeChild(li); // li 태그 삭제
        } else {
            console.error("재료 항목을 찾을 수 없거나 이미 삭제되었습니다.");
        }
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
        li.onclick = function() {
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


// input에서 oninput 이벤트가 발생할 때마다 호출됨
function filterMaterials(input, index) {
    updateDropdown(input, index);
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

// 이미지 미리보기 기능
function previewImage(event) {
    const reader = new FileReader();
    reader.onload = function () {
        const output = document.getElementById('imagePreview');
        output.src = reader.result;
    };
    reader.readAsDataURL(event.target.files[0]);
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

        // 여기서 서버로 formData를 전송하는 로직을 구현
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

document.addEventListener('DOMContentLoaded', function() {
    const categoryFilter = document.getElementById('categoryFilter');
    const categoryTitle = document.getElementById('categoryTitle');
    const searchInput = document.getElementById('searchInput');
    const searchButton = document.getElementById('searchButton');

    // 카테고리 선택 시 필터링 및 검색어 비우기
    categoryFilter.addEventListener('change', function() {
        var selectedCategory = categoryFilter.value;
        categoryTitle.textContent = selectedCategory;
        searchInput.value = ''; // 카테고리 변경 시 검색어 비우기
        filterProducts();
    });

    // 엔터 키 입력 시 필터링
    searchInput.addEventListener('keyup', function(event) {
        if (event.key === 'Enter') {
            filterProducts();
        }
    });

    searchInput.addEventListener('input', function() {
        filterProducts();
    });

    // 서버에서 보낸 productName이 존재하면 검색창에 삽입하고 검색 수행
    if (window.productName && window.productName.trim() !== '') {
        searchInput.value = window.productName;
        filterProducts();
    }

    // 상품 필터링 함수 (카테고리와 검색어 모두 적용)
    function filterProducts() {
        var selectedCategory = categoryFilter.value;
        var searchKeyword = searchInput.value.toLowerCase(); // 검색어를 소문자로 변환

        const productList = document.querySelectorAll('#productList tr');

        productList.forEach(function(row) {
            var productCategoryCell = row.querySelector('td:nth-child(3)');
            var productNameCell = row.querySelector('td:nth-child(2)'); // 상품명 있는 열 선택
            if (productCategoryCell && productNameCell) {
                var productCategory = productCategoryCell.textContent.trim();
                var productName = productNameCell.textContent.trim().toLowerCase(); // 상품명 소문자로 변환

                // 카테고리와 검색어가 모두 일치해야 보이게 함
                var categoryMatch = selectedCategory === '전체' || productCategory === selectedCategory;
                var searchMatch = productName.includes(searchKeyword);

                if (categoryMatch && searchMatch) {
                    row.style.display = ''; // 카테고리와 검색어가 일치하는 경우 보이기
                } else {
                    row.style.display = 'none'; // 일치하지 않는 경우 숨기기
                }
            }
        });
    }
});

// 특정 자재의 unit을 찾는 함수
function getUnitByName(materialName) {
    const material = materialDTOList.find(item => item.name === materialName);
    return material ? material.unit : null; // 자재가 존재하면 unit 반환, 없으면 null 반환
}

function getSubUnitByName(materialName) {
    const material = materialDTOList.find(item => item.name === materialName);
    return material ? material.subUnit : null; // 자재가 존재하면 unit 반환, 없으면 null 반환
}

function checkUnit(name, unit) {
    const materialName = getUnitByName(name).toLowerCase();
    const effectivenessUnit = unit.toUpperCase();

    let materialUnit = getUnitByName(name);
    let materialSubUnit = getSubUnitByName(name);

        if (materialUnit === effectivenessUnit || materialSubUnit === effectivenessUnit) {
            return true;
        } else {
            alert(`${name}의 단위는 [` + materialUnit + '] 또는 [' + materialSubUnit + '] 만 사용 가능합니다.');
            return false;
        }
}
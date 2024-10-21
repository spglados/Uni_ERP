<!DOCTYPE html>
<html lang="en">
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!-- header.jsp  -->
<%@include file="/WEB-INF/view/layout/header.jsp"%>

<div class="container">

	<!-- 검색 폼 -->
	<form id="searchForm" class="d-flex">
		<div class="form-group p-2">
			<select class="form-control" id="searchRange" name="searchRange">
				<option value="" disabled selected>취소사유</option>
				<option value="simple">단순변심</option>
				<option value="cancelSubscribe">구독취소</option>
				<option value="portfolio">포트폴리오</option>
				<option value="doublePay">중복결제</option>
			</select>
		</div>
		<div class="form-group p-2 flex-fill">
			<input type="search" class="form-control" id="searchContents"
				name="searchContents" placeholder="검색할 결제키를 입력하세요.">
		</div>
		<button type="submit" class="btn btn-outline-info p-2">검색</button>
	</form>

	<div id="" class="container text-center">
		<table class="table table-hover">
			<caption style="caption-side: top">취소 내역</caption>
			<thead>
				<tr>
					<th>번호</th>
					<th>결제키</th>
					<th>취소 사유</th>
					<th>요청일</th>
					<th>승인일</th>
					<th>금액</th>
					<th>관리자</th>
				</tr>
			</thead>
			<tbody id="refundList">
				<!-- 데이터가 동적으로 삽입되는 구역 -->
			</tbody>
		</table>

		<!-- Pagination -->
		<div id="paginationContainer" class="d-flex justify-content-center">
			<!-- 페이지네이션이 동적으로 삽입되는 구역 -->
		</div>

	</div>

</div>

<script>
//페이지가 로드될 때 데이터 불러오기
document.addEventListener('DOMContentLoaded', () => {
    fetchPage(1, 10); // 페이지 1, 사이즈 10으로 초기 데이터 로드
});

//검색 폼의 제출 이벤트 처리
   document.getElementById('searchForm').addEventListener('submit', function(event) {
            event.preventDefault(); // 폼 제출 시 페이지의 새로고침, 서버로 데이터 전송 방지

            const searchRange = document.getElementById('searchRange').value;
            const searchContents = document.getElementById('searchContents').value;

            fetchPage(1, 10, searchRange, searchContents); // 페이지 1, 사이즈 10으로 검색
        });

   // 페이지를 불러오는 함수
   function fetchPage(page, size, searchRange = '', searchContents = '') {

   	// 전체 리스트 조회 시 URL
   	let fetchUrl = `http://localhost:8080/pay/searchPaymentList?type=refundList&page=` + page + `&size=` + size;

       // 검색 시 URL
        if (searchRange) {
           fetchUrl += `&searchRange=` + encodeURIComponent(searchRange);
       }
       if (searchContents) {
           fetchUrl += `&searchContents=` + encodeURIComponent(searchContents);
       }

       fetch(fetchUrl)
           .then(response => response.json())
           .then(data => {
        	   console.log(data);
        	   renderRefundList(data.refundList); // 공지사항 목록 렌더링
               renderPagination(data.totalCount, data.currentPage, data.pageSize, data.totalPages, searchContents); // 페이징 처리 렌더링
           })
           .catch(error => {
               console.error('Error:', error);
           });
   }

   // 공지사항 목록을 렌더링하는 함수
   function renderRefundList(refundList) {
       const refundListContainer = document.getElementById('refundList');

       // 기존 공지사항 항목 제거 - 새 데이터 추가 시 중복 방지
       while (refundListContainer.firstChild) {
    	   refundListContainer.removeChild(refundListContainer.firstChild);
       }
   // 공지사항 항목을 생성하고 컨테이너에 추가
   refundList.forEach(refundList => {
       // 행(tr) 요소 생성
       const tr = document.createElement('tr');

       // 각 셀(td) 요소 생성 및 추가
       const idCell = document.createElement('td');
       idCell.textContent = refundList.id;
       tr.appendChild(idCell);

       const paymentKeyCell = document.createElement('td');
       paymentKeyCell.textContent = refundList.paymentKey;
       tr.appendChild(paymentKeyCell);

       const cancelReasonCell = document.createElement('td');
       cancelReasonCell.textContent = refundList.cancelReason;
       tr.appendChild(cancelReasonCell);

       const requestedAtCell = document.createElement('td');
       requestedAtCell.textContent = refundList.requestedAt;
       tr.appendChild(requestedAtCell);

       const approvedAtCell = document.createElement('td');
       approvedAtCell.textContent = refundList.approvedAt;
       tr.appendChild(approvedAtCell);

       const cancelAmountCell = document.createElement('td');
       cancelAmountCell.textContent = refundList.cancelAmount;
       tr.appendChild(cancelAmountCell);

       const adminIdCell = document.createElement('td');
       adminIdCell.textContent = refundList.adminId;
       tr.appendChild(adminIdCell);

       // 완성된 행을 공지사항 목록 컨테이너에 추가
       refundListContainer.appendChild(tr);
   });
   }


//페이지네이션을 렌더링하는 함수
function renderPagination(totalCount, currentPage, pageSize, totalPages, searchContents = '') {

	 const paginationContainer = document.getElementById('paginationContainer');

	    // 기존 페이지네이션 항목 제거
	    while (paginationContainer.firstChild) {
	        paginationContainer.removeChild(paginationContainer.firstChild);
	    }

	    // 페이지네이션 리스트 생성
	    const ul = document.createElement('ul');
	    ul.className = 'pagination';

	    // 이전 페이지 링크
	    const prevLi = document.createElement('li');
	    prevLi.className = currentPage > 1 ? 'page-item' : 'page-item disabled';
	    const prevLink = document.createElement('a');
	    prevLink.className = 'page-link';
	    prevLink.textContent = 'Previous';
	    prevLink.addEventListener('click', (event) => {
	        event.preventDefault(); // 페이지 이동 및 새로 고침 방지
	        if (currentPage > 1) {fetchPage(currentPage - 1, pageSize, categories, searchRange,searchContents)};
	    });
	    prevLi.appendChild(prevLink);
	    ul.appendChild(prevLi);

	    // 페이지 번호 링크
	    for (let i = 1; i <= totalPages; i++) {
	        const li = document.createElement('li');
	        li.className = i === currentPage ? 'page-item active' : 'page-item';
	        const link = document.createElement('a');
	        link.className = 'page-link';
	        link.textContent = i;
	        link.addEventListener('click', (event) => {
	            event.preventDefault(); // 페이지 이동 및 새로 고침 방지
	            fetchPage(i, pageSize, categories, searchRange,searchContents);
	        });
	        li.appendChild(link);
	        ul.appendChild(li);
	    }

	    // 다음 페이지 링크
	    const nextLi = document.createElement('li');
	    nextLi.className = currentPage < totalPages ? 'page-item' : 'page-item disabled';
	    const nextLink = document.createElement('a');
	    nextLink.className = 'page-link';
	    nextLink.textContent = 'Next';
	    nextLink.addEventListener('click', (event) => {
	        event.preventDefault(); // 페이지 이동 및 새로 고침 방지
	        if (currentPage < totalPages) {fetchPage(currentPage + 1, pageSize, categories, searchRange, searchContents)};
	    });
	    nextLi.appendChild(nextLink);
	    ul.appendChild(nextLi);

	    // 완성된 페이지네이션을 페이지네이션 컨테이너에 추가
	    paginationContainer.appendChild(ul);
}
</script>

<!-- footer.jsp  -->
<%@include file="/WEB-INF/view/layout/footer.jsp"%>
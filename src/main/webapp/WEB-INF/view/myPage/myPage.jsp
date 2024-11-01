<!DOCTYPE html>
<html lang="ko">
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/view/layout/header.jsp"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>마이페이지</title>
    <style>
        .sidebar {
            width: 200px;
            float: left;
            margin-right: 20px;
            border-right: 1px solid #ccc;
            padding: 10px;
        }
        .sidebar a {
            display: block;
            margin: 10px 0;
            text-decoration: none;
            color: #333;
        }
        .sidebar a:hover {
            color: #007bff;
        }
        .profile-info {
            overflow: hidden;
        }
        table {
            width: 100%;
            border-collapse: collapse;
        }
        th, td {
            border: 1px solid #ccc;
            padding: 10px;
            text-align: left;
        }
        th {
            background-color: #f2f2f2;
        }
        .hidden {
            display: none;
        }
    </style>
</head>
<body>
    <div class="sidebar">
        <h3>내 정보</h3>
        <a href="/myPage">회원 정보 및 수정</a>
        <a href="/myPage/storeList">가게 등록</a>
        <a href="/myPage/paymentHistory">결제 내역</a>
        <a href="/myPage/refundHistory">환불 내역</a>
        <a href="/myPage/contact">내 문의 내역</a>
    </div>

    <h1>마이페이지</h1>
    <div class="profile-info">
        <table>
            <tr>
                <th>항목</th>
                <th>정보</th>
            </tr>
            <tr>
                <td>이름</td>
                <td>${user.name}</td>
            </tr>
            <tr>
                <td>이메일</td>
                <td>
                    <span id="emailDisplay">${user.email}</span>
                    <span id="emailEdit" class="hidden">
                        <input type="text" id="newEmail" value="${user.email}" />
                        <button onclick="confirmEdit()">확인</button>
                        <button onclick="cancelEdit()">취소</button>
                    </span>
                    <button id="editButton" onclick="editEmail()">수정하기</button>
                </td>
            </tr>
            <tr>
                <td>가입일</td>
                <td><fmt:formatDate value="${user.createdAt}" pattern="yyyy-MM-dd" /></td>
            </tr>
            <tr>
                <td>전화번호</td>
                <td>
                    <span id="phoneDisplay">${user.phone}</span>
                    <span id="phoneEdit" class="hidden">
                        <input type="tel" id="newPhone" value="${user.phone}" onfocus="this.placeholder=''" maxlength="13" oninput="autoHyphen(this)" placeholder="휴대폰 번호" autocomplete="off" name="users_phone" />
                        <button onclick="confirmPhoneEdit()">확인</button>
                        <button onclick="cancelPhoneEdit()">취소</button>
                    </span>
                    <button id="editPhoneButton" onclick="editPhone()">수정하기</button>
                </td>
            </tr>
            <tr>
                <td>주소</td>
                <td>
                    <span id="addressDisplay">${user.address}</span>
                    <span id="addressEdit" class="hidden">
                        <input type="text" id="newAddress" value="${user.address}" style="width: 300px;" placeholder="주소 입력" /><br>
                        <input type="text" id="newDetailAddress" value="" placeholder="상세 주소 입력" />
                        <button onclick="confirmAddressEdit()">확인</button>
                        <button onclick="cancelAddressEdit()">취소</button>
                        <input type="button" class="check--btn" onclick="execDaumPostcode()" value="주소 검색" style="display: none;" id="searchAddressButton">
                    </span>
                    <button id="editAddressButton" onclick="editAddress()">수정하기</button>
                </td>
            </tr>

            <tr>
                <td>구독 상태</td>
                <td>${user.membership}</td>
            </tr>
            <tr>
                <td>정기 결제일</td>
                <td>
                    <c:choose>
                        <c:when test="${not empty user.paymentDate}">
                            <span id="paymentDateDisplay">${user.paymentDate}</span>
                            <span id="paymentDateEdit" class="hidden">
                                <select id="newPaymentDate">
                                    <c:forEach var="day" begin="1" end="31">
                                        <option value="${day}" >${day}</option>
                                    </c:forEach>
                                </select>
                                <button onclick="confirmPaymentDateEdit()">확인</button>
                                <button onclick="cancelPaymentDateEdit()">취소</button>
                            </span>
                            <button id="editPaymentDateButton" onclick="editPaymentDate()">수정하기</button>
                        </c:when>
                        <c:otherwise>
                            -
                        </c:otherwise>
                    </c:choose>
                </td>
            </tr>
        </table>
    </div>
</body>
<%@include file="/WEB-INF/view/layout/footer.jsp"%>

<script src="//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>
<script src="//dapi.kakao.com/v2/maps/sdk.js?appkey=3aea5e049cf7a27eae091e77ca0e1429&libraries=services"></script>

<script>

   function execDaumPostcode() {
       new daum.Postcode({
           oncomplete: function(data) {
               var addr = data.address;
               document.getElementById("newAddress").value = addr; // 선택한 주소를 newAddress 필드에 넣기
           }
       }).open();
   }

      function editAddress() {
          document.getElementById('addressDisplay').classList.add('hidden');
          document.getElementById('addressEdit').classList.remove('hidden');
          document.getElementById('editAddressButton').style.visibility = 'hidden'; // 수정하기 버튼 숨기기
          document.getElementById('searchAddressButton').style.display = 'inline-block'; // 주소 검색 버튼 보이기

           const fullAddress = '${user.address}';
           const addressParts = fullAddress.split(',');


          // 기존 주소와 상세 주소를 입력 필드에 설정
          document.getElementById('newAddress').value = addressParts[0].trim();;
          document.getElementById('newDetailAddress').value = addressParts.length > 1 ? addressParts[1].trim() : ''; // 상세 주소는 빈칸으로 초기화 (필요에 따라 조정)

           document.getElementById('newAddress').disabled = true;
      }

      function cancelAddressEdit() {
          document.getElementById('addressDisplay').classList.remove('hidden');
          document.getElementById('addressEdit').classList.add('hidden');
          document.getElementById('editAddressButton').style.visibility = 'visible'; // 수정하기 버튼 다시 보이기
          document.getElementById('searchAddressButton').style.display = 'none'; // 주소 검색 버튼 숨기기
      }


      function confirmAddressEdit() {
              const newAddress = document.getElementById('newAddress').value;
              const newDetailAddress = document.getElementById('newDetailAddress').value;

              if (!newAddress) {
                      alert('주소를 선택해 주세요.');
                      return;
                  }

               // 주소와 상세 주소를 합칩니다.
               const fullAddress = newAddress + ' ,' + newDetailAddress;

              if (confirm('주소를 변경하시겠습니까?')) {
                  fetch('/myPage/updateAddress', {
                      method: 'POST',
                      headers: {
                          'Content-Type': 'application/json'
                      },
                      body: JSON.stringify({ address: fullAddress })
                  })
                  .then(response => {
                      if (response.ok) {
                          document.getElementById('addressDisplay').innerText = fullAddress; // 주소 표시 업데이트
                          cancelAddressEdit(); // 수정 후에는 취소 버튼으로 돌아갑니다.
                      } else {
                          alert('주소 업데이트에 실패했습니다.');
                      }
                  })
                  .catch(error => {
                      console.error('Error:', error);
                      alert('서버 오류가 발생했습니다.');
                  });
              }
          }




    function editEmail() {
        document.getElementById('emailDisplay').classList.add('hidden');
        document.getElementById('emailEdit').classList.remove('hidden');
        document.getElementById('editButton').style.visibility = 'hidden'; // 수정하기 버튼 숨기기
    }



        function confirmEdit() {
            const newEmail = document.getElementById('newEmail').value;

            // 이메일 유효성 검사 (한글 포함 불가)
            const emailPattern = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
            if (!emailPattern.test(newEmail)) {
                alert('유효한 이메일 형식을 입력하세요. (한글은 포함할 수 없습니다.)');
                return;
            }

            if (confirm('이메일을 변경하시겠습니까?')) {
                fetch('/myPage/updateEmail', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify({ email: newEmail })
                })
                .then(response => {
                    if (response.ok) {
                        document.getElementById('emailDisplay').innerText = newEmail;
                        cancelEdit(); // 수정 후에는 취소 버튼으로 돌아갑니다.
                    } else {
                        alert('이메일 업데이트에 실패했습니다.');
                    }
                })
                .catch(error => {
                    console.error('Error:', error);
                    alert('서버 오류가 발생했습니다.');
                });
            }
        }

    function cancelEdit() {
        document.getElementById('emailDisplay').classList.remove('hidden');
        document.getElementById('emailEdit').classList.add('hidden');
        document.getElementById('editButton').style.visibility = 'visible'; // 수정하기 버튼 다시 보이기
    }

    function editPhone() {
        document.getElementById('phoneDisplay').classList.add('hidden');
        document.getElementById('phoneEdit').classList.remove('hidden');
        document.getElementById('editPhoneButton').style.visibility = 'hidden'; // 수정하기 버튼 숨기기
    }

   function confirmPhoneEdit() {
       const newPhone = document.getElementById('newPhone').value;

       // 전화번호 유효성 검사
       const phonePattern = /^010-\d{4}-\d{4}$/;
       if (!phonePattern.test(newPhone)) {
           alert('유효한 전화번호를 입력하세요.');
           return;
       }

       if (confirm('전화번호를 변경하시겠습니까?')) {
           fetch('/myPage/updatePhone', {
               method: 'POST',
               headers: {
                   'Content-Type': 'application/json'
               },
               body: JSON.stringify({ phone: newPhone })
           })
           .then(response => {
               if (response.ok) {
                   document.getElementById('phoneDisplay').innerText = newPhone;
                   cancelPhoneEdit(); // 수정 후에는 취소 버튼으로 돌아갑니다.
               } else {
                   alert('전화번호 업데이트에 실패했습니다.');
               }
           })
           .catch(error => {
               console.error('Error:', error);
               alert('서버 오류가 발생했습니다.');
           });
       }
   }

    function cancelPhoneEdit() {
        document.getElementById('phoneDisplay').classList.remove('hidden');
        document.getElementById('phoneEdit').classList.add('hidden');
        document.getElementById('editPhoneButton').style.visibility = 'visible'; // 수정하기 버튼 다시 보이기
    }

    function autoHyphen(input) {
        let value = input.value.replace(/-/g, '').replace(/[^0-9]/g, '');

        if (value.length < 4) {
            input.value = value;
        } else if (value.length < 8) {
            input.value = value.replace(/(\d{3})(\d{0,4})/, '$1-$2');
        } else {
            input.value = value.replace(/(\d{3})(\d{4})(\d{0,4})/, '$1-$2-$3');
        }
    }

    document.getElementById('newPhone').addEventListener('focusout', function() {
        let value = this.value.replace(/-/g, '').replace(/[^0-9]/g, '');
        if (value.length >= 10) {
            this.value = value.replace(/(\d{3})(\d{4})(\d{4})/, '$1-$2-$3');
        }
    });

    function editPaymentDate() {
        document.getElementById('paymentDateDisplay').classList.add('hidden');
        document.getElementById('paymentDateEdit').classList.remove('hidden');
        document.getElementById('editPaymentDateButton').style.visibility = 'hidden'; // 수정하기 버튼 숨기기
    }

    function cancelPaymentDateEdit() {
        document.getElementById('paymentDateDisplay').classList.remove('hidden');
        document.getElementById('paymentDateEdit').classList.add('hidden');
        document.getElementById('editPaymentDateButton').style.visibility = 'visible'; // 수정하기 버튼 다시 보이기
    }

    function confirmPaymentDateEdit() {
        const newPaymentDate = document.getElementById('newPaymentDate').value; // 입력값 가져오기

        if (confirm('결제일을 변경하시겠습니까?')) {
            fetch('/myPage/updatePaymentDate', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ paymentDate: newPaymentDate })
            })
            .then(response => {
                if (response.ok) {
                    document.getElementById('paymentDateDisplay').innerText = newPaymentDate; // 결제일 표시 업데이트
                    window.location.reload(); // 페이지 리로드
                    cancelPaymentDateEdit(); // 수정 후에는 취소 버튼으로 돌아갑니다.
                } else {
                    alert('결제일 업데이트에 실패했습니다.');
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert('서버 오류가 발생했습니다.');
            });
        }
    }


</script>

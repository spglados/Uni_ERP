
# < ⚙️UNI-ERP : 소상공인 대상 ERP 사이트  >
### Springboot와 JPA를 사용한 소상공인 대상 ERP 사이트
&nbsp;
&nbsp; 
![logo](https://github.com/user-attachments/assets/9608567a-f41e-4bca-ac1a-87bc81338aef)

* 유튜브 시연 영상 : https://www.youtube.com/watch?v=XGrV_bXjmmg
* 노션 : https://www.notion.so/unierp/

&nbsp;
### 목차
1. 프로젝트 개요
2. 구성원 및 맡은 역할
3. 서비스 환경
4. 사용 라이브러리 및 외부 API
5. 사이트 맵 (유저, 관리자)
6. 주요 기능
7. ERD 다이어그램
&nbsp; &nbsp;&nbsp;
## 1️⃣ 프로젝트 개요
### (1) 프로젝트 주제 및 목적
* Springboot와 h2-console을 사용한 
* 인사, 재고, 상품, 매출을 ERP에서 관리하고 POS로 구현한 소상공인 대상 ERP 웹사이트 제작

### (2) 프로젝트 핵심 기능
* 재고관리 - 유통기한 확인, 이론재고와 실재고 비교, 폐기등록 기능
* 상품관리 - 상품에 따른 재고 수정, 등록 기능
* 인사관리 - 직원정보 엑셀파일 출력, 근무 일정표, 수당을 포함한 급여산출 기능
* 매출관리 - 시간대별 금일 매출 확인, 연목표달성률에 따른 상품별 매출 비교, 월별 상품매출 비교 기능
* POS를 통한 직원출퇴근, 주문 환불/취소, 금고관리, 시재점검, 가게오픈/마감 기능
* 서비스 이용료 정기결제를 통한 수익창출
&nbsp; 
## 2️⃣ 구성원 및 맡은 역할
| 이름    | 직책     | 역할 및 책임                                               |
|---------|---------|----------------------------------------------------------|
| 이건우  | 팀장     | - 팀장 및 프로젝트 총괄<br> - POS 결제 및 기능 구현<br> - 메인, POS, ERP CSS 작업 |
| 최이제  | 부팀장  | - 직원 목록 엑셀 다운로드 기능<br> - 가게 CRUD 구현<br> - 직책 및 직원 CRUD |
| 강경훈  | 팀원     | - 근무 일정표 구현<br> - Git 관리<br> - 근태 관리 및 급여 산출<br> - 근무 일정 및 급여 CRUD<br> - 출퇴근 기능 (POS) |
| 김남철  | 팀원     | - 임박 유통기한 알림 기능<br> - 폐기 및 재고손실 관리<br> - 이론재고/실재고 비교 로직 및 구현<br> - 재고 현황 및 입고 관리<br> - 재고 CRUD<br> - 상품 CRUD |
| 방민석  | 팀원     | - 주문 조회 및 환불/취소 기능 (POS)<br> - 매출 기록 및 통계 조회<br> - 관리자 로그인 및 대시보드 구현<br> - 가게 관리 및 유저 CRUD (관리자)<br> - 문의 답변 기능 (관리자) |
| 서치원  | 팀원     | - 정기 결제 및 SMS 인증 기능<br> - 결제 및 환불 내역 관리<br> - 목표 달성 현황 (관리자)<br> - 문의 신청/내역 조회 및 회원 수정<br> - 가게 등록/삭제 및 금고 관리<br> - 가게 오픈/마감 및 시재 점검 기능 |
## 3️⃣ 서비스 환경 
|유형|구분|서비스 배포 환경|
|------|---|---|
|SW|OS| Windows10 |
||Browser| Chrome 130.0.6723.117<br> Microsoft Edge 130.0.2849.68<br> Firefox 132.0.1 |
||Tool| IntelliJ IDEA 242.23339.11 |
||BackEnd| Java 17<br> h2<br> JPA<br>|
||Version/Issue 관리| GitHub & GitBash |
||Communication| Discord & Notion |

## 4️⃣ 사용 라이브러리 및 외부 API
### (1) 사용 라이브러리
|라이브러리 명|버전|용도|
|------|---|---|
|javax servlet api|4.0.1| 커스텀 라이브러리 구현체 사용 |
|jakarta servlet jsp jstl|3.0.0| 커스텀 라이브러리 구현체 사용을 위한 인터페이스|
|Gson |2.10.1 | JSON 데이터 변환 및 가공|
|Lombok|1.18.34|어노테이션을 활용한 간단한 메서드 사용 및 편의성 증가|
|apache POI|5.2.3 | 엑셀 파일 읽기/쓰기 기능을 제공 |
|Apache XMLBeans|5.0.2| 엑셀 파일에서 XML 처리|
|fullcalendar|4.4.2| 캘린더 및 일정 관리 기능|
|ag-grid|32.3.2| 데이터 테이블을 생성하고 관리|
|bootstrp|4.6.2| 반응형 웹 디자인을 위한 CSS |
|chart.js|4.4.6| 다양한 유형의 차트를 쉽게 생성하고 시각화|

### (2) 사용 외부 API
|기능|API 명|제공|용도|
|------|------|-----|-----|
|이메일 인증|CoolSMS API|CoolSMS|회원가입 이메일 인증|
|결제|토스 페이먼츠 결제|TossPayments|서비스구독료 정기 결제|
|환불|토스 페이먼츠 결제|TossPayments|서비스구독료 환불|
|휴일 수당 계산|특일 정보 - 공휴일 정보 조회|한국천문연구원|근무일정구현을 위한 공유일 정보 조회|


## 4️⃣ 사이트맵
![9ca5949e6f4baddf](https://github.com/user-attachments/assets/776bbb89-36ec-4924-9832-dc26ace08715)




## 5️⃣ ERD 다이어그램
![ppt_erd](https://github.com/user-attachments/assets/59bd18cb-d726-4031-a68c-20cdf3bdfac8)




## 6️⃣ 주요 기능 및 화면 소개 &nbsp;
&nbsp;&nbsp;
#### 1. 이메일 인증
![image](https://github.com/user-attachments/assets/91d6dcea-5f23-4381-adb1-93c80cac6b67)
<br><br>
#### 2. 휴대폰 인증
![image](https://github.com/user-attachments/assets/967d4faa-d988-40b1-b9dc-0a6502fc788e)
<br><br>
#### 3. 토스페이먼츠 결제
![image](https://github.com/user-attachments/assets/d232c4ae-81b1-4e4a-b860-ee28f4d6ff92)
<br><br>
#### 4. 가게 관리
![image](https://github.com/user-attachments/assets/8c163bcf-b448-4564-b92d-810d2e6e90e9)
<br><br>
#### 5. 직원 등록
![image](https://github.com/user-attachments/assets/9f59d15e-09e9-4bfa-811a-f2299df253ff)
<br><br>
#### 6. 직원목록 상세 조회
![image](https://github.com/user-attachments/assets/e6e4889d-4be4-4522-8e16-39304857bd4b)
<br><br>
#### 7. 직원정보 상세 수정
![image](https://github.com/user-attachments/assets/7154cd3b-5ecb-4aff-a1be-b2d8891f9fa9)
<br><br>
#### 8. 직원 목록 엑셀 출력
![image](https://github.com/user-attachments/assets/00449b62-c0bd-410f-aa39-709b462cde64)
<br><br>
#### 9. 근무일정표
![image](https://github.com/user-attachments/assets/524a9bb1-71ec-4aa7-8e0d-f3c1cb1505af)
<br><br>
#### 10. 근무 일정 수정
![image](https://github.com/user-attachments/assets/185a55b9-df75-49a8-8ad4-492784859e71)
<br><br>
#### 11. 근태 관리
![image](https://github.com/user-attachments/assets/cd4329bf-2900-4feb-abec-fe59e744d68f)
<br><br>
#### 12. 급여 계산
![image](https://github.com/user-attachments/assets/5e45bdb5-e326-42b3-bade-31c4d80d966f)
<br><br>
#### 13. 급여 관리
![image](https://github.com/user-attachments/assets/76acc79e-561c-4733-8177-a29c8daa9961)
<br><br>
#### 14. 상품 등록
![image](https://github.com/user-attachments/assets/d5428ebb-1f45-4ef9-81a3-eba90e4b107e)
<br><br>
#### 15. 상품 목록
![image](https://github.com/user-attachments/assets/29289608-8e4f-4b99-a139-3b8472b3ff5a)
<br><br>
#### 16. 상품 수정
![image](https://github.com/user-attachments/assets/68e4634b-02b0-4ed6-bf6e-88e00390bc79)
<br><br>
#### 17. 입고 조회
![image](https://github.com/user-attachments/assets/debb1370-72b2-4e75-ae5a-ff9e4ebc1860)
<br><br>
#### 18. 입고 등록
![image](https://github.com/user-attachments/assets/ebfad0d6-624d-440f-b098-cdf02eae51e8)
<br><br>
#### 19. 재고 조회
![image](https://github.com/user-attachments/assets/1af87113-0621-410a-92c7-2c074f726649)
<br><br>
#### 20. 재고 등록
![image](https://github.com/user-attachments/assets/2830cd67-3dcc-4c67-bc52-eb540d9b647a)
<br><br>
#### 21. 자재 수정
![image](https://github.com/user-attachments/assets/a6c732be-4e4a-4cda-a813-d11a776da1a8)
<br><br>
#### 21. 월 재고 현황
![image](https://github.com/user-attachments/assets/1d7dec8c-0a3a-4ae0-b58f-3783deac571e)
<br><br>
#### 22. 일 재고 현황
![image](https://github.com/user-attachments/assets/a0ddd554-08c2-415e-85f5-5e42d76e4fe2)
<br><br>
#### 23. 폐기 등록
![image](https://github.com/user-attachments/assets/4a683b0d-230d-431a-b8ab-e5419a20fc97)
<br><br>
#### 23. 포스 상품 주문
![image](https://github.com/user-attachments/assets/4860d8e6-7c97-4751-ae49-eefae16fc4e8)
<br><br>
#### 24. 포스 주문 내역 조회 및 취소
![image](https://github.com/user-attachments/assets/5b29f6de-8095-4ce5-b1b6-99369fda1586)
<br><br>
#### 25. 금고 관리
![image](https://github.com/user-attachments/assets/87f1231a-f720-4f20-861a-a1b58900c340)
<br><br>
#### 26. 시재 점검
![image](https://github.com/user-attachments/assets/e2e94e31-23e3-4b42-99b0-6e2b599f57e8)
<br><br>
#### 27. 시재 추가
![image](https://github.com/user-attachments/assets/b77fefff-a09c-4873-b347-f208e80b4bb5)
<br><br>
#### 28. 마감 관리
![image](https://github.com/user-attachments/assets/8ba4437a-f158-4b3d-a5a8-ec2a3e195f72)
<br><br>
#### 29. 포스 직원 출퇴근 관리
![image](https://github.com/user-attachments/assets/573b4894-df37-45a6-bede-2902a19f05d1)
<br><br>
#### 30. 매출 기록
![image](https://github.com/user-attachments/assets/5fe23e1e-8fb1-4ffd-ab82-0edef6e5a5d4)
<br><br>
#### 31. 금일 매출 통계
![image](https://github.com/user-attachments/assets/a9cb8365-e0d4-4fea-aebb-55769a1d69c9)
<br><br>
#### 32. 관리자 대시 보드
![image](https://github.com/user-attachments/assets/16c73427-1326-4808-87a1-73841bc4086c)
<br><br>
#### 33. 관리자 유저 관리
![image](https://github.com/user-attachments/assets/5d87b7a9-53e6-498d-81e9-64d624984763)
<br><br>
#### 34. 관리자 가게 관리
![image](https://github.com/user-attachments/assets/b03aa89d-ca08-4a41-88bd-2d8f64301a7f)
<br><br>
#### 35. 공지사항 관리
![image](https://github.com/user-attachments/assets/fe35ecd1-3455-44ea-b8e2-85f3aa3c647f)
<br><br>
#### 36. 문의 신청
![image](https://github.com/user-attachments/assets/94377204-2e0c-4c3b-a9e6-4643de78ced7)
<br><br>
#### 37. 문의 답변
![image](https://github.com/user-attachments/assets/8b6a5cf5-2853-4c7c-964c-2764d6efe770)
<br><br>
#### 38. 환불 요청
![image](https://github.com/user-attachments/assets/c24dfea4-6051-4f24-b0ed-4928eebcea1b)
<br><br>
#### 39. 환불 승인
![image](https://github.com/user-attachments/assets/292710a6-5597-4e62-84b7-5925f372573d)


INSERT INTO employee_tb
(name, birthday, gender, email, phone, address, account_number, position, store_id, store_employee_number, employment_status, bank_id, updated_at, unique_employee_number)
VALUES ('최이제', '1994-01-30', 'M', 'sss@naver.com', '010-3082-7894', '부산시 서구 대영로38번길 11', '121212', '주방장', 1, 1, 'ACTIVE', 11, NOW(), '1-1'),
       ('김영희', '1990-05-15', 'F', 'kim@naver.com', '010-1234-5678', '서울시 강남구 테헤란로 123', '121213', '매니저', 1, 2, 'ACTIVE', 03, NOW(), '1-2'),
       ('이철수', '1988-03-20', 'M', 'lee@naver.com', '010-9876-5432', '대전시 유성구 봉명로 25', '121214', '주방장', 1, 3, 'INACTIVE', 12, NOW(), '1-3'),
       ('박지민', '1995-07-10', 'F', 'park@naver.com', '010-1111-2222', '광주광역시 서구 상무누리로 33', '121215', '서빙', 1, 4, 'ACTIVE', 20, NOW(), '1-4'),
       ('정우성', '1992-12-05', 'M', 'jung@naver.com', '010-3333-4444', '부산시 해운대구 센텀남대로 45', '121216', '주방장', 1, 5, 'ONLEAVE', 90, NOW(), '1-5'),
       ('한지민', '1993-04-18', 'F', 'han@naver.com', '010-5555-6666', '서울시 종로구 종로 58', '121217', '서빙', 1, 6, 'ACTIVE', 92, NOW(), '1-6'),
       ('이민호', '1989-02-27', 'M', 'lee_min@naver.com', '010-7777-8888', '인천시 남동구 간석동 42', '121218', '매니저', 1, 7, 'INACTIVE', 48, NOW(), '1-7'),
       ('서현', '1996-08-21', 'F', 'seo@naver.com', '010-9999-0000', '대구광역시 중구 명덕로 70', '121219', '주방장', 1, 8, 'ACTIVE', 88, NOW(), '1-8'),
       ('장동건', '1991-09-12', 'M', 'jang@naver.com', '010-2222-3333', '울산광역시 남구 달동 20', '121220', '서빙', 1, 9, 'ONLEAVE', 11, NOW(), '1-9'),
       ('고소영', '1997-11-03', 'F', 'ko@naver.com', '010-4444-5555', '경기도 성남시 분당구 판교로 1', '121221', '매니저', 1, 10, 'ACTIVE', 92, NOW(), '1-10');

-- 샘플
INSERT INTO emp_document_tb (emp_id, employment_contract, health_certificate, identification_copy, bank_account_copy, resident_registration)
VALUES ('1', false, false, false, false, false),
       ('2', false, false, false, false, false),
       ('3', false, false, false, false, false),
       ('4', false, false, false, false, false),
       ('5', false, false, false, false, false),
       ('6', false, false, false, false, false),
       ('7', false, false, false, false, false),
       ('8', false, false, false, false, false),
       ('9', false, false, false, false, false),
       ('10', false, false, false, false, false);
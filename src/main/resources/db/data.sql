-- 첫 번째 샘플 데이터
INSERT INTO user_tb (name, email, password, phone, address, membership, created_at)
VALUES ('김남철', 'asd@asd.com', '1234', '010-1234-5678', '서울특별시 강남구 테헤란로 123', 'COMMON', NOW());

-- 두 번째 샘플 데이터
INSERT INTO user_tb (name, email, password, phone, address, membership, created_at)
VALUES ('이영희', 'younghee.lee@example.com', 'securePass!', '010-9876-5432', '부산광역시 해운대구 해운대로 456', 'PREMIUM', NOW());

-- 세 번째 샘플 데이터
INSERT INTO user_tb (name, email, password, phone, address, membership, created_at)
VALUES ('박민수', 'minsoo.park@example.com', 'minsooPass45', '010-5555-6666', '인천광역시 연수구 송도대로 789', 'COMMON', NOW());

-- 직책 커스텀 샘플
INSERT INTO position_tb (name, user_id) VALUES ('주방장', 1);

-- user 별 employee 샘플
INSERT INTO employee_tb (name, birthday, gender, email, phone, account_number, position_id, user_id, employment_status, created_at)
VALUES ('최이제', '1994-01-30', 'M', 'sss@naver.com', '010-3082-7894', '121212', '1', '1', 'ACTIVE', NOW());
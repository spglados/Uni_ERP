-- 첫 번째 샘플 데이터
INSERT INTO user_tb (name, email, password, phone, address, membership, created_at)
VALUES ('김남철', 'asd@asd.com', '1234', '010-1234-5678', '서울특별시 강남구 테헤란로 123', 'COMMON', NOW());

-- 두 번째 샘플 데이터
INSERT INTO user_tb (name, email, password, phone, address, membership, created_at)
VALUES ('이영희', 'younghee.lee@example.com', 'securePass!', '010-9876-5432', '부산광역시 해운대구 해운대로 456', 'PREMIUM',NOW());

-- 세 번째 샘플 데이터
INSERT INTO user_tb (name, email, password, phone, address, membership, created_at)
VALUES ('박민수', 'minsoo.park@example.com', 'minsooPass45', '010-5555-6666', '인천광역시 연수구 송도대로 789', 'COMMON', NOW());
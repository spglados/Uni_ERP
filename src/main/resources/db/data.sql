-- 첫 번째 샘플 데이터
INSERT INTO user_tb (name, email, password, phone, address, membership, created_at)
VALUES ('김남철', 'asd@asd.com', '1234', '010-1234-5678', '서울특별시 강남구 테헤란로 123', 'COMMON', NOW());

-- 두 번째 샘플 데이터
INSERT INTO user_tb (name, email, password, phone, address, membership, created_at)
VALUES ('이영희', 'younghee.lee@example.com', 'securePass!', '010-9876-5432', '부산광역시 해운대구 해운대로 456', 'PREMIUM',NOW());

-- 세 번째 샘플 데이터
INSERT INTO user_tb (name, email, password, phone, address, membership, created_at)
VALUES ('박민수', 'minsoo.park@example.com', 'minsooPass45', '010-5555-6666', '인천광역시 연수구 송도대로 789', 'COMMON', NOW());

-- 은행 데이터
INSERT INTO bank_tb (id, name) VALUES
(002, '산업은행'),
(003, '기업은행'),
(004, '국민은행'),
(005, '외환은행'),
(007, '수협중앙회'),
(011, '농협은행'),
(012, '지역농.축협'),
(020, '우리은행'),
(023, 'SC은행'),
(027, '한국씨티은행'),
(031, '대구은행'),
(032, '부산은행'),
(034, '광주은행'),
(035, '제주은행'),
(037, '전북은행'),
(039, '경남은행'),
(045, '새마을금고중앙회'),
(048, '신협중앙회'),
(050, '상호저축은행'),
(081, 'KEB하나은행'),
(088, '신한은행'),
(089, '케이뱅크'),
(090, '카카오뱅크'),
(092, '토스뱅크');
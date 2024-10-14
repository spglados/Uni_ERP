INSERT INTO store_tb (name, user_id)
VALUES ('기가 막히는 한정식', 1);

-- 첫 번째 제품 삽입
INSERT INTO product_tb (product_code, name, category, price, store_id)
VALUES (111, '김치찌개', '메인', 9900, 1),
(112, '된장찌개', '메인', 8500, 1);

INSERT INTO ingredient_tb (name, amount, unit, product_id)
-- 김치찌개
VALUES
('김치', 200, 'G', 1),
('돼지고기', 150, 'G', 1),
('두부', 100, 'G', 1),
('고춧가루', 10, 'G', 1),
('마늘', 5, 'G', 1),
-- 된장찌개
('된장', 50, 'G', 2),
('두부', 100, 'G', 2),
('애호박', 80, 'G', 2),
('양파', 50, 'G', 2),
('버섯', 70, 'G', 2);
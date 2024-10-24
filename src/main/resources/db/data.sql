-- 첫 번째 샘플 데이터
INSERT INTO user_tb (name, email, password, phone, address, membership, created_at)
VALUES ('김남철', 'asd@asd.com', '1234', '01012345678', '서울특별시 강남구 테헤란로 123', 'COMMON', NOW());

-- 두 번째 샘플 데이터
INSERT INTO user_tb (name, email, password, phone, address, membership, created_at)
VALUES ('이영희', 'younghee.lee@example.com', 'securePass!', '010-9876-5432', '부산광역시 해운대구 해운대로 456', 'PREMIUM', NOW());

-- 세 번째 샘플 데이터
INSERT INTO user_tb (name, email, password, phone, address, membership, created_at)
VALUES ('박민수', 'minsoo.park@example.com', 'minsooPass45', '010-5555-6666', '인천광역시 연수구 송도대로 789', 'COMMON', NOW());

INSERT INTO PAYMENT_TB (
    ID,
    AMOUNT,
    APPROVED_AT,
    BILLING_KEY,
    CANCEL,
    CUSTOMER_KEY,
    DATE,
    LAST_TRANSACTION_KEY,
    NEXT_PAY,
    NEXT_PAY_AMOUNT,
    NOW_PAY_AMOUNT,
    ORDER_ID,
    ORDER_NAME,
    PAYMENT_KEY,
    REQUESTED_AT,
    TOTAL_AMOUNT,
    USER_ID
) VALUES (
    1,
    50000,
    '2024-10-23T17:10:03+09:00',
    'xTkYOcNoK9C4Vrv4xeVH-BEYWsMuh7LLdjO21YCl4hw=',
    'N',
    '9bj1ebfvm4',
    '2024-10-23 17:10:40',
    'F5EEE43A328E02364438C46DF47D16B2',
    '2024-11-08',
    50000,
    62903,
    '930b65d3-d81c-4cd0-ac26-ee3bd66699c4',
    '첫번째 결제',
    'tviva20241023171003xl9r9',
    '2024-10-23T17:10:03+09:00',
    NULL,
    1
);
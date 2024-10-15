-- user 별 employee 샘플
INSERT INTO employee_tb (name, birthday, gender, email, phone, address, account_number, position, store_id, employment_status, updated_at)
VALUES ('최이제', '1994-01-30', 'M', 'sss@naver.com', '010-3082-7894', '부산시 서구 대영로38번길 11','121212', '주방장', '1', 'ACTIVE', NOW());
-- 샘플
INSERT INTO emp_document_tb (emp_id, employment_contract, health_certificate, identification_copy, bank_account_copy, resident_registration)
VALUES ('1',false,false,false,false,false);

INSERT INTO employee_tb (name, birthday, gender, email, phone, address, account_number, position, store_id, employment_status, updated_at)
VALUES ('강경훈', '1994-05-10', 'M', 'ddd@naver.com', '010-3434-2322', '부산시 서구 대영로39번길 11','123452', '서버', '1', 'ACTIVE', NOW());
-- 샘플
INSERT INTO emp_document_tb (emp_id, employment_contract, health_certificate, identification_copy, bank_account_copy, resident_registration)
VALUES ('2',false,false,false,false,false);

-- 스케쥴 샘플
INSERT INTO hr_schedule_tb (store_id, emp_id, start_time, end_time, schedule_type)
VALUES
    -- PLAN 스케줄
    (1, 1, '2024-10-15 11:00:00', '2024-10-15 14:00:00', 'PLAN'),
    (1, 2, '2024-10-16 12:00:00', '2024-10-16 15:00:00', 'PLAN'),
    (1, 1, '2024-10-17 13:00:00', '2024-10-17 16:00:00', 'PLAN'),
    (1, 2, '2024-10-18 14:00:00', '2024-10-18 17:00:00', 'PLAN'),
    (1, 1, '2024-10-19 15:00:00', '2024-10-19 18:00:00', 'PLAN'),
    (1, 2, '2024-10-20 16:00:00', '2024-10-20 19:00:00', 'PLAN'),
    (1, 1, '2024-10-21 17:00:00', '2024-10-21 20:00:00', 'PLAN'),
    (1, 2, '2024-10-22 11:30:00', '2024-10-22 14:30:00', 'PLAN'),
    (1, 1, '2024-10-23 12:00:00', '2024-10-23 15:00:00', 'PLAN'),
    (1, 2, '2024-10-24 13:00:00', '2024-10-24 16:00:00', 'PLAN'),

    -- EXECUTE 스케줄 (80%는 PLAN과 동일한 시간)
    (1, 1, '2024-10-15 11:00:00', '2024-10-15 14:00:00', 'EXECUTE'),
    (1, 2, '2024-10-16 12:00:00', '2024-10-16 15:00:00', 'EXECUTE'),
    (1, 1, '2024-10-17 13:00:00', '2024-10-17 16:00:00', 'EXECUTE'),
    (1, 2, '2024-10-18 14:00:00', '2024-10-18 17:00:00', 'EXECUTE'),
    (1, 1, '2024-10-19 15:00:00', '2024-10-19 18:00:00', 'EXECUTE'),
    (1, 2, '2024-10-20 16:00:00', '2024-10-20 19:00:00', 'EXECUTE'),
    (1, 1, '2024-10-21 17:00:00', '2024-10-21 20:00:00', 'EXECUTE'),
    (1, 2, '2024-10-22 11:30:00', '2024-10-22 14:30:00', 'EXECUTE'),
    (1, 1, '2024-10-23 12:00:00', '2024-10-23 15:00:00', 'EXECUTE'),
    (1, 2, '2024-10-24 13:00:00', '2024-10-24 16:00:00', 'EXECUTE');

-- EXECUTE 스케줄 (20%는 시간 차이 있음)
INSERT INTO hr_schedule_tb (store_id, emp_id, start_time, end_time, schedule_type)
VALUES
    (1, 1, '2024-10-25 11:30:00', '2024-10-25 14:30:00', 'PLAN'),
    (1, 2, '2024-10-26 12:00:00', '2024-10-26 15:00:00', 'PLAN'),
    (1, 1, '2024-10-27 13:00:00', '2024-10-27 16:00:00', 'PLAN'),
    (1, 2, '2024-10-28 14:00:00', '2024-10-28 17:00:00', 'PLAN'),

    -- 20% 시간이 어긋난 EXECUTE
    (1, 1, '2024-10-25 11:45:00', '2024-10-25 14:15:00', 'EXECUTE'), -- 15분 차이
    (1, 2, '2024-10-26 12:10:00', '2024-10-26 15:10:00', 'EXECUTE'), -- 10분 차이
    (1, 1, '2024-10-27 13:30:00', '2024-10-27 16:30:00', 'EXECUTE'), -- 30분 차이
    (1, 2, '2024-10-28 13:45:00', '2024-10-28 17:15:00', 'EXECUTE'); -- 15분 차이

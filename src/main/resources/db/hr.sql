INSERT INTO employee_tb
(name, birthday, gender, email, phone, address, account_number, position, store_id, store_employee_number, employment_status, bank_id, updated_at, unique_employee_number)
VALUES
    ('최이제', '1994-01-30', 'M', 'sss@naver.com', '010-3082-7894', '부산시 서구 대영로38번길 11', '121212', '주방장', 1, 1, 'ACTIVE', 11, NOW(), '1-1');
-- 샘플
INSERT INTO emp_document_tb (emp_id, employment_contract, health_certificate, identification_copy, bank_account_copy, resident_registration)
VALUES ('1',false,false,false,false,false);
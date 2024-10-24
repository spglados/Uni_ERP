-- 스케쥴 샘플
-- NOT_EXECUTED 스케줄
INSERT INTO hr_schedule_tb (store_id, emp_id, start_time, end_time, status)
VALUES (1, 1, '2024-10-15 11:00:00', '2024-10-15 14:00:00', 'ATTENDED');
INSERT INTO hr_schedule_tb (store_id, emp_id, start_time, end_time, status)
VALUES (1, 2, '2024-10-16 12:00:00', '2024-10-16 15:00:00', 'ATTENDED');
INSERT INTO hr_schedule_tb (store_id, emp_id, start_time, end_time, status)
VALUES (1, 1, '2024-10-17 13:00:00', '2024-10-17 16:00:00', 'ATTENDED');
INSERT INTO hr_schedule_tb (store_id, emp_id, start_time, end_time, status)
VALUES (1, 2, '2024-10-18 14:00:00', '2024-10-18 17:00:00', 'ATTENDED');
INSERT INTO hr_schedule_tb (store_id, emp_id, start_time, end_time, status)
VALUES (1, 1, '2024-10-19 15:00:00', '2024-10-19 18:00:00', 'ATTENDED');
INSERT INTO hr_schedule_tb (store_id, emp_id, start_time, end_time, status)
VALUES (1, 2, '2024-10-20 16:00:00', '2024-10-20 19:00:00', 'ATTENDED');
INSERT INTO hr_schedule_tb (store_id, emp_id, start_time, end_time, status)
VALUES (1, 1, '2024-10-21 17:00:00', '2024-10-21 20:00:00', 'ATTENDED');
INSERT INTO hr_schedule_tb (store_id, emp_id, start_time, end_time, status)
VALUES (1, 2, '2024-10-22 11:30:00', '2024-10-22 14:30:00', 'ATTENDED');
INSERT INTO hr_schedule_tb (store_id, emp_id, start_time, end_time, status)
VALUES (1, 1, '2024-10-23 12:00:00', '2024-10-23 15:00:00', 'ATTENDED');
INSERT INTO hr_schedule_tb (store_id, emp_id, start_time, end_time, status)
VALUES (1, 2, '2024-10-24 13:00:00', '2024-10-24 16:00:00', 'ATTENDED');

-- ATTENDANCE
INSERT INTO hr_attendance_tb (store_id, emp_id, start_time, end_time, status, schedule_id)
VALUES (1, 1, '2024-10-15 11:00:00', '2024-10-15 14:00:00', 'ATTENDED', 1);
INSERT INTO hr_attendance_tb (store_id, emp_id, start_time, end_time, status, schedule_id)
VALUES (1, 2, '2024-10-16 12:00:00', '2024-10-16 15:00:00', 'ATTENDED', 2);
INSERT INTO hr_attendance_tb (store_id, emp_id, start_time, end_time, status, schedule_id)
VALUES (1, 1, '2024-10-17 13:00:00', '2024-10-17 16:00:00', 'ATTENDED', 3);
INSERT INTO hr_attendance_tb (store_id, emp_id, start_time, end_time, status, schedule_id)
VALUES (1, 2, '2024-10-18 14:00:00', '2024-10-18 17:00:00', 'ATTENDED', 4);
INSERT INTO hr_attendance_tb (store_id, emp_id, start_time, end_time, status, schedule_id)
VALUES (1, 1, '2024-10-19 15:00:00', '2024-10-19 18:00:00', 'ATTENDED', 5);
INSERT INTO hr_attendance_tb (store_id, emp_id, start_time, end_time, status, schedule_id)
VALUES (1, 2, '2024-10-20 16:00:00', '2024-10-20 19:00:00', 'ATTENDED', 6);
INSERT INTO hr_attendance_tb (store_id, emp_id, start_time, end_time, status, schedule_id)
VALUES (1, 1, '2024-10-21 17:00:00', '2024-10-21 20:00:00', 'ATTENDED', 7);
INSERT INTO hr_attendance_tb (store_id, emp_id, start_time, end_time, status, schedule_id)
VALUES (1, 2, '2024-10-22 11:30:00', '2024-10-22 14:30:00', 'ATTENDED', 8);
INSERT INTO hr_attendance_tb (store_id, emp_id, start_time, end_time, status, schedule_id)
VALUES (1, 1, '2024-10-23 12:00:00', '2024-10-23 15:00:00', 'ATTENDED', 9);
INSERT INTO hr_attendance_tb (store_id, emp_id, start_time, end_time, status, schedule_id)
VALUES (1, 2, '2024-10-24 13:00:00', '2024-10-24 16:00:00', 'ATTENDED', 10);

-- 지켜지지 않은 스케줄
INSERT INTO hr_schedule_tb (store_id, emp_id, start_time, end_time, status, minutes)
VALUES (1, 1, '2024-10-25 11:30:00', '2024-10-25 14:30:00', 'LATE', 15);
INSERT INTO hr_schedule_tb (store_id, emp_id, start_time, end_time, status, minutes)
VALUES (1, 2, '2024-10-26 12:00:00', '2024-10-26 15:00:00', 'LATE', 10);
INSERT INTO hr_schedule_tb (store_id, emp_id, start_time, end_time, status, minutes)
VALUES (1, 1, '2024-10-27 13:00:00', '2024-10-27 16:00:00', 'LATE', 30);
INSERT INTO hr_schedule_tb (store_id, emp_id, start_time, end_time, status, minutes)
VALUES (1, 2, '2024-10-28 14:00:00', '2024-10-28 17:00:00', 'LEFT_EARLY', 45);

-- ATTENDANCE
INSERT INTO hr_attendance_tb (store_id, emp_id, start_time, end_time, status, schedule_id)
VALUES (1, 1, '2024-10-25 11:45:00', '2024-10-25 14:15:00', 'LATE', 11);
INSERT INTO hr_attendance_tb (store_id, emp_id, start_time, end_time, status, schedule_id)
VALUES (1, 2, '2024-10-26 12:10:00', '2024-10-26 15:10:00', 'LATE', 12);
INSERT INTO hr_attendance_tb (store_id, emp_id, start_time, end_time, status, schedule_id)
VALUES (1, 1, '2024-10-27 13:30:00', '2024-10-27 16:30:00', 'LATE', 13);
INSERT INTO hr_attendance_tb (store_id, emp_id, start_time, end_time, status, schedule_id)
VALUES (1, 2, '2024-10-28 14:00:00', '2024-10-28 16:15:00', 'LEFT_EARLY', 14);

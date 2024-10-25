package com.uni.uni_erp.repository.erp.hr;

import com.uni.uni_erp.domain.entity.erp.hr.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface AttendanceRepository extends JpaRepository<Attendance, Integer> {

    @Query(value = "SELECT emp_id FROM hr_attendance_tb WHERE start_time <= ? AND end_time >= ? AND store_id = ?", nativeQuery = true)
    List<Integer> findAttendanceByDateAndStoreId(Date startDate, Date endDate, Integer storeId);

}

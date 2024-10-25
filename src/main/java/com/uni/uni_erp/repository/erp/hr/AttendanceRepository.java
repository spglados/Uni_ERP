package com.uni.uni_erp.repository.erp.hr;

import com.uni.uni_erp.domain.entity.erp.hr.Attendance;
import com.uni.uni_erp.dto.AttendanceDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

public interface AttendanceRepository extends JpaRepository<Attendance, Integer> {

    @Query(value = "SELECT employee_id FROM Attendance_tb WHERE start_time BETWEEN ? AND ? AND store_id = ? AND status = 'WORKING'", nativeQuery = true)
    List<Integer> findAttendanceByDateAndStoreId(Date startDate, Date endDate, Integer storeId);

}

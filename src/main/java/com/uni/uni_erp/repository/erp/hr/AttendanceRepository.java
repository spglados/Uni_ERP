package com.uni.uni_erp.repository.erp.hr;

import com.uni.uni_erp.domain.entity.erp.hr.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public interface AttendanceRepository extends JpaRepository<Attendance, Integer> {

    @Query(value = "SELECT emp_id FROM hr_attendance_tb WHERE start_time <= ? AND end_time >= ? AND store_id = ?", nativeQuery = true)
    List<Integer> findAttendanceByDateAndStoreId(Date startDate, Date endDate, Integer storeId);

    /**
     * 근무 일정의 시간대를 지정하여 해당 사번의 근무를 모두 조회한다.
     * 단, 예정에 없던 근무일 수도 있기 때문에 null인 경우도 포함하고
     * 기존에 작성된 예정에 없던 근무가 조회되는걸 피하기 위해 상태를 지정한다.
     */
    @Query("SELECT a FROM Attendance a " +
            "JOIN FETCH a.employee e " +
            "JOIN FETCH a.store s " +
            "LEFT JOIN FETCH a.schedule sch " +
            "WHERE e.uniqueEmployeeNumber = :uniqueEmployeeNumber " +
            "AND (sch IS NULL OR (sch.startTime >= :startOfDay AND sch.startTime < :endOfDay)) " +
            "AND a.status <> 'UNPLANNED_WORK'")
    List<Attendance> findByUniqueEmployeeNumberAndTodayExcludingUnplanned(
            @Param("uniqueEmployeeNumber") Long uniqueEmployeeNumber,
            @Param("startOfDay") Timestamp startOfDay,
            @Param("endOfDay") Timestamp endOfDay);
}

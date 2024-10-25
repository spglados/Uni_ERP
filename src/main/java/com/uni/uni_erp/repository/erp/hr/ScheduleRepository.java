package com.uni.uni_erp.repository.erp.hr;

import com.uni.uni_erp.domain.entity.erp.hr.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Integer> {

    @Query("SELECT s FROM Schedule s WHERE (:type IS NULL OR s.status = :type) AND s.store.id = :storeId")
    List<Schedule> findByStoreIdAndType(@Param("storeId") Integer storeId, @Param("type") Schedule.Status type);
}

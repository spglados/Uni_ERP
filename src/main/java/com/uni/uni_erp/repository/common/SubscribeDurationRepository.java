package com.uni.uni_erp.repository.common;

import com.uni.uni_erp.domain.entity.SubscribeDuration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.util.List;

public interface SubscribeDurationRepository extends JpaRepository<SubscribeDuration, Integer> {

    @Modifying
    @Query(value = "UPDATE subscribe_duration SET end_time = :nextMonthLastDayTimestamp WHERE user_id = :userId", nativeQuery = true)
    void updateEndTimeByUserId(@Param("userId") Integer userId, @Param("nextMonthLastDayTimestamp") Timestamp nextMonthLastDayTimestamp);

    @Query(value = "SELECT * FROM subscribe_duration_tb WHERE end_time IS NOT NULL", nativeQuery = true)
    List<SubscribeDuration> findSubscribeDurationsWithEndTime();
}

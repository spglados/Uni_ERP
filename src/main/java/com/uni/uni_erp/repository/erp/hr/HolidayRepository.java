package com.uni.uni_erp.repository.erp.hr;

import com.uni.uni_erp.domain.entity.erp.hr.Holiday;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface HolidayRepository extends JpaRepository<Holiday, Integer> {

    List<Holiday> findByDateBetweenAndType(LocalDate startDate, LocalDate endDate, Holiday.Type type);
    List<Holiday> findByDateBetween(LocalDate startDate, LocalDate endDate);
}

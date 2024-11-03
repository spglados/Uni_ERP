package com.uni.uni_erp.repository.erp.hr;

import com.uni.uni_erp.domain.entity.erp.hr.Employee;
import com.uni.uni_erp.domain.entity.erp.hr.Payroll;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

public interface PayrollRepository extends JpaRepository<Payroll, Integer> {

    @Query("SELECT e FROM Employee e WHERE e.store.id = :storeId AND e.id NOT IN " +
            "(SELECT p.employee.id FROM Payroll p WHERE p.yearMonth = :yearMonth AND p.employee.store.id = :storeId)")
    List<Employee> findEmployeesWithoutPayroll(@Param("storeId") Integer storeId,
                                               @Param("yearMonth") YearMonth yearMonth);
    Optional<Payroll> findByEmployee_IdAndYearMonth(Integer empId, YearMonth yearMonth);
}
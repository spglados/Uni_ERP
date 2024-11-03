package com.uni.uni_erp.repository.erp.hr;

import com.uni.uni_erp.domain.entity.erp.hr.EmpDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmpDocumentRepository extends JpaRepository<EmpDocument, Integer> {

    // 직원 문서 조회
    Optional<EmpDocument> findByEmployeeId(Integer employeeId);

    @Modifying
    @Query("UPDATE EmpDocument d SET d.employmentContract = :employmentContract, d.healthCertificate = :healthCertificate, d.healthCertificateDate = :healthCertificateDate, d.identificationCopy = :identificationCopy, d.bankAccountCopy = :bankAccountCopy, d.residentRegistration = :residentRegistration WHERE d.id = :id")
    void updateEmpDocument(@Param("id") Integer id,
                           @Param("employmentContract") Boolean employmentContract,
                           @Param("healthCertificate") Boolean healthCertificate,
                           @Param("healthCertificateDate") String healthCertificateDate,
                           @Param("identificationCopy") Boolean identificationCopy,
                           @Param("bankAccountCopy") Boolean bankAccountCopy,
                           @Param("residentRegistration") Boolean residentRegistration);

}

package com.audit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.audit.entity.AuditReport;

@Repository
public interface AuditReportRepository extends JpaRepository<AuditReport, Integer> {
    @Query(value = "select * from audit_report where id = ?1",nativeQuery = true)
    AuditReport findOneById(Integer id);
}

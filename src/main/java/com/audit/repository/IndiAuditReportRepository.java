package com.audit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.audit.entity.IndividualAuditReport;

@Repository
public interface IndiAuditReportRepository extends JpaRepository<IndividualAuditReport, Integer> {
	
	@Query(value = "select * from individual_audit_report where id = ?1",nativeQuery = true)
	IndividualAuditReport findIndiAuditReportById(Integer id);

	@Query(value = "update individual_audit_report set score = ?1 where id =?2",nativeQuery = true)
	void updateScore(Double score, Integer id);

	@Query(value = "select * from individual_audit_report where trainee_id = ?1 and audit_report_id = ?2",nativeQuery = true)
	IndividualAuditReport getIndividualAuditReportById(Integer audit_report_id, Integer trainee_id);
}

package com.audit.repository;

import com.audit.entity.AuditSchedule;
import com.audit.response.AuditResult;
import com.audit.response.AuditScheduleResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AuditScheduleRepository extends JpaRepository<AuditSchedule, Integer> {
    @Query(value = "SELECT * FROM audit_schedule au WHERE au.is_deleted = false", countQuery = "SELECT COUNT(*) FROM audit_schedule au WHERE au.is_deleted = false", nativeQuery = true)
    public Page<AuditScheduleResult> findAllAD(Pageable pageable);

    @Query(value = "SELECT * FROM audit_schedule au WHERE au.is_deleted = false AND au.id = ?1", nativeQuery = true)
    public Optional<AuditSchedule> findById(int id);

    @Query(value = "select * from audit_schedule where id = ?1", nativeQuery = true)
    AuditSchedule findAuditScheduleById(Integer id);

    @Query(value = "select a.traineeId as trainee_id,a.id as individual_report_id, c.time as date_evaluated, a.score as score from IndividualAuditReport a, AuditReport b, AuditSchedule c where a.auditReport=b.id and b.auditSchedule=c.id and c.classId=:class_id and c.session=:audit_session", countQuery = "SELECT COUNT(*) from IndividualAuditReport a, AuditReport b, AuditSchedule c where a.auditReport=b.id and b.auditSchedule=c.id and c.classId=:class_id and c.session=:audit_session")
    public Page<AuditResult> findByClassIdAndSession(int class_id, int audit_session, Pageable pageable);

    @Query(value = "SELECT COUNT(*) from IndividualAuditReport a, AuditReport b, AuditSchedule c where a.auditReport=b.id and b.auditSchedule=c.id and c.classId=:class_id and c.session=:audit_session")
    public int countByClassIdAndSession(int class_id, int audit_session);
}
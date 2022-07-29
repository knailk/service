package com.audit.repository;

import com.audit.entity.AuditSchedule;
import com.audit.entity.AuditTrainee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;

public interface AuditTraineeRepository extends JpaRepository<AuditTrainee, Integer> {
    @Query(value = "SELECT at.trainee_id FROM audit_trainee at WHERE at.audit_schedule_id = ?1 and at.is_deleted = false",
            countQuery = "SELECT COUNT(*) FROM audit_trainee at WHERE at.audit_schedule_id = ?1 and at.is_deleted = false",
            nativeQuery = true)
    public Page<Integer> getAllByIdAudit(int id, Pageable pageable);

    @Query(value = "SELECT * FROM audit_trainee at WHERE at.audit_schedule_id = ?1 and at.trainee_id not in ?2",
            nativeQuery = true)
    public List<AuditTrainee> getAllNotInList(int auditScheduleId, Set<Integer> traineeIdList);

    @Query(value = "SELECT COUNT(*) FROM audit_trainee at WHERE at.audit_schedule_id = ?1 and at.is_deleted = false",
            nativeQuery = true)
    public int countByScheduleId(int id);

}

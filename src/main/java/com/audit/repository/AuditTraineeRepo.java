package com.audit.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.audit.entity.AuditTrainee;

public interface AuditTraineeRepo extends JpaRepository<AuditTrainee, Integer> {

}

package com.audit.entity;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "audit_trainee")
@IdClass(CompositeKey.class)
public class AuditTrainee {
    @Id
    @JsonManagedReference
    @ManyToOne
    @JoinColumn(name = "audit_schedule_id")
    private AuditSchedule auditSchedule;
    @Id
    @Column(name = "trainee_id")
    private int traineeId;

    @Column(name = "is_deleted")
    private boolean isDeleted;

    public AuditTrainee() {
    }

    public AuditSchedule getAuditSchedule() {
        return auditSchedule;
    }

    public void setAuditSchedule(AuditSchedule auditSchedule) {
        this.auditSchedule = auditSchedule;
    }

    public int getTraineeId() {
        return traineeId;
    }

    public void setTraineeId(int traineeId) {
        this.traineeId = traineeId;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public AuditTrainee(AuditSchedule auditSchedule, int traineeId) {
        this.auditSchedule = auditSchedule;
        this.traineeId = traineeId;
        this.isDeleted = false;
    }
}

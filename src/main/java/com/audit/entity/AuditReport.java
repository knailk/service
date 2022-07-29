package com.audit.entity;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "audit_report")
public class AuditReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "duration_time")
    private double durationTime;
    @OneToOne
    @JoinColumn(name = "audit_schedule_id")
    private AuditSchedule auditSchedule;

    @OneToMany(mappedBy = "auditReport")
    private Set<IndividualAuditReport> individualAuditReports;

    public AuditReport(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getDurationTime() {
        return durationTime;
    }

    public void setDurationTime(double durationTime) {
        this.durationTime = durationTime;
    }

    public AuditSchedule getAuditSchedule() {
        return auditSchedule;
    }

    public void setAuditSchedule(AuditSchedule auditSchedule) {
        this.auditSchedule = auditSchedule;
    }

    public Set<IndividualAuditReport> getIndividualAuditReports() {
        return individualAuditReports;
    }

    public void setIndividualAuditReports(Set<IndividualAuditReport> individualAuditReports) {
        this.individualAuditReports = individualAuditReports;
    }

    public AuditReport(double durationTime, AuditSchedule auditSchedule, Set<IndividualAuditReport> individualAuditReports) {
        this.durationTime = durationTime;
        this.auditSchedule = auditSchedule;
        this.individualAuditReports = individualAuditReports;
    }
}

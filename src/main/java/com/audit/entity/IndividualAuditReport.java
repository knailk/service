package com.audit.entity;

import javax.persistence.*;
import java.util.Set;
@Entity
@Table(name = "individual_audit_report")
public class IndividualAuditReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "assessment")
    private String assessment;
    @Column(name = "score")
    private double score;
    @Column(name = "trainee_id")
    private int traineeId;
    @ManyToOne
    @JoinColumn(name = "audit_report_id")
    private AuditReport auditReport;
    @OneToMany(mappedBy = "individualAuditReport")
    private Set<AuditQuestion> auditQuestions;
    public IndividualAuditReport(){}

    public int getId() {
        return id;
    }
    public void setId(int id) { this.id = id;}
    public String getAssessment() {
        return assessment;
    }

    public void setAssessment(String assessment) {
        this.assessment = assessment;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public int getTraineeId() {
        return traineeId;
    }

    public void setTraineeId(int traineeId) {
        this.traineeId = traineeId;
    }

    public AuditReport getAuditReport() {
        return auditReport;
    }

    public void setAuditReport(AuditReport auditReport) {
        this.auditReport = auditReport;
    }

    public Set<AuditQuestion> getAuditQuestions(){
        return auditQuestions;
    }

    public void setAuditQuestions(Set<AuditQuestion> auditQuestions){
        this.auditQuestions =auditQuestions;
    }

    public IndividualAuditReport(String assessment, double score, int traineeId, AuditReport auditReport, Set<AuditQuestion> auditQuestions) {
        this.assessment = assessment;
        this.score = score;
        this.traineeId = traineeId;
        this.auditReport = auditReport;
        this.auditQuestions = auditQuestions;
    }
}

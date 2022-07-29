package com.audit.entity;

import javax.persistence.*;
@Entity
@Table(name = "audit_question")
public class AuditQuestion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    @JoinColumn(name = "question_id")
    private Question question;
    @Column(name = "assessment")
    private String assessment;
    @Column(name = "evaluation")
    private double evaluation;
    @ManyToOne
    @JoinColumn(name = "individual_report_id")
    private IndividualAuditReport individualAuditReport;

    public AuditQuestion(){}

    public int getId() {
        return id;
    }
    public void setId(int id) { this.id = id;}
    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public String getAssessment() {
        return assessment;
    }

    public void setAssessment(String assessment) {
        this.assessment = assessment;
    }

    public double getEvaluation() {
        return evaluation;
    }

    public void setEvaluation(double evaluation) {
        this.evaluation = evaluation;
    }

    public IndividualAuditReport getIndividualAuditReport() {
        return individualAuditReport;
    }

    public void setIndividualAuditReport(IndividualAuditReport individualAuditReport) {
        this.individualAuditReport = individualAuditReport;
    }

    public AuditQuestion(Question question, String assessment, double evaluation, IndividualAuditReport individualAuditReport) {
        this.question = question;
        this.assessment = assessment;
        this.evaluation = evaluation;
        this.individualAuditReport = individualAuditReport;
    }
}

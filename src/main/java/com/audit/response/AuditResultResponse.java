package com.audit.response;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AuditResultResponse {

    private int individual_report_id;
    private String trainee_email;
    private double score;
    private String full_name;
    private Date date_evaluated;

    public AuditResultResponse(int individual_report_id, String trainee_email, double score,
            String full_name,
            Date date_evaluated) {
        this.individual_report_id = individual_report_id;
        this.trainee_email = trainee_email;
        this.score = score;
        this.full_name = full_name;
        this.date_evaluated = date_evaluated;
    }

    // public Date getDate_evaluated() {
    // return date_evaluated;
    // }

    public String getDate_evaluated() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy.MM.dd hh:mm");
        return df.format(date_evaluated);
    }

    public void setDate_evaluated(Date date_evaluated) {
        this.date_evaluated = date_evaluated;
    }

    public int getIndividual_report_id() {
        return individual_report_id;
    }

    public void setIndividual_report_id(int individual_report_id) {
        this.individual_report_id = individual_report_id;
    }

    public String getEmail() {
        return trainee_email;
    }

    public void setTrainee_email(String trainee_email) {
        this.trainee_email = trainee_email;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

}

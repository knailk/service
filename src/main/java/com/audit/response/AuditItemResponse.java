package com.audit.response;

public class AuditItemResponse {
    private int id;
    private String traineeName;

    private int idTrainee;
    private String email;
    private double grade;
    private String status;

    public AuditItemResponse(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTraineeName() {
        return traineeName;
    }

    public void setTraineeName(String traineeName) {
        this.traineeName = traineeName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public double getGrade() {
        return grade;
    }

    public void setGrade(double grade) {
        this.grade = grade;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getIdTrainee() {
        return idTrainee;
    }

    public void setIdTrainee(int idTrainee) {
        this.idTrainee = idTrainee;
    }

    public AuditItemResponse(int id, String traineeName, int idTrainee, String email, double grade, String status) {
        this.id = id;
        this.traineeName = traineeName;
        this.idTrainee = idTrainee;
        this.email = email;
        this.grade = grade;
        this.status = status;
    }
}

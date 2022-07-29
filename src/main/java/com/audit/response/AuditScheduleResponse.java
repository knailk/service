package com.audit.response;


import java.sql.Time;
import java.util.Date;

public class AuditScheduleResponse {
    private int id;
    private String nameAuditor;
    private String date;
    private String time;
    private String room;

    public AuditScheduleResponse(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNameAuditor() {
        return nameAuditor;
    }

    public void setNameAuditor(String nameAuditor) {
        this.nameAuditor = nameAuditor;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public AuditScheduleResponse(int id, String nameAuditor, String date, String time, String room) {
        this.id = id;
        this.nameAuditor = nameAuditor;
        this.date = date;
        this.time = time;
        this.room = room;
    }
}

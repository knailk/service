package com.audit.response;

import java.util.List;

public class DetailAuditScheduleResponse {
    private int idAuditReport;
    private String auditorName;
    private String dateTime;
    private String room;
    private List<AuditItemResponse> auditItems;

    public DetailAuditScheduleResponse(){}

    public int getIdAuditReport() {
        return idAuditReport;
    }

    public void setIdAuditReport(int idAuditReport) {
        this.idAuditReport = idAuditReport;
    }

    public String getAuditorName() {
        return auditorName;
    }

    public void setAuditorName(String auditorName) {
        this.auditorName = auditorName;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public List<AuditItemResponse> getAuditItems() {
        return auditItems;
    }

    public void setAuditItems(List<AuditItemResponse> auditItems) {
        this.auditItems = auditItems;
    }

    public DetailAuditScheduleResponse(int idAuditReport, String auditorName, String dateTime, String room, List<AuditItemResponse> auditItems) {
        this.idAuditReport = idAuditReport;
        this.auditorName = auditorName;
        this.dateTime = dateTime;
        this.room = room;
        this.auditItems = auditItems;
    }
}

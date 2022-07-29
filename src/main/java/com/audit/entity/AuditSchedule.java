package com.audit.entity;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "audit_schedule")
public class AuditSchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date time;
    @Column(name = "auditor_id")
    private int auditorId;
    @Column(name = "skill_id")
    private int skillId;
    @Column(name = "room")
    private String room;
    @Column(name = "module_id")
    private int moduleId;
    @Column(name = "class_id")
    private int classId;
    @OneToOne(mappedBy = "auditSchedule")
    private AuditReport auditReport;

    @JsonBackReference
    @OneToMany(mappedBy = "auditSchedule")
    private Set<AuditTrainee> auditTrainees;

    @Column(name = "session")
    private int session;

    @Column(name = "is_deleted")
    private boolean isDeleted = false;

    public int getSession() {
        return session;
    }

    public void setSession(int session) {
        this.session = session;
    }

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean deleted) {
        isDeleted = deleted;
    }

    public AuditSchedule() {
    }

    public Set<AuditTrainee> getAuditTrainees() {
        return this.auditTrainees;
    }

    public void setAuditTrainees(Set<AuditTrainee> auditTrainees) {
        this.auditTrainees = auditTrainees;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public int getAuditorId() {
        return auditorId;
    }

    public void setAuditorId(int auditorId) {
        this.auditorId = auditorId;
    }

    public int getSkillId() {
        return skillId;
    }

    public void setSkillId(int skillId) {
        this.skillId = skillId;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public int getModuleId() {
        return moduleId;
    }

    public void setModuleId(int moduleId) {
        this.moduleId = moduleId;
    }

    public int getClassId() {
        return classId;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }

    public AuditReport getAuditReport() {
        return auditReport;
    }

    public void setAuditReport(AuditReport auditReport) {
        this.auditReport = auditReport;
    }

    public AuditSchedule(Date time, int auditorId, int skillId, String room, int moduleId, int classId,
            AuditReport auditReport, Set<AuditTrainee> auditTrainees, int session, Boolean isDeleted) {
        this.time = time;
        this.auditorId = auditorId;
        this.skillId = skillId;
        this.room = room;
        this.moduleId = moduleId;
        this.classId = classId;
        this.auditReport = auditReport;
        this.auditTrainees = auditTrainees;
        this.session = session;
        this.isDeleted = isDeleted;
    }

    public AuditSchedule(Date time, int auditorId, int skillId, String room, int moduleId, int classId,
            Set<AuditTrainee> auditTrainees, int session, Boolean isDeleted) {
        this.time = time;
        this.auditorId = auditorId;
        this.skillId = skillId;
        this.room = room;
        this.moduleId = moduleId;
        this.classId = classId;
        this.auditTrainees = auditTrainees;
        this.session = session;
        this.isDeleted = isDeleted;
    }
}

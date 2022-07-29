package com.audit.request;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Set;

public class AuditScheduleRequest {
    private int id;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    @NotNull(message = "Missing Time!")
    private Date time;
    @NotNull(message = "Missing Auditor!")
    @Min(value = 1, message = "More than 1 and not null!")
    private int auditorId;

    @Min(value = 1, message = "More than 1 and not null!")
    @NotNull(message = "Missing Skill!")
    private int skillId;
    @NotNull(message = "Missing Room!")
    @NotBlank(message = "Missing Room!")
    private String room;
    @Min(value = 1, message = "More than 1 and not null!")
    @NotNull(message = "Missing Module!")
    private int moduleId;
    @Min(value = 1, message = "More than 1 and not null!")
    @NotNull(message = "Missing Class!")
    private int classId;
    @NotEmpty(message = "Missing Trainee!")
    private Set<Integer> traineesId;
    @Min(value = 1, message = "More than 1 and not null!")
    @NotNull(message = "Missing session!")
    private int session;

    public AuditScheduleRequest() {
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

    public Set<Integer> getTraineesId() {
        return traineesId;
    }

    public void setTraineesId(Set<Integer> traineesId) {
        this.traineesId = traineesId;
    }

    public int getSession() {
        return session;
    }

    public void setSession(int session) {
        this.session = session;
    }

    public AuditScheduleRequest(int id, Date time, int auditorId, int skillId, String room, int moduleId, int classId,
            Set<Integer> traineesId, int session) {
        this.id = id;
        this.time = time;
        this.auditorId = auditorId;
        this.skillId = skillId;
        this.room = room;
        this.moduleId = moduleId;
        this.classId = classId;
        this.traineesId = traineesId;
        this.session = session;
    }
}

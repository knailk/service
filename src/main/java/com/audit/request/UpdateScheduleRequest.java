package com.audit.request;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

public class UpdateScheduleRequest {
    private int id;
    @NotNull(message = "Missing Time!")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm")
    private Date time;
    @NotNull(message = "Missing Auditor!")
    @Min(value = 1, message = "More than 1 and not null!")
    private int auditorId;
    @NotNull(message = "Missing Room!")
    @NotBlank(message = "Missing Room!")
    private String room;

    public UpdateScheduleRequest(){};
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

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public UpdateScheduleRequest(int id, Date time, int auditorId, String room) {
        this.id = id;
        this.time = time;
        this.auditorId = auditorId;
        this.room = room;
    }
}

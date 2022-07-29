package com.audit.response;

import java.util.Date;

public interface AuditScheduleResult {
    public int getId();
    public int getAuditor_id();
    public Date getTime();
    public String getRoom();
}

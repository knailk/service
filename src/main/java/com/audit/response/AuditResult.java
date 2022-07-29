package com.audit.response;

import java.util.Date;

public interface AuditResult {
    int getIndividual_report_id();

    int getTrainee_id();

    double getScore();

    Date getDate_evaluated();
}
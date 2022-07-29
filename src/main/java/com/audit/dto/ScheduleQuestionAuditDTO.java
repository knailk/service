package com.audit.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ScheduleQuestionAuditDTO {
    private int question_id ;
    private String assessment;
    private double evaluation;
}

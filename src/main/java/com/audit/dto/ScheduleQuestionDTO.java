package com.audit.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ScheduleQuestionDTO {
    private String content;
    private String solution;
    private String level;
    private int module_id;
}

package com.audit.response;

import java.util.ArrayList;
import java.util.List;

import com.audit.dto.ScheduleQuestionDTO;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ScheduleShowQuestionResponse {
    private List<ScheduleQuestionDTO> listQuestion = new ArrayList<>();	
}

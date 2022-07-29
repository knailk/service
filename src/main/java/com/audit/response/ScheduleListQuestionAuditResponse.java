package com.audit.response;

import java.util.ArrayList;
import java.util.List;

import com.audit.dto.ScheduleQuestionAuditDTO;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ScheduleListQuestionAuditResponse {
    private List<ScheduleQuestionAuditDTO> listAuditQuestionDTO = new ArrayList<>();	
}

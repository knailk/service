package com.audit.converter;

import org.springframework.stereotype.Component;

import com.audit.dto.EvaluationDTO;
import com.audit.entity.AuditSchedule;
import com.audit.entity.IndividualAuditReport;
import com.audit.entity.Question;

@Component
public class EvaluatedConverter {
    public EvaluationDTO toDTO(AuditSchedule aSchedule, IndividualAuditReport IAReport, Question qBank, String auditor_name){
        EvaluationDTO dto = new EvaluationDTO();
        dto.setTime(aSchedule.getTime());
        dto.setContent(qBank.getContent());
        dto.setLevel(qBank.getLevel());
        dto.setAssessment(IAReport.getAssessment());
        dto.setScore(IAReport.getScore());
        dto.setAuditorName(auditor_name);
        return dto;
    }
}

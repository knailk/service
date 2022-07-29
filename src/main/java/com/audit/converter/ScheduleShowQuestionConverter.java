package com.audit.converter;

import org.springframework.stereotype.Component;

import com.audit.dto.ScheduleQuestionDTO;
import com.audit.entity.Question;

@Component
public class ScheduleShowQuestionConverter {
    public ScheduleQuestionDTO toDto(Question qBank){
        ScheduleQuestionDTO convertDto = new ScheduleQuestionDTO();
        convertDto.setContent(qBank.getContent());
        convertDto.setLevel(qBank.getLevel());
        convertDto.setModule_id(qBank.getModuleId());
        convertDto.setSolution(qBank.getSolution());
        return convertDto;
    }
}

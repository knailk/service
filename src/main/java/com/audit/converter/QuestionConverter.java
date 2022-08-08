package com.audit.converter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.audit.dto.QuestionDTO;
import com.audit.entity.Question;
import com.audit.request.QuestionCreateRequest;
import com.audit.service.CommonService;
import com.audit.service.QuestionService;

@Component
public class QuestionConverter {
    @Autowired
    QuestionService questionService;

    public QuestionDTO toDto(Question question) {
        return new QuestionDTO(
            question.getId(), 
            question.getContent(), 
            question.getSolution(), 
            question.getLevel(), 
            questionService.getSkill(question.getSkillId()),
            questionService.getModule(question.getModuleId()), 
            question.getStatus()
        );
    }

    public Question toEnity(QuestionDTO questionDTO) {
        return new Question(
            questionDTO.getId(),
            questionDTO.getQuestion(),
            questionDTO.getAnswer(),
            questionDTO.getLevel(),
            questionService.getSkillId(questionDTO.getSkill()),
            questionService.getModuleId(questionDTO.getModule()),
            questionDTO.getStatus(),
            false
        );
    }

    public Question toEnity(QuestionCreateRequest question) {
        return new Question(
            question.getQuestion(),
            question.getAnswer(),
            question.getLevel(),
            questionService.getSkillId(question.getSkill()),
            questionService.getModuleId(question.getModule()),
            question.getStatus(),
            false
        );
    }

    public List<QuestionDTO> toDto(List<Question> questions) {
        List<QuestionDTO> list = new ArrayList<>();
        for (Question i : questions) {
            list.add(toDto(i));
        }
        return list;
    }
}

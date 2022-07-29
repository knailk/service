package com.audit.dto;

import com.audit.entity.Question;

public class QuestionDTO {
    private Integer id;
    private String question;
    private String answer;
    private String level;
    private String skill;
    private String module;
    private String status;

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }
    public void setQuestion(String question) {
        this.question = question;
    }
    public String getAnswer() {
        return answer;
    }   
    public void setAnswer(String answer) {
        this.answer = answer;
    }
    public String getLevel() {
        return level;
    }
    public void setLevel(String level) {
        this.level = level;
    }

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    public String getModule() {
        return module;
    }
    public void setModule(String module) {
        this.module = module;
    }

    public String getSkill() {
        return skill;
    }

    public void setSkill(String skill) {
        this.skill = skill;
    }

    public QuestionDTO(Integer id, String question, String answer, String level, String skill, String module,
            String status) {
        this.id = id;
        this.question = question;
        this.answer = answer;
        this.level = level;
        this.skill = skill;
        this.module = module;
        this.status = status;
    }

}


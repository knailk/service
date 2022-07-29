package com.audit.request;

import javax.validation.constraints.NotBlank;

public class QuestionCreateRequest {
    @NotBlank(message = "Question is mandatory")
    private String question;
    private String answer;
    @NotBlank(message = "Level is mandatory")
    private String level;
    private String skill;
    private String module;
    @NotBlank(message = "Status is mandatory")
    private String status;
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
    public String getSkill() {
        return skill;
    }
    public void setSkill(String skill) {
        this.skill = skill;
    }
    public String getModule() {
        return module;
    }
    public void setModule(String module) {
        this.module = module;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public QuestionCreateRequest(String question, String answer, String level, String skill, String module,
            String status) {
        this.question = question;
        this.answer = answer;
        this.level = level;
        this.skill = skill;
        this.module = module;
        this.status = status;
    }
    
}

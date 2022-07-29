package com.audit.entity;

import javax.persistence.*;
import java.util.List;
@Entity
@Table(name = "question_bank")
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "content")
    private String content;
    @Column(name = "solution")
    private String solution;
    @Column(name = "level")
    private String level;
    @Column(name = "skill_id")
    private int skillId;
    @Column(name = "module_id")
    private int moduleId;
    @OneToMany(mappedBy = "question")
    private List<AuditQuestion> auditQuestions;

    @Column(name = "status")
    private String status;

    @Column(name = "is_deleted")
    private boolean isDeleted;

    public Question(){}

    public List<AuditQuestion> getAuditQuestions() {
        return auditQuestions;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) { this.id = id;}

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSolution() {
        return solution;
    }

    public void setSolution(String solution) {
        this.solution = solution;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public int getSkillId() {
        return skillId;
    }

    public void setSkillId(int skillId) {
        this.skillId = skillId;
    }

    public int getModuleId() {
        return moduleId;
    }

    public void setModuleId(int moduleId) {
        this.moduleId = moduleId;
    }

    public List<AuditQuestion> getAuditQuestion(){
        return auditQuestions;
    }

    public void setAuditQuestions(List<AuditQuestion> auditQuestions){
        this.auditQuestions =auditQuestions;
    }

    

    public Question(int id, String content, String solution, String level, int skillId, int moduleId, String status,
            boolean isDeleted) {
        this.id = id;
        this.content = content;
        this.solution = solution;
        this.level = level;
        this.skillId = skillId;
        this.moduleId = moduleId;
        this.status = status;
        this.isDeleted = isDeleted;
    }
    public Question(String content, String solution, String level, int skillId, int moduleId, String status,
                    boolean isDeleted) {
        this.content = content;
        this.solution = solution;
        this.level = level;
        this.skillId = skillId;
        this.moduleId = moduleId;
        this.status = status;
        this.isDeleted = isDeleted;
    }
    public Question(Question question) {
        this.content = question.content;
        this.solution = question.solution;
        this.level = question.level;
        this.skillId = question.skillId;
        this.moduleId = question.moduleId;
        this.auditQuestions = question.auditQuestions;
        this.status = question.status;
        this.isDeleted = question.isDeleted;
    }

    @Override
    public String toString() {
        return "Question [content=" + content + ", id=" + id + ", isDeleted=" + isDeleted + ", level=" + level
                + ", moduleId=" + moduleId + ", skillId=" + skillId + ", solution=" + solution + ", status=" + status
                + "]";
    }
}

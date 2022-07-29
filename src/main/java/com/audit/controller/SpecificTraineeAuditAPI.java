package com.audit.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.audit.dto.ScheduleQuestionAuditDTO;
import com.audit.entity.Question;
import com.audit.exception.BadRequestException;
import com.audit.exception.NotFoundException;
import com.audit.repository.QuestionRepository;
import com.audit.service.SpecificTraineeAuditService;

@RestController
@RequestMapping("api/schedules")
public class SpecificTraineeAuditAPI {
    @Autowired
    private SpecificTraineeAuditService specificTraineeAuditService;
    @Autowired
    QuestionRepository questionRepository;

    // /detail/1/2?page=1
    @GetMapping("/detail/{audit_id}/{trainee_id}")
    public ResponseEntity<?> showListQuestionOfTraineeAudit(
        @PathVariable(value = "audit_id", required = true) int schedule_audit_id, @PathVariable(value = "trainee_id", required = true) int schedule_trainee_audit_id,@RequestParam(value = "page",required = true) Integer page){
            if(page == null)
                throw new  BadRequestException("Wrong page value");
            if(page <= 0 )
                throw new  BadRequestException("Wrong page value");
            Pageable pageable = PageRequest.of(page - 1,10,Sort.by("id").ascending());
            return  ResponseEntity.status(HttpStatus.OK).body(specificTraineeAuditService.findById(schedule_audit_id, pageable));
    }

    @PostMapping("/detail/{audit_id}/{trainee_id}")
    public ResponseEntity<?> createQuestion(@RequestBody Question newQuestion, @PathVariable(value = "audit_id", required = true) int schedule_audit_id, @PathVariable(value = "trainee_id", required = true) int schedule_trainee_audit_id){
        if(newQuestion.getContent() == "")
            throw new NotFoundException("Resource not found!");
        if(questionRepository.findByContent(newQuestion.getContent()) != null) 
        throw new BadRequestException("Data has exist!");
        return  ResponseEntity.status(HttpStatus.OK).body(specificTraineeAuditService.saveToDb(newQuestion,schedule_audit_id, schedule_trainee_audit_id));
    }


    //add comment and evaluate of a trainee in audit
    @PutMapping("/detail/{audit_id}/{trainee_id}")
    public ResponseEntity<?> editQuestion(@RequestBody List<ScheduleQuestionAuditDTO> listAuditQuestionDTO , @PathVariable(value = "audit_id", required = true) int schedule_audit_id, @PathVariable(value = "trainee_id", required = true) int schedule_trainee_audit_id){
            return ResponseEntity.ok(specificTraineeAuditService.saveResultAudit(listAuditQuestionDTO,schedule_audit_id,schedule_trainee_audit_id));
    }
}

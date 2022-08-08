package com.audit.controller;

import com.audit.dto.ScheduleQuestionAuditDTO;
import com.audit.entity.AuditSchedule;
import com.audit.entity.Question;
import com.audit.exception.BadRequestException;
import com.audit.exception.InternalServerException;
import com.audit.exception.NotFoundException;
import com.audit.repository.QuestionRepository;
import com.audit.request.AuditScheduleRequest;
import com.audit.request.UpdateScheduleRequest;
import com.audit.response.*;
import com.audit.service.AuditScheduleService;
import com.audit.service.SpecificTraineeAuditService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.audit.service.CommonService;
import com.audit.service.EvaluationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.util.List;

@RestController
@RequestMapping(path = "api/schedules")
public class AuditScheduleController {
    @Autowired
    AuditScheduleService auditScheduleService;

    @Autowired
    EvaluationService evaluationService;
    @Autowired
    private SpecificTraineeAuditService specificTraineeAuditService;
    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    CommonService commonService;

    @GetMapping("auditors")
    public ResponseEntity<Object> getAuditors() {
        return ResponseEntity.status(HttpStatus.OK).body(auditScheduleService.getAuditors());
    }

    @GetMapping("modules/{class_id}")
    public ResponseEntity<Object> getModules(@PathVariable int class_id) {
        return ResponseEntity.status(HttpStatus.OK).body(auditScheduleService.getModules(class_id));
    }

    @GetMapping("trainees/{class_id}")
    public ResponseEntity<Object> getTraineesByClass(@PathVariable int class_id) {
        return ResponseEntity.status(HttpStatus.OK).body(auditScheduleService.getTrainees(class_id));
    }

    @PostMapping("/{adminId}")
    public ResponseEntity<Object> createAudit(@RequestBody @Valid AuditScheduleRequest auditSchedule, @PathVariable(value = "adminId", required = true) int adminId) throws JsonProcessingException {
        AuditSchedule response = auditScheduleService.createAudit(auditSchedule);
        auditScheduleService.sendMail(auditSchedule, adminId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("classes")
    public ResponseEntity<Object> getClasss() {
        return ResponseEntity.status(HttpStatus.OK).body(evaluationService.getClasses());
    }

    @GetMapping()
    ResponseEntity<Object> getAllAuditSchedule(@RequestParam(value = "page", required = true) int page,
            @RequestParam(value = "size", defaultValue = "50") int size, @RequestParam(value = "user-id") int id, @RequestParam(value = "role") int role) {
        int totalPage = commonService.totalPage(page, size, auditScheduleService.countAll(id, role));
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("time"));
        List<AuditScheduleResponse> auditScheduleResponses = auditScheduleService.getAllAuditSchedule(id, role, pageable);
        ListShowResponse response = new ListShowResponse(auditScheduleResponses, page, size, totalPage);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("{auditScheduleId}")
    ResponseEntity<Object> getDetailAuditSchedule(@PathVariable("auditScheduleId") int auditScheduleId,
            @RequestParam(value = "page", required = true) int page,
            @RequestParam(value = "size", defaultValue = "50") int size, @RequestParam(value = "user-id") int id, @RequestParam(value = "role") int role) {
        int totalPage = auditScheduleService.totalPage(page, size, auditScheduleId);
        Pageable pageable = PageRequest.of(page - 1, size);
        DetailAuditScheduleResponse detailAuditScheduleResponse = auditScheduleService
                .getDetailAuditSchedule(auditScheduleId, id, role, pageable);
        ShowResponse response = new ShowResponse(detailAuditScheduleResponse, page, size, totalPage);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("")
    ResponseEntity<Object> deleteAuditScheduleList(@RequestBody @NotEmpty List<Integer> idList) {
        List<StatusDeleteAuditScheduleResponse> statusList = auditScheduleService.deleteAuditSchedule(idList);
        return ResponseEntity.status(HttpStatus.OK).body(statusList);
    }

    @PutMapping("")
    ResponseEntity<Object> updateAuditSchedule(@Valid @RequestBody UpdateScheduleRequest updateScheduleRequest) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(auditScheduleService.updateAuditSchedule(updateScheduleRequest));

    }

    // /detail/1/2?page=1?size=2
    @GetMapping("/detail/{auditId}/{traineeId}")
    public ResponseEntity<?> showListQuestionOfTraineeAudit(
            @PathVariable(value = "auditId", required = true) int scheduleAuditId,
            @PathVariable(value = "traineeId", required = true) int scheduleTraineeAuditId,
            @RequestParam(value = "page", required = true) Integer page,
            @RequestParam(value = "size", required = true) Integer size) {
        if (page == null || size == null)
            throw new BadRequestException("Wrong page value");
        if (page <= 0 || size <=0)
            throw new BadRequestException("Wrong page value");
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("id").ascending());
        return ResponseEntity.status(HttpStatus.OK)
                .body(specificTraineeAuditService.findById(scheduleAuditId, pageable));
    }

    @PostMapping("/detail/{auditId}/{traineeId}")
    public ResponseEntity<?> createQuestion(@RequestBody List<Integer> listQuestionId,
            @PathVariable(value = "auditId", required = true) int scheduleAuditId,
            @PathVariable(value = "traineeId", required = true) int scheduleTraineeAuditId) {
        if (listQuestionId == null)
            throw new BadRequestException("Data not exist!");
        specificTraineeAuditService.saveToDb(listQuestionId, scheduleAuditId, scheduleTraineeAuditId);
        return ResponseEntity.status(HttpStatus.OK).body(
            new ResponseObject(false, "Insert audit question successfully", null)
        );
    }

    // add comment and evaluate of a trainee in audit
    @PutMapping("/detail/{auditId}/{traineeId}")
    public ResponseEntity<?> editQuestion(@RequestBody List<ScheduleQuestionAuditDTO> listAuditQuestionDTO,
            @PathVariable(value = "auditId", required = true) int scheduleAuditId,
            @PathVariable(value = "traineeId", required = true) int scheduleTraineeAuditId) {
        return ResponseEntity.ok(specificTraineeAuditService.saveResultAudit(listAuditQuestionDTO, scheduleAuditId,
        scheduleTraineeAuditId));
    }

    @DeleteMapping("/detail/{auditId}/{traineeId}")
    public ResponseEntity<?> deleteQuestion(@RequestBody Integer id_question,
    @PathVariable(value = "auditId", required = true) int scheduleAuditId,
    @PathVariable(value = "traineeId", required = true) int scheduleTraineeAuditId){
        specificTraineeAuditService.deleteQuestion(id_question, scheduleAuditId, scheduleTraineeAuditId);
        return ResponseEntity.status(HttpStatus.OK).body(
        new ResponseObject(false, "Delete audit question successfully", null)
      );
    }
}

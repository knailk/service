package com.audit.controller;

import com.audit.exception.BadRequestException;
import com.audit.exception.InternalServerException;
import com.audit.exception.NotFoundException;
import com.audit.request.AuditScheduleRequest;
import com.audit.request.UpdateScheduleRequest;
import com.audit.response.AuditScheduleResponse;
import com.audit.response.DetailAuditScheduleResponse;
import com.audit.response.StatusDeleteAuditScheduleResponse;
import com.audit.service.AuditScheduleService;
import com.audit.service.AuditService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@RestController
@RequestMapping(path = "api/schedules")
public class AuditScheduleController {
    @Autowired
    AuditScheduleService auditScheduleService;

    @Autowired
    AuditService auditService;

    @GetMapping("auditors")
    public ResponseEntity<Object> getAuditors() {
        return ResponseEntity.status(HttpStatus.OK).body(auditService.getAuditorList());
    }

    @GetMapping("modules/{class_id}")
    public ResponseEntity<Object> getModules(@PathVariable int class_id) {
        return ResponseEntity.status(HttpStatus.OK).body(auditService.getModuleList(class_id));
    }

    @GetMapping("trainees/{class_id}")
    public ResponseEntity<Object> getTraineesByClass(@PathVariable int class_id) {
        return ResponseEntity.status(HttpStatus.OK).body(auditService.getTraineeList(class_id));
    }

    @PostMapping("")
    public ResponseEntity<Object> createAudit(@RequestBody @Valid AuditScheduleRequest auditSchedule) {
        return ResponseEntity.status(HttpStatus.OK).body(auditService.createAudit(auditSchedule));
    }

    @GetMapping("classes")
    public ResponseEntity<Object> getClasss() {
        return ResponseEntity.status(HttpStatus.OK).body(auditService.getClassList());
    }

    @GetMapping("")
    ResponseEntity<Object> getAllAuditSchedule(@RequestParam(value = "page", required = true) int page,
            @RequestParam(value = "size", required = true) int size) {
        int totalPage = auditScheduleService.totalPage(page, size);
        Pageable pageable = PageRequest.of(page - 1, size);
        List<AuditScheduleResponse> auditScheduleResponses = auditScheduleService.getAllAuditSchedule(pageable);
        return ResponseEntity.status(HttpStatus.OK).body(auditScheduleResponses);
    }

    @GetMapping("{auditScheduleId}")
    ResponseEntity<Object> getDetailAuditSchedule(@PathVariable("auditScheduleId") @Min(10) int auditScheduleId,
            @RequestParam(value = "page", required = true) int page,
            @RequestParam(value = "size", required = true) int size) {
        int totalPage = auditScheduleService.totalPage(page, size, auditScheduleId);
        Pageable pageable = PageRequest.of(page - 1, size);
        DetailAuditScheduleResponse detailAuditScheduleResponse = auditScheduleService
                .getDetailAuditSchedule(auditScheduleId, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(detailAuditScheduleResponse);
    }

    @DeleteMapping("")
    ResponseEntity<Object> deleteAuditScheduleList(@RequestBody @NotEmpty List<Integer> idList) {
        List<StatusDeleteAuditScheduleResponse> statusList = auditScheduleService.deleteAuditScheduleList(idList);
        return ResponseEntity.status(HttpStatus.OK).body(statusList);
    }

    @DeleteMapping("{id}")
    ResponseEntity<Object> deleteAuditSchedule(@PathVariable int id) {
        StatusDeleteAuditScheduleResponse status = auditScheduleService.deleteAuditSchedule(id);
        return ResponseEntity.status(HttpStatus.OK).body(status);
    }

    @PutMapping("")
    ResponseEntity<Object> updateAuditSchedule(@Valid @RequestBody UpdateScheduleRequest updateScheduleRequest) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(auditScheduleService.updateAuditSchedule(updateScheduleRequest));

    }
}

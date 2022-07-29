package com.audit.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.audit.entity.AuditSchedule;
import com.audit.exception.BadRequestException;
import com.audit.exception.NotFoundException;
import com.audit.feignclients.CommonFeignClient;
import com.audit.request.AuditScheduleRequest;
import com.audit.response.AuditResultResponse;
import com.audit.response.ClassResponse;
import com.audit.response.ModuleResponse;
import com.audit.response.ResponseObject;
import com.audit.response.UserNameResponse;
import com.audit.service.AuditService;

@RestController
@RequestMapping("api")
public class AuditController {

    @Autowired
    AuditService auditService;

    @GetMapping("/number-audit/{class_id}")
    public ResponseEntity<Object> getNumAudit(@PathVariable int class_id) {
        return ResponseEntity.status(HttpStatus.OK).body(auditService.getNumAudit(class_id));
    }

    @GetMapping("/audit-results/{class_id}/{audit_session}")
    public ResponseEntity<Object> getAuditResults(@PathVariable int class_id,
            @PathVariable int audit_session, @RequestParam(value = "page", required = true) int page,
            @RequestParam(value = "size", required = true) int size) {
        int totalPage = (int) Math.ceil((double) auditService.countByClassIdAndSession(class_id, audit_session) / size);
        if (page > totalPage) {
            throw new NotFoundException("Resource not found!");
        } else if (page < 0 || size < 0) {
            throw new BadRequestException("page or size invalid!");
        }
        Pageable pageable = PageRequest.of(page - 1, size);
        return ResponseEntity.status(HttpStatus.OK)
                .body(auditService.getAuditResultListResponsee(class_id, audit_session, pageable));
    }
}

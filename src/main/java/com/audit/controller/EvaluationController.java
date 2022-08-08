package com.audit.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.audit.service.CommonService;
import com.audit.service.EvaluationService;

@RestController
@RequestMapping("api/")
public class EvaluationController {

	@Autowired
	CommonService commonService;
	@Autowired
	private EvaluationService evaluationService;

	// get detail by id
	@GetMapping("audit-results/trainee/{indi_id}")
	public ResponseEntity<?> showEvaluation(
			@PathVariable(value = "indi_id", required = true) int invidualAuditReportId) {
		return ResponseEntity.status(HttpStatus.OK).body(evaluationService.findById(invidualAuditReportId));
	}

	@GetMapping("/number-audit/{class_id}")
	public ResponseEntity<Object> getNumAudit(@PathVariable int class_id) {
		return ResponseEntity.status(HttpStatus.OK).body(evaluationService.getNumAudit(class_id));
	}

	@GetMapping("/audit-results/{class_id}/{audit_session}")
	public ResponseEntity<Object> getAuditResults(@PathVariable int class_id,
			@PathVariable int audit_session, @RequestParam(value = "page", required = true) int page,
			@RequestParam(value = "size", required = true) int size) {
		int totalPage = commonService.totalPage(page, size,
				evaluationService.countByClassIdAndSession(class_id, audit_session));
		Pageable pageable = PageRequest.of(page - 1, size, Sort.by("traineeId"));
		return ResponseEntity.status(HttpStatus.OK)
				.body(evaluationService.getAuditResults(class_id, audit_session, pageable));
	}
}
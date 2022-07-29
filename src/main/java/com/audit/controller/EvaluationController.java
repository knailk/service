package com.audit.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.audit.service.EvaluationService;

@RestController
@RequestMapping("api/")
public class EvaluationController {
	@Autowired
	private EvaluationService evaService;

	// get detail by id
	@GetMapping("audit-results/trainee/{indi_id}")
	public ResponseEntity<?> showEvaluation(@PathVariable(value = "indi_id", required = true) int invidualAuditReportId){
		return  ResponseEntity.status(HttpStatus.OK).body(evaService.findById(invidualAuditReportId));
	}
}
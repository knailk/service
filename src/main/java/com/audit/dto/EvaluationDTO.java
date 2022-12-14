package com.audit.dto;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class EvaluationDTO {
	private Date time;  //AuditSchedule
	private String content; // Question
	private String level; // Question
	private String assessment; // IndividualAuditReport
	private double score; // IndividualAuditReport
	private String auditorName; //user-> first_name
}

package com.audit.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.audit.converter.EvaluatedConverter;
import com.audit.dto.EvaluationDTO;
import com.audit.entity.AuditQuestion;
import com.audit.entity.AuditReport;
import com.audit.entity.AuditSchedule;
import com.audit.entity.IndividualAuditReport;
import com.audit.entity.Question;
import com.audit.exception.NotFoundException;
import com.audit.repository.AuditQuestionRepository;
import com.audit.repository.IndiAuditReportRepository;
import com.audit.response.EvaluatedShowResponse;

@Service
public class EvaluationService {
	@Autowired
	private AuditQuestionRepository auditQuestionRepository;

	@Autowired
	private IndiAuditReportRepository individualAuditReportRepository;

	@Autowired
	private EvaluatedConverter evaluatedConverter;

	public EvaluatedShowResponse findById(Integer id) {
		EvaluatedShowResponse response = new EvaluatedShowResponse();
		List<EvaluationDTO> listEva = new ArrayList<>();
		// get data
		IndividualAuditReport individualAuditReport = individualAuditReportRepository.findIndiAuditReportById(id);
		AuditReport auditReportEntity = individualAuditReport.getAuditReport();
		AuditSchedule auditScheduleEntity = auditReportEntity.getAuditSchedule();
		List<AuditQuestion> auditQuestionEntity = auditQuestionRepository
				.findByIndividualReportId(individualAuditReport.getId());
		for (AuditQuestion item : auditQuestionEntity) {
			Question questionBankEntity = item.getQuestion();
			listEva.add(evaluatedConverter.toDTO(auditScheduleEntity, individualAuditReport, questionBankEntity));
		}

		if (listEva.size() == 0) {
			throw new NotFoundException("Data not found");
		}
		response.setListEva(listEva);
		return response;
	}
}

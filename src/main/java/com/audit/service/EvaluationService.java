package com.audit.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.audit.converter.EvaluatedConverter;
import com.audit.dto.EvaluationDTO;
import com.audit.entity.AuditQuestion;
import com.audit.entity.AuditReport;
import com.audit.entity.AuditSchedule;
import com.audit.entity.IndividualAuditReport;
import com.audit.entity.Question;
import com.audit.exception.BadRequestException;
import com.audit.exception.NotFoundException;
import com.audit.feignclients.ClassFeignClient;
import com.audit.feignclients.UserFeignClient;
import com.audit.repository.AuditQuestionRepository;
import com.audit.repository.AuditScheduleRepository;
import com.audit.repository.IndiAuditReportRepository;
import com.audit.response.AuditResult;
import com.audit.response.AuditResultResponse;
import com.audit.response.ClassResponse;
import com.audit.response.EvaluatedShowResponse;
import com.audit.response.UserMailResponse;

@Service
public class EvaluationService {
	@Autowired
	private AuditQuestionRepository auditQuestionRepository;

	@Autowired
	private IndiAuditReportRepository individualAuditReportRepository;

	@Autowired
	AuditScheduleRepository auditScheduleRepository;

	@Autowired
	private EvaluatedConverter evaluatedConverter;

	@Autowired
	ClassFeignClient classFeignClient;

	@Autowired
	UserFeignClient userFeignClient;

	@Autowired
	CommonService commonService;

	public EvaluatedShowResponse findById(Integer id) {
		if(id == null) throw new BadRequestException("Id individual audit report is null");
		EvaluatedShowResponse response = new EvaluatedShowResponse();
		List<EvaluationDTO> listEva = new ArrayList<>();
		// get data
		IndividualAuditReport individualAuditReport = individualAuditReportRepository.findIndiAuditReportById(id);
		AuditReport auditReportEntity = individualAuditReport.getAuditReport();
		AuditSchedule auditScheduleEntity = auditReportEntity.getAuditSchedule();
		int auditor_id = auditScheduleEntity.getAuditorId();
		List<Integer> list=new ArrayList<Integer>();
		list.add(auditor_id);
		Map<Integer, String> auditor_name = userFeignClient.getNameByIds(list);
		if (auditor_name == null) throw new NotFoundException("Auditor name not found!");
		List<AuditQuestion> auditQuestionEntity = auditQuestionRepository
				.findByIndividualReportId(individualAuditReport.getId());
		for (AuditQuestion item : auditQuestionEntity) {
			Question questionBankEntity = item.getQuestion();
			listEva.add(evaluatedConverter.toDTO(auditScheduleEntity, individualAuditReport, questionBankEntity, auditor_name.get(auditor_id)));
		}
		if (listEva.size() == 0) {
			throw new NotFoundException("Data not found");
		}
		response.setListEva(listEva);
		response.setGpa(individualAuditReport.getScore());
		return response;
	}

	public List<ClassResponse> getClasses() {
		return classFeignClient.getClasses();
	}

	public int countByClassIdAndSession(int class_id, int audit_session) {
		return auditScheduleRepository.countByClassIdAndSession(class_id, audit_session);
	}

	public int getNumAudit(int class_id) {
		if (!commonService.checkClass(class_id))
			throw new NotFoundException("Not found class id " + class_id);
		return classFeignClient.getNumAudit(class_id);
	}

	public List<AuditResultResponse> getAuditResults(int class_id, int audit_session, Pageable pageable) {
		if (!commonService.checkClass(class_id))
			throw new NotFoundException("Not found class id " + class_id);
		if (classFeignClient.getNumAudit(class_id) < audit_session)
			throw new NotFoundException("Not found session " + audit_session + " of this class id " + class_id);
		List<AuditResult> auditResults = auditScheduleRepository
				.findByClassIdAndSession(class_id, audit_session, pageable).getContent();
		List<Integer> traineesId = new ArrayList<>();
		auditResults.forEach(v1 -> {
			traineesId.add(v1.getTrainee_id());
		});
		List<UserMailResponse> traineesInfo = userFeignClient.getTraineesInfo(traineesId);
		List<AuditResultResponse> auditResultResponses = new ArrayList<>();
		for (int i = 0; i < traineesInfo.size(); i++) {
			auditResultResponses.add(new AuditResultResponse(auditResults.get(i).getIndividual_report_id(),
					traineesInfo.get(i).getMail(), auditResults.get(i).getScore(), traineesInfo.get(i).getName(),
					auditResults.get(i).getDate_evaluated()));
		}
		return auditResultResponses;
	}
}

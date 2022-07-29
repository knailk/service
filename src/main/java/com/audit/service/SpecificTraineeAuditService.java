package com.audit.service;

import java.util.List;
import java.util.Set;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.audit.converter.ScheduleShowQuestionConverter;
import com.audit.dto.ScheduleQuestionAuditDTO;
import com.audit.dto.ScheduleQuestionDTO;
import com.audit.entity.AuditQuestion;
import com.audit.entity.AuditReport;
import com.audit.entity.AuditSchedule;
import com.audit.entity.IndividualAuditReport;
import com.audit.entity.Question;
import com.audit.repository.AuditQuestionRepository;
import com.audit.repository.AuditReportRepository;
import com.audit.repository.AuditScheduleRepository;
import com.audit.repository.IndiAuditReportRepository;
import com.audit.repository.QuestionRepository;
import com.audit.response.ScheduleListQuestionAuditResponse;
import com.audit.response.ScheduleShowQuestionResponse;

@Service
public class SpecificTraineeAuditService {
    @Autowired
    AuditScheduleRepository auditScheduleRepository;

    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    IndiAuditReportRepository indiAuditReportRepository;

    @Autowired
    AuditQuestionRepository auditQuestionRepository;

    @Autowired
    AuditReportRepository auditReportRepository;

    @Autowired
    ScheduleShowQuestionConverter scheduleShowQuestionConverter;

    public ScheduleShowQuestionResponse findById(Integer schedule_audit_id, Pageable pageable) {
        ScheduleShowQuestionResponse response = new ScheduleShowQuestionResponse();
        List<ScheduleQuestionDTO> listDto = new ArrayList<>();

        AuditSchedule auditScheduleEntity = auditScheduleRepository.findAuditScheduleById(schedule_audit_id);
        List<Question> listQuestionBankEntity = questionRepository
                .getListQuestionByScheduleModule(auditScheduleEntity.getModuleId(), pageable).getContent();
        for (Question item : listQuestionBankEntity) {
            listDto.add(scheduleShowQuestionConverter.toDto(item));
        }
        response.setListQuestion(listDto);
        return response;
    }

    public Question saveToDb(Question newQuestion, Integer schedule_audit_id,
            Integer schedule_trainee_audit_id) {
        // get audit schedule entity by id
        AuditSchedule auditScheduleEntity = auditScheduleRepository.findAuditScheduleById(schedule_audit_id);

        // get audit report
        AuditReport auditReportEntity = auditScheduleEntity.getAuditReport();

        Set<IndividualAuditReport> listIndiAuditReport = auditReportEntity.getIndividualAuditReports();
        IndividualAuditReport currentIndiAuditReport = new IndividualAuditReport();
        // flag to check individual audit report exist
        boolean flag = false;
        for (IndividualAuditReport item : listIndiAuditReport) {
            if (item.getTraineeId() == schedule_trainee_audit_id) {
                currentIndiAuditReport = item;
                flag = true;
            }
        }
        // create new indiauditreport
        if (flag == false) {
            currentIndiAuditReport.setAssessment(null);
            currentIndiAuditReport.setScore(-1);
            currentIndiAuditReport.setTraineeId(schedule_trainee_audit_id);
            currentIndiAuditReport.setAuditReport(auditReportEntity);
            // currentIndiAuditReport.setAuditQuestions();?
        }
        indiAuditReportRepository.save(currentIndiAuditReport);

        AuditQuestion auditQuestionEntity = new AuditQuestion();
        // set module question = module audit schedule
        newQuestion.setModuleId(auditScheduleEntity.getModuleId());
        // create new auditQuestion
        auditQuestionEntity.setEvaluation(-1);
        auditQuestionEntity.setAssessment(null);
        auditQuestionEntity.setQuestion(newQuestion);

        auditQuestionEntity.setIndividualAuditReport(currentIndiAuditReport);
        // save question
        Question saveQuestion = questionRepository.save(newQuestion);
        auditQuestionRepository.save(auditQuestionEntity);
        return saveQuestion;
    }

    public ScheduleListQuestionAuditResponse saveResultAudit(List<ScheduleQuestionAuditDTO> listAuditQuestionDTO, Integer schedule_audit_id, Integer schedule_audit_trainee_id) {
        ScheduleListQuestionAuditResponse response = new ScheduleListQuestionAuditResponse();
        
        AuditSchedule auditScheduleEntity = auditScheduleRepository.findAuditScheduleById(schedule_audit_id);
        AuditReport auditReportEntity = auditScheduleEntity.getAuditReport();
        int auditReportId = auditReportEntity.getId();
        IndividualAuditReport indiEntity = indiAuditReportRepository.getIndividualAuditReportById(auditReportId,schedule_audit_trainee_id);
        double countScore = 0;
        int count = 0;
        for (ScheduleQuestionAuditDTO item : listAuditQuestionDTO) {
                auditQuestionRepository.updateDb(item.getEvaluation(),item.getAssessment(), item.getQuestion_id(), indiEntity.getId());
                countScore += item.getEvaluation();
                count++;
        }
        indiAuditReportRepository.updateScore(countScore/count, indiEntity.getId());

        response.setListAuditQuestionDTO(listAuditQuestionDTO);
       
        return response;
    }
}

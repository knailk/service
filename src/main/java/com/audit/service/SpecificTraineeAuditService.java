package com.audit.service;

import java.util.List;
import java.util.Set;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.audit.converter.ScheduleShowQuestionConverter;
import com.audit.dto.ScheduleQuestionAuditDTO;
import com.audit.dto.ScheduleQuestionDTO;
import com.audit.entity.AuditQuestion;
import com.audit.entity.AuditReport;
import com.audit.entity.AuditSchedule;
import com.audit.entity.IndividualAuditReport;
import com.audit.entity.Question;
import com.audit.exception.BadRequestException;
import com.audit.exception.NotFoundException;
import com.audit.repository.AuditQuestionRepository;
import com.audit.repository.AuditReportRepository;
import com.audit.repository.AuditScheduleRepository;
import com.audit.repository.IndiAuditReportRepository;
import com.audit.repository.QuestionRepository;
import com.audit.response.ScheduleListQuestionAuditResponse;
import com.audit.response.ScheduleShowQuestionResponse;

import javassist.expr.NewArray;

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
        if (auditScheduleRepository.findById(schedule_audit_id) == null)
            throw new NotFoundException("Not found audit schedule with id = "+ schedule_audit_id +"!");
        AuditSchedule auditScheduleEntity = auditScheduleRepository.findAuditScheduleById(schedule_audit_id);
        List<Question> listQuestionBankEntity = questionRepository
                .getListQuestionByScheduleModule(auditScheduleEntity.getModuleId(), pageable).getContent();
        for (Question item : listQuestionBankEntity) {
            listDto.add(scheduleShowQuestionConverter.toDto(item));
        }
        response.setListQuestion(listDto);
        return response;
    }

    public void saveToDb(List<Integer> listQuestionId, Integer schedule_audit_id,
            Integer schedule_trainee_audit_id) {
        AuditSchedule auditScheduleEntity = auditScheduleRepository.findAuditScheduleById(schedule_audit_id);
        if (auditScheduleEntity == null)
            throw new NotFoundException("Not found audit schedule with id = "+ schedule_audit_id +"!");
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

        // AuditQuestion auditQuestionEntity = new AuditQuestion();
        for (int i : listQuestionId) {
            AuditQuestion auditQuestionEntity = new AuditQuestion();
            auditQuestionEntity.setEvaluation(-1);
            auditQuestionEntity.setAssessment(null);
            Question curQuestion = questionRepository.findOneById(i);
            if (curQuestion == null)
                throw new NotFoundException("The question with id = "+ i +" doesn't exists");
            if (curQuestion.getModuleId() != auditScheduleEntity.getModuleId())
                throw new BadRequestException("The question with id = "+ i +" not in this module!");
            auditQuestionEntity.setQuestion(curQuestion);

            auditQuestionEntity.setIndividualAuditReport(currentIndiAuditReport);
            // save question
            auditQuestionRepository.save(auditQuestionEntity);
        }
        // create new auditQuestion

    }

    @Transactional
    public ScheduleListQuestionAuditResponse saveResultAudit(List<ScheduleQuestionAuditDTO> listAuditQuestionDTO,
            Integer schedule_audit_id, Integer schedule_audit_trainee_id) {
        ScheduleListQuestionAuditResponse response = new ScheduleListQuestionAuditResponse();
        AuditSchedule auditScheduleEntity = auditScheduleRepository.findAuditScheduleById(schedule_audit_id);
        if (auditScheduleEntity == null)
            throw new NotFoundException("Not found audit schedule with id = "+ schedule_audit_id +"!");
        AuditReport auditReportEntity = auditScheduleEntity.getAuditReport();
        int auditReportId = auditReportEntity.getId();
        IndividualAuditReport indiEntity = indiAuditReportRepository.getIndividualAuditReportById(auditReportId,
                schedule_audit_trainee_id);
        if (indiEntity == null)
            throw new NotFoundException("Not found individual audit report!");
        double countScore = 0;
        int count = 0;
        List<AuditQuestion> listAuditQuestion = new ArrayList<>();
            for (ScheduleQuestionAuditDTO item : listAuditQuestionDTO) {
                // auditQuestionRepository.updateDb(item.getEvaluation(), item.getAssessment(),
                // item.getQuestion_id(),
                // indiEntity.getId());
                AuditQuestion curAuditQuestion = new AuditQuestion();
                curAuditQuestion = auditQuestionRepository.findOne(item.getQuestion_id(), indiEntity.getId());
                curAuditQuestion.setAssessment(item.getAssessment());
                curAuditQuestion.setEvaluation(item.getEvaluation());
                listAuditQuestion.add(curAuditQuestion);
                countScore += item.getEvaluation();
                count++;
            }
        auditQuestionRepository.saveAll(listAuditQuestion);
        indiAuditReportRepository.updateScore(countScore / count, indiEntity.getId());
        response.setListAuditQuestionDTO(listAuditQuestionDTO);
        return response;
    }

    public void deleteQuestion(Integer id, Integer schedule_audit_id, Integer trainee_id) {
        if (id == null || schedule_audit_id == null || trainee_id == null)
            throw new BadRequestException("Something is null");
        AuditSchedule auditScheduleEntity = auditScheduleRepository.findAuditScheduleById(schedule_audit_id);
        if (auditScheduleEntity == null)
            throw new NotFoundException("Not found audit schedule with id = "+ schedule_audit_id +"!");
        AuditReport auditReportEntity = auditScheduleEntity.getAuditReport();
        int auditReportId = auditReportEntity.getId();
        IndividualAuditReport indiAuditReportEntity = indiAuditReportRepository
                .getIndividualAuditReportById(auditReportId, trainee_id);
        auditQuestionRepository.deleteAuditQuestion(id, indiAuditReportEntity.getId());
    }
}

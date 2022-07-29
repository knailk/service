package com.audit.service;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.audit.entity.AuditSchedule;
import com.audit.entity.AuditTrainee;
import com.audit.exception.BadRequestException;
import com.audit.exception.NotFoundException;
import com.audit.feignclients.ClassFeignClient;
import com.audit.feignclients.UserFeignClient;
import com.audit.repository.*;
import com.audit.request.AuditScheduleRequest;
import com.audit.response.*;

@Service
public class AuditService {
    @Autowired
    ClassFeignClient classFeignClient;

    @Autowired
    UserFeignClient userFeignClient;
    @Autowired
    AuditScheduleRepository repo;
    @Autowired
    AuditTraineeRepo auditTraineeRepo;

    @Autowired
    CommonService commonService;

    public int countByClassIdAndSession(int class_id, int audit_session) {
        return repo.countByClassIdAndSession(class_id, audit_session);
    }

    public void checkAuditSchedule(AuditScheduleRequest auditScheduleRequest) {
        int class_id = auditScheduleRequest.getClassId();
        if (!commonService.checkAuditor(auditScheduleRequest.getAuditorId())) {
            throw new BadRequestException(
                    "Auditor id = " + auditScheduleRequest.getAuditorId() + " does not exist in the database");
        }
        if (!commonService.checkClass(class_id)) {
            throw new BadRequestException(
                    "Class id = " + class_id + " does not exist in the database");
        }
        if (!commonService.checkModule(auditScheduleRequest.getModuleId())) {
            throw new BadRequestException(
                    "Module id = " + auditScheduleRequest.getModuleId() + " does not exist in the database");
        }
        if (!commonService.checkSkill(auditScheduleRequest.getSkillId())) {
            throw new BadRequestException(
                    "Skill id = " + auditScheduleRequest.getSkillId() + " does not exist in the database");
        }
        if (auditScheduleRequest.getTime().before(new Date()))
            throw new BadRequestException("Time must later than current time");
        Set<Integer> traineesId = userFeignClient.getTraineesId(auditScheduleRequest.getClassId());
        Set<Integer> traineesIdNotExist = new HashSet<Integer>(auditScheduleRequest.getTraineesId());
        traineesIdNotExist.removeAll(traineesId);
        if (!traineesIdNotExist.isEmpty())
            throw new BadRequestException("Trainee with id " + traineesIdNotExist.toString()
                    + " do not exist in class id " + class_id);
        if (classFeignClient.getNumAudit(class_id) < auditScheduleRequest.getSession())
            throw new BadRequestException(
                    "Session " + auditScheduleRequest.getSession() + " not available for class id " + class_id);
    }

    public AuditSchedule createAudit(AuditScheduleRequest auditScheduleRequest) {
        int class_id = auditScheduleRequest.getClassId();
        int session = auditScheduleRequest.getSession();
        checkAuditSchedule(auditScheduleRequest);
        Set<AuditTrainee> auditTrainees = new HashSet<AuditTrainee>();
        auditScheduleRequest.getTraineesId().forEach(id -> {
            auditTrainees.add(new AuditTrainee(null, id));
        });
        AuditSchedule auditSchedule = new AuditSchedule(auditScheduleRequest.getTime(),
                auditScheduleRequest.getAuditorId(), auditScheduleRequest.getSkillId(),
                auditScheduleRequest.getRoom(),
                auditScheduleRequest.getModuleId(), class_id, auditTrainees,
                session, false);
        repo.save(auditSchedule);
        auditSchedule.getAuditTrainees().forEach(trainee -> {
            trainee.setAuditSchedule(auditSchedule);
            auditTraineeRepo.save(trainee);
        });
        return auditSchedule;
    }

    public List<UserNameResponse> getAuditorList() {
        return userFeignClient.getAuditorList();
    }

    public List<UserNameResponse> getTraineeList(int class_id) {
        if (!commonService.checkClass(class_id))
            throw new NotFoundException("Not found class id " + class_id);
        return userFeignClient.getTraineeList(class_id);
    }

    public List<ModuleResponse> getModuleList(int class_id) {
        if (!commonService.checkClass(class_id))
            throw new NotFoundException("Not found class id " + class_id);
        return classFeignClient.getModules(class_id);
    }

    public List<ClassResponse> getClassList() {
        return classFeignClient.getClassList();
    }

    public List<AuditResultResponse> getAuditResultListResponsee(int class_id, int audit_session, Pageable pageable) {
        if (!commonService.checkClass(class_id))
            throw new NotFoundException("Not found class id " + class_id);
        if (classFeignClient.getNumAudit(class_id) < audit_session)
            throw new NotFoundException("Not found session " + audit_session + " of this class id " + class_id);
        return getAuditResultList(class_id, audit_session, pageable);
    }

    public int getNumAudit(int class_id) {
        if (!commonService.checkClass(class_id))
            throw new NotFoundException("Not found class id " + class_id);
        return classFeignClient.getNumAudit(class_id);
    }

    public List<AuditResultResponse> getAuditResultList(int class_id, int audit_session, Pageable pageable) {
        List<UserMailResponse> users = userFeignClient.getUserList();
        List<AuditResult> auditResults = repo.findByClassIdAndSession(class_id, audit_session, pageable).getContent();
        List<AuditResultResponse> auditResultResponses = users.stream().flatMap(v1 -> auditResults.stream()
                .filter(v2 -> v1.getId() == v2.getTrainee_id())
                .map(v2 -> new AuditResultResponse(v2.getIndividual_report_id(),
                        v1.getMail(), v2.getScore(), v1.getName(),
                        v2.getDate_evaluated())))
                .collect(Collectors.toList());
        return auditResultResponses;
    }
}

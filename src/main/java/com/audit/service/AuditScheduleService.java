package com.audit.service;

import com.audit.exception.BadRequestException;
import com.audit.exception.NotFoundException;
import com.audit.feignclients.ClassFeignClient;
import com.audit.feignclients.UserFeignClient;
import com.audit.request.AuditScheduleRequest;
import com.audit.request.MailRequest;
import com.audit.entity.AuditReport;
import com.audit.entity.AuditSchedule;
import com.audit.entity.AuditTrainee;
import com.audit.entity.IndividualAuditReport;
import com.audit.repository.AuditReportRepository;
import com.audit.repository.AuditScheduleRepository;
import com.audit.repository.AuditTraineeRepository;
import com.audit.request.UpdateScheduleRequest;
import com.audit.request.UserNameEmailRequest;
import com.audit.response.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javassist.expr.NewArray;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class AuditScheduleService {
    @Autowired
    AuditScheduleRepository auditScheduleRepository;

    @Autowired
    AuditReportRepository auditReportRepository;

    @Autowired
    AuditTraineeRepository auditTraineeRepository;
    @Autowired
    CommonService commonService;

    @Autowired
    UserFeignClient userFeignClient;
    @Autowired
    ClassFeignClient classFeignClient;

    public long countAll(int id, int role) {
        if(role!=4)
            return auditScheduleRepository.countByIsDeleted();
        else{
            return auditScheduleRepository.countByIsDeletedAU(id);
        }
    }

    public int totalPage(int page, int size, int auditScheduleId) {
        int totalPage = (int) Math.ceil((double) auditTraineeRepository.countByScheduleId(auditScheduleId) / size);
        commonService.checkPage(page, size, totalPage);
        return totalPage;
    }

    ////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////// GET ALL AUDIT SCHEDULE///////////////////////////
    public List<AuditScheduleResponse> getAllAuditSchedule(int id, int role, Pageable pageable) {
        List<AuditScheduleResult> auditSchedules;
        // Find all auditSchedule in database
        if(role != 4) {
            auditSchedules  = auditScheduleRepository.findAllAD(pageable).getContent();
        }
        else{
            auditSchedules = auditScheduleRepository.findAllAU(id, pageable).getContent();
        }
        List<Integer> auditorResults = new ArrayList<>();
        for(AuditScheduleResult auditScheduleItem : auditSchedules){
            auditorResults.add(auditScheduleItem.getAuditor_id());
        }
        Map<Integer,String> auditors = commonService.getUserNameById(auditorResults);
        List<AuditScheduleResponse> auditScheduleResponses = new ArrayList<>();
        for (AuditScheduleResult auditScheduleItem : auditSchedules) {
            // Date and time
            List<String> dateTime = subDateTime(auditScheduleItem.getTime().toString());
            // Response
            String nameAuditor = auditors.get(auditScheduleItem.getAuditor_id());
            if(null == nameAuditor) {
                nameAuditor = "Not Found!";
            }
            auditScheduleResponses.add(new AuditScheduleResponse(auditScheduleItem.getId(), nameAuditor,
                    dateTime.get(0), dateTime.get(1), auditScheduleItem.getRoom()));
        }
        return auditScheduleResponses;
    }

    public List<String> subDateTime(String dateTime) {
        // Date and time
        // Convert date to dd/mm/yyyy
        String date = dateTime.substring(8, 10) + "-" + dateTime.substring(5, 7) + "-" + dateTime.substring(0, 4);
        // Convert time to hh:mm
        String time = dateTime.substring(11, 16);
        return List.of(date, time);
    }

    //////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////// GET DETAIL OF AUDIT SCHEDULE//////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////

    public DetailAuditScheduleResponse getDetailAuditSchedule(int id, int userId, int role, Pageable pageable) {
        // Find AuditSchedule
        if(role !=4){
            throw new BadRequestException("You can not access this data");
        }
        Optional<AuditSchedule> auditScheduleOptional = auditScheduleRepository.findById(id);
        if (auditScheduleOptional.isPresent()) {
            AuditSchedule auditSchedule = auditScheduleOptional.get();
            if(auditSchedule.getAuditorId() != userId){
                throw new BadRequestException("You cannot access this data");
            }
            // Get auditor's name
            Map<Integer, String> auditorName = commonService.getUserNameById(List.of(auditSchedule.getAuditorId()));
            // Get date time
            List<String> dateTime = subDateTime(auditSchedule.getTime().toString());
            AuditReport auditReport;
            // if auditReport not exists, create auditReport
            if (auditSchedule.getAuditReport() == null) {
                auditReport = new AuditReport(-1, auditSchedule, null);
                auditSchedule.setAuditReport(auditReport);
                auditReportRepository.save(auditReport);
            } else {
                auditReport = auditSchedule.getAuditReport();
            }
            List<AuditItemResponse> auditItemResponse = new ArrayList<>();
            List<Integer> traineeIdList = auditTraineeRepository.getAllByIdAudit(id, pageable).getContent();

            Map<Integer,UserNameEmailRequest> trainees = commonService.getUserNameEmailById(traineeIdList);
            Set<IndividualAuditReport> individualAuditReportSet = null;
            if (auditReport.getIndividualAuditReports() != null) {
                individualAuditReportSet = auditReport.getIndividualAuditReports();
            }
            // For list auditTrainee to find auditItemResponse
            for (int traineeId : traineeIdList) {
                // Check if the Individual Audit Report already exists
                List<String> idGrade = new ArrayList<>();
                idGrade.add("-1");
                idGrade.add("-1");
                // Audited
                // Unaudited
                // Auditing
                String evaluate = "Unaudited Yet";
                if (individualAuditReportSet != null) {
                    idGrade = getIndividualAuditReport(individualAuditReportSet, traineeId);
                }
                // If the id is found and the score has not been filled in, an auditing case
                // occurs
                if (!idGrade.get(0).equals("-1")) {
                    if (idGrade.get(1).equals("-1"))
                        evaluate = "Auditing";
                    else
                        evaluate = "Audited";
                }
                UserNameEmailRequest trainee = trainees.get(traineeId);
                if(trainee == null){
                    trainee = new UserNameEmailRequest("Not Found!", "Not Found!");
                }
                // Add detailAuditScheduleResponse to list
                auditItemResponse.add(new AuditItemResponse(Integer.parseInt(idGrade.get(0)), trainee.getName(),
                        traineeId, trainee.getMail(), Double.parseDouble(idGrade.get(1)),
                        evaluate));
            }
            String nameAuditor = auditorName.get(auditSchedule.getAuditorId());
            if(nameAuditor == null){
                nameAuditor = "Not Found!";
            }
            return new DetailAuditScheduleResponse(auditReport.getId(), nameAuditor,
                    dateTime.get(0) + " " + dateTime.get(1), auditSchedule.getRoom(), auditItemResponse);

        } else {
            throw new NotFoundException("Not found AuditSchedule");
        }
    }

    // Check individualAuditReport exists or not, if yes return
    // List<String>(id,score), otherwise return List<String>("-1","-1")
    public List<String> getIndividualAuditReport(Set<IndividualAuditReport> individualAuditReportSet, int idTrainee) {
        for (IndividualAuditReport item : individualAuditReportSet) {
            if (item.getTraineeId() == idTrainee) {
                int id = item.getId();
                double score = item.getScore();
                individualAuditReportSet.remove(item);
                return List.of(Integer.toString(id), Double.toString(score));
            }
        }
        return List.of("-1", "-1");
    }

    /////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////// DELETE AUDIT SCHEDULE//////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////

    public List<StatusDeleteAuditScheduleResponse> deleteAuditSchedule(@NotNull List<Integer> idList) {
        List<StatusDeleteAuditScheduleResponse> statusList = new ArrayList<>();
        // Find AuditSchedule
        for (int id : idList) {
            Optional<AuditSchedule> auditScheduleOptional = auditScheduleRepository.findById(id);
            if (auditScheduleOptional.isPresent()) {
                AuditSchedule auditSchedule = auditScheduleOptional.get();
                auditSchedule.setIsDeleted(true);
                auditScheduleRepository.save(auditSchedule);
                statusList.add(new StatusDeleteAuditScheduleResponse(id, "Success"));
            } else {
                statusList.add(new StatusDeleteAuditScheduleResponse(id, "Failed"));
            }
        }
        return statusList;
    }

    public UpdateScheduleRequest updateAuditSchedule(UpdateScheduleRequest auditScheduleRequest) {
        Optional<AuditSchedule> auditScheduleOptional = auditScheduleRepository.findById(auditScheduleRequest.getId());
        if (auditScheduleOptional.isPresent()) {
            assignAuditSchedule(auditScheduleOptional.get(), auditScheduleRequest);
            return auditScheduleRequest;
        } else {
            throw new NotFoundException("Not found AuditSchedule");
        }
    }

    public void assignAuditSchedule(AuditSchedule auditSchedule, UpdateScheduleRequest auditScheduleRequest) {
        if (checkUpdate(auditScheduleRequest)) {
            auditSchedule.setAuditorId(auditScheduleRequest.getAuditorId());
            auditSchedule.setRoom(auditScheduleRequest.getRoom());
            auditSchedule.setTime(auditScheduleRequest.getTime());

            auditScheduleRepository.save(auditSchedule);
        }
    }

    public boolean checkUpdate(UpdateScheduleRequest updateScheduleRequest) {
        if (!commonService.checkAuditor(updateScheduleRequest.getAuditorId())) {
            throw new BadRequestException(
                    "Auditor id = " + updateScheduleRequest.getAuditorId() + " does not exist in the database");
        }
        if (updateScheduleRequest.getTime().before(new Date())) {
            throw new BadRequestException("Time must later than current time");
        }
        return true;
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
        Set<Integer> traineesIdNotExist = new HashSet<>(auditScheduleRequest.getTraineesId());
        traineesIdNotExist.removeAll(traineesId);
        if (!traineesIdNotExist.isEmpty())
            throw new BadRequestException("Trainee with id " + traineesIdNotExist
                    + " do not exist in class id " + class_id);
        if (classFeignClient.getNumAudit(class_id) < auditScheduleRequest.getSession())
            throw new BadRequestException(
                    "Session " + auditScheduleRequest.getSession() + " not available for class id " + class_id);
    }

    public AuditSchedule createAudit(AuditScheduleRequest auditScheduleRequest) {
        int class_id = auditScheduleRequest.getClassId();
        int session = auditScheduleRequest.getSession();
        checkAuditSchedule(auditScheduleRequest);
        Set<AuditTrainee> auditTrainees = new HashSet<>();
        auditScheduleRequest.getTraineesId().forEach(id -> {
            auditTrainees.add(new AuditTrainee(null, id));
        });
        AuditSchedule auditSchedule = new AuditSchedule(auditScheduleRequest.getTime(),
                auditScheduleRequest.getAuditorId(), auditScheduleRequest.getSkillId(),
                auditScheduleRequest.getRoom(),
                auditScheduleRequest.getModuleId(), class_id, auditTrainees,
                session, false);
        auditScheduleRepository.save(auditSchedule);
        auditSchedule.getAuditTrainees().forEach(trainee -> {
            trainee.setAuditSchedule(auditSchedule);
            auditTraineeRepository.save(trainee);
        });
        return auditSchedule;
    }

    public List<UserNameResponse> getAuditors() {
        return userFeignClient.getAuditorList();
    }

    public List<UserNameResponse> getTrainees(int class_id) {
        if (!commonService.checkClass(class_id))
            throw new NotFoundException("Not found class id " + class_id);
        return userFeignClient.getTraineeList(class_id);
    }

    public List<ModuleResponse> getModules(int class_id) {
        if (!commonService.checkClass(class_id))
            throw new NotFoundException("Not found class id " + class_id);
        return classFeignClient.getModules(class_id);
    }

    public void sendMail(AuditScheduleRequest auditScheduleRequest, int adminId) throws JsonProcessingException{
        List<Integer> listIdTrainees = new ArrayList<>(auditScheduleRequest.getTraineesId());
        Map<Integer,UserNameEmailRequest> listEmailTrainee = userFeignClient.getNameEmailByIds(listIdTrainees);
        int auditorId = auditScheduleRequest.getAuditorId();
        Map<Integer,UserNameEmailRequest> auditorInfo = userFeignClient.getNameEmailByIds(new ArrayList<>(auditorId));
        MailRequest mailRequest = new MailRequest();
        String[] cc = new String[1];
        cc[0] = auditorInfo.get(auditorId).getMail();
        String[] toMail = new String[10];
        Set<Integer> key = listEmailTrainee.keySet();
        int count = 0;
        for (int i : key) {
            toMail[count] = listEmailTrainee.get(i).getMail();
            count++;
        }
        DateFormat dateFormat = new SimpleDateFormat();
        mailRequest.setAuditor(auditorInfo.get(auditorId).getName());
        mailRequest.setToMail(toMail);
        mailRequest.setCc(cc);
        mailRequest.setBcc(null);
        mailRequest.setContent(null);
        mailRequest.setFullName(null);
        mailRequest.setUsername(null);
        mailRequest.setPassword(null);
        mailRequest.setSubject(null);
        mailRequest.setTime(dateFormat.format(auditScheduleRequest.getTime()));
        mailRequest.setLocation(auditScheduleRequest.getRoom());

        ObjectMapper mapper = new ObjectMapper();
        userFeignClient.sendEmailForAuditFeature(adminId, "SCHEDULE_AUDIT", null, mapper.writeValueAsString(mailRequest));
    }
}

package com.audit.service;

import com.audit.exception.BadRequestException;
import com.audit.exception.NotFoundException;
import com.audit.feignclients.ClassFeignClient;
import com.audit.feignclients.UserFeignClient;
import com.audit.request.AuditScheduleRequest;
import com.audit.entity.AuditReport;
import com.audit.entity.AuditSchedule;
import com.audit.entity.AuditTrainee;
import com.audit.entity.IndividualAuditReport;
import com.audit.repository.AuditReportRepository;
import com.audit.repository.AuditScheduleRepository;
import com.audit.repository.AuditTraineeRepository;
import com.audit.request.UpdateScheduleRequest;
import com.audit.request.UserNameEmailRequest;
import com.audit.request.UserNameRequest;
import com.audit.response.*;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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

    public int totalItem() {
        return (int) auditScheduleRepository.count();
    }

    public int totalItem(int id) {
        return auditTraineeRepository.countByScheduleId(id);
    }

    public int totalPage(int page, int size){
        int totalPage = (int) Math.ceil((double) ((int) auditScheduleRepository.count()) / size);
        checkPage(page, size, totalPage);
        return totalPage;
    }

    public int totalPage(int page, int size, int auditScheduleId){
        int totalPage = (int) Math.ceil((double) auditTraineeRepository.countByScheduleId(auditScheduleId) / size);
        checkPage(page, size, totalPage);
        return totalPage;
    }

    public void checkPage(int page, int size, int totalPage){
        if (page > totalPage) {
            throw new NotFoundException("Resource not found!");
        } else if (page < 0 || size < 0) {
            throw new BadRequestException("page or size invalid!");
        }
    }

    ////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////// GET ALL AUDIT SCHEDULE///////////////////////////
    public List<AuditScheduleResponse> getAllAuditSchedule(Pageable pageable) {
        // Find all auditSchedule in database
        List<AuditScheduleResult> auditSchedules = auditScheduleRepository.findAllAD(pageable).getContent();
        List<AuditScheduleResponse> auditScheduleResponses = new ArrayList<>();
        for (AuditScheduleResult auditScheduleItem : auditSchedules) {
            // Name auditor
            UserNameRequest nameAuditor = commonService.getUserNameById(auditScheduleItem.getAuditor_id());
            // Date and time
            List<String> dateTime = subDateTime(auditScheduleItem.getTime().toString());
            // Response
            auditScheduleResponses.add(new AuditScheduleResponse(auditScheduleItem.getId(), nameAuditor.getName(),
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
    /////////////////////////////// GET DETAIL OF AUDIT
    ////////////////////////////////////////////////////////////////////////////////////// SCHEDULE//////////////////////////

    public DetailAuditScheduleResponse getDetailAuditSchedule(int id, Pageable pageable) {
        // Find AuditSchedule
        Optional<AuditSchedule> auditScheduleOptional = auditScheduleRepository.findById(id);
        if (auditScheduleOptional.isPresent()) {
            AuditSchedule auditSchedule = auditScheduleOptional.get();
            // Get auditor's name
            UserNameRequest auditorName = commonService.getUserNameById(auditSchedule.getAuditorId());
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

            Set<IndividualAuditReport> individualAuditReportSet = null;
            if (auditReport.getIndividualAuditReports() != null) {
                individualAuditReportSet = auditReport.getIndividualAuditReports();

            }
            // For list auditTrainee to find auditItemResponse
            for (int traineeId : traineeIdList) {
                // String[] nameEmail = {"haha","haha"};
                UserNameEmailRequest nameEmail = commonService.getUserNameEmailById(traineeId);
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
                    if(idGrade.get(1).equals("-1")) evaluate = "Auditing";
                    else evaluate = "Audited";
                }
                // Add detailAuditScheduleResponse to list
                auditItemResponse.add(new AuditItemResponse(Integer.parseInt(idGrade.get(0)), nameEmail.getName(),
                        traineeId, nameEmail.getEmail(), Double.parseDouble(idGrade.get(1)),
                        evaluate));
            }
            return new DetailAuditScheduleResponse(auditReport.getId(), auditorName.getName(),
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
    ///////////////////////////////// DELETE AUDIT
    ///////////////////////////////////////////////////////////////////////////////////// SCHEDULE//////////////////////////////

    public List<StatusDeleteAuditScheduleResponse> deleteAuditScheduleList(@NotNull List<Integer> idList) {
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

    public StatusDeleteAuditScheduleResponse deleteAuditSchedule(int id) {
        Optional<AuditSchedule> auditScheduleOptional = auditScheduleRepository.findById(id);
        StatusDeleteAuditScheduleResponse status;
        if (auditScheduleOptional.isPresent()) {
            AuditSchedule auditSchedule = auditScheduleOptional.get();
            auditSchedule.setIsDeleted(true);
            auditScheduleRepository.save(auditSchedule);
            status = new StatusDeleteAuditScheduleResponse(id, "Success");
        } else {
            status = new StatusDeleteAuditScheduleResponse(id, "Failed");
        }
        return status;
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

    public boolean checkAuditSchedule(AuditScheduleRequest auditScheduleRequest) {
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
        return true;
    }
}

package com.audit.feignclients;

import com.audit.dto.SkillDTO;
import com.audit.request.UserNameEmailRequest;
import com.audit.response.UserMailResponse;
import com.audit.response.UserNameResponse;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@FeignClient(value = "api-gateway/user-service/")
public interface UserFeignClient {
    // @GetMapping("api/skill/name/{id}")
    // String getSkill(@PathVariable Integer id);

    // @GetMapping("api/skill/id/{name}")
    // Integer getId(@PathVariable String name);

    // @GetMapping("api/skill/list")
    // List<String> getSkillList();

    @GetMapping("api/skills")
    List<SkillDTO> getSkills();

    @PostMapping("api/user/name")
    Map<Integer, String> getNameByIds(@RequestBody List<Integer> ids);

    @PostMapping("api/user/name-email")
    Map<Integer,UserNameEmailRequest> getNameEmailByIds(@RequestBody List<Integer> ids);

    @GetMapping("api/auditor/{id}/check")
    boolean checkAuditor(@PathVariable int id);

    @GetMapping("api/skill/{id}/check")
    boolean checkSkill(@PathVariable int id);

    @GetMapping("/api/user/")
    List<UserMailResponse> getUserList();

    @GetMapping("/api/user/auditors/")
    List<UserNameResponse> getAuditorList();

    @GetMapping("/api/user/{class_id}/trainees/")
    List<UserNameResponse> getTraineeList(@PathVariable int class_id);

    @GetMapping("/api/user/{class_id}/traineesId/")
    Set<Integer> getTraineesId(@PathVariable int class_id);

    @GetMapping("/api/user/trainees/")
    List<UserMailResponse> getTraineesInfo(@RequestParam List<Integer> traineesId);

    @PostMapping("/api/admins/{adminId}/mails")
    public void sendEmailForAuditFeature(
        @PathVariable Integer adminId, @RequestParam(name = "feature_type", required = false) String featureType,
        @RequestParam(name = "file", required = false) MultipartFile file,
        @RequestParam(name = "mailRequest", required = false) String jsonMailRequest
        );

}

package com.audit.feignclients;

import com.audit.request.UserNameEmailRequest;
import com.audit.request.UserNameRequest;
import com.audit.response.UserMailResponse;
import com.audit.response.UserNameResponse;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@FeignClient(value = "api-gateway/user-service/")
public interface UserFeignClient {
    @GetMapping("api/skill/name/{id}")
    String getSkill(@PathVariable Integer id);

    @GetMapping("api/skill/id/{name}")
    Integer getId(@PathVariable String name);

    @GetMapping("api/skill/list")
    List<String> getSkillList();

    @GetMapping("api/user_name/{id}")
    public UserNameRequest getNameById(@PathVariable int id);

    @GetMapping("api/user_name-email/{id}")
    public UserNameEmailRequest getNameEmailById(@PathVariable int id);

    @GetMapping("api/check_auditor/{id}")
    public boolean checkAuditor(@PathVariable int id);

    @GetMapping("api/check_trainee/{id}")
    public boolean checkTrainee(@PathVariable int id);

    @GetMapping("api/check_skill/{id}")
    public boolean checkSkill(@PathVariable int id);

    @GetMapping("/api/user/")
    public List<UserMailResponse> getUserList();

    @GetMapping("/api/user/auditors/")
    public List<UserNameResponse> getAuditorList();

    @GetMapping("/api/user/{class_id}/trainees/")
    public List<UserNameResponse> getTraineeList(@PathVariable int class_id);

    @GetMapping("/api/user/{class_id}/traineesId/")
    public Set<Integer> getTraineesId(@PathVariable int class_id);

}

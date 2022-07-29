package com.audit.feignclients;

import java.util.List;

import com.audit.dto.ModuleDTO;
import com.audit.dto.SkillDTO;
import com.audit.request.UserNameEmailRequest;
import com.audit.request.UserNameRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.audit.response.UserNameResponse;
import com.audit.response.ClassResponse;
import com.audit.response.ModuleResponse;
import com.audit.response.UserMailResponse;

@FeignClient(value = "api-gateway")
public interface CommonFeignClient {

    @GetMapping("class-service/api/trainings/modules")
    List<ModuleDTO> getModules();

    @GetMapping("user-service/api/skills")
    List<SkillDTO> getSkills();

}

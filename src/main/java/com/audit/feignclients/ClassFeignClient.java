package com.audit.feignclients;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import com.audit.dto.ModuleDTO;
import com.audit.response.ClassResponse;
import com.audit.response.ModuleResponse;

@FeignClient(value = "api-gateway/class-service/")
public interface ClassFeignClient {

    // @GetMapping("api/training/module/name/{id}")
    // String getModule(@PathVariable Integer id);

    // @GetMapping("api/training/module/id/{name}")
    // Integer getId(@PathVariable String name);

    // @GetMapping("api/training/module/list")
    // List<String> getListModule();

    @GetMapping("api/trainings/modules")
    List<ModuleDTO> getModules();

    @GetMapping("api/module/{id}/check")
    boolean checkModule(@PathVariable int id);

    @GetMapping("api/class/{id}/check")
    boolean checkClass(@PathVariable int id);

    @GetMapping("/api/audit/classes")
    List<ClassResponse> getClasses();

    @GetMapping("api/learning-paths/{class_id}/num-audit")
    int getNumAudit(@PathVariable int class_id);

    @GetMapping("api/learning-paths/{class_id}/modules")
    List<ModuleResponse> getModules(@PathVariable int class_id);

}

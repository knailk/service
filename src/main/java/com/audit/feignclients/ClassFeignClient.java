package com.audit.feignclients;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import com.audit.response.ClassResponse;
import com.audit.response.ModuleResponse;

@FeignClient(value = "api-gateway/class-service/")
public interface ClassFeignClient {

    @GetMapping("api/training/module/name/{id}")
    String getModule(@PathVariable Integer id);

    @GetMapping("api/training/module/id/{name}")
    Integer getId(@PathVariable String name);

    @GetMapping("api/training/module/list")
    List<String> getListModule();

    @GetMapping("api/check_module/{id}")
    public boolean checkModule(@PathVariable int id);

    @GetMapping("api/check_class/{id}")
    public boolean checkClass(@PathVariable int id);

    @GetMapping("api/check_session/{id}")
    public boolean checkSession(@PathVariable int id);

    @GetMapping("/api/getClasses")
    public List<ClassResponse> getClassList();

    @GetMapping("api/learningPaths/{class_id}/num-audit")
    public int getNumAudit(@PathVariable int class_id);

    @GetMapping("api/learningPaths/{class_id}/modules")
    public List<ModuleResponse> getModules(@PathVariable int class_id);

}

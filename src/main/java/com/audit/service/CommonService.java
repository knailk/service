package com.audit.service;

import com.audit.dto.*;
import com.audit.exception.*;
import com.audit.feignclients.ClassFeignClient;
import com.audit.feignclients.CommonFeignClient;
import com.audit.feignclients.UserFeignClient;
import com.audit.request.UserNameEmailRequest;
import com.audit.request.UserNameRequest;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommonService {
    Logger logger = LoggerFactory.getLogger(CommonService.class);
    long count = 1;
    @Autowired
    UserFeignClient userFeignClient;

    @Autowired
    ClassFeignClient classFeignClient;

    @Autowired
    CommonFeignClient feignclient;

    @CircuitBreaker(name = "userService", fallbackMethod = "fallbackGetUserNameById")
    public UserNameRequest getUserNameById(Integer id){
        logger.info("count = "+count);
        count++;
        return userFeignClient.getNameById(id);
    }

    public UserNameRequest fallbackGetUserNameById(Throwable th){
        logger.error("error" + th);
        return new UserNameRequest("Not found!");
    }

    @CircuitBreaker(name = "userService", fallbackMethod = "fallbackGetUserNameEmailById")
    public UserNameEmailRequest getUserNameEmailById(Integer id){
        logger.info("count = "+count);
        count++;
        return userFeignClient.getNameEmailById(id);
    }

    public UserNameEmailRequest fallbackGetUserNameEmailById(Throwable th){
        logger.error("error" + th);
        return new UserNameEmailRequest("Not found!", "Not found!");
    }

    @CircuitBreaker(name = "userService", fallbackMethod = "fallbackCheck")
    public boolean checkModule(int id){
        logger.info("count = "+count);
        count++;
        return classFeignClient.checkModule(id);
    }

    @CircuitBreaker(name = "userService", fallbackMethod = "fallbackCheck")
    public boolean checkClass(int id){
        logger.info("count = "+count);
        count++;
        return classFeignClient.checkClass(id);
    }

    @CircuitBreaker(name = "userService", fallbackMethod = "fallbackCheck")
    public boolean checkSession(int id){
        logger.info("count = "+count);
        count++;
        return classFeignClient.checkSession(id);
    }

    @CircuitBreaker(name = "userService", fallbackMethod = "fallbackCheck")
    public boolean checkAuditor(int id){
        logger.info("count = "+count);
        count++;
        return userFeignClient.checkAuditor(id);
    }

    @CircuitBreaker(name = "userService", fallbackMethod = "fallbackCheck")
    public boolean checkTrainee(int id){
        logger.info("count = "+count);
        count++;
        return userFeignClient.checkTrainee(id);
    }

    @CircuitBreaker(name = "userService", fallbackMethod = "fallbackCheck")
    public boolean checkSkill(int id){
        logger.info("count = "+count);
        count++;
        return userFeignClient.checkSkill(id);
    }

    public boolean fallbackCheck(Throwable th){
        logger.error("error" + th);
        return false;
    }


    
    @CircuitBreaker(name = "classService", fallbackMethod = "getModuleListFallback")
    public List<ModuleDTO> getModuleList() {
        logger.info("count = "+count);
        count++;
        List<ModuleDTO> modules = feignclient.getModules();
        if (modules.isEmpty()) throw new NotFoundException("The list of modules is empty.");
        return modules;
    }

    @SuppressWarnings("unused")
    private List<ModuleDTO> getModuleListFallback(Throwable th) {
        logger.error("error" + th);
        return new ArrayList<>();
    }

    @CircuitBreaker(name = "userService", fallbackMethod = "getSkillListFallback")
    public List<SkillDTO> getSkillList() {
        logger.info("count = "+count);
        count++;
        List<SkillDTO> skills = feignclient.getSkills();
        if (skills.isEmpty()) throw new NotFoundException("The list of skill is empty.");
        return skills;
    }

    @SuppressWarnings("unused")
    private List<SkillDTO> getSkillListFallback(Throwable th) {
        logger.error("error" + th);
        return new ArrayList<>();
    }

}

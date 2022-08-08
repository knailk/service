package com.audit.service;

import com.audit.dto.*;
import com.audit.exception.*;
import com.audit.feignclients.ClassFeignClient;
import com.audit.feignclients.UserFeignClient;
import com.audit.request.UserNameEmailRequest;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @CircuitBreaker(name = "userService", fallbackMethod = "fallbackGetUserNameById")
    public Map<Integer, String> getUserNameById(List<Integer> ids) {
        logger.info("count = " + count);
        count++;
        return userFeignClient.getNameByIds(ids);
    }

    public Map<Integer, String> fallbackGetUserNameById(List<Integer> ids, Throwable th) {
        logger.error("error" + th);
        Map<Integer, String> results = new HashMap<>();
        for(int id : ids){
            results.put(id, "Not found!");
        }
        return results;
    }

    @CircuitBreaker(name = "userService", fallbackMethod = "fallbackGetUserNameEmailById")
    public Map<Integer, UserNameEmailRequest> getUserNameEmailById(List<Integer> ids) {
        logger.info("count = " + count);
        count++;
        return userFeignClient.getNameEmailByIds(ids);
    }

    public Map<Integer, UserNameEmailRequest> fallbackGetUserNameEmailById(List<Integer> ids, Throwable th) {
        logger.error("error" + th);
        Map<Integer, UserNameEmailRequest> results = new HashMap<>();
        for(int id : ids){
            results.put(id, new UserNameEmailRequest("Not found!", "Not found!"));
        }
        return results;
    }

    @CircuitBreaker(name = "userService", fallbackMethod = "fallbackCheck")
    public boolean checkModule(int id) {
        logger.info("count = " + count);
        count++;
        return classFeignClient.checkModule(id);
    }

    @CircuitBreaker(name = "userService", fallbackMethod = "fallbackCheck")
    public boolean checkClass(int id) {
        logger.info("count = " + count);
        count++;
        return classFeignClient.checkClass(id);
    }

    @CircuitBreaker(name = "userService", fallbackMethod = "fallbackCheck")
    public boolean checkAuditor(int id) {
        logger.info("count = " + count);
        count++;
        return userFeignClient.checkAuditor(id);
    }

    @CircuitBreaker(name = "userService", fallbackMethod = "fallbackCheck")
    public boolean checkSkill(int id) {
        logger.info("count = " + count);
        count++;
        return userFeignClient.checkSkill(id);
    }

    public boolean fallbackCheck(Throwable th) {
        logger.error("error" + th);
        return false;
    }

    @CircuitBreaker(name = "classService", fallbackMethod = "getModuleListFallback")
    public List<ModuleDTO> getModuleList() {
        logger.info("count = " + count);
        count++;
        List<ModuleDTO> modules = classFeignClient.getModules();
        if (modules.isEmpty())
            throw new NotFoundException("The list of modules is empty.");
        return modules;
    }

    @SuppressWarnings("unused")
    private List<ModuleDTO> getModuleListFallback(Throwable th) {
        logger.error("error" + th);
        return new ArrayList<>();
    }

    @CircuitBreaker(name = "userService", fallbackMethod = "getSkillListFallback")
    public List<SkillDTO> getSkillList() {
        logger.info("count = " + count);
        count++;
        List<SkillDTO> skills =  userFeignClient.getSkills();
        if (skills.isEmpty())
            throw new NotFoundException("The list of skill is empty.");
        return skills;
    }

    @SuppressWarnings("unused")
    private List<SkillDTO> getSkillListFallback(Throwable th) {
        logger.error("error" + th);
        return new ArrayList<>();
    }

    public int totalPage(int page, int size, long count) {
        int totalPage = (int) Math.ceil((double) count / size);
        checkPage(page, size, totalPage);
        return totalPage;
    }

    public void checkPage(int page, int size, int totalPage) {
        if (page > totalPage) {
            throw new NotFoundException("Resource not found!");
        } else if (page < 0 || size < 0) {
            throw new BadRequestException("page or size invalid!");
        }
    }

}

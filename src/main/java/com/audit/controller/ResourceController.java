package com.audit.controller;

import com.audit.dto.QuestionDTO;
import com.audit.exception.BadRequestException;
import com.audit.exception.NotFoundException;
import com.audit.request.QuestionCreateRequest;
import com.audit.response.ListShowResponse;
import com.audit.response.ResponseObject;
import com.audit.service.QuestionService;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;



@RestController
@RequestMapping(path = "api/question")
public class ResourceController {

    @Autowired
    private QuestionService questionService;

    @GetMapping
    ResponseEntity<?> getQuestions(@RequestParam(value = "page", required = false) Integer page,
                                    @RequestParam(value = "size", required = false) Integer limit) {
      if(page == null) page = 1;
      if (limit == null) limit = 50;
      if (page <= 0 || limit <= 0) throw new BadRequestException("Page or size is invalid");
      Pageable pageable = PageRequest.of(page - 1,limit,Sort.by("id").ascending());
      int totalPage = (int) Math.ceil((double) (questionService.countQuestion()) / limit);
      if (page > totalPage) 
        throw new NotFoundException("Resource not found"); 
      List<?> listQuestion =  questionService.getQuestions(pageable);
      return ResponseEntity.status(HttpStatus.OK).body( 
          new ListShowResponse(listQuestion, page, limit, totalPage)
      );
      
    } 

    @GetMapping("/{id}")
    ResponseEntity<?> findById(@PathVariable Integer id) {
      return ResponseEntity.status(HttpStatus.OK).body(
        questionService.getQuestionById(id)
      );
    }

    @PostMapping
    ResponseEntity<?> createQuestion(@Valid @RequestBody QuestionCreateRequest question) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
          new ResponseObject(false, "Create question successfully", 
               questionService.createQuestion(question)
          )
        );
    }

    @PutMapping
    ResponseEntity<?> updateQuestion(@RequestBody QuestionDTO question) {
      return ResponseEntity.status(HttpStatus.OK).body(
        new ResponseObject(false, "Update question successfully", 
              questionService.updateQuestion(question)
        )
      );
  }

    @DeleteMapping("/{id}")
    ResponseEntity<?> deleteQuestion(@PathVariable Integer id) {
      questionService.deleteQuestion(id);
      return ResponseEntity.status(HttpStatus.OK).body(
        new ResponseObject(false, "Delete question successfully", null)
      );
    }

    @DeleteMapping
    ResponseEntity<?> deleteQuestions(@RequestBody List<Integer> list) {
      List<Integer> res = new ArrayList<>();
      List<Boolean> isActive = questionService.deleteQuestions(list); 
      for(Integer i = 0; i < list.size(); i++) {
        if (Boolean.FALSE.equals(isActive.get(i))) 
          res.add(list.get(i));
      }
      if (!res.isEmpty()) return ResponseEntity.status(HttpStatus.OK).body(
        new ResponseObject(true, "Deletion of question list is fail with the following id",res)
      );

      return ResponseEntity.status(HttpStatus.OK).body(
        new ResponseObject(false, "Deletion of question list is successful",null)
      );
    }

    @GetMapping("/module") 
    ResponseEntity<?> getModules() {
      return ResponseEntity.status(HttpStatus.OK).body(
        questionService.getModules()
      );
    }

    @GetMapping("/skill") 
    ResponseEntity<?> getSkills() {
      return ResponseEntity.status(HttpStatus.OK).body(
        questionService.getSkills()
      );
    }

 }

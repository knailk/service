package com.audit.controller;

import com.audit.dto.ModuleDTO;
import com.audit.dto.QuestionDTO;
import com.audit.entity.Question;
import com.audit.exception.BadRequestException;
import com.audit.request.QuestionCreateRequest;
import com.audit.response.ListShowResponse;
import com.audit.response.ResponseObject;
import com.audit.service.CommonService;
import com.audit.service.QuestionService;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;



@RestController
@RequestMapping(path = "api/question")
public class ResourceController {

    @Autowired
    private QuestionService questionService;
    @Autowired
    private CommonService commonService;

    @GetMapping
    ResponseEntity<?> getAllQuestions(){
      return ResponseEntity.status(HttpStatus.OK).body(
        // new ResponseObject(false, "Get all of questions", questionService.toDto(questionService.getAllQuestion()))
        questionService.toDto(questionService.getAllQuestion())
      );
    }
    @GetMapping("/")
    ResponseEntity<?> getAllQuestions(@RequestParam(value = "page",required = true) Integer page,
                                    @RequestParam(value = "size",required = false) Integer limit) {
      if(page == null || page <= 0 )
        throw new  BadRequestException("Wrong page value");
      if (limit == null || limit <= 0) limit = 50;

      Pageable pageable = PageRequest.of(page - 1,limit,Sort.by("id").ascending());
      int totalPage = (int) Math.ceil((double) (questionService.countQuestion()) / limit);
      if (page > totalPage) 
        throw new  BadRequestException("Wrong page value"); 
      List<Question> listQuestion =  questionService.getAllQuestion(pageable);
      return ResponseEntity.status(HttpStatus.OK).body(
        // new ResponseObject(false, "Get page "+page+ " of limit "+limit+ " of question list", 
          new ListShowResponse(
            questionService.toDto(listQuestion), 
            page, 
            limit, 
            totalPage
          )
        // )

        
      );
      
    } 

    @GetMapping("/{id}")
    ResponseEntity<?> findById(@PathVariable Integer id) {
      // if (bindingResult.hasErrors()) throw new BadRequestException("The question id is invalid");
      return ResponseEntity.status(HttpStatus.OK).body(
        // new ResponseObject(false, "Get question with id = "+id, questionService.toDto(questionService.getQuestionById(id)))
        questionService.toDto(questionService.getQuestionById(id))
      );
    }

    @PostMapping
    ResponseEntity<?> createQuestion(@Valid @RequestBody QuestionCreateRequest question) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
          new ResponseObject(false, "Create question successfully", 
                questionService.toDto(questionService.createQuestion(questionService.toEnity(question)))
          )
        );
    }

    @PutMapping
    ResponseEntity<?> updateQuestion(@RequestBody QuestionDTO question) {
      return ResponseEntity.status(HttpStatus.OK).body(
        new ResponseObject(false, "Update question successfully", 
                questionService.toDto(questionService.updateQuestion(questionService.toEnity(question)))
        )
      );
  }

    @DeleteMapping("/{id}")
    ResponseEntity<?> deleteQuestion(@Valid @PathVariable Integer id) {
      questionService.deleteQuestion(id);
      return ResponseEntity.status(HttpStatus.OK).body(
        new ResponseObject(false, "Delete question successfully", null)
      );
    }

    @DeleteMapping
    ResponseEntity<?> deleteQuestionList(@RequestBody List<Integer> list) {
      List<Integer> res = new ArrayList<>();
      List<Boolean> isActive = questionService.deleteQuestionList(list); 
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
    ResponseEntity<?> getListModule() {
      return ResponseEntity.status(HttpStatus.OK).body(
        // new ResponseObject(false, "Get list of module",  questionService.getListModule() )
        questionService.getListModule()
      );
    }

    @GetMapping("/skill") 
    ResponseEntity<?> getListSkill() {
      return ResponseEntity.status(HttpStatus.OK).body(
        // new ResponseObject(false, "Get list of skill",  questionService.getListSkill() )
        questionService.getListSkill()
      );
    }

    // @GetMapping("/module/")
    // ResponseEntity<ResponseObject> getQuestionByModule(@RequestParam("name") String name) {
    //   return ResponseEntity.status(HttpStatus.OK).body(
    //     new ResponseObject(false, "Get list of question filled by module name.", 
    //                                                       questionService.findQuestionByModule(name) )
    //   );
    // }

    // @GetMapping("/skill/")
    // ResponseEntity<ResponseObject> getQuestionBySkill(@RequestParam("name") String name) {
    //   return ResponseEntity.status(HttpStatus.OK).body(
    //     new ResponseObject(false, "Get list of question filled by skill name.", 
    //                                                       questionService.findQuestionBySkill(name) )
    //   );
    // }
    // @GetMapping("/moduleSkill")
    // ResponseEntity<ResponseObject> getQuestionByModuleSkill(@RequestParam("module") String module,
    //                                                         @RequestParam("skill") String skill) {
    //   return ResponseEntity.status(HttpStatus.OK).body(
    //     new ResponseObject(false, "Get list of question filled by skill name.", 
    //                                                       questionService.findQuestionByModuleSkill(module, skill) )
    //   );
    // }

  

  
    
 }

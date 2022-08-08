package com.audit.service;

import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.audit.converter.QuestionConverter;
import com.audit.dto.ModuleDTO;
import com.audit.dto.QuestionDTO;
import com.audit.dto.SkillDTO;
import com.audit.entity.Question;
import com.audit.exception.*;
import com.audit.repository.QuestionRepository;
import com.audit.request.QuestionCreateRequest;



@Service
public class QuestionService {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private CommonService commonService;

    @Autowired
    private QuestionConverter questionConverter;

    private final Logger logger = LoggerFactory.getLogger(QuestionService.class);

    public int countQuestion() {
        return questionRepository.countQuestion();
    }

    public List<?> getQuestions(Pageable pageable) {
        if (countQuestion() == 0) {
            throw new NotFoundException("Not found any question");
        }
        try {
            return questionConverter.toDto( questionRepository.findAll(pageable).getContent() );
        }
        catch (Exception e) {
            throw new InternalServerException("Error in finding question");
        }
    }

    public QuestionDTO getQuestionById(Integer id) {
        if (id == null) throw new BadRequestException("The question id is null");
        Optional<Question> foundQuestion = questionRepository.findById(id);
        if (!foundQuestion.isPresent()) throw new NotFoundException("The question with id = "+ id +" doesn't exists");
        return questionConverter.toDto( foundQuestion.get() );
    }

    public QuestionDTO createQuestion(QuestionCreateRequest newQuestion) {
        if (newQuestion == null) throw new BadRequestException("The question is null");
        Question question = questionConverter.toEnity(newQuestion); 
        if (question.getSkillId() < 0) throw new BadRequestException("The skill id is unvalid");
        if (question.getModuleId() < 0) throw new BadRequestException("The module id is unvalid");
        Optional<Question> foundQuestion = questionRepository.findById(question.getId());
        if (foundQuestion.isPresent()) throw new NotFoundException("The question with id = "+ question.getId() +" has already existed.");
        else return questionConverter.toDto( questionRepository.save(question) );
    }

    public QuestionDTO updateQuestion(QuestionDTO questionDto) {
        if (questionDto == null) throw new BadRequestException("The question is null");
        Question question = questionConverter.toEnity(questionDto);
        Optional<Question> foundQuestion = questionRepository.findById(question.getId());
        if (foundQuestion.isPresent()) {
            Question question2 = foundQuestion.get();
            if (question2.getSkillId() < 0) throw new BadRequestException("The skill id is unvalid");
            if (question2.getModuleId() < 0) throw new BadRequestException("The module id is unvalid");
            question2.setContent(question.getContent());
            question2.setSolution(question.getSolution());
            question2.setLevel(question.getLevel());
            question2.setModuleId(question.getModuleId());
            question2.setSkillId(question.getSkillId());
            question2.setStatus(question.getStatus());
        }
        else {
            throw new NotFoundException("The question with id = "+ question.getId() +" doesn't exist.");
        }
        return questionConverter.toDto( questionRepository.save( foundQuestion.get() ) );
    }

    public void deleteQuestion(Integer id) {
        if (id == null) throw new BadRequestException("The question id is null");
        Optional<Question> foundQuestion = questionRepository.findById(id);
        if (foundQuestion.isPresent()) {
            foundQuestion.get().setIsDeleted(true);
            questionRepository.save(foundQuestion.get());
        }
        else throw new NotFoundException("This question id = "+ id +" doesn't exists");
    }
    public List<Boolean> deleteQuestions(List<Integer> list) {
        if (list == null) throw new BadRequestException("The question list is null");
        if (list.isEmpty()) throw new BadRequestException("The question list is empty");
        List<Boolean> res = new ArrayList<>();
        for (Integer id : list) {
            Optional<Question> foundQuestion = questionRepository.findById(id);
            if (foundQuestion.isPresent()) {
                foundQuestion.get().setIsDeleted(true);
                questionRepository.save(foundQuestion.get());
                res.add(true);
            }
            else res.add(false);
        }
        return res;
    }

    //Class Service
    public String getModule(Integer id) {
        if (id == null) throw new BadRequestException("The module id is null");
        if (id < 0) throw new BadRequestException("The module id is unvalid");
        List<ModuleDTO> modules = commonService.getModuleList();
        if (modules.isEmpty()) return "Not found";
        for (ModuleDTO module : modules) {
            if (module.getId().equals(id)) return module.getName();
        }
        throw new com.audit.exception.NotFoundException("The module with id = "+id+" doesn't exist");
    }

    public Integer getModuleId(String name) {
        if (name == null) throw new BadRequestException("The module name is null");
        List<ModuleDTO> modules = commonService.getModuleList();
        if (name.equals("Not found")) throw new BadRequestException("The module name is unvalid");
        if (modules.isEmpty()) return -1;
        for (ModuleDTO module : modules) {
            if (module.getName().equals(name)) return module.getId();
        }
        throw new com.audit.exception.NotFoundException("The module with name = "+name+" doesn't exist");

    }

    public List<String> getModules() {
        List<ModuleDTO> modules = commonService.getModuleList();
        List<String> res = new ArrayList<>();
        for (ModuleDTO module : modules) {
            res.add(module.getName());
        }
        return res;
    }

    //User Service
    public String getSkill(Integer id) {
        if (id == null) throw new BadRequestException("The skill id is null");
        if (id < 0) throw new BadRequestException("The skill id is unvalid");
        List<SkillDTO> skills = commonService.getSkillList();
        if (skills.isEmpty()) return "Not found";
        for (SkillDTO skill : skills) {
            if (skill.getId().equals(id)) return skill.getName();
        }
        throw new com.audit.exception.NotFoundException("The skill with id = "+id+" doesn't exist");
    }

    public Integer getSkillId(String name) {
        if (name == null) throw new BadRequestException("The skill name is null");
        List<SkillDTO> skills = commonService.getSkillList();
        if (skills.isEmpty()) return -1;
        for (SkillDTO skill : skills) {
            if (skill.getName().equals(name)) return skill.getId();
        }
        throw new com.audit.exception.NotFoundException("The skill with name = "+name+" doesn't exist");
    }

    public List<String> getSkills() {
        List<SkillDTO> skills = commonService.getSkillList();
        List<String> res = new ArrayList<>();
        for (SkillDTO skill : skills) {
            res.add(skill.getName());
        }
        return res;
    }

}

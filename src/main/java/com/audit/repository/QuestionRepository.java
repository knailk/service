package com.audit.repository;

import com.audit.entity.Question;

import java.util.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Integer> {
    @Query(value = "SELECT * FROM question_bank WHERE is_deleted = false ORDER BY id", nativeQuery = true)
    List<Question> findAll();
    @Query(value = "SELECT * FROM question_bank WHERE id = ?1 AND is_deleted = false", nativeQuery = true)
    Optional<Question> findById(Integer id);
    @Query(value = "SELECT count(id) FROM question_bank WHERE is_deleted = false", nativeQuery = true)
    int countQuestion();
    @Query(value = "SELECT * FROM question_bank WHERE is_deleted = false", nativeQuery = true)
    Page<Question> findAll(Pageable pageable);
    // @Query(value = "SELECT * FROM question_bank WHERE module_id = ?1 AND is_deleted = false", nativeQuery = true)
    // List<Question> findByModuleId(Integer id);
    // @Query(value = "SELECT * FROM question_bank WHERE skill_id = ?1 AND is_deleted = false", nativeQuery = true)
    // List<Question> findBySkillId(Integer id);
    // @Query(value = "SELECT * FROM question_bank WHERE module_id = ?1 AND skill_id = ?2 AND is_deleted = false", nativeQuery = true)
    // List<Question> findByModuleSkillId(Integer module, Integer skill);
    @Query(value = "select * from question_bank where module_id = ?1 and is_deleted = false",nativeQuery = true)
    Page<Question> getListQuestionByScheduleModule(Integer module_id,Pageable pageable);
    @Query(value = "select * from question_bank where content = ?1 and is_deleted = false",nativeQuery = true)
    Question findByContent(String content);
}

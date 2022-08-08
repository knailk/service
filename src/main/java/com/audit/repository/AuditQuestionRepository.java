package com.audit.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.audit.entity.AuditQuestion;

@Repository
public interface AuditQuestionRepository extends JpaRepository<AuditQuestion, Integer> {
    @Query(value = "select * from audit_question where individual_report_id = ?1",nativeQuery = true)
	List<AuditQuestion> findByIndividualReportId(Integer id);

    @Query(value = "select count(id) from audit_question where id = ?1", nativeQuery = true)
    int isExist(Integer id);

    @Query(value = "update audit_question set evaluation = ?1, assessment =?2 where question_id =?3 and individual_report_id = ?4",nativeQuery = true)
    void updateDb(Double evaluation, String assessment, Integer question_id, Integer indi_id);
    
    @Query(value = "delete from audit_question where question_id = ?1 and individual_report_id = ?2", nativeQuery = true)
    void deleteAuditQuestion(Integer id, Integer indi_id);

    @Query(value = "select * from audit_question where question_id = ?1  and individual_report_id = ?2",nativeQuery = true)
    AuditQuestion findOne(Integer question_id, Integer indi_id);
}

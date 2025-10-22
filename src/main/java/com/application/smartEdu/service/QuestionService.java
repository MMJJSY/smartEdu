package com.application.smartEdu.service;

import java.sql.SQLException;
import java.util.List;

import com.application.smartEdu.dto.CoursesVO;
import com.application.smartEdu.dto.QuestionVO;

public interface QuestionService {
    
    // 질문 등록
    void registQuestion(QuestionVO question) throws SQLException;
    // 질문 목록
    List<QuestionVO> getQuestionList(String role, int memberId) throws SQLException;
    // 상세보기
    QuestionVO getQuestionDetail(int questionId) throws SQLException;
    // 질문수정
    void modifyQuestion(QuestionVO question) throws SQLException;
    // 질문삭제
    void deleteQuestion(int questionId) throws SQLException;
    // 조회수증가
    void increaseViewCount(int questionId) throws SQLException;
    // 강좌 목록(수강생이 구매한)
    List<CoursesVO> getCourseBystudent(int studentId) throws SQLException;
}

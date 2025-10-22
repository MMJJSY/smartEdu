package com.application.smartEdu.dao;

import java.sql.SQLException;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.application.smartEdu.dto.CoursesVO;
import com.application.smartEdu.dto.QuestionVO;

public interface QuestionDAO {

    //질문등록
    void insertQuestion(QuestionVO question) throws SQLException;
    //질문리스트
    List<QuestionVO> selectQuestionList(@Param("role") String role, 
                                        @Param("memberId") int memberId) throws SQLException;
    //질문상세
    QuestionVO selectQuestionById(@Param("questionId")int questionId) throws SQLException;
    //조회수증가
    void updateViewCount(int questionId) throws SQLException;
    //질문수정
    void updateQuestion(QuestionVO question) throws SQLException;
    //질문삭제
    void deleteQuestion(int questionId) throws SQLException;
    // 강좌 조회(수강생 구매)
    List<CoursesVO> selectCourseByStudent(int studentId) throws SQLException;

}

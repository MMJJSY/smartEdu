package com.application.smartEdu.dao;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.application.smartEdu.dto.CoursesVO;
import com.application.smartEdu.dto.QuestionVO;

@Repository
public class QuestionDAOImpl implements QuestionDAO {

    @Autowired
    private SqlSession sqlSession;

    @Override
    public void deleteQuestion(int questionId) throws SQLException {
        sqlSession.update("Question-Mapper.deleteQuestion", questionId);
    }

    @Override
    public void insertQuestion(QuestionVO question) throws SQLException {
        sqlSession.insert("Question-Mapper.insertQuestion", question);
        
    }

    @Override
    public QuestionVO selectQuestionById(int questionId) throws SQLException {
        return sqlSession.selectOne("Question-Mapper.selectQuestionById", questionId);
    }

    @Override
    public List<QuestionVO> selectQuestionList(String role, int memberId) throws SQLException {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("role", role);
        paramMap.put("memberId", memberId);
        return sqlSession.selectList("Question-Mapper.selectQuestionList", paramMap);
    }

    @Override
    public void updateQuestion(QuestionVO question) throws SQLException {
        sqlSession.update("Question-Mapper.updateQuestion", question);
        
    }

    @Override
    public void updateViewCount(int questionId) throws SQLException {
        sqlSession.update("Question-Mapper.updateViewCount", questionId);
        
    }

    @Override
    public List<CoursesVO> selectCourseByStudent(int studentId) throws SQLException {
        return sqlSession.selectList("Question-Mapper.selectCourseByStudent", studentId);
        
    }

    

}

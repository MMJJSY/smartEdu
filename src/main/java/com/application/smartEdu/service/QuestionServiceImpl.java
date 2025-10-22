package com.application.smartEdu.service;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.application.smartEdu.dao.QuestionDAO;
import com.application.smartEdu.dto.CoursesVO;
import com.application.smartEdu.dto.QuestionVO;

@Service
public class QuestionServiceImpl implements QuestionService{

    @Autowired
    private QuestionDAO questionDAO;

    @Override
    public void deleteQuestion(int questionId) throws SQLException {
        questionDAO.deleteQuestion(questionId);
        
    }

    @Override
    public QuestionVO getQuestionDetail(int questionId) throws SQLException {
        return questionDAO.selectQuestionById(questionId);
    }

    @Override
    public List<QuestionVO> getQuestionList(String role, int memberId) throws SQLException {
        return questionDAO.selectQuestionList(role, memberId);
    }

    @Override
    public void increaseViewCount(int questionId) throws SQLException {
        questionDAO.updateViewCount(questionId);
        
    }

    @Override
    public void modifyQuestion(QuestionVO question) throws SQLException {
        questionDAO.updateQuestion(question);
        
    }

    @Override
    public void registQuestion(QuestionVO question) throws SQLException {
        questionDAO.insertQuestion(question);
        
    }

    @Override
    public List<CoursesVO> getCourseBystudent(int studentId) throws SQLException {
        return questionDAO.selectCourseByStudent(studentId);
    }

    

}

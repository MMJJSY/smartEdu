package com.application.smartEdu.service;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.application.smartEdu.dao.CommentDAO;
import com.application.smartEdu.dto.CommentVO;

@Service
public class CommentSerrviceImpl implements CommentService{

    @Autowired
    private CommentDAO commentDAO;

    @Override
    public void delete(int commentId) throws SQLException {
        commentDAO.deleteComment(commentId);
        
    }

    @Override
    public List<CommentVO> getCommentListByQuestionId(int questionId) throws SQLException {
        return commentDAO.selectCommentListByQuestionId(questionId);
    }

    @Override
    public CommentVO getCommentbyId(int commentId) throws SQLException {
        return commentDAO.selectCommentById(commentId);
    }

    @Override
    public void modify(CommentVO comment) throws SQLException {
        commentDAO.updateComment(comment);
        
    }

    @Override
    public void regist(CommentVO comment) throws SQLException {
        commentDAO.insertComment(comment);
        
    }

    


}

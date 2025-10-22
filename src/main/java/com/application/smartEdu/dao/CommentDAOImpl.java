package com.application.smartEdu.dao;

import java.sql.SQLException;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.application.smartEdu.dto.CommentVO;

@Repository
public class CommentDAOImpl implements CommentDAO{


    @Autowired
    private SqlSession sqlsession;
    @Override
    public void deleteComment(int commentId) throws SQLException {
        sqlsession.update("Comment-Mapper.deleteComment", commentId);
        
    }

    @Override
    public void insertComment(CommentVO comment) throws SQLException {
       sqlsession.insert("Comment-Mapper.insertComment", comment);
    }

    @Override
    public CommentVO selectCommentById(int commentId) throws SQLException {
        return sqlsession.selectOne("Comment-Mapper.selectCommentById", commentId);
    }

    @Override
    public List<CommentVO> selectCommentListByQuestionId(int questionId) throws SQLException {
        return sqlsession.selectList("Comment-Mapper.selectCommentListByQuestionId", questionId);
    }

    @Override
    public void updateComment(CommentVO comment) throws SQLException {
        sqlsession.update("Comment-Mapper.updateComment", comment);
        
    }

    

}

package com.application.smartEdu.dao;


import java.sql.SQLException;
import java.util.List;


import com.application.smartEdu.dto.CommentVO;

public interface CommentDAO {

    // 댓글 등록
    void insertComment (CommentVO comment) throws SQLException;
    // 댓글 수정
    void updateComment (CommentVO comment) throws SQLException;
    // 댓글 삭제
    void deleteComment (int commentId) throws SQLException;
    // 댓글 목록
    List<CommentVO> selectCommentListByQuestionId(int questionId) throws SQLException;
    // 댓글 
    CommentVO selectCommentById(int commentId) throws SQLException;


    
   


}

package com.application.smartEdu.service;

import java.sql.SQLException;
import java.util.List;

import com.application.smartEdu.dto.CommentVO;

public interface CommentService {

    //댓글 등록
    void regist(CommentVO comment) throws SQLException;
    // 수정
    void modify(CommentVO comment) throws SQLException;
    // 삭제
    void delete(int CommentId) throws SQLException;
    // 목록
    List<CommentVO> getCommentListByQuestionId(int questionId) throws SQLException;
    // 조회
    CommentVO getCommentbyId(int commentId) throws SQLException;



}

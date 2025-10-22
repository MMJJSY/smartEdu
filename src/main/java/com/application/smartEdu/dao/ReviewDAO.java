package com.application.smartEdu.dao;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.jdbc.SQL;

import com.application.smartEdu.dto.ReviewVO;

public interface ReviewDAO {
       // 후기 등록
    void insertReview(ReviewVO review) throws SQLException;

    // 후기 목록 조회
     List<ReviewVO> selectReviewList(@Param("category") String category) throws SQLException;

    // 후기 조회 
    ReviewVO selectReviewById(int reviewId) throws SQLException;

    // 후기 조회(중복 방지용)
    ReviewVO selectReviewByStudentAndCourse(@Param("studentId")int studentId, @Param("courseId")int courseId) throws SQLException;

    // 후기 수정
    void updateReview(ReviewVO review) throws SQLException;

    // 후기 삭제
    void deleteReview(int reviewId) throws SQLException;

   


}

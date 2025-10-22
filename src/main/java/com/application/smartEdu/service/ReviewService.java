package com.application.smartEdu.service;

import java.sql.SQLException;
import java.util.List;

import com.application.smartEdu.dto.ReviewVO;

public interface ReviewService {

     void insertReview(ReviewVO review) throws SQLException;

    // 후기 수정
    void updateReview(ReviewVO review) throws SQLException;

    // 후기 삭제
    void deleteReview(int reviewId) throws SQLException;

    // 후기 조회
    ReviewVO getReviewById(int reviewId) throws SQLException;

    // 후기 조회(중복방지용)
    ReviewVO getReviewByStudentAndCourse(int studentId, int courseId) throws SQLException;

    // 후기 리스트
    List<ReviewVO> getReviewList(String category) throws SQLException;

}

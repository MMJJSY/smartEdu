package com.application.smartEdu.service;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.application.smartEdu.dao.ReviewDAO;
import com.application.smartEdu.dto.ReviewVO;

@Service
public class ReviewServiceImpl implements ReviewService{

    @Autowired 
    private ReviewDAO reviewDAO;

    @Override
    public void deleteReview(int reviewId) throws SQLException {
        reviewDAO.deleteReview(reviewId);
        
    }

    @Override
    public ReviewVO getReviewById(int reviewId) throws SQLException {
        return reviewDAO.selectReviewById(reviewId);
    }

    @Override
    public List<ReviewVO> getReviewList(String category) throws SQLException {
        return reviewDAO.selectReviewList(category);
    }

    @Override
    public void insertReview(ReviewVO review) throws SQLException {
        reviewDAO.insertReview(review);
        
    }

    @Override
    public void updateReview(ReviewVO review) throws SQLException {
        reviewDAO.updateReview(review);
        
    }

    @Override
    public ReviewVO getReviewByStudentAndCourse(int studentId, int courseId) throws SQLException {
        return reviewDAO.selectReviewByStudentAndCourse(studentId, courseId);
    }
    


}

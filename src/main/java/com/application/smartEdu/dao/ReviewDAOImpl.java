package com.application.smartEdu.dao;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.application.smartEdu.dto.ReviewVO;

@Repository
public class ReviewDAOImpl implements ReviewDAO{

    @Autowired
    private SqlSession session;

    @Override
    public void deleteReview(int reviewId) throws SQLException {
        session.delete("Review-Mapper.deleteReview", reviewId);
        
    }

    @Override
    public void insertReview(ReviewVO review) throws SQLException {
        session.insert("Review-Mapper.insertReview", review);
    }

    @Override
    public ReviewVO selectReviewById(int reviewId) throws SQLException {
        return session.selectOne("Review-Mapper.selectReviewById", reviewId);
    }

    @Override
    public List<ReviewVO> selectReviewList(String category) throws SQLException {
        return session.selectList("Review-Mapper.selectReviewList",category);
    }

    @Override
    public void updateReview(ReviewVO review) throws SQLException {
        session.update("Review-Mapper.selectReviewList", review);
        
    }

    @Override
    public ReviewVO selectReviewByStudentAndCourse(int studentId, int courseId) throws SQLException {
        Map<String, Object> param = new HashMap<>();
        param.put("studentId", studentId);
        param.put("courseId", courseId);

        return session.selectOne("Review-Mapper.selectReviewByStudentAndCourse", param);
    }

    
    


}

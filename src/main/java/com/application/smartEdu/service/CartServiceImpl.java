package com.application.smartEdu.service;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.application.smartEdu.dao.CartDAO;
import com.application.smartEdu.dao.CoursesDAO;
import com.application.smartEdu.dto.CartItemWithCourseVO;
import com.application.smartEdu.dto.CoursesVO;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartDAO cartDAO;

    @Autowired(required = false)
    private CoursesDAO courseDAO;

    // 장바구니 + 강좌 JOIN 목록
    @Override
    @Transactional(readOnly = true)
    public List<CartItemWithCourseVO> getCartWithCourses(int studentId) throws SQLException {
        return cartDAO.selectCartItemsWithCourseByStudentId(studentId);
    }

    // 장바구니 업서트
    @Override
    @Transactional
    public void upsertToCart(int studentId, int courseId, Long courseAmount) throws SQLException {
        if (courseDAO != null) {
            CoursesVO found = courseDAO.findCourseById(courseId);
            if (found == null) {
                throw new SQLException("존재하지 않는 강좌입니다. courseId=" + courseId);
            }
        }
        cartDAO.upsertCart(studentId, courseId, courseAmount);
    }

    // 장바구니 한건 삭제
    @Override
    @Transactional
    public void removeFromCart(int studentId, int courseId) throws SQLException {
        cartDAO.deleteCartByStudentAndCourse(studentId, courseId);
    }

    // 장바구니 총액(강좌 가격 기준)
    @Override
    @Transactional(readOnly = true)
    public Long sumCartAmountByStudent(int studentId) throws SQLException {
        Long sum = cartDAO.sumCartAmountByStudentId(studentId);
        return (sum == null) ? 0L : sum;
    }

    @Override
    @Transactional
    public boolean addToCartAndTellIfDuplicated(int studentId, int courseId, Long courseAmount) throws SQLException {
        boolean duplicated = cartDAO.existsByStudentAndCourse(studentId, courseId) > 0;
        // 기존 upsert 재사용 (값은 유지)
        cartDAO.upsertCart(studentId, courseId, (courseAmount == null || courseAmount < 0) ? 0L : courseAmount);
        return duplicated;
    }

}

package com.application.smartEdu.service;

import java.sql.SQLException;
import java.util.List;

import com.application.smartEdu.dto.CartItemWithCourseVO;

public interface CartService {
    // 목록
    List<CartItemWithCourseVO> getCartWithCourses(int studentId) throws SQLException;

    // 찜(업서트)
    void upsertToCart(int studentId, int courseId, Long courseAmount) throws SQLException;

    // 한건 삭제
    void removeFromCart(int studentId, int courseId) throws SQLException;

    // 총액(강좌 가격 기준)
    Long sumCartAmountByStudent(int studentId) throws SQLException;

    // 담고, 이미 있었다면 true(중복), 새로 담겼다면 false 반환
    boolean addToCartAndTellIfDuplicated(int studentId, int courseId, Long courseAmount) throws SQLException;

}

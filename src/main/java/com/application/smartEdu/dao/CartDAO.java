package com.application.smartEdu.dao;

import java.sql.SQLException;
import java.util.List;

import com.application.smartEdu.dto.CartItemWithCourseVO;

public interface CartDAO {

    // 장바구니 + 강좌 JOIN 목록
    List<CartItemWithCourseVO> selectCartItemsWithCourseByStudentId(int studentId) throws SQLException;

    // 업서트
    int upsertCart(int studentId, int courseId, Long courseAmount) throws SQLException;

    // 단건 삭제
    int deleteCartByStudentAndCourse(int studentId,
                                     int courseId) throws SQLException;
                                     
    // 합계(강좌 가격 기준)
    Long sumCartAmountByStudentId(int studentId) throws SQLException;

    // 학생 장바구니에 담긴 강좌 ID 목록
    // 결제 처리 시 중복 여부 확인이나 결제 대상 강좌 추출 등에 사용
    List<Integer> selectCourseIdsInCartByStudentId(int studentId) throws SQLException;

    int existsByStudentAndCourse(int studentId, int courseId) throws SQLException;


}

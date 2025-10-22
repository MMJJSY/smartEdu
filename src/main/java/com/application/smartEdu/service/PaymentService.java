package com.application.smartEdu.service;

import java.sql.SQLException;
import java.util.List;

import com.application.smartEdu.dto.PaymentItemWithCourseVO;

public interface PaymentService {

    // 결제 내역 목록 (REFUNDED 제외)
    List<PaymentItemWithCourseVO> getPaymentsWithCourse(int studentId) throws SQLException;

    // 환불
    void refund(int paymentId) throws SQLException;

    // 장바구니 전체 결제 (중복 있으면 예외로 강좌명 안내)
    void checkoutAllFromCart(int studentId) throws SQLException;
}

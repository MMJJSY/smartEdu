package com.application.smartEdu.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.application.smartEdu.dto.PaymentItemWithCourseVO;

public interface PaymentDAO {

    // 결제 + 강좌 JOIN 목록 (REFUNDED 제외)
    List<PaymentItemWithCourseVO> selectPaymentsWithCourseByStudentId(int studentId) throws SQLException;

    // 환불 (NORMAL -> REFUNDED)
    int refundPayment(int paymentId) throws SQLException;

    // 장바구니 -> 결제내역 일괄 등록 (NOT EXISTS로 중복 차단)
    int insertPaymentsFromCartByStudentId(int studentId) throws SQLException;

    // 결제 후 장바구니 비우기
    int clearCartByStudentId(int studentId) throws SQLException;

    // 사전 점검 결과(DUPLICATE 목록 또는 OK 1행) 조회
    List<Map<String, Object>> checkoutFromCartOnce(int studentId) throws SQLException;

    // ✅ 강의 시청 시 결제 상태 NORMAL -> CANCELLED 변경
    int cancelPaymentOnView(int studentId, int courseId) throws SQLException;
}
package com.application.smartEdu.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.application.smartEdu.dao.PaymentDAO;
import com.application.smartEdu.dto.PaymentItemWithCourseVO;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired private PaymentDAO paymentDAO;

    @Transactional(readOnly = true)
    public List<PaymentItemWithCourseVO> getPaymentsWithCourse(int studentId) throws SQLException {
        return paymentDAO.selectPaymentsWithCourseByStudentId(studentId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void refund(int paymentId) throws SQLException {
        int updated = paymentDAO.refundPayment(paymentId);
        if (updated <= 0) {
            throw new SQLException("환불할 수 없는 결제입니다. paymentId=" + paymentId);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void checkoutAllFromCart(int studentId) throws SQLException {
        // 5) 중복 검사 (DUPLICATE / OK)
        List<Map<String, Object>> rows = paymentDAO.checkoutFromCartOnce(studentId);
        if (rows == null || rows.isEmpty()) throw new SQLException("결제 처리 결과가 비어 있습니다.");

        String status = String.valueOf(rows.get(0).get("status"));
        if ("DUPLICATE".equalsIgnoreCase(status)) {
            List<String> titles = rows.stream()
                    .map(r -> r.get("payload"))
                    .filter(Objects::nonNull)
                    .map(Object::toString)
                    .distinct()
                    .toList();
            throw new SQLException("이미 결제한 강좌가 포함되어 있습니다: " + String.join(", ", titles));
        }
        if (!"OK".equalsIgnoreCase(status)) {
            throw new SQLException("알 수 없는 결제 처리 상태입니다: " + status);
        }

        // ✅ OK일 때만 3 → 4 실행
        paymentDAO.insertPaymentsFromCartByStudentId(studentId); // 3)
        paymentDAO.clearCartByStudentId(studentId);              // 4)
    }
}

package com.application.smartEdu.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.application.smartEdu.dto.PaymentItemWithCourseVO;

@Repository
public class PaymentDAOImpl implements PaymentDAO {

    @Autowired
    private SqlSession sqlSession;

    @Override
    public List<PaymentItemWithCourseVO> selectPaymentsWithCourseByStudentId(int studentId) throws SQLException {
        return sqlSession.selectList("Payment-Mapper.selectPaymentsWithCourseByStudentId", studentId);
    }

    @Override
    public int refundPayment(int paymentId) throws SQLException {
        return sqlSession.update("Payment-Mapper.refundPayment", paymentId);
    }

    @Override
    public int insertPaymentsFromCartByStudentId(int studentId) throws SQLException {
        return sqlSession.insert("Payment-Mapper.insertPaymentsFromCartByStudentId", studentId);
    }

    @Override
    public int clearCartByStudentId(int studentId) throws SQLException {
        return sqlSession.delete("Payment-Mapper.clearCartByStudentId", studentId);
    }

    @Override
    public List<Map<String, Object>> checkoutFromCartOnce(int studentId) throws SQLException {
        return sqlSession.selectList("Payment-Mapper.checkoutFromCartOnce", studentId);
    }
}

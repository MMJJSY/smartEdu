package com.application.smartEdu.dao;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.application.smartEdu.dto.CartItemWithCourseVO;
// test
@Repository
public class CartDAOimpl implements CartDAO {

    @Autowired
    private SqlSession sqlSession;

    @Override
    public List<CartItemWithCourseVO> selectCartItemsWithCourseByStudentId(int studentId) throws SQLException {
        return sqlSession.selectList("Cart-Mapper.selectCartItemsWithCourseByStudentId", studentId);
    }

    @Override
public int upsertCart(int studentId, int courseId, Long courseAmount) throws SQLException {
    Map<String, Object> p = new HashMap<>();
    p.put("studentId", studentId);
    p.put("courseId", courseId);
    p.put("courseAmount", courseAmount);
    return sqlSession.insert("Cart-Mapper.upsertCart", p);
}


    @Override
    public int deleteCartByStudentAndCourse(int studentId, int courseId) throws SQLException {
        Map<String, Object> p = new HashMap<>();
        p.put("param1", studentId);
        p.put("param2", courseId);
        return sqlSession.delete("Cart-Mapper.deleteCartByStudentAndCourse", p);
    }

    @Override
    public Long sumCartAmountByStudentId(int studentId) throws SQLException{
        return sqlSession.selectOne("Cart-Mapper.sumCartAmountByStudentId", studentId);
    }

    @Override
    public List<Integer> selectCourseIdsInCartByStudentId(int studentId) throws SQLException {
        return sqlSession.selectList("Cart-Mapper.selectCourseIdsInCartByStudentId", studentId);
    }

        @Override
    public int existsByStudentAndCourse(int studentId, int courseId) throws SQLException {
        Map<String, Object> p = new HashMap<>();
        p.put("param1", studentId);
        p.put("param2", courseId);
        return sqlSession.selectOne("Cart-Mapper.existsByStudentAndCourse", p);
    }


}


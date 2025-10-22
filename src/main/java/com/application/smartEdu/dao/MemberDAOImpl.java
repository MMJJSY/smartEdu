package com.application.smartEdu.dao;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.application.smartEdu.command.PageMaker;
import com.application.smartEdu.dto.InstructorVO;
import com.application.smartEdu.dto.MemberVO;

@Repository
public class MemberDAOImpl implements MemberDAO {

    @Autowired
    private SqlSession sqlSession;

    @Override
    public void insertMember(MemberVO member) throws SQLException {
        sqlSession.insert("Member-Mapper.insertMember", member);

    }

    @Override
    public void updateMember(MemberVO member) throws SQLException {
        sqlSession.update("Member-Mapper.updateMember", member);

    }

    @Override
    public void withdrawMember(int member_id) throws SQLException {
        sqlSession.update("Member-Mapper.withdrawMember", member_id);

    }

    @Override
    public MemberVO selectMemberByEmail(String email) throws SQLException {
        return sqlSession.selectOne("Member-Mapper.selectMemberByEmail", email);
    }

    @Override
    public void insertInstructor(InstructorVO instructor) throws SQLException {
        sqlSession.insert("Member-Mapper.insertInstructor", instructor);
        
    }

    @Override
    public InstructorVO selectInstructorById(int member_id) throws SQLException {
        return sqlSession.selectOne("Member-Mapper.selectInstructorById", member_id);
    }

    @Override
    public int countPendingInstructors(PageMaker pm) throws SQLException {
        int ttc = sqlSession.selectOne("Member-Mapper.countPendingInstructors",pm);
        return ttc;
    }

    @Override
    public List<InstructorVO> findPendingInstructors(PageMaker pm) throws SQLException {

        return sqlSession.selectList("Member-Mapper.findPendingInstructors",pm);
    }

    @Override
    public void updatePendingStatus(InstructorVO instructor) throws SQLException {
        sqlSession.update("Member-Mapper.updatePendingStatus",instructor);
        
    }

    @Override
    public List<InstructorVO> findPendingInstructors(int instructorId, String filter) throws SQLException {
        Map<String, Object> params = new HashMap<>();
        params.put("instructorId", instructorId);
        params.put("status", filter);
        return sqlSession.selectList("Member-Mapper.findPendingInstructors",params);
    }

    

    


    
    
}

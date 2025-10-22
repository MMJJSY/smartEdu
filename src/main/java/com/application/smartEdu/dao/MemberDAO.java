package com.application.smartEdu.dao;

import java.sql.SQLException;
import java.util.List;

import com.application.smartEdu.command.PageMaker;
import com.application.smartEdu.dto.InstructorVO;
import com.application.smartEdu.dto.MemberVO;

public interface MemberDAO {

    // 회원 등록
    void insertMember(MemberVO member) throws SQLException;

    // 비밀번호 변경
    void updateMember(MemberVO member) throws SQLException;

    // 회원 탈퇴
    void withdrawMember(int memberId) throws SQLException;

    // 회원번호로 회원 조회
    MemberVO selectMemberByEmail(String email) throws SQLException;

    // 강사 등록
    void insertInstructor(InstructorVO instructor) throws SQLException;

    InstructorVO selectInstructorById(int member_id) throws SQLException;

    List<InstructorVO> findPendingInstructors(PageMaker pm) throws SQLException;

    void updatePendingStatus(InstructorVO instructor) throws SQLException;

    int countPendingInstructors(PageMaker pm) throws SQLException;

    List<InstructorVO> findPendingInstructors(int instructorId, String filter) throws SQLException;

}

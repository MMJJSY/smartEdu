package com.application.smartEdu.service;

import java.sql.SQLException;
import java.util.List;

import com.application.smartEdu.command.PageMaker;
import com.application.smartEdu.dto.InstructorVO;
import com.application.smartEdu.dto.MemberVO;
import com.application.smartEdu.exception.InstructorPendingException;
import com.application.smartEdu.exception.InstructorRejectedException;
import com.application.smartEdu.exception.InvalidPasswordException;
import com.application.smartEdu.exception.NotFoundEmailException;

public interface MemberService {

    // 회원조회
    MemberVO getMember(String email) throws SQLException;

    // 강사조회
    InstructorVO getInstructor(int memberId) throws SQLException;

    // 회원등록
    void regist(MemberVO member) throws SQLException;

    // 강사등록
    void registInstructor(InstructorVO instructor) throws SQLException;

    // 회원수정
    void modify(MemberVO member) throws SQLException;

    // 비밀번호 변경
    void changePassword(String email, String newPwd) throws SQLException, NotFoundEmailException;

    // 회원탈퇴
    void withdraw(int memberId) throws SQLException;

    // 로그인
    MemberVO login(String email, String pwd) throws SQLException, NotFoundEmailException, InvalidPasswordException,
            InstructorPendingException, InstructorRejectedException;

    // ✅ 승인 대기중 강사 목록 (검색 + 페이징)
    List<InstructorVO> findPendingInstructors(PageMaker pm) throws SQLException;

    // ✅ 승인 대기중 강사 총 수
    int countPendingInstructors(PageMaker pm) throws SQLException;

    // ✅ 승인 상태 업데이트 (APPROVED / REJECTED)
    void updatePendingStatus(InstructorVO instructor) throws SQLException;

    List<InstructorVO> findPendingInstructors(int instructorId, String filter) throws SQLException;

}

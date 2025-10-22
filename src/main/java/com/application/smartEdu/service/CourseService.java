package com.application.smartEdu.service;

import java.sql.SQLException;
import java.util.List;

import com.application.smartEdu.command.CourseWithLectureDetailVO;
import com.application.smartEdu.command.PageMaker;
import com.application.smartEdu.dto.CoursesVO;


public interface CourseService {
    // 목록
    List<CoursesVO> list(PageMaker pm) throws SQLException;
    // 강좌 상세 (강좌)
    public CoursesVO detail(int courseId) throws SQLException;
    // 강좌 상세 (강좌 + 강의목록)
    public CourseWithLectureDetailVO getAlldetail(Integer courseId) throws SQLException;
    // 강좌 수정
    void modify(CoursesVO course) throws SQLException;
    CoursesVO getCourse(int courseId) throws SQLException;
    // 삭제
    void remove(int courseId) throws SQLException;
    // 생성
    void insertCourse(CoursesVO course) throws SQLException;

    List<CoursesVO> getPopularCourses() throws SQLException;
    List<CoursesVO> getLatestCourses() throws SQLException;
    List<CoursesVO> getRecommendedCourses() throws SQLException;

    List<CoursesVO> findByInstructorAndStatus(int instructorId, String filter) throws SQLException;

    int countCoursesList(PageMaker pm) throws SQLException;
    
    

}

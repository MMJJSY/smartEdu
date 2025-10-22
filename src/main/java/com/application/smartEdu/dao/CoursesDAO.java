package com.application.smartEdu.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.application.smartEdu.command.PageMaker;
import com.application.smartEdu.dto.CoursesVO;

public interface CoursesDAO {
    List<CoursesVO> selectCoursesList(PageMaker pm) ; // 검색(카테고리, 강사명, 제목 , 내용 포함임)

    int countCoursesList(PageMaker pm) throws SQLException;

    CoursesVO findCourseById(Integer  courseId) throws SQLException; // detail부분용

    void insertCourse(CoursesVO course) throws SQLException; //생성

    void updateCourse(CoursesVO course) throws SQLException; //수정

    void deleteCourse(int courseId) throws SQLException; //삭제 - 상태값 변경임(status = CLOSE)

    void incrementViewCount(int courseId) throws SQLException; //조회수 증가

    List<CoursesVO> selectPopularCourses() throws SQLException;
    List<CoursesVO> selectLatestCourses() throws SQLException;
    List<CoursesVO> selectRecommendedCourses() throws SQLException;

    List<CoursesVO> findByInstructorAndStatus(int instructorId, String filter) throws SQLException;

}

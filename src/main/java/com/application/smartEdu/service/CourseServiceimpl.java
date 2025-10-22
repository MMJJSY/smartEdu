package com.application.smartEdu.service;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.application.smartEdu.command.CourseWithLectureDetailVO;
import com.application.smartEdu.command.PageMaker;
import com.application.smartEdu.dao.CoursesDAO;
import com.application.smartEdu.dao.LectureDAO;
import com.application.smartEdu.dto.CoursesVO;
import com.application.smartEdu.dto.LecturesVO;

@Service
public class CourseServiceimpl implements CourseService {
    @Autowired
    private CoursesDAO courseDAO;
    @Autowired
    private LectureDAO lectureDAO;

    @Override
    public CourseWithLectureDetailVO getAlldetail(Integer courseId) throws SQLException {
        CoursesVO course = courseDAO.findCourseById(courseId);
        List<LecturesVO> lectures = lectureDAO.findLecturesByCourseId(courseId);
        return new CourseWithLectureDetailVO(course, lectures);
    }

    @Override
    public CoursesVO getCourse(int courseId) throws SQLException {
        CoursesVO course = courseDAO.findCourseById(courseId);
        courseDAO.incrementViewCount(courseId); // 조회수 증가
        return course;
    }

    @Override
    public List<CoursesVO> list(PageMaker pm) throws SQLException {
        List<CoursesVO> courseList = courseDAO.selectCoursesList(pm);
        int totalCount = courseDAO.countCoursesList(pm);
        pm.setTotalCount(totalCount);
        return courseList;
    }

    @Override
    public void modify(CoursesVO course) throws SQLException {
        courseDAO.updateCourse(course);

    }

    @Override
    public void remove(int courseId) throws SQLException {
        courseDAO.deleteCourse(courseId);

    }

    @Override
    public void insertCourse(CoursesVO course) throws SQLException {
        courseDAO.insertCourse(course);

    }

    @Override
    public CoursesVO detail(int courseId) throws SQLException {
        CoursesVO course = courseDAO.findCourseById(courseId);
        courseDAO.incrementViewCount(courseId); // 조회수 증가
        return course;
    }

    public List<CoursesVO> getPopularCourses() throws SQLException {
        return courseDAO.selectPopularCourses();
    }

    public List<CoursesVO> getLatestCourses() throws SQLException {
        return courseDAO.selectLatestCourses();
    }

    public List<CoursesVO> getRecommendedCourses() throws SQLException {
        return courseDAO.selectRecommendedCourses();
    }



    @Override
    public List<CoursesVO> findByInstructorAndStatus(int instructorId, String filter) throws SQLException {
        return courseDAO.findByInstructorAndStatus(instructorId,filter);
    }

    @Override
    public int countCoursesList(PageMaker pm) throws SQLException {
        return courseDAO.countCoursesList(pm);
        
    }
    
    

}

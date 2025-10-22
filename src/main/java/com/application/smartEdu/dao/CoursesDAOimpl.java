package com.application.smartEdu.dao;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.application.smartEdu.command.PageMaker;
import com.application.smartEdu.dto.CoursesVO;

@Repository
public class CoursesDAOimpl implements CoursesDAO {
    @Autowired
    private SqlSession sqlSession;

    @Override
    public int countCoursesList(PageMaker pm) throws SQLException {
        int count = sqlSession.selectOne("Course-Mapper.countCoursesList", pm);
        return count;
    }

    @Override
    public void deleteCourse(int courseId) throws SQLException {
        sqlSession.update("Course-Mapper.deleteCourse", courseId);

    }

    @Override
    public CoursesVO findCourseById(Integer courseId) throws SQLException {
        CoursesVO course = sqlSession.selectOne("Course-Mapper.findCourseById", courseId);
        return course;
    }

    @Override
    public void incrementViewCount(int courseId) throws SQLException {
        sqlSession.update("Course-Mapper.incrementViewCount", courseId);

    }

    @Override
    public void insertCourse(CoursesVO course) throws SQLException {
        sqlSession.insert("Course-Mapper.insertCourse", course);

    }

    @Override
    public List<CoursesVO> selectCoursesList(PageMaker pm) {

        return sqlSession.selectList(
                "Course-Mapper.selectCoursesList", pm);
    }

    @Override
    public void updateCourse(CoursesVO course) throws SQLException {
        sqlSession.update("Course-Mapper.updateCourse", course);

    }

    @Override
    public List<CoursesVO> selectPopularCourses() throws SQLException {
        return sqlSession.selectList("Course-Mapper.selectPopularCourses");
    }

    @Override
    public List<CoursesVO> selectLatestCourses() throws SQLException {
        return sqlSession.selectList("Course-Mapper.selectLatestCourses");
    }

    @Override
    public List<CoursesVO> selectRecommendedCourses() throws SQLException {
        return sqlSession.selectList("Course-Mapper.selectRecommendedCourses");
    }

    @Override
    public List<CoursesVO> findByInstructorAndStatus(int instructorId, String filter) throws SQLException {
        Map<String, Object> params = new HashMap<>();
        params.put("instructorId", instructorId);
        params.put("status", filter);
        return sqlSession.selectList("Course-Mapper.findByInstructorAndStatus", params);
    }

}

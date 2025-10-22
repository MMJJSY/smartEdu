package com.application.smartEdu.dao;

import java.sql.SQLException;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.application.smartEdu.dto.LecturesVO;

@Repository
public class LectureDAOimpl implements LectureDAO {
    @Autowired
    private SqlSession sqlSession;

    @Override
    public List<LecturesVO> findLecturesByCourseId(int courseId) throws SQLException {
        return sqlSession.selectList("Lectures-Mapper.findLecturesByCourseId", courseId);
    }

    @Override
    public void addLecture(LecturesVO lecture) throws SQLException {
        sqlSession.insert("Lectures-Mapper.addLecture", lecture);
        
    }

    @Override
    public void deleteLecture(int lectureId) throws SQLException {
        sqlSession.delete("Lectures-Mapper.deleteLecture", lectureId);
        
    }

    @Override
    public LecturesVO getLecture(int lectureId) throws SQLException {
        return sqlSession.selectOne("Lectures-Mapper.getLecture", lectureId);
        
    }

    @Override
    public void updateLecture(LecturesVO lecture) throws SQLException {
        sqlSession.update("Lectures-Mapper.updateLecture", lecture);
        
    }
    
}

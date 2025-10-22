package com.application.smartEdu.service;

import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.application.smartEdu.dao.LectureDAO;
import com.application.smartEdu.dto.LecturesVO;



@Service
public class LectureServiceimpl implements LectureService {
    @Autowired
    private LectureDAO lectureDAO;

    @Override
    public LecturesVO getLecture(int lectureId) throws SQLException {
        return lectureDAO.getLecture(lectureId);
    }

    @Override
    public void deleteLecture(int lectureId) throws SQLException {
        lectureDAO.deleteLecture(lectureId);
        
    }

    @Override
    public void updateLecture(LecturesVO lecture) throws SQLException {
        lectureDAO.updateLecture(lecture);
        
    }

     @Override
    public void addLecture(LecturesVO lecture) throws SQLException {
        lectureDAO.addLecture(lecture);
        
    }


}

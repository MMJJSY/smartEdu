package com.application.smartEdu.service;

import java.sql.SQLException;

import com.application.smartEdu.dto.LecturesVO;

public interface LectureService {
    LecturesVO getLecture(int lectureId) throws Exception;

    void addLecture(LecturesVO lecture) throws SQLException;

    void updateLecture(LecturesVO lecture) throws SQLException;
    
    void deleteLecture(int lectureId) throws SQLException;

}

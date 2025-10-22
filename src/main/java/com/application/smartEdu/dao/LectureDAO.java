package com.application.smartEdu.dao;

import java.sql.SQLException;
import java.util.List;

import com.application.smartEdu.dto.LecturesVO;

public interface LectureDAO {
    // 강좌에서 강의목록 조회
    List<LecturesVO> findLecturesByCourseId(int courseId) throws SQLException;
    // 강의 상세 조회
    LecturesVO getLecture(int lectureId) throws SQLException;
    // 강의 생성
    void addLecture(LecturesVO lecture) throws SQLException;
    // 강의 수정
    void updateLecture(LecturesVO lecture) throws SQLException;
    // 강의 삭제
    void deleteLecture(int lectureId) throws SQLException;
}

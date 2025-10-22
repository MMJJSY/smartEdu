package com.application.smartEdu.command;

import com.application.smartEdu.dto.LecturesVO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LectuerModifyCommand extends LectureRegistCommand {

    // 🔴 수정 구분용
    private int lectureId;   // 폼 name: lecturesModify[i].lectureId
    private int sectionNo;   // 폼 name: lecturesModify[i].sectionNo

    private String oldVideo;

    @Override
    public LecturesVO toLectureVO() {
        LecturesVO lecture = super.toLectureVO();

        // ★★ 반드시 넣기
        lecture.setLectureId(this.lectureId);
        lecture.setSectionNo(this.sectionNo);

        // 파일 교체 없으면 기존 유지
        lecture.setVideo(this.oldVideo);
        return lecture;
    }
}

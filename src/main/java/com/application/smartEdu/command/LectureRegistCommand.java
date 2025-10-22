package com.application.smartEdu.command;

import org.springframework.web.multipart.MultipartFile;

import com.application.smartEdu.dto.LecturesVO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LectureRegistCommand {
    private int courseId;
    private String title;

    // 업로드된 동영상
    private MultipartFile video;

    private String description;

    // FFmpeg로 추출한 길이 (초 단위)
    private int duration;

    // 섹션 번호 등 순서
    private int sectionNo;

    public LecturesVO toLectureVO(){
        LecturesVO lecture = LecturesVO.builder()
            .courseId(this.courseId)
            .title(this.title)
            .description(this.description)
            .sectionNo(this.sectionNo)
            .build();
        return lecture;
    }

}

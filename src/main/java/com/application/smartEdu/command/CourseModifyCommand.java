package com.application.smartEdu.command;

import java.util.ArrayList;
import java.util.List;

import com.application.smartEdu.dto.CoursesVO;
import com.application.smartEdu.enums.CourseModifyStatus;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CourseModifyCommand extends CoursesRegistCommand {

    // 🔴 수정폼에서 hidden으로 넘어오는 id
    private Integer courseId;

    private String oldPicture;     
    private String modifyComment;  // DB 저장하려면 VO에도 매핑해야 함
    private List<LectuerModifyCommand> lecturesModify = new ArrayList<>();

    @Override
    public CoursesVO toCourseVO() {
        CoursesVO course = super.toCourseVO();

        // ★★ 반드시 넣기
        course.setCourseId(this.courseId);

        // 옵션들
        course.setImg(oldPicture);
        course.setModifyStatus(CourseModifyStatus.PENDING);

        // 수정 사유를 DB에 저장하고 싶다면 VO에도 필드가 있어야 함
        course.setModifyComment(this.modifyComment);

        return course;
    }
}




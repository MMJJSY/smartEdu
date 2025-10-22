package com.application.smartEdu.command;

import java.util.List;

import com.application.smartEdu.dto.CoursesVO;
import com.application.smartEdu.dto.LecturesVO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CourseWithLectureDetailVO {
    private CoursesVO course;
    private List<LecturesVO> lectures;
}

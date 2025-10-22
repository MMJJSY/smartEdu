package com.application.smartEdu.command;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.application.smartEdu.dto.CoursesVO;
import com.application.smartEdu.enums.CourseCategory;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CoursesRegistCommand {

    private String title;
    private Double price;
    private CourseCategory category;
    private int instructorId;
    private String instructorName; // 강사 이름
    private String description;
    private String curriculum;

    // 썸네일 이미지 업로드용
    private MultipartFile img;

    // 함께 생성할 강의 목록
    private List<LectureRegistCommand> lectures = new ArrayList<>();

    public CoursesVO toCourseVO(){
        CoursesVO course = new CoursesVO();
        course.setTitle(this.title);
        course.setPrice(this.price);
        course.setCategory(this.category);
        course.setInstructorName(instructorName);
        course.setDescription(this.description);
        course.setCurriculum(this.curriculum);
        course.setInstructorId(this.instructorId);
        return course;
    }

}
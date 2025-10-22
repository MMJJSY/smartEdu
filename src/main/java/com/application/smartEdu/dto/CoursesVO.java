package com.application.smartEdu.dto;

import java.sql.Timestamp;

import com.application.smartEdu.enums.CourseApprovedStatus;
import com.application.smartEdu.enums.CourseCategory;
import com.application.smartEdu.enums.CourseModifyStatus;
import com.application.smartEdu.enums.CoursePendingStatus;

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
public class CoursesVO {
    private int courseId;
    private String title;
    private Double price;
    private int instructorId;
    private CourseCategory category;
    private String img;
    private String description;
    private String curriculum;
    private int viewCount;
    private CourseApprovedStatus approvedStatus;
    private CoursePendingStatus status;
    private CourseModifyStatus modifyStatus;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    private String modifyComment;

    private String instructorName; // 조인해서 가져올 강사 이름
    


}

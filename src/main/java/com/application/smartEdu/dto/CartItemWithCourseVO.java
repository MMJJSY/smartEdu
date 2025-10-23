package com.application.smartEdu.dto;

import java.sql.Timestamp;

import com.application.smartEdu.enums.CourseCategory;
import com.application.smartEdu.enums.CoursePendingStatus; // Courses.status: PENDING/OPEN/CLOSE

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
public class CartItemWithCourseVO {

    // Carts
    private int cartId;
    private int studentId;
    private int courseId;
    private Long courseAmount;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    // Courses
    private String courseTitle;
    private CourseCategory courseCategory;
    private String courseImg;
    private String courseDescription;
    private int courseViewCount;
    private Double coursePrice; 
    private CoursePendingStatus courseStatus;

    // 강사명
    private String instructorName;
}

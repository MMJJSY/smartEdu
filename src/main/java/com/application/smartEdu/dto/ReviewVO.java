package com.application.smartEdu.dto;

import java.time.LocalDateTime;

import com.application.smartEdu.enums.CourseCategory;

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
public class ReviewVO {

    private int reviewId; // 후기번호
    private int courseId; // 강좌번호
    private int studentId; // 학생번호
    private int rating; // 평점(1~5)
    private String content; // 내용
    private int viewCount; // 조회수
    private LocalDateTime createdAt; // 등록일
    private LocalDateTime updatedAt; // 수정일

    private String courseTitle; // 강좌명
    private String studentName; // 학생명
    private CourseCategory category; // 카테고리

}

package com.application.smartEdu.dto;

import java.time.LocalDateTime;

import com.application.smartEdu.enums.QuestionStatus;

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
public class QuestionVO {

    private int questionId; 
    private int courseId;
    private int memberId;
    private String title;
    private String content;
    private int viewCount;
    private QuestionStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private String courseName;
    private String memberName;
    private int commentCount;
}

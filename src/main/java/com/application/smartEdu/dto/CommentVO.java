package com.application.smartEdu.dto;

import java.time.LocalDateTime;

import com.application.smartEdu.enums.CommentStatus;

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
public class CommentVO {

    private int commentId;
    private int questionId;
    private int memberId;
    private String content;
    private Integer parentId;
    private CommentStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    private String memberName;





}

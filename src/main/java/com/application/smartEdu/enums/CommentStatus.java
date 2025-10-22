package com.application.smartEdu.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CommentStatus {

    POSTED("게시"),
    EDITED("수정"),
    DELETED("삭제");

    private final String CommentStatusName;

}

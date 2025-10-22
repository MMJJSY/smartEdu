package com.application.smartEdu.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum QuestionStatus {

    ACTIVE("활성"),
    DELETED("삭제됨");

    private final String qnaStatusName;

}

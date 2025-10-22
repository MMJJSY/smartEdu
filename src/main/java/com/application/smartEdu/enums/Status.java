package com.application.smartEdu.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Status {
    ACTIVE("활성"),
    DORMANT("휴면"),
    WITHDRAW("탈퇴"),
    BLOCKED("차단");

    private final String statusName;


}

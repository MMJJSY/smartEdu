package com.application.smartEdu.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PendingStatus {
    PENDING("대기"),
    APPROVED("승인됨"),
    REJECTED("거절됨");

    private final String pendingStatusName;


}

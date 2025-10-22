package com.application.smartEdu.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
    STUDENT("수강생"),
    INSTRUCTOR("강사"),
    ADMIN("원장");

    private final String roleName;

}

package com.application.smartEdu.exception;

public class InstructorPendingException extends RuntimeException {
    public InstructorPendingException() {
        super("승인 요청 중입니다.");
    }

}

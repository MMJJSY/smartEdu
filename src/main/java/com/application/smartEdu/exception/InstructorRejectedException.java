package com.application.smartEdu.exception;

public class InstructorRejectedException extends RuntimeException {
    public InstructorRejectedException() {
        super("승인이 거절되었습니다.");
    }

}

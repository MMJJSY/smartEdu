package com.application.smartEdu.exception;

public class NotFoundEmailException extends Exception{
    public NotFoundEmailException(){
        super("이메일이 존재하지 않습니다.");
    }

}

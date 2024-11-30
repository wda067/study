package com.study.exception;

public class EmailAlreadyExists extends CustomException {

    private static final String MESSAGE = "이미 가입된 이메일입니다.";

    public EmailAlreadyExists() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 409;
    }
}

package com.study.exception;

public class LoginFailed extends CustomException{

    private static final String MESSAGE = "이메일 또는 비밀번호가 잘못 되었습니다.";

    public LoginFailed() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 401;
    }
}

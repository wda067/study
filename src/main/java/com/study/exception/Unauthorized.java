package com.study.exception;

public class Unauthorized extends CustomException {

    private static final String MESSAGE = "로그인 해주세요.";

    public Unauthorized() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 401;
    }
}
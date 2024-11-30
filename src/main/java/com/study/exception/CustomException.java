package com.study.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;

@Getter
public abstract class CustomException extends RuntimeException {

    private final ObjectNode validation = new ObjectMapper().createObjectNode();

    public CustomException(String message) {
        super(message);
    }

    public abstract int getStatusCode();
}

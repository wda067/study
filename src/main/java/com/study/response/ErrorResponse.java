package com.study.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Builder;
import lombok.Getter;

/**
 * {
 *     "code": "400",
 *     "message": "잘못된 요청입니다.",
 *     "validation": {
 *         "email": "이메일을 입력해 주세요."
 *     }
 * }
 */
@Getter
@JsonInclude(value = Include.NON_EMPTY)  //빈 값은 제외
public class ErrorResponse {

    private final String code;
    private final String message;
    private final ObjectNode validation;

    @Builder
    public ErrorResponse(String code, String message, ObjectNode validation) {
        this.code = code;
        this.message = message;
        this.validation = initializeValidation(validation);
    }

    private ObjectNode initializeValidation(ObjectNode validation) {
        if (validation == null) {
            return new ObjectMapper().createObjectNode();
        }
        return validation;
    }

    public void addValidation(String field, String message) {
        validation.put(field, message);
    }
}

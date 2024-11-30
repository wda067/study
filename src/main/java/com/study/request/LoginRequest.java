package com.study.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginRequest {

    @Email(message = "이메일 형식으로 입력해 주세요.")
    @NotBlank(message = "이메일을 입력해 주세요.")
    private String email;

    @NotBlank(message = "비밀번호를 입력해 주세요.")
    private String password;
}

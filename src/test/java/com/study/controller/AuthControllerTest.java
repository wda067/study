package com.study.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.study.config.TestSecurityConfig;
import com.study.domain.Member;
import com.study.repository.MemberRepository;
import com.study.request.LoginRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@Import(TestSecurityConfig.class)
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    void setUp() {
        memberRepository.deleteAll();
        String encryptedPassword = passwordEncoder.encode("password");
        Member member = Member.builder()
                .email("test@test.com")
                .name("test")
                .password(encryptedPassword)
                .build();
        memberRepository.save(member);
    }

    @Test
    void 정상적인_요청을_하면_로그인이_성공한다() throws Exception {
        //given
        LoginRequest request = LoginRequest.builder()
                .email("test@test.com")
                .password("password")
                .build();
        String json = objectMapper.writeValueAsString(request);

        //expected
        mockMvc.perform(post("/api/login")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void 유효하지_않은_이메일과_비밀번호를_입력하면_로그인이_실패한다() throws Exception {
        //given
        LoginRequest request = LoginRequest.builder()
                .email("test@test.com")
                .password("invalid")
                .build();
        String json = objectMapper.writeValueAsString(request);

        //expected
        mockMvc.perform(post("/api/login")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message")
                        .value("이메일 또는 비밀번호가 잘못 되었습니다."))
                .andDo(print());
    }

    @Test
    void 로그인한_상태에서만_로그아웃을_할_수_있다() throws Exception {
        //given
        LoginRequest request = LoginRequest.builder()
                .email("test@test.com")
                .password("password")
                .build();
        String json = objectMapper.writeValueAsString(request);

        MockHttpSession session = new MockHttpSession();
        mockMvc.perform(post("/api/login")
                .contentType(APPLICATION_JSON)
                .content(json)
                .session(session));

        //expected
        mockMvc.perform(post("/api/logout")
                        .session(session))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void 비로그인_상태에서_로그아웃을_할_수_없다() throws Exception {
        //expected
        mockMvc.perform(post("/api/logout"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message")
                        .value("로그인 해주세요."))
                .andDo(print());
    }
}
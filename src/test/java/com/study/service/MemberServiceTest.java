package com.study.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.study.config.TestSecurityConfig;
import com.study.domain.Member;
import com.study.exception.EmailAlreadyExists;
import com.study.repository.MemberRepository;
import com.study.request.JoinRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
@Import(TestSecurityConfig.class)
class MemberServiceTest {

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        memberRepository.deleteAll();
    }

    @Test
    void 회원가입에_성공한다() {
        //given
        JoinRequest request = JoinRequest.builder()
                .email("test@test.com")
                .name("test")
                .password("password")
                .build();

        //when
        memberService.join(request);

        //then
        Member findMember = memberRepository.findAll().get(0);
        assertEquals(1, memberRepository.count());
        assertEquals("test@test.com", findMember.getEmail());
        assertEquals("test", findMember.getName());
    }

    @Test
    void 중복된_이메일으로_회원가입할_수_없다() {
        //given
        Member member = Member.builder()
                .email("test@test.com")
                .build();
        memberRepository.save(member);

        JoinRequest request = JoinRequest.builder()
                .email("test@test.com")
                .name("test")
                .password("password")
                .build();

        //expected
        assertThrows(EmailAlreadyExists.class, () -> memberService.join(request));
    }

    @Test
    void 회원탈퇴에_성공한다() {
        //given
        Member member = Member.builder()
                .email("test@test.com")
                .build();
        memberRepository.save(member);

        //when
        memberService.leave(member.getId());

        //then
        assertEquals(0, memberRepository.count());
        assertTrue(memberRepository.findByEmail("test@test.com").isEmpty());
    }
}
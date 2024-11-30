package com.study.service;

import com.study.domain.Member;
import com.study.exception.LoginFailed;
import com.study.repository.MemberRepository;
import com.study.request.LoginRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public Member login(LoginRequest request) {
        return memberRepository.findByEmail(request.getEmail())
                .filter(member -> passwordEncoder.matches(request.getPassword(), member.getPassword()))
                .orElseThrow(LoginFailed::new);
    }
}

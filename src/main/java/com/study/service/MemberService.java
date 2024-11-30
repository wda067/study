package com.study.service;

import com.study.domain.Member;
import com.study.exception.EmailAlreadyExists;
import com.study.exception.MemberNotFound;
import com.study.repository.MemberRepository;
import com.study.request.JoinRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public void join(JoinRequest request) {
        if (memberRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new EmailAlreadyExists();
        }

        String encryptedPassword = passwordEncoder.encode(request.getPassword());
        Member member = Member.builder()
                .email(request.getEmail())
                .name(request.getName())
                .password(encryptedPassword)
                .build();

        memberRepository.save(member);
    }

    public void leave(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(MemberNotFound::new);

        memberRepository.delete(member);
    }
}

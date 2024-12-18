package com.study.service;

import com.study.domain.Member;
import com.study.exception.EmailAlreadyExists;
import com.study.exception.MemberNotFound;
import com.study.repository.MemberRepository;
import com.study.request.JoinRequest;
import com.study.response.MemberInfo;
import java.util.List;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Transactional
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

    @Transactional
    public void leave(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(MemberNotFound::new);

        memberRepository.delete(member);
    }

    @Cacheable(value = "member", key = "#memberId", cacheManager = "memberCacheManager")
    public MemberInfo getMember(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(MemberNotFound::new);

        return new MemberInfo(member);
    }

    @Cacheable(value = "members", key = "'all'", cacheManager = "memberCacheManager")
    public List<MemberInfo> getMembers() {
        return memberRepository.findAll().stream()
                .map(MemberInfo::new)
                .toList();
    }
}

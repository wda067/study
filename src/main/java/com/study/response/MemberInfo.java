package com.study.response;

import com.study.domain.Member;
import lombok.Getter;

@Getter
public class MemberInfo {

    private final String email;
    private final String name;

    public MemberInfo(Member member) {
        this.email = member.getEmail();
        this.name = member.getName();
    }
}

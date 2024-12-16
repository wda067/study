package com.study.controller;

import com.study.request.JoinRequest;
import com.study.response.MemberInfo;
import com.study.service.MemberService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private static final Logger log = LoggerFactory.getLogger(MemberController.class);
    private final MemberService memberService;

    @PostMapping("/api/join")
    public void join(@RequestBody @Validated JoinRequest request) {
        memberService.join(request);
    }

    @PostMapping("/api/member/{memberId}/leave")
    public void leave(@PathVariable("memberId") Long memberId) {
        memberService.leave(memberId);
    }

    @GetMapping("/api/members/{memberId}")
    public MemberInfo get(@PathVariable("memberId") Long memberId) {
        return memberService.getMember(memberId);
    }

    @GetMapping("/api/members")
    public List<MemberInfo> getMembers() {
        return memberService.getMembers();
    }

    @GetMapping("/test-log")
    public void testLog() {
        log.info("This is an INFO log in production!");
        log.error("This is an ERROR log in production!");
    }
}

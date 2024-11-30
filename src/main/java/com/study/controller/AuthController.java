package com.study.controller;

import com.study.domain.Member;
import com.study.request.LoginRequest;
import com.study.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/api/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest, HttpServletRequest request) {
        Member member = authService.login(loginRequest);

        HttpSession session = request.getSession();
        session.setAttribute("member", member);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/api/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();  //세션 제거
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping("/api/session-check")
    public ResponseEntity<String> sessionCheck(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok("Session ID: " + session.getId());
    }
}

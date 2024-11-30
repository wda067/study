package com.study.config;

import com.study.exception.Unauthorized;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * 스프링 인터셉터 흐름
 * HTTP 요청 -> WAS -> 필터 -> 서블릿 -> 스프링 인터셉터 -> 컨트롤러
 * 적절하지 않은 요청이라고 판단하면 컨트롤러 호출 전에 요청을 중단할 수 있다.
 */
@Slf4j
public class AuthInterceptor implements HandlerInterceptor {

    /**
     * preHandle : 컨트롤러 호출 전에 호출된다. (더 정확히는 핸들러 어댑터 호출 전에 호출된다.)
     * preHandle의 응답값이 true면 다음으로 진행하고, false면 더는 진행하지 않는다.
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        String requestURI = request.getRequestURI();
        log.info("인증 인터셉터 실행: {}", requestURI);
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("member") == null) {
            log.info("미인증 사용자 요청");
            throw new Unauthorized();
        }

        return true;
    }

    /**
     * postHandle : 컨트롤러 호출 후에 호출된다. (더 정확히는 핸들러 어댑터 호출 후에 호출된다.)
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    /**
     * afterCompletion : 뷰가 렌더링 된 이후에 호출된다.
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}

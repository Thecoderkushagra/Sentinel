package com.TheCoderKushagra.Sentinel.security;

import com.TheCoderKushagra.Sentinel.entity.AiResponse;
import com.TheCoderKushagra.Sentinel.entity.DemoRequestContext;
import com.TheCoderKushagra.Sentinel.entity.ThreatEvaluationResult;
import com.TheCoderKushagra.Sentinel.service.ThreatEvaluationService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalTime;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class SentinelFilter extends OncePerRequestFilter {
    private final ThreatEvaluationService service;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return path.startsWith("/demo")
                || path.startsWith("/public")
                || "OPTIONS".equalsIgnoreCase(request.getMethod());
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        DemoRequestContext newRequest = buildThreatContext(request);
        ThreatEvaluationResult evaluationResult = service.evaluate(newRequest);

        if (Objects.equals(evaluationResult.getDecision(), "ALLOW")) {
            filterChain.doFilter(request, response);
            return;
        }

        if (Objects.equals(evaluationResult.getDecision(), "BLOCK")) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write("Blocked");
            return;
        }

        if (Objects.equals(evaluationResult.getDecision(), "CHALLENGE")) {

            AiResponse aiResponse = service.callAiService(evaluationResult.toString());

            if ("ALLOW_TRACK_LOGS".equals(aiResponse.getDecision())) {
                filterChain.doFilter(request, response);
                return;
            }

            if ("CAPTCHA".equals(aiResponse.getDecision())) {
                System.out.printf("CAPTCHA: %s%n", aiResponse);
                response.setStatus(HttpServletResponse.SC_ACCEPTED);
                return;
            }

            if ("DENY_TIMEOUT".equals(aiResponse.getDecision())) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.getWriter().write("Request denied with 5 minute timeout");
                return;
            }

            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write("Unknown security decision");
        }
    }


    private DemoRequestContext buildThreatContext(HttpServletRequest request) {

        String userIdHeader = request.getHeader("X-user-id");

        return DemoRequestContext.builder()
                .userId(userIdHeader != null ? Long.valueOf(userIdHeader) : null)
                .role(request.getHeader("X-user-role"))
                .ip(request.getRemoteAddr()).country(null)
                .device(null).usingVpn(false).usingTor(false)
                .failedAttemptsLastHour(0)
                .loginTime(LocalTime.now())
                .endpoint(request.getRequestURI())
                .httpMethod(request.getMethod())
                .build();
    }
}
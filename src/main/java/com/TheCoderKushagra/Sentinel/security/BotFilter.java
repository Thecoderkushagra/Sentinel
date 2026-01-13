package com.TheCoderKushagra.Sentinel.security;

import jakarta.annotation.Nonnull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

@Component
public class BotFilter extends OncePerRequestFilter {

    // ===== Known bot / automation user-agents =====
    private static final List<Pattern> BOT_UA_PATTERNS = List.of(
            Pattern.compile(".*curl.*", Pattern.CASE_INSENSITIVE),
            Pattern.compile(".*wget.*", Pattern.CASE_INSENSITIVE),
            Pattern.compile(".*python-requests.*", Pattern.CASE_INSENSITIVE),
            Pattern.compile(".*scrapy.*", Pattern.CASE_INSENSITIVE),
            Pattern.compile(".*httpclient.*", Pattern.CASE_INSENSITIVE),
            Pattern.compile(".*go-http-client.*", Pattern.CASE_INSENSITIVE),
            Pattern.compile(".*headlesschrome.*", Pattern.CASE_INSENSITIVE)
    );

    // ===== Minimum realistic User-Agent length =====
    private static final int MIN_UA_LENGTH = 15;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            @Nonnull HttpServletResponse response,
            @Nonnull FilterChain filterChain
    ) throws ServletException, IOException {

        String userAgent = request.getHeader("User-Agent");

        // ---------------- A. USER-AGENT VALIDATION ----------------

        // Missing or very short UA → block immediately
        if (userAgent == null || userAgent.length() < MIN_UA_LENGTH) {
            block(response, "Missing or invalid User-Agent");
            return;
        }

        // Known bot UA patterns
        for (Pattern pattern : BOT_UA_PATTERNS) {
            if (pattern.matcher(userAgent).matches()) {
                block(response, "Known bot User-Agent");
                return;
            }
        }

        // ---------------- B. HEADER CONSISTENCY CHECK ----------------

        boolean suspicious = false;

        // Browsers almost always send these
        if (request.getHeader("Accept-Language") == null) {
            suspicious = true;
        }

        if (request.getHeader("Accept") == null) {
            suspicious = true;
        }

        // Modern browsers send at least one Sec-Fetch header
        if (request.getHeader("Sec-Fetch-Site") == null &&
                request.getHeader("Sec-Fetch-Mode") == null) {
            suspicious = true;
        }

        // Optional but strong signal
        if (request.getHeader("Connection") == null) {
            suspicious = true;
        }

        // Mark request for next security layers
        if (suspicious) {
            request.setAttribute("BOT_SUSPECT_LEVEL", 1);
        }

        // Continue filter chain
        filterChain.doFilter(request, response);
    }

    private void block(HttpServletResponse response, String reason) throws IOException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json");

        response.getWriter().write("""
            {
              "status": "blocked",
              "reason": "%s"
            }
            """.formatted(reason));
    }
}


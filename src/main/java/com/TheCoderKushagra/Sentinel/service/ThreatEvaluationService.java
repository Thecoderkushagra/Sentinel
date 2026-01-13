package com.TheCoderKushagra.Sentinel.service;

import com.TheCoderKushagra.Sentinel.entity.DemoRequestContext;
import com.TheCoderKushagra.Sentinel.entity.ThreatEvaluationResult;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ThreatEvaluationService {
    public ThreatEvaluationResult evaluate(DemoRequestContext ctx) {

        int score = 0;
        List<String> reasons = new ArrayList<>();

        // ---------------- NETWORK SIGNALS ----------------
        if (ctx.isUsingTor()) {
            score += 30;
            reasons.add("Request originated from TOR network");
        }

        if (ctx.isUsingVpn()) {
            score += 15;
            reasons.add("VPN usage detected");
        }

        // ---------------- AUTH BEHAVIOR ----------------
        if (ctx.getFailedAttemptsLastHour() >= 3) {
            score += 20;
            reasons.add("Multiple failed authentication attempts");
        }

        // ---------------- DEVICE TRUST ----------------
        if ("NEW".equalsIgnoreCase(ctx.getDevice())) {
            score += 10;
            reasons.add("Login from a new device");
        }

        // ---------------- TIME-BASED HEURISTICS ----------------
        int hour = ctx.getLoginTime().getHour();
        if (hour >= 1 && hour <= 5) {
            score += 10;
            reasons.add("Login during unusual hours");
        }

        // ---------------- CONTEXTUAL RULES ----------------
        if ("ADMIN".equalsIgnoreCase(ctx.getRole())
                && ctx.getEndpoint().startsWith("/admin")
                && ctx.getFailedAttemptsLastHour() > 0) {

            score += 30;
            reasons.add("Admin access attempt with prior failures");
        }

        // ---------------- FINAL DECISION ----------------
        String decision;
        if (score <= 20) {
            decision = "ALLOW";
        } else if (score <= 50) {
            decision = "CHALLENGE";
        } else {
            decision = "BLOCK";
        }

        return ThreatEvaluationResult.builder()
                .threatScore(score)
                .decision(decision)
                .reasons(reasons)
                .build();
    }
}

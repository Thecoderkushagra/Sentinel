package com.TheCoderKushagra.Sentinel.service;

import com.TheCoderKushagra.Sentinel.entity.BotProtectionData;
import com.TheCoderKushagra.Sentinel.repository.BotDataRepo;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Slf4j
@Service
public class Default {
    @Autowired
    private BotDataRepo botDataRepo;

    @PostConstruct
    public void initializeData() {
        boolean b = botDataRepo.existsById("1");

        if (!b) {
            BotProtectionData data = new BotProtectionData();
            data.setId("1");

            data.setHero(new BotProtectionData.Hero(
                    "Bot-Protected Data Access",
                    "Understanding intelligent bot detection and data protection"
            ));

            data.setRealityCheck(new BotProtectionData.RealityCheck(
                    "What Bot Protection Really Means",
                    "Bot-protected pages don't block all automation—they intelligently filter malicious behavior while allowing legitimate access.",
                    "Complete bot immunity",
                    Arrays.asList(
                            new BotProtectionData.RealityItem("❌", "Automated scraping detected", "danger"),
                            new BotProtectionData.RealityItem("⚠️", "Suspicious automation challenged", "warning"),
                            new BotProtectionData.RealityItem("🚫", "Aggressive bots blocked", "dark"),
                            new BotProtectionData.RealityItem("✅", "Real humans pass seamlessly", "success")
                    ),
                    "Protection is risk-based, not absolute"
            ));

            data.setArchitecture(new BotProtectionData.Architecture(
                    "Protection Architecture",
                    "Multi-layered defense system",
                    Arrays.asList(
                            "Client Request",
                            "Spring Security Filter",
                            "Level 1: Static Bot Signals",
                            "Level 2: AI Risk Engine",
                            "Decision Engine",
                            "Response: Allow | Challenge | Block"
                    )
            ));

            data.setStaticDetection(new BotProtectionData.StaticDetection(
                    "Level 1: Static Detection",
                    "Fast, rule-based checks that catch 80% of scrapers instantly",
                    Arrays.asList(
                            new BotProtectionData.DetectionMethod(
                                    "User-Agent Validation",
                                    "Detects missing, fake, or known bot user agents",
                                    Arrays.asList("python-requests", "curl", "wget", "Scrapy", "HeadlessChrome"),
                                    null,
                                    null,
                                    "High"
                            ),
                            new BotProtectionData.DetectionMethod(
                                    "Header Consistency",
                                    "Validates presence of browser-standard headers",
                                    null,
                                    Arrays.asList("Accept-Language", "Sec-Fetch-*", "Referer", "Connection"),
                                    null,
                                    "Very High"
                            ),
                            new BotProtectionData.DetectionMethod(
                                    "Rate Limiting",
                                    "Monitors request patterns per IP and fingerprint",
                                    null,
                                    null,
                                    Arrays.asList("Too many requests", "No think-time", "Sequential patterns"),
                                    "High"
                            )
                    )
            ));

            data.setBehavioralDetection(new BotProtectionData.BehavioralDetection(
                    "Level 2: Behavioral Analysis",
                    "AI-powered pattern recognition that identifies sophisticated bots",
                    Arrays.asList(
                            new BotProtectionData.BehaviorSignal("Request Timing", "Random intervals", "Constant/predictable"),
                            new BotProtectionData.BehaviorSignal("Page Depth", "Shallow browsing", "Deep crawling"),
                            new BotProtectionData.BehaviorSignal("Mouse Movement", "Natural patterns", "None detected"),
                            new BotProtectionData.BehaviorSignal("JS Execution", "Full support", "Often absent"),
                            new BotProtectionData.BehaviorSignal("Session Continuity", "Stable sessions", "Broken/missing"),
                            new BotProtectionData.BehaviorSignal("Cookie Handling", "Proper support", "Inconsistent")
                    ),
                    Arrays.asList(
                            new BotProtectionData.RiskScore("No JS execution", 30),
                            new BotProtectionData.RiskScore("Fast navigation", 25),
                            new BotProtectionData.RiskScore("No cookies", 20),
                            new BotProtectionData.RiskScore("Patterned URLs", 15),
                            new BotProtectionData.RiskScore("Headless signals", 40)
                    ),
                    Arrays.asList(
                            new BotProtectionData.Decision("< 30", "Allow", "success"),
                            new BotProtectionData.Decision("30-60", "Challenge (CAPTCHA)", "warning"),
                            new BotProtectionData.Decision("> 60", "Block", "danger")
                    )
            ));
            botDataRepo.save(data);
            log.info("Bot data added to the database");
        } else {
            log.info("Bot data Already exist");
        }

    }
}


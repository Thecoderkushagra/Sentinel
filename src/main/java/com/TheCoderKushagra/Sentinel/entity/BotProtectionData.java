package com.TheCoderKushagra.Sentinel.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Document(collection = "bot_protection_data")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BotProtectionData {

    @Id
    private String id;

    private Hero hero;
    private RealityCheck realityCheck;
    private Architecture architecture;
    private StaticDetection staticDetection;
    private BehavioralDetection behavioralDetection;

    // Inner Classes for nested structure

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Hero {
        private String title;
        private String subtitle;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RealityCheck {
        private String title;
        private String description;
        private String misconception;
        private List<RealityItem> reality;
        private String note;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RealityItem {
        private String icon;
        private String label;
        private String color;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Architecture {
        private String title;
        private String subtitle;
        private List<String> flow;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StaticDetection {
        private String title;
        private String description;
        private List<DetectionMethod> methods;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DetectionMethod {
        private String name;
        private String description;
        private List<String> examples;
        private List<String> headers;
        private List<String> indicators;
        private String effectiveness;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BehavioralDetection {
        private String title;
        private String description;
        private List<BehaviorSignal> signals;
        private List<RiskScore> riskScoring;
        private List<Decision> decisions;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BehaviorSignal {
        private String metric;
        private String human;
        private String bot;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RiskScore {
        private String condition;
        private Integer score;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Decision {
        private String range;
        private String action;
        private String type;
    }
}

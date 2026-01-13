package com.TheCoderKushagra.Sentinel.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DemoRequestContext {

    private Long userId;
    private String role;

    // Network
    private String ip;
    private String country;

    // Device / Session
    private String device;
    private boolean usingVpn;
    private boolean usingTor;

    // Auth behavior
    private int failedAttemptsLastHour;

    // Timing
    private LocalTime loginTime;

    // Request intent
    private String endpoint;
    private String httpMethod;
}

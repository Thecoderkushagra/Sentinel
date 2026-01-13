package com.TheCoderKushagra.Sentinel.entity;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ThreatEvaluationResult {
    private int threatScore;
    private String decision;
    private List<String> reasons;
}

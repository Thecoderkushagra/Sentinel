package com.TheCoderKushagra.Sentinel.controller;

import com.TheCoderKushagra.Sentinel.entity.AiResponse;
import com.TheCoderKushagra.Sentinel.entity.BotProtectionData;
import com.TheCoderKushagra.Sentinel.entity.DemoRequestContext;
import com.TheCoderKushagra.Sentinel.entity.ThreatEvaluationResult;
import com.TheCoderKushagra.Sentinel.repository.BotDataRepo;
import com.TheCoderKushagra.Sentinel.service.ThreatEvaluationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api")
public class SentinelController {
    @Autowired
    private BotDataRepo botDataRepo;
    @Autowired
    private ThreatEvaluationService threatEvaluationService;


    @GetMapping("/bot/data")
    public ResponseEntity<BotProtectionData> botData() {
        try{
            BotProtectionData data = botDataRepo.findById("1")
                    .orElseThrow(() -> new RuntimeException("Data not found"));
            return new ResponseEntity<>(data, HttpStatus.OK);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/evaluate")
    public ResponseEntity<Map<String, Object>> evaluateThreat(@RequestBody DemoRequestContext requestContext) {
        ThreatEvaluationResult result = threatEvaluationService.evaluate(requestContext);
        AiResponse aiResponse = null;

        if ("CHALLENGE".equals(result.getDecision())) {
            try {
                aiResponse = threatEvaluationService.callAiService(result.toString());
            } catch (Exception e) {
                log.error("AI Evaluation failed", e);
            }
        }

        Map<String, Object> response = new HashMap<>();
        response.put("result", result);
        response.put("aiResponse", aiResponse);

        return ResponseEntity.ok(response);
    }
}
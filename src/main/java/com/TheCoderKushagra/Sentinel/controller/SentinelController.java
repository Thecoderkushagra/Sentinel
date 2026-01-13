package com.TheCoderKushagra.Sentinel.controller;

import com.TheCoderKushagra.Sentinel.entity.BotProtectionData;
import com.TheCoderKushagra.Sentinel.entity.DemoRequestContext;
import com.TheCoderKushagra.Sentinel.entity.ThreatEvaluationResult;
import com.TheCoderKushagra.Sentinel.repository.BotDataRepo;
import com.TheCoderKushagra.Sentinel.service.ThreatEvaluationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
            BotProtectionData data = botDataRepo.findById("1").orElseThrow(
                    () -> new RuntimeException("Data not found"));
            return new ResponseEntity<>(data, HttpStatus.OK);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/evaluate")
    public ResponseEntity<ThreatEvaluationResult> evaluateThreat (@RequestBody DemoRequestContext requestContext) {
        ThreatEvaluationResult result = threatEvaluationService.evaluate(requestContext);
        // also add AI response here ==========>
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}

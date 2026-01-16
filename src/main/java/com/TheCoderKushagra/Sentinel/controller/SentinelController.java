package com.TheCoderKushagra.Sentinel.controller;

import com.TheCoderKushagra.Sentinel.entity.BotProtectionData;
import com.TheCoderKushagra.Sentinel.entity.DemoRequestContext;
import com.TheCoderKushagra.Sentinel.entity.ThreatEvaluationResult;
import com.TheCoderKushagra.Sentinel.repository.BotDataRepo;
import com.TheCoderKushagra.Sentinel.service.ThreatEvaluationService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/api")
public class SentinelController {
    @Autowired
    private BotDataRepo botDataRepo;
    @Autowired
    private ThreatEvaluationService threatEvaluationService;
    @Autowired
    private ChatClient Gemini;

    @Value("classpath:/prompts/systemPrompt.st")
    private Resource systemPrompt;

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
    public ResponseEntity<Map<String,Object>> evaluateThreat(@RequestBody DemoRequestContext requestContext) {
        ThreatEvaluationResult result = threatEvaluationService.evaluate(requestContext);
        String aiString = null;
        if (Objects.equals(result.getDecision(), "CHALLENGE")) {
            aiString = Gemini.prompt()
                    .user(result.toString())
                    .system(systemPrompt)
                    .call().content();
        }
        return new ResponseEntity<>(
                Map.of(
                        "result", result,
                        "aiResponse", aiString),
                HttpStatus.OK);
    }
}

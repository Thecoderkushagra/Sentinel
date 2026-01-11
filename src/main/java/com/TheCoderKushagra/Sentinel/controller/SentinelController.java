package com.TheCoderKushagra.Sentinel.controller;

import com.TheCoderKushagra.Sentinel.entity.BotProtectionData;
import com.TheCoderKushagra.Sentinel.repository.BotDataRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@CrossOrigin("*")
public class SentinelController {
    @Autowired
    private BotDataRepo botDataRepo;

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
}

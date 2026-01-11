package com.TheCoderKushagra.Sentinel.repository;

import com.TheCoderKushagra.Sentinel.entity.BotProtectionData;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BotDataRepo extends MongoRepository<BotProtectionData, String> {
}

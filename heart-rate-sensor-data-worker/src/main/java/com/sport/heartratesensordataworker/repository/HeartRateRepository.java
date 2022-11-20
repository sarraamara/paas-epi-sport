package com.sport.heartratesensordataworker.repository;

import com.sport.heartratesensordataworker.model.UserHeartRate;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface HeartRateRepository extends MongoRepository<UserHeartRate, String> {
}

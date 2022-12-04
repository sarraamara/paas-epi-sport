package com.sport.heartratesensordataworker.repository;

import com.sport.heartratesensordataworker.model.Emergency;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmergencyRepository extends MongoRepository<Emergency, String> {
}

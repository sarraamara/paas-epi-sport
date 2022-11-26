package com.sport.emergencynotifagent.repository;

import com.sport.emergencynotifagent.model.UserProfile;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserProfileRepository extends CrudRepository<UserProfile, Long> {
    UserProfile findByUserId(int userId);
}

package com.sport.emergencynotifagent.repository;

import com.sport.emergencynotifagent.model.UserCoach;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserCoachRepository extends CrudRepository<UserCoach, Long> {

    @Query(value="SELECT userCoachId, userId, coachId FROM userCoach WHERE userId=:userId", nativeQuery = true)
    List<UserCoach> findUserCoachesByUserId(@Param("userId") int userId);
}

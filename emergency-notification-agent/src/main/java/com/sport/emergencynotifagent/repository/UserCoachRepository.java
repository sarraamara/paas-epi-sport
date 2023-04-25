package com.sport.emergencynotifagent.repository;

import com.sport.emergencynotifagent.model.UserCoachDto;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface UserCoachRepository extends CrudRepository<UserCoachDto, Long> {

    @Query(value="SELECT userCoachId, userId, coachId FROM userCoach WHERE userId=:userId", nativeQuery = true)
    List<UserCoachDto> findUserCoachesByUserId(@Param("userId") int userId);
}

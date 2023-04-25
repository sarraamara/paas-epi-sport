package com.sport.emergencynotifagent.repository;

import com.sport.common.model.CoachProfile;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CoachProfileRepository extends CrudRepository<CoachProfile, Long> {

    @Query(value="SELECT * FROM coachProfile WHERE coachId=:coachId", nativeQuery = true)
    CoachProfile findByCoachId(@Param("coachId") int coachId);
}

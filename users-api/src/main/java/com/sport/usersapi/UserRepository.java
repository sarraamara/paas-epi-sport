package com.sport.usersapi;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    @Query(value = "SELECT * FROM epi_sport.userProfile", nativeQuery = true)
    Iterable<User> getAll();

    @Query(value = "SELECT * FROM epi_sport.userProfile WHERE userId=:userId", nativeQuery = true)
    User getUserByUserId(Long userId);

    @Query(value = "DELETE FROM epi_sport.userProfile WHERE userId=:userId", nativeQuery = true)
    void deleteByUserId(Long userId);
}

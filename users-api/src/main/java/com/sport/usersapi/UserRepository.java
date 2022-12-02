package com.sport.usersapi;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("select u from User u where u.userId = ?1")
    User findUserByUserId(Long userId);

    @Query("select u from User u")
    List<User> findAll();

    @Transactional
    @Modifying
    @Query("delete from User u where u.userId = ?1")
    void deleteByUserId(Long userId);
}

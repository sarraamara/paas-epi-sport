package com.sport.notificationchannelmanager.repository;

import com.sport.notificationchannelmanager.model.Coach;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotifyCoachRepository extends CrudRepository<Coach, String> {
}

package com.sport.notificationchannelmanager;

import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotifyCoachRepository extends CrudRepository<Coach, String> {
}

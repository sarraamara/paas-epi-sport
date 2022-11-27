package com.sport.notificationchannelmanager;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

@AllArgsConstructor
@Getter
@Setter
@RedisHash(value = "Coach", timeToLive = 600)
public class Coach implements Serializable {
    @Id
    private String coachId;
}

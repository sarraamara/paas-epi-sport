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
@RedisHash(value = "Coach")
public class Coach implements Serializable {
    @Id
    private String coachId;

    @Override
    public String toString() {
        return "Coach{" +
                "coachId='" + coachId + '\'' +
                '}';
    }

    public String getCoachId() {
        return coachId;
    }

    public void setCoachId(String coachId) {
        this.coachId = coachId;
    }

}

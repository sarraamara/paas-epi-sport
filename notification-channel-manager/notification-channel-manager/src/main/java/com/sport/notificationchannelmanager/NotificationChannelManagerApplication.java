package com.sport.notificationchannelmanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@SpringBootApplication
@EnableRedisRepositories
public class NotificationChannelManagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(NotificationChannelManagerApplication.class, args);
	}

//	@Bean
//	JedisConnectionFactory jedisConnectionFactory() {
//		JedisConnectionFactory jedisConFactory
//				= new JedisConnectionFactory();
//		jedisConFactory.setHostName("172.31.250.236");
//		jedisConFactory.setPort(6379);
//		return jedisConFactory;
//	}

	@Bean
	public RedisTemplate<?, ?> redisTemplate(RedisConnectionFactory connectionFactory) {
		RedisTemplate<?, ?> template = new RedisTemplate<>();
		template.setConnectionFactory(connectionFactory);
		return template;
	}
}

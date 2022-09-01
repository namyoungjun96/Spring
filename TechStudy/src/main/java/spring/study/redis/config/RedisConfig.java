package spring.study.redis.config;

import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

public class RedisConfig {
	@Bean
	public LettuceConnectionFactory redisConnectionFactory() {
		return new LettuceConnectionFactory(new RedisStandaloneConfiguration("server", 6379));
	}
	
	@Bean
	public RedisTemplate<String, Object> redisTemplate() {
		RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
		
		return redisTemplate;
	}
}

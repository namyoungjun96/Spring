package spring.study.login.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

@Configuration
public class LettuceConfig {
	 @Bean
	  public LettuceConnectionFactory redisConnectionFactory() {

	    return new LettuceConnectionFactory(new RedisStandaloneConfiguration("server", 6379));
	  }
}
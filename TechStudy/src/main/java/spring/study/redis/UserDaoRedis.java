package spring.study.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

public class UserDaoRedis {
	@Autowired RedisTemplate<String, Object> redisTempalte;
}

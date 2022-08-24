package spring.study.login.dao;

import java.net.URL;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;

import spring.study.login.domain.User;

public class UserDaoRedis implements UserDao {
	@Autowired
	private RedisTemplate<String, String> template;

	// inject the template as ListOperations
	@Resource(name="redisTemplate")
	private ListOperations<String, String> listOps;

	public void addLink(String userId, URL url) {
		listOps.leftPush(userId, url.toExternalForm());
	}

	@Override
	public void add(User user) {
		// TODO Auto-generated method stub
	}

	@Override
	public User get(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<User> getAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteAll() {
		// TODO Auto-generated method stub

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void update(User user) {
		// TODO Auto-generated method stub

	}

}

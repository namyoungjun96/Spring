package spring.study.login.dao;

import java.util.List;

import spring.study.login.domain.User;

interface UserDao {
	void add(User user);
	User get(String id);
	List<User> getAll();
	void deleteAll();
	int getCount();
	void update(User user);
}

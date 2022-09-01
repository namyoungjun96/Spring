package com.jun.jwt.dao;

import java.util.List;

import com.jun.jwt.domain.User;

interface UserDao {
	void add(User user);
	User get(String id);
	List<User> getAll();
	void deleteAll();
	int getCount();
	void update(User user);
}

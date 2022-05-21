package springbook.user.service;

import springbook.user.domain.User;

interface UserService {
	void add(User user);
	void upgradeLevels();
}

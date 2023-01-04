package springbook.learningtest.optional;

import java.util.Optional;

public class UseOptional {
	public int userIsNull(User user) {
		Optional<User> optional = Optional.ofNullable(user);
		User result = optional.get();

		return result.getMoney();
	}
}

package springbook.learningtest.optional;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.hamcrest.CoreMatchers.equalTo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

public class OptionalTest {
	User[] user;

	@BeforeEach
	void setUp() {
		user = new User[10];
	}

	@Test
	void printNullValue() {
		Optional optional = Optional.ofNullable(user[0]);

		int value = (int) optional.orElse(1);

		assertThat(1, equalTo(value));
	}

	@Test
	void isPresent() {
		UseOptional sut = new UseOptional();

		int money = sut.userIsNull(user[0]);

		assertThat(5000, equalTo(money));
	}
}

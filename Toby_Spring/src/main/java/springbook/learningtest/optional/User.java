package springbook.learningtest.optional;

import lombok.Data;

@Data
public class User {
	private Integer money;
	private Product toy;

	public User(int money, Product toy) {
		this.money = money;
		this.toy = toy;
	}

	public User() {}
}

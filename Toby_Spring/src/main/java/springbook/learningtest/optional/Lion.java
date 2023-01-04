package springbook.learningtest.optional;

import lombok.Data;

@Data
public class Lion implements Doll {
	private int price;
	private int count;

	@Override
	public void bark() {
		System.out.println("lion");
	}
}

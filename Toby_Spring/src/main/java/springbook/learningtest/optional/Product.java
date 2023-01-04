package springbook.learningtest.optional;

public enum Product {
	LION("사자", 5000), NOTHING("없음", 0);

	private String name;
	private int price;

	Product(String name, int price) {
		this.name = name;
		this.price = price;
	}

	public String getName() {
		return name;
	}

	public int getPrice() {
		return price;
	}
}

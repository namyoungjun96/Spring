package springbook.learningtest.polymorphism;

public class B implements B1, B2 {
	private String name;
	private int num;
	
	public void setNum(int num) {
		this.num = num;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String print() {
		return name;
	}

	@Override
	public int num() {
		return num;
	}
}

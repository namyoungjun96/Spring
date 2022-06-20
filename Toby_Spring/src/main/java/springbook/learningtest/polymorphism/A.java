package springbook.learningtest.polymorphism;

public class A {
	B1 b1;
	
	public void setB1(B1 b1) {
		this.b1 = b1;
	}
	
	public String printName() {
		String name = b1.print();
		return "my name is " + name;
	}
}

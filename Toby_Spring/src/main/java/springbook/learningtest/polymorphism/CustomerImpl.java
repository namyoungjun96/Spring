package springbook.learningtest.polymorphism;

public class CustomerImpl implements Customer {
	Bugerking bugerking;
	
	public void setBugerking(Bugerking bugerking) {
		this.bugerking = bugerking;
	}
	
	@Override
	public String orderFood() {
		String food = bugerking.giveHambuger();
		return food;
	}
}

package springbook.learningtest.polymorphism;

public class Store implements Bugerking, Kfc {
	Hambuger hambuger;
	
	public void setHambuger(Hambuger hambuger) {
		this.hambuger = hambuger;
	}
	
	@Override
	public String giveHambuger() {
		String food = this.hambuger.returnBuger();
		return food;
	}
	
	@Override
	public String giveChicken() {
		// TODO Auto-generated method stub
		return null;
	}
}

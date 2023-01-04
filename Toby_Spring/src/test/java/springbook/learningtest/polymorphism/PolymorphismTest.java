package springbook.learningtest.polymorphism;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = "/Polymorphism-context.xml")
public class PolymorphismTest {
	@Autowired Customer customer;
	
	@Test
	public void giveFood() {
		assertThat(customer.orderFood(), equalTo("와퍼"));
	}
}

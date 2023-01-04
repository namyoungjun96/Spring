package com.yeongjun.unittestbook.Unit1;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CalculatorTest {
	Calculator sut;

	@BeforeEach
	void setUp() {
		sut = new Calculator();
	}

	@Test
	void sumOfTwoNumbers() {
		double first = 10;
		double second = 20;

		double result = sut.sum(first, second);

		assertEquals(30, result);
	}
}
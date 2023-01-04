package com.yeongjun.unittestbook.Unit1;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CodeCoverageTest {
	CodeCoverage codeCoverage;

	@BeforeEach
	void setUp() {
		codeCoverage = new CodeCoverage();
	}

	@Test
	void isStringLong() {
		boolean result = codeCoverage.isStringLong("abc");
		assertEquals(false, result);
	}

	@Test
	void parse() {
		int result = codeCoverage.Parse("5");
		assertEquals(5, result);
	}
}
package com.yeongjun.unittestbook.Unit1;

public class CodeCoverage {
	public boolean isStringLong(String input) {
		boolean result = input.length() > 5;
		WasLasStringLongObject obj = new WasLasStringLongObject();
		obj.setWasLasStringLong(result);

		return result;
	}

	public int Parse(String input) {
		return Integer.parseInt(input);
	}
}

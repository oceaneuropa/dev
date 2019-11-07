package org.plutus.lottery.common.powerball;

public class FirstDigitsCount {

	protected int[] firstDigitsArray;
	protected String firstDigits;
	protected Integer count;

	public FirstDigitsCount() {
	}

	public FirstDigitsCount(int[] firstDigitsArray, String firstDigits, Integer count) {
		this.firstDigitsArray = firstDigitsArray;
		this.firstDigits = firstDigits;
		this.count = count;
	}

	public int[] getFirstDigitsArray() {
		return this.firstDigitsArray;
	}

	public void setFirstDigitsArray(int[] firstDigitsArray) {
		this.firstDigitsArray = firstDigitsArray;
	}

	public String getFirstDigits() {
		return this.firstDigits;
	}

	public void setFirstDigits(String firstDigits) {
		this.firstDigits = firstDigits;
	}

	public Integer getCount() {
		return this.count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

}

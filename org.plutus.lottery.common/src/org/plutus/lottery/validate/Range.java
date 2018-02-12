package org.plutus.lottery.validate;

public class Range<T extends Comparable<T>> {

	public static String DEFAULT_LEFT_NOTATION = "[";
	public static String DEFAULT_RIGHT_NOTATION = "]";

	protected String leftNotation;
	protected T min;
	protected T max;
	protected String rightNotation;

	/**
	 * @param leftNotation
	 * @param min
	 * @param max
	 * @param rightNotation
	 */
	public Range(String leftNotation, T min, T max, String rightNotation) {
		this.leftNotation = checkLeftNotation(leftNotation);
		this.min = min;
		this.max = max;
		this.rightNotation = checkRighNotation(rightNotation);
	}

	protected String checkLeftNotation(String leftNotation) {
		if (!"[".equals(leftNotation) && !"(".equals(leftNotation)) {
			leftNotation = DEFAULT_LEFT_NOTATION;
		}
		return leftNotation;
	}

	protected String checkRighNotation(String rightNotation) {
		if (!"]".equals(rightNotation) && !")".equals(rightNotation)) {
			rightNotation = DEFAULT_RIGHT_NOTATION;
		}
		return rightNotation;
	}

	public T getMin() {
		return min;
	}

	public void setMin(T min) {
		this.min = min;
	}

	public T getMax() {
		return max;
	}

	public void setMax(T max) {
		this.max = max;
	}

	public boolean inRange(T value) {
		boolean withinLeft = false;
		boolean withinRight = false;

		int leftCompare = value.compareTo(this.min);
		if ("[".equals(this.leftNotation)) {
			if (leftCompare >= 0) {
				withinLeft = true;
			}
		} else {
			if (leftCompare > 0) {
				withinLeft = true;
			}
		}

		int rightCompare = value.compareTo(this.max);
		if ("]".equals(this.rightNotation)) {
			if (rightCompare <= 0) {
				withinRight = true;
			}
		} else {
			if (rightCompare < 0) {
				withinRight = true;
			}
		}

		if (withinLeft && withinRight) {
			return true;
		}
		return false;
	}

	@Override
	public String toString() {
		String text = this.leftNotation + this.min + "," + this.max + this.rightNotation;
		return text;
	}

}

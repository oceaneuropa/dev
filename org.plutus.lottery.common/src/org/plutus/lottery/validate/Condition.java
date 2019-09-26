package org.plutus.lottery.validate;

import org.plutus.lottery.common.Draw;

public abstract class Condition<T extends Comparable<T>> {

	protected String name;
	protected Range<T> range;
	protected T actualValue;

	public Condition(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public Range<T> getRange() {
		return range;
	}

	public void setRange(Range<T> range) {
		this.range = range;
	}

	public T getActualValue() {
		return this.actualValue;
	}

	public void setActualValue(T actualValue) {
		this.actualValue = actualValue;
	}

	public abstract boolean match(Draw draw);

	public String toString() {
		String text = getName() + " -> " + this.range.toString() + " " + this.actualValue;
		return text;
	}

}

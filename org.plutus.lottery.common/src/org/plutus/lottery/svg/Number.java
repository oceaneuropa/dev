package org.plutus.lottery.svg;

import org.origin.svg.widgets.Control;

public class Number extends Control {

	protected int number;
	protected boolean match;

	public Number(Board board, int number, boolean match) {
		super(board);
		this.number = number;
		this.match = match;
	}

	public void createContents() {

	}

	public int getNumber() {
		return this.number;
	}

	public boolean match() {
		return this.match;
	}

}

package org.plutus.lottery.svg.control;

import org.origin.svg.graphics.Point;
import org.origin.svg.widgets.Control;

public class NumberPart extends Control {

	protected int number;
	protected boolean match;
	protected Point index; // (x,y) index within board
	protected Point location; // (x,y) abs location within board
	protected boolean showNumber = true;

	/**
	 * 
	 * @param board
	 * @param number
	 * @param match
	 */
	public NumberPart(DrawPart board, int number, boolean match) {
		super(board);
		this.number = number;
		this.match = match;
	}

	public int getNumber() {
		return this.number;
	}

	public boolean match() {
		return this.match;
	}

	public Point getIndex() {
		return this.index;
	}

	public void setIndex(Point index) {
		this.index = index;
	}

	public Point getLocation() {
		return this.location;
	}

	public void setLocation(Point location) {
		this.location = location;
	}

	public boolean showNumber() {
		return this.showNumber;
	}

	public void setShowNumber(boolean showNumber) {
		this.showNumber = showNumber;
	}

	public void createContents() {

	}

}

package org.plutus.lottery.render.control;

import org.origin.svg.graphics.Point;
import org.origin.wwt.widgets.Composite;
import org.origin.wwt.widgets.WebWidget;
import org.origin.wwt.widgets.layout.WWT;

public class NumberPart extends WebWidget {

	protected int number;
	protected boolean isPBNumber;
	protected boolean match;
	protected boolean isDummy;

	protected Point index; // (x,y) index within board
	protected Point location; // (x,y) abs location within board
	protected boolean showNumber = true;

	/**
	 * 
	 * @param parent
	 * @param number
	 * @param isPBNumber
	 * @param match
	 * @param isDummy
	 */
	public NumberPart(Composite parent, int number, boolean isPBNumber, boolean match, boolean isDummy) {
		super(parent, WWT.NOT_SET, null);
		this.number = number;
		this.isPBNumber = isPBNumber;
		this.match = match;
		this.isDummy = isDummy;
	}

	public int getNumber() {
		return this.number;
	}

	public boolean isPBNumber() {
		return this.isPBNumber;
	}

	public boolean match() {
		return this.match;
	}

	public boolean isDummy() {
		return this.isDummy;
	}

	public void setDummy(boolean isDummy) {
		this.isDummy = isDummy;
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

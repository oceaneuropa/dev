package org.plutus.lottery.svg.control;

import java.util.ArrayList;
import java.util.List;

import org.origin.svg.graphics.Rectangle;
import org.origin.svg.widgets.Composite;
import org.origin.svg.widgets.Display;

public class IntegersPart extends Composite {

	protected int startNum;
	protected int endNum;
	protected List<Integer> matchedNums = new ArrayList<Integer>();
	protected List<NumberPart> numberParts = new ArrayList<NumberPart>();
	protected List<NumberPart> matchedNumberParts = new ArrayList<NumberPart>();

	public IntegersPart(Display display, Integer startNum, Integer endNum, int style) {
		super(display, checkStyle(style));
		this.startNum = startNum;
		this.endNum = endNum;
	}

	public IntegersPart(Composite parent, Integer startNum, Integer endNum, int style) {
		super(parent, checkStyle(style));
		this.startNum = startNum;
		this.endNum = endNum;
	}

	public Integer getMatchedNum() {
		if (!this.matchedNums.isEmpty()) {
			return this.matchedNums.get(0);
		}
		return null;
	}

	public void setMatchedNum(Integer matchedNum) {
		if (!this.matchedNums.contains(matchedNum)) {
			this.matchedNums.add(matchedNum);
		}
	}

	protected static int checkStyle(int style) {
		return style;
	}

	public List<NumberPart> getNumberParts() {
		return this.numberParts;
	}

	public List<NumberPart> getMatchedNumberParts() {
		return this.matchedNumberParts;
	}

	public void createContents() {
		int number_x = 0;
		int number_y = 0;
		int number_w = 10;
		int number_h = 10;

		for (int i = this.startNum; i <= this.endNum; i++) {
			Rectangle numberBounds = new Rectangle(number_x, number_y, number_w, number_h);

			NumberPart numberPart = new NumberPart(this, i, false, this.matchedNums.contains(i));
			numberPart.setBounds(numberBounds);
			numberPart.createContents();

			this.numberParts.add(numberPart);
			if (numberPart.match()) {
				this.matchedNumberParts.add(numberPart);
			}
			number_y += number_h;
		}
	}

}

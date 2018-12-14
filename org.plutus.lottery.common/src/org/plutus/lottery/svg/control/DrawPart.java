package org.plutus.lottery.svg.control;

import java.util.ArrayList;
import java.util.List;

import org.origin.svg.graphics.Point;
import org.origin.svg.graphics.Rectangle;
import org.origin.svg.widgets.Composite;
import org.origin.svg.widgets.Display;
import org.origin.svg.widgets.util.StyleUtil;
import org.plutus.lottery.powerball.Draw;
import org.plutus.lottery.svg.PBConstants;

public class DrawPart extends Composite {

	protected Draw draw;
	protected List<NumberPart> numberParts = new ArrayList<NumberPart>();
	protected List<NumberPart> matchedNumberParts = new ArrayList<NumberPart>();
	protected List<LinkPart> linkParts = new ArrayList<LinkPart>();
	protected boolean showLinks = false;

	/**
	 * 
	 * @param display
	 * @param draw
	 */
	public DrawPart(Display display, Draw draw, int style) {
		super(display, checkStyle(style));
		this.draw = draw;
	}

	/**
	 * 
	 * @param parent
	 * @param draw
	 */
	public DrawPart(Composite parent, Draw draw, int style) {
		super(parent, checkStyle(style));
		this.draw = draw;
	}

	protected static int checkStyle(int style) {
		if (!StyleUtil.hasStyle(style, PBConstants.DRAW_ROUND) //
				&& !StyleUtil.hasStyle(style, PBConstants.DRAW_SQUARE_01x69) //
				&& !StyleUtil.hasStyle(style, PBConstants.DRAW_SQUARE_10x07) //
				&& !StyleUtil.hasStyle(style, PBConstants.DRAW_SQUARE_14x05) //
		) {
			style = StyleUtil.appendStyle(style, PBConstants.DRAW_SQUARE_14x05);
		}
		return style;
	}

	public boolean showLinks() {
		return this.showLinks;
	}

	public void setShowLinks(boolean showLinks) {
		this.showLinks = showLinks;
	}

	public Draw getDraw() {
		return this.draw;
	}

	public List<NumberPart> getNumbers() {
		return this.numberParts;
	}

	public List<NumberPart> getMatchedNumbers() {
		return this.matchedNumberParts;
	}

	public List<LinkPart> getLinks() {
		return this.linkParts;
	}

	public void createContents() {
		if (hasStyle(PBConstants.DRAW_ROUND)) {

		} else if (hasStyle(PBConstants.DRAW_SQUARE_01x69)) {
			createContents_01x69();
		} else if (hasStyle(PBConstants.DRAW_SQUARE_14x05)) {
			createContents_14x05();
		} else if (hasStyle(PBConstants.DRAW_SQUARE_10x07)) {
			createContents_10x07();
		} else {
			createContents_14x05();
		}
	}

	public void createContents_01x69() {
		int number_x = 0;
		int number_y = 0;
		int number_w = 10;
		int number_h = 10;

		for (int i = 1; i <= 69; i++) {
			Rectangle numberBounds = new Rectangle(number_x, number_y, number_w, number_h);

			NumberPart numberPart = new NumberPart(this, i, false, this.draw.numContains(i));
			numberPart.setBounds(numberBounds);
			numberPart.createContents();

			this.numberParts.add(numberPart);
			if (numberPart.match()) {
				this.matchedNumberParts.add(numberPart);
			}

			number_y += number_h;
		}
	}

	public void createContents_10x07() {
		int number_x = 0;
		int number_y = 0 + 10; // 10 is title height
		int number_w = 10;
		int number_h = 10;

		int index_x = 0;
		int index_y = 0;

		for (int i = 1; i <= 69; i++) {
			Rectangle numberBounds = new Rectangle(number_x, number_y, number_w, number_h);

			NumberPart numberPart = new NumberPart(this, i, false, this.draw.numContains(i));
			numberPart.setBounds(numberBounds);
			numberPart.createContents();

			this.numberParts.add(numberPart);
			if (numberPart.match()) {
				this.matchedNumberParts.add(numberPart);
			}

			number_x += number_w;
			if ((i % 10) == 0) {
				number_x = 0;
				number_y += number_h;
			}

			Point index = new Point(index_x, index_y);
			Point location = new Point(numberBounds.getX() + 5 + 1, numberBounds.getY() + 5 + 1);
			numberPart.setIndex(index);
			numberPart.setLocation(location);

			index_x++;
			if ((i % 10) == 0) {
				index_x = 0;
				index_y++;
			}
		}

		if (showLinks()) {
			List<LinkPart> links = createLinks(this.matchedNumberParts);
			for (LinkPart link : links) {
				this.linkParts.add(link);
			}
		}
	}

	public void createContents_14x05() {
		int number_x = 0;
		int number_y = 0 + 10; // 10 is title height
		int number_w = 10;
		int number_h = 10;

		int index_x = 0;
		int index_y = 0;

		for (int i = 1; i <= 69; i++) {
			Rectangle numberBounds = new Rectangle(number_x, number_y, number_w, number_h);

			NumberPart numberPart = new NumberPart(this, i, false, this.draw.numContains(i));
			numberPart.setBounds(numberBounds);
			numberPart.createContents();
			this.numberParts.add(numberPart);
			if (numberPart.match()) {
				this.matchedNumberParts.add(numberPart);
			}

			number_x += number_w;
			// if (i == 14 || i == 28 || i == 42 || i == 56) {
			if ((i % 14) == 0) {
				number_x = 0;
				number_y += number_h;
			}

			Point index = new Point(index_x, index_y);
			Point location = new Point(numberBounds.getX() + 5 + 1, numberBounds.getY() + 5 + 1);
			numberPart.setIndex(index);
			numberPart.setLocation(location);

			index_x++;
			if ((i % 14) == 0) {
				index_x = 0;
				index_y++;
			}
		}

		if (showLinks()) {
			List<LinkPart> links = createLinks(this.matchedNumberParts);
			for (LinkPart link : links) {
				this.linkParts.add(link);
			}
		}
	}

	protected List<LinkPart> createLinks_test1(List<NumberPart> matchedNumbers) {
		List<LinkPart> links = new ArrayList<LinkPart>();

		NumberPart num1 = matchedNumbers.get(0);
		NumberPart num2 = matchedNumbers.get(1);
		LinkPart link = new LinkPart(this, num1, num2);
		link.createContents();
		links.add(link);

		return links;
	}

	protected List<LinkPart> createLinks(List<NumberPart> matchedNumbers) {
		List<LinkPart> links = new ArrayList<LinkPart>();

		List<NumberPart> theMatchedNumbers = new ArrayList<NumberPart>(matchedNumbers);
		NumberPart num1 = theMatchedNumbers.remove(0);
		NumberPart num2 = null;
		NumberPart num3 = null;
		NumberPart num4 = null;
		NumberPart num5 = null;

		double minAngle = 361;
		double maxAngle = -1;

		int shift = 0;
		for (NumberPart currOtherMatchedNumber : theMatchedNumbers) {
			Point currPoint = currOtherMatchedNumber.getIndex().clone();
			// Point currPoint = currOtherMatchedNumber.getLocation().clone();
			currPoint.shiftX(shift);
			currPoint.shiftY(shift);

			Point num1Point = num1.getIndex();
			// Point num1Point = num1.getLocation();
			double currAngle = num1Point.getAngle(currPoint);
			if (currAngle < minAngle) {
				minAngle = currAngle;
				num2 = currOtherMatchedNumber;
			}
			if (currAngle > maxAngle) {
				maxAngle = currAngle;
				num3 = currOtherMatchedNumber;
			}

			shift++;
		}
		theMatchedNumbers.remove(num2);
		theMatchedNumbers.remove(num3);

		minAngle = 361;
		maxAngle = -1;
		// shift = 0;
		for (NumberPart currOtherMatchedNumber : theMatchedNumbers) {
			Point currPoint = currOtherMatchedNumber.getIndex().clone();
			// Point currPoint = currOtherMatchedNumber.getLocation().clone();
			currPoint.shiftX(shift);
			currPoint.shiftY(shift);

			Point num2Point = num2.getIndex();
			// Point num2Point = num2.getLocation();
			double currAngle = num2Point.getAngle(currPoint);
			if (currAngle < minAngle) {
				minAngle = currAngle;
				num4 = currOtherMatchedNumber;
			}

			shift++;
		}
		theMatchedNumbers.remove(num4);
		num5 = theMatchedNumbers.get(0);

		LinkPart link1 = new LinkPart(this, num1, num2);
		link1.createContents();
		LinkPart link2 = new LinkPart(this, num1, num3);
		link2.createContents();
		LinkPart link3 = new LinkPart(this, num2, num4);
		link3.createContents();
		LinkPart link4 = new LinkPart(this, num3, num5);
		link4.createContents();
		LinkPart link5 = new LinkPart(this, num4, num5);
		link5.createContents();

		links.add(link1);
		links.add(link2);
		links.add(link3);
		links.add(link4);
		links.add(link5);
		return links;
	}

}

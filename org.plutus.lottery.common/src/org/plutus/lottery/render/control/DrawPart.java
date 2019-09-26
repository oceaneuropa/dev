package org.plutus.lottery.render.control;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.origin.svg.graphics.Point;
import org.origin.svg.graphics.Rectangle;
import org.origin.svg.widgets.Composite;
import org.origin.svg.widgets.Display;
import org.origin.svg.widgets.util.StyleUtil;
import org.plutus.lottery.common.Draw;
import org.plutus.lottery.powerball.PBConstants;

public class DrawPart extends Composite {

	protected Draw draw;
	protected int maxNum;

	protected List<NumberPart> numberParts = new ArrayList<NumberPart>();
	protected Map<Integer, NumberPart> numberPartsMap = new LinkedHashMap<Integer, NumberPart>();
	protected List<NumberPart> matchedNumberParts = new ArrayList<NumberPart>();
	protected List<LinkPart> linkParts = new ArrayList<LinkPart>();
	protected boolean showLinks = false;

	protected List<Draw> predictedDraws;
	protected Map<Integer, String> indexToPredictedLinkStrokeColor = new HashMap<Integer, String>();

	/**
	 * 
	 * @param display
	 * @param draw
	 * @param style
	 * @param maxNum
	 */
	public DrawPart(Display display, Draw draw, int style, int maxNum) {
		super(display, checkStyle(style));
		this.draw = draw;
		this.maxNum = maxNum;
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
				&& !StyleUtil.hasStyle(style, PBConstants.DRAW_SQUARE_01_PER_ROW) //
				&& !StyleUtil.hasStyle(style, PBConstants.DRAW_SQUARE_10_PER_ROW) //
				&& !StyleUtil.hasStyle(style, PBConstants.DRAW_SQUARE_14_PER_ROW) //
		) {
			style = StyleUtil.appendStyle(style, PBConstants.DRAW_SQUARE_14_PER_ROW);
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

	public List<NumberPart> getNumberParts() {
		return this.numberParts;
	}

	public NumberPart getNumberPart(Integer number) {
		return this.numberPartsMap.get(number);
	}

	public List<NumberPart> getMatchedNumberParts() {
		return this.matchedNumberParts;
	}

	public List<LinkPart> getLinks() {
		return this.linkParts;
	}

	public List<Draw> getPredictedDraws() {
		return this.predictedDraws;
	}

	public void setPredictedDraws(List<Draw> predictedDraws) {
		this.predictedDraws = predictedDraws;
	}

	public Map<Integer, String> getIndexToPredictedLinkStrokeColor() {
		return this.indexToPredictedLinkStrokeColor;
	}

	public void setIndexToPredictedLinkStrokeColor(Map<Integer, String> indexToPredictedLinkStrokeColor) {
		this.indexToPredictedLinkStrokeColor = indexToPredictedLinkStrokeColor;
	}

	public void createContents() {
		if (hasStyle(PBConstants.DRAW_ROUND)) {

		} else if (hasStyle(PBConstants.DRAW_SQUARE_01_PER_ROW)) {
			createContents_01_per_row();

		} else if (hasStyle(PBConstants.DRAW_SQUARE_14_PER_ROW)) {
			createContents_14_per_row();

		} else if (hasStyle(PBConstants.DRAW_SQUARE_10_PER_ROW)) {
			createContents_10_per_row();

		} else {
			createContents_14_per_row();
		}
	}

	public void createContents_01_per_row() {
		int number_x = 0;
		int number_y = 0;
		int number_w = 10;
		int number_h = 10;

		boolean isDummy = this.draw.isDummy();
		for (int number = 1; number <= this.maxNum; number++) {
			Rectangle numberBounds = new Rectangle(number_x, number_y, number_w, number_h);

			NumberPart numberPart = new NumberPart(this, number, false, this.draw.numContains(number), isDummy);
			numberPart.setBounds(numberBounds);
			numberPart.createContents();

			this.numberParts.add(numberPart);
			this.numberPartsMap.put(number, numberPart);
			if (numberPart.match()) {
				this.matchedNumberParts.add(numberPart);
			}

			number_y += number_h;
		}
	}

	public void createContents_10_per_row() {
		int number_x = 0;
		int number_y = 0 + 10; // 10 is title height
		int number_w = 10;
		int number_h = 10;

		int index_x = 0;
		int index_y = 0;

		for (int number = 1; number <= this.maxNum; number++) {
			Rectangle numberBounds = new Rectangle(number_x, number_y, number_w, number_h);

			NumberPart numberPart = new NumberPart(this, number, false, this.draw.numContains(number), false);
			numberPart.setBounds(numberBounds);
			numberPart.createContents();

			this.numberParts.add(numberPart);
			this.numberPartsMap.put(number, numberPart);
			if (numberPart.match()) {
				this.matchedNumberParts.add(numberPart);
			}

			number_x += number_w;
			if ((number % 10) == 0) {
				number_x = 0;
				number_y += number_h;
			}

			Point index = new Point(index_x, index_y);
			Point location = new Point(numberBounds.getX() + 5 + 2, numberBounds.getY() + 5 + 2);
			numberPart.setIndex(index);
			numberPart.setLocation(location);

			index_x++;
			if ((number % 10) == 0) {
				index_x = 0;
				index_y++;
			}
		}

		if (showLinks()) {
			if (!this.matchedNumberParts.isEmpty()) {
				List<LinkPart> links = createLinks(this.matchedNumberParts, false, null);
				for (LinkPart link : links) {
					this.linkParts.add(link);
				}
			}

			if (this.predictedDraws != null && !this.predictedDraws.isEmpty()) {
				for (int j = 0; j < predictedDraws.size(); j++) {
					Draw predictedDraw = predictedDraws.get(j);
					String strokeColor = indexToPredictedLinkStrokeColor.get(j);

					NumberPart numPart1 = this.numberPartsMap.get(predictedDraw.getNum1());
					NumberPart numPart2 = this.numberPartsMap.get(predictedDraw.getNum2());
					NumberPart numPart3 = this.numberPartsMap.get(predictedDraw.getNum3());
					NumberPart numPart4 = this.numberPartsMap.get(predictedDraw.getNum4());
					NumberPart numPart5 = this.numberPartsMap.get(predictedDraw.getNum5());

					List<NumberPart> predictedNumberParts = new ArrayList<NumberPart>();
					predictedNumberParts.add(numPart1);
					predictedNumberParts.add(numPart2);
					predictedNumberParts.add(numPart3);
					predictedNumberParts.add(numPart4);
					predictedNumberParts.add(numPart5);
					createLinks(predictedNumberParts, true, strokeColor);
				}
			}
		}
	}

	public void createContents_14_per_row() {
		int number_x = 0;
		int number_y = 0 + 10; // 10 is title height
		int number_w = 10;
		int number_h = 10;

		int index_x = 0;
		int index_y = 0;

		for (int number = 1; number <= this.maxNum; number++) {
			Rectangle numberBounds = new Rectangle(number_x, number_y, number_w, number_h);

			NumberPart numberPart = new NumberPart(this, number, false, this.draw.numContains(number), false);
			numberPart.setBounds(numberBounds);
			numberPart.createContents();
			this.numberParts.add(numberPart);
			this.numberPartsMap.put(number, numberPart);
			if (numberPart.match()) {
				this.matchedNumberParts.add(numberPart);
			}

			number_x += number_w;
			// if (i == 14 || i == 28 || i == 42 || i == 56) {
			if ((number % 14) == 0) {
				number_x = 0;
				number_y += number_h;
			}

			Point index = new Point(index_x, index_y);
			Point location = new Point(numberBounds.getX() + 5 + 2, numberBounds.getY() + 5 + 2);
			numberPart.setIndex(index);
			numberPart.setLocation(location);

			index_x++;
			if ((number % 14) == 0) {
				index_x = 0;
				index_y++;
			}
		}

		if (showLinks()) {
			if (!this.matchedNumberParts.isEmpty()) {
				List<LinkPart> links = createLinks(this.matchedNumberParts, false, null);
				for (LinkPart link : links) {
					this.linkParts.add(link);
				}
			}

			if (this.predictedDraws != null && !this.predictedDraws.isEmpty()) {
				for (int j = 0; j < predictedDraws.size(); j++) {
					Draw predictedDraw = predictedDraws.get(j);
					String strokeColor = indexToPredictedLinkStrokeColor.get(j);

					NumberPart numPart1 = this.numberPartsMap.get(predictedDraw.getNum1());
					NumberPart numPart2 = this.numberPartsMap.get(predictedDraw.getNum2());
					NumberPart numPart3 = this.numberPartsMap.get(predictedDraw.getNum3());
					NumberPart numPart4 = this.numberPartsMap.get(predictedDraw.getNum4());
					NumberPart numPart5 = this.numberPartsMap.get(predictedDraw.getNum5());

					List<NumberPart> predictedNumberParts = new ArrayList<NumberPart>();
					predictedNumberParts.add(numPart1);
					predictedNumberParts.add(numPart2);
					predictedNumberParts.add(numPart3);
					predictedNumberParts.add(numPart4);
					predictedNumberParts.add(numPart5);
					createLinks(predictedNumberParts, true, strokeColor);
				}
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

	/**
	 * 
	 * @param matchedNumbers
	 * @param isPredicted
	 * @param strokeColor
	 * @return
	 */
	protected List<LinkPart> createLinks(List<NumberPart> matchedNumbers, boolean isPredicted, String strokeColor) {
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
		link1.setPredicted(isPredicted);
		link1.setStrokeColor(strokeColor);

		LinkPart link2 = new LinkPart(this, num1, num3);
		link2.setPredicted(isPredicted);
		link2.setStrokeColor(strokeColor);

		LinkPart link3 = new LinkPart(this, num2, num4);
		link3.setPredicted(isPredicted);
		link3.setStrokeColor(strokeColor);

		LinkPart link4 = new LinkPart(this, num3, num5);
		link4.setPredicted(isPredicted);
		link4.setStrokeColor(strokeColor);

		LinkPart link5 = new LinkPart(this, num4, num5);
		link5.setPredicted(isPredicted);
		link5.setStrokeColor(strokeColor);

		link1.createContents();
		link2.createContents();
		link3.createContents();
		link4.createContents();
		link5.createContents();

		links.add(link1);
		links.add(link2);
		links.add(link3);
		links.add(link4);
		links.add(link5);

		return links;
	}

}

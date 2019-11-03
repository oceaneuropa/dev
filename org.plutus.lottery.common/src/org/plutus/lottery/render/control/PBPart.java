package org.plutus.lottery.render.control;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.origin.svg.graphics.Rectangle;
import org.origin.wwt.widgets.Composite;
import org.origin.wwt.widgets.Display;
import org.origin.wwt.widgets.util.StyleUtil;
import org.plutus.lottery.common.Draw;
import org.plutus.lottery.powerball.PBConstants;

public class PBPart extends Composite {

	protected Draw draw;
	protected int maxNum;

	protected List<NumberPart> numberParts = new ArrayList<NumberPart>();
	protected Map<Integer, NumberPart> numberPartsMap = new LinkedHashMap<Integer, NumberPart>();
	protected NumberPart matchedNumberPart;

	/**
	 * 
	 * @param display
	 * @param draw
	 * @param style
	 * @param maxNum
	 */
	public PBPart(Display display, Draw draw, int style, int maxNum) {
		super(display, checkStyle(style));
		this.draw = draw;
		this.maxNum = maxNum;
	}

	/**
	 * 
	 * @param parent
	 * @param draw
	 */
	public PBPart(Composite parent, Draw draw, int style) {
		super(parent, checkStyle(style));
		this.draw = draw;
	}

	protected static int checkStyle(int style) {
		if (!StyleUtil.hasStyle(style, PBConstants.PB_ROUND) //
				&& !StyleUtil.hasStyle(style, PBConstants.PB_SQUARE_01_PER_ROW) //
		) {
			style = StyleUtil.appendStyle(style, PBConstants.PB_SQUARE_01_PER_ROW);
		}
		return style;
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

	public NumberPart getMatchedNumberPart() {
		return this.matchedNumberPart;
	}

	public void createContents() {
		if (hasStyle(PBConstants.PB_ROUND)) {

		} else if (hasStyle(PBConstants.PB_SQUARE_01_PER_ROW)) {
			createContents_01x26();

		} else {
			createContents_01x26();
		}
	}

	public void createContents_01x26() {
		int number_x = 0;
		int number_y = 0;
		int number_w = 10;
		int number_h = 10;

		boolean isDummy = this.draw.isDummy();
		for (int number = 1; number <= this.maxNum; number++) {
			Rectangle numberBounds = new Rectangle(number_x, number_y, number_w, number_h);

			NumberPart numberPart = new NumberPart(this, number, true, this.draw.isPB(number), isDummy);
			numberPart.setBounds(numberBounds);
			numberPart.createContents();

			this.numberParts.add(numberPart);
			this.numberPartsMap.put(number, numberPart);
			if (numberPart.match()) {
				this.matchedNumberPart = numberPart;
			}

			number_y += number_h;
		}
	}

}

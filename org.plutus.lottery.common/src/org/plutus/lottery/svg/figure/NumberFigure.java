package org.plutus.lottery.svg.figure;

import org.origin.svg.Circle;
import org.origin.svg.Text;
import org.origin.svg.util.ColorConstants;
import org.origin.svg.util.SVGConstants;
import org.origin.svg.widgets.render.impl.AbstractControlFigure;
import org.plutus.lottery.svg.control.NumberPart;

public class NumberFigure extends AbstractControlFigure {

	public NumberFigure(NumberPart numberPart) {
		super(numberPart);

		boolean match = numberPart.match();
		boolean isDummy = numberPart.isDummy();
		boolean showNumber = numberPart.showNumber();

		int r = 4;
		int cx = r + 3;
		int cy = r + 3;

		if (match) {
			r = 6;
			cx = r + 0 + 1;
			cy = r + 0;
		} else if (isDummy) {
			r = 4;
			cx = r + 0;
			cy = r + 0;
		}

		Circle circle = new Circle(cx, cy, r);
		if (numberPart.match()) {
			if (numberPart.isPBNumber()) {
				circle.setFillColor(ColorConstants.PURPLE_LITERAL);
				circle.setStrokeColor(ColorConstants.PURPLE_LITERAL);
			} else {
				circle.setFillColor(ColorConstants.GREEN_LITERAL);
				circle.setStrokeColor(ColorConstants.GREEN_LITERAL);
			}
		} else {
			circle.setFillColor(ColorConstants.LIGHT_GREY_LITERAL);
			circle.setStrokeColor(ColorConstants.LIGHT_GREY_LITERAL);
			circle.setFillOpacity(0.4);
		}
		circle.setStrokeWidth(1);

		add(circle);

		if (showNumber) {
			int number = numberPart.getNumber();

			Text text = new Text(4, 4, 5, 8);
			if (match) {
				text = new Text(5, 4, 1, 5);
			} else if (isDummy) {
				text = new Text(4, 4, 1, 5);
			}

			text.setText(String.valueOf(number));
			text.setTextAnchor(SVGConstants.TEXT_ANCHOR_MIDDLE);
			if (match) {
				text.setStrokeColor(ColorConstants.WHITE_LITERAL);
				text.setFillColor(ColorConstants.WHITE_LITERAL);
			} else {
				if (isDummy) {
					text.setStrokeColor(ColorConstants.BLUE_LITERAL);
					text.setFillColor(ColorConstants.BLUE_LITERAL);
				} else {
					text.setStrokeColor(ColorConstants.BLACK_LITERAL);
					text.setFillColor(ColorConstants.BLACK_LITERAL);
					text.setStrokeOpacity(0.4);
					text.setFillOpacity(0.4);
				}
			}

			if (match) {
				text.getTransform().scale(1.2);
			} else {
				if (isDummy) {
					text.getTransform().scale(0.8);
				} else {
					text.getTransform().scale(0.8);
				}
			}

			add(text);
		}
	}

	@Override
	public void updateFigure() {
	}

}

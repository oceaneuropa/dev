package org.plutus.lottery.svg;

import org.origin.svg.Circle;
import org.origin.svg.util.ColorConstants;
import org.origin.svg.widgets.render.impl.AbstractControlFigure;

public class NumberFigure extends AbstractControlFigure {

	public NumberFigure(Number number) {
		super(number);

		int r = 3;
		int cx = r + 3;
		int cy = r + 3;

		Circle circle = new Circle(cx, cy, r);
		if (number.match()) {
			circle.setFillColor(ColorConstants.GREEN_LITERAL);
			circle.setStrokeColor(ColorConstants.GREEN_LITERAL);
		} else {
			circle.setFillColor(ColorConstants.LIGHT_GREY_LITERAL);
			circle.setStrokeColor(ColorConstants.LIGHT_GREY_LITERAL);
		}
		circle.setStrokeWidth(1);

		add(circle);
	}

	@Override
	public void updateFigure() {
	}

}

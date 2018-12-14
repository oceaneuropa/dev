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

		int r = 3;
		int cx = r + 3;
		int cy = r + 3;
		Circle circle = new Circle(cx, cy, r);
		if (numberPart.match()) {
			circle.setFillColor(ColorConstants.GREEN_LITERAL);
			circle.setStrokeColor(ColorConstants.GREEN_LITERAL);
		} else {
			circle.setFillColor(ColorConstants.LIGHT_GREY_LITERAL);
			circle.setStrokeColor(ColorConstants.LIGHT_GREY_LITERAL);
		}
		circle.setStrokeWidth(1);

		add(circle);

		if (numberPart.showNumber()) {
			int number = numberPart.getNumber();
			boolean match = numberPart.match();

			Text text = new Text(4, 4, 5, 8);
			text.setText(String.valueOf(number));
			text.setTextAnchor(SVGConstants.TEXT_ANCHOR_MIDDLE);
			if (match) {
				text.setStrokeColor(ColorConstants.BLACK_LITERAL);
				text.setFillColor(ColorConstants.BLACK_LITERAL);
			} else {
				text.setStrokeOpacity(0.2);
				text.setFillOpacity(0.2);
			}
			text.getTransform().scale(0.7);

			add(text);
		}
	}

	@Override
	public void updateFigure() {
	}

}

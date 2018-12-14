package org.plutus.lottery.svg.figure;

import org.origin.svg.Text;
import org.origin.svg.graphics.Rectangle;
import org.origin.svg.util.SVGConstants;
import org.origin.svg.widgets.render.impl.AbstractControlFigure;
import org.plutus.lottery.svg.control.DrawPart;

public class DrawFigure_Square_01x69 extends AbstractControlFigure {

	/**
	 * 
	 * @param drawPart
	 */
	public DrawFigure_Square_01x69(DrawPart drawPart) {
		super(drawPart);

		Rectangle bounds = drawPart.getBounds();

		// 1. Draw content figures
		// (1) Draw Id
		int drawId_text_x = 0;
		int drawId_text_y = bounds.height + 10;
		int drawId_text_dx = 0;
		int drawId_text_dy = 0;
		Text drawIdText = new Text(drawId_text_x, drawId_text_y, drawId_text_dx, drawId_text_dy);
		drawIdText.setText("#" + String.valueOf(drawPart.getDraw().getDrawId()));
		drawIdText.setTextAnchor(SVGConstants.TEXT_ANCHOR_START);

		// (2) Date
		int date_text_x = 0;
		int date_text_y = bounds.height + 20;
		int date_text_dx = 0;
		int date_text_dy = 0;
		Text dateText = new Text(date_text_x, date_text_y, date_text_dx, date_text_dy);
		dateText.setText(drawPart.getDraw().getDateString());
		dateText.setTextAnchor(SVGConstants.TEXT_ANCHOR_START);

		// 2. Add figures to parent (this)
		add(drawIdText);
		add(dateText);
	}

	@Override
	public void updateFigure() {
	}

}

// // (1) Id
// int drawId_text_x = 5;
// int drawId_text_y = 10;
// int drawId_text_dx = -3;
// int drawId_text_dy = -1;
// Text drawIdText = new Text(drawId_text_x, drawId_text_y, drawId_text_dx, drawId_text_dy);
// drawIdText.setText("#" + String.valueOf(drawPart.getDraw().getDrawId()));
// drawIdText.setTextAnchor(SVGConstants.TEXT_ANCHOR_START);

// (2) Date
// int date_text_x = 10; // text length;
// int date_text_y = 10; // text height
// int date_text_dx = bounds.width - date_text_x; // text x shift
// int date_text_dy = -1; // text y shift
// Text dateText = new Text(date_text_x, date_text_y, date_text_dx, date_text_dy);
// dateText.setText(drawPart.getDraw().getDateString());
// dateText.setTextAnchor(SVGConstants.TEXT_ANCHOR_END);

// (3) Border
// Rect rect = new Rect();
// rect.setX(1);
// rect.setY(1);
// rect.setWidth(bounds.width);
// rect.setHeight(bounds.height);
// rect.setRx(2);
// rect.setRy(2);
// rect.setFillColor(ColorConstants.WHITE_LITERAL);
// rect.setStrokeColor(ColorConstants.GREY_LITERAL);
// rect.setStrokeWidth(1);
// rect.setStrokeOpacity(0.2);
// rect.setFillOpacity(0.2);

// add(drawIdText);
// add(dateText);

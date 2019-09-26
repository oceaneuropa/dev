package org.plutus.lottery.render.render;

import org.origin.svg.Rect;
import org.origin.svg.Text;
import org.origin.svg.graphics.Rectangle;
import org.origin.svg.render.widget.AbstractControlFigure;
import org.origin.svg.util.ColorConstants;
import org.origin.svg.util.SVGConstants;
import org.plutus.lottery.render.control.YearPart;

public class YearFigure extends AbstractControlFigure {

	/**
	 * 
	 * @param yearPart
	 */
	public YearFigure(YearPart yearPart) {
		super(yearPart);

		Rectangle bounds = yearPart.getBounds();

		// 1. Draw content figures
		// (1) Year
		int year_text_x = 5;
		int year_text_y = 10;
		int year_text_dx = -3;
		int year_text_dy = -1;
		Text yearText = new Text(year_text_x, year_text_y, year_text_dx, year_text_dy);
		yearText.setText(String.valueOf(yearPart.getYear()));
		yearText.setTextAnchor(SVGConstants.TEXT_ANCHOR_START);

		// (3) Border
		Rect rect = new Rect();
		rect.setX(1);
		rect.setY(1 + 10); // 10 is for the height of the title area
		rect.setWidth(bounds.width);
		rect.setHeight(bounds.height);
		rect.setRx(5);
		rect.setRy(5);
		rect.setFillColor(ColorConstants.WHITE_LITERAL);
		rect.setStrokeColor(ColorConstants.GREY_LITERAL);
		rect.setStrokeWidth(1);

		// 2. Add all figures to parent figure (this)
		add(yearText);
		add(rect);
	}

	@Override
	public void updateFigure() {
	}

}
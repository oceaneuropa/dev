package org.plutus.lottery.svg;

import org.origin.svg.Rect;
import org.origin.svg.Text;
import org.origin.svg.graphics.Rectangle;
import org.origin.svg.util.ColorConstants;
import org.origin.svg.util.SVGConstants;
import org.origin.svg.widgets.render.impl.AbstractControlFigure;

public class BoardFigure extends AbstractControlFigure {

	/**
	 * 
	 * @param board
	 */
	public BoardFigure(Board board) {
		super(board);

		Rectangle bounds = board.getBounds();

		int draw_text_x = 5;
		int draw_text_y = 10;
		int draw_text_dx = -3;
		int draw_text_dy = -1;
		Text drawText = new Text(draw_text_x, draw_text_y, draw_text_dx, draw_text_dy);
		drawText.setText("#" + String.valueOf(board.getDraw().getDrawId()));
		drawText.setTextAnchor(SVGConstants.TEXT_ANCHOR_START);

		int date_text_x = 10; // text length;
		int date_text_y = 10; // text height
		// int date_text_dx = bounds.width / 2 - date_text_x / 2;
		int date_text_dx = bounds.width - date_text_x; // text x shift
		int date_text_dy = -1; // text y shift
		Text dateText = new Text(date_text_x, date_text_y, date_text_dx, date_text_dy);
		dateText.setText(board.getDraw().getDateString());
		dateText.setTextAnchor(SVGConstants.TEXT_ANCHOR_END);

		Rect rect = new Rect();
		rect.setX(1);
		rect.setY(11);
		rect.setWidth(bounds.width);
		rect.setHeight(bounds.height);
		rect.setRx(2);
		rect.setRy(2);

		rect.setFillColor(ColorConstants.WHITE_LITERAL);
		rect.setStrokeColor(ColorConstants.GREY_LITERAL);
		rect.setStrokeWidth(1);

		add(drawText);
		add(dateText);
		add(rect);
	}

	@Override
	public void updateFigure() {
	}

}

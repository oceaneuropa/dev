package org.plutus.lottery.render.render;

import org.origin.svg.Rect;
import org.origin.svg.Text;
import org.origin.svg.adapter.Notification;
import org.origin.svg.graphics.Rectangle;
import org.origin.svg.util.ColorConstants;
import org.origin.svg.util.SVGConstants;
import org.origin.wwt.widgets.render.AbstractFigure;
import org.plutus.lottery.render.control.DrawPart;

public class DrawFigure_Square_14x05 extends AbstractFigure {

	/**
	 * 
	 * @param drawPart
	 */
	public DrawFigure_Square_14x05(DrawPart drawPart) {
		super(drawPart);

		Rectangle bounds = drawPart.getBounds();

		// 1. Draw content figures
		// (1) Id
		int drawId_text_x = 5;
		int drawId_text_y = 10;
		int drawId_text_dx = -3;
		int drawId_text_dy = -1;
		Text drawIdText = new Text(drawId_text_x, drawId_text_y, drawId_text_dx, drawId_text_dy);
		drawIdText.setText("#" + String.valueOf(drawPart.getDraw().getDrawId()));
		drawIdText.setTextAnchor(SVGConstants.TEXT_ANCHOR_START);

		// (2) Date
		int date_text_x = 10; // text length;
		int date_text_y = 10; // text height
		int date_text_dx = bounds.width - date_text_x; // text x shift
		int date_text_dy = -1; // text y shift
		Text dateText = new Text(date_text_x, date_text_y, date_text_dx, date_text_dy);
		dateText.setText(drawPart.getDraw().getDateString());
		dateText.setTextAnchor(SVGConstants.TEXT_ANCHOR_END);

		// (3) Border
		Rect rect = new Rect();
		rect.setX(1);
		rect.setY(1 + 10); // 10 is for the height of the title area
		rect.setWidth(bounds.width);
		rect.setHeight(bounds.height);
		rect.setRx(2);
		rect.setRy(2);
		rect.setFillColor(ColorConstants.WHITE_LITERAL);
		rect.setStrokeColor(ColorConstants.GREY_LITERAL);
		rect.setStrokeWidth(1);

		// 2. Add figures to parent (this)
		add(drawIdText);
		add(dateText);
		add(rect);
	}

	@Override
	public void updateFigure(Notification notification) {
	}

}

// int date_text_dx = bounds.width / 2 - date_text_x / 2;

// (4) Lines which connect matched numbers
// List<Line> lines = getLines(board);

// if (lines != null) {
// for (Line line : lines) {
// add(line);
// }
// }

// protected List<Line> getLines(Board board) {
// List<Line> lines = new ArrayList<Line>();
//
// Map<Point, Point> indexToLocationMap = board.getIndexToLocationMap();
// List<Point> indexes = new ArrayList<Point>();
// for (Iterator<Point> indexItor = indexToLocationMap.keySet().iterator(); indexItor.hasNext();) {
// Point index = indexItor.next();
// indexes.add(index);
// }
//
// // test
// {
// Point idx1 = indexes.remove(0);
// Point idx2 = indexes.remove(0);
//
// Point loc1 = indexToLocationMap.get(idx1);
// Point loc2 = indexToLocationMap.get(idx2);
//
// Line line1 = new Line(loc1, loc2);
// line1.setStrokeColor(ColorConstants.BLUE_LITERAL);
// line1.setStrokeWidth(2);
//
// lines.add(line1);
// }
//
// return lines;
// }

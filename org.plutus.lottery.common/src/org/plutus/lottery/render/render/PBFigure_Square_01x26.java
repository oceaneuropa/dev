package org.plutus.lottery.render.render;

import org.origin.svg.Text;
import org.origin.svg.adapter.Notification;
import org.origin.svg.graphics.Rectangle;
import org.origin.svg.util.SVGConstants;
import org.origin.wwt.widgets.render.AbstractControlFigure;
import org.plutus.lottery.render.control.PBPart;

public class PBFigure_Square_01x26 extends AbstractControlFigure {

	/**
	 * 
	 * @param pbPart
	 */
	public PBFigure_Square_01x26(PBPart pbPart) {
		super(pbPart);

		Rectangle bounds = pbPart.getBounds();

		// 1. Draw content figures
		// (1) Draw Id
		int drawId_text_x = 0;
		int drawId_text_y = bounds.height + 10;
		int drawId_text_dx = 0;
		int drawId_text_dy = 0;
		Text drawIdText = new Text(drawId_text_x, drawId_text_y, drawId_text_dx, drawId_text_dy);
		drawIdText.setText("#" + String.valueOf(pbPart.getDraw().getDrawId()));
		drawIdText.setTextAnchor(SVGConstants.TEXT_ANCHOR_START);

		// (2) Date
		int date_text_x = 0;
		int date_text_y = bounds.height + 20;
		int date_text_dx = 0;
		int date_text_dy = 0;
		Text dateText = new Text(date_text_x, date_text_y, date_text_dx, date_text_dy);
		dateText.setText(pbPart.getDraw().getDateString());
		dateText.setTextAnchor(SVGConstants.TEXT_ANCHOR_START);

		// 2. Add figures to parent (this)
		add(drawIdText);
		add(dateText);
	}

	@Override
	public void updateFigure(Notification notification) {
	}

}

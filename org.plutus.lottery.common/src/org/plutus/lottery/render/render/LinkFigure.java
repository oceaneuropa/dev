package org.plutus.lottery.render.render;

import org.origin.svg.Line;
import org.origin.svg.adapter.Notification;
import org.origin.svg.util.ColorConstants;
import org.origin.svg.widgets.Control;
import org.plutus.lottery.render.control.LinkPart;

public class LinkFigure extends Line {

	protected LinkPart link;

	public LinkFigure(LinkPart linkPart) {
		super(linkPart.getSource().getLocation(), linkPart.getTarget().getLocation());
		this.link = linkPart;

		if (linkPart.isPredicted()) {
			String strokeColor = linkPart.getStrokeColor();
			if (strokeColor == null) {
				strokeColor = ColorConstants.BLUE_LITERAL;
			}
			if (linkPart.getTarget().isPBNumber()) {
				setStrokeColor(strokeColor);
			} else {
				setStrokeColor(strokeColor);
			}
			setStrokeWidth(2);
			setStrokeOpacity(0.4);

		} else {
			if (linkPart.getTarget().isPBNumber()) {
				setStrokeColor(ColorConstants.PURPLE_LITERAL);
			} else {
				setStrokeColor(ColorConstants.GREEN_LITERAL);
			}
			setStrokeWidth(5);
			setStrokeOpacity(0.5);
		}
	}

	public void updateFigure(Control control, Notification notification) {

	}

}

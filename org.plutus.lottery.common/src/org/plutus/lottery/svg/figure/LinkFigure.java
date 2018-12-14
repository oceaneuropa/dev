package org.plutus.lottery.svg.figure;

import org.origin.svg.Line;
import org.origin.svg.adapter.Notification;
import org.origin.svg.util.ColorConstants;
import org.origin.svg.widgets.Control;
import org.plutus.lottery.svg.control.LinkPart;

public class LinkFigure extends Line {

	protected LinkPart link;

	public LinkFigure(LinkPart linkPart) {
		super(linkPart.getSource().getLocation(), linkPart.getTarget().getLocation());
		this.link = linkPart;

		if (linkPart.getTarget().isPBNumber()) {
			setStrokeColor(ColorConstants.PURPLE_LITERAL);
		} else {
			setStrokeColor(ColorConstants.GREEN_LITERAL);
		}
		setStrokeWidth(5);
		setStrokeOpacity(0.5);
	}

	public void updateFigure(Control control, Notification notification) {

	}

}

package org.plutus.lottery.render.render;

import org.origin.svg.Shape;
import org.origin.svg.adapter.Notification;
import org.origin.wwt.widgets.render.FigureHandlerByType;
import org.origin.wwt.widgets.render.FigureHandlerRegistry;
import org.plutus.lottery.render.control.LinkPart;

public class LinkFigureFactory extends FigureHandlerByType<LinkPart> {

	public static LinkFigureFactory INSTANCE = new LinkFigureFactory();

	public static void register() {
		FigureHandlerRegistry.register(INSTANCE);
	}

	public static void unregister() {
		FigureHandlerRegistry.unregister(INSTANCE);
	}

	public LinkFigureFactory() {
		super(LinkPart.class);
	}

	@Override
	public Shape createFigure(LinkPart linkPart) {
		return new LinkFigure(linkPart);
	}

	@Override
	public Shape updateFigure(LinkPart linkPart, Notification notification) {
		return null;
	}

}

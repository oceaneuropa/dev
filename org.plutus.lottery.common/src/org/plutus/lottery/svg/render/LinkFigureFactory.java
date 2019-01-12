package org.plutus.lottery.svg.render;

import org.origin.svg.Shape;
import org.origin.svg.adapter.Notification;
import org.origin.svg.widgets.render.FigureFactoryRegistry;
import org.origin.svg.widgets.render.TypedFigureFactory;
import org.plutus.lottery.svg.control.LinkPart;

public class LinkFigureFactory extends TypedFigureFactory<LinkPart> {

	public static LinkFigureFactory INSTANCE = new LinkFigureFactory();

	public static void register() {
		FigureFactoryRegistry.register(INSTANCE);
	}

	public static void unregister() {
		FigureFactoryRegistry.unregister(INSTANCE);
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

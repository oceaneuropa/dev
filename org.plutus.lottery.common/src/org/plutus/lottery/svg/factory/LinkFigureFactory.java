package org.plutus.lottery.svg.factory;

import org.origin.svg.Shape;
import org.origin.svg.adapter.Notification;
import org.origin.svg.widgets.render.TypedFigureFactory;
import org.origin.svg.widgets.render.TypedFigureFactoryRegistry;
import org.plutus.lottery.svg.control.LinkPart;
import org.plutus.lottery.svg.figure.LinkFigure;

public class LinkFigureFactory extends TypedFigureFactory<LinkPart> {

	public static LinkFigureFactory INSTANCE = new LinkFigureFactory();

	public static void register() {
		TypedFigureFactoryRegistry.register(INSTANCE);
	}

	public static void unregister() {
		TypedFigureFactoryRegistry.unregister(INSTANCE);
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

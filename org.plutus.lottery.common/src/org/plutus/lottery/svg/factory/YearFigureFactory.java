package org.plutus.lottery.svg.factory;

import org.origin.svg.Shape;
import org.origin.svg.adapter.Notification;
import org.origin.svg.widgets.render.TypedFigureFactoryRegistry;
import org.origin.svg.widgets.render.TypedFigureFactory;
import org.plutus.lottery.svg.control.YearPart;
import org.plutus.lottery.svg.figure.YearFigure;

public class YearFigureFactory extends TypedFigureFactory<YearPart> {

	public static YearFigureFactory INSTANCE = new YearFigureFactory();

	public static void register() {
		TypedFigureFactoryRegistry.register(INSTANCE);
	}

	public static void unregister() {
		TypedFigureFactoryRegistry.unregister(INSTANCE);
	}

	public YearFigureFactory() {
		super(YearPart.class);
	}

	@Override
	public Shape createFigure(YearPart yearPart) {
		return new YearFigure(yearPart);
	}

	@Override
	public Shape updateFigure(YearPart yearPart, Notification notification) {
		return null;
	}

}

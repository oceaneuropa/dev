package org.plutus.lottery.render.render;

import org.origin.svg.Shape;
import org.origin.svg.adapter.Notification;
import org.origin.svg.widgets.render.FigureHandlerRegistry;
import org.origin.svg.widgets.render.FigureHandlerByType;
import org.plutus.lottery.render.control.YearPart;

public class YearFigureFactory extends FigureHandlerByType<YearPart> {

	public static YearFigureFactory INSTANCE = new YearFigureFactory();

	public static void register() {
		FigureHandlerRegistry.register(INSTANCE);
	}

	public static void unregister() {
		FigureHandlerRegistry.unregister(INSTANCE);
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

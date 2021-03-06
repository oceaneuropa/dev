package org.plutus.lottery.render.render;

import org.origin.svg.Shape;
import org.origin.svg.adapter.Notification;
import org.origin.wwt.widgets.render.FigureHandlerByType;
import org.origin.wwt.widgets.render.FigureHandlerRegistry;
import org.plutus.lottery.render.control.NumberPart;

public class NumberFigureFactory extends FigureHandlerByType<NumberPart> {

	public static NumberFigureFactory INSTANCE = new NumberFigureFactory();

	public static void register() {
		FigureHandlerRegistry.register(INSTANCE);
	}

	public static void unregister() {
		FigureHandlerRegistry.unregister(INSTANCE);
	}

	public NumberFigureFactory() {
		super(NumberPart.class);
	}

	@Override
	public Shape createFigure(NumberPart numberPart) {
		return new NumberFigure(numberPart);
	}

	@Override
	public Shape updateFigure(NumberPart numberPart, Notification notification) {
		return null;
	}

}

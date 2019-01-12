package org.plutus.lottery.svg.render;

import org.origin.svg.Shape;
import org.origin.svg.adapter.Notification;
import org.origin.svg.widgets.render.FigureFactoryRegistry;
import org.origin.svg.widgets.render.TypedFigureFactory;
import org.plutus.lottery.svg.control.NumberPart;

public class NumberFigureFactory extends TypedFigureFactory<NumberPart> {

	public static NumberFigureFactory INSTANCE = new NumberFigureFactory();

	public static void register() {
		FigureFactoryRegistry.register(INSTANCE);
	}

	public static void unregister() {
		FigureFactoryRegistry.unregister(INSTANCE);
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

package org.plutus.lottery.svg.factory;

import org.origin.svg.Shape;
import org.origin.svg.adapter.Notification;
import org.origin.svg.widgets.render.TypedFigureFactoryRegistry;
import org.origin.svg.widgets.render.TypedFigureFactory;
import org.plutus.lottery.svg.control.NumberPart;
import org.plutus.lottery.svg.figure.NumberFigure;

public class NumberFigureFactory extends TypedFigureFactory<NumberPart> {

	public static NumberFigureFactory INSTANCE = new NumberFigureFactory();

	public static void register() {
		TypedFigureFactoryRegistry.register(INSTANCE);
	}

	public static void unregister() {
		TypedFigureFactoryRegistry.unregister(INSTANCE);
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

package org.plutus.lottery.svg.factory;

import org.origin.svg.Shape;
import org.origin.svg.adapter.Notification;
import org.origin.svg.widgets.render.TypedFigureFactory;
import org.origin.svg.widgets.render.TypedFigureFactoryRegistry;
import org.plutus.lottery.svg.PB;
import org.plutus.lottery.svg.control.DrawPart;
import org.plutus.lottery.svg.figure.DrawFigure_Square01x69;
import org.plutus.lottery.svg.figure.DrawFigure_Square10x07;
import org.plutus.lottery.svg.figure.DrawFigure_Square14x05;

public class DrawFigureFactory extends TypedFigureFactory<DrawPart> {

	public static DrawFigureFactory INSTANCE = new DrawFigureFactory();

	public static void register() {
		TypedFigureFactoryRegistry.register(INSTANCE);
	}

	public static void unregister() {
		TypedFigureFactoryRegistry.unregister(INSTANCE);
	}

	public DrawFigureFactory() {
		super(DrawPart.class);
	}

	@Override
	public Shape createFigure(DrawPart drawPart) {
		Shape figure = null;
		if (drawPart.hasStyle(PB.DRAW_ROUND)) {

		} else if (drawPart.hasStyle(PB.DRAW_SQUARE_01x69)) {
			figure = new DrawFigure_Square01x69(drawPart);

		} else if (drawPart.hasStyle(PB.DRAW_SQUARE_10x07)) {
			figure = new DrawFigure_Square10x07(drawPart);

		} else if (drawPart.hasStyle(PB.DRAW_SQUARE_14x05)) {
			figure = new DrawFigure_Square14x05(drawPart);

		} else {
			figure = new DrawFigure_Square14x05(drawPart);
		}
		return figure;
	}

	@Override
	public Shape updateFigure(DrawPart drawPart, Notification notification) {
		return null;
	}

}

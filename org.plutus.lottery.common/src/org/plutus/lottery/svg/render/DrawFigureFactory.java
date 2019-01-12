package org.plutus.lottery.svg.render;

import org.origin.svg.Shape;
import org.origin.svg.adapter.Notification;
import org.origin.svg.widgets.render.FigureFactoryRegistry;
import org.origin.svg.widgets.render.TypedFigureFactory;
import org.plutus.lottery.svg.PBConstants;
import org.plutus.lottery.svg.control.DrawPart;

public class DrawFigureFactory extends TypedFigureFactory<DrawPart> {

	public static DrawFigureFactory INSTANCE = new DrawFigureFactory();

	public static void register() {
		FigureFactoryRegistry.register(INSTANCE);
	}

	public static void unregister() {
		FigureFactoryRegistry.unregister(INSTANCE);
	}

	public DrawFigureFactory() {
		super(DrawPart.class);
	}

	@Override
	public Shape createFigure(DrawPart drawPart) {
		Shape figure = null;
		if (drawPart.hasStyle(PBConstants.DRAW_ROUND)) {

		} else if (drawPart.hasStyle(PBConstants.DRAW_SQUARE_01x69)) {
			figure = new DrawFigure_Square_01x69(drawPart);

		} else if (drawPart.hasStyle(PBConstants.DRAW_SQUARE_10x07)) {
			figure = new DrawFigure_Square_10x07(drawPart);

		} else if (drawPart.hasStyle(PBConstants.DRAW_SQUARE_14x05)) {
			figure = new DrawFigure_Square_14x05(drawPart);

		} else {
			figure = new DrawFigure_Square_14x05(drawPart);
		}
		return figure;
	}

	@Override
	public Shape updateFigure(DrawPart drawPart, Notification notification) {
		return null;
	}

}

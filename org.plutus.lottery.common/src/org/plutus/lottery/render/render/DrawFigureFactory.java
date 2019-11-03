package org.plutus.lottery.render.render;

import org.origin.svg.Shape;
import org.origin.svg.adapter.Notification;
import org.origin.wwt.widgets.render.FigureHandlerByType;
import org.origin.wwt.widgets.render.FigureHandlerRegistry;
import org.plutus.lottery.powerball.PBConstants;
import org.plutus.lottery.render.control.DrawPart;

public class DrawFigureFactory extends FigureHandlerByType<DrawPart> {

	public static DrawFigureFactory INSTANCE = new DrawFigureFactory();

	public static void register() {
		FigureHandlerRegistry.register(INSTANCE);
	}

	public static void unregister() {
		FigureHandlerRegistry.unregister(INSTANCE);
	}

	public DrawFigureFactory() {
		super(DrawPart.class);
	}

	@Override
	public Shape createFigure(DrawPart drawPart) {
		Shape figure = null;
		if (drawPart.hasStyle(PBConstants.DRAW_ROUND)) {

		} else if (drawPart.hasStyle(PBConstants.DRAW_SQUARE_01_PER_ROW)) {
			figure = new DrawFigure_Square_01x69(drawPart);

		} else if (drawPart.hasStyle(PBConstants.DRAW_SQUARE_10_PER_ROW)) {
			figure = new DrawFigure_Square_10x07(drawPart);

		} else if (drawPart.hasStyle(PBConstants.DRAW_SQUARE_14_PER_ROW)) {
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

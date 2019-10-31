package org.plutus.lottery.render.render;

import org.origin.svg.Shape;
import org.origin.svg.adapter.Notification;
import org.origin.svg.widgets.render.FigureHandlerRegistry;
import org.origin.svg.widgets.render.FigureHandlerByType;
import org.plutus.lottery.powerball.PBConstants;
import org.plutus.lottery.render.control.PBPart;

public class PBFigureFactory extends FigureHandlerByType<PBPart> {

	public static PBFigureFactory INSTANCE = new PBFigureFactory();

	public static void register() {
		FigureHandlerRegistry.register(INSTANCE);
	}

	public static void unregister() {
		FigureHandlerRegistry.unregister(INSTANCE);
	}

	public PBFigureFactory() {
		super(PBPart.class);
	}

	@Override
	public Shape createFigure(PBPart pbPart) {
		Shape figure = null;
		if (pbPart.hasStyle(PBConstants.PB_ROUND)) {

		} else if (pbPart.hasStyle(PBConstants.PB_SQUARE_01_PER_ROW)) {
			figure = new PBFigure_Square_01x26(pbPart);

		} else {
			figure = new PBFigure_Square_01x26(pbPart);
		}
		return figure;
	}

	@Override
	public Shape updateFigure(PBPart pbPart, Notification notification) {
		return null;
	}

}

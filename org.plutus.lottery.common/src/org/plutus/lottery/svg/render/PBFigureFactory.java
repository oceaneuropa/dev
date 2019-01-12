package org.plutus.lottery.svg.render;

import org.origin.svg.Shape;
import org.origin.svg.adapter.Notification;
import org.origin.svg.widgets.render.FigureFactoryRegistry;
import org.origin.svg.widgets.render.TypedFigureFactory;
import org.plutus.lottery.svg.PBConstants;
import org.plutus.lottery.svg.control.PBPart;

public class PBFigureFactory extends TypedFigureFactory<PBPart> {

	public static PBFigureFactory INSTANCE = new PBFigureFactory();

	public static void register() {
		FigureFactoryRegistry.register(INSTANCE);
	}

	public static void unregister() {
		FigureFactoryRegistry.unregister(INSTANCE);
	}

	public PBFigureFactory() {
		super(PBPart.class);
	}

	@Override
	public Shape createFigure(PBPart pbPart) {
		Shape figure = null;
		if (pbPart.hasStyle(PBConstants.PB_ROUND)) {

		} else if (pbPart.hasStyle(PBConstants.PB_SQUARE_01x26)) {
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

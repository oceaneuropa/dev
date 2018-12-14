package org.plutus.lottery.svg.factory;

import org.origin.svg.Shape;
import org.origin.svg.adapter.Notification;
import org.origin.svg.widgets.render.TypedFigureFactory;
import org.origin.svg.widgets.render.TypedFigureFactoryRegistry;
import org.plutus.lottery.svg.PBConstants;
import org.plutus.lottery.svg.control.PBPart;
import org.plutus.lottery.svg.figure.PBFigure_Square_01x26;

public class PBFigureFactory extends TypedFigureFactory<PBPart> {

	public static PBFigureFactory INSTANCE = new PBFigureFactory();

	public static void register() {
		TypedFigureFactoryRegistry.register(INSTANCE);
	}

	public static void unregister() {
		TypedFigureFactoryRegistry.unregister(INSTANCE);
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

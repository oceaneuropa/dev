package org.plutus.lottery.svg;

import org.origin.svg.Shape;
import org.origin.svg.adapter.Notification;
import org.origin.svg.widgets.Control;
import org.origin.svg.widgets.render.ExtensionFigureFactoryRegistry;
import org.origin.svg.widgets.render.FigureFactory;

public class BoardFigureFactory implements FigureFactory {

	public static BoardFigureFactory INSTANCE = new BoardFigureFactory();

	public static void register() {
		ExtensionFigureFactoryRegistry.register(Board.class, INSTANCE);
	}

	public static void unregister() {
		ExtensionFigureFactoryRegistry.unregister(Board.class);
	}

	@Override
	public String getName() {
		return Board.class.getName() + "FigureFactory";
	}

	@Override
	public Shape createFigure(Control control) {
		if (!(control instanceof Board)) {
			return null;
		}
		return new BoardFigure((Board) control);
	}

	@Override
	public Shape updateFigure(Control control, Notification notification) {
		return null;
	}

}

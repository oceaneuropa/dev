package org.plutus.lottery.svg;

import org.origin.svg.Shape;
import org.origin.svg.adapter.Notification;
import org.origin.svg.widgets.Control;
import org.origin.svg.widgets.render.ExtensionFigureFactoryRegistry;
import org.origin.svg.widgets.render.FigureFactory;

public class NumberFigureFactory implements FigureFactory {

	public static NumberFigureFactory INSTANCE = new NumberFigureFactory();

	public static void register() {
		ExtensionFigureFactoryRegistry.register(Number.class, INSTANCE);
	}

	public static void unregister() {
		ExtensionFigureFactoryRegistry.unregister(Number.class);
	}

	@Override
	public String getName() {
		return Number.class.getName() + "FigureFactory";
	}

	@Override
	public Shape createFigure(Control control) {
		if (!(control instanceof Number)) {
			return null;
		}
		return new NumberFigure((Number) control);
	}

	@Override
	public Shape updateFigure(Control control, Notification notification) {
		return null;
	}

}

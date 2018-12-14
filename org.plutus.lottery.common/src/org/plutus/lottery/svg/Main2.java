package org.plutus.lottery.svg;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.origin.common.io.IOUtil;
import org.origin.common.util.SystemUtils;
import org.origin.svg.Shape;
import org.origin.svg.graphics.Rectangle;
import org.origin.svg.graphics.Size;
import org.origin.svg.util.SVGStringWriter;
import org.origin.svg.widgets.Display;
import org.origin.svg.widgets.render.impl.WidgetFigureFactory;
import org.plutus.lottery.powerball.Draw;
import org.plutus.lottery.powerball.DrawHelper;
import org.plutus.lottery.powerball.DrawReaderV2;
import org.plutus.lottery.svg.control.DrawPart;
import org.plutus.lottery.svg.control.YearPart;
import org.plutus.lottery.svg.factory.DrawFigureFactory;
import org.plutus.lottery.svg.factory.LinkFigureFactory;
import org.plutus.lottery.svg.factory.NumberFigureFactory;
import org.plutus.lottery.svg.factory.YearFigureFactory;

public class Main2 {

	public static void main(String[] args) {
		FileOutputStream output = null;
		try {
			DrawFigureFactory.register();
			NumberFigureFactory.register();
			LinkFigureFactory.register();
			YearFigureFactory.register();

			File inputFile = new File(SystemUtils.getUserDir(), "/doc/data/DownloadAllNumbers.txt");

			List<Draw> allDraws = DrawReaderV2.read(inputFile);
			Map<Integer, List<Draw>> yearToDraws = DrawHelper.INSTANCE.groupByYear(allDraws);

			// int total_h = (allDraws.size() / 8) * (50 + 10 + 10) + 100;
			int total_w = 1300;
			int total_h = yearToDraws.size() * 800;
			// for (Iterator<Integer> yearItor = yearToDraws.keySet().iterator(); yearItor.hasNext();) {
			// Integer year = yearItor.next();
			// List<Draw> draws = yearToDraws.get(year);
			// total_h += 1000;
			// }

			Size size = new Size(total_w, total_h);
			Display display = new Display(size);

			int year_x = 0 + 10; // 10 is for top shift
			int year_y = 0 + 10; // 10 is for left shift
			int year_w = 1200;
			int year_h = 800;

			for (Iterator<Integer> yearItor = yearToDraws.keySet().iterator(); yearItor.hasNext();) {
				Integer year = yearItor.next();
				List<Draw> draws = yearToDraws.get(year);
				YearPart yearPart = new YearPart(display, year, draws);

				Rectangle bounds = new Rectangle(year_x, year_y, year_w, year_h);
				yearPart.setBounds(bounds);
				yearPart.createContents();

				year_y += year_h;
			}

			Shape rootShape = WidgetFigureFactory.getInstance().createFigure(display);

			File outputFile = new File(SystemUtils.getUserDir(), "/doc/svg/powerball_draws_all.svg");
			output = new FileOutputStream(outputFile);

			SVGStringWriter writer = new SVGStringWriter(rootShape);
			writer.write(output);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			IOUtil.closeQuietly(output, true);
		}
	}

	/**
	 * 
	 * @param draws
	 * @param output
	 * @throws IOException
	 */
	protected static void generate(List<Draw> draws, FileOutputStream output) throws IOException {
		int draw_x = 0 + 10; // 10 is for top shift
		int draw_y = 0 + 10; // 10 is for left shift
		int draw_w = 140;
		int draw_h = 50;

		int total_h = (draws.size() / 8) * (50 + 10) + 100;
		if (total_h < 1000) {
			total_h = 1000;
		}

		Size size = new Size(1300, total_h);
		Display display = new Display(size);

		int count = 0;
		List<DrawPart> drawParts = new ArrayList<DrawPart>();
		for (Draw draw : draws) {
			Rectangle currBounds = new Rectangle(draw_x, draw_y, draw_w, draw_h);

			DrawPart drawPart = new DrawPart(display, draw, PBConstants.DRAW_SQUARE_14x05);
			drawPart.setBounds(currBounds);
			drawPart.createContents();
			drawParts.add(drawPart);

			draw_x += draw_w + 10;
			if (draw_x + draw_w > size.x) {
				draw_x = 0 + 10; // 10 is for top shift
				draw_y += draw_h + 10 + 4; // 4 is for distance between rows
			}

			count++;
			if (count >= 1) {
				// break;
			}
		}

		Shape rootShape = WidgetFigureFactory.getInstance().createFigure(display);

		SVGStringWriter writer = new SVGStringWriter(rootShape);
		// String xmlStr = writer.toXml();
		// System.out.println(xmlStr);
		writer.write(output);
	}

}

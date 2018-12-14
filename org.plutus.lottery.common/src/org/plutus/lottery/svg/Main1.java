package org.plutus.lottery.svg;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.origin.common.io.IOUtil;
import org.origin.common.util.SystemUtils;
import org.origin.svg.Shape;
import org.origin.svg.graphics.Point;
import org.origin.svg.graphics.Rectangle;
import org.origin.svg.graphics.Size;
import org.origin.svg.util.SVGStringWriter;
import org.origin.svg.widgets.Display;
import org.origin.svg.widgets.render.impl.WidgetFigureFactory;
import org.plutus.lottery.powerball.Draw;
import org.plutus.lottery.powerball.DrawHelper;
import org.plutus.lottery.powerball.DrawReaderV2;
import org.plutus.lottery.svg.control.DrawPart;
import org.plutus.lottery.svg.control.LinkPart;
import org.plutus.lottery.svg.control.NumberPart;
import org.plutus.lottery.svg.factory.DrawFigureFactory;
import org.plutus.lottery.svg.factory.LinkFigureFactory;
import org.plutus.lottery.svg.factory.NumberFigureFactory;

public class Main1 {

	static {
		DrawFigureFactory.register();
		NumberFigureFactory.register();
		LinkFigureFactory.register();
	}

	public static void main(String[] args) {
		try {
			Map<Integer, List<Draw>> yearToDraws = getDrawsByYear();
			generate(yearToDraws, SystemUtils.getUserDir(), "/doc/svg/powerball_draws__square_01x69__{0}.svg", PB.DRAW_SQUARE_01x69);
			generate(yearToDraws, SystemUtils.getUserDir(), "/doc/svg/powerball_draws__square_10x07__{0}.svg", PB.DRAW_SQUARE_10x07);
			generate(yearToDraws, SystemUtils.getUserDir(), "/doc/svg/powerball_draws__square_14x05__{0}.svg", PB.DRAW_SQUARE_14x05);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param yearToDraws
	 * @param baseFolder
	 * @param outputFileLocation
	 * @param drawStyle
	 */
	public static void generate(Map<Integer, List<Draw>> yearToDraws, File baseFolder, String outputFileLocation, int drawStyle) {
		for (Iterator<Integer> yearItor = yearToDraws.keySet().iterator(); yearItor.hasNext();) {
			Integer year = yearItor.next();
			List<Draw> draws = yearToDraws.get(year);

			FileOutputStream output = null;
			try {
				String fileLocation = MessageFormat.format(outputFileLocation, new Object[] { String.valueOf(year) });

				Display display = null;
				if (drawStyle == PB.DRAW_SQUARE_01x69) {
					display = generate_01x69(draws);

				} else if (drawStyle == PB.DRAW_SQUARE_10x07) {
					display = generate_10x07(draws);

				} else if (drawStyle == PB.DRAW_SQUARE_14x05) {
					display = generate_14x05(draws);
				}

				if (display != null) {
					Shape rootShape = WidgetFigureFactory.getInstance().createFigure(display);
					SVGStringWriter writer = new SVGStringWriter(rootShape);

					File outputFile = new File(baseFolder, fileLocation);
					output = new FileOutputStream(outputFile);
					writer.write(output);
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				IOUtil.closeQuietly(output, true);
			}
		}
	}

	/**
	 * 
	 * @return
	 * @throws IOException
	 */
	protected static Map<Integer, List<Draw>> getDrawsByYear() throws IOException {
		File inputFile = new File(SystemUtils.getUserDir(), "/doc/data/DownloadAllNumbers.txt");
		List<Draw> allDraws = DrawReaderV2.read(inputFile);
		Map<Integer, List<Draw>> drawsByYear = DrawHelper.INSTANCE.groupByYear(allDraws);
		return drawsByYear;
	}

	/**
	 * 
	 * @param draws
	 * @return
	 */
	protected static Display generate_01x69(List<Draw> draws) {
		// 10 and 10 are for the overall shift for all draw figures
		int draw_x = 0 + 10;
		int draw_y = 0 + 10;
		int draw_w = 10;
		int draw_h = 690;
		int space_w = 100;

		int draw_width = draw_w + space_w; // total draw width (one number column and empt space)
		int display_width = draws.size() * draw_width;
		Size size = new Size(display_width, draw_h + 100);
		Display display = new Display(size);

		List<DrawPart> drawParts = new ArrayList<DrawPart>();
		for (int i = 0; i < draws.size(); i++) {
			Draw draw = draws.get(i);

			Rectangle drawBounds = new Rectangle(draw_x, draw_y, draw_w, draw_h);

			DrawPart drawPart = new DrawPart(display, draw, PB.DRAW_SQUARE_01x69);
			drawPart.setBounds(drawBounds);
			drawPart.createContents();
			drawParts.add(drawPart);

			draw_x += draw_width;
		}

		boolean showLinks = true;
		if (showLinks) {
			DrawPart prevDrawPart = null;
			for (DrawPart currDrawPart : drawParts) {
				int curr_draw_x = currDrawPart.getBounds().getX();

				NumberPart numPartCurr1 = currDrawPart.getMatchedNumbers().get(0);
				NumberPart numPartCurr2 = currDrawPart.getMatchedNumbers().get(1);
				NumberPart numPartCurr3 = currDrawPart.getMatchedNumbers().get(2);
				NumberPart numPartCurr4 = currDrawPart.getMatchedNumbers().get(3);
				NumberPart numPartCurr5 = currDrawPart.getMatchedNumbers().get(4);

				numPartCurr1.setIndex(new Point(0, numPartCurr1.getNumber() - 1));
				numPartCurr2.setIndex(new Point(0, numPartCurr2.getNumber() - 1));
				numPartCurr3.setIndex(new Point(0, numPartCurr3.getNumber() - 1));
				numPartCurr4.setIndex(new Point(0, numPartCurr4.getNumber() - 1));
				numPartCurr5.setIndex(new Point(0, numPartCurr5.getNumber() - 1));

				int shiftX = 5;
				int shiftY = 5;
				numPartCurr1.setLocation(new Point(curr_draw_x + shiftX, numPartCurr1.getNumber() * 10 + shiftY));
				numPartCurr2.setLocation(new Point(curr_draw_x + shiftX, numPartCurr2.getNumber() * 10 + shiftY));
				numPartCurr3.setLocation(new Point(curr_draw_x + shiftX, numPartCurr3.getNumber() * 10 + shiftY));
				numPartCurr4.setLocation(new Point(curr_draw_x + shiftX, numPartCurr4.getNumber() * 10 + shiftY));
				numPartCurr5.setLocation(new Point(curr_draw_x + shiftX, numPartCurr5.getNumber() * 10 + shiftY));

				if (prevDrawPart != null) {
					NumberPart numPartPrev1 = prevDrawPart.getMatchedNumbers().get(0);
					NumberPart numPartPrev2 = prevDrawPart.getMatchedNumbers().get(1);
					NumberPart numPartPrev3 = prevDrawPart.getMatchedNumbers().get(2);
					NumberPart numPartPrev4 = prevDrawPart.getMatchedNumbers().get(3);
					NumberPart numPartPrev5 = prevDrawPart.getMatchedNumbers().get(4);

					LinkPart link1 = new LinkPart(display, numPartPrev1, numPartCurr1);
					link1.createContents();
					LinkPart link2 = new LinkPart(display, numPartPrev2, numPartCurr2);
					link2.createContents();
					LinkPart link3 = new LinkPart(display, numPartPrev3, numPartCurr3);
					link3.createContents();
					LinkPart link4 = new LinkPart(display, numPartPrev4, numPartCurr4);
					link4.createContents();
					LinkPart link5 = new LinkPart(display, numPartPrev5, numPartCurr5);
					link5.createContents();
				}

				prevDrawPart = currDrawPart;
			}
		}

		return display;
	}

	/**
	 * 
	 * @param draws
	 * @return
	 */
	protected static Display generate_10x07(List<Draw> draws) {
		int draw_x = 0 + 10; // 10 is for top shift
		int draw_y = 0 + 10; // 10 is for left shift
		int draw_w = 100;
		int draw_h = 70;

		int total_h = (draws.size() / 10) * (70 + 15) + 100;

		Size size = new Size(1400, total_h);
		Display display = new Display(size);

		List<DrawPart> drawParts = new ArrayList<DrawPart>();
		for (Draw draw : draws) {
			Rectangle drawBounds = new Rectangle(draw_x, draw_y, draw_w, draw_h);

			DrawPart drawPart = new DrawPart(display, draw, PB.DRAW_SQUARE_10x07);
			drawPart.setShowLinks(true);
			drawPart.setBounds(drawBounds);
			drawPart.createContents();
			drawParts.add(drawPart);

			draw_x += draw_w + 10;
			if (draw_x + draw_w > size.x) {
				draw_x = 0 + 10; // 10 is for top shift
				draw_y += draw_h + 10 + 4; // 4 is for distance between rows
			}
		}

		return display;
	}

	/**
	 * 
	 * @param draws
	 * @return
	 */
	protected static Display generate_14x05(List<Draw> draws) {
		int draw_x = 0 + 10; // 10 is for top shift
		int draw_y = 0 + 10; // 10 is for left shift
		int draw_w = 140;
		int draw_h = 50;

		int total_h = (draws.size() / 9) * (50 + 15) + 100;

		Size size = new Size(1400, total_h);
		Display display = new Display(size);

		List<DrawPart> drawParts = new ArrayList<DrawPart>();
		for (Draw draw : draws) {
			Rectangle drawBounds = new Rectangle(draw_x, draw_y, draw_w, draw_h);

			DrawPart drawPart = new DrawPart(display, draw, PB.DRAW_SQUARE_14x05);
			drawPart.setShowLinks(true);
			drawPart.setBounds(drawBounds);
			drawPart.createContents();
			drawParts.add(drawPart);

			draw_x += draw_w + 10;
			if (draw_x + draw_w > size.x) {
				draw_x = 0 + 10; // 10 is for top shift
				draw_y += draw_h + 10 + 4; // 4 is for distance between rows
			}
		}

		return display;
	}

}

// String xmlStr = writer.toXml();
// System.out.println(xmlStr);

// int count = 0;
// count++;
// if (count >= 1) {
// // break;
// }

// outputFile = new File(baseFolder, "/doc/svg/powerball_draws__square_10x7__" + year + ".svg");
// outputFile = new File(baseFolder, "/doc/svg/powerball_draws__square_14x5__" + year + ".svg");

// if (total_h < 1000) {
// total_h = 1000;
// }

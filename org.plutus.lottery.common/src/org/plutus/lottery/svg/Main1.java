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
import org.plutus.lottery.powerball.impl.DrawReaderV2;
import org.plutus.lottery.svg.control.DrawPart;
import org.plutus.lottery.svg.control.LinkPart;
import org.plutus.lottery.svg.control.NumberPart;
import org.plutus.lottery.svg.control.PBPart;
import org.plutus.lottery.svg.factory.DrawFigureFactory;
import org.plutus.lottery.svg.factory.LinkFigureFactory;
import org.plutus.lottery.svg.factory.NumberFigureFactory;
import org.plutus.lottery.svg.factory.PBFigureFactory;

public class Main1 {

	static {
		DrawFigureFactory.register();
		PBFigureFactory.register();
		NumberFigureFactory.register();
		LinkFigureFactory.register();
	}

	public static void main(String[] args) {
		try {
			Map<Integer, List<Draw>> yearToDraws = getDrawsByYear();
			generate(yearToDraws, SystemUtils.getUserDir(), "/doc/svg/powerball_draws__square_01x69__{0}.svg", PBConstants.DRAW_SQUARE_01x69);
			generate(yearToDraws, SystemUtils.getUserDir(), "/doc/svg/powerball_draws__square_10x07__{0}.svg", PBConstants.DRAW_SQUARE_10x07);
			generate(yearToDraws, SystemUtils.getUserDir(), "/doc/svg/powerball_draws__square_14x05__{0}.svg", PBConstants.DRAW_SQUARE_14x05);
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

			try {
				String fileLocation = MessageFormat.format(outputFileLocation, new Object[] { String.valueOf(year) });

				Display display = null;
				if (drawStyle == PBConstants.DRAW_SQUARE_01x69) {
					display = generate_01x69(draws, true, true);

				} else if (drawStyle == PBConstants.DRAW_SQUARE_10x07) {
					display = generate_10x07(draws);

				} else if (drawStyle == PBConstants.DRAW_SQUARE_14x05) {
					display = generate_14x05(draws);
				}

				if (display != null) {
					File outputFile = new File(baseFolder, fileLocation);
					save(display, outputFile);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static Map<Integer, List<Draw>> getDrawsByYear() throws IOException {
		List<Draw> draws = DrawHelper.INSTANCE.read(DrawReaderV2.INSTANCE, new File(SystemUtils.getUserDir(), "/doc/data/DownloadAllNumbers.txt"));
		Map<Integer, List<Draw>> drawsByYear = DrawHelper.INSTANCE.groupByYear(draws);
		return drawsByYear;
	}

	public static void save(Display display, File file) throws IOException {
		FileOutputStream output = null;
		try {
			Shape rootShape = WidgetFigureFactory.getInstance().createFigure(display);
			SVGStringWriter writer = new SVGStringWriter(rootShape);

			output = new FileOutputStream(file);
			writer.write(output);
		} finally {
			IOUtil.closeQuietly(output, true);
		}
	}

	/**
	 * 
	 * @param draws
	 * @param showLinks
	 * @param showPB
	 * @return
	 */
	protected static Display generate_01x69(List<Draw> draws, boolean showLinks, boolean showPB) {
		// 10 and 10 are for the overall shift for all figures
		int draw_x = 0 + 10;
		int draw_y = 0 + 10;
		int draw_w = 10;
		int draw_h = 690;
		int space_w = 100;
		int draw_width = draw_w + space_w; // total draw width (one number column and empty space)

		int pb_x = draw_x;
		int pb_y = draw_y + draw_h + 20 + 10;
		int pb_w = 10;
		int pb_h = 260;
		int pb_width = draw_width;

		int display_width = draws.size() * draw_width + 100;
		Size size = new Size(display_width, draw_h + pb_h + 100);
		Display display = new Display(size);

		List<DrawPart> drawParts = new ArrayList<DrawPart>();
		for (int i = 0; i < draws.size(); i++) {
			Draw draw = draws.get(i);
			Rectangle drawBounds = new Rectangle(draw_x, draw_y, draw_w, draw_h);

			DrawPart drawPart = new DrawPart(display, draw, PBConstants.DRAW_SQUARE_01x69);
			drawPart.setBounds(drawBounds);
			drawPart.createContents();
			drawParts.add(drawPart);

			draw_x += draw_width;
		}

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

		if (showPB) {
			List<PBPart> pbParts = new ArrayList<PBPart>();
			for (int i = 0; i < draws.size(); i++) {
				Draw draw = draws.get(i);
				Rectangle pbBounds = new Rectangle(pb_x, pb_y, pb_w, pb_h);

				PBPart pbPart = new PBPart(display, draw, PBConstants.PB_SQUARE_01x26);
				pbPart.setBounds(pbBounds);
				pbPart.createContents();
				pbParts.add(pbPart);

				pb_x += pb_width;
			}

			if (showLinks) {
				PBPart prevPBPart = null;
				for (PBPart currPBPart : pbParts) {
					int curr_draw_x = currPBPart.getBounds().getX();

					NumberPart currNumPart = currPBPart.getMatchedNumber();
					if (currNumPart == null) {
						continue;
					}

					currNumPart.setIndex(new Point(0, currNumPart.getNumber() - 1));

					int shiftX = 5;
					int shiftY = -4;
					currNumPart.setLocation(new Point(curr_draw_x + shiftX, pb_y + currNumPart.getNumber() * 10 + shiftY));

					if (prevPBPart != null) {
						NumberPart prevNumPart = prevPBPart.getMatchedNumber();

						LinkPart link = new LinkPart(display, prevNumPart, currNumPart);
						link.createContents();
					}

					prevPBPart = currPBPart;
				}
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

			DrawPart drawPart = new DrawPart(display, draw, PBConstants.DRAW_SQUARE_10x07);
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

			DrawPart drawPart = new DrawPart(display, draw, PBConstants.DRAW_SQUARE_14x05);
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

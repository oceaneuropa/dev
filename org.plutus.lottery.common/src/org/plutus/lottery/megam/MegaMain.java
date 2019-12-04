package org.plutus.lottery.megam;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.origin.common.io.IOUtil;
import org.origin.common.util.DateUtil;
import org.origin.common.util.SystemUtils;
import org.origin.svg.Shape;
import org.origin.svg.graphics.Point;
import org.origin.svg.graphics.Rectangle;
import org.origin.svg.graphics.Size;
import org.origin.svg.util.ColorConstants;
import org.origin.svg.util.SVGStringWriter;
import org.origin.wwt.render.widget.util.WidgetFigureHandler;
import org.origin.wwt.widgets.Display;
import org.origin.wwt.widgets.render.FigureHandlerRegistry;
import org.plutus.lottery.common.Draw;
import org.plutus.lottery.powerball.PBConstants;
import org.plutus.lottery.render.control.DrawPart;
import org.plutus.lottery.render.control.LinkPart;
import org.plutus.lottery.render.control.NumberPart;
import org.plutus.lottery.render.control.PBPart;
import org.plutus.lottery.render.render.DrawFigureFactory;
import org.plutus.lottery.render.render.LinkFigureFactory;
import org.plutus.lottery.render.render.NumberFigureFactory;
import org.plutus.lottery.render.render.PBFigureFactory;
import org.plutus.lottery.util.DrawHelper;
import org.plutus.lottery.util.DrawReaderV2;

public class MegaMain {

	protected static Map<Integer, String> indexToPredictedLinkStrokeColor = new HashMap<Integer, String>();

	static {
		FigureHandlerRegistry.register(new WidgetFigureHandler());
		DrawFigureFactory.register();
		PBFigureFactory.register();
		NumberFigureFactory.register();
		LinkFigureFactory.register();

		indexToPredictedLinkStrokeColor.put(0, ColorConstants.RED_LITERAL);
		indexToPredictedLinkStrokeColor.put(1, ColorConstants.DARK_ORANGE_LITERAL);
		indexToPredictedLinkStrokeColor.put(2, ColorConstants.BLUE_LITERAL);
		indexToPredictedLinkStrokeColor.put(3, ColorConstants.ORANGE_LITERAL);
	}

	public static void main(String[] args) {
		try {
			Map<Integer, List<Draw>> yearToDraws = getDrawsByYear();
			Map<Integer, List<Draw>> idToPredictedNumbers = getPredictedDrawsById();

			generate(yearToDraws, idToPredictedNumbers, SystemUtils.getUserDir(), "/doc/svg/megamillions_draws__square_01x69__{0}.svg", PBConstants.DRAW_SQUARE_01_PER_ROW);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param yearToDraws
	 * @param idToPredictedNumbers
	 * @param baseFolder
	 * @param outputFileLocation
	 * @param drawStyle
	 */
	public static void generate(Map<Integer, List<Draw>> yearToDraws, Map<Integer, List<Draw>> idToPredictedNumbers, File baseFolder, String outputFileLocation, int drawStyle) {
		int thisYear = DateUtil.getYear(new Date());
		for (Iterator<Integer> yearItor = yearToDraws.keySet().iterator(); yearItor.hasNext();) {
			Integer year = yearItor.next();
			List<Draw> draws = yearToDraws.get(year);

			boolean showDummyDraw = (thisYear == year) ? true : false;
			try {
				String fileLocation = MessageFormat.format(outputFileLocation, new Object[] { String.valueOf(year) });

				Display display = null;
				if (drawStyle == PBConstants.DRAW_SQUARE_01_PER_ROW) {
					display = generate_01_per_row(draws, idToPredictedNumbers, true, true, showDummyDraw);

				} else if (drawStyle == PBConstants.DRAW_SQUARE_10_PER_ROW) {
					display = generate_10_per_row(draws, idToPredictedNumbers);

				} else if (drawStyle == PBConstants.DRAW_SQUARE_14_PER_ROW) {
					display = generate_14_per_row(draws, idToPredictedNumbers);
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
		List<Draw> draws = DrawHelper.INSTANCE.read(DrawReaderV2.INSTANCE, new File(SystemUtils.getUserDir(), "/doc/megam/DownloadAllNumbers.txt"));
		Map<Integer, List<Draw>> drawsByYear = DrawHelper.INSTANCE.groupByYear(draws, 10);
		return drawsByYear;
	}

	public static Map<Integer, List<Draw>> getPredictedDrawsById() throws IOException {
		List<Draw> draws = DrawHelper.INSTANCE.read(DrawReaderV2.INSTANCE, new File(SystemUtils.getUserDir(), "/doc/megam/PredictedNumbers.txt"));
		Map<Integer, List<Draw>> drawsById = DrawHelper.INSTANCE.groupByDrawId(draws);
		return drawsById;
	}

	public static void save(Display display, File file) throws IOException {
		FileOutputStream output = null;
		try {
			Shape rootShape = FigureHandlerRegistry.getFigureHandler(display).createFigure(display);
			// Shape rootShape = WidgetFigureFactory.getInstance().createFigure(display);
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
	 * @param idToPredictedNumbers
	 * @param showLinks
	 * @param showMB
	 * @param showDummyDraw
	 * @return
	 */
	protected static Display generate_01_per_row(List<Draw> draws, Map<Integer, List<Draw>> idToPredictedNumbers, boolean showLinks, boolean showMB, boolean showDummyDraw) {
		// Draw lastDraw = new Draw();
		// lastDraw.setDummy(true);
		// draws.add(lastDraw);

		// 10 and 10 are for the overall shift for all figures
		int draw_x = 0 + 10;
		int draw_y = 0 + 10;
		int draw_w = 10;
		int draw_h = 700;
		int space_w = 100;
		int draw_width = draw_w + space_w; // total draw width (one number column and empty space)

		int pb_x = draw_x;
		int pb_y = draw_y + draw_h + 20 + 10;
		int pb_w = 10;
		int pb_h = 250;
		int pb_width = draw_width;

		int display_width = (draws.size() + 1) * draw_width + 100;
		Size size = new Size(display_width, draw_h + pb_h + 100);
		Display display = new Display(null, "display", size.getWidth(), size.getHeight(), null, false, null, null);

		Date last = null;
		List<DrawPart> drawParts = new ArrayList<DrawPart>();
		int length = draws.size();
		for (int i = 0; i < length; i++) {
			Draw draw = draws.get(i);
			Rectangle drawBounds = new Rectangle(draw_x, draw_y, draw_w, draw_h);

			DrawPart drawPart = new DrawPart(display, draw, PBConstants.DRAW_SQUARE_01_PER_ROW, MetaConstants.WHITE_BALL_MAX);
			drawPart.setBounds(drawBounds.x, drawBounds.y, drawBounds.width, drawBounds.height);
			drawPart.createContents();
			drawParts.add(drawPart);

			draw_x += draw_width;

			if (i == length - 1) {
				last = draw.getDate();
			}
		}

		// Last dummy draw
		Draw dummyDraw = new Draw();
		dummyDraw.setDrawId((!draws.isEmpty()) ? (draws.get(draws.size() - 1).getDrawId() + 1) : 1);
		dummyDraw.setDate(DateUtil.addHours(last, 24 * 3));
		dummyDraw.setDummy(true);

		// Part for dummy draw
		{
			DrawPart drawPart = new DrawPart(display, dummyDraw, PBConstants.DRAW_SQUARE_01_PER_ROW, MetaConstants.WHITE_BALL_MAX);
			drawPart.setBounds(draw_x, draw_y + 3, draw_w, draw_h);
			drawPart.createContents();
			drawParts.add(drawPart);
		}

		if (showLinks) {
			DrawPart prevDrawPart = null;
			for (DrawPart currDrawPart : drawParts) {
				try {
					Draw currDraw = currDrawPart.getDraw();
					// if (currDraw.isDummy()) {
					// continue;
					// }

					int curr_draw_x = currDrawPart.getBounds().getX();
					int shiftX = currDraw.isDummy() ? 4 : 7;
					int shiftY = 5;

					NumberPart currNumPart1 = null;
					NumberPart currNumPart2 = null;
					NumberPart currNumPart3 = null;
					NumberPart currNumPart4 = null;
					NumberPart currNumPart5 = null;
					if (!currDraw.isDummy()) {
						currNumPart1 = currDrawPart.getMatchedNumberParts().get(0);
						currNumPart2 = currDrawPart.getMatchedNumberParts().get(1);
						currNumPart3 = currDrawPart.getMatchedNumberParts().get(2);
						currNumPart4 = currDrawPart.getMatchedNumberParts().get(3);
						currNumPart5 = currDrawPart.getMatchedNumberParts().get(4);

						currNumPart1.setIndex(new Point(0, currNumPart1.getNumber() - 1));
						currNumPart2.setIndex(new Point(0, currNumPart2.getNumber() - 1));
						currNumPart3.setIndex(new Point(0, currNumPart3.getNumber() - 1));
						currNumPart4.setIndex(new Point(0, currNumPart4.getNumber() - 1));
						currNumPart5.setIndex(new Point(0, currNumPart5.getNumber() - 1));

						currNumPart1.setLocation(new Point(curr_draw_x + shiftX, currNumPart1.getNumber() * 10 + shiftY));
						currNumPart2.setLocation(new Point(curr_draw_x + shiftX, currNumPart2.getNumber() * 10 + shiftY));
						currNumPart3.setLocation(new Point(curr_draw_x + shiftX, currNumPart3.getNumber() * 10 + shiftY));
						currNumPart4.setLocation(new Point(curr_draw_x + shiftX, currNumPart4.getNumber() * 10 + shiftY));
						currNumPart5.setLocation(new Point(curr_draw_x + shiftX, currNumPart5.getNumber() * 10 + shiftY));
					}

					if (prevDrawPart != null) {
						NumberPart prevNumPart1 = prevDrawPart.getMatchedNumberParts().get(0);
						NumberPart prevNumPart2 = prevDrawPart.getMatchedNumberParts().get(1);
						NumberPart prevNumPart3 = prevDrawPart.getMatchedNumberParts().get(2);
						NumberPart prevNumPart4 = prevDrawPart.getMatchedNumberParts().get(3);
						NumberPart prevNumPart5 = prevDrawPart.getMatchedNumberParts().get(4);

						if (!currDraw.isDummy()) {
							LinkPart link1 = new LinkPart(display, prevNumPart1, currNumPart1);
							LinkPart link2 = new LinkPart(display, prevNumPart2, currNumPart2);
							LinkPart link3 = new LinkPart(display, prevNumPart3, currNumPart3);
							LinkPart link4 = new LinkPart(display, prevNumPart4, currNumPart4);
							LinkPart link5 = new LinkPart(display, prevNumPart5, currNumPart5);

							link1.createContents();
							link2.createContents();
							link3.createContents();
							link4.createContents();
							link5.createContents();
						}

						// ------------------------------------------------------------------------------------
						// draw predicted numbers begins
						// ------------------------------------------------------------------------------------
						List<Draw> predictedDraws = idToPredictedNumbers.get(currDraw.getDrawId());
						if (predictedDraws != null && !predictedDraws.isEmpty()) {
							for (int j = 0; j < predictedDraws.size(); j++) {
								Draw predictedDraw = predictedDraws.get(j);

								String strokeColor = indexToPredictedLinkStrokeColor.get(j);

								NumberPart predictedNumPart1 = currDrawPart.getNumberPart(predictedDraw.getNum1());
								NumberPart predictedNumPart2 = currDrawPart.getNumberPart(predictedDraw.getNum2());
								NumberPart predictedNumPart3 = currDrawPart.getNumberPart(predictedDraw.getNum3());
								NumberPart predictedNumPart4 = currDrawPart.getNumberPart(predictedDraw.getNum4());
								NumberPart predictedNumPart5 = currDrawPart.getNumberPart(predictedDraw.getNum5());

								if (predictedNumPart1.getIndex() == null || predictedNumPart1.getLocation() == null) {
									predictedNumPart1.setIndex(new Point(0, predictedNumPart1.getNumber() - 1));
									predictedNumPart1.setLocation(new Point(curr_draw_x + shiftX, predictedNumPart1.getNumber() * 10 + shiftY));
								}
								if (predictedNumPart2.getIndex() == null || predictedNumPart2.getLocation() == null) {
									predictedNumPart2.setIndex(new Point(0, predictedNumPart2.getNumber() - 1));
									predictedNumPart2.setLocation(new Point(curr_draw_x + shiftX, predictedNumPart2.getNumber() * 10 + shiftY));
								}
								if (predictedNumPart3.getIndex() == null || predictedNumPart3.getLocation() == null) {
									predictedNumPart3.setIndex(new Point(0, predictedNumPart3.getNumber() - 1));
									predictedNumPart3.setLocation(new Point(curr_draw_x + shiftX, predictedNumPart3.getNumber() * 10 + shiftY));
								}
								if (predictedNumPart4.getIndex() == null || predictedNumPart4.getLocation() == null) {
									predictedNumPart4.setIndex(new Point(0, predictedNumPart4.getNumber() - 1));
									predictedNumPart4.setLocation(new Point(curr_draw_x + shiftX, predictedNumPart4.getNumber() * 10 + shiftY));
								}
								if (predictedNumPart5.getIndex() == null || predictedNumPart5.getLocation() == null) {
									predictedNumPart5.setIndex(new Point(0, predictedNumPart5.getNumber() - 1));
									predictedNumPart5.setLocation(new Point(curr_draw_x + shiftX, predictedNumPart5.getNumber() * 10 + shiftY));
								}

								LinkPart predictedLink1 = new LinkPart(display, prevNumPart1, predictedNumPart1);
								LinkPart predictedLink2 = new LinkPart(display, prevNumPart2, predictedNumPart2);
								LinkPart predictedLink3 = new LinkPart(display, prevNumPart3, predictedNumPart3);
								LinkPart predictedLink4 = new LinkPart(display, prevNumPart4, predictedNumPart4);
								LinkPart predictedLink5 = new LinkPart(display, prevNumPart5, predictedNumPart5);

								predictedLink1.setPredicted(true);
								predictedLink2.setPredicted(true);
								predictedLink3.setPredicted(true);
								predictedLink4.setPredicted(true);
								predictedLink5.setPredicted(true);

								predictedLink1.setStrokeColor(strokeColor);
								predictedLink2.setStrokeColor(strokeColor);
								predictedLink3.setStrokeColor(strokeColor);
								predictedLink4.setStrokeColor(strokeColor);
								predictedLink5.setStrokeColor(strokeColor);

								predictedLink1.createContents();
								predictedLink2.createContents();
								predictedLink3.createContents();
								predictedLink4.createContents();
								predictedLink5.createContents();
							}
						}
						// ------------------------------------------------------------------------------------
						// draw predicted numbers ends
						// ------------------------------------------------------------------------------------
					}

					prevDrawPart = currDrawPart;

				} catch (Exception e) {
					// e.printStackTrace();
				}
			}
		}

		if (showMB) {
			List<PBPart> pbParts = new ArrayList<PBPart>();
			for (int i = 0; i < draws.size(); i++) {
				Draw draw = draws.get(i);

				PBPart pbPart = new PBPart(display, draw, PBConstants.PB_SQUARE_01_PER_ROW, MetaConstants.MEGA_BALL_MAX);
				pbPart.setBounds(pb_x, pb_y, pb_w, pb_h);
				pbPart.createContents();
				pbParts.add(pbPart);

				pb_x += pb_width;
			}

			// Part for dummy draw
			{
				PBPart pbPart = new PBPart(display, dummyDraw, PBConstants.PB_SQUARE_01_PER_ROW, MetaConstants.MEGA_BALL_MAX);
				pbPart.setBounds(pb_x, pb_y + 3, pb_w, pb_h);
				pbPart.createContents();
				pbParts.add(pbPart);
			}

			if (showLinks) {
				PBPart prevPBPart = null;
				for (PBPart currPBPart : pbParts) {
					Draw currDraw = currPBPart.getDraw();
					// if (currDraw.isDummy()) {
					// continue;
					// }

					int curr_draw_x = currPBPart.getBounds().getX();
					int shiftX = currDraw.isDummy() ? 4 : 7;
					int shiftY = -4;

					NumberPart currNumPart = currPBPart.getMatchedNumberPart();
					if (currNumPart != null) {
						currNumPart.setIndex(new Point(0, currNumPart.getNumber() - 1));
						currNumPart.setLocation(new Point(curr_draw_x + shiftX, pb_y + currNumPart.getNumber() * 10 + shiftY));
					}

					if (prevPBPart != null) {
						NumberPart prevNumPart = prevPBPart.getMatchedNumberPart();

						if (prevNumPart != null && currNumPart != null) {
							LinkPart link = new LinkPart(display, prevNumPart, currNumPart);
							link.createContents();
						}

						// ------------------------------------------------------------------------------------
						// draw predicted PB number begins
						// ------------------------------------------------------------------------------------
						List<Draw> predictedDraws = idToPredictedNumbers.get(currDraw.getDrawId());
						if (predictedDraws != null && !predictedDraws.isEmpty()) {
							for (int j = 0; j < predictedDraws.size(); j++) {
								Draw predictedDraw = predictedDraws.get(j);
								String strokeColor = indexToPredictedLinkStrokeColor.get(j);

								NumberPart predictedNumPart = currPBPart.getNumberPart(predictedDraw.getPB());

								if (predictedNumPart.getIndex() == null || predictedNumPart.getLocation() == null) {
									predictedNumPart.setIndex(new Point(0, predictedNumPart.getNumber() - 1));
									predictedNumPart.setLocation(new Point(curr_draw_x + shiftX, pb_y + predictedNumPart.getNumber() * 10 + shiftY));
								}

								LinkPart predictedLink = new LinkPart(display, prevNumPart, predictedNumPart);
								predictedLink.setPredicted(true);
								predictedLink.setStrokeColor(strokeColor);
								predictedLink.createContents();
							}
						}
						// ------------------------------------------------------------------------------------
						// draw predicted PB number begins
						// ------------------------------------------------------------------------------------
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
	 * @param idToPredictedNumbers
	 * @return
	 */
	protected static Display generate_10_per_row(List<Draw> draws, Map<Integer, List<Draw>> idToPredictedNumbers) {
		int draw_x = 0 + 10; // 10 is for top shift
		int draw_y = 0 + 10; // 10 is for left shift
		int draw_w = 102;
		int draw_h = 72;

		int total_h = ((draws.size() + 1) / 10) * (72 + 15) + 100;

		Size size = new Size(1300, total_h);
		Display display = new Display(null, "display", size.getWidth(), size.getHeight(), null, false, null, null);

		// Last dummy draw
		Date last = (!draws.isEmpty()) ? draws.get(draws.size() - 1).getDate() : new Date();
		Draw dummyDraw = new Draw();
		dummyDraw.setDrawId((!draws.isEmpty()) ? (draws.get(draws.size() - 1).getDrawId() + 1) : 1);
		dummyDraw.setDate(DateUtil.addHours(last, 24 * 3));
		dummyDraw.setDummy(true);

		List<Draw> draws2 = new ArrayList<Draw>();
		draws2.addAll(draws);
		draws2.add(dummyDraw);

		List<DrawPart> drawParts = new ArrayList<DrawPart>();
		for (Draw draw : draws2) {
			int drawId = draw.getDrawId();
			List<Draw> predictedDraws = idToPredictedNumbers.get(drawId);

			DrawPart drawPart = new DrawPart(display, draw, PBConstants.DRAW_SQUARE_10_PER_ROW, MetaConstants.WHITE_BALL_MAX);
			drawPart.setPredictedDraws(predictedDraws);
			drawPart.setIndexToPredictedLinkStrokeColor(indexToPredictedLinkStrokeColor);
			drawPart.setShowLinks(true);
			drawPart.setBounds(draw_x, draw_y, draw_w, draw_h);
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
	 * @param idToPredictedNumbers
	 * @return
	 */
	protected static Display generate_14_per_row(List<Draw> draws, Map<Integer, List<Draw>> idToPredictedNumbers) {
		int draw_x = 0 + 10; // 10 is for top shift
		int draw_y = 0 + 10; // 10 is for left shift
		int draw_w = 142;
		int draw_h = 52;

		int total_h = ((draws.size() + 1) / 8) * (52 + 15) + 100;

		Size size = new Size(1300, total_h);
		Display display = new Display(null, "display", size.getWidth(), size.getHeight(), null, false, null, null);

		// Last dummy draw
		Date last = (!draws.isEmpty()) ? draws.get(draws.size() - 1).getDate() : new Date();
		Draw dummyDraw = new Draw();
		dummyDraw.setDrawId((!draws.isEmpty()) ? (draws.get(draws.size() - 1).getDrawId() + 1) : 1);
		dummyDraw.setDate(DateUtil.addHours(last, 24 * 3));
		dummyDraw.setDummy(true);

		List<Draw> draws2 = new ArrayList<Draw>();
		draws2.addAll(draws);
		draws2.add(dummyDraw);

		List<DrawPart> drawParts = new ArrayList<DrawPart>();
		for (Draw draw : draws2) {
			int drawId = draw.getDrawId();
			List<Draw> predictedDraws = idToPredictedNumbers.get(drawId);

			DrawPart drawPart = new DrawPart(display, draw, PBConstants.DRAW_SQUARE_14_PER_ROW, MetaConstants.WHITE_BALL_MAX);
			drawPart.setPredictedDraws(predictedDraws);
			drawPart.setIndexToPredictedLinkStrokeColor(indexToPredictedLinkStrokeColor);
			drawPart.setShowLinks(true);
			drawPart.setBounds(draw_x, draw_y, draw_w, draw_h);
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

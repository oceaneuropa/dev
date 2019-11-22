package org.plutus.lottery.powerball;

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
import org.origin.svg.graphics.Size;
import org.origin.svg.util.SVGStringWriter;
import org.origin.wwt.render.widget.util.WidgetFigureHandler;
import org.origin.wwt.widgets.Display;
import org.origin.wwt.widgets.render.FigureHandlerRegistry;
import org.plutus.lottery.common.AnalysisContext;
import org.plutus.lottery.common.AnalysisRegistry;
import org.plutus.lottery.common.Draw;
import org.plutus.lottery.powerball.analysis.A11_MinMaxAvgAnalysis;
import org.plutus.lottery.powerball.analysis.A12_NumberDiffAnalysis;
import org.plutus.lottery.powerball.analysis.A13_RangeNormalizationAnalysis;
import org.plutus.lottery.powerball.analysis.A21_OddEvenAnalysis;
import org.plutus.lottery.powerball.analysis.A22_SumAnalysis;
import org.plutus.lottery.powerball.analysis.A23_HotColdAnalysis;
import org.plutus.lottery.powerball.analysis.A24_RepetitionAnalysis;
import org.plutus.lottery.render.control.IntegersPart;
import org.plutus.lottery.render.control.LinkPart;
import org.plutus.lottery.render.control.NumberPart;
import org.plutus.lottery.render.render.DrawFigureFactory;
import org.plutus.lottery.render.render.LinkFigureFactory;
import org.plutus.lottery.render.render.NumberFigureFactory;
import org.plutus.lottery.render.render.PBFigureFactory;
import org.plutus.lottery.util.DrawHelper;
import org.plutus.lottery.util.DrawReaderV2;

public class PBMain2 {

	static {
		FigureHandlerRegistry.register(new WidgetFigureHandler());
		A11_MinMaxAvgAnalysis.INSTANCE.register();
		A12_NumberDiffAnalysis.INSTANCE.register();
		A13_RangeNormalizationAnalysis.INSTANCE.register();
		A21_OddEvenAnalysis.INSTANCE.register();
		A24_RepetitionAnalysis.INSTANCE.register();
		A22_SumAnalysis.INSTANCE.register();
		A23_HotColdAnalysis.INSTANCE.register();
	}

	static {
		DrawFigureFactory.register();
		PBFigureFactory.register();
		NumberFigureFactory.register();
		LinkFigureFactory.register();
	}

	public static void main(String[] args) {
		try {
			List<Draw> draws = getDraws();
			AnalysisContext context = new AnalysisContext();
			context.setDraws(draws);

			AnalysisRegistry.getInstance().run(context);
			AnalysisRegistry.getInstance().printResult(context);

			// Map<Integer, List<Draw>> yearToDraws = getDrawsByYear();
			Map<Integer, List<Draw>> yearToDraws = DrawHelper.INSTANCE.groupByYear(draws);

			// avg range: [12, 57]
			generate_avg(yearToDraws, SystemUtils.getUserDir(), "/doc/svg/powerball_draws__avg__{0}.svg", PBConstants.DRAW_AVG);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected static List<Draw> getDraws() throws IOException {
		return DrawHelper.INSTANCE.read(DrawReaderV2.INSTANCE, new File(SystemUtils.getUserDir(), "/doc/data/DownloadAllNumbers.txt"));
	}

	public static void generate_avg(Map<Integer, List<Draw>> yearToDraws, File baseFolder, String outputFileLocation, int drawStyle) {
		for (Iterator<Integer> yearItor = yearToDraws.keySet().iterator(); yearItor.hasNext();) {
			Integer year = yearItor.next();
			List<Draw> draws = yearToDraws.get(year);

			try {
				String fileLocation = MessageFormat.format(outputFileLocation, new Object[] { String.valueOf(year) });
				Display display = null;
				if (drawStyle == PBConstants.DRAW_AVG) {
					display = generate_avg(draws, true);
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
			Shape rootShape = FigureHandlerRegistry.getFigureHandler(display).createFigure(display);
			// Shape rootShape = WidgetFigureFactory.getInstance().createFigure(display);
			SVGStringWriter writer = new SVGStringWriter(rootShape);

			output = new FileOutputStream(file);
			writer.write(output);
		} finally {
			IOUtil.closeQuietly(output, true);
		}
	}

	protected static Display generate_avg(List<Draw> draws, boolean showAvgLinks) {
		// 10 and 10 are for the overall shift for all figures
		int draw_x = 0 + 10;
		int draw_y = 0 + 10;
		int draw_w = 10;
		int draw_h = 690;
		int space_w = 100;
		int draw_width = draw_w + space_w; // total draw width (one number column and empty space)

		int display_width = draws.size() * draw_width + 100;
		Size size = new Size(display_width, draw_h + 100);
		Display display = new Display("display", size.getWidth(), size.getHeight(), null, false, null);

		List<IntegersPart> integersParts = new ArrayList<IntegersPart>();
		for (int i = 0; i < draws.size(); i++) {
			Draw draw = draws.get(i);
			int avg = draw.getStat().get(DrawStat.PROP_AVG, Integer.class);

			IntegersPart integersPart = new IntegersPart(display, 1, 69, PBConstants.NONE);
			integersPart.setMatchedNum(avg);
			integersPart.setBounds(draw_x, draw_y, draw_w, draw_h);
			integersPart.createContents();
			integersParts.add(integersPart);

			draw_x += draw_width;
		}

		if (showAvgLinks) {
			IntegersPart prevIntegersPart = null;
			for (IntegersPart currIntegersPart : integersParts) {
				int curr_draw_x = currIntegersPart.getBounds().getX();

				NumberPart numPartCurr1 = currIntegersPart.getMatchedNumberParts().get(0);
				numPartCurr1.setIndex(new Point(0, numPartCurr1.getNumber() - 1));

				int shiftX = 5;
				int shiftY = 5;
				numPartCurr1.setLocation(new Point(curr_draw_x + shiftX, numPartCurr1.getNumber() * 10 + shiftY));

				if (prevIntegersPart != null) {
					NumberPart numPartPrev1 = prevIntegersPart.getMatchedNumberParts().get(0);

					LinkPart link1 = new LinkPart(display, numPartPrev1, numPartCurr1);
					link1.createContents();
				}

				prevIntegersPart = currIntegersPart;
			}
		}

		return display;
	}

}

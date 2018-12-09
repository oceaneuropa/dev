package org.plutus.lottery.svg;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
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

public class Main {

	public static void main(String[] args) {
		try {
			BoardFigureFactory.register();
			NumberFigureFactory.register();

			File inputFile = new File(SystemUtils.getUserDir(), "/doc/data/DownloadAllNumbers.txt");

			List<Draw> allDraws = DrawReaderV2.read(inputFile);
			Map<Integer, List<Draw>> yearToDraws = DrawHelper.INSTANCE.groupByYear(allDraws);

			for (Iterator<Integer> yearItor = yearToDraws.keySet().iterator(); yearItor.hasNext();) {
				Integer year = yearItor.next();
				List<Draw> draws = yearToDraws.get(year);

				FileOutputStream output = null;
				try {
					File outputFile = new File(SystemUtils.getUserDir(), "/doc/svg/powerball_draws_" + year + ".svg");
					output = new FileOutputStream(outputFile);
					generate(draws, output);

				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					IOUtil.closeQuietly(output, true);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param draws
	 * @param output
	 * @throws IOException
	 */
	protected static void generate(List<Draw> draws, FileOutputStream output) throws IOException {
		int board_x = 0 + 10; // 10 is for top shift
		int board_y = 0 + 10; // 10 is for left shift
		int board_w = 140;
		int board_h = 50;

		int total_h = (draws.size() / 8) * (50 + 10) + 100;
		if (total_h < 1000) {
			total_h = 1000;
		}

		Size size = new Size(1300, total_h);
		Display display = new Display(size);

		int count = 0;
		List<Board> boards = new ArrayList<Board>();
		for (Draw draw : draws) {
			Rectangle bounds = new Rectangle(board_x, board_y, board_w, board_h);
			Board board = new Board(display, draw);
			board.setBounds(bounds);
			board.createContents();
			boards.add(board);

			board_x += board_w + 10;
			if (board_x + board_w > size.x) {
				board_x = 0 + 10; // 10 is for top shift
				board_y += board_h + 10 + 4; // 4 is for distance between rows
			}

			count++;
			if (count >= 200) {
				break;
			}
		}

		Shape rootShape = WidgetFigureFactory.getInstance().createFigure(display);

		SVGStringWriter writer = new SVGStringWriter(rootShape);
		// String xmlStr = writer.toXml();
		// System.out.println(xmlStr);
		writer.write(output);
	}

}

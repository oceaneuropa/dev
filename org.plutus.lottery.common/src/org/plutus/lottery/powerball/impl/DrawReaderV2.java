package org.plutus.lottery.powerball.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.origin.common.io.IOUtil;
import org.origin.common.util.DateUtil;
import org.plutus.lottery.common.Comparators;
import org.plutus.lottery.powerball.Draw;
import org.plutus.lottery.powerball.DrawReader;

public class DrawReaderV2 implements DrawReader {

	public static DrawReaderV2 INSTANCE = new DrawReaderV2();

	public DrawReaderV2() {
	}

	/**
	 * 
	 * @param input
	 * @return
	 */
	public List<Draw> read(InputStream input) throws IOException {
		List<Draw> draws = new ArrayList<Draw>();

		List<String> lines = IOUtil.readLines(input, "UTF-8");
		if (lines != null) {
			for (String line : lines) {
				if (line.contains("Draw #") || line.contains("Draw Date") || line.contains("Number_")) {
					continue;
				}

				Draw draw = convertToDraw(line);
				if (draw != null) {
					draws.add(draw);
				}
			}
		}

		if (draws.size() > 1) {
			Collections.sort(draws, Comparators.DrawComparator.ASC);
		}

		// set 1 based index to draws.
		// first draw's index is 1
		// latest draw's index is the size of all draws
		for (int i = 0; i < draws.size(); i++) {
			Draw draw = draws.get(i);
			draw.setIndex(i + 1);
		}

		return draws;
	}

	/**
	 * 
	 * @param line
	 * @return
	 */
	protected Draw convertToDraw(String line) {
		if (line == null || line.trim().isEmpty() || line.startsWith("#")) {
			return null;
		}
		String[] segments = line.split("\\s+");
		if (segments.length < 11) {
			System.err.println(getClass().getSimpleName() + ".convertToDraw() invalid line: " + line);
			return null;
		}

		String drawIdStr = segments[0];

		String dateStrDayOfWeek = segments[1];
		String dateStrMonth = segments[2];
		String dateStrDay = segments[3];
		String dateStrYear = segments[4];
		String dateStr = dateStrDayOfWeek + dateStrMonth + " " + dateStrDay + dateStrYear;

		String num1Str = segments[5];
		String num2Str = segments[6];
		String num3Str = segments[7];
		String num4Str = segments[8];
		String num5Str = segments[9];
		String pbStr = segments[10];

		try {
			int drawId = Integer.parseInt(drawIdStr);
			Date date = DateUtil.toDate(dateStr, DateUtil.MONTH_DAY_YEAR_FORMAT2);
			int num1 = Integer.parseInt(num1Str);
			int num2 = Integer.parseInt(num2Str);
			int num3 = Integer.parseInt(num3Str);
			int num4 = Integer.parseInt(num4Str);
			int num5 = Integer.parseInt(num5Str);
			int pb = Integer.parseInt(pbStr);

			List<Integer> nums = new ArrayList<Integer>();
			nums.add(num1);
			nums.add(num2);
			nums.add(num3);
			nums.add(num4);
			nums.add(num5);
			Collections.sort(nums);

			int newNum1 = nums.get(0);
			int newNum2 = nums.get(1);
			int newNum3 = nums.get(2);
			int newNum4 = nums.get(3);
			int newNum5 = nums.get(4);

			Draw draw = new Draw(date, newNum1, newNum2, newNum3, newNum4, newNum5, pb);
			draw.setDrawId(drawId);
			return draw;

		} catch (Exception e) {
			System.err.println("convertToDraw() invalid line: " + line);
			// e.printStackTrace();
		}
		return null;
	}

}

// /**
// *
// * @param file
// * @return
// * @throws IOException
// */
// public static List<Draw> read(File file) throws IOException {
// List<Draw> draws = null;
// FileInputStream fis = null;
// try {
// fis = new FileInputStream(file);
// DrawReaderV2 reader = new DrawReaderV2();
// draws = reader.read(fis);
// } finally {
// IOUtil.closeQuietly(fis, true);
// }
// if (draws == null) {
// draws = Collections.emptyList();
// }
// return draws;
// }

package org.plutus.lottery.powerball;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.origin.common.io.IOUtil;
import org.origin.common.util.DateUtil;

public class DrawReader {

	public DrawReader() {
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
				if (line.contains("Draw Date") || line.contains("WB") || line.contains("PB") || line.contains("PP")) {
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

		return draws;
	}

	/**
	 * 
	 * @param line
	 * @return
	 */
	protected Draw convertToDraw(String line) {
		String[] segments = line.split("\\s+");
		if (segments == null || segments.length < 7) {
			System.err.println("convertToDraw() invalid line: " + line);
			return null;
		}

		String dateStr = segments[0];
		String num1Str = segments[1];
		String num2Str = segments[2];
		String num3Str = segments[3];
		String num4Str = segments[4];
		String num5Str = segments[5];
		String pbStr = segments[6];
		// String ppStr = segments[7]; // not used

		try {
			Date date = DateUtil.toDate(dateStr, DateUtil.MONTH_DAY_YEAR_FORMAT1);
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
			return draw;

		} catch (Exception e) {
			System.err.println("convertToDraw() invalid line: " + line);
			e.printStackTrace();
		}
		return null;
	}

}

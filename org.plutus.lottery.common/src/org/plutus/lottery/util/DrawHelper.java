package org.plutus.lottery.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.origin.common.util.DateUtil;
import org.origin.common.util.IOUtil;
import org.plutus.lottery.common.Draw;

public class DrawHelper {

	public static DrawHelper INSTANCE = new DrawHelper();

	/**
	 * 
	 * @param reader
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public List<Draw> read(DrawReader reader, File file) throws IOException {
		List<Draw> draws = null;
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(file);
			draws = reader.read(fis);
		} finally {
			IOUtil.closeQuietly(fis, true);
		}
		if (draws == null) {
			draws = Collections.emptyList();
		}
		return draws;
	}

	/**
	 * 
	 * @param allDraws
	 * @return
	 */
	public Map<Integer, List<Draw>> groupByYear(List<Draw> allDraws) {
		return groupByYear(allDraws, 0);
	}

	/**
	 * 
	 * @param allDraws
	 * @param numberOfDrawsFromNextYear
	 * @return
	 */
	public Map<Integer, List<Draw>> groupByYear(List<Draw> allDraws, int numberOfDrawsFromNextYear) {
		Map<Integer, List<Draw>> yearToDraws = new TreeMap<Integer, List<Draw>>();

		for (int i = 0; i < allDraws.size(); i++) {
			Draw draw = allDraws.get(i);
			int year = DateUtil.getYear(draw.getDate());

			List<Draw> draws = yearToDraws.get(year);
			if (draws == null) {
				draws = new ArrayList<Draw>();
				yearToDraws.put(year, draws);
			}
			draws.add(draw);
		}

		if (numberOfDrawsFromNextYear > 0) {
			for (Iterator<Integer> yearItor = yearToDraws.keySet().iterator(); yearItor.hasNext();) {
				int year = yearItor.next();
				int nextYear = year + 1;

				List<Draw> drawsOfNextYear = yearToDraws.get(nextYear);
				if (drawsOfNextYear != null && !drawsOfNextYear.isEmpty()) {
					List<Draw> drawsOfCurrYear = yearToDraws.get(year);
					if (drawsOfCurrYear == null) {
						drawsOfCurrYear = new ArrayList<Draw>();
						yearToDraws.put(year, drawsOfCurrYear);
					}

					int numToAdd = numberOfDrawsFromNextYear < drawsOfNextYear.size() ? numberOfDrawsFromNextYear : drawsOfNextYear.size();
					drawsOfCurrYear.addAll(drawsOfNextYear.subList(0, numToAdd));
				}
			}
		}

		return yearToDraws;
	}

	/**
	 * 
	 * @param allDraws
	 * @return
	 */
	public Map<Integer, List<Draw>> groupByDrawId(List<Draw> allDraws) {
		Map<Integer, List<Draw>> idToDraws = new TreeMap<Integer, List<Draw>>();
		for (Draw draw : allDraws) {
			int drawId = draw.getDrawId();

			List<Draw> draws = idToDraws.get(drawId);
			if (draws == null) {
				draws = new ArrayList<Draw>();
				idToDraws.put(drawId, draws);
			}
			draws.add(draw);
		}
		return idToDraws;
	}

}

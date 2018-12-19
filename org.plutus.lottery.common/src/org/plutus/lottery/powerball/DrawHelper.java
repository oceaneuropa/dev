package org.plutus.lottery.powerball;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.origin.common.io.IOUtil;
import org.origin.common.util.DateUtil;

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
		Map<Integer, List<Draw>> yearToDraws = new TreeMap<Integer, List<Draw>>();
		for (Draw draw : allDraws) {
			int year = DateUtil.getYear(draw.getDate());

			List<Draw> draws = yearToDraws.get(year);
			if (draws == null) {
				draws = new ArrayList<Draw>();
				yearToDraws.put(year, draws);
			}
			draws.add(draw);
		}
		return yearToDraws;
	}

}

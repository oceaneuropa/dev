package org.plutus.lottery.powerball;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.origin.common.util.DateUtil;

public class DrawHelper {

	public static DrawHelper INSTANCE = new DrawHelper();

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

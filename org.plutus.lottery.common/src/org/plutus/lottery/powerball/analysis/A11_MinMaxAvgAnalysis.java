package org.plutus.lottery.powerball.analysis;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.origin.common.util.StatUtil;
import org.plutus.lottery.powerball.Analysis;
import org.plutus.lottery.powerball.AnalysisContext;
import org.plutus.lottery.powerball.AnalysisRegistry;
import org.plutus.lottery.powerball.Draw;
import org.plutus.lottery.powerball.DrawStat;

public class A11_MinMaxAvgAnalysis implements Analysis {

	public static String ANALYSIS_ID = "MinMaxAvgAnalysis";

	public static A11_MinMaxAvgAnalysis INSTANCE = new A11_MinMaxAvgAnalysis();

	@Override
	public String getId() {
		return ANALYSIS_ID;
	}

	@Override
	public int getPriority() {
		return 1;
	}

	@Override
	public void start() {
		AnalysisRegistry.getInstance().add(this);
	}

	@Override
	public void stop() {
		AnalysisRegistry.getInstance().remove(this);
	}

	@Override
	public void run(AnalysisContext context) {
		DrawStat globalStat = context.getGlobalStat();
		List<Draw> draws = context.getDraws();

		for (Draw draw : draws) {
			DrawStat stat = draw.getStat();
			int n1 = draw.getNum1();
			int n2 = draw.getNum2();
			int n3 = draw.getNum3();
			int n4 = draw.getNum4();
			int n5 = draw.getNum5();

			// ----------------------------------------------------
			// 最小值 最大值 中值
			// ----------------------------------------------------
			int min = n1;
			int mid = n3;
			int max = n5;

			// ----------------------------------------------------
			// 均值
			// ----------------------------------------------------
			int avg = (int) StatUtil.avg(2, n1, n2, n3, n4, n5);
			int avgToMidPercent = StatUtil.normalizeByPercentage(avg, mid, 100);

			// ----------------------------------------------------
			// 间距均值
			// ----------------------------------------------------
			int dist1 = n2 - n1;
			int dist2 = n3 - n2;
			int dist3 = n4 - n3;
			int dist4 = n5 - n4;
			int dist_avg = (int) StatUtil.avg(2, dist1, dist2, dist3, dist4);

			// ----------------------------------------------------
			// 保存统计结果
			// ----------------------------------------------------
			stat.put(DrawStat.PROP_MIN, min);
			stat.put(DrawStat.PROP_MID, mid);
			stat.put(DrawStat.PROP_MAX, max);
			stat.put(DrawStat.PROP_AVG, avg);
			stat.put(DrawStat.PROP_AVG_VS_MID_BY_PERCENTAGE, avgToMidPercent);
			stat.put(DrawStat.PROP_AVG_VS_MID_BY_PERCENTAGE_ZERO_BASED, avgToMidPercent - 100);
			stat.put(DrawStat.PROP_DIST_AVG, dist_avg);
		}

		// ----------------------------------------------------
		// 平均值的重复次数
		// ----------------------------------------------------
		updateAvgCounts(globalStat, draws);
	}

	/**
	 * 
	 * @param globalStat
	 * @param draws
	 */
	protected void updateAvgCounts(DrawStat globalStat, List<Draw> draws) {
		Map<Integer, Integer> avgToCountMap = new TreeMap<Integer, Integer>();
		Map<Integer, Integer> avgToPercentMap = new TreeMap<Integer, Integer>();

		for (Draw draw : draws) {
			DrawStat stat = draw.getStat();
			Integer avg = stat.get(DrawStat.PROP_AVG, Integer.class);

			Integer count = avgToCountMap.get(avg);
			if (count == null) {
				count = new Integer(1);
			} else {
				count = new Integer(count.intValue() + 1);
			}
			avgToCountMap.put(avg, count);
		}

		int totalCount = 0;
		for (Iterator<Integer> keyItor = avgToCountMap.keySet().iterator(); keyItor.hasNext();) {
			Integer avg = keyItor.next();
			int count = avgToCountMap.get(avg);
			totalCount += count;
		}
		for (Iterator<Integer> keyItor = avgToCountMap.keySet().iterator(); keyItor.hasNext();) {
			Integer avg = keyItor.next();
			int count = avgToCountMap.get(avg);

			int percent = StatUtil.normalizeByPercentage(count, totalCount, 100);
			avgToPercentMap.put(avg, percent);
		}

		globalStat.put(DrawStat.PROP_AVG_COUNT_MAP, avgToCountMap);
		globalStat.put(DrawStat.PROP_AVG_PERCENT_MAP, avgToPercentMap);
	}

}

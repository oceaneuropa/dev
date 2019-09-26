package org.plutus.lottery.powerball.analysis;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.origin.common.util.BigDecimalUtil;
import org.origin.common.util.StatUtil;
import org.plutus.lottery.common.Analysis;
import org.plutus.lottery.common.AnalysisContext;
import org.plutus.lottery.common.AnalysisImpl;
import org.plutus.lottery.common.AnalysisRegistry;
import org.plutus.lottery.common.Draw;
import org.plutus.lottery.powerball.DrawStat;

public class A22_SumAnalysis extends AnalysisImpl implements Analysis {

	public static String ANALYSIS_ID = "SumAnalysis";

	public static A22_SumAnalysis INSTANCE = new A22_SumAnalysis();

	@Override
	public void register() {
		AnalysisRegistry.getInstance().add(this);
	}

	@Override
	public void unregister() {
		AnalysisRegistry.getInstance().remove(this);
	}

	@Override
	public String getId() {
		return ANALYSIS_ID;
	}

	@Override
	public int getPriority() {
		return 3;
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
			// 加和
			// ----------------------------------------------------
			long sum = StatUtil.sum(n1, n2, n3, n4, n5);
			stat.put(DrawStat.PROP_SUM, sum);
		}

		// ----------------------------------------------------
		// 总和值与其出现次数的Map
		// ----------------------------------------------------
		Map<Long, Integer> sumToCountMap = new TreeMap<Long, Integer>();
		for (Draw draw : draws) {
			DrawStat stat = draw.getStat();

			Long sum = stat.get(DrawStat.PROP_SUM, Long.class);
			Integer count = sumToCountMap.get(sum);
			if (count == null) {
				count = new Integer(1);
			} else {
				count = new Integer(count.intValue() + 1);
			}
			sumToCountMap.put(sum, count);
		}

		// ----------------------------------------------------
		// 不重复的总和值列表
		// ----------------------------------------------------
		List<Long> sumList = new ArrayList<Long>();
		for (Iterator<Long> sumItor = sumToCountMap.keySet().iterator(); sumItor.hasNext();) {
			Long sum = sumItor.next();
			sumList.add(sum);
		}

		// ----------------------------------------------------
		// 出现次数与总和值列表的Map(按出现次数排序)
		// 所有总和的全部重复次数
		// ----------------------------------------------------
		int sumTotalCount = 0;
		Map<Integer, List<Long>> countToSumListMap = new TreeMap<Integer, List<Long>>();
		for (Iterator<Long> sumItor = sumToCountMap.keySet().iterator(); sumItor.hasNext();) {
			Long sum = sumItor.next();
			Integer count = sumToCountMap.get(sum);

			sumTotalCount += count;

			List<Long> currSumList = countToSumListMap.get(count);
			if (currSumList == null) {
				currSumList = new ArrayList<Long>();
				currSumList.add(sum);
				countToSumListMap.put(count, currSumList);
			} else {
				if (!currSumList.contains(sum)) {
					currSumList.add(sum);
				}
			}
		}

		// ----------------------------------------------------
		// 总和值列表的中值索引 基于中值索引的向上百分比索引和向下百分比索引
		// ----------------------------------------------------
		int sumSize = sumList.size();
		int _10percentSize = (int) (BigDecimalUtil.rounding(sumSize * 0.1));
		int _20percentSize = (int) (BigDecimalUtil.rounding(sumSize * 0.2));
		int _30percentSize = (int) (BigDecimalUtil.rounding(sumSize * 0.3));
		int _34percentSize = (int) (BigDecimalUtil.rounding(sumSize * 0.34));

		int sumMidIndex = 0;
		int sumUp__10PercentIndex = 0;
		int sumDown10PercentIndex = 0;
		int sumUp__20PercentIndex = 0;
		int sumDown20PercentIndex = 0;
		int sumUp__30PercentIndex = 0;
		int sumDown30PercentIndex = 0;
		int sumUp__34PercentIndex = 0;
		int sumDown34PercentIndex = 0;

		if (sumSize % 2 == 0) {
			sumMidIndex = sumSize / 2 - 1;

			sumUp__10PercentIndex = sumMidIndex + 1 - _10percentSize;
			sumDown10PercentIndex = sumMidIndex + _10percentSize;

			sumUp__20PercentIndex = sumMidIndex + 1 - _20percentSize;
			sumDown20PercentIndex = sumMidIndex + _20percentSize;

			sumUp__30PercentIndex = sumMidIndex + 1 - _30percentSize;
			sumDown30PercentIndex = sumMidIndex + _30percentSize;

			sumUp__34PercentIndex = sumMidIndex + 1 - _34percentSize;
			sumDown34PercentIndex = sumMidIndex + _34percentSize;

		} else {
			sumMidIndex = (sumSize) / 2;

			sumUp__10PercentIndex = sumMidIndex - _10percentSize;
			sumDown10PercentIndex = sumMidIndex + _10percentSize;

			sumUp__20PercentIndex = sumMidIndex - _20percentSize;
			sumDown20PercentIndex = sumMidIndex + _20percentSize;

			sumUp__30PercentIndex = sumMidIndex - _30percentSize;
			sumDown30PercentIndex = sumMidIndex + _30percentSize;

			sumUp__34PercentIndex = sumMidIndex - _34percentSize;
			sumDown34PercentIndex = sumMidIndex + _34percentSize;
		}

		// ----------------------------------------------------
		// 总和值与其出现次数的Map
		// ----------------------------------------------------
		Map<Integer, Integer> distAvgToCountMap = new TreeMap<Integer, Integer>();
		for (Draw draw : draws) {
			DrawStat stat = draw.getStat();

			Integer distAvg = stat.get(DrawStat.PROP_DIST_AVG, Integer.class);
			Integer count = distAvgToCountMap.get(distAvg);
			if (count == null) {
				count = new Integer(1);
			} else {
				count = new Integer(count.intValue() + 1);
			}
			distAvgToCountMap.put(distAvg, count);
		}

		// ----------------------------------------------------
		// 不重复的总和值列表
		// ----------------------------------------------------
		List<Integer> distAvgList = new ArrayList<Integer>();
		for (Iterator<Integer> distAvgItor = distAvgToCountMap.keySet().iterator(); distAvgItor.hasNext();) {
			Integer distAvg = distAvgItor.next();
			distAvgList.add(distAvg);
		}

		// ----------------------------------------------------
		// 出现次数与总和值列表的Map(按出现次数排序)
		// 所有平均间距的全部重复次数
		// ----------------------------------------------------
		int distAvgTotalCount = 0;
		Map<Integer, List<Integer>> countToDistAvgListMap = new TreeMap<Integer, List<Integer>>();
		for (Iterator<Integer> distAvgItor = distAvgToCountMap.keySet().iterator(); distAvgItor.hasNext();) {
			Integer distAvg = distAvgItor.next();
			Integer count = distAvgToCountMap.get(distAvg);

			distAvgTotalCount += count;

			List<Integer> currDistAvgList = countToDistAvgListMap.get(count);
			if (currDistAvgList == null) {
				currDistAvgList = new ArrayList<Integer>();
				currDistAvgList.add(distAvg);
				countToDistAvgListMap.put(count, currDistAvgList);
			} else {
				if (!currDistAvgList.contains(distAvg)) {
					currDistAvgList.add(distAvg);
				}
			}
		}

		// ----------------------------------------------------
		// 保存统计结果
		// ----------------------------------------------------
		globalStat.put(DrawStat.PROP_SUM_LIST, sumList);
		globalStat.put(DrawStat.PROP_SUM_TO_COUNT_MAP, sumToCountMap);
		globalStat.put(DrawStat.PROP_COUNT_TO_SUM_LIST_MAP, countToSumListMap);
		globalStat.put(DrawStat.PROP_SUM_TOTAL_COUNT, new Integer(sumTotalCount));

		globalStat.put(DrawStat.PROP_SUM_MID_INDEX, sumMidIndex);
		globalStat.put(DrawStat.PROP_SUM_UP___10_PERCENT_INDEX, sumUp__10PercentIndex);
		globalStat.put(DrawStat.PROP_SUM_DOWN_10_PERCENT_INDEX, sumDown10PercentIndex);
		globalStat.put(DrawStat.PROP_SUM_UP___20_PERCENT_INDEX, sumUp__20PercentIndex);
		globalStat.put(DrawStat.PROP_SUM_DOWN_20_PERCENT_INDEX, sumDown20PercentIndex);
		globalStat.put(DrawStat.PROP_SUM_UP___30_PERCENT_INDEX, sumUp__30PercentIndex);
		globalStat.put(DrawStat.PROP_SUM_DOWN_30_PERCENT_INDEX, sumDown30PercentIndex);
		globalStat.put(DrawStat.PROP_SUM_UP___34_PERCENT_INDEX, sumUp__34PercentIndex);
		globalStat.put(DrawStat.PROP_SUM_DOWN_34_PERCENT_INDEX, sumDown34PercentIndex);

		globalStat.put(DrawStat.PROP_DIST_AVG_LIST, distAvgList);
		globalStat.put(DrawStat.PROP_DIST_AVG_TO_COUNT_MAP, distAvgToCountMap);
		globalStat.put(DrawStat.PROP_COUNT_TO_DIST_AVG_LIST_MAP, countToDistAvgListMap);
		globalStat.put(DrawStat.PROP_DIST_AVG_TOTAL_COUNT, new Integer(distAvgTotalCount));
	}

	@Override
	public void printResult(AnalysisContext context) {

		DrawStat globalStat = context.getGlobalStat();

		List<Integer> sumList = globalStat.get(DrawStat.PROP_SUM_LIST, List.class);
		Map<Integer, Integer> sumToCountMap = globalStat.get(DrawStat.PROP_SUM_TO_COUNT_MAP, Map.class);
		Map<Integer, List<Integer>> countToSumListMap = globalStat.get(DrawStat.PROP_COUNT_TO_SUM_LIST_MAP, Map.class);
		int sumTotalCount = globalStat.get(DrawStat.PROP_SUM_TOTAL_COUNT, Integer.class);

		int sumMidIndex = globalStat.get(DrawStat.PROP_SUM_MID_INDEX, Integer.class);
		int sumUp__10Index = globalStat.get(DrawStat.PROP_SUM_UP___10_PERCENT_INDEX, Integer.class);
		int sumDown10Index = globalStat.get(DrawStat.PROP_SUM_DOWN_10_PERCENT_INDEX, Integer.class);
		int sumUp__20Index = globalStat.get(DrawStat.PROP_SUM_UP___20_PERCENT_INDEX, Integer.class);
		int sumDown20Index = globalStat.get(DrawStat.PROP_SUM_DOWN_20_PERCENT_INDEX, Integer.class);
		int sumUp__30Index = globalStat.get(DrawStat.PROP_SUM_UP___30_PERCENT_INDEX, Integer.class);
		int sumDown30Index = globalStat.get(DrawStat.PROP_SUM_DOWN_30_PERCENT_INDEX, Integer.class);
		// int sumUp__34Index = globalStat.get(DrawStat.PROP_SUM_UP___34_PERCENT_INDEX, Integer.class);
		// int sumDown34Index = globalStat.get(DrawStat.PROP_SUM_DOWN_34_PERCENT_INDEX, Integer.class);

		System.out.println("<count>:<sum-list>:");
		System.out.println("--------------------------------------------------------------------------------------------------------");
		for (Iterator<Integer> countItor = countToSumListMap.keySet().iterator(); countItor.hasNext();) {
			Integer count = countItor.next();
			List<Integer> currSumList = countToSumListMap.get(count);

			String currSumListStr = null;
			try {
				Object[] objects = currSumList.toArray(new Object[currSumList.size()]);
				currSumListStr = Arrays.toString(objects);
			} catch (Exception e) {
				e.printStackTrace();
			}

			double percent = (int) (BigDecimalUtil.rounding(count * currSumList.size() * 100.00 / sumTotalCount, 2));

			System.out.println(count + " -> " + currSumListStr + " (" + percent + " %)");
		}
		System.out.println("--------------------------------------------------------------------------------------------------------");

		System.out.println();
		System.out.println("<sum>:<count> (size=" + sumList.size() + "):");
		System.out.println("--------------------------------------------------------------------------------------------------------");
		for (int i = 0; i < sumList.size(); i++) {
			Object sum = sumList.get(i);
			Integer count = sumToCountMap.get(sum);

			boolean isMidIndex = (i == sumMidIndex) ? true : false;
			boolean isUp__10Index = (i == sumUp__10Index) ? true : false;
			boolean isDown10Index = (i == sumDown10Index) ? true : false;
			boolean isUp__20Index = (i == sumUp__20Index) ? true : false;
			boolean isDown20Index = (i == sumDown20Index) ? true : false;
			boolean isUp__30Index = (i == sumUp__30Index) ? true : false;
			boolean isDown30Index = (i == sumDown30Index) ? true : false;

			// boolean isUp__34Index = (i == sumUp__34Index) ? true : false;
			// boolean isDown34Index = (i == sumDown34Index) ? true : false;
			// boolean within68Percent = (i >= sumUp34Index && i <= sumDown34Index) ? true : false;
			// boolean within20Percent = (i >= sumUp10Index && i <= sumDown10Index) ? true : false;

			if (isUp__30Index) {
				System.out.println("-------------------- up 30 percent --------------------");
			}

			if (isUp__20Index) {
				System.out.println("-------------------- up 20 percent --------------------");
			}

			if (isUp__10Index) {
				System.out.println("-------------------- up 10 percent --------------------");
			}

			if (isMidIndex) {
				System.out.println("--------------------   mid index   --------------------");
			}

			System.out.println("[" + sum + "] " + addSpace(count, false) + ">" + count);

			if (isMidIndex) {
				System.out.println("--------------------   mid index   --------------------");
			}

			if (isDown10Index) {
				System.out.println("-------------------- down 10 percent --------------------");
			}

			if (isDown20Index) {
				System.out.println("-------------------- down 20 percent --------------------");
			}

			if (isDown30Index) {
				System.out.println("-------------------- down 30 percent --------------------");
			}
		}
		System.out.println("--------------------------------------------------------------------------------------------------------");

		List<Integer> distAvgList = globalStat.get(DrawStat.PROP_DIST_AVG_LIST, List.class);
		Map<Integer, Integer> distAvgToCountMap = globalStat.get(DrawStat.PROP_DIST_AVG_TO_COUNT_MAP, Map.class);
		Map<Integer, List<Integer>> countToDistAvgListMap = globalStat.get(DrawStat.PROP_COUNT_TO_DIST_AVG_LIST_MAP, Map.class);
		int distAvgTotalCount = globalStat.get(DrawStat.PROP_DIST_AVG_TOTAL_COUNT, Integer.class);

		System.out.println();
		System.out.println("<count>:<dist-avg-list> map (size=" + countToDistAvgListMap.size() + "):");
		System.out.println("--------------------------------------------------------------------------------------------------------");
		for (Iterator<Integer> countItor = countToDistAvgListMap.keySet().iterator(); countItor.hasNext();) {
			Integer count = countItor.next();
			List<Integer> currDistAvgList = countToDistAvgListMap.get(count);
			String currDistAvgListStr = Arrays.toString(currDistAvgList.toArray(new Integer[currDistAvgList.size()]));

			double percent = (int) (BigDecimalUtil.rounding(count * currDistAvgList.size() * 100.00 / distAvgTotalCount, 2));

			System.out.println(count + " -> " + currDistAvgListStr + " (" + percent + " %)");
		}
		System.out.println("--------------------------------------------------------------------------------------------------------");

		System.out.println();
		System.out.println("<dist-avg>:<count> map:");
		System.out.println("平均距离次数统计");
		System.out.println("--------------------------------------------------------------------------------------------------------");
		for (int i = 0; i < distAvgList.size(); i++) {
			Integer distAvg = distAvgList.get(i);
			Integer count = distAvgToCountMap.get(distAvg);

			double percent = (int) (BigDecimalUtil.rounding(count * 100.00 / distAvgTotalCount, 2));

			System.out.println("[" + distAvg + "] " + "-> " + count + " (" + percent + " %)");
		}
		System.out.println("--------------------------------------------------------------------------------------------------------");
	}

}

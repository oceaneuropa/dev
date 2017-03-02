package org.plutus.lottery.powerball.updater;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.origin.common.util.BigDecimalUtil;
import org.plutus.lottery.powerball.Draw;
import org.plutus.lottery.powerball.DrawStat;
import org.plutus.lottery.powerball.StatUpdater;
import org.plutus.lottery.powerball.StatUpdaterRegistry;

public class NormalDistributionUpdater implements StatUpdater {

	public static String UPDATER_ID = "NormalDistribution";

	public static NormalDistributionUpdater INSTANCE = new NormalDistributionUpdater();

	public static void register() {
		StatUpdaterRegistry.getInstance().add(INSTANCE);
	}

	public static void unregister() {
		StatUpdaterRegistry.getInstance().remove(UPDATER_ID);
	}

	@Override
	public String getId() {
		return UPDATER_ID;
	}

	@Override
	public int getPriority() {
		return 2;
	}

	@Override
	public void update(DrawStat globalStat, List<Draw> draws) {

		// ----------------------------------------------------
		// 总和值与其出现次数的Map
		// ----------------------------------------------------
		Map<Integer, Integer> sumToCountMap = new TreeMap<Integer, Integer>();
		for (Draw draw : draws) {
			DrawStat stat = draw.getStat();

			Integer sum = stat.get(DrawStat.PROP_SUM, Integer.class);
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
		List<Integer> sumList = new ArrayList<Integer>();
		for (Iterator<Integer> sumItor = sumToCountMap.keySet().iterator(); sumItor.hasNext();) {
			Integer sum = sumItor.next();
			sumList.add(sum);
		}

		// ----------------------------------------------------
		// 出现次数与总和值列表的Map(按出现次数排序)
		// 所有总和的全部重复次数
		// ----------------------------------------------------
		int sumTotalCount = 0;
		Map<Integer, List<Integer>> countToSumListMap = new TreeMap<Integer, List<Integer>>();
		for (Iterator<Integer> sumItor = sumToCountMap.keySet().iterator(); sumItor.hasNext();) {
			Integer sum = sumItor.next();
			Integer count = sumToCountMap.get(sum);

			sumTotalCount += count;

			List<Integer> currSumList = countToSumListMap.get(count);
			if (currSumList == null) {
				currSumList = new ArrayList<Integer>();
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
		globalStat.set(DrawStat.PROP_SUM_LIST, sumList);
		globalStat.set(DrawStat.PROP_SUM_TO_COUNT_MAP, sumToCountMap);
		globalStat.set(DrawStat.PROP_COUNT_TO_SUM_LIST_MAP, countToSumListMap);
		globalStat.set(DrawStat.PROP_SUM_TOTAL_COUNT, new Integer(sumTotalCount));

		globalStat.set(DrawStat.PROP_SUM_MID_INDEX, sumMidIndex);
		globalStat.set(DrawStat.PROP_SUM_UP___10_PERCENT_INDEX, sumUp__10PercentIndex);
		globalStat.set(DrawStat.PROP_SUM_DOWN_10_PERCENT_INDEX, sumDown10PercentIndex);
		globalStat.set(DrawStat.PROP_SUM_UP___20_PERCENT_INDEX, sumUp__20PercentIndex);
		globalStat.set(DrawStat.PROP_SUM_DOWN_20_PERCENT_INDEX, sumDown20PercentIndex);
		globalStat.set(DrawStat.PROP_SUM_UP___30_PERCENT_INDEX, sumUp__30PercentIndex);
		globalStat.set(DrawStat.PROP_SUM_DOWN_30_PERCENT_INDEX, sumDown30PercentIndex);
		globalStat.set(DrawStat.PROP_SUM_UP___34_PERCENT_INDEX, sumUp__34PercentIndex);
		globalStat.set(DrawStat.PROP_SUM_DOWN_34_PERCENT_INDEX, sumDown34PercentIndex);

		globalStat.set(DrawStat.PROP_DIST_AVG_LIST, distAvgList);
		globalStat.set(DrawStat.PROP_DIST_AVG_TO_COUNT_MAP, distAvgToCountMap);
		globalStat.set(DrawStat.PROP_COUNT_TO_DIST_AVG_LIST_MAP, countToDistAvgListMap);
		globalStat.set(DrawStat.PROP_DIST_AVG_TOTAL_COUNT, new Integer(distAvgTotalCount));
	}

}

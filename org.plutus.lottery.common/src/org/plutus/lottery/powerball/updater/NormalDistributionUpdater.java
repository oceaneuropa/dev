package org.plutus.lottery.powerball.updater;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

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
		// 总和与其出现次数Map 总和最小值 总和最大值
		// ----------------------------------------------------
		Map<Integer, Integer> sumToCountMap = new TreeMap<Integer, Integer>();
		int minSumCount = -1;
		int maxSumCount = -1;
		if (!draws.isEmpty()) {
			minSumCount = Integer.MAX_VALUE;
			maxSumCount = Integer.MIN_VALUE;
		}
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

			if (sum < minSumCount) {
				minSumCount = sum;
			}
			if (sum > maxSumCount) {
				maxSumCount = sum;
			}
		}

		globalStat.set(DrawStat.PROP_SUM_TO_COUNT_MAP, sumToCountMap);
		globalStat.set(DrawStat.PROP_SUM_MIN_COUNT, minSumCount);
		globalStat.set(DrawStat.PROP_SUM_MAX_COUNT, maxSumCount);
	}

}

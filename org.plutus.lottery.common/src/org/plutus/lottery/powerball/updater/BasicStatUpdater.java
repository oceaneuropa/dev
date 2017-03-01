package org.plutus.lottery.powerball.updater;

import java.util.List;

import org.origin.common.util.StatUtil;
import org.plutus.lottery.powerball.Draw;
import org.plutus.lottery.powerball.DrawStat;
import org.plutus.lottery.powerball.StatUpdater;
import org.plutus.lottery.powerball.StatUpdaterRegistry;

public class BasicStatUpdater implements StatUpdater {

	public static String UPDATER_ID = "Basic";

	public static BasicStatUpdater INSTANCE = new BasicStatUpdater();

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
		return 1;
	}

	@Override
	public void update(DrawStat globalStat, List<Draw> draws) {
		for (Draw draw : draws) {
			DrawStat stat = draw.getStat();
			int n1 = draw.getNum1();
			int n2 = draw.getNum2();
			int n3 = draw.getNum3();
			int n4 = draw.getNum4();
			int n5 = draw.getNum5();

			// ----------------------------------------------------
			// 最大值 中值
			// ----------------------------------------------------
			int min = n1;
			int mid = n3;
			int max = n5;
			stat.set(DrawStat.PROP_MIN, min);
			stat.set(DrawStat.PROP_MID, mid);
			stat.set(DrawStat.PROP_MAX, max);

			// ----------------------------------------------------
			// 加和 均值
			// ----------------------------------------------------
			int sum = StatUtil.sum(n1, n2, n3, n4, n5);
			// int avg = (int) (sum / 5);
			int avg = (int) StatUtil.avg(2, n1, n2, n3, n4, n5);
			int avgToMidPercent = StatUtil.normalizeByPercenaget(avg, mid);

			stat.set(DrawStat.PROP_SUM, sum);
			stat.set(DrawStat.PROP_AVG, avg);
			stat.set(DrawStat.PROP_AVG_VS_MID_BY_PERCENTAGE, avgToMidPercent);
			stat.set(DrawStat.PROP_AVG_VS_MID_BY_PERCENTAGE_ZERO_BASED, avgToMidPercent - 100);

			// ----------------------------------------------------
			// 奇数个数 偶数个数
			// ----------------------------------------------------
			int oddCount = 0;
			int evenCount = 0;
			if (n1 % 2 == 0) {
				evenCount++;
			} else {
				oddCount++;
			}
			if (n2 % 2 == 0) {
				evenCount++;
			} else {
				oddCount++;
			}
			if (n3 % 2 == 0) {
				evenCount++;
			} else {
				oddCount++;
			}
			if (n4 % 2 == 0) {
				evenCount++;
			} else {
				oddCount++;
			}
			if (n5 % 2 == 0) {
				evenCount++;
			} else {
				oddCount++;
			}
			stat.set(DrawStat.PROP_ODD_COUNT, oddCount);
			stat.set(DrawStat.PROP_EVEN_COUNT, evenCount);

			// ----------------------------------------------------
			// 间距均值
			// ----------------------------------------------------
			int dist1 = n2 - n1;
			int dist2 = n3 - n2;
			int dist3 = n4 - n3;
			int dist4 = n5 - n4;
			int dist_avg = (int) StatUtil.avg(2, dist1, dist2, dist3, dist4);
			stat.set(DrawStat.PROP_DISTANCE_AVG, dist_avg);
		}
	}

}

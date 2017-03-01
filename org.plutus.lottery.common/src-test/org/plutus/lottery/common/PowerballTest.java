package org.plutus.lottery.common;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runners.MethodSorters;
import org.origin.common.util.SystemUtils;
import org.plutus.lottery.powerball.Draw;
import org.plutus.lottery.powerball.DrawReader;
import org.plutus.lottery.powerball.DrawStat;
import org.plutus.lottery.powerball.StatUpdater;
import org.plutus.lottery.powerball.StatUpdaterRegistry;
import org.plutus.lottery.powerball.updater.BasicStatUpdater;
import org.plutus.lottery.powerball.updater.NormalDistributionUpdater;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PowerballTest {

	protected File file;

	public PowerballTest() {
		setUp();
	}

	protected void setUp() {
		this.file = new File(SystemUtils.getUserDir(), "/doc/pb-winnums-text_02-22-2017.txt");
		BasicStatUpdater.register();
		NormalDistributionUpdater.register();
	}

	@Ignore
	@Test
	public void test001() {
		System.out.println("--- --- --- test001() --- --- ---");

		try {
			List<Draw> draws = DrawReader.read(this.file);
			System.out.println("draws.size() = " + draws.size());
			System.out.println("----------------------------------------------------------------------");
			for (Draw draw : draws) {
				System.out.println(draw);
			}
			System.out.println("----------------------------------------------------------------------");

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	@Test
	public void test002() {
		System.out.println("--- --- --- test002() --- --- ---");

		try {
			DrawStat globalStat = new DrawStat();
			List<Draw> draws = DrawReader.read(this.file);
			List<StatUpdater> updaters = StatUpdaterRegistry.getInstance().getUpdaters();
			for (StatUpdater updater : updaters) {
				updater.update(globalStat, draws);
			}

			// ----------------------------------------------------
			// Basic
			// ----------------------------------------------------
			for (Draw draw : draws) {
				String dateStr = draw.getDateString();
				String numsStr = draw.getNumsString(false);

				DrawStat stat = draw.getStat();
				int sum = stat.get(DrawStat.PROP_SUM, Integer.class);
				int mid = stat.get(DrawStat.PROP_MID, Integer.class);
				int avg = stat.get(DrawStat.PROP_AVG, Integer.class);
				int avgVsMidPercentZeroBased = stat.get(DrawStat.PROP_AVG_VS_MID_BY_PERCENTAGE_ZERO_BASED, Integer.class);
				int evenCount = stat.get(DrawStat.PROP_EVEN_COUNT, Integer.class);
				int oddCount = stat.get(DrawStat.PROP_ODD_COUNT, Integer.class);
				int dist_avg = stat.get(DrawStat.PROP_DISTANCE_AVG, Integer.class);

				String message = dateStr;
				message += " (" + numsStr + ")";
				message += "  sum:" + sum;
				// message += ", avg/mid(percent):" + avg + "/" + mid + "(" + avgVsMidPercent + ")";
				message += ",  avg/mid(percent):" + avgVsMidPercentZeroBased;
				message += ",  odd/even count:" + oddCount + "/" + evenCount;
				message += ",  avg distance:" + dist_avg;

				System.out.println(message);
			}

			// ----------------------------------------------------
			// Normal Distribution
			// ----------------------------------------------------
			Map<Integer, Integer> sumToCountMap = globalStat.get(DrawStat.PROP_SUM_TO_COUNT_MAP, Map.class);
			// int minSumCount = globalStat.get(DrawStat.PROP_SUM_MIN_COUNT, Integer.class);
			// int maxSumCount = globalStat.get(DrawStat.PROP_SUM_MAX_COUNT, Integer.class);

			System.out.println("sum-to-count map:");
			System.out.println("----------------------------------------------------");
			for (Iterator<Integer> sumItor = sumToCountMap.keySet().iterator(); sumItor.hasNext();) {
				Integer sum = sumItor.next();
				Integer count = sumToCountMap.get(sum);

				System.out.println("[" + sum + "] " + addSpace(count) + ">" + count);
			}
			System.out.println("----------------------------------------------------");

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param currCount
	 * @param minCount
	 * @param maxCount
	 * @param minRange
	 * @param maxRange
	 * @return
	 */
	protected String addSpace(int count) {
		String spaces = "";
		for (int i = 0; i < count; i++) {
			spaces = spaces + "  ";
		}
		return spaces;
	}

	public static void main(String[] args) {
		Result result = JUnitCore.runClasses(PowerballTest.class);
		System.out.println("--- --- --- PowerballTest.main() --- --- ---");
		for (Failure failure : result.getFailures()) {
			System.out.println(failure.toString());
		}
		System.out.println("result.wasSuccessful() = " + result.wasSuccessful());
	}

}

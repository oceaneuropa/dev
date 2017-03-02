package org.plutus.lottery.common;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
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
import org.origin.common.util.BigDecimalUtil;
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
				// int mid = stat.get(DrawStat.PROP_MID, Integer.class);
				// int avg = stat.get(DrawStat.PROP_AVG, Integer.class);
				int avgVsMidPercentZeroBased = stat.get(DrawStat.PROP_AVG_VS_MID_BY_PERCENTAGE, Integer.class);
				int evenCount = stat.get(DrawStat.PROP_EVEN_COUNT, Integer.class);
				int oddCount = stat.get(DrawStat.PROP_ODD_COUNT, Integer.class);
				int dist_avg = stat.get(DrawStat.PROP_DIST_AVG, Integer.class);

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
			{
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

				System.out.println();
				System.out.println("count-to-sum-list map (size=" + countToSumListMap.size() + "):");
				System.out.println("--------------------------------------------------------------------------------------------------------");
				for (Iterator<Integer> countItor = countToSumListMap.keySet().iterator(); countItor.hasNext();) {
					Integer count = countItor.next();
					List<Integer> currSumList = countToSumListMap.get(count);
					String currSumListStr = Arrays.toString(currSumList.toArray(new Integer[currSumList.size()]));
					System.out.println(count + " -> " + currSumListStr);
				}
				System.out.println("--------------------------------------------------------------------------------------------------------");

				System.out.println();
				System.out.println("sum-to-count map (size=" + sumList.size() + "):");
				System.out.println("--------------------------------------------------------------------------------------------------------");
				for (int i = 0; i < sumList.size(); i++) {
					Integer sum = sumList.get(i);
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
			}

			{
				List<Integer> distAvgList = globalStat.get(DrawStat.PROP_DIST_AVG_LIST, List.class);
				Map<Integer, Integer> distAvgToCountMap = globalStat.get(DrawStat.PROP_DIST_AVG_TO_COUNT_MAP, Map.class);
				Map<Integer, List<Integer>> countToDistAvgListMap = globalStat.get(DrawStat.PROP_COUNT_TO_DIST_AVG_LIST_MAP, Map.class);
				int distAvgTotalCount = globalStat.get(DrawStat.PROP_DIST_AVG_TOTAL_COUNT, Integer.class);

				System.out.println();
				System.out.println("count-to-dist-avg-list map (size=" + countToDistAvgListMap.size() + "):");
				System.out.println("--------------------------------------------------------------------------------------------------------");
				for (Iterator<Integer> countItor = countToDistAvgListMap.keySet().iterator(); countItor.hasNext();) {
					Integer count = countItor.next();
					List<Integer> currDistAvgList = countToDistAvgListMap.get(count);
					String currDistAvgListStr = Arrays.toString(currDistAvgList.toArray(new Integer[currDistAvgList.size()]));
					System.out.println(count + " -> " + currDistAvgListStr);
				}
				System.out.println("--------------------------------------------------------------------------------------------------------");

				System.out.println();
				System.out.println("dist-avg-to-count map (size=" + distAvgList.size() + "):");
				System.out.println("--------------------------------------------------------------------------------------------------------");
				for (int i = 0; i < distAvgList.size(); i++) {
					Integer distAvg = distAvgList.get(i);
					Integer count = distAvgToCountMap.get(distAvg);

					double percent = (int) (BigDecimalUtil.rounding(count * 100.00 / distAvgTotalCount, 2));

					System.out.println("[" + distAvg + "] " + ">" + count + " (" + percent + " %)");
				}
				System.out.println("--------------------------------------------------------------------------------------------------------");
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param count
	 * @param dashed
	 * @return
	 */
	protected String addSpace(int count, boolean dashed) {
		String spaces = "";
		for (int i = 0; i < count; i++) {
			if (dashed) {
				spaces = spaces + "---";
			} else {
				spaces = spaces + "   ";
			}
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

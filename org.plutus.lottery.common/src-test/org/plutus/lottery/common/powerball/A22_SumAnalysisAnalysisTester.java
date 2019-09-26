package org.plutus.lottery.common.powerball;

import java.io.File;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runners.MethodSorters;
import org.origin.common.util.BigDecimalUtil;
import org.origin.common.util.SystemUtils;
import org.plutus.lottery.powerball.DrawStat;
import org.plutus.lottery.util.DrawReaderV2;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class A22_SumAnalysisAnalysisTester extends AbstractAnalysisTester {

	public A22_SumAnalysisAnalysisTester() {
		// super(new File(SystemUtils.getUserDir(), "/doc/data/pb-winnums.txt"));
		super(DrawReaderV2.INSTANCE, new File(SystemUtils.getUserDir(), "/doc/data/DownloadAllNumbers.txt"));
	}

	@Test
	public void test001_readDraws() {
		super.test001_readDraws();
	}

	@Test
	public void test002_runAnalyses() {
		super.test002_runAnalyses();
	}

	@SuppressWarnings("unchecked")
	@Test
	public void test003_printStat() {
		System.out.println("--- --- --- test003_printStat() --- --- ---");

		System.out.println();
		System.out.println("////////////////////////////////////////");
		System.out.println("// SumAnalysis Test");
		System.out.println("////////////////////////////////////////");
		System.out.println();

		try {
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

		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println();
	}

	public static void main(String[] args) {
		Result result = JUnitCore.runClasses(A22_SumAnalysisAnalysisTester.class);
		System.out.println("--- --- --- A22_SumAnalysisAnalysisTester.main() --- --- ---");
		for (Failure failure : result.getFailures()) {
			System.out.println(failure.toString());
		}
		System.out.println("result.wasSuccessful() = " + result.wasSuccessful());
	}

}

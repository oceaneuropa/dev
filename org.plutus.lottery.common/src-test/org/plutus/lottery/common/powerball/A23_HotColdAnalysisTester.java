package org.plutus.lottery.common.powerball;

import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runners.MethodSorters;
import org.origin.common.util.SystemUtils;
import org.plutus.lottery.powerball.Draw;
import org.plutus.lottery.powerball.DrawStat;
import org.plutus.lottery.powerball.impl.DrawReaderV2;
import org.plutus.lottery.powerball.report.NumberReport;
import org.plutus.lottery.powerball.report.NumberReports;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class A23_HotColdAnalysisTester extends AbstractAnalysisTester {

	public A23_HotColdAnalysisTester() {
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
		System.out.println("// Basic Count");
		System.out.println("////////////////////////////////////////");
		System.out.println();

		try {
			DrawStat globalStat = context.getGlobalStat();
			List<Draw> draws = context.getDraws();

			// 1.平均值的次数
			{
				Map<Integer, Integer> avgToCountMap = globalStat.get(DrawStat.PROP_AVG_COUNT_MAP, Map.class);
				Map<Integer, Integer> avgToPercentMap = globalStat.get(DrawStat.PROP_AVG_PERCENT_MAP, Map.class);
				System.out.println("平均值的次数");
				System.out.println("Avg\tCount\t%");
				int totalCount = 0;
				for (Iterator<Integer> keyItor = avgToCountMap.keySet().iterator(); keyItor.hasNext();) {
					Integer avg = keyItor.next();
					int count = avgToCountMap.get(avg);
					int percent = avgToPercentMap.get(avg);
					totalCount += count;
					String message = avg + "\t[" + count + "]\t" + percent + "%";
					System.out.println(message);
				}
				System.out.println("(totalCount = " + totalCount + ")");
				System.out.println();
			}

			// 2.奇数偶数次数统计
			{
				Map<String, Integer> oddEvenToCountMap = globalStat.get(DrawStat.PROP_ODD_TO_EVEN_COUNT_MAP, Map.class);
				Map<String, Integer> oddEvenToPercentMap = globalStat.get(DrawStat.PROP_ODD_TO_EVEN_PERCENT_MAP, Map.class);
				System.out.println("奇数偶数次数统计");
				int totalCount = 0;
				for (Iterator<String> keyItor = oddEvenToCountMap.keySet().iterator(); keyItor.hasNext();) {
					String oddEvenKey = keyItor.next();
					int count = oddEvenToCountMap.get(oddEvenKey);
					int percent = oddEvenToPercentMap.get(oddEvenKey);
					totalCount += count;
					String message = oddEvenKey + " [" + count + "] " + percent + "%";
					System.out.println(message);
				}
				System.out.println("(totalCount = " + totalCount + ")");
				System.out.println();
			}

			// 3.最近30期重复最多数字(前10位)
			{
				System.out.println("最近30期重复最多数字(前10位)");
				for (Draw draw : draws) {
					String dateStr = draw.getDateString();
					String numsStr = draw.getNumsString(false);

					DrawStat stat = draw.getStat();
					// List<NumberReport> _10DrawNumberReports = stat.get(DrawStat.PROP_NUMBER_COUNTS_10DRAWS_FIRST10, List.class);
					// if (_10DrawNumberReports == null) {
					// continue;
					// }
					// List<NumberReport> _20DrawNumberReports = stat.get(DrawStat.PROP_NUMBER_COUNTS_20DRAWS_FIRST10, List.class);
					List<NumberReport> _30DrawNumberReports = stat.get(DrawStat.PROP_NUMBER_COUNTS_30DRAWS_FIRST10, List.class);
					if (_30DrawNumberReports == null) {
						continue;
					}

					String numberReportsString = NumberReports.toNumberReportsString(_30DrawNumberReports, true);

					String message = dateStr;
					message += " (" + numsStr + ")";
					message += "  most-repeated-in-30-draws: [ " + numberReportsString + " ]";

					System.out.println(message);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println();
	}

	public static void main(String[] args) {
		Result result = JUnitCore.runClasses(A23_HotColdAnalysisTester.class);
		System.out.println("--- --- --- A23_HotColdAnalysisTester.main() --- --- ---");
		for (Failure failure : result.getFailures()) {
			System.out.println(failure.toString());
		}
		System.out.println("result.wasSuccessful() = " + result.wasSuccessful());
	}

}

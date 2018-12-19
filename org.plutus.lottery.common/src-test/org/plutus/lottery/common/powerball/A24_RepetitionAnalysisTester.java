package org.plutus.lottery.common.powerball;

import java.io.File;
import java.text.MessageFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runners.MethodSorters;
import org.origin.common.util.Range;
import org.origin.common.util.StatUtil;
import org.origin.common.util.SystemUtils;
import org.plutus.lottery.powerball.DrawStat;
import org.plutus.lottery.powerball.impl.DrawReaderV2;
import org.plutus.lottery.powerball.report.CombinationReport;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class A24_RepetitionAnalysisTester extends AbstractAnalysisTester {

	public A24_RepetitionAnalysisTester() {
		// super(new File(SystemUtils.getUserDir(), "/doc/data/pb-winnums.txt"));
		super(DrawReaderV2.INSTANCE, new File(SystemUtils.getUserDir(), "/doc/data/DownloadAllNumbers.txt"));
	}

	@Override
	protected void setup() {
		super.setup();
		printDraws = true;
	}

	@Test
	public void test001_readDraws() {
		super.test001_readDraws();
	}

	@Test
	public void test002_runAnalyses() {
		super.test002_runAnalyses();
	}

	@SuppressWarnings({ "unchecked" })
	@Test
	public void test003_printStat() {
		System.out.println("--- --- --- test003_printStat() --- --- ---");

		System.out.println();
		System.out.println("////////////////////////////////////////");
		System.out.println("// Combination");
		System.out.println("////////////////////////////////////////");
		System.out.println();

		try {
			DrawStat globalStat = context.getGlobalStat();

			List<CombinationReport> combin3Reports = globalStat.get(DrawStat.PROP_COMBIN3_REPORTS, List.class);
			List<CombinationReport> combin4Reports = globalStat.get(DrawStat.PROP_COMBIN4_REPORTS, List.class);
			List<CombinationReport> combin5Reports = globalStat.get(DrawStat.PROP_COMBIN5_REPORTS, List.class);

			int combin3MinDistance = globalStat.get(DrawStat.PROP_COMBIN3_MIN_DISTANCE, Integer.class);
			int combin4MinDistance = globalStat.get(DrawStat.PROP_COMBIN4_MIN_DISTANCE, Integer.class);
			int combin5MinDistance = globalStat.get(DrawStat.PROP_COMBIN5_MIN_DISTANCE, Integer.class);

			int combin3MaxDistance = globalStat.get(DrawStat.PROP_COMBIN3_MAX_DISTANCE, Integer.class);
			int combin4MaxDistance = globalStat.get(DrawStat.PROP_COMBIN4_MAX_DISTANCE, Integer.class);
			int combin5MaxDistance = globalStat.get(DrawStat.PROP_COMBIN5_MAX_DISTANCE, Integer.class);

			int combin3AvgDistance = globalStat.get(DrawStat.PROP_COMBIN3_AVG_DISTANCE, Integer.class);
			int combin4AvgDistance = globalStat.get(DrawStat.PROP_COMBIN4_AVG_DISTANCE, Integer.class);
			int combin5AvgDistance = globalStat.get(DrawStat.PROP_COMBIN5_AVG_DISTANCE, Integer.class);

			Map<Range<Integer>, Integer> combin3RangeCountMap = globalStat.get(DrawStat.PROP_COMBIN3_RANGE_COUNT_MAP, Map.class);
			Map<Range<Integer>, Integer> combin4RangeCountMap = globalStat.get(DrawStat.PROP_COMBIN4_RANGE_COUNT_MAP, Map.class);
			Map<Range<Integer>, Integer> combin5RangeCountMap = globalStat.get(DrawStat.PROP_COMBIN5_RANGE_COUNT_MAP, Map.class);

			System.out.println("5重复数字的投注 (共" + combin5Reports.size() + "种组合)");
			for (CombinationReport report : combin5Reports) {
				System.out.println(report);
			}
			System.out.println();

			int combin5RangeCount = 0;
			for (Iterator<Range<Integer>> rangeItor = combin5RangeCountMap.keySet().iterator(); rangeItor.hasNext();) {
				Range<Integer> range = rangeItor.next();
				int count = combin5RangeCountMap.get(range);
				combin5RangeCount += count;
			}
			System.out.println("间隔范围计数(共" + combin5RangeCount + "个间隔)");
			for (Iterator<Range<Integer>> rangeItor = combin5RangeCountMap.keySet().iterator(); rangeItor.hasNext();) {
				Range<Integer> range = rangeItor.next();
				int count = combin5RangeCountMap.get(range);
				int percent = StatUtil.normalizeByPercentage(count, combin5RangeCount, 100);
				System.out.println(MessageFormat.format("{0} - [{1}次 或 {2}%]", new Object[] { range, count, percent }));
			}
			System.out.println();

			System.out.println("最小间隔:" + combin5MinDistance);
			System.out.println("最大间隔:" + combin5MaxDistance);
			System.out.println("平均间隔:" + combin5AvgDistance);
			System.out.println();

			System.out.println("4重复数字重复的投注 (共" + combin4Reports.size() + "种组合)");
			for (CombinationReport report : combin4Reports) {
				System.out.println(report);
			}
			System.out.println();

			int combin4RangeCount = 0;
			for (Iterator<Range<Integer>> rangeItor = combin4RangeCountMap.keySet().iterator(); rangeItor.hasNext();) {
				Range<Integer> range = rangeItor.next();
				int count = combin4RangeCountMap.get(range);
				combin4RangeCount += count;
			}
			System.out.println("间隔范围计数(共" + combin4RangeCount + "个间隔)");
			for (Iterator<Range<Integer>> rangeItor = combin4RangeCountMap.keySet().iterator(); rangeItor.hasNext();) {
				Range<Integer> range = rangeItor.next();
				int count = combin4RangeCountMap.get(range);
				int percent = StatUtil.normalizeByPercentage(count, combin4RangeCount, 100);
				System.out.println(MessageFormat.format("{0} - [{1}次 或 {2}%]", new Object[] { range, count, percent }));
			}
			System.out.println();

			System.out.println("最小间隔:" + combin4MinDistance);
			System.out.println("最大间隔:" + combin4MaxDistance);
			System.out.println("平均间隔:" + combin4AvgDistance);

			System.out.println();

			System.out.println("3个数字重复的投注 (" + combin3Reports.size() + "种组合)");
			for (CombinationReport report : combin3Reports) {
				System.out.println(report);
			}
			System.out.println();

			int combin3RangeCount = 0;
			for (Iterator<Range<Integer>> rangeItor = combin3RangeCountMap.keySet().iterator(); rangeItor.hasNext();) {
				Range<Integer> range = rangeItor.next();
				int count = combin3RangeCountMap.get(range);
				combin3RangeCount += count;
			}
			System.out.println("间隔范围计数(共" + combin3RangeCount + "个间隔)");
			for (Iterator<Range<Integer>> rangeItor = combin3RangeCountMap.keySet().iterator(); rangeItor.hasNext();) {
				Range<Integer> range = rangeItor.next();
				int count = combin3RangeCountMap.get(range);
				int percent = StatUtil.normalizeByPercentage(count, combin3RangeCount, 100);
				System.out.println(MessageFormat.format("{0} - [{1}次 或 {2}%]", new Object[] { range, count, percent }));
			}
			System.out.println();

			System.out.println("最小间隔:" + combin3MinDistance);
			System.out.println("最大间隔:" + combin3MaxDistance);
			System.out.println("平均间隔:" + combin3AvgDistance);

			System.out.println();
			System.out.println();

		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println();
	}

	public static void main(String[] args) {
		Result result = JUnitCore.runClasses(A24_RepetitionAnalysisTester.class);
		System.out.println("--- --- --- A24_RepetitionAnalysisTester.main() --- --- ---");
		for (Failure failure : result.getFailures()) {
			System.out.println(failure.toString());
		}
		System.out.println("result.wasSuccessful() = " + result.wasSuccessful());
	}

}

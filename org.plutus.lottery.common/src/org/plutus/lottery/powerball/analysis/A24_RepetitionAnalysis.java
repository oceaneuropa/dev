package org.plutus.lottery.powerball.analysis;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.origin.common.util.ArrayUtil;
import org.origin.common.util.CombinationUtil;
import org.origin.common.util.Range;
import org.origin.common.util.RangeConstants;
import org.origin.common.util.Ranges;
import org.origin.common.util.RangesUtil;
import org.origin.common.util.StatUtil;
import org.plutus.lottery.common.Comparators;
import org.plutus.lottery.powerball.Analysis;
import org.plutus.lottery.powerball.AnalysisContext;
import org.plutus.lottery.powerball.AnalysisRegistry;
import org.plutus.lottery.powerball.Draw;
import org.plutus.lottery.powerball.DrawStat;
import org.plutus.lottery.powerball.report.CombinationReport;

public class A24_RepetitionAnalysis implements Analysis {

	public static String ANALYSIS_ID = "RepetitionAnalysis";

	public static A24_RepetitionAnalysis INSTANCE = new A24_RepetitionAnalysis();

	@Override
	public String getId() {
		return ANALYSIS_ID;
	}

	@Override
	public int getPriority() {
		return 3;
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

		// ----------------------------------------------------
		// 重复3个数字，4个数字，5个数字的投注的统计
		// ----------------------------------------------------
		List<CombinationReport> combin3Reports = getCombinationReports(globalStat, draws, 3);
		List<CombinationReport> combin4Reports = getCombinationReports(globalStat, draws, 4);
		List<CombinationReport> combin5Reports = getCombinationReports(globalStat, draws, 5);

		// For a 3-numbers-combination report, if a 4-numbers-combination report contains the 3-numbers-combination,
		// remove the draws from the 3-numbers-combination report, if the draw exists in the 4-numbers-combination report.
		for (CombinationReport combin3Report : combin3Reports) {
			int[] combins3 = combin3Report.getCombination();
			for (CombinationReport combin4Report : combin4Reports) {
				int[] combins4 = combin4Report.getCombination();
				if (CombinationUtil.contains(combins4, combins3)) {
					combin3Report.removeDraws(combin4Report.getDraws());
				}
			}
		}

		// For a 4-numbers-combination report, if a 5-numbers-combination report contains the 4-numbers-combination,
		// remove the draws from the 4-numbers-combination report, if the draw exists in the 5-numbers-combination report.
		for (CombinationReport combin4Report : combin4Reports) {
			int[] combins4 = combin4Report.getCombination();
			for (CombinationReport combin5Report : combin5Reports) {
				int[] combins5 = combin5Report.getCombination();
				if (CombinationUtil.contains(combins5, combins4)) {
					combin4Report.removeDraws(combin5Report.getDraws());
				}
			}
		}

		// 除去重复次数等于或少于1的组合
		for (Iterator<CombinationReport> reportItor = combin3Reports.iterator(); reportItor.hasNext();) {
			if (reportItor.next().getCount() <= 1) {
				reportItor.remove();
			}
		}
		// 除去重复次数等于或少于1的组合
		for (Iterator<CombinationReport> reportItor = combin4Reports.iterator(); reportItor.hasNext();) {
			if (reportItor.next().getCount() <= 1) {
				reportItor.remove();
			}
		}

		Collections.sort(combin4Reports, Comparators.CombinationReportComparator.DESC);
		Collections.sort(combin3Reports, Comparators.CombinationReportComparator.DESC);

		// ----------------------------------------------------
		// 重复3个数字，4个数字，5个数字的投注期间隔
		// ----------------------------------------------------
		List<Integer> allCombin3Distances = new ArrayList<Integer>();
		for (CombinationReport combin3Report : combin3Reports) {
			List<Integer> combin3Distances = combin3Report.getNumberOfDrawsBetween();
			allCombin3Distances.addAll(combin3Distances);
		}
		int combin3MinDistance = StatUtil.min(allCombin3Distances);
		int combin3MaxDistance = StatUtil.max(allCombin3Distances);
		int combin3AvgDistance = (int) StatUtil.avg(0, allCombin3Distances);

		List<Integer> allCombin4Distances = new ArrayList<Integer>();
		for (CombinationReport combin4Report : combin4Reports) {
			List<Integer> combin4Distances = combin4Report.getNumberOfDrawsBetween();
			allCombin4Distances.addAll(combin4Distances);
		}
		int combin4MinDistance = StatUtil.min(allCombin4Distances);
		int combin4MaxDistance = StatUtil.max(allCombin4Distances);
		int combin4AvgDistance = (int) StatUtil.avg(0, allCombin4Distances);

		List<Integer> allCombin5Distances = new ArrayList<Integer>();
		for (CombinationReport combin5Report : combin5Reports) {
			List<Integer> combin5Distances = combin5Report.getNumberOfDrawsBetween();
			allCombin5Distances.addAll(combin5Distances);
		}
		int combin5MinDistance = StatUtil.min(allCombin5Distances);
		int combin5MaxDistance = StatUtil.max(allCombin5Distances);
		int combin5AvgDistance = (int) StatUtil.avg(0, allCombin5Distances);

		// ----------------------------------------------------
		// 重复3个数字的投注期间隔的范围统计
		// ----------------------------------------------------
		Ranges<Integer> ranges = new Ranges<Integer>();
		// ranges.add(RangeConstants.unlimited_0¢); // no need to have range (~, 0], since distance is at least 1.
		// ranges.add(RangeConstants.¢1_200c); // break down [1, 200) into [1, 25) [25, 50) [50, 100) [100, 200)
		ranges.add(RangeConstants.¢1_25c);
		ranges.add(RangeConstants.¢25_50c);
		ranges.add(RangeConstants.¢50_100c);
		ranges.add(RangeConstants.¢100_200c);
		ranges.add(RangeConstants.¢200_400c);
		ranges.add(RangeConstants.¢400_600c);
		ranges.add(RangeConstants.¢600_800c);
		ranges.add(RangeConstants.¢800_1000c);
		ranges.add(RangeConstants.¢1000_1200c);
		ranges.add(RangeConstants.¢1200_1400c);
		ranges.add(RangeConstants.¢1400_1600c);
		ranges.add(RangeConstants.¢1600_1800c);
		ranges.add(RangeConstants.¢1800_2000c);
		ranges.add(RangeConstants.¢2000_2200c);
		ranges.add(RangeConstants.¢2200_2400c);
		ranges.add(RangeConstants.¢2400_2600c);
		ranges.add(RangeConstants.¢2600_2800c);
		ranges.add(RangeConstants.¢2800_3000c);

		Map<Range<Integer>, Integer> combin3RangeCountMap = RangesUtil.getRangeCountMap(ranges, allCombin3Distances);
		Map<Range<Integer>, Integer> combin4RangeCountMap = RangesUtil.getRangeCountMap(ranges, allCombin4Distances);
		Map<Range<Integer>, Integer> combin5RangeCountMap = RangesUtil.getRangeCountMap(ranges, allCombin5Distances);

		// ----------------------------------------------------
		// 保存统计结果
		// ----------------------------------------------------
		globalStat.put(DrawStat.PROP_COMBIN3_REPORTS, combin3Reports);
		globalStat.put(DrawStat.PROP_COMBIN4_REPORTS, combin4Reports);
		globalStat.put(DrawStat.PROP_COMBIN5_REPORTS, combin5Reports);

		globalStat.put(DrawStat.PROP_COMBIN3_MIN_DISTANCE, combin3MinDistance);
		globalStat.put(DrawStat.PROP_COMBIN4_MIN_DISTANCE, combin4MinDistance);
		globalStat.put(DrawStat.PROP_COMBIN5_MIN_DISTANCE, combin5MinDistance);

		globalStat.put(DrawStat.PROP_COMBIN3_MAX_DISTANCE, combin3MaxDistance);
		globalStat.put(DrawStat.PROP_COMBIN4_MAX_DISTANCE, combin4MaxDistance);
		globalStat.put(DrawStat.PROP_COMBIN5_MAX_DISTANCE, combin5MaxDistance);

		globalStat.put(DrawStat.PROP_COMBIN3_AVG_DISTANCE, combin3AvgDistance);
		globalStat.put(DrawStat.PROP_COMBIN4_AVG_DISTANCE, combin4AvgDistance);
		globalStat.put(DrawStat.PROP_COMBIN5_AVG_DISTANCE, combin5AvgDistance);

		globalStat.put(DrawStat.PROP_COMBIN3_RANGE_COUNT_MAP, combin3RangeCountMap);
		globalStat.put(DrawStat.PROP_COMBIN4_RANGE_COUNT_MAP, combin4RangeCountMap);
		globalStat.put(DrawStat.PROP_COMBIN5_RANGE_COUNT_MAP, combin5RangeCountMap);
	}

	/**
	 * 
	 * @param globalStat
	 * @param draws
	 * @param r
	 * @return
	 */
	private List<CombinationReport> getCombinationReports(DrawStat globalStat, List<Draw> draws, int r) {
		List<CombinationReport> reports = new ArrayList<CombinationReport>();

		Map<String, CombinationReport> reportMap = new TreeMap<String, CombinationReport>();
		for (Draw draw : draws) {
			DrawStat stat = draw.getStat();
			int[] numbers = draw.getNumberArray(false);
			int n = numbers.length;

			// Get combinations of r numbers out of n numbers
			List<int[]> combinations = CombinationUtil.getCombinationsV2(numbers, n, r);

			// CombinationUtil.print(numbers, n);
			for (int[] combination : combinations) {
				// CombinationUtil.print(combination, r);
				String combinationKey = ArrayUtil.toArrayString(combination, " ");
				CombinationReport report = reportMap.get(combinationKey);
				if (report == null) {
					report = new CombinationReport(combinationKey, combination);
					reportMap.put(combinationKey, report);
				}
				report.addDraw(draw);
			}

			if (r == 3) {
				stat.put(DrawStat.PROP_COMBIN3_NUMS, combinations);
			} else if (r == 4) {
				stat.put(DrawStat.PROP_COMBIN4_NUMS, combinations);
			} else if (r == 5) {
				stat.put(DrawStat.PROP_COMBIN5_NUMS, combinations);
			}
		}

		for (Iterator<String> combinationKeyItor = reportMap.keySet().iterator(); combinationKeyItor.hasNext();) {
			String combinationKey = combinationKeyItor.next();
			CombinationReport report = reportMap.get(combinationKey);
			if (report.getCount() > 1) {
				reports.add(report);
			}
		}

		Collections.sort(reports, Comparators.CombinationReportComparator.DESC);

		return reports;
	}

}
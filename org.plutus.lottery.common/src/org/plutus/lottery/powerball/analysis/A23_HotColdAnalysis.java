package org.plutus.lottery.powerball.analysis;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.plutus.lottery.common.Analysis;
import org.plutus.lottery.common.AnalysisContext;
import org.plutus.lottery.common.AnalysisImpl;
import org.plutus.lottery.common.AnalysisRegistry;
import org.plutus.lottery.common.Draw;
import org.plutus.lottery.powerball.DrawStat;
import org.plutus.lottery.report.NumberReport;
import org.plutus.lottery.report.NumberReports;

public class A23_HotColdAnalysis extends AnalysisImpl implements Analysis {

	public static String ANALYSIS_ID = "HotColdAnalysis";

	public static A23_HotColdAnalysis INSTANCE = new A23_HotColdAnalysis();

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
		List<Draw> draws = context.getDraws();

		// ----------------------------------------------------
		// 最近10期重复最多数字(前10位)
		// 最近20期重复最多数字(前10位)
		// 最近30期重复最多数字(前10位)
		// ----------------------------------------------------
		// updateNumberCounts(draws, 10, 10, DrawStat.PROP_NUMBER_COUNTS_10DRAWS_FIRST10);
		// updateNumberCounts(draws, 20, 10, DrawStat.PROP_NUMBER_COUNTS_20DRAWS_FIRST10);
		updateNumberCounts(draws, 30, 10, DrawStat.PROP_NUMBER_COUNTS_30DRAWS_FIRST10);
	}

	/**
	 * 
	 * @param draws
	 * @param numOfDraws
	 * @param firstNNums
	 * @param propName
	 */
	protected void updateNumberCounts(List<Draw> draws, int numOfDraws, int firstNNums, String propName) {
		Map<Integer, NumberReports> indexToReportsMap = new LinkedHashMap<Integer, NumberReports>();
		int size = draws.size();

		int fromDrawIndex = 0;
		int toDrawIndex = fromDrawIndex - 1 + numOfDraws;

		int prevFromDrawIndex = -1;
		int prevToDrawIndex = -1;

		while (toDrawIndex < size) {
			Draw currDraw = draws.get(toDrawIndex);

			prevFromDrawIndex = fromDrawIndex - 1;
			prevToDrawIndex = toDrawIndex - 1;

			NumberReports currReports = new NumberReports(draws, fromDrawIndex, toDrawIndex);

			NumberReports prevReports = indexToReportsMap.get(prevToDrawIndex);
			if (prevReports != null) {
				Map<Integer, Integer> prevNumberToCountMap = prevReports.getNumberToCountMap();
				Draw prevFromDraw = draws.get(prevFromDrawIndex);
				currReports.calculate(prevNumberToCountMap, prevFromDraw, currDraw);
			} else {
				currReports.calculate();
			}

			Map<Integer, Integer> currNumberToCountMap = currReports.getNumberToCountMap();
			List<NumberReport> currNumberReports = NumberReports.convertToNumberReports(currNumberToCountMap, firstNNums);
			currDraw.getStat().put(propName, currNumberReports);

			indexToReportsMap.put(toDrawIndex, currReports);
			fromDrawIndex++;
			toDrawIndex++;
		}
	}

	@Override
	public void printResult(AnalysisContext context) {
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
	}

}

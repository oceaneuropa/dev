package org.plutus.lottery.powerball.analysis;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.plutus.lottery.powerball.Analysis;
import org.plutus.lottery.powerball.AnalysisContext;
import org.plutus.lottery.powerball.AnalysisRegistry;
import org.plutus.lottery.powerball.Draw;
import org.plutus.lottery.powerball.DrawStat;
import org.plutus.lottery.powerball.report.NumberReport;
import org.plutus.lottery.powerball.report.NumberReports;

public class A23_HotColdAnalysis implements Analysis {

	public static String ANALYSIS_ID = "HotColdAnalysis";

	public static A23_HotColdAnalysis INSTANCE = new A23_HotColdAnalysis();

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

}

package org.plutus.lottery.svg;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.origin.common.util.SystemUtils;
import org.plutus.lottery.powerball.AnalysisContext;
import org.plutus.lottery.powerball.AnalysisRegistry;
import org.plutus.lottery.powerball.Draw;
import org.plutus.lottery.powerball.DrawHelper;
import org.plutus.lottery.powerball.DrawStat;
import org.plutus.lottery.powerball.analysis.A11_MinMaxAvgAnalysis;
import org.plutus.lottery.powerball.analysis.A12_NumberDiffAnalysis;
import org.plutus.lottery.powerball.analysis.A13_RangeNormalizationAnalysis;
import org.plutus.lottery.powerball.analysis.A21_OddEvenAnalysis;
import org.plutus.lottery.powerball.analysis.A22_SumAnalysis;
import org.plutus.lottery.powerball.analysis.A23_HotColdAnalysis;
import org.plutus.lottery.powerball.analysis.A24_RepetitionAnalysis;
import org.plutus.lottery.powerball.impl.DrawReaderV2;

public class MainTest {

	static {
		A11_MinMaxAvgAnalysis.INSTANCE.register();
		A12_NumberDiffAnalysis.INSTANCE.register();
		A13_RangeNormalizationAnalysis.INSTANCE.register();
		A21_OddEvenAnalysis.INSTANCE.register();
		A22_SumAnalysis.INSTANCE.register();
		A23_HotColdAnalysis.INSTANCE.register();
		A24_RepetitionAnalysis.INSTANCE.register();
	}

	protected static List<Draw> getDraws() throws IOException {
		return DrawHelper.INSTANCE.read(DrawReaderV2.INSTANCE, new File(SystemUtils.getUserDir(), "/doc/data/DownloadAllNumbers.txt"));
	}

	public static void main(String[] args) {
		try {
			List<Draw> draws = getDraws();
			AnalysisContext context = new AnalysisContext();
			context.setDraws(draws);

			AnalysisRegistry.getInstance().run(context);
			AnalysisRegistry.getInstance().printResult(context);

			// Map<Integer, List<Draw>> yearToDraws = DrawHelper.INSTANCE.groupByYear(draws);
			// generateNumbersDiff(context, yearToDraws, SystemUtils.getUserDir(), "/doc/temp/powerball_draws__nums_diff__{0}.txt");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param yearToDraws
	 * @param baseFolder
	 * @param fileLocationPattern
	 */
	public static void generateNumbersDiff(AnalysisContext context, Map<Integer, List<Draw>> yearToDraws, File baseFolder, String fileLocationPattern) {
		DrawStat globalStat = context.getGlobalStat();

		// System.out.println("------------------------------------------------------------------------");
		// System.out.println("Diff Ranges");
		// System.out.println("------------------------------------------------------------------------");
		// int num1_diff_min = globalStat.get(DrawStat.NUM1_DIFF_MIN, Integer.class);
		// int num1_diff_max = globalStat.get(DrawStat.NUM1_DIFF_MAX, Integer.class);
		// int num2_diff_min = globalStat.get(DrawStat.NUM2_DIFF_MIN, Integer.class);
		// int num2_diff_max = globalStat.get(DrawStat.NUM2_DIFF_MAX, Integer.class);
		// int num3_diff_min = globalStat.get(DrawStat.NUM3_DIFF_MIN, Integer.class);
		// int num3_diff_max = globalStat.get(DrawStat.NUM3_DIFF_MAX, Integer.class);
		// int num4_diff_min = globalStat.get(DrawStat.NUM4_DIFF_MIN, Integer.class);
		// int num4_diff_max = globalStat.get(DrawStat.NUM4_DIFF_MAX, Integer.class);
		// int num5_diff_min = globalStat.get(DrawStat.NUM5_DIFF_MIN, Integer.class);
		// int num5_diff_max = globalStat.get(DrawStat.NUM5_DIFF_MAX, Integer.class);
		// int pb_diff_min = globalStat.get(DrawStat.PB_DIFF_MIN, Integer.class);
		// int pb_diff_max = globalStat.get(DrawStat.PB_DIFF_MAX, Integer.class);
		// System.out.println("Num1: [" + num1_diff_min + ", " + num1_diff_max + "]");
		// System.out.println("Num2: [" + num2_diff_min + ", " + num2_diff_max + "]");
		// System.out.println("Num3: [" + num3_diff_min + ", " + num3_diff_max + "]");
		// System.out.println("Num4: [" + num4_diff_min + ", " + num4_diff_max + "]");
		// System.out.println("Num5: [" + num5_diff_min + ", " + num5_diff_max + "]");
		// System.out.println("PB: [" + pb_diff_min + ", " + pb_diff_max + "]");
		// System.out.println();

		Integer max_avg = -1;
		Integer min_avg = 1000;
		for (Iterator<Integer> yearItor = yearToDraws.keySet().iterator(); yearItor.hasNext();) {
			Integer year = yearItor.next();
			List<Draw> draws = yearToDraws.get(year);

			for (Draw draw : draws) {
				// int drawId = draw.getDrawId();
				DrawStat stat = draw.getStat();
				Integer avg = stat.get(DrawStat.PROP_AVG, Integer.class);
				// String text = drawId + " " + avg;
				// System.out.println(text);
				if (avg > max_avg) {
					max_avg = avg;
				}
				if (avg < min_avg) {
					min_avg = avg;
				}
			}
		}
		System.out.println();
		System.out.println("max_avg = " + max_avg);
		System.out.println("min_avg = " + min_avg);
		System.out.println();

		for (Iterator<Integer> yearItor = yearToDraws.keySet().iterator(); yearItor.hasNext();) {
			Integer year = yearItor.next();
			List<Draw> draws = yearToDraws.get(year);

			System.out.println("------------------------------------------------------------------------");
			System.out.println(String.valueOf(year));
			System.out.println("------------------------------------------------------------------------");

			// int num1_diff_min = globalStat.get(year + "." + DrawStat.NUM1_DIFF_MIN, Integer.class);
			// int num1_diff_max = globalStat.get(year + "." + DrawStat.NUM1_DIFF_MAX, Integer.class);
			// int num2_diff_min = globalStat.get(year + "." + DrawStat.NUM2_DIFF_MIN, Integer.class);
			// int num2_diff_max = globalStat.get(year + "." + DrawStat.NUM2_DIFF_MAX, Integer.class);
			// int num3_diff_min = globalStat.get(year + "." + DrawStat.NUM3_DIFF_MIN, Integer.class);
			// int num3_diff_max = globalStat.get(year + "." + DrawStat.NUM3_DIFF_MAX, Integer.class);
			// int num4_diff_min = globalStat.get(year + "." + DrawStat.NUM4_DIFF_MIN, Integer.class);
			// int num4_diff_max = globalStat.get(year + "." + DrawStat.NUM4_DIFF_MAX, Integer.class);
			// int num5_diff_min = globalStat.get(year + "." + DrawStat.NUM5_DIFF_MIN, Integer.class);
			// int num5_diff_max = globalStat.get(year + "." + DrawStat.NUM5_DIFF_MAX, Integer.class);
			// int pb_diff_min = globalStat.get(year + "." + DrawStat.PB_DIFF_MIN, Integer.class);
			// int pb_diff_max = globalStat.get(year + "." + DrawStat.PB_DIFF_MAX, Integer.class);

			// System.out.println("Num1: [" + num1_diff_min + ", " + num1_diff_max + "]");
			// System.out.println("Num2: [" + num2_diff_min + ", " + num2_diff_max + "]");
			// System.out.println("Num3: [" + num3_diff_min + ", " + num3_diff_max + "]");
			// System.out.println("Num4: [" + num4_diff_min + ", " + num4_diff_max + "]");
			// System.out.println("Num5: [" + num5_diff_min + ", " + num5_diff_max + "]");
			// System.out.println("PB: [" + pb_diff_min + ", " + pb_diff_max + "]");
			// System.out.println();

			int size = draws.size();
			for (int i = 0; i < size; i++) {
				// if (i == size - 1) {
				// break;
				// }
				Draw draw = draws.get(i);
				DrawStat stat = draw.getStat();
				int drawId = draw.getDrawId();
				// int num1_diff = stat.get(DrawStat.NUM1_DIFF, Integer.class);
				// int num2_diff = stat.get(DrawStat.NUM2_DIFF, Integer.class);
				// int num3_diff = stat.get(DrawStat.NUM3_DIFF, Integer.class);
				// int num4_diff = stat.get(DrawStat.NUM4_DIFF, Integer.class);
				// int num5_diff = stat.get(DrawStat.NUM5_DIFF, Integer.class);
				// int pb_diff = stat.get(DrawStat.PB_DIFF, Integer.class);

				int avg = stat.get(DrawStat.PROP_AVG, Integer.class);

				// String text = drawId + " avg: " + avg + ", diff: " + num1_diff + ", " + num2_diff + ", " + num3_diff + ", " + num4_diff + ", " + num5_diff +
				// ", " + pb_diff;
				String text = drawId + " avg: " + avg;
				System.out.println(text);
			}
			System.out.println();
		}
	}

}

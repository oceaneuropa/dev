package org.plutus.lottery.powerball.analysis;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.plutus.lottery.powerball.Analysis;
import org.plutus.lottery.powerball.AnalysisContext;
import org.plutus.lottery.powerball.AnalysisRegistry;
import org.plutus.lottery.powerball.Draw;
import org.plutus.lottery.powerball.DrawHelper;
import org.plutus.lottery.powerball.DrawStat;
import org.plutus.lottery.powerball.impl.AnalysisImpl;

public class A12_NumberDiffAnalysis extends AnalysisImpl implements Analysis {

	public static String ANALYSIS_ID = "NumberDiffAnalysis";

	public static A12_NumberDiffAnalysis INSTANCE = new A12_NumberDiffAnalysis();

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
		return 1;
	}

	@Override
	public void run(AnalysisContext context) {
		DrawStat globalStat = context.getGlobalStat();
		List<Draw> draws = context.getDraws();

		int size = draws.size();
		for (int i = 0; i < size; i++) {
			Draw draw = draws.get(i);
			DrawStat stat = draw.getStat();

			if (i < size - 1) {
				Draw nextDraw = draws.get(i + 1);
				int num1_diff = nextDraw.getNum1() - draw.getNum1();
				int num2_diff = nextDraw.getNum2() - draw.getNum2();
				int num3_diff = nextDraw.getNum3() - draw.getNum3();
				int num4_diff = nextDraw.getNum4() - draw.getNum4();
				int num5_diff = nextDraw.getNum5() - draw.getNum5();
				int pb_diff = nextDraw.getPB() - draw.getPB();

				stat.put(DrawStat.NUM1_DIFF, num1_diff);
				stat.put(DrawStat.NUM2_DIFF, num2_diff);
				stat.put(DrawStat.NUM3_DIFF, num3_diff);
				stat.put(DrawStat.NUM4_DIFF, num4_diff);
				stat.put(DrawStat.NUM5_DIFF, num5_diff);
				stat.put(DrawStat.PB_DIFF, pb_diff);
			}
		}

		updateGlobalMinMaxDiffRange(globalStat, null, draws);

		Map<Integer, List<Draw>> yearToDraws = DrawHelper.INSTANCE.groupByYear(draws);
		for (Iterator<Integer> yearItor = yearToDraws.keySet().iterator(); yearItor.hasNext();) {
			Integer year = yearItor.next();
			List<Draw> currDraws = yearToDraws.get(year);
			updateGlobalMinMaxDiffRange(globalStat, year, currDraws);
		}
	}

	/**
	 * 
	 * @param globalStat
	 * @param year
	 * @param draws
	 */
	protected void updateGlobalMinMaxDiffRange(DrawStat globalStat, Integer year, List<Draw> draws) {
		int num1_diff_min = 1000;
		int num1_diff_max = -1000;
		int num2_diff_min = 1000;
		int num2_diff_max = -1000;
		int num3_diff_min = 1000;
		int num3_diff_max = -1000;
		int num4_diff_min = 1000;
		int num4_diff_max = -1000;
		int num5_diff_min = 1000;
		int num5_diff_max = -1000;
		int pb_diff_min = 1000;
		int pb_diff_max = -1000;

		int size = draws.size();
		for (int i = 0; i < size; i++) {
			if (i == size - 1) {
				break;
			}
			Draw draw = draws.get(i);
			DrawStat stat = draw.getStat();

			int num1_diff = stat.get(DrawStat.NUM1_DIFF, Integer.class);
			int num2_diff = stat.get(DrawStat.NUM2_DIFF, Integer.class);
			int num3_diff = stat.get(DrawStat.NUM3_DIFF, Integer.class);
			int num4_diff = stat.get(DrawStat.NUM4_DIFF, Integer.class);
			int num5_diff = stat.get(DrawStat.NUM5_DIFF, Integer.class);
			int pb_diff = stat.get(DrawStat.PB_DIFF, Integer.class);

			if (num1_diff < num1_diff_min) {
				num1_diff_min = num1_diff;
			}
			if (num1_diff > num1_diff_max) {
				num1_diff_max = num1_diff;
			}

			if (num2_diff < num2_diff_min) {
				num2_diff_min = num2_diff;
			}
			if (num2_diff > num2_diff_max) {
				num2_diff_max = num2_diff;
			}

			if (num3_diff < num3_diff_min) {
				num3_diff_min = num3_diff;
			}
			if (num3_diff > num3_diff_max) {
				num3_diff_max = num3_diff;
			}

			if (num4_diff < num4_diff_min) {
				num4_diff_min = num4_diff;
			}
			if (num4_diff > num4_diff_max) {
				num4_diff_max = num4_diff;
			}

			if (num5_diff < num5_diff_min) {
				num5_diff_min = num5_diff;
			}
			if (num5_diff > num5_diff_max) {
				num5_diff_max = num5_diff;
			}

			if (pb_diff < pb_diff_min) {
				pb_diff_min = pb_diff;
			}
			if (pb_diff > pb_diff_max) {
				pb_diff_max = pb_diff;
			}
		}

		if (year != null) {
			globalStat.put(year + "." + DrawStat.NUM1_DIFF_MIN, num1_diff_min);
			globalStat.put(year + "." + DrawStat.NUM1_DIFF_MAX, num1_diff_max);
			globalStat.put(year + "." + DrawStat.NUM2_DIFF_MIN, num2_diff_min);
			globalStat.put(year + "." + DrawStat.NUM2_DIFF_MAX, num2_diff_max);
			globalStat.put(year + "." + DrawStat.NUM3_DIFF_MIN, num3_diff_min);
			globalStat.put(year + "." + DrawStat.NUM3_DIFF_MAX, num3_diff_max);
			globalStat.put(year + "." + DrawStat.NUM4_DIFF_MIN, num4_diff_min);
			globalStat.put(year + "." + DrawStat.NUM4_DIFF_MAX, num4_diff_max);
			globalStat.put(year + "." + DrawStat.NUM5_DIFF_MIN, num5_diff_min);
			globalStat.put(year + "." + DrawStat.NUM5_DIFF_MAX, num5_diff_max);
			globalStat.put(year + "." + DrawStat.PB_DIFF_MIN, pb_diff_min);
			globalStat.put(year + "." + DrawStat.PB_DIFF_MAX, pb_diff_max);

		} else {
			globalStat.put(DrawStat.NUM1_DIFF_MIN, num1_diff_min);
			globalStat.put(DrawStat.NUM1_DIFF_MAX, num1_diff_max);

			globalStat.put(DrawStat.NUM2_DIFF_MIN, num2_diff_min);
			globalStat.put(DrawStat.NUM2_DIFF_MAX, num2_diff_max);

			globalStat.put(DrawStat.NUM3_DIFF_MIN, num3_diff_min);
			globalStat.put(DrawStat.NUM3_DIFF_MAX, num3_diff_max);

			globalStat.put(DrawStat.NUM4_DIFF_MIN, num4_diff_min);
			globalStat.put(DrawStat.NUM4_DIFF_MAX, num4_diff_max);

			globalStat.put(DrawStat.NUM5_DIFF_MIN, num5_diff_min);
			globalStat.put(DrawStat.NUM5_DIFF_MAX, num5_diff_max);

			globalStat.put(DrawStat.PB_DIFF_MIN, pb_diff_min);
			globalStat.put(DrawStat.PB_DIFF_MAX, pb_diff_max);
		}
	}

}

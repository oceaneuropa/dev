package org.plutus.lottery.powerball.analysis;

import java.util.Date;
import java.util.List;

import org.origin.common.util.BigDecimalUtil;
import org.plutus.lottery.common.Analysis;
import org.plutus.lottery.common.AnalysisContext;
import org.plutus.lottery.common.AnalysisImpl;
import org.plutus.lottery.common.AnalysisRegistry;
import org.plutus.lottery.common.Draw;
import org.plutus.lottery.powerball.DrawStat;

public class A13_RangeNormalizationAnalysis extends AnalysisImpl implements Analysis {

	public static String ANALYSIS_ID = "RangeNormalizationAnalysis";

	public static A13_RangeNormalizationAnalysis INSTANCE = new A13_RangeNormalizationAnalysis();

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
		for (Draw draw : draws) {
			DrawStat stat = draw.getStat();

			int index = draw.getIndex();
			Date date = draw.getDate();
			int n1 = draw.getNum1();
			int n2 = draw.getNum2();
			int n3 = draw.getNum3();
			int n4 = draw.getNum4();
			int n5 = draw.getNum5();
			int pb = draw.getPB();

			int range = n5 - n1 + 1;

			double n2_double = BigDecimalUtil.rounding((n2 - n1 + 1) / (double) range * (double) 100, 0);
			double n3_double = BigDecimalUtil.rounding((n3 - n1 + 1) / (double) range * (double) 100, 0);
			double n4_double = BigDecimalUtil.rounding((n4 - n1 + 1) / (double) range * (double) 100, 0);
			double n5_double = BigDecimalUtil.rounding((n5 - n1 + 1) / (double) range * (double) 100, 0);

			double pb_double = BigDecimalUtil.rounding(pb / (double) 26 * (double) 100, 0);

			int n1_int = 1;
			int n2_int = (int) n2_double;
			int n3_int = (int) n3_double;
			int n4_int = (int) n4_double;
			int n5_int = (int) n5_double;
			int pb_int = (int) pb_double;

			Draw ranged_draw = new Draw(date, n1_int, n2_int, n3_int, n4_int, n5_int, pb_int);
			ranged_draw.setIndex(index);

			stat.put(DrawStat.PROP_RANGE_1_TO_100, ranged_draw);
		}
	}

	@Override
	public void printResult(AnalysisContext context) {
		List<Draw> draws = context.getDraws();
		for (Draw draw : draws) {
			DrawStat stat = draw.getStat();
			Draw ranged_draw = stat.get(DrawStat.PROP_RANGE_1_TO_100, Draw.class);

			String str1 = draw.toString();
			String str2 = (ranged_draw != null) ? ranged_draw.toString() : "";

			System.out.println(str1 + "\t->\t" + str2);
		}
	}

}

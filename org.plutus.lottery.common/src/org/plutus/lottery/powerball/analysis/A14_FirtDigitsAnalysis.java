package org.plutus.lottery.powerball.analysis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.plutus.lottery.common.Analysis;
import org.plutus.lottery.common.AnalysisContext;
import org.plutus.lottery.common.AnalysisImpl;
import org.plutus.lottery.common.AnalysisRegistry;
import org.plutus.lottery.common.Draw;
import org.plutus.lottery.common.powerball.FirstDigitsCount;
import org.plutus.lottery.powerball.DrawStat;

public class A14_FirtDigitsAnalysis extends AnalysisImpl implements Analysis {

	public static String ANALYSIS_ID = "FirtDigitsAnalysis";

	public static A14_FirtDigitsAnalysis INSTANCE = new A14_FirtDigitsAnalysis();

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
		DrawStat globalStat = context.getGlobalStat();
		List<Draw> draws = context.getDraws();

		Map<String, Integer> firstDigitsToCountMap = new HashMap<String, Integer>();
		Map<String, Integer> firstDigitsToCountTreeMap = new HashMap<String, Integer>();

		for (Draw draw : draws) {
			int n1 = draw.getNum1();
			int n2 = draw.getNum2();
			int n3 = draw.getNum3();
			int n4 = draw.getNum4();
			int n5 = draw.getNum5();

			int n11 = n1 / 10;
			int n21 = n2 / 10;
			int n31 = n3 / 10;
			int n41 = n4 / 10;
			int n51 = n5 / 10;

			// int[] firstDigitsArray = new int[] { n11, n21, n31, n41, n51 };
			String firstDigits = n11 + "," + n21 + "," + n31 + "," + n41 + "," + n51;
			Integer count = firstDigitsToCountMap.get(firstDigits);
			if (count == null) {
				count = new Integer(0);
			}
			count = new Integer(count.intValue() + 1);
			firstDigitsToCountMap.put(firstDigits, new Integer(count));
		}

		List<FirstDigitsCount> firstDigitsCountList = new ArrayList<FirstDigitsCount>();

		for (Iterator<String> itor = firstDigitsToCountMap.keySet().iterator(); itor.hasNext();) {
			String firstDigits = itor.next();
			Integer count = firstDigitsToCountMap.get(firstDigits);
			firstDigitsToCountTreeMap.put(firstDigits, count);

			String[] array = firstDigits.split(",");
			int[] firstDigitsArray = new int[] { Integer.parseInt(array[0]), Integer.parseInt(array[1]), Integer.parseInt(array[2]), Integer.parseInt(array[3]), Integer.parseInt(array[4]) };
			firstDigitsCountList.add(new FirstDigitsCount(firstDigitsArray, firstDigits, count));
		}

		globalStat.put(DrawStat.PROP_FIRST_DIGITS_TO_COUNT_LIST, firstDigitsCountList);
		globalStat.put(DrawStat.PROP_FIRST_DIGITS_TO_COUNT_MAP, firstDigitsToCountMap);
		globalStat.put(DrawStat.PROP_FIRST_DIGITS_TO_COUNT_TREEMAP, firstDigitsToCountTreeMap);
	}

}

package org.plutus.lottery.powerball.analysis;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.origin.common.util.StatUtil;
import org.plutus.lottery.powerball.Analysis;
import org.plutus.lottery.powerball.AnalysisContext;
import org.plutus.lottery.powerball.AnalysisRegistry;
import org.plutus.lottery.powerball.Draw;
import org.plutus.lottery.powerball.DrawStat;
import org.plutus.lottery.powerball.impl.AnalysisImpl;

public class A21_OddEvenAnalysis extends AnalysisImpl implements Analysis {

	public static String ANALYSIS_ID = "OddEvenAnalysis";

	public static String _0_5 = "0/5";
	public static String _1_4 = "1/4";
	public static String _2_3 = "2/3";
	public static String _3_2 = "3/2";
	public static String _4_1 = "4/1";
	public static String _5_0 = "5/0";

	public static A21_OddEvenAnalysis INSTANCE = new A21_OddEvenAnalysis();

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

		for (Draw draw : draws) {
			DrawStat stat = draw.getStat();
			int n1 = draw.getNum1();
			int n2 = draw.getNum2();
			int n3 = draw.getNum3();
			int n4 = draw.getNum4();
			int n5 = draw.getNum5();

			// ----------------------------------------------------
			// 奇数个数 偶数个数
			// ----------------------------------------------------
			int oddCount = 0;
			int evenCount = 0;
			if (n1 % 2 == 0) {
				evenCount++;
			} else {
				oddCount++;
			}
			if (n2 % 2 == 0) {
				evenCount++;
			} else {
				oddCount++;
			}
			if (n3 % 2 == 0) {
				evenCount++;
			} else {
				oddCount++;
			}
			if (n4 % 2 == 0) {
				evenCount++;
			} else {
				oddCount++;
			}
			if (n5 % 2 == 0) {
				evenCount++;
			} else {
				oddCount++;
			}

			// ----------------------------------------------------
			// 保存统计结果
			// ----------------------------------------------------
			stat.put(DrawStat.PROP_ODD_COUNT, oddCount);
			stat.put(DrawStat.PROP_EVEN_COUNT, evenCount);
		}

		// ----------------------------------------------------
		// 奇数偶数比的重复次数
		// ----------------------------------------------------
		updateOddEvenCounts(globalStat, draws);
	}

	/**
	 * 
	 * @param globalStat
	 * @param draws
	 */
	protected void updateOddEvenCounts(DrawStat globalStat, List<Draw> draws) {
		Map<String, Integer> oddEvenToCountMap = new LinkedHashMap<String, Integer>();
		Map<String, Integer> oddEvenToPercentMap = new LinkedHashMap<String, Integer>();

		oddEvenToCountMap.put(_0_5, new Integer(0));
		oddEvenToCountMap.put(_1_4, new Integer(0));
		oddEvenToCountMap.put(_2_3, new Integer(0));
		oddEvenToCountMap.put(_3_2, new Integer(0));
		oddEvenToCountMap.put(_4_1, new Integer(0));
		oddEvenToCountMap.put(_5_0, new Integer(0));

		for (Draw draw : draws) {
			DrawStat stat = draw.getStat();

			int oddCount = stat.get(DrawStat.PROP_ODD_COUNT, Integer.class); // 奇数个数
			int evenCount = stat.get(DrawStat.PROP_EVEN_COUNT, Integer.class); // 偶数个数
			String oddEvenKey = getOddEvenKey(oddCount, evenCount);

			Integer count = oddEvenToCountMap.get(oddEvenKey);
			if (count == null) {
				count = new Integer(1);
			} else {
				count = new Integer(count.intValue() + 1);
			}
			oddEvenToCountMap.put(oddEvenKey, count);
		}

		int totalCount = 0;
		for (Iterator<String> keyItor = oddEvenToCountMap.keySet().iterator(); keyItor.hasNext();) {
			String oddEvenKey = keyItor.next();
			int count = oddEvenToCountMap.get(oddEvenKey);
			totalCount += count;
		}

		int _0_5_count = oddEvenToCountMap.get(_0_5);
		int _1_4_count = oddEvenToCountMap.get(_1_4);
		int _2_3_count = oddEvenToCountMap.get(_2_3);
		int _3_2_count = oddEvenToCountMap.get(_3_2);
		int _4_1_count = oddEvenToCountMap.get(_4_1);
		int _5_0_count = oddEvenToCountMap.get(_5_0);

		int _0_5_percent = StatUtil.normalizeByPercentage(_0_5_count, totalCount, 100);
		int _1_4_percent = StatUtil.normalizeByPercentage(_1_4_count, totalCount, 100);
		int _2_3_percent = StatUtil.normalizeByPercentage(_2_3_count, totalCount, 100);
		int _3_2_percent = StatUtil.normalizeByPercentage(_3_2_count, totalCount, 100);
		int _4_1_percent = StatUtil.normalizeByPercentage(_4_1_count, totalCount, 100);
		int _5_0_percent = StatUtil.normalizeByPercentage(_5_0_count, totalCount, 100);

		oddEvenToPercentMap.put(_0_5, _0_5_percent);
		oddEvenToPercentMap.put(_1_4, _1_4_percent);
		oddEvenToPercentMap.put(_2_3, _2_3_percent);
		oddEvenToPercentMap.put(_3_2, _3_2_percent);
		oddEvenToPercentMap.put(_4_1, _4_1_percent);
		oddEvenToPercentMap.put(_5_0, _5_0_percent);

		globalStat.put(DrawStat.PROP_ODD_TO_EVEN_COUNT_MAP, oddEvenToCountMap);
		globalStat.put(DrawStat.PROP_ODD_TO_EVEN_PERCENT_MAP, oddEvenToPercentMap);
	}

	/**
	 * 
	 * @param oddCount
	 * @param evenCount
	 * @return
	 */
	private String getOddEvenKey(int oddCount, int evenCount) {
		return String.valueOf(oddCount) + "/" + String.valueOf(evenCount);
	}

	@Override
	public void printResult(AnalysisContext context) {
		DrawStat globalStat = context.getGlobalStat();

		// 奇数偶数次数统计
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

		System.out.println();
		System.out.println("TotalCount is: " + totalCount);
	}

}
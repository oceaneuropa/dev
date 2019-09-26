package org.plutus.lottery.powerball.analysis;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.origin.common.util.StatUtil;
import org.plutus.lottery.common.Analysis;
import org.plutus.lottery.common.AnalysisContext;
import org.plutus.lottery.common.AnalysisImpl;
import org.plutus.lottery.common.AnalysisRegistry;
import org.plutus.lottery.common.Draw;
import org.plutus.lottery.powerball.DrawStat;

public class A11_MinMaxAvgAnalysis extends AnalysisImpl implements Analysis {

	public static String ANALYSIS_ID = "MinMaxAvgAnalysis";

	public static A11_MinMaxAvgAnalysis INSTANCE = new A11_MinMaxAvgAnalysis();

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

		List<Integer> n1_list = new ArrayList<Integer>();
		List<Integer> n2_list = new ArrayList<Integer>();
		List<Integer> n3_list = new ArrayList<Integer>();
		List<Integer> n4_list = new ArrayList<Integer>();
		List<Integer> n5_list = new ArrayList<Integer>();
		List<Integer> pb_list = new ArrayList<Integer>();

		for (Draw draw : draws) {
			DrawStat stat = draw.getStat();
			int n1 = draw.getNum1();
			int n2 = draw.getNum2();
			int n3 = draw.getNum3();
			int n4 = draw.getNum4();
			int n5 = draw.getNum5();
			int pb = draw.getPB();

			// ----------------------------------------------------
			// 最小值 最大值 中值
			// ----------------------------------------------------
			int min = n1;
			int mid = n3;
			int max = n5;

			// ----------------------------------------------------
			// 均值
			// ----------------------------------------------------
			int avg = (int) StatUtil.avg(2, n1, n2, n3, n4, n5);
			int avgToMidPercent = StatUtil.normalizeByPercentage(avg, mid, 100);

			// ----------------------------------------------------
			// 间距均值
			// ----------------------------------------------------
			// int dist1 = n2 - n1;
			// int dist2 = n3 - n2;
			// int dist3 = n4 - n3;
			// int dist4 = n5 - n4;
			// int dist_avg = (int) StatUtil.avg(2, dist1, dist2, dist3, dist4);
			int dist_avg = (int) StatUtil.diff_avg(2, n1, n2, n3, n4, n5);

			// ----------------------------------------------------
			// 单个数字的均值和标准差 part1
			// ----------------------------------------------------
			n1_list.add(n1);
			n2_list.add(n2);
			n3_list.add(n3);
			n4_list.add(n4);
			n5_list.add(n5);
			pb_list.add(pb);

			// ----------------------------------------------------
			// 保存统计结果
			// ----------------------------------------------------
			stat.put(DrawStat.PROP_MIN, min);
			stat.put(DrawStat.PROP_MID, mid);
			stat.put(DrawStat.PROP_MAX, max);
			stat.put(DrawStat.PROP_AVG, avg);
			stat.put(DrawStat.PROP_AVG_VS_MID_BY_PERCENTAGE, avgToMidPercent);
			stat.put(DrawStat.PROP_AVG_VS_MID_BY_PERCENTAGE_ZERO_BASED, avgToMidPercent - 100);
			stat.put(DrawStat.PROP_DIST_AVG, dist_avg);
		}

		// ----------------------------------------------------
		// 单个数字的均值和标准差 part2
		// ----------------------------------------------------
		int scale = 1;
		float n1_avg = StatUtil.getAvg(n1_list, scale);
		float n2_avg = StatUtil.getAvg(n2_list, scale);
		float n3_avg = StatUtil.getAvg(n3_list, scale);
		float n4_avg = StatUtil.getAvg(n4_list, scale);
		float n5_avg = StatUtil.getAvg(n5_list, scale);
		float pb_avg = StatUtil.getAvg(pb_list, scale);

		float n1_sd = StatUtil.getStandardDeviation(n1_list, scale);
		float n2_sd = StatUtil.getStandardDeviation(n2_list, scale);
		float n3_sd = StatUtil.getStandardDeviation(n3_list, scale);
		float n4_sd = StatUtil.getStandardDeviation(n4_list, scale);
		float n5_sd = StatUtil.getStandardDeviation(n5_list, scale);
		float pb_sd = StatUtil.getStandardDeviation(pb_list, scale);

		globalStat.put(DrawStat.NUM1_AVG, n1_avg);
		globalStat.put(DrawStat.NUM2_AVG, n2_avg);
		globalStat.put(DrawStat.NUM3_AVG, n3_avg);
		globalStat.put(DrawStat.NUM4_AVG, n4_avg);
		globalStat.put(DrawStat.NUM5_AVG, n5_avg);
		globalStat.put(DrawStat.PB_AVG, pb_avg);

		globalStat.put(DrawStat.NUM1_SD, n1_sd);
		globalStat.put(DrawStat.NUM2_SD, n2_sd);
		globalStat.put(DrawStat.NUM3_SD, n3_sd);
		globalStat.put(DrawStat.NUM4_SD, n4_sd);
		globalStat.put(DrawStat.NUM5_SD, n5_sd);
		globalStat.put(DrawStat.PB_SD, pb_sd);

		// ----------------------------------------------------
		// 平均值的重复次数
		// ----------------------------------------------------
		updateAvgCounts(globalStat, draws);
	}

	@Override
	public void printResult(AnalysisContext context) {
		System.out.println();
		System.out.println("////////////////////////////////////////");
		System.out.println("// MinMaxAvgAnalysis Test");
		System.out.println("////////////////////////////////////////");
		System.out.println();

		List<Draw> draws = context.getDraws();
		for (Draw draw : draws) {
			String dateStr = draw.getDateString();
			String numsStr = draw.getNumsString(false);

			DrawStat stat = draw.getStat();
			long sum = stat.get(DrawStat.PROP_SUM, Long.class);
			// int mid = stat.get(DrawStat.PROP_MID, Integer.class);
			// int avg = stat.get(DrawStat.PROP_AVG, Integer.class);
			int avgVsMidPercentZeroBased = stat.get(DrawStat.PROP_AVG_VS_MID_BY_PERCENTAGE, Integer.class);
			int oddCount = stat.get(DrawStat.PROP_ODD_COUNT, Integer.class);
			int evenCount = stat.get(DrawStat.PROP_EVEN_COUNT, Integer.class);
			int dist_avg = stat.get(DrawStat.PROP_DIST_AVG, Integer.class);

			String message = dateStr;
			message += " (" + numsStr + ")";
			message += "  sum:" + sum;
			// message += ", avg/mid(percent):" + avg + "/" + mid + "(" + avgVsMidPercent + ")";
			message += ",  avg/mid(percent):" + avgVsMidPercentZeroBased;
			message += ",  odd/even count:" + oddCount + "/" + evenCount;
			message += ",  avg distance:" + dist_avg;

			System.out.println(message);
		}

		DrawStat globalStat = context.getGlobalStat();
		float num1_avg = globalStat.get(DrawStat.NUM1_AVG, Float.class);
		float num2_avg = globalStat.get(DrawStat.NUM2_AVG, Float.class);
		float num3_avg = globalStat.get(DrawStat.NUM3_AVG, Float.class);
		float num4_avg = globalStat.get(DrawStat.NUM4_AVG, Float.class);
		float num5_avg = globalStat.get(DrawStat.NUM5_AVG, Float.class);
		float pb_avg = globalStat.get(DrawStat.PB_AVG, Float.class);

		float num1_sd = globalStat.get(DrawStat.NUM1_SD, Float.class);
		float num2_sd = globalStat.get(DrawStat.NUM2_SD, Float.class);
		float num3_sd = globalStat.get(DrawStat.NUM3_SD, Float.class);
		float num4_sd = globalStat.get(DrawStat.NUM4_SD, Float.class);
		float num5_sd = globalStat.get(DrawStat.NUM5_SD, Float.class);
		float pb_sd = globalStat.get(DrawStat.PB_SD, Float.class);

		System.out.println();
		System.out.println("单个数字平均值：");
		System.out.println("NUM1  NUM2  NUM3  NUM4  NUM5    PB");
		System.out.println(num1_avg + ", " + num2_avg + ", " + num3_avg + ", " + num4_avg + ", " + num5_avg + "    " + pb_avg);

		System.out.println();
		System.out.println("单个数字标准差：");
		System.out.println("NUM1  NUM2  NUM3  NUM4  NUM5    PB");
		System.out.println(" " + num1_sd + ", " + num2_sd + ", " + num3_sd + ", " + num4_sd + ", " + num5_sd + "    " + pb_sd);
	}

	/**
	 * 
	 * @param globalStat
	 * @param draws
	 */
	protected void updateAvgCounts(DrawStat globalStat, List<Draw> draws) {
		Map<Integer, Integer> avgToCountMap = new TreeMap<Integer, Integer>();
		Map<Integer, Integer> avgToPercentMap = new TreeMap<Integer, Integer>();

		for (Draw draw : draws) {
			DrawStat stat = draw.getStat();
			Integer avg = stat.get(DrawStat.PROP_AVG, Integer.class);

			Integer count = avgToCountMap.get(avg);
			if (count == null) {
				count = new Integer(1);
			} else {
				count = new Integer(count.intValue() + 1);
			}
			avgToCountMap.put(avg, count);
		}

		int totalCount = 0;
		for (Iterator<Integer> keyItor = avgToCountMap.keySet().iterator(); keyItor.hasNext();) {
			Integer avg = keyItor.next();
			int count = avgToCountMap.get(avg);
			totalCount += count;
		}
		for (Iterator<Integer> keyItor = avgToCountMap.keySet().iterator(); keyItor.hasNext();) {
			Integer avg = keyItor.next();
			int count = avgToCountMap.get(avg);

			int percent = StatUtil.normalizeByPercentage(count, totalCount, 100);
			avgToPercentMap.put(avg, percent);
		}

		globalStat.put(DrawStat.PROP_AVG_COUNT_MAP, avgToCountMap);
		globalStat.put(DrawStat.PROP_AVG_PERCENT_MAP, avgToPercentMap);
	}

}

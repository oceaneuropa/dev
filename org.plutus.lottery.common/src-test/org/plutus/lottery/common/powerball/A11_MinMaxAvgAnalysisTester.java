package org.plutus.lottery.common.powerball;

import java.io.File;
import java.util.List;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runners.MethodSorters;
import org.origin.common.util.SystemUtils;
import org.plutus.lottery.powerball.Draw;
import org.plutus.lottery.powerball.DrawStat;
import org.plutus.lottery.powerball.impl.DrawReaderV2;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class A11_MinMaxAvgAnalysisTester extends AbstractAnalysisTester {

	public A11_MinMaxAvgAnalysisTester() {
		// super(DrawReaderV1.INSTANCE, new File(SystemUtils.getUserDir(), "/doc/data/pb-winnums.txt"));
		super(DrawReaderV2.INSTANCE, new File(SystemUtils.getUserDir(), "/doc/data/DownloadAllNumbers.txt"));
	}

	@Override
	protected void setup() {
		super.setup();
		// printDraws = true;
	}

	@Test
	public void test001_readDraws() {
		super.test001_readDraws();
	}

	@Test
	public void test002_runAnalyses() {
		super.test002_runAnalyses();
	}

	@Test
	public void test003_printStat() {
		System.out.println("--- --- --- test003_printStat() --- --- ---");

		try {
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

		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println();
	}

	public static void main(String[] args) {
		Result result = JUnitCore.runClasses(A11_MinMaxAvgAnalysisTester.class);
		System.out.println("--- --- --- A11_MinMaxAvgAnalysisTester.main() --- --- ---");
		for (Failure failure : result.getFailures()) {
			System.out.println(failure.toString());
		}
		System.out.println("result.wasSuccessful() = " + result.wasSuccessful());
	}

}

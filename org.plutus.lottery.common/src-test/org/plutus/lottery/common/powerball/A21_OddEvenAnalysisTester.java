package org.plutus.lottery.common.powerball;

import java.io.File;
import java.util.Iterator;
import java.util.Map;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runners.MethodSorters;
import org.origin.common.util.SystemUtils;
import org.plutus.lottery.powerball.DrawStat;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class A21_OddEvenAnalysisTester extends AbstractAnalysisTester {

	public A21_OddEvenAnalysisTester() {
		super(new File(SystemUtils.getUserDir(), "/doc/pb-winnums-text_02-22-2017.txt"));
	}

	@Test
	@Override
	public void test001_readDraws() {
		super.test001_readDraws();
	}

	@Test
	@Override
	public void test002_runAnalyses() {
		super.test002_runAnalyses();
	}

	@SuppressWarnings("unchecked")
	@Test
	public void test003_print() {
		System.out.println("--- --- --- test003_print() --- --- ---");

		try {
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

		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println();
	}

	public static void main(String[] args) {
		// Run JUnit test
		Result result = JUnitCore.runClasses(A21_OddEvenAnalysisTester.class);

		// Print out JUnit test result
		System.out.println("---------------------------------------------");
		System.out.println("A21_OddEvenAnalysisTester.main()");
		for (Failure failure : result.getFailures()) {
			System.out.println(failure.toString());
		}
		System.out.println("result.wasSuccessful() : " + result.wasSuccessful());
		System.out.println("---------------------------------------------");
	}

}
package org.plutus.lottery.common.powerball;

import java.io.File;
import java.util.Collections;
import java.util.List;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runners.MethodSorters;
import org.origin.common.util.SystemUtils;
import org.plutus.lottery.powerball.DrawStat;
import org.plutus.lottery.util.Comparators.FirstDigitsCountCountComparator;
import org.plutus.lottery.util.DrawReaderV2;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class A14_FirstDigitsAnalysisTester extends AbstractAnalysisTester {

	public A14_FirstDigitsAnalysisTester() {
		// super(DrawReaderV1.INSTANCE, new File(SystemUtils.getUserDir(), "/doc/data/pb-winnums.txt"));
		super(DrawReaderV2.INSTANCE, new File(SystemUtils.getUserDir(), "/doc/data/DownloadAllNumbers.txt"));
	}

	@Override
	protected void setup() {
		super.setup();
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
			System.out.println("// A14_FirstDigitsAnalysisTester");
			System.out.println("////////////////////////////////////////");
			System.out.println();

			List<FirstDigitsCount> firstDigitsCountList = context.getGlobalStat().get(DrawStat.PROP_FIRST_DIGITS_TO_COUNT_LIST, List.class);
			// Map<String, Integer> firstDigitsToCountMap = context.getGlobalStat().get(DrawStat.PROP_FIRST_DIGITS_TO_COUNT_MAP, Map.class);
			// Map<String, Integer> firstDigitsToCountTreeMap = context.getGlobalStat().get(DrawStat.PROP_FIRST_DIGITS_TO_COUNT_TREEMAP, Map.class);

			Collections.sort(firstDigitsCountList, FirstDigitsCountCountComparator.DESC);
			System.out.println("size: " + firstDigitsCountList.size());
			System.out.println("-----------------------------------------");
			for (FirstDigitsCount firstDigitsCount : firstDigitsCountList) {
				String firstDigits = firstDigitsCount.getFirstDigits();
				Integer count = firstDigitsCount.getCount();
				System.out.println(firstDigits + " -> " + count);
			}
			System.out.println("-----------------------------------------");
			System.out.println();
			System.out.println();

			// System.out.println("-----------------------------------------");
			// for (Iterator<String> itor = firstDigitsToCountMap.keySet().iterator(); itor.hasNext();) {
			// String firstDigits = itor.next();
			// Integer count = firstDigitsToCountMap.get(firstDigits);
			// System.out.println(firstDigits + " -> " + count);
			// }
			// System.out.println("-----------------------------------------");
			// System.out.println();
			// System.out.println();

			// System.out.println("-----------------------------------------");
			// for (Iterator<String> itor = firstDigitsToCountTreeMap.keySet().iterator(); itor.hasNext();) {
			// String firstDigits = itor.next();
			// Integer count = firstDigitsToCountTreeMap.get(firstDigits);
			// System.out.println(firstDigits + " -> " + count);
			// }
			// System.out.println("-----------------------------------------");
			// System.out.println();
			// System.out.println();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println();
	}

	public static void main(String[] args) {
		Result result = JUnitCore.runClasses(A14_FirstDigitsAnalysisTester.class);
		System.out.println("--- --- --- A14_FirstDigitsAnalysisTester.main() --- --- ---");
		for (Failure failure : result.getFailures()) {
			System.out.println(failure.toString());
		}
		System.out.println("result.wasSuccessful() = " + result.wasSuccessful());
	}

}

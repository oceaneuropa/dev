package org.origin.common.util.test;

import java.text.MessageFormat;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runners.MethodSorters;
import org.origin.common.util.Range;
import org.origin.common.util.Ranges;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class RangeTest {

	/**
	 * For every test method, a new RangeTest instance is created by junit.
	 */
	public RangeTest() {
	}

	protected void setup() {
	}

	@Test
	public void test01_rangesTest() {
		System.out.println("--- --- --- rangesTest() --- --- ---");

		try {
			Ranges<Integer> ranges = new Ranges<Integer>();
			ranges.setOverlapAllowed(true);

			ranges.add(new Range<Integer>(null, false, true, -10, false, false)); // (~, -10)
			ranges.add(new Range<Integer>(1, false, false, 3, true, false)); // (1, 3]
			ranges.add(new Range<Integer>(4, true, false, 6, false, false)); // [4, 6)
			ranges.add(new Range<Integer>(7, true, false, 8, true, false)); // [7, 8]
			ranges.add(new Range<Integer>(8, true, false, 9, true, false)); // [8, 9]
			ranges.add(new Range<Integer>(15, true, false, null, false, true)); // [15, ~)

			System.out.println("Ranges are: " + ranges);
			System.out.println();

			containsValueTest(ranges, -200);
			containsValueTest(ranges, -11);
			containsValueTest(ranges, -10);
			containsValueTest(ranges, -9);

			System.out.println();

			containsValueTest(ranges, 1);
			containsValueTest(ranges, 2);
			containsValueTest(ranges, 3);
			containsValueTest(ranges, 4);
			containsValueTest(ranges, 5);
			containsValueTest(ranges, 6);
			containsValueTest(ranges, 7);
			containsValueTest(ranges, 8);
			containsValueTest(ranges, 9);

			System.out.println();

			containsValueTest(ranges, 14);
			containsValueTest(ranges, 15);
			containsValueTest(ranges, 16);
			containsValueTest(ranges, 200);

		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println();
	}

	/**
	 * 
	 * @param ranges
	 * @param value
	 */
	protected void containsValueTest(Ranges<Integer> ranges, Integer value) {
		System.out.println(MessageFormat.format("Ranges {0} contains value <{1}> : {2}", new Object[] { ranges, value, (ranges.containsValue(value) ? true : false) }));
	}

	@Test
	public void test02_overlapTest() {
		System.out.println("--- --- --- overlapTest() --- --- ---");

		try {
			// (3, 4) vs (1, 6)
			overlapTest(new Range<Integer>(3, false, false, 4, false, false), new Range<Integer>(1, false, false, 6, false, false));
			// (1, 5) vs (2, 4)
			overlapTest(new Range<Integer>(1, false, false, 5, false, false), new Range<Integer>(2, false, false, 4, false, false));
			// (3, 5) vs (3, 5)
			overlapTest(new Range<Integer>(3, false, false, 5, false, false), new Range<Integer>(3, false, false, 5, false, false));
			// (3, 5) vs (4, 6)
			overlapTest(new Range<Integer>(3, false, false, 5, false, false), new Range<Integer>(4, false, false, 6, false, false));

			System.out.println();

			// (1, 3) vs (6, 8)
			overlapTest(new Range<Integer>(1, false, false, 3, false, false), new Range<Integer>(6, false, false, 8, false, false));
			// (1, 5) vs (5, 8)
			overlapTest(new Range<Integer>(1, false, false, 5, false, false), new Range<Integer>(5, false, false, 8, false, false));
			// (1, 5] vs (5, 8)
			overlapTest(new Range<Integer>(1, false, false, 5, true, false), new Range<Integer>(5, false, false, 8, false, false));
			// (1, 5) vs [5, 8)
			overlapTest(new Range<Integer>(1, false, false, 5, false, false), new Range<Integer>(5, true, false, 8, false, false));
			// (1, 5] vs [5, 8)
			overlapTest(new Range<Integer>(1, false, false, 5, true, false), new Range<Integer>(5, true, false, 8, false, false));

			System.out.println();

			// (3, ~) vs (3, ~)
			overlapTest(new Range<Integer>(3, false, false, null, false, true), new Range<Integer>(3, false, false, null, false, true));
			// (1, ~) vs (6, ~)
			overlapTest(new Range<Integer>(1, false, false, null, false, true), new Range<Integer>(6, false, false, null, false, true));
			// (~, 1) vs (~, 8)
			overlapTest(new Range<Integer>(null, false, true, 1, false, false), new Range<Integer>(null, false, true, 8, false, false));
			// (3, ~) vs (~, 4)
			overlapTest(new Range<Integer>(3, false, false, null, false, true), new Range<Integer>(null, false, true, 4, false, false));
			// (~, 6) vs (5, ~)
			overlapTest(new Range<Integer>(null, false, true, 6, false, false), new Range<Integer>(5, false, false, null, false, true));
			// (3, ~) vs (~, 3)
			overlapTest(new Range<Integer>(3, false, false, null, false, true), new Range<Integer>(null, false, true, 3, false, false));
			// (~, 3) vs (3, ~)
			overlapTest(new Range<Integer>(null, false, true, 3, false, false), new Range<Integer>(3, false, false, null, false, true));
			// (~, 1) vs (5, ~)
			overlapTest(new Range<Integer>(null, false, true, 1, false, false), new Range<Integer>(5, false, false, null, false, true));

			System.out.println();

			// (~, ~) vs (~, ~)
			overlapTest(new Range<Integer>(null, false, true, null, false, true), new Range<Integer>(null, false, true, null, false, true));

			System.out.println();

		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println();
	}

	/**
	 * 
	 * @param r1
	 * @param r2
	 */
	protected void overlapTest(Range<Integer> r1, Range<Integer> r2) {
		System.out.println(MessageFormat.format("{0} overlap with {1} : {2}", new Object[] { r1, r2, r1.overlap(r2) }));
	}

	public static void main2(String[] args) {
		Result result = JUnitCore.runClasses(RangeTest.class);
		System.out.println("--- --- --- RangeTest.main() --- --- ---");
		for (Failure failure : result.getFailures()) {
			System.out.println(failure.toString());
		}
		System.out.println("result.wasSuccessful() = " + result.wasSuccessful());
	}

}

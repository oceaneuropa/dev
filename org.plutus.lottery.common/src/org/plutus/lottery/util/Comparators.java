package org.plutus.lottery.util;

import java.util.Date;

import org.origin.common.util.BaseComparator;
import org.plutus.lottery.common.Analysis;
import org.plutus.lottery.common.Draw;
import org.plutus.lottery.common.powerball.FirstDigitsCount;
import org.plutus.lottery.report.CombinationReport;
import org.plutus.lottery.report.NumberReport;

public class Comparators {

	/**
	 * Compare Draw objects
	 *
	 */
	public static class DrawComparator extends BaseComparator<Draw> {
		public static DrawComparator ASC = new DrawComparator(SORT_ASC);
		public static DrawComparator DESC = new DrawComparator(SORT_DESC);

		public DrawComparator() {
			this(SORT_ASC);
		}

		public DrawComparator(String sort) {
			this.sort = check(sort);
		}

		@Override
		public int compare(Draw o1, Draw o2) {
			if (desc()) {
				Draw tmp = o1;
				o1 = o2;
				o2 = tmp;
			}

			Date date1 = o1.getDate();
			Date date2 = o2.getDate();

			if (date1 == null) {
				if (date2 == null) {
					return -1;
				} else {
					return 1;
				}
			} else {
				if (date2 == null) {
					return -1;
				} else {
					return date1.compareTo(date2);
				}
			}
		}
	}

	public static class StatAnalysisComparator extends BaseComparator<Analysis> {
		public static StatAnalysisComparator ASC = new StatAnalysisComparator(SORT_ASC);
		public static StatAnalysisComparator DESC = new StatAnalysisComparator(SORT_DESC);

		public StatAnalysisComparator() {
			this(SORT_ASC);
		}

		public StatAnalysisComparator(String sort) {
			this.sort = check(sort);
		}

		@Override
		public int compare(Analysis o1, Analysis o2) {
			if (desc()) {
				Analysis tmp = o1;
				o1 = o2;
				o2 = tmp;
			}

			int priority1 = o1.getPriority();
			int priority2 = o2.getPriority();

			return priority1 - priority2;
		}
	}

	public static class NumberReportCountComparator extends BaseComparator<NumberReport> {
		public static NumberReportCountComparator ASC = new NumberReportCountComparator(SORT_ASC);
		public static NumberReportCountComparator DESC = new NumberReportCountComparator(SORT_DESC);

		public NumberReportCountComparator() {
			this(SORT_ASC);
		}

		public NumberReportCountComparator(String sort) {
			this.sort = check(sort);
		}

		@Override
		public int compare(NumberReport r1, NumberReport r2) {
			if (desc()) {
				NumberReport tmp = r1;
				r1 = r2;
				r2 = tmp;
			}

			int count1 = r1.getCount();
			int count2 = r2.getCount();

			return count1 - count2;
		}
	}

	public static class CombinationReportComparator extends BaseComparator<CombinationReport> {
		public static CombinationReportComparator ASC = new CombinationReportComparator(SORT_ASC);
		public static CombinationReportComparator DESC = new CombinationReportComparator(SORT_DESC);

		public CombinationReportComparator() {
			this(SORT_ASC);
		}

		public CombinationReportComparator(String sort) {
			this.sort = check(sort);
		}

		@Override
		public int compare(CombinationReport r1, CombinationReport r2) {
			if (desc()) {
				CombinationReport tmp = r1;
				r1 = r2;
				r2 = tmp;
			}

			int count1 = r1.getCount();
			int count2 = r2.getCount();

			if (count1 != count2) {
				return count1 - count2;
			}

			String key1 = r1.getCombinationKey();
			String key2 = r2.getCombinationKey();
			return key1.compareTo(key2);
		}
	}

	public static class FirstDigitsCountDigitsComparator extends BaseComparator<FirstDigitsCount> {
		public static FirstDigitsCountDigitsComparator ASC = new FirstDigitsCountDigitsComparator(SORT_ASC);
		public static FirstDigitsCountDigitsComparator DESC = new FirstDigitsCountDigitsComparator(SORT_DESC);

		public FirstDigitsCountDigitsComparator() {
			this(SORT_ASC);
		}

		public FirstDigitsCountDigitsComparator(String sort) {
			this.sort = check(sort);
		}

		@Override
		public int compare(FirstDigitsCount obj1, FirstDigitsCount obj2) {
			if (desc()) {
				FirstDigitsCount tmp = obj1;
				obj1 = obj2;
				obj2 = tmp;
			}
			String digits1 = obj1.getFirstDigits();
			String digits2 = obj2.getFirstDigits();
			return digits1.compareTo(digits2);
		}
	}

	public static class FirstDigitsCountCountComparator extends BaseComparator<FirstDigitsCount> {
		public static FirstDigitsCountCountComparator ASC = new FirstDigitsCountCountComparator(SORT_ASC);
		public static FirstDigitsCountCountComparator DESC = new FirstDigitsCountCountComparator(SORT_DESC);

		public FirstDigitsCountCountComparator() {
			this(SORT_ASC);
		}

		public FirstDigitsCountCountComparator(String sort) {
			this.sort = check(sort);
		}

		@Override
		public int compare(FirstDigitsCount obj1, FirstDigitsCount obj2) {
			FirstDigitsCount obj1Origin = obj1;
			FirstDigitsCount obj2Origin = obj2;

			if (desc()) {
				FirstDigitsCount tmp = obj1;
				obj1 = obj2;
				obj2 = tmp;
			}
			int count1 = obj1.getCount();
			int count2 = obj2.getCount();
			if (count1 != count2) {
				return count1 - count2;
			}
			return count1 - count2;

			// String digits1 = obj1Origin.getFirstDigits();
			// String digits2 = obj2Origin.getFirstDigits();
			// return digits1.compareTo(digits2);
		}
	}

}

package org.plutus.lottery.powerball;

import java.util.Date;

import org.origin.common.util.BaseComparator;

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

}

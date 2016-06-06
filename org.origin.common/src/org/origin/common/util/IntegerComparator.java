package org.origin.common.util;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class IntegerComparator implements Comparator<Integer> {

	// sort order by asc or desc constants
	public static final String SORT_ASC = "asc";
	public static final String SORT_DESC = "desc";

	public static IntegerComparator ASC = new IntegerComparator(SORT_ASC);
	public static IntegerComparator DESC = new IntegerComparator(SORT_DESC);

	public static void sort(List<Integer> serviceEntries) {
		sort(serviceEntries, SORT_ASC);
	}

	public static void sort(List<Integer> serviceEntries, String sort) {
		if (SORT_ASC.equalsIgnoreCase(sort)) {
			Collections.sort(serviceEntries, ASC);
		} else if (SORT_DESC.equalsIgnoreCase(sort)) {
			Collections.sort(serviceEntries, DESC);
		} else {
			Collections.sort(serviceEntries, ASC);
		}
	}

	protected String sort;

	public IntegerComparator() {
		this(SORT_ASC);
	}

	public IntegerComparator(String sort) {
		this.sort = check(sort);
	}

	protected String check(String sort) {
		if (sort != null) {
			sort = sort.toLowerCase();
		}
		if (!(SORT_ASC.equals(sort)) && !(SORT_DESC.equals(sort))) {
			sort = SORT_ASC;
		}
		return sort;
	}

	public boolean asc() {
		return SORT_ASC.equalsIgnoreCase(this.sort);
	}

	public boolean desc() {
		return SORT_DESC.equalsIgnoreCase(this.sort);
	}

	@Override
	public int compare(Integer integer1, Integer integer2) {
		return integer1.compareTo(integer2);
	}

}
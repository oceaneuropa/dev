package org.origin.common.util;

import java.util.Comparator;

/**
 * asc/desc aware Comparator
 *
 * @param <T>
 */
public abstract class BaseComparator<T> implements Comparator<T> {

	public static final String SORT_ASC = "asc"; //$NON-NLS-1$
	public static final String SORT_DESC = "desc"; //$NON-NLS-1$

	protected String sort;

	protected String check(String sort) {
		if (!SORT_ASC.equalsIgnoreCase(sort) && !SORT_DESC.equalsIgnoreCase(sort)) {
			return SORT_ASC;
		}
		return sort;
	}

	protected boolean asc() {
		return SORT_ASC.equalsIgnoreCase(sort);
	}

	protected boolean desc() {
		return SORT_DESC.equalsIgnoreCase(sort);
	}

}
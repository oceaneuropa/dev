package org.origin.mgm.model.comparator;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.origin.mgm.model.runtime.IndexItem;

public class IndexItemComparator implements Comparator<IndexItem> {

	// sort order by asc or desc constants
	public static final String SORT_ASC = "asc";
	public static final String SORT_DESC = "desc";

	public static IndexItemComparator ASC = new IndexItemComparator(SORT_ASC);
	public static IndexItemComparator DESC = new IndexItemComparator(SORT_DESC);

	public static void sort(List<IndexItem> serviceEntries) {
		sort(serviceEntries, SORT_ASC);
	}

	public static void sort(List<IndexItem> serviceEntries, String sort) {
		if (SORT_ASC.equalsIgnoreCase(sort)) {
			Collections.sort(serviceEntries, ASC);
		} else if (SORT_DESC.equalsIgnoreCase(sort)) {
			Collections.sort(serviceEntries, DESC);
		} else {
			Collections.sort(serviceEntries, ASC);
		}
	}

	protected String sort;

	public IndexItemComparator() {
		this(SORT_ASC);
	}

	public IndexItemComparator(String sort) {
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
	public int compare(IndexItem item1, IndexItem item2) {
		Integer indexItemId1 = item1.getIndexItemId();
		// String type1 = item1.getType();
		// String name1 = item1.getName();

		Integer indexItemId2 = item2.getIndexItemId();
		// String type2 = item2.getType();
		// String name2 = item2.getName();

		// sort desc
		if (desc()) {
			// if (!type1.equals(type2)) {
			// return type2.compareTo(type1);
			// }
			// return name2.compareTo(name1);
			return indexItemId2.compareTo(indexItemId1);
		}

		// sort asc
		// if (!type1.equals(type2)) {
		// return type1.compareTo(type2);
		// }
		// return name1.compareTo(name2);
		return indexItemId1.compareTo(indexItemId2);
	}

}

package org.origin.core.resources.impl.misc;

import java.util.Comparator;

import org.origin.core.resources.IWorkspace;

public class WorkspaceNameComparator implements Comparator<IWorkspace> {

	// sort by order
	public static final String SORT_ASC = "asc"; //$NON-NLS-1$
	public static final String SORT_DESC = "desc"; //$NON-NLS-1$

	public static WorkspaceNameComparator ASC = new WorkspaceNameComparator(SORT_ASC);
	public static WorkspaceNameComparator DESC = new WorkspaceNameComparator(SORT_DESC);

	protected String sort;

	public WorkspaceNameComparator() {
		this(SORT_ASC);
	}

	public WorkspaceNameComparator(String sort) {
		this.sort = check(sort);
	}

	protected String check(String sort) {
		if (sort == null) {
			sort = "asc"; //$NON-NLS-1$
		} else {
			sort = sort.toLowerCase();
		}
		if (!sort.equals("asc") && !sort.equals("desc")) { //$NON-NLS-1$ //$NON-NLS-2$
			sort = "asc"; //$NON-NLS-1$
		}
		return sort;
	}

	protected boolean asc() {
		return SORT_ASC.equalsIgnoreCase(sort);
	}

	protected boolean desc() {
		return SORT_DESC.equalsIgnoreCase(sort);
	}

	@Override
	public int compare(IWorkspace ws1, IWorkspace ws2) {
		String name1 = ws1.getName();
		if (name1 == null) {
			name1 = "";
		}
		String name2 = ws2.getName();
		if (name2 == null) {
			name2 = "";
		}

		if (desc()) {
			return name2.compareTo(name1);
		}
		return name1.compareTo(name2);
	}

}

package org.orbit.fs.server.googledrive.util;

import java.util.Comparator;

import com.google.api.services.drive.model.File;

public class Comparators {

	/**
	 * asc/desc aware Comparator
	 *
	 * @param <T>
	 */
	public static abstract class BaseComparator<T> implements Comparator<T> {
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

	/**
	 * Compare Google File objects
	 *
	 */
	public static class GoogleFileComparator extends BaseComparator<File> {
		public static GoogleFileComparator ASC = new GoogleFileComparator(SORT_ASC);
		public static GoogleFileComparator DESC = new GoogleFileComparator(SORT_DESC);

		public GoogleFileComparator() {
			this(SORT_ASC);
		}

		public GoogleFileComparator(String sort) {
			this.sort = check(sort);
		}

		@Override
		public int compare(File f1, File f2) {
			if (desc()) {
				File tmp = f1;
				f1 = f2;
				f2 = tmp;
			}

			boolean isDir1 = GoogleDriveHelper.INSTANCE.isDirectory(f1);
			boolean isDir2 = GoogleDriveHelper.INSTANCE.isDirectory(f2);

			if (isDir1 && !isDir2) {
				return -1;
			}
			if (!isDir1 && isDir2) {
				return 1;
			}

			String name1 = f1.getName();
			String name2 = f2.getName();

			return name1.compareTo(name2);
		}
	}

}

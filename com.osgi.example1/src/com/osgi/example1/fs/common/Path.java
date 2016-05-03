package com.osgi.example1.fs.common;

public class Path implements Comparable<Path> {

	// path separator
	public static final String SEPARATOR = "/";
	public static final char SEPARATOR_CHAR = '/';

	protected String pathString;

	/**
	 * 
	 * @param pathString
	 */
	public Path(String pathString) {
		this.pathString = pathString;
	}

	/**
	 * 
	 * @param parent
	 * @param child
	 */
	public Path(String parent, String child) {
		this(new Path(parent), new Path(child));
	}

	/**
	 * 
	 * @param parent
	 * @param child
	 */
	public Path(Path parent, String child) {
		this(parent, new Path(child));
	}

	/**
	 * 
	 * @param parent
	 * @param child
	 */
	public Path(String parent, Path child) {
		this(new Path(parent), child);
	}

	/**
	 * 
	 * @param parent
	 * @param child
	 */
	public Path(Path parent, Path child) {
		String path1 = parent.getName();
		String path2 = child.getName();

		String newPath = path1;

		// path1 ends with "/" and path2 starts with "/" --- make sure there is only one "/" between then.
		if (path1.endsWith(SEPARATOR) && path2.startsWith(SEPARATOR)) {
			if (path2.length() > 1) {
				path2 = path2.substring(1);
			} else {
				// path2's length is either 1 or greater than 1. if not greater than 1, must be 1 --- the "/"
				path2 = "";
			}
		}
		if (!path1.endsWith(SEPARATOR) && !path2.startsWith(SEPARATOR)) {
			newPath += SEPARATOR;
		}

		newPath += path2;

		this.pathString = newPath;
	}

	public String getName() {
		return this.pathString;
	}

	public Path getParent() {
		int lastSeparatorIndex = pathString.lastIndexOf(SEPARATOR_CHAR);
		if (pathString.length() == 0 // path is empty --- empty path --- doesn't have parent path.
				|| lastSeparatorIndex == 0 // path starts with "/" --- root path --- doesn't have parent path.
		) {
			return null;
		}
		if (lastSeparatorIndex == -1) {
			// path does not contain "/" --- relative path --- doesn't have parent path.
			return null;
		}
		// now the lastSeparatorIndex must be greater than 1.
		return new Path(pathString.substring(0, lastSeparatorIndex));
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Path)) {
			return false;
		}
		Path that = (Path) o;
		return this.pathString.equals(that.pathString);
	}

	@Override
	public int hashCode() {
		return this.pathString.hashCode();
	}

	@Override
	public int compareTo(Path path) {
		return this.pathString.compareTo(path.pathString);
	}

	@Override
	public String toString() {
		return this.pathString;
	}

}

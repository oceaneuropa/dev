package com.osgi.example1.fs.common;

import java.util.Arrays;

public class Path implements Comparable<Path> {

	// path separator
	public static final String SEPARATOR = "/";
	public static final char SEPARATOR_CHAR = '/';
	private static final String[] EMPTY_STRINGS = new String[] {};

	protected String pathString;

	/**
	 * 
	 * @param pathString
	 */
	public Path(String pathString) {
		if (pathString != null) {
			pathString = pathString.trim();
		}
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
		String path1 = parent.getPathString();
		String path2 = child.getPathString();

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

		if (newPath != null) {
			newPath = newPath.trim();
		}
		this.pathString = newPath;
	}

	/**
	 * Get Path starting from path segment with specified start index (inclusive) and extend until the end of the path segment.
	 * 
	 * @param startIndex
	 *            start index of path segment, inclusive
	 * @return
	 */
	public Path getPath(int startIndex) {
		String pathString = getPathString(startIndex);
		if (pathString != null) {
			return new Path(pathString);
		}
		return null;
	}

	/**
	 * Get Path starting from path segment with specified start index (inclusive) and extend until the path segment with specified end index
	 * (exclusive).
	 * 
	 * @param startIndex
	 *            start index of path segment, inclusive
	 * @param endIndex,
	 *            end index of path segment, exclusive
	 * @return
	 */
	public Path getPath(int startIndex, int endIndex) {
		String pathString = getPathString(startIndex, endIndex);
		if (pathString != null) {
			return new Path(pathString);
		}
		return null;
	}

	/**
	 * Get the string path of this Path.
	 * 
	 * @return
	 */
	public String getPathString() {
		return this.pathString;
	}

	/**
	 * Get Path string starting from path segment with specified start index (inclusive) and extend until the end of the path segment.
	 * 
	 * @param startIndex
	 *            start index of path segment, inclusive
	 * @return
	 */
	public String getPathString(int startIndex) {
		if (isRoot() || isEmpty()) {
			return null;
		}

		String[] segments = getSegments();
		if (startIndex < 0 || startIndex >= segments.length) {
			System.err.println("Start index is invalid.");
			return null;
		}
		int endIndex = segments.length;
		return getPathString(startIndex, endIndex);
	}

	/**
	 * Get Path starting from path segment with specified start index (inclusive) and extend until the path segment with specified end index
	 * (exclusive).
	 * 
	 * @param startIndex
	 *            start index of path segment, inclusive
	 * @param endIndex,
	 *            end index of path segment, exclusive
	 * @return
	 */
	public String getPathString(int startIndex, int endIndex) {
		if (isRoot() || isEmpty()) {
			return null;
		}

		String[] segments = getSegments();

		if (startIndex < 0 || startIndex >= segments.length) {
			System.err.println("Start index is invalid.");
			return null;
		}
		if (endIndex <= startIndex || startIndex > segments.length) {
			System.err.println("End index is invalid.");
			return null;
		}

		StringBuilder sb = new StringBuilder();

		if (startIndex == 0 && this.pathString != null && this.pathString.startsWith(SEPARATOR)) {
			sb.append(SEPARATOR);
		}

		for (int i = startIndex; i < endIndex; i++) {
			String segment = segments[i];
			boolean isEndingSegment = (i == (endIndex - 1)) ? true : false;

			sb.append(segment);

			if (!isEndingSegment) {
				sb.append(SEPARATOR);
			}
		}

		return sb.toString();
	}

	/**
	 * Get the string path of the parent Path.
	 * 
	 * @return
	 */
	public String getParentPathString() {
		Path parentPath = getParent();
		if (parentPath != null) {
			return parentPath.getPathString();
		}
		return null;
	}

	/**
	 * Check whether a path is "/"
	 * 
	 * @return return true when the path is "/". return false otherwise.
	 */
	public boolean isRoot() {
		return (SEPARATOR.equals(this.pathString)) ? true : false;
	}

	/**
	 * Check whether a path is empty.
	 * 
	 * @return return true when the path has 0 path segments. return false when the path has 1 or more path segments.
	 */
	public boolean isEmpty() {
		return (getSegments().length == 0) ? true : false;
	}

	/**
	 * Get all the segments, separated by / in a path.
	 * 
	 * @return
	 */
	public String[] getSegments() {
		String[] segments = null;
		if (this.pathString != null) {
			String thePathString = this.pathString.trim();

			if (thePathString.isEmpty() || SEPARATOR.equals(thePathString)) {
				return EMPTY_STRINGS;
			}
			// remove starting "/"
			while (thePathString.startsWith(SEPARATOR)) {
				thePathString = thePathString.substring(1);
				if (thePathString.isEmpty() || SEPARATOR.equals(thePathString)) {
					return EMPTY_STRINGS;
				}
			}
			// remove ending "/"
			while (thePathString.endsWith(SEPARATOR)) {
				thePathString = thePathString.substring(0, thePathString.lastIndexOf(SEPARATOR));
				if (thePathString.isEmpty() || SEPARATOR.equals(thePathString)) {
					return EMPTY_STRINGS;
				}
			}
			// split by "/"
			segments = thePathString.split(SEPARATOR);
		}
		if (segments == null) {
			segments = EMPTY_STRINGS;
		}
		return segments;
	}

	public String getLastSegment() {
		String[] segments = getSegments();
		if (segments != null && segments.length > 0) {
			return segments[segments.length - 1];
		}
		return null;
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

	public static void main0(String[] args) {
		String path0a = "";
		String path0b = "/";
		String path1 = "/dir1";
		String path2 = "dir1";
		String path3 = "/readme1.txt";
		String path4 = "readme1.txt";
		String path5 = "/path/to/the/dir2";
		String path6 = "path/to/the/dir2";
		String path7 = "/path/to/the/file.rar";
		String path8 = "path/to/the/file.rar";

		String[] segments0a = path0a.split(SEPARATOR);
		String[] segments0b = path0b.split(SEPARATOR);
		String[] segments1 = path1.split(SEPARATOR);
		String[] segments2 = path2.split(SEPARATOR);
		String[] segments3 = path3.split(SEPARATOR);
		String[] segments4 = path4.split(SEPARATOR);
		String[] segments5 = path5.split(SEPARATOR);
		String[] segments6 = path6.split(SEPARATOR);
		String[] segments7 = path7.split(SEPARATOR);
		String[] segments8 = path8.split(SEPARATOR);

		System.out.println(Arrays.toString(segments0a) + " (length=" + segments0a.length + ")");
		System.out.println(Arrays.toString(segments0b) + " (length=" + segments0b.length + ")");
		System.out.println(Arrays.toString(segments1) + " (length=" + segments1.length + ")");
		System.out.println(Arrays.toString(segments2) + " (length=" + segments2.length + ")");
		System.out.println(Arrays.toString(segments3) + " (length=" + segments3.length + ")");
		System.out.println(Arrays.toString(segments4) + " (length=" + segments4.length + ")");
		System.out.println(Arrays.toString(segments5) + " (length=" + segments5.length + ")");
		System.out.println(Arrays.toString(segments6) + " (length=" + segments6.length + ")");
		System.out.println(Arrays.toString(segments7) + " (length=" + segments7.length + ")");
		System.out.println(Arrays.toString(segments8) + " (length=" + segments8.length + ")");
	}

	public static void main(String[] args) {
		Path path0a = new Path("");
		Path path0b = new Path("/");
		Path path1 = new Path("/dir1");
		Path path2 = new Path("dir1");
		Path path3 = new Path("/readme1.txt");
		Path path4 = new Path("readme1.txt");
		Path path5 = new Path("/path/to/the/dir2");
		Path path6 = new Path("path/to/the/dir2");
		Path path7 = new Path("/path/to/the/file.rar");
		Path path8 = new Path("path/to/the/file.rar");

		String[] segments0a = path0a.getSegments();
		String[] segments0b = path0b.getSegments();
		String[] segments1 = path1.getSegments();
		String[] segments2 = path2.getSegments();
		String[] segments3 = path3.getSegments();
		String[] segments4 = path4.getSegments();
		String[] segments5 = path5.getSegments();
		String[] segments6 = path6.getSegments();
		String[] segments7 = path7.getSegments();
		String[] segments8 = path8.getSegments();

		System.out.println(Arrays.toString(segments0a) + " (length=" + segments0a.length + ")");
		System.out.println(Arrays.toString(segments0b) + " (length=" + segments0b.length + ")");
		System.out.println(Arrays.toString(segments1) + " (length=" + segments1.length + ")");
		System.out.println(Arrays.toString(segments2) + " (length=" + segments2.length + ")");
		System.out.println(Arrays.toString(segments3) + " (length=" + segments3.length + ")");
		System.out.println(Arrays.toString(segments4) + " (length=" + segments4.length + ")");
		System.out.println(Arrays.toString(segments5) + " (length=" + segments5.length + ")");
		System.out.println(Arrays.toString(segments6) + " (length=" + segments6.length + ")");
		System.out.println(Arrays.toString(segments7) + " (length=" + segments7.length + ")");
		System.out.println(Arrays.toString(segments8) + " (length=" + segments8.length + ")");

		String subPath7 = path7.getPathString(0, 3);
		String subPath8 = path8.getPathString(0, 3);
		System.out.println("subPath7 = " + subPath7);
		System.out.println("subPath8 = " + subPath8);
	}

}

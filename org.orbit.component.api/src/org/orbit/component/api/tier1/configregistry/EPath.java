package org.orbit.component.api.tier1.configregistry;

import org.origin.common.util.StringUtil;

/**
 * Note: This class MUST be in-sync with org.orbit.component.model.configregistry.runtime.EPath class.
 *
 */
public class EPath implements Comparable<EPath> {

	public static final EPath ROOT = new EPath("/");

	// path separator
	public static final String SEPARATOR = "/";
	public static final char SEPARATOR_CHAR = '/';

	private static final String[] EMPTY_STRINGS = new String[] {};

	protected String pathString;

	/**
	 * 
	 * @param pathString
	 */
	public EPath(String pathString) {
		if (pathString != null) {
			pathString = pathString.trim();
		}
		this.pathString = checkPath(pathString);
	}

	/**
	 * 
	 * @param parent
	 * @param child
	 */
	public EPath(String parent, String child) {
		this(new EPath(parent), new EPath(child));
	}

	/**
	 * 
	 * @param parent
	 * @param child
	 */
	public EPath(EPath parent, String child) {
		this(parent, new EPath(child));
	}

	/**
	 * 
	 * @param parent
	 * @param child
	 */
	public EPath(String parent, EPath child) {
		this(new EPath(parent), child);
	}

	/**
	 * 
	 * @param parent
	 * @param child
	 */
	public EPath(EPath parent, EPath child) {
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
		this.pathString = checkPath(newPath);
	}

	/**
	 * 
	 * @param path
	 * @return
	 */
	protected static String checkPath(String path) {
		if (path != null && path.length() > 1 && path.endsWith(EPath.SEPARATOR)) {
			path = path.substring(0, path.length() - 1);
		}
		return path;
	}

	/**
	 * Get Path starting from path segment with specified start index (inclusive) and extend until the end of the path segment.
	 * 
	 * @param startIndex
	 *            start index of path segment, inclusive
	 * @return
	 */
	public EPath getPath(int startIndex) {
		String pathString = getPathString(startIndex);
		if (pathString != null) {
			return new EPath(pathString);
		}
		return null;
	}

	/**
	 * Get Path starting from path segment with specified start index (inclusive) and extend until the path segment with specified end index (exclusive).
	 * 
	 * @param startIndex
	 *            start index of path segment, inclusive
	 * @param endIndex,
	 *            end index of path segment, exclusive
	 * @return
	 */
	public EPath getPath(int startIndex, int endIndex) {
		String pathString = getPathString(startIndex, endIndex);
		if (pathString != null) {
			return new EPath(pathString);
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
	 * Get Path starting from path segment with specified start index (inclusive) and extend until the path segment with specified end index (exclusive).
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
		EPath parentPath = getParent();
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
			thePathString = StringUtil.removeStartingCharacters(thePathString, SEPARATOR);
			// remove ending "/"
			thePathString = StringUtil.removeEndingCharacters(thePathString, SEPARATOR);

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

	public EPath getParent() {
		if (pathString.length() == 0) {
			// path is empty --- empty path --- doesn't have parent path.
			return null;
		}

		int lastSeparatorIndex = pathString.lastIndexOf(SEPARATOR_CHAR);
		if (lastSeparatorIndex == 0) {
			// path starts with "/" --- current path is contained by root path.
			return ROOT;
		}
		if (lastSeparatorIndex == -1) {
			// path does not contain "/" --- relative path --- doesn't have parent path.
			return null;
		}

		// now the lastSeparatorIndex must be greater than 1.
		return new EPath(pathString.substring(0, lastSeparatorIndex));
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof EPath)) {
			return false;
		}
		EPath that = (EPath) o;
		return this.pathString.equals(that.pathString);
	}

	@Override
	public int hashCode() {
		return this.pathString.hashCode();
	}

	@Override
	public int compareTo(EPath path) {
		return this.pathString.compareTo(path.pathString);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Path(pathString=").append(this.pathString);
		sb.append(")");
		return sb.toString();
	}

}

package org.origin.common.resource;

public interface IPath {

	boolean isRoot();

	boolean isEmpty();

	String[] getSegments();

	String getLastSegment();

	String getFileExtension();

	IPath getParent();

	IPath getPath(int startIndex);

	IPath getPath(int startIndex, int endIndex);

	String getPathString();

	String getPathString(int startIndex);

	String getPathString(int startIndex, int endIndex);

	IPath append(String pathString);

	IPath append(IPath path);

}

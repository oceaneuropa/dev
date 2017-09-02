package org.origin.core.resources;

import org.origin.core.resources.internal.PathImpl;

public interface IPath {

	public static final IPath ROOT = PathImpl.createRootPath();

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

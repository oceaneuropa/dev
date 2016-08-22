package org.origin.core.workspace;

import org.origin.common.adapter.IAdaptable;

public interface IResource extends IAdaptable {

	// flags for resource types
	public static final int FILE = 0x1;
	public static final int FOLDER = 0x2;
	public static final int PROJECT = 0x4;
	public static final int ROOT = 0x8;

	public Workspace getWorkspace();

	public IProject getProject();

	public IContainer getParent();

	public int getType();

	public String getName();

	public boolean exists();

}

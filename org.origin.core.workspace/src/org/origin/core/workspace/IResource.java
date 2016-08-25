package org.origin.core.workspace;

import java.io.File;
import java.io.IOException;

import org.origin.common.adapter.IAdaptable;

public interface IResource extends IAdaptable {

	// Resource type constants
	public static final int FILE = 0x1;
	public static final int FOLDER = 0x2;
	public static final int PROJECT = 0x4;
	public static final int WORKSPACE = 0x8;

	/**
	 * Get resource type. Values are FILE, FOLDER, PROJECT and WORKSPACE.
	 * 
	 * @return
	 */
	public int getType();

	/**
	 * Get java.io.File of the resource.
	 * 
	 * @return
	 */
	public File getFile();

	/**
	 * Get the description file of the resource.
	 * 
	 * @return
	 */
	public File getDescriptionFile();

	/**
	 * Load the resource.
	 * 
	 * @throws IOException
	 */
	public void load() throws IOException;

	/**
	 * Save the resource.
	 * 
	 * @throws IOException
	 */
	public void save() throws IOException;

	/**
	 * Delete the resource.
	 */
	public void delete();

	/**
	 * Get the workspace the resource belongs to.
	 * 
	 * @return
	 */
	public IWorkspace getWorkspace();

	/**
	 * Get the project the resource belongs to.
	 * 
	 * @return
	 */
	public IProject getProject();

	/**
	 * Get the container resource of the resource.
	 * 
	 * @return
	 */
	public IContainer getParent();

	/**
	 * Get the resource name.
	 * 
	 * @return
	 */
	public String getName();

	/**
	 * Check whether java.io.File exists.
	 * 
	 * @return
	 */
	public boolean exists();

}

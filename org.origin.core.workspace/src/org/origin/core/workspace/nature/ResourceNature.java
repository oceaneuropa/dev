package org.origin.core.workspace.nature;

import org.origin.core.workspace.IResource;

public interface ResourceNature<RES extends IResource> {

	/**
	 * 
	 * @return
	 */
	public String getNatureId();

	/**
	 * 
	 * @param resource
	 */
	public void setResource(RES resource);

	/**
	 * 
	 */
	public RES getResource();

	/**
	 * Called when a resource is loaded. Also called when a natureId is added to a resource.
	 * 
	 * Subclass can implement this method to load additional information (e.g. additional configuration files from file system) and store then in the
	 * resource.
	 */
	public void load();

	/**
	 * Called when a resource is saved.
	 * 
	 * Subclass can implement this method to get additional information stored in the resource and save then (e.g. to file system)
	 */
	public void save();

	/**
	 * Called when a natureId is added to a resource.
	 * 
	 * @param resource
	 */
	public void configure();

	/**
	 * Called when a natureId is removed from a resource.
	 * 
	 * @param resource
	 */
	public void deconfigure();

}

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

	// ------------------------------------------------------------------------------------------------
	// 1. When a resource is created from a description with natureIds
	// (1) nature.load()
	// (2) nature.configure()
	// (3) save description file
	//
	// 2. When an existing resource is loaded
	// (1) nature.load()
	//
	// 3. When a description with natureIds is set to a resource
	// (1) for added natures: nature.configure(); nature.load();
	// (2) for removed natures: nature.deconfigure();
	// (3) save description file. nature.save()
	//
	// 4. When a natureId is added to a resource
	// (1) nature.load()
	// (2) nature.configure()
	// (3) save description file
	//
	// 5. When a resource is saved/shutdown/closed
	// (1) nature.save()
	//
	// 6. When a natureId is removed from a resource
	// (1) nature.deconfigure()
	// (2) save description file
	//
	// 7. When a resource is deleted
	// (1) nature.deconfigure()
	// ------------------------------------------------------------------------------------------------
	/**
	 * Called when a resource is loaded.
	 * 
	 * Called when a natureId is added to a resource.
	 * 
	 * Note: Load additional configurations from the file system and store then in the resource as properties.
	 */
	public void load();

	/**
	 * Called when a natureId is added to a resource.
	 * 
	 * Note: Configure the resource.
	 */
	public void configure();

	/**
	 * Called when a resource is saved.
	 * 
	 * Note: Get additional configurations from the resource properties and save then to the file system.
	 */
	public void save();

	/**
	 * Called when a natureId is removed from a resource.
	 * 
	 * Note: Deconfigure the resource.
	 */
	public void deconfigure();

}

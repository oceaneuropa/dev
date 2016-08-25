package org.origin.core.workspace;

public interface IFolder extends IContainer {

	/**
	 * Create a folder.
	 * 
	 * @param description
	 */
	public void create(IFolderDescription description);

	/**
	 * Get folder description.
	 * 
	 * @return
	 */
	public IFolderDescription getDescription();

	/**
	 * Set folder description.
	 * 
	 * @param description
	 */
	public void setDescription(IFolderDescription description);

}

package org.origin.core.workspace;

public interface IProject extends IContainer {

	/**
	 * Create a project using this IProject handle.
	 * 
	 * @param description
	 */
	public void create(IProjectDescription description);

	/**
	 * Set the project description.
	 * 
	 * @param description
	 */
	public void setDescription(IProjectDescription description);

	/**
	 * Get the project description.
	 * 
	 * @return
	 */
	public IProjectDescription getDescription();

}

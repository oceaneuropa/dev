package org.orbit.component.model.tier3.nodecontrol.dto;

public interface NodeConfig {

	/**
	 * Get Node Id.
	 * 
	 * @return
	 */
	public String getId();

	/**
	 * Get Node name.
	 * 
	 * @return
	 */
	public String getName();

	/**
	 * Get Node description.
	 * 
	 * @return
	 */
	public String getDescription();

	/**
	 * 
	 * @return
	 */
	public String getHostURL();

	/**
	 * 
	 * @return
	 */
	public String getContextRoot();

}

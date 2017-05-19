package org.orbit.component.api.tier3.transferagent;

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

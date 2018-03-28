package org.orbit.component.api.tier3.domainmanagement;

public interface NodeConfig {

	/**
	 * Get Machine Id.
	 * 
	 * @return
	 */
	public String getMachineId();

	/**
	 * Get platform Id.
	 * 
	 * @return
	 */
	public String getPlatformId();

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
	 * Get Node home.
	 * 
	 * @return
	 */
	public String getHome();

	/**
	 * Get Node host URL.
	 * 
	 * @return
	 */
	public String getHostURL();

	/**
	 * Get Node context root.
	 * 
	 * @return
	 */
	public String getContextRoot();

}

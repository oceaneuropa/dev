package org.orbit.component.api.tier3.domain;

/**
 * TransferAgent configuration
 * 
 */
public interface TransferAgentConfig {

	/**
	 * Get TransferAgent Id.
	 * 
	 * @return
	 */
	public String getId();

	/**
	 * Get TransferAgent name.
	 * 
	 * @return
	 */
	public String getName();

	/**
	 * Get TransferAgent home.
	 * 
	 * @return
	 */
	public String getTAHome();

	/**
	 * Get TransferAgent host URL.
	 * 
	 * @return
	 */
	public String getHostURL();

	/**
	 * Get TransferAgent context root.
	 * 
	 * @return
	 */
	public String getContextRoot();

}

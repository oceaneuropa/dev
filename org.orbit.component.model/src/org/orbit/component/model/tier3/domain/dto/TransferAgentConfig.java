package org.orbit.component.model.tier3.domain.dto;

/**
 * TransferAgent configuration
 * 
 */
public interface TransferAgentConfig {

	/**
	 * Get Machine Id.
	 * 
	 * @return
	 */
	public String getMachineId();

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
	public String getHome();

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

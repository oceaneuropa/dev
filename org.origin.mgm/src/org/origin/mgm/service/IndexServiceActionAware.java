package org.origin.mgm.service;

import java.util.Map;

public interface IndexServiceActionAware {

	/**
	 * Check whether an action is supported.
	 * 
	 * @param action
	 * @return
	 */
	public boolean isActionSupported(String action);

	/**
	 * Response to an action.
	 * 
	 * @param action
	 *            an action to be executed.
	 * @param parameters
	 *            optional parameters of the action.
	 * @return
	 */
	public boolean onAction(String action, Map<String, Object> parameters);

}

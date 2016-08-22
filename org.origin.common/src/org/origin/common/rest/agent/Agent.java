package org.origin.common.rest.agent;

import org.origin.common.rest.model.Request;
import org.origin.common.rest.model.Responses;

public interface Agent {

	/**
	 * 
	 * @param request
	 * @return
	 */
	public Responses action(Request request);

}

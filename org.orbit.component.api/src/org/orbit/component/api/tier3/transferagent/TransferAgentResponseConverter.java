package org.orbit.component.api.tier3.transferagent;

import org.origin.common.rest.model.Responses;

public interface TransferAgentResponseConverter {

	/**
	 * 
	 * @param responses
	 * @return
	 */
	NodespaceInfo[] convertToNodespaceInfos(Responses responses);

	/**
	 * 
	 * @param responses
	 * @return
	 */
	NodespaceInfo convertToNodespaceInfo(Responses responses);

	/**
	 * 
	 * @param responses
	 * @return
	 */
	NodeInfo[] convertToNodeInfos(Responses responses);

	/**
	 * 
	 * @param responses
	 * @return
	 */
	NodeInfo convertToNodeInfo(Responses responses);

}

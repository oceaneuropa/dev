package org.orbit.component.runtime.tier3.nodemanagement.ws.command.other;

import org.orbit.component.runtime.tier3.nodemanagement.service.NodeManagementService;
import org.origin.common.command.AbstractCommand;
import org.origin.common.rest.model.Request;

public abstract class AbstractTransferAgentCommandV1 extends AbstractCommand {

	protected NodeManagementService service;
	protected Request request;

	/**
	 * 
	 * @param service
	 * @param request
	 */
	public AbstractTransferAgentCommandV1(NodeManagementService service, Request request) {
		this.service = service;
		this.request = request;
	}

	public NodeManagementService getService() {
		return service;
	}

	public void setService(NodeManagementService service) {
		this.service = service;
	}

	public Request getRequest() {
		return request;
	}

	public void setRequest(Request request) {
		this.request = request;
	}

}

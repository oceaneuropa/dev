package org.orbit.component.server.tier3.transferagent.command;

import org.orbit.component.server.tier3.transferagent.service.TransferAgentService;
import org.origin.common.command.AbstractCommand;
import org.origin.common.rest.model.Request;

public abstract class AbstractTransferAgentCommand extends AbstractCommand {

	protected TransferAgentService service;
	protected Request request;

	/**
	 * 
	 * @param service
	 * @param request
	 */
	public AbstractTransferAgentCommand(TransferAgentService service, Request request) {
		this.service = service;
		this.request = request;
	}

	public TransferAgentService getService() {
		return service;
	}

	public void setService(TransferAgentService service) {
		this.service = service;
	}

	public Request getRequest() {
		return request;
	}

	public void setRequest(Request request) {
		this.request = request;
	}

}

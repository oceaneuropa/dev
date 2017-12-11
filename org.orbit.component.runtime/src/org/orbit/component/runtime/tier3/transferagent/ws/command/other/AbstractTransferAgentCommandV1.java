package org.orbit.component.runtime.tier3.transferagent.ws.command.other;

import org.orbit.component.runtime.tier3.transferagent.service.TransferAgentService;
import org.origin.common.command.AbstractCommand;
import org.origin.common.rest.model.Request;

public abstract class AbstractTransferAgentCommandV1 extends AbstractCommand {

	protected TransferAgentService service;
	protected Request request;

	/**
	 * 
	 * @param service
	 * @param request
	 */
	public AbstractTransferAgentCommandV1(TransferAgentService service, Request request) {
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

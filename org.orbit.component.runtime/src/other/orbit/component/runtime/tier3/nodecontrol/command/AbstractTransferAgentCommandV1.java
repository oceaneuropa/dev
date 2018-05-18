package other.orbit.component.runtime.tier3.nodecontrol.command;

import org.orbit.component.runtime.tier3.nodecontrol.service.NodeControlService;
import org.origin.common.command.AbstractCommand;
import org.origin.common.rest.model.Request;

public abstract class AbstractTransferAgentCommandV1 extends AbstractCommand {

	protected NodeControlService service;
	protected Request request;

	/**
	 * 
	 * @param service
	 * @param request
	 */
	public AbstractTransferAgentCommandV1(NodeControlService service, Request request) {
		this.service = service;
		this.request = request;
	}

	public NodeControlService getService() {
		return service;
	}

	public void setService(NodeControlService service) {
		this.service = service;
	}

	public Request getRequest() {
		return request;
	}

	public void setRequest(Request request) {
		this.request = request;
	}

}

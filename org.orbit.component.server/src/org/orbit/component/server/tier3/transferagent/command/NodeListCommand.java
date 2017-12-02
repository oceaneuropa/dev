package org.orbit.component.server.tier3.transferagent.command;

import javax.ws.rs.core.Response;

import org.orbit.component.server.tier3.transferagent.service.TransferAgentService;
import org.origin.common.rest.editpolicy.WSCommand;
import org.origin.common.rest.model.Request;
import org.origin.core.resources.IResource;
import org.origin.core.resources.IWorkspace;
import org.origin.core.resources.node.INode;

public class NodeListCommand implements WSCommand {

	protected TransferAgentService service;

	public NodeListCommand(TransferAgentService service) {
		this.service = service;
	}

	@Override
	public Response execute(Request request) {
		IWorkspace nodeWorkspace = service.getNodeWorkspace();

		
		IResource[] rootMembers = nodeWorkspace.getRootMembers();
		for (IResource rootMember : rootMembers) {
			if (rootMember instanceof INode) {
				INode node = (INode) rootMember;
			}
		}
		return null;
	}

}

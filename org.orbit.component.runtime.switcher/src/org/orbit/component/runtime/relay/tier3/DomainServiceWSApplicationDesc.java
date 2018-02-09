package org.orbit.component.runtime.relay.tier3;

import org.origin.common.rest.server.WSApplicationDesc;
import org.origin.common.rest.server.WSMethodDesc;
import org.origin.common.rest.server.WSResourceDesc;

public class DomainServiceWSApplicationDesc extends WSApplicationDesc {

	/**
	 * 
	 * @param contextRoot
	 */
	public DomainServiceWSApplicationDesc(String contextRoot) {
		super(contextRoot);

		WSResourceDesc rootWSResource = new WSResourceDesc(this, "/");
		new WSMethodDesc(rootWSResource, WSMethodDesc.GET, WSMethodDesc.JSON, "echo");

		WSResourceDesc machinesWSResource = new WSResourceDesc(this, "/machines");
		new WSMethodDesc(machinesWSResource, WSMethodDesc.GET, WSMethodDesc.JSON, "");
		new WSMethodDesc(machinesWSResource, WSMethodDesc.GET, WSMethodDesc.JSON, "{machineId}");
		new WSMethodDesc(machinesWSResource, WSMethodDesc.POST, WSMethodDesc.JSON, "");
		new WSMethodDesc(machinesWSResource, WSMethodDesc.PUT, WSMethodDesc.JSON, "");
		new WSMethodDesc(machinesWSResource, WSMethodDesc.DELETE, WSMethodDesc.JSON, "{machineId}");

		WSResourceDesc transferAgentsWSResource = new WSResourceDesc(this, "/machines/{machineId}/transferagents");
		new WSMethodDesc(transferAgentsWSResource, WSMethodDesc.GET, WSMethodDesc.JSON, "");
		new WSMethodDesc(transferAgentsWSResource, WSMethodDesc.GET, WSMethodDesc.JSON, "{transferAgentId}");
		new WSMethodDesc(transferAgentsWSResource, WSMethodDesc.POST, WSMethodDesc.JSON, "");
		new WSMethodDesc(transferAgentsWSResource, WSMethodDesc.PUT, WSMethodDesc.JSON, "");
		new WSMethodDesc(transferAgentsWSResource, WSMethodDesc.DELETE, WSMethodDesc.JSON, "{transferAgentId}");

		WSResourceDesc nodesWSResource = new WSResourceDesc(this, "/machines/{machineId}/transferagents/{transferAgentId}/nodes");
		new WSMethodDesc(nodesWSResource, WSMethodDesc.GET, WSMethodDesc.JSON, "");
		new WSMethodDesc(nodesWSResource, WSMethodDesc.GET, WSMethodDesc.JSON, "{nodeId}");
		new WSMethodDesc(nodesWSResource, WSMethodDesc.POST, WSMethodDesc.JSON, "");
		new WSMethodDesc(nodesWSResource, WSMethodDesc.PUT, WSMethodDesc.JSON, "");
		new WSMethodDesc(nodesWSResource, WSMethodDesc.DELETE, WSMethodDesc.JSON, "{nodeId}");
	}

}

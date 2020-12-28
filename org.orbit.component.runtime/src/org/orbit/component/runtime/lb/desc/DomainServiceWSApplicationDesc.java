package org.orbit.component.runtime.lb.desc;

import org.origin.common.rest.server.FeatureConstants;
import org.origin.common.rest.server.WSApplicationDesc;
import org.origin.common.rest.server.WSMethodDesc;
import org.origin.common.rest.server.WSResourceDesc;
import org.origin.common.service.IWebService;

public class DomainServiceWSApplicationDesc extends WSApplicationDesc {

	/**
	 * 
	 * @param webService
	 */
	public DomainServiceWSApplicationDesc(IWebService webService) {
		super(webService);
		setRemoteFeature(FeatureConstants.METADATA | FeatureConstants.NAME | FeatureConstants.ECHO);

		// WSResourceDesc rootWSResource = new WSResourceDesc(this, "/");
		// new WSMethodDesc(rootWSResource, WSMethodDesc.GET, WSMethodDesc.JSON, "echo");
		// new WSMethodDesc(rootWSResource, WSMethodDesc.GET, WSMethodDesc.JSON, "name");

		WSResourceDesc machinesWSResource = new WSResourceDesc(this, "/machines");
		new WSMethodDesc(machinesWSResource, WSMethodDesc.GET, WSMethodDesc.JSON, "");
		new WSMethodDesc(machinesWSResource, WSMethodDesc.GET, WSMethodDesc.JSON, "{machineId}");
		new WSMethodDesc(machinesWSResource, WSMethodDesc.POST, WSMethodDesc.JSON, "");
		new WSMethodDesc(machinesWSResource, WSMethodDesc.PUT, WSMethodDesc.JSON, "");
		new WSMethodDesc(machinesWSResource, WSMethodDesc.DELETE, WSMethodDesc.JSON, "{machineId}");

		WSResourceDesc platformsWSResource = new WSResourceDesc(this, "/machines/{machineId}/platforms");
		new WSMethodDesc(platformsWSResource, WSMethodDesc.GET, WSMethodDesc.JSON, "");
		new WSMethodDesc(platformsWSResource, WSMethodDesc.GET, WSMethodDesc.JSON, "{platformId}");
		new WSMethodDesc(platformsWSResource, WSMethodDesc.POST, WSMethodDesc.JSON, "");
		new WSMethodDesc(platformsWSResource, WSMethodDesc.PUT, WSMethodDesc.JSON, "");
		new WSMethodDesc(platformsWSResource, WSMethodDesc.DELETE, WSMethodDesc.JSON, "{platformId}");

		WSResourceDesc nodesWSResource = new WSResourceDesc(this, "/machines/{machineId}/platforms/{platformId}/nodes");
		new WSMethodDesc(nodesWSResource, WSMethodDesc.GET, WSMethodDesc.JSON, "");
		new WSMethodDesc(nodesWSResource, WSMethodDesc.GET, WSMethodDesc.JSON, "{nodeId}");
		new WSMethodDesc(nodesWSResource, WSMethodDesc.POST, WSMethodDesc.JSON, "");
		new WSMethodDesc(nodesWSResource, WSMethodDesc.PUT, WSMethodDesc.JSON, "");
		new WSMethodDesc(nodesWSResource, WSMethodDesc.DELETE, WSMethodDesc.JSON, "{nodeId}");
	}

}

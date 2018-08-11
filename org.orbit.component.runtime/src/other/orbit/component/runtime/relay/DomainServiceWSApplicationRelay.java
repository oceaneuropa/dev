package other.orbit.component.runtime.relay;

import java.net.URI;

import org.glassfish.jersey.server.model.Resource;
import org.origin.common.rest.client.WSClientFactory;
import org.origin.common.rest.server.FeatureConstants;
import org.origin.common.rest.server.WSMethodInflector;
import org.origin.common.rest.server.WSRelayApplication;
import org.origin.common.rest.switcher.Switcher;
import org.origin.common.service.WebServiceAwareImpl;

public class DomainServiceWSApplicationRelay extends WSRelayApplication {

	/**
	 * 
	 * @param contextRoot
	 * @param switcher
	 * @param factory
	 */
	public DomainServiceWSApplicationRelay(String contextRoot, Switcher<URI> switcher, WSClientFactory factory) {
		super(new WebServiceAwareImpl(null, null, contextRoot), FeatureConstants.PING, switcher);

		Resource.Builder wsResource = Resource.builder("/");
		new WSMethodInflector(wsResource, "echo", GET, JSON, factory.createClient(null), switcher);

		Resource.Builder machinesWSResource = Resource.builder("/machines");
		new WSMethodInflector(machinesWSResource, "", GET, JSON, factory.createClient(null), switcher);
		new WSMethodInflector(machinesWSResource, "{machineId}", GET, JSON, factory.createClient(null), switcher);
		new WSMethodInflector(machinesWSResource, "", POST, JSON, factory.createClient(null), switcher);
		new WSMethodInflector(machinesWSResource, "", PUT, JSON, factory.createClient(null), switcher);
		new WSMethodInflector(machinesWSResource, "{machineId}", DELETE, JSON, factory.createClient(null), switcher);

		Resource.Builder transferAgentsWSResource = Resource.builder("/machines/{machineId}/transferagents");
		new WSMethodInflector(transferAgentsWSResource, "", GET, JSON, factory.createClient(null), switcher);
		new WSMethodInflector(transferAgentsWSResource, "{transferAgentId}", GET, JSON, factory.createClient(null), switcher);
		new WSMethodInflector(transferAgentsWSResource, "", POST, JSON, factory.createClient(null), switcher);
		new WSMethodInflector(transferAgentsWSResource, "", PUT, JSON, factory.createClient(null), switcher);
		new WSMethodInflector(transferAgentsWSResource, "{transferAgentId}", DELETE, JSON, factory.createClient(null), switcher);

		Resource.Builder nodesWSResource = Resource.builder("/machines/{machineId}/transferagents/{transferAgentId}/nodes");
		new WSMethodInflector(nodesWSResource, "", GET, JSON, factory.createClient(null), switcher);
		new WSMethodInflector(nodesWSResource, "{nodeId}", GET, JSON, factory.createClient(null), switcher);
		new WSMethodInflector(nodesWSResource, "", POST, JSON, factory.createClient(null), switcher);
		new WSMethodInflector(nodesWSResource, "", PUT, JSON, factory.createClient(null), switcher);
		new WSMethodInflector(nodesWSResource, "{nodeId}", DELETE, JSON, factory.createClient(null), switcher);

		registerResources(wsResource.build());
		registerResources(machinesWSResource.build());
		registerResources(transferAgentsWSResource.build());
		registerResources(nodesWSResource.build());
	}

}

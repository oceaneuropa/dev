package org.orbit.component.runtime.switcher;

import java.net.URI;

import org.glassfish.jersey.server.model.Resource;
import org.origin.common.rest.client.WSClientFactory;
import org.origin.common.rest.server.AbstractJerseyWSApplication;
import org.origin.common.rest.server.FeatureConstants;
import org.origin.common.rest.server.WSMethodInflector;
import org.origin.common.rest.switcher.Switcher;
import org.osgi.framework.BundleContext;

public class DomainServiceWSApplicationSwitcher extends AbstractJerseyWSApplication {

	protected Switcher<URI> switcher;

	/**
	 * 
	 * @param contextRoot
	 * @param factory
	 * @param switcher
	 */
	public DomainServiceWSApplicationSwitcher(String contextRoot, WSClientFactory factory, Switcher<URI> switcher) {
		super(contextRoot, FeatureConstants.PING);
		this.switcher = switcher;

		// Note:
		// - All access to this web service application are handled by one Switcher instance.
		// - Each API path has its own web service client instance
		Resource.Builder wsResource = Resource.builder("/");
		new WSMethodInflector(wsResource, "echo", GET, JSON, factory.createClient(null), this.switcher);

		Resource.Builder machinesWSResource = Resource.builder("/machines");
		new WSMethodInflector(machinesWSResource, "", GET, JSON, factory.createClient(null), this.switcher);
		new WSMethodInflector(machinesWSResource, "{machineId}", GET, JSON, factory.createClient(null), this.switcher);
		new WSMethodInflector(machinesWSResource, "", POST, JSON, factory.createClient(null), this.switcher);
		new WSMethodInflector(machinesWSResource, "", PUT, JSON, factory.createClient(null), this.switcher);
		new WSMethodInflector(machinesWSResource, "{machineId}", DELETE, JSON, factory.createClient(null), this.switcher);

		Resource.Builder transferAgentsWSResource = Resource.builder("/machines/{machineId}/transferagents");
		new WSMethodInflector(transferAgentsWSResource, "", GET, JSON, factory.createClient(null), this.switcher);
		new WSMethodInflector(transferAgentsWSResource, "{transferAgentId}", GET, JSON, factory.createClient(null), this.switcher);
		new WSMethodInflector(transferAgentsWSResource, "", POST, JSON, factory.createClient(null), this.switcher);
		new WSMethodInflector(transferAgentsWSResource, "", PUT, JSON, factory.createClient(null), this.switcher);
		new WSMethodInflector(transferAgentsWSResource, "{transferAgentId}", DELETE, JSON, factory.createClient(null), this.switcher);

		Resource.Builder nodesWSResource = Resource.builder("/machines/{machineId}/transferagents/{transferAgentId}/nodes");
		new WSMethodInflector(nodesWSResource, "", GET, JSON, factory.createClient(null), this.switcher);
		new WSMethodInflector(nodesWSResource, "{nodeId}", GET, JSON, factory.createClient(null), this.switcher);
		new WSMethodInflector(nodesWSResource, "", POST, JSON, factory.createClient(null), this.switcher);
		new WSMethodInflector(nodesWSResource, "", PUT, JSON, factory.createClient(null), this.switcher);
		new WSMethodInflector(nodesWSResource, "{nodeId}", DELETE, JSON, factory.createClient(null), this.switcher);

		registerResources(wsResource.build());
		registerResources(machinesWSResource.build());
		registerResources(transferAgentsWSResource.build());
		registerResources(nodesWSResource.build());
	}

	@Override
	public void start(BundleContext bundleContext) {
		this.switcher.start();
		super.start(bundleContext);
	}

	@Override
	public void stop(BundleContext bundleContext) {
		super.stop(bundleContext);
		this.switcher.stop();
	}

}

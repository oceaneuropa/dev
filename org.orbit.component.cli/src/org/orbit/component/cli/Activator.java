package org.orbit.component.cli;

import java.util.Hashtable;
import java.util.Map;

import org.orbit.infra.api.indexes.IndexServiceConnector;
import org.orbit.infra.api.indexes.IndexServiceConnectorAdapter;
import org.orbit.infra.api.indexes.IndexServiceLoadBalancer;
import org.orbit.infra.api.indexes.IndexServiceUtil;
import org.origin.common.util.PropertyUtil;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {

	private static BundleContext context;

	static BundleContext getContext() {
		return context;
	}

	protected IndexServiceConnectorAdapter indexServiceConnectorAdapter;

	protected ServicesCommand servicesCommand;
	protected AuthCommand authCommand;
	protected UserRegistryCommand userRegistryCommand;
	protected AppStoreCommand appStoreCommand;
	protected DomainServiceCommand domainMgmtCommand;
	protected TransferAgentCommand transferAgentCommand;

	@Override
	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;

		this.indexServiceConnectorAdapter = new IndexServiceConnectorAdapter() {
			@Override
			public void connectorAdded(IndexServiceConnector connector) {
				doStart(Activator.context, connector);
			}

			@Override
			public void connectorRemoved(IndexServiceConnector connector) {
				doStop(Activator.context);
			}
		};
		this.indexServiceConnectorAdapter.start(bundleContext);
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		doStop(bundleContext);

		Activator.context = null;
	}

	protected void doStart(BundleContext bundleContext, IndexServiceConnector connector) {
		// Get load balancer for IndexProvider
		Map<Object, Object> indexProviderProps = new Hashtable<Object, Object>();
		PropertyUtil.loadProperty(bundleContext, indexProviderProps, org.orbit.infra.api.OrbitConstants.COMPONENT_INDEX_SERVICE_URL_PROP);
		IndexServiceLoadBalancer indexServiceLoadBalancer = IndexServiceUtil.getIndexServiceLoadBalancer(connector, indexProviderProps);

		// Start commands
		this.servicesCommand = new ServicesCommand(bundleContext, indexServiceLoadBalancer.createLoadBalancableIndexService());
		this.servicesCommand.start();

		this.authCommand = new AuthCommand();
		this.authCommand.start(bundleContext);

		this.userRegistryCommand = new UserRegistryCommand();
		this.userRegistryCommand.start(bundleContext);

		this.appStoreCommand = new AppStoreCommand(bundleContext);
		this.appStoreCommand.start();

		this.domainMgmtCommand = new DomainServiceCommand(bundleContext);
		this.domainMgmtCommand.start();

		this.transferAgentCommand = new TransferAgentCommand();
		this.transferAgentCommand.start(bundleContext);
	}

	protected void doStop(BundleContext bundleContext) {
		// Stop commands
		if (this.servicesCommand != null) {
			this.servicesCommand.stop();
			this.servicesCommand = null;
		}

		if (this.authCommand != null) {
			this.authCommand.stop(bundleContext);
			this.authCommand = null;
		}

		if (this.userRegistryCommand != null) {
			this.userRegistryCommand.stop(bundleContext);
			this.userRegistryCommand = null;
		}

		if (this.appStoreCommand != null) {
			this.appStoreCommand.stop();
			this.appStoreCommand = null;
		}

		if (this.domainMgmtCommand != null) {
			this.domainMgmtCommand.stop();
			this.domainMgmtCommand = null;
		}

		if (this.transferAgentCommand != null) {
			this.transferAgentCommand.stop(bundleContext);
			this.transferAgentCommand = null;
		}
	}

}

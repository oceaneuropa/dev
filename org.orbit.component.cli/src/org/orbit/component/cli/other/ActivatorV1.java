package org.orbit.component.cli.other;

import java.util.Hashtable;
import java.util.Map;

import org.orbit.component.cli.AppStoreCommand;
import org.orbit.component.cli.AuthCommand;
import org.orbit.component.cli.DomainServiceCommand;
import org.orbit.component.cli.ServicesCommand;
import org.orbit.component.cli.TransferAgentCommand;
import org.orbit.component.cli.UserRegistryCommand;
import org.orbit.infra.api.indexes.IndexServiceLoadBalancer;
import org.orbit.infra.api.indexes.IndexServiceUtil;
import org.orbit.infra.api.indexes.other.IndexServiceConnectorAdapterV1;
import org.orbit.infra.api.indexes.other.IndexServiceConnectorV1;
import org.origin.common.util.PropertyUtil;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class ActivatorV1 implements BundleActivator {

	private static BundleContext context;

	static BundleContext getContext() {
		return context;
	}

	protected IndexServiceConnectorAdapterV1 indexServiceConnectorAdapter;

	protected ServicesCommand servicesCommand;
	protected AuthCommand authCommand;
	protected UserRegistryCommand userRegistryCommand;
	protected AppStoreCommand appStoreCommand;
	protected DomainServiceCommand domainMgmtCommand;
	protected TransferAgentCommand transferAgentCommand;

	@Override
	public void start(BundleContext bundleContext) throws Exception {
		ActivatorV1.context = bundleContext;

		this.indexServiceConnectorAdapter = new IndexServiceConnectorAdapterV1() {
			@Override
			public void connectorAdded(IndexServiceConnectorV1 connector) {
				doStart(ActivatorV1.context, connector);
			}

			@Override
			public void connectorRemoved(IndexServiceConnectorV1 connector) {
				doStop(ActivatorV1.context);
			}
		};
		this.indexServiceConnectorAdapter.start(bundleContext);
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		doStop(bundleContext);

		ActivatorV1.context = null;
	}

	protected void doStart(BundleContext bundleContext, IndexServiceConnectorV1 connector) {
		// Get load balancer for IndexProvider
		Map<Object, Object> indexProviderProps = new Hashtable<Object, Object>();
		PropertyUtil.loadProperty(bundleContext, indexProviderProps, org.orbit.infra.api.InfraConstants.ORBIT_INDEX_SERVICE_URL);
		// IndexServiceLoadBalancer indexServiceLoadBalancer = IndexServiceUtil.getIndexServiceLoadBalancer(connector, indexProviderProps);

		// Start commands
		// this.servicesCommand = new ServicesCommand(bundleContext, indexServiceLoadBalancer.createLoadBalancableIndexService());
		// this.servicesCommand.start();
		this.servicesCommand = new ServicesCommand();
		this.servicesCommand.start(bundleContext);

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
			this.servicesCommand.stop(bundleContext);
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

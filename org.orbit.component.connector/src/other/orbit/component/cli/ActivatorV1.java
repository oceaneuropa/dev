package other.orbit.component.cli;

import java.util.Hashtable;
import java.util.Map;

import org.orbit.component.cli.AppStoreClientCommand;
import org.orbit.component.cli.AuthClientCommand;
import org.orbit.component.cli.DomainManagementClientCommand;
import org.orbit.component.cli.NodeControlClientCommand;
import org.orbit.component.cli.ServicesClientCommand;
import org.orbit.component.cli.UserRegistryClientCommand;
import org.origin.common.util.PropertyUtil;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import other.orbit.infra.api.indexes.IndexServiceConnectorAdapterV1;
import other.orbit.infra.api.indexes.IndexServiceConnectorV1;

public class ActivatorV1 implements BundleActivator {

	private static BundleContext context;

	static BundleContext getContext() {
		return context;
	}

	protected IndexServiceConnectorAdapterV1 indexServiceConnectorAdapter;

	protected ServicesClientCommand servicesCommand;
	protected AuthClientCommand authCommand;
	protected UserRegistryClientCommand userRegistryCommand;
	protected AppStoreClientCommand appStoreCommand;
	protected DomainManagementClientCommand domainMgmtCommand;
	protected NodeControlClientCommand nodeControlCommand;

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
		this.servicesCommand = new ServicesClientCommand();
		this.servicesCommand.start(bundleContext);

		this.authCommand = new AuthClientCommand();
		this.authCommand.start(bundleContext);

		this.userRegistryCommand = new UserRegistryClientCommand();
		this.userRegistryCommand.start(bundleContext);

		this.appStoreCommand = new AppStoreClientCommand();
		this.appStoreCommand.start(bundleContext);

		this.domainMgmtCommand = new DomainManagementClientCommand();
		this.domainMgmtCommand.start(bundleContext);

		this.nodeControlCommand = new NodeControlClientCommand();
		this.nodeControlCommand.start(bundleContext);
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
			this.appStoreCommand.stop(bundleContext);
			this.appStoreCommand = null;
		}

		if (this.domainMgmtCommand != null) {
			this.domainMgmtCommand.stop(bundleContext);
			this.domainMgmtCommand = null;
		}

		if (this.nodeControlCommand != null) {
			this.nodeControlCommand.stop(bundleContext);
			this.nodeControlCommand = null;
		}
	}

}

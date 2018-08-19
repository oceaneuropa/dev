package org.orbit.component.runtime;

import java.util.Hashtable;
import java.util.Map;

import org.orbit.component.runtime.tier1.account.service.UserRegistryService;
import org.orbit.component.runtime.tier1.account.ws.UserRegistryServiceAdapter;
import org.orbit.component.runtime.tier1.auth.service.AuthService;
import org.orbit.component.runtime.tier1.auth.ws.AuthServiceAdapter;
import org.orbit.component.runtime.tier1.config.service.ConfigRegistryService;
import org.orbit.component.runtime.tier1.config.ws.ConfigRegistryServiceAdapter;
import org.orbit.component.runtime.tier1.identity.service.IdentityService;
import org.orbit.component.runtime.tier1.identity.ws.IdentityServiceAdapter;
import org.orbit.component.runtime.tier2.appstore.service.AppStoreService;
import org.orbit.component.runtime.tier2.appstore.ws.AppStoreServiceAdapter;
import org.orbit.component.runtime.tier3.domain.service.DomainManagementService;
import org.orbit.component.runtime.tier3.domain.ws.DomainServiceAdapter;
import org.orbit.component.runtime.tier3.nodecontrol.service.NodeControlService;
import org.orbit.component.runtime.tier3.nodecontrol.ws.NodeControlServiceAdapter;
import org.orbit.component.runtime.tier4.missioncontrol.service.MissionControlService;
import org.orbit.component.runtime.tier4.missioncontrol.ws.MissionControlAdapter;
import org.orbit.infra.api.indexes.IndexProvider;
import org.origin.common.rest.client.ServiceConnector;
import org.origin.common.rest.client.ServiceConnectorAdapter;
import org.origin.common.util.PropertyUtil;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OrbitServices {

	protected static Logger LOG = LoggerFactory.getLogger(OrbitServices.class);

	private static Object lock = new Object[0];
	private static OrbitServices instance = null;

	public static OrbitServices getInstance() {
		if (instance == null) {
			synchronized (lock) {
				if (instance == null) {
					instance = new OrbitServices();
				}
			}
		}
		return instance;
	}

	protected Map<Object, Object> properties;
	protected ServiceConnectorAdapter<IndexProvider> indexProviderConnector;

	// tier1
	protected UserRegistryServiceAdapter userRegistryServiceAdapter;
	protected IdentityServiceAdapter identityServiceAdapter;
	protected AuthServiceAdapter authServiceAdapter;
	protected ConfigRegistryServiceAdapter configRegistryServiceAdapter;

	// tier2
	protected AppStoreServiceAdapter appStoreServiceAdapter;

	// tier3
	protected DomainServiceAdapter domainServiceAdapter;
	protected NodeControlServiceAdapter nodeControlServiceAdapter;

	// tier4
	protected MissionControlAdapter missionControlServiceAdapter;

	/**
	 * 
	 * @param bundleContext
	 */
	public void start(final BundleContext bundleContext) {
		Map<Object, Object> properties = new Hashtable<Object, Object>();
		PropertyUtil.loadProperty(bundleContext, properties, ComponentsConstants.ORBIT_INDEX_SERVICE_URL);
		this.properties = properties;

		this.indexProviderConnector = new ServiceConnectorAdapter<IndexProvider>(IndexProvider.class) {
			@Override
			public void connectorAdded(ServiceConnector<IndexProvider> connector) {
				doStart(bundleContext);
			}

			@Override
			public void connectorRemoved(ServiceConnector<IndexProvider> connector) {
			}
		};
		this.indexProviderConnector.start(bundleContext);
	}

	/**
	 * 
	 * @param bundleContext
	 */
	public void stop(final BundleContext bundleContext) {
		doStop(bundleContext);
	}

	public void doStart(BundleContext bundleContext) {
		// Start service adapters
		// tier1
		this.userRegistryServiceAdapter = new UserRegistryServiceAdapter(this.properties);
		this.userRegistryServiceAdapter.start(bundleContext);

		this.identityServiceAdapter = new IdentityServiceAdapter(this.properties);
		this.identityServiceAdapter.start(bundleContext);

		this.authServiceAdapter = new AuthServiceAdapter(this.properties);
		this.authServiceAdapter.start(bundleContext);

		this.configRegistryServiceAdapter = new ConfigRegistryServiceAdapter(this.properties);
		this.configRegistryServiceAdapter.start(bundleContext);

		// tier2
		this.appStoreServiceAdapter = new AppStoreServiceAdapter(this.properties);
		this.appStoreServiceAdapter.start(bundleContext);

		// tier3
		this.domainServiceAdapter = new DomainServiceAdapter(this.properties);
		this.domainServiceAdapter.start(bundleContext);

		this.nodeControlServiceAdapter = new NodeControlServiceAdapter(this.properties);
		this.nodeControlServiceAdapter.start(bundleContext);

		// tier4
		this.missionControlServiceAdapter = new MissionControlAdapter(this.properties);
		this.missionControlServiceAdapter.start(bundleContext);
	}

	public void doStop(BundleContext bundleContext) {
		// Stop service adapters
		// tier4
		if (this.missionControlServiceAdapter != null) {
			this.missionControlServiceAdapter.stop(bundleContext);
			this.missionControlServiceAdapter = null;
		}

		// tier3
		if (this.domainServiceAdapter != null) {
			this.domainServiceAdapter.stop(bundleContext);
			this.domainServiceAdapter = null;
		}
		if (this.nodeControlServiceAdapter != null) {
			this.nodeControlServiceAdapter.stop(bundleContext);
			this.nodeControlServiceAdapter = null;
		}

		// tier2
		if (this.appStoreServiceAdapter != null) {
			this.appStoreServiceAdapter.stop(bundleContext);
			this.appStoreServiceAdapter = null;
		}

		// tier1
		if (this.userRegistryServiceAdapter != null) {
			this.userRegistryServiceAdapter.stop(bundleContext);
			this.userRegistryServiceAdapter = null;
		}
		if (this.identityServiceAdapter != null) {
			this.identityServiceAdapter.stop(bundleContext);
			this.identityServiceAdapter = null;
		}
		if (this.authServiceAdapter != null) {
			this.authServiceAdapter.stop(bundleContext);
			this.authServiceAdapter = null;
		}
		if (this.configRegistryServiceAdapter != null) {
			this.configRegistryServiceAdapter.stop(bundleContext);
			this.configRegistryServiceAdapter = null;
		}
	}

	// tier1
	public UserRegistryService getUserRegistryService() {
		return (this.userRegistryServiceAdapter != null) ? this.userRegistryServiceAdapter.getService() : null;
	}

	public IdentityService getIdentityService() {
		return (this.identityServiceAdapter != null) ? this.identityServiceAdapter.getService() : null;
	}

	public AuthService getAuthService() {
		return (this.authServiceAdapter != null) ? this.authServiceAdapter.getService() : null;
	}

	public ConfigRegistryService getConfigRegistryService() {
		return (this.configRegistryServiceAdapter != null) ? this.configRegistryServiceAdapter.getService() : null;
	}

	// tier2
	public AppStoreService getAppStoreService() {
		return (this.appStoreServiceAdapter != null) ? this.appStoreServiceAdapter.getService() : null;
	}

	// tier3
	public DomainManagementService getDomainService() {
		return (this.domainServiceAdapter != null) ? this.domainServiceAdapter.getService() : null;
	}

	public NodeControlService getTransferAgentService() {
		return (this.nodeControlServiceAdapter != null) ? this.nodeControlServiceAdapter.getService() : null;
	}

	// tier4
	public MissionControlService getMissionControlService() {
		return (this.missionControlServiceAdapter != null) ? this.missionControlServiceAdapter.getService() : null;
	}

}

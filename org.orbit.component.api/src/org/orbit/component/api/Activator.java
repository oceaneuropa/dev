package org.orbit.component.api;

import java.util.Hashtable;
import java.util.Map;

import org.orbit.component.api.tier1.account.UserRegistryConnector;
import org.orbit.component.api.tier1.auth.AuthConnector;
import org.orbit.component.api.tier1.config.ConfigRegistryConnector;
import org.orbit.component.api.tier2.appstore.AppStoreConnector;
import org.orbit.component.api.tier3.domain.DomainManagementConnector;
import org.orbit.component.api.tier3.transferagent.TransferAgentAdapter;
import org.orbit.component.cli.ServicesCommand;
import org.orbit.component.cli.tier0.ChannelCommand;
import org.orbit.component.cli.tier1.AuthCommand;
import org.orbit.component.cli.tier1.UserRegistryCommand;
import org.orbit.component.cli.tier2.AppStoreCommand;
import org.orbit.component.cli.tier3.DomainManagementCommand;
import org.orbit.component.cli.tier3.TransferAgentCommand;
import org.origin.common.util.PropertyUtil;
import org.origin.mgm.client.api.IndexService;
import org.origin.mgm.client.api.IndexServiceUtil;
import org.origin.mgm.client.loadbalance.IndexServiceLoadBalancer;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Activator implements BundleActivator {

	protected static Logger LOG = LoggerFactory.getLogger(Activator.class);

	protected static BundleContext context;
	protected static Activator instance;

	public static BundleContext getContext() {
		return context;
	}

	public static Activator getInstance() {
		return instance;
	}

	// Connector service trackers
	// tier1
	protected ServiceTracker<UserRegistryConnector, UserRegistryConnector> userRegistryConnectorTracker;
	protected ServiceTracker<ConfigRegistryConnector, ConfigRegistryConnector> configRegistryConnectorTracker;
	protected ServiceTracker<AuthConnector, AuthConnector> authConnectorTracker;
	// tier2
	protected ServiceTracker<AppStoreConnector, AppStoreConnector> appStoreConnectorTracker;
	// tier3
	protected ServiceTracker<DomainManagementConnector, DomainManagementConnector> domainMgmtConnectorTracker;
	protected TransferAgentAdapter transferAgentAdapter;

	// Commands
	protected ServicesCommand servicesCommand;
	protected ChannelCommand channelCommand;
	protected AuthCommand authCommand;
	protected UserRegistryCommand userRegistryCommand;
	protected AppStoreCommand appStoreCommand;
	protected DomainManagementCommand domainMgmtCommand;
	protected TransferAgentCommand transferAgentCommand;

	protected IndexServiceLoadBalancer indexServiceLoadBalancer;

	@Override
	public void start(final BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
		Activator.instance = this;

		// Get IndexProvider load balancer
		// load properties from accessing index service
		Map<Object, Object> indexProviderProps = new Hashtable<Object, Object>();
		PropertyUtil.loadProperty(bundleContext, indexProviderProps, OrbitConstants.COMPONENT_INDEX_SERVICE_URL);
		this.indexServiceLoadBalancer = IndexServiceUtil.getIndexServiceLoadBalancer(indexProviderProps);

		// Start connector service trackers
		// tier1
		this.userRegistryConnectorTracker = new ServiceTracker<UserRegistryConnector, UserRegistryConnector>(bundleContext, UserRegistryConnector.class, new ServiceTrackerCustomizer<UserRegistryConnector, UserRegistryConnector>() {
			@Override
			public UserRegistryConnector addingService(ServiceReference<UserRegistryConnector> reference) {
				LOG.debug(getClass().getName() + " UserRegistryConnector service is added.");
				return bundleContext.getService(reference);
			}

			@Override
			public void modifiedService(ServiceReference<UserRegistryConnector> reference, UserRegistryConnector arg1) {
				LOG.debug(getClass().getName() + " UserRegistryConnector service is modified.");
			}

			@Override
			public void removedService(ServiceReference<UserRegistryConnector> reference, UserRegistryConnector arg1) {
				LOG.debug(getClass().getName() + " UserRegistryConnector service is removed.");
			}
		});

		this.configRegistryConnectorTracker = new ServiceTracker<ConfigRegistryConnector, ConfigRegistryConnector>(bundleContext, ConfigRegistryConnector.class, new ServiceTrackerCustomizer<ConfigRegistryConnector, ConfigRegistryConnector>() {
			@Override
			public ConfigRegistryConnector addingService(ServiceReference<ConfigRegistryConnector> reference) {
				LOG.debug(getClass().getName() + " ConfigRegistryConnector service is added.");
				return bundleContext.getService(reference);
			}

			@Override
			public void modifiedService(ServiceReference<ConfigRegistryConnector> reference, ConfigRegistryConnector appStoreManager) {
				LOG.debug(getClass().getName() + " ConfigRegistryConnector service is modified.");
			}

			@Override
			public void removedService(ServiceReference<ConfigRegistryConnector> reference, ConfigRegistryConnector appStoreManager) {
				LOG.debug(getClass().getName() + " ConfigRegistryConnector service is removed.");
			}
		});
		this.configRegistryConnectorTracker.open();

		this.authConnectorTracker = new ServiceTracker<AuthConnector, AuthConnector>(bundleContext, AuthConnector.class, new ServiceTrackerCustomizer<AuthConnector, AuthConnector>() {
			@Override
			public AuthConnector addingService(ServiceReference<AuthConnector> reference) {
				LOG.debug(getClass().getName() + " AuthConnector service is added.");
				return bundleContext.getService(reference);
			}

			@Override
			public void modifiedService(ServiceReference<AuthConnector> reference, AuthConnector arg1) {
				LOG.debug(getClass().getName() + " AuthConnector service is modified.");
			}

			@Override
			public void removedService(ServiceReference<AuthConnector> reference, AuthConnector arg1) {
				LOG.debug(getClass().getName() + " AuthConnector service is removed.");
			}
		});

		// tier2
		this.appStoreConnectorTracker = new ServiceTracker<AppStoreConnector, AppStoreConnector>(bundleContext, AppStoreConnector.class, new ServiceTrackerCustomizer<AppStoreConnector, AppStoreConnector>() {
			@Override
			public AppStoreConnector addingService(ServiceReference<AppStoreConnector> reference) {
				LOG.debug(getClass().getName() + " AppStoreConnector service is added.");
				return bundleContext.getService(reference);
			}

			@Override
			public void modifiedService(ServiceReference<AppStoreConnector> reference, AppStoreConnector appStoreManager) {
				LOG.debug(getClass().getName() + " AppStoreConnector service is modified.");
			}

			@Override
			public void removedService(ServiceReference<AppStoreConnector> reference, AppStoreConnector appStoreManager) {
				LOG.debug(getClass().getName() + " AppStoreConnector service is removed.");
			}
		});
		this.appStoreConnectorTracker.open();

		// tier3
		this.domainMgmtConnectorTracker = new ServiceTracker<DomainManagementConnector, DomainManagementConnector>(bundleContext, DomainManagementConnector.class, new ServiceTrackerCustomizer<DomainManagementConnector, DomainManagementConnector>() {
			@Override
			public DomainManagementConnector addingService(ServiceReference<DomainManagementConnector> reference) {
				LOG.debug(getClass().getName() + " DomainMgmtConnector service is added.");
				return bundleContext.getService(reference);
			}

			@Override
			public void modifiedService(ServiceReference<DomainManagementConnector> reference, DomainManagementConnector arg1) {
				LOG.debug(getClass().getName() + " DomainMgmtConnector service is modified.");
			}

			@Override
			public void removedService(ServiceReference<DomainManagementConnector> reference, DomainManagementConnector arg1) {
				LOG.debug(getClass().getName() + " DomainMgmtConnector service is removed.");
			}
		});

		this.transferAgentAdapter = new TransferAgentAdapter();
		this.transferAgentAdapter.start(bundleContext);

		// Start CLI commands
		IndexService indexService = this.indexServiceLoadBalancer.createLoadBalancableIndexService();
		this.servicesCommand = new ServicesCommand(bundleContext, indexService);
		this.servicesCommand.start();

		this.channelCommand = new ChannelCommand();
		this.channelCommand.start(bundleContext);

		this.authCommand = new AuthCommand(bundleContext);
		this.authCommand.start();

		this.userRegistryCommand = new UserRegistryCommand(bundleContext);
		this.userRegistryCommand.start();

		this.appStoreCommand = new AppStoreCommand(bundleContext);
		this.appStoreCommand.start();

		this.domainMgmtCommand = new DomainManagementCommand(bundleContext);
		this.domainMgmtCommand.start();

		this.transferAgentCommand = new TransferAgentCommand(bundleContext);
		this.transferAgentCommand.start();
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		// Stop CLI commands
		if (this.servicesCommand != null) {
			this.servicesCommand.stop();
			this.servicesCommand = null;
		}

		if (this.channelCommand != null) {
			this.channelCommand.stop(bundleContext);
			this.channelCommand = null;
		}

		if (this.authCommand != null) {
			this.authCommand.stop();
			this.authCommand = null;
		}

		if (this.userRegistryCommand != null) {
			this.userRegistryCommand.stop();
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
			this.transferAgentCommand.stop();
			this.transferAgentCommand = null;
		}

		// Stop connector service trackers
		// tier3
		if (this.domainMgmtConnectorTracker != null) {
			this.domainMgmtConnectorTracker.close();
			this.domainMgmtConnectorTracker = null;
		}
		if (this.transferAgentAdapter != null) {
			this.transferAgentAdapter.stop(bundleContext);
			this.transferAgentAdapter = null;
		}

		// tier2
		if (this.appStoreConnectorTracker != null) {
			this.appStoreConnectorTracker.close();
			this.appStoreConnectorTracker = null;
		}

		// tier1
		if (this.configRegistryConnectorTracker != null) {
			this.configRegistryConnectorTracker.close();
			this.configRegistryConnectorTracker = null;
		}

		if (this.userRegistryConnectorTracker != null) {
			this.userRegistryConnectorTracker.close();
			this.userRegistryConnectorTracker = null;
		}

		if (this.authConnectorTracker != null) {
			this.authConnectorTracker.close();
			this.authConnectorTracker = null;
		}

		Activator.instance = null;
		Activator.context = null;
	}

	public UserRegistryConnector getUserRegistryConnector() {
		UserRegistryConnector connector = null;
		if (this.userRegistryConnectorTracker != null) {
			connector = this.userRegistryConnectorTracker.getService();
		}
		return connector;
	}

	public ConfigRegistryConnector getConfigRegistryConnector() {
		ConfigRegistryConnector connector = null;
		if (this.configRegistryConnectorTracker != null) {
			connector = this.configRegistryConnectorTracker.getService();
		}
		return connector;
	}

	public AuthConnector getAuthConnector() {
		AuthConnector connector = null;
		if (this.authConnectorTracker != null) {
			connector = this.authConnectorTracker.getService();
		}
		return connector;
	}

	public AppStoreConnector getAppStoreConnector() {
		AppStoreConnector connector = null;
		if (this.appStoreConnectorTracker != null) {
			connector = this.appStoreConnectorTracker.getService();
		}
		return connector;
	}

	public DomainManagementConnector getDomainMgmtConnector() {
		DomainManagementConnector connector = null;
		if (this.domainMgmtConnectorTracker != null) {
			connector = this.domainMgmtConnectorTracker.getService();
		}
		return connector;
	}

	public TransferAgentAdapter getTransferAgentAdapter() {
		return this.transferAgentAdapter;
	}

}

// protected ServiceTracker<OAuth2Connector, OAuth2Connector> oauth2ConnectorTracker;

// public OAuth2Connector getOAuth2Connector() {
// OAuth2Connector connector = null;
// if (this.oauth2ConnectorTracker != null) {
// connector = this.oauth2ConnectorTracker.getService();
// }
// return connector;
// }

// Stop OAuth2Connector ServiceTracker
// if (this.oauth2ConnectorTracker != null) {
// this.oauth2ConnectorTracker.close();
// this.oauth2ConnectorTracker = null;
// }

// Start ServiceTracker for tracking OAuth2Connector service
// this.oauth2ConnectorTracker = new ServiceTracker<OAuth2Connector, OAuth2Connector>(bundleContext, OAuth2Connector.class, new
// ServiceTrackerCustomizer<OAuth2Connector, OAuth2Connector>() {
// @Override
// public OAuth2Connector addingService(ServiceReference<OAuth2Connector> reference) {
// if (debug) {
// System.out.println(getClass().getName() + " OAuth2Connector service is added.");
// }
// return bundleContext.getService(reference);
// }
//
// @Override
// public void modifiedService(ServiceReference<OAuth2Connector> reference, OAuth2Connector arg1) {
// if (debug) {
// System.out.println(getClass().getName() + " OAuth2Connector service is modified.");
// }
// }
//
// @Override
// public void removedService(ServiceReference<OAuth2Connector> reference, OAuth2Connector arg1) {
// if (debug) {
// System.out.println(getClass().getName() + " OAuth2Connector service is removed.");
// }
// }
// });

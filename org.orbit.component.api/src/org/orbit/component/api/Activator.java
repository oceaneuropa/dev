package org.orbit.component.api;

import org.orbit.component.api.tier1.account.UserRegistryConnector;
import org.orbit.component.api.tier1.config.ConfigRegistryConnector;
import org.orbit.component.api.tier1.session.OAuth2Connector;
import org.orbit.component.api.tier2.appstore.AppStoreConnector;
import org.orbit.component.api.tier3.domain.DomainMgmtConnector;
import org.orbit.component.cli.tier2.AppStoreCommand;
import org.orbit.component.cli.tier3.DomainMgmtCommand;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

public class Activator implements BundleActivator {

	protected static BundleContext context;
	protected static Activator instance;

	public static BundleContext getContext() {
		return context;
	}

	public static Activator getInstance() {
		return instance;
	}

	protected ServiceTracker<UserRegistryConnector, UserRegistryConnector> userRegistryConnectorTracker;
	protected ServiceTracker<ConfigRegistryConnector, ConfigRegistryConnector> configRegistryConnectorTracker;
	protected ServiceTracker<OAuth2Connector, OAuth2Connector> oauth2ConnectorTracker;

	protected ServiceTracker<AppStoreConnector, AppStoreConnector> appStoreConnectorTracker;

	protected ServiceTracker<DomainMgmtConnector, DomainMgmtConnector> domainMgmtConnectorTracker;

	protected boolean debug = true;
	protected AppStoreCommand appStoreCommand;
	protected DomainMgmtCommand domainMgmtCommand;

	@Override
	public void start(final BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
		Activator.instance = this;

		// --------------------------------------------------------------------------------
		// Start tier1 service connectors
		// --------------------------------------------------------------------------------
		// Start ServiceTracker for tracking UserRegistryConnector service
		this.userRegistryConnectorTracker = new ServiceTracker<UserRegistryConnector, UserRegistryConnector>(bundleContext, UserRegistryConnector.class, new ServiceTrackerCustomizer<UserRegistryConnector, UserRegistryConnector>() {
			@Override
			public UserRegistryConnector addingService(ServiceReference<UserRegistryConnector> reference) {
				if (debug) {
					System.out.println(getClass().getName() + " UserRegistryConnector service is added.");
				}
				return bundleContext.getService(reference);
			}

			@Override
			public void modifiedService(ServiceReference<UserRegistryConnector> reference, UserRegistryConnector arg1) {
				if (debug) {
					System.out.println(getClass().getName() + " UserRegistryConnector service is modified.");
				}
			}

			@Override
			public void removedService(ServiceReference<UserRegistryConnector> reference, UserRegistryConnector arg1) {
				if (debug) {
					System.out.println(getClass().getName() + " UserRegistryConnector service is removed.");
				}
			}
		});

		// Start ServiceTracker for tracking ConfigRegistryConnector service
		this.configRegistryConnectorTracker = new ServiceTracker<ConfigRegistryConnector, ConfigRegistryConnector>(bundleContext, ConfigRegistryConnector.class, new ServiceTrackerCustomizer<ConfigRegistryConnector, ConfigRegistryConnector>() {
			@Override
			public ConfigRegistryConnector addingService(ServiceReference<ConfigRegistryConnector> reference) {
				if (debug) {
					System.out.println(getClass().getName() + " ConfigRegistryConnector service is added.");
				}
				return bundleContext.getService(reference);
			}

			@Override
			public void modifiedService(ServiceReference<ConfigRegistryConnector> reference, ConfigRegistryConnector appStoreManager) {
				if (debug) {
					System.out.println(getClass().getName() + " ConfigRegistryConnector service is modified.");
				}
			}

			@Override
			public void removedService(ServiceReference<ConfigRegistryConnector> reference, ConfigRegistryConnector appStoreManager) {
				if (debug) {
					System.out.println(getClass().getName() + " ConfigRegistryConnector service is removed.");
				}
			}
		});
		this.configRegistryConnectorTracker.open();

		// Start ServiceTracker for tracking OAuth2Connector service
		this.oauth2ConnectorTracker = new ServiceTracker<OAuth2Connector, OAuth2Connector>(bundleContext, OAuth2Connector.class, new ServiceTrackerCustomizer<OAuth2Connector, OAuth2Connector>() {
			@Override
			public OAuth2Connector addingService(ServiceReference<OAuth2Connector> reference) {
				if (debug) {
					System.out.println(getClass().getName() + " OAuth2Connector service is added.");
				}
				return bundleContext.getService(reference);
			}

			@Override
			public void modifiedService(ServiceReference<OAuth2Connector> reference, OAuth2Connector arg1) {
				if (debug) {
					System.out.println(getClass().getName() + " OAuth2Connector service is modified.");
				}
			}

			@Override
			public void removedService(ServiceReference<OAuth2Connector> reference, OAuth2Connector arg1) {
				if (debug) {
					System.out.println(getClass().getName() + " OAuth2Connector service is removed.");
				}
			}
		});

		// --------------------------------------------------------------------------------
		// Start tier2 service connectors
		// --------------------------------------------------------------------------------
		// Start ServiceTracker for tracking AppStoreConnector service
		this.appStoreConnectorTracker = new ServiceTracker<AppStoreConnector, AppStoreConnector>(bundleContext, AppStoreConnector.class, new ServiceTrackerCustomizer<AppStoreConnector, AppStoreConnector>() {
			@Override
			public AppStoreConnector addingService(ServiceReference<AppStoreConnector> reference) {
				if (debug) {
					System.out.println(getClass().getName() + " AppStoreConnector service is added.");
				}
				return bundleContext.getService(reference);
			}

			@Override
			public void modifiedService(ServiceReference<AppStoreConnector> reference, AppStoreConnector appStoreManager) {
				if (debug) {
					System.out.println(getClass().getName() + " AppStoreConnector service is modified.");
				}
			}

			@Override
			public void removedService(ServiceReference<AppStoreConnector> reference, AppStoreConnector appStoreManager) {
				if (debug) {
					System.out.println(getClass().getName() + " AppStoreConnector service is removed.");
				}
			}
		});
		this.appStoreConnectorTracker.open();

		// --------------------------------------------------------------------------------
		// Start tier3 service connectors
		// --------------------------------------------------------------------------------
		this.domainMgmtConnectorTracker = new ServiceTracker<DomainMgmtConnector, DomainMgmtConnector>(bundleContext, DomainMgmtConnector.class, new ServiceTrackerCustomizer<DomainMgmtConnector, DomainMgmtConnector>() {
			@Override
			public DomainMgmtConnector addingService(ServiceReference<DomainMgmtConnector> reference) {
				if (debug) {
					System.out.println(getClass().getName() + " DomainMgmtConnector service is added.");
				}
				return bundleContext.getService(reference);
			}

			@Override
			public void modifiedService(ServiceReference<DomainMgmtConnector> reference, DomainMgmtConnector arg1) {
				if (debug) {
					System.out.println(getClass().getName() + " DomainMgmtConnector service is modified.");
				}
			}

			@Override
			public void removedService(ServiceReference<DomainMgmtConnector> reference, DomainMgmtConnector arg1) {
				if (debug) {
					System.out.println(getClass().getName() + " DomainMgmtConnector service is removed.");
				}
			}
		});

		// --------------------------------------------------------------------------------
		// Start CLI commands
		// --------------------------------------------------------------------------------
		// Start AppStore command
		this.appStoreCommand = new AppStoreCommand(bundleContext);
		this.appStoreCommand.start();

		// Start DomainManagement command
		this.domainMgmtCommand = new DomainMgmtCommand(bundleContext);
		this.domainMgmtCommand.start();
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		// --------------------------------------------------------------------------------
		// Stop CLI commands
		// --------------------------------------------------------------------------------
		// Stop AppStore command
		if (this.appStoreCommand != null) {
			this.appStoreCommand.stop();
			this.appStoreCommand = null;
		}

		// Stop DomainManagement command
		if (this.domainMgmtCommand != null) {
			this.domainMgmtCommand.stop();
			this.domainMgmtCommand = null;
		}

		// --------------------------------------------------------------------------------
		// Stop tier3 service connectors
		// --------------------------------------------------------------------------------
		// Stop DomainMgmtConnector ServiceTracker
		if (this.domainMgmtConnectorTracker != null) {
			this.domainMgmtConnectorTracker.close();
			this.domainMgmtConnectorTracker = null;
		}

		// --------------------------------------------------------------------------------
		// Stop tier2 service connectors
		// --------------------------------------------------------------------------------
		// Stop AppStoreConnector ServiceTracker
		if (this.appStoreConnectorTracker != null) {
			this.appStoreConnectorTracker.close();
			this.appStoreConnectorTracker = null;
		}

		// --------------------------------------------------------------------------------
		// Stop tier1 service connectors
		// --------------------------------------------------------------------------------
		// Stop ConfigRegistryConnector ServiceTracker
		if (this.configRegistryConnectorTracker != null) {
			this.configRegistryConnectorTracker.close();
			this.configRegistryConnectorTracker = null;
		}

		// Stop UserRegistryConnector ServiceTracker
		if (this.userRegistryConnectorTracker != null) {
			this.userRegistryConnectorTracker.close();
			this.userRegistryConnectorTracker = null;
		}

		// Stop OAuth2Connector ServiceTracker
		if (this.oauth2ConnectorTracker != null) {
			this.oauth2ConnectorTracker.close();
			this.oauth2ConnectorTracker = null;
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

	public OAuth2Connector getOAuth2Connector() {
		OAuth2Connector connector = null;
		if (this.oauth2ConnectorTracker != null) {
			connector = this.oauth2ConnectorTracker.getService();
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

	public DomainMgmtConnector getDomainMgmtConnector() {
		DomainMgmtConnector connector = null;
		if (this.domainMgmtConnectorTracker != null) {
			connector = this.domainMgmtConnectorTracker.getService();
		}
		return connector;
	}

}

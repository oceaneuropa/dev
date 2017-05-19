package org.orbit.component.api;

import org.orbit.component.api.tier1.account.UserRegistryConnector;
import org.orbit.component.api.tier1.config.ConfigRegistryConnector;
import org.orbit.component.api.tier1.session.OAuth2Connector;
import org.orbit.component.api.tier2.appstore.AppStoreConnector;
import org.orbit.component.cli.tier2.AppStoreCommand;
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

	protected ServiceTracker<AppStoreConnector, AppStoreConnector> appStoreConnectorTracker;
	protected ServiceTracker<ConfigRegistryConnector, ConfigRegistryConnector> configRegistryConnectorTracker;
	protected ServiceTracker<UserRegistryConnector, UserRegistryConnector> userRegistryConnectorTracker;
	protected ServiceTracker<OAuth2Connector, OAuth2Connector> oauth2ConnectorTracker;

	protected boolean debug = true;
	protected AppStoreCommand appStoreCommand;

	@Override
	public void start(final BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
		Activator.instance = this;

		// --------------------------------------------------------------------------------
		// Start service connectors
		// --------------------------------------------------------------------------------
		// Start ServiceTracker for tracking AppStoreConnector service
		this.appStoreConnectorTracker = new ServiceTracker<AppStoreConnector, AppStoreConnector>(bundleContext, AppStoreConnector.class, new ServiceTrackerCustomizer<AppStoreConnector, AppStoreConnector>() {
			@Override
			public AppStoreConnector addingService(ServiceReference<AppStoreConnector> reference) {
				if (debug) {
					System.out.println("org.orbit.component.api.Activator AppStoreConnector service is added.");
				}
				return bundleContext.getService(reference);
			}

			@Override
			public void modifiedService(ServiceReference<AppStoreConnector> reference, AppStoreConnector appStoreManager) {
				if (debug) {
					System.out.println("org.orbit.component.api.Activator AppStoreConnector service is modified.");
				}
			}

			@Override
			public void removedService(ServiceReference<AppStoreConnector> reference, AppStoreConnector appStoreManager) {
				if (debug) {
					System.out.println("org.orbit.component.api.Activator AppStoreConnector service is removed.");
				}
			}
		});
		this.appStoreConnectorTracker.open();

		// Start ServiceTracker for tracking ConfigRegistryConnector service
		this.configRegistryConnectorTracker = new ServiceTracker<ConfigRegistryConnector, ConfigRegistryConnector>(bundleContext, ConfigRegistryConnector.class, new ServiceTrackerCustomizer<ConfigRegistryConnector, ConfigRegistryConnector>() {
			@Override
			public ConfigRegistryConnector addingService(ServiceReference<ConfigRegistryConnector> reference) {
				if (debug) {
					System.out.println("org.orbit.component.api.Activator ConfigRegistryConnector service is added.");
				}
				return bundleContext.getService(reference);
			}

			@Override
			public void modifiedService(ServiceReference<ConfigRegistryConnector> reference, ConfigRegistryConnector appStoreManager) {
				if (debug) {
					System.out.println("org.orbit.component.api.Activator ConfigRegistryConnector service is modified.");
				}
			}

			@Override
			public void removedService(ServiceReference<ConfigRegistryConnector> reference, ConfigRegistryConnector appStoreManager) {
				if (debug) {
					System.out.println("org.orbit.component.api.Activator ConfigRegistryConnector service is removed.");
				}
			}
		});
		this.configRegistryConnectorTracker.open();

		// Start ServiceTracker for tracking UserRegistryConnector service
		this.userRegistryConnectorTracker = new ServiceTracker<UserRegistryConnector, UserRegistryConnector>(bundleContext, UserRegistryConnector.class, new ServiceTrackerCustomizer<UserRegistryConnector, UserRegistryConnector>() {
			@Override
			public UserRegistryConnector addingService(ServiceReference<UserRegistryConnector> reference) {
				if (debug) {
					System.out.println("org.orbit.component.api.Activator UserRegistryConnector service is added.");
				}
				return bundleContext.getService(reference);
			}

			@Override
			public void modifiedService(ServiceReference<UserRegistryConnector> reference, UserRegistryConnector arg1) {
				if (debug) {
					System.out.println("org.orbit.component.api.Activator UserRegistryConnector service is modified.");
				}
			}

			@Override
			public void removedService(ServiceReference<UserRegistryConnector> reference, UserRegistryConnector arg1) {
				if (debug) {
					System.out.println("org.orbit.component.api.Activator UserRegistryConnector service is removed.");
				}
			}
		});

		// Start ServiceTracker for tracking OAuth2Connector service
		this.oauth2ConnectorTracker = new ServiceTracker<OAuth2Connector, OAuth2Connector>(bundleContext, OAuth2Connector.class, new ServiceTrackerCustomizer<OAuth2Connector, OAuth2Connector>() {
			@Override
			public OAuth2Connector addingService(ServiceReference<OAuth2Connector> reference) {
				if (debug) {
					System.out.println("org.orbit.component.api.Activator OAuth2Connector service is added.");
				}
				return bundleContext.getService(reference);
			}

			@Override
			public void modifiedService(ServiceReference<OAuth2Connector> reference, OAuth2Connector arg1) {
				if (debug) {
					System.out.println("org.orbit.component.api.Activator OAuth2Connector service is modified.");
				}
			}

			@Override
			public void removedService(ServiceReference<OAuth2Connector> reference, OAuth2Connector arg1) {
				if (debug) {
					System.out.println("org.orbit.component.api.Activator OAuth2Connector service is removed.");
				}
			}
		});

		// --------------------------------------------------------------------------------
		// Start CLI commands
		// --------------------------------------------------------------------------------
		// Start AppStore command
		this.appStoreCommand = new AppStoreCommand(bundleContext);
		this.appStoreCommand.start();
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		Activator.instance = null;
		Activator.context = null;

		// --------------------------------------------------------------------------------
		// Stop CLI commands
		// --------------------------------------------------------------------------------
		// Stop AppStore command
		if (this.appStoreCommand != null) {
			this.appStoreCommand.stop();
			this.appStoreCommand = null;
		}

		// --------------------------------------------------------------------------------
		// Stop service connectors
		// --------------------------------------------------------------------------------
		// Stop ServiceTracker for tracking AppStoreConnector service
		if (this.appStoreConnectorTracker != null) {
			this.appStoreConnectorTracker.close();
			this.appStoreConnectorTracker = null;
		}

		// Stop ServiceTracker for tracking ConfigRegistryConnector service
		if (this.configRegistryConnectorTracker != null) {
			this.configRegistryConnectorTracker.close();
			this.configRegistryConnectorTracker = null;
		}

		// Stop ServiceTracker for tracking UserRegistryConnector service
		if (this.userRegistryConnectorTracker != null) {
			this.userRegistryConnectorTracker.close();
			this.userRegistryConnectorTracker = null;
		}

		// Stop ServiceTracker for tracking OAuth2Connector service
		if (this.oauth2ConnectorTracker != null) {
			this.oauth2ConnectorTracker.close();
			this.oauth2ConnectorTracker = null;
		}
	}

	public AppStoreConnector getAppStoreConnector() {
		AppStoreConnector appStoreConnector = null;
		if (this.appStoreConnectorTracker != null) {
			appStoreConnector = this.appStoreConnectorTracker.getService();
		}
		return appStoreConnector;
	}

	public ConfigRegistryConnector getConfigRegistryConnector() {
		ConfigRegistryConnector configRegistryConnector = null;
		if (this.configRegistryConnectorTracker != null) {
			configRegistryConnector = this.configRegistryConnectorTracker.getService();
		}
		return configRegistryConnector;
	}

	public UserRegistryConnector getUserRegistryConnector() {
		UserRegistryConnector userRegistryConnector = null;
		if (this.userRegistryConnectorTracker != null) {
			userRegistryConnector = this.userRegistryConnectorTracker.getService();
		}
		return userRegistryConnector;
	}

	public OAuth2Connector getOAuth2Connector() {
		OAuth2Connector oauth2Connector = null;
		if (this.oauth2ConnectorTracker != null) {
			oauth2Connector = this.oauth2ConnectorTracker.getService();
		}
		return oauth2Connector;
	}

}

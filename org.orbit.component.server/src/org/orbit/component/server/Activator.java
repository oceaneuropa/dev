package org.orbit.component.server;

import java.util.Hashtable;
import java.util.Map;

import org.orbit.component.cli.OrbitServicesMgmtCommand;
import org.orbit.component.server.tier1.account.service.UserRegistryService;
import org.orbit.component.server.tier1.account.ws.UserRegistryWSApplication;
import org.orbit.component.server.tier1.config.service.ConfigRegistryService;
import org.orbit.component.server.tier1.config.ws.ConfigRegistryWSApplication;
import org.orbit.component.server.tier1.session.service.OAuth2Service;
import org.orbit.component.server.tier1.session.ws.OAuth2WSApplication;
import org.orbit.component.server.tier2.appstore.service.AppStoreService;
import org.orbit.component.server.tier2.appstore.ws.AppStoreWSApplication;
import org.orbit.component.server.tier3.domain.service.DomainMgmtService;
import org.orbit.component.server.tier3.domain.ws.DomainMgmtWSApplication;
import org.origin.common.util.PropertyUtil;
import org.origin.mgm.client.api.IndexProvider;
import org.origin.mgm.client.api.IndexServiceUtil;
import org.origin.mgm.client.loadbalance.IndexProviderLoadBalancer;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

public class Activator implements BundleActivator {

	protected static BundleContext bundleContext;

	public static BundleContext getBundleContext() {
		return bundleContext;
	}

	protected static ServiceTracker<UserRegistryService, UserRegistryService> userRegistryServiceTracker;
	protected static ServiceTracker<OAuth2Service, OAuth2Service> oauth2ServiceTracker;
	protected static ServiceTracker<ConfigRegistryService, ConfigRegistryService> configRegistryServiceTracker;
	protected static ServiceTracker<AppStoreService, AppStoreService> appStoreServiceTracker;
	protected static ServiceTracker<DomainMgmtService, DomainMgmtService> domainMgmtServiceTracker;

	public static UserRegistryService getUserRegistryService() {
		return (userRegistryServiceTracker != null) ? userRegistryServiceTracker.getService() : null;
	}

	public static OAuth2Service getOAuth2Service() {
		return (oauth2ServiceTracker != null) ? oauth2ServiceTracker.getService() : null;
	}

	public static ConfigRegistryService getConfigRegistryService() {
		return (configRegistryServiceTracker != null) ? configRegistryServiceTracker.getService() : null;
	}

	public static AppStoreService getAppStoreService() {
		return (appStoreServiceTracker != null) ? appStoreServiceTracker.getService() : null;
	}

	public static DomainMgmtService getDomainMgmtService() {
		return (domainMgmtServiceTracker != null) ? domainMgmtServiceTracker.getService() : null;
	}

	protected IndexProviderLoadBalancer indexProviderLoadBalancer;

	protected OrbitServicesMgmtCommand servicesMgmtCommand;

	protected UserRegistryWSApplication userRegistryApp;
	protected OAuth2WSApplication oauth2App;
	protected ConfigRegistryWSApplication configRegistryApp;
	protected AppStoreWSApplication appStoreApp;
	protected DomainMgmtWSApplication domainMgmtApp;

	@Override
	public void start(BundleContext bundleContext) throws Exception {
		System.out.println(getClass().getName() + ".start()");

		Activator.bundleContext = bundleContext;

		// -----------------------------------------------------------------------------
		// Get load balancer for IndexProvider
		// -----------------------------------------------------------------------------
		// load properties from accessing index service
		Map<Object, Object> indexProviderProps = new Hashtable<Object, Object>();
		PropertyUtil.loadProperty(bundleContext, indexProviderProps, OrbitConstants.COMPONENT_INDEX_SERVICE_URL);
		this.indexProviderLoadBalancer = IndexServiceUtil.getIndexProviderLoadBalancer(indexProviderProps);

		// -----------------------------------------------------------------------------
		// Open service trackers
		// -----------------------------------------------------------------------------
		// tier1
		openUserRegistryServiceTracker(bundleContext);
		openOAuth2ServiceTracker(bundleContext);
		openConfigRegistryServiceTracker(bundleContext);
		// tier2
		openAppStoreServiceTracker(bundleContext);
		// tier3
		openDomainMgmtServiceTracker(bundleContext);

		// --------------------------------------------------------------------------------
		// Start CLI commands
		// --------------------------------------------------------------------------------
		startServicesMgmtCommand(bundleContext);
		if (this.servicesMgmtCommand != null) {
			this.servicesMgmtCommand.startservice(OrbitServicesMgmtCommand.USER_REGISTRY);
			this.servicesMgmtCommand.startservice(OrbitServicesMgmtCommand.OAUTH2);
			this.servicesMgmtCommand.startservice(OrbitServicesMgmtCommand.CONFIGR_EGISTRY);
			this.servicesMgmtCommand.startservice(OrbitServicesMgmtCommand.APP_STORE);
			this.servicesMgmtCommand.startservice(OrbitServicesMgmtCommand.DOMAIN_MGMT);
		}
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		System.out.println(getClass().getName() + ".stop()");

		// --------------------------------------------------------------------------------
		// Stop CLI commands
		// --------------------------------------------------------------------------------
		if (this.servicesMgmtCommand != null) {
			this.servicesMgmtCommand.stopservice(OrbitServicesMgmtCommand.DOMAIN_MGMT);
			this.servicesMgmtCommand.stopservice(OrbitServicesMgmtCommand.APP_STORE);
			this.servicesMgmtCommand.stopservice(OrbitServicesMgmtCommand.CONFIGR_EGISTRY);
			this.servicesMgmtCommand.stopservice(OrbitServicesMgmtCommand.OAUTH2);
			this.servicesMgmtCommand.stopservice(OrbitServicesMgmtCommand.USER_REGISTRY);
		}
		stopServicesMgmtCommand();

		// -----------------------------------------------------------------------------
		// Close service trackers
		// -----------------------------------------------------------------------------
		// tier3
		closeDomainMgmtServiceTracker();
		// tier2
		closeAppStoreServiceTracker();
		// tier1
		closeConfigRegistryServiceTracker();
		closeOAuth2ServiceTracker();
		closeUserRegistryServiceTracker();

		Activator.bundleContext = null;
	}

	public IndexProvider createIndexProvider() {
		if (this.indexProviderLoadBalancer == null) {
			return null;
		}
		return this.indexProviderLoadBalancer.createLoadBalancableIndexProvider();
	}

	protected void startServicesMgmtCommand(BundleContext bundleContext) {
		this.servicesMgmtCommand = new OrbitServicesMgmtCommand();
		this.servicesMgmtCommand.start(bundleContext);
	}

	protected void stopServicesMgmtCommand() {
		if (this.servicesMgmtCommand != null) {
			this.servicesMgmtCommand.stop(bundleContext);
			this.servicesMgmtCommand = null;
		}
	}

	protected void openUserRegistryServiceTracker(final BundleContext bundleContext) {
		userRegistryServiceTracker = new ServiceTracker<UserRegistryService, UserRegistryService>(bundleContext, UserRegistryService.class, new ServiceTrackerCustomizer<UserRegistryService, UserRegistryService>() {
			@Override
			public UserRegistryService addingService(ServiceReference<UserRegistryService> reference) {
				UserRegistryService userRegistryService = bundleContext.getService(reference);
				System.out.println("UserRegistryService [" + userRegistryService + "] is added.");

				startUserRegistryWebService(userRegistryService);
				return userRegistryService;
			}

			@Override
			public void modifiedService(ServiceReference<UserRegistryService> reference, UserRegistryService userRegistryService) {
				System.out.println("UserRegistryService [" + userRegistryService + "] is modified.");

				stopUserRegistryWebService();
				startUserRegistryWebService(userRegistryService);
			}

			@Override
			public void removedService(ServiceReference<UserRegistryService> reference, UserRegistryService userRegistryService) {
				System.out.println("UserRegistryService [" + userRegistryService + "] is removed.");

				stopUserRegistryWebService();
			}
		});
		userRegistryServiceTracker.open();
	}

	protected void closeUserRegistryServiceTracker() {
		if (userRegistryServiceTracker != null) {
			userRegistryServiceTracker.close();
			userRegistryServiceTracker = null;
		}
	}

	protected void openOAuth2ServiceTracker(final BundleContext bundleContext) {
		oauth2ServiceTracker = new ServiceTracker<OAuth2Service, OAuth2Service>(bundleContext, OAuth2Service.class, new ServiceTrackerCustomizer<OAuth2Service, OAuth2Service>() {
			@Override
			public OAuth2Service addingService(ServiceReference<OAuth2Service> reference) {
				OAuth2Service oauth2Service = bundleContext.getService(reference);
				System.out.println("OAuth2Service [" + oauth2Service + "] is added.");

				startOauth2WebService(oauth2Service);
				return oauth2Service;
			}

			@Override
			public void modifiedService(ServiceReference<OAuth2Service> reference, OAuth2Service oauth2Service) {
				System.out.println("OAuth2Service [" + oauth2Service + "] is modified.");

				stopOauth2WebService();
				startOauth2WebService(oauth2Service);
			}

			@Override
			public void removedService(ServiceReference<OAuth2Service> reference, OAuth2Service oauth2Service) {
				System.out.println("OAuth2Service [" + oauth2Service + "] is removed.");

				stopOauth2WebService();
			}
		});
		oauth2ServiceTracker.open();
	}

	protected void closeOAuth2ServiceTracker() {
		if (oauth2ServiceTracker != null) {
			oauth2ServiceTracker.close();
			oauth2ServiceTracker = null;
		}
	}

	protected void openConfigRegistryServiceTracker(final BundleContext bundleContext) {
		// Start ConfigRegistry service tracker
		configRegistryServiceTracker = new ServiceTracker<ConfigRegistryService, ConfigRegistryService>(bundleContext, ConfigRegistryService.class, new ServiceTrackerCustomizer<ConfigRegistryService, ConfigRegistryService>() {
			@Override
			public ConfigRegistryService addingService(ServiceReference<ConfigRegistryService> reference) {
				ConfigRegistryService configRegistryService = bundleContext.getService(reference);
				System.out.println("ConfigRegistryService [" + configRegistryService + "] is added.");

				startConfigRegistryWebService(configRegistryService);
				return configRegistryService;
			}

			@Override
			public void modifiedService(ServiceReference<ConfigRegistryService> reference, ConfigRegistryService configRegistryService) {
				System.out.println("ConfigRegistryService [" + configRegistryService + "] is modified.");

				stopConfigRegistryWebService();
				startConfigRegistryWebService(configRegistryService);
			}

			@Override
			public void removedService(ServiceReference<ConfigRegistryService> reference, ConfigRegistryService configRegistryService) {
				System.out.println("ConfigRegistryService [" + configRegistryService + "] is removed.");

				stopConfigRegistryWebService();
			}
		});
		configRegistryServiceTracker.open();
	}

	protected void closeConfigRegistryServiceTracker() {
		if (configRegistryServiceTracker != null) {
			configRegistryServiceTracker.close();
			configRegistryServiceTracker = null;
		}
	}

	protected void openAppStoreServiceTracker(final BundleContext bundleContext) {
		appStoreServiceTracker = new ServiceTracker<AppStoreService, AppStoreService>(bundleContext, AppStoreService.class, new ServiceTrackerCustomizer<AppStoreService, AppStoreService>() {
			@Override
			public AppStoreService addingService(ServiceReference<AppStoreService> reference) {
				AppStoreService appStoreService = bundleContext.getService(reference);
				System.out.println("AppStoreService [" + appStoreService + "] is added.");

				startAppStoreWebService(appStoreService);
				return appStoreService;
			}

			@Override
			public void modifiedService(ServiceReference<AppStoreService> reference, AppStoreService appStoreService) {
				System.out.println("AppStoreService [" + appStoreService + "] is modified.");

				stopAppStoreWebService();
				startAppStoreWebService(appStoreService);
			}

			@Override
			public void removedService(ServiceReference<AppStoreService> reference, AppStoreService appStoreService) {
				System.out.println("AppStoreService [" + appStoreService + "] is removed.");

				stopAppStoreWebService();
			}
		});
		appStoreServiceTracker.open();
	}

	protected void closeAppStoreServiceTracker() {
		if (appStoreServiceTracker != null) {
			appStoreServiceTracker.close();
			appStoreServiceTracker = null;
		}
	}

	protected void openDomainMgmtServiceTracker(final BundleContext bundleContext) {
		domainMgmtServiceTracker = new ServiceTracker<DomainMgmtService, DomainMgmtService>(bundleContext, DomainMgmtService.class, new ServiceTrackerCustomizer<DomainMgmtService, DomainMgmtService>() {
			@Override
			public DomainMgmtService addingService(ServiceReference<DomainMgmtService> reference) {
				DomainMgmtService domainMgmtService = bundleContext.getService(reference);
				System.out.println("DomainMgmtService [" + domainMgmtService + "] is added.");

				startDomainMgmtWebService(domainMgmtService);
				return domainMgmtService;
			}

			@Override
			public void modifiedService(ServiceReference<DomainMgmtService> reference, DomainMgmtService domainMgmtService) {
				System.out.println("DomainMgmtService [" + domainMgmtService + "] is modified.");

				stopDomainMgmtWebService();
				startDomainMgmtWebService(domainMgmtService);
			}

			@Override
			public void removedService(ServiceReference<DomainMgmtService> reference, DomainMgmtService domainMgmtService) {
				System.out.println("DomainMgmtService [" + domainMgmtService + "] is removed.");

				stopDomainMgmtWebService();
			}
		});
		domainMgmtServiceTracker.open();
	}

	protected void closeDomainMgmtServiceTracker() {
		if (domainMgmtServiceTracker != null) {
			domainMgmtServiceTracker.close();
			domainMgmtServiceTracker = null;
		}
	}

	protected void startUserRegistryWebService(UserRegistryService userRegistryService) {
		this.userRegistryApp = new UserRegistryWSApplication();
		this.userRegistryApp.setBundleContext(bundleContext);
		this.userRegistryApp.setContextRoot(userRegistryService.getContextRoot());
		this.userRegistryApp.setIndexProvider(this.indexProviderLoadBalancer.createLoadBalancableIndexProvider());
		this.userRegistryApp.start();
	}

	protected void stopUserRegistryWebService() {
		if (this.userRegistryApp != null) {
			this.userRegistryApp.stop();
			this.userRegistryApp = null;
		}
	}

	protected void startOauth2WebService(OAuth2Service oauth2Service) {
		this.oauth2App = new OAuth2WSApplication();
		this.oauth2App.setBundleContext(bundleContext);
		this.oauth2App.setContextRoot(oauth2Service.getContextRoot());
		this.oauth2App.setIndexProvider(this.indexProviderLoadBalancer.createLoadBalancableIndexProvider());
		this.oauth2App.start();
	}

	protected void stopOauth2WebService() {
		if (this.oauth2App != null) {
			this.oauth2App.stop();
			this.oauth2App = null;
		}
	}

	protected void startConfigRegistryWebService(ConfigRegistryService configRegistryService) {
		this.configRegistryApp = new ConfigRegistryWSApplication();
		this.configRegistryApp.setBundleContext(bundleContext);
		this.configRegistryApp.setContextRoot(configRegistryService.getContextRoot());
		this.configRegistryApp.setIndexProvider(this.indexProviderLoadBalancer.createLoadBalancableIndexProvider());
		this.configRegistryApp.start();
	}

	protected void stopConfigRegistryWebService() {
		if (this.configRegistryApp != null) {
			this.configRegistryApp.stop();
			this.configRegistryApp = null;
		}
	}

	protected void startAppStoreWebService(AppStoreService appStoreService) {
		this.appStoreApp = new AppStoreWSApplication();
		this.appStoreApp.setBundleContext(bundleContext);
		this.appStoreApp.setContextRoot(appStoreService.getContextRoot());
		this.appStoreApp.setIndexProvider(this.indexProviderLoadBalancer.createLoadBalancableIndexProvider());
		this.appStoreApp.start();
	}

	protected void stopAppStoreWebService() {
		if (this.appStoreApp != null) {
			this.appStoreApp.stop();
			this.appStoreApp = null;
		}
	}

	protected void startDomainMgmtWebService(DomainMgmtService domainMgmtService) {
		this.domainMgmtApp = new DomainMgmtWSApplication();
		this.domainMgmtApp.setBundleContext(bundleContext);
		this.domainMgmtApp.setContextRoot(domainMgmtService.getContextRoot());
		this.domainMgmtApp.setIndexProvider(this.indexProviderLoadBalancer.createLoadBalancableIndexProvider());
		this.domainMgmtApp.start();
	}

	protected void stopDomainMgmtWebService() {
		if (this.domainMgmtApp != null) {
			this.domainMgmtApp.stop();
			this.domainMgmtApp = null;
		}
	}

}

//// -----------------------------------------------------------------------------
//// Stop web services
//// -----------------------------------------------------------------------------
//// tier3
// stopDomainMgmtWebService();
//// tier2
// stopAppStoreWebService();
//// tier1
// stopConfigRegistryWebService();
// stopOauth2WebService();
// stopUserRegistryWebService();

// -----------------------------------------------------------------------------
// Get common properties
// -----------------------------------------------------------------------------
// Map<Object, Object> commonProps = new Hashtable<Object, Object>();
// PropertyUtil.loadProperty(bundleContext, commonProps, OrbitConstants.ORBIT_HOST_URL);
// String hostURL = (String) commonProps.get(OrbitConstants.ORBIT_HOST_URL);
// System.out.println("hostURL = " + hostURL);

// Map<Object, Object> commonProps = new Hashtable<Object, Object>();
// PropertyUtil.loadProperty(bundleContext, commonProps, OrbitConstants.OSGI_HTTP_PORT);
// PropertyUtil.loadProperty(bundleContext, commonProps, OrbitConstants.ORBIT_HTTP_HOST);
// PropertyUtil.loadProperty(bundleContext, commonProps, OrbitConstants.ORBIT_HTTP_PORT);
// PropertyUtil.loadProperty(bundleContext, commonProps, OrbitConstants.ORBIT_HTTP_CONTEXTROOT);
// PropertyUtil.loadProperty(bundleContext, commonProps, OrbitConstants.ORBIT_HOST_URL);

// // get http host
// String host = (String) commonProps.get(OrbitConstants.ORBIT_HTTP_HOST);
// if (host == null || host.isEmpty()) {
// try {
// InetAddress address = InetAddress.getLocalHost();
// host = address.getHostAddress();
// } catch (UnknownHostException e) {
// e.printStackTrace();
// }
// }
//
// // get http port
// String port = (String) commonProps.get(OrbitConstants.OSGI_HTTP_PORT);
// if (port == null || port.isEmpty()) {
// port = (String) commonProps.get(OrbitConstants.ORBIT_HTTP_PORT);
// }
// if (port == null || port.isEmpty()) {
// port = "80";
// }
//
// // current host URL
// String hostURL = "http://" + host + ":" + port;
// System.out.println("hostURL = " + hostURL);

// port = (String) commonProps.get(OrbitConstants.ORBIT_HOST_URL);

// public static void main(String[] args) {
// String host = "";
// try {
// InetAddress address = InetAddress.getLocalHost();
// host = address.getHostAddress();
// } catch (UnknownHostException e) {
// e.printStackTrace();
// }
// System.out.println("host = " + host);
// }

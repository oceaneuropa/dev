package org.orbit.component.server;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Hashtable;
import java.util.Map;

import org.orbit.component.server.tier1.account.service.UserRegistryService;
import org.orbit.component.server.tier1.account.service.impl.UserRegistryServiceDatabaseImpl;
import org.orbit.component.server.tier1.account.ws.UserRegistryWSApplication;
import org.orbit.component.server.tier1.config.service.ConfigRegistryService;
import org.orbit.component.server.tier1.config.service.impl.ConfigRegistryServiceDatabaseImpl;
import org.orbit.component.server.tier1.config.ws.ConfigRegistryWSApplication;
import org.orbit.component.server.tier1.session.service.OAuth2Service;
import org.orbit.component.server.tier1.session.service.impl.OAuth2ServiceDatabaseImpl;
import org.orbit.component.server.tier1.session.ws.OAuth2WSApplication;
import org.orbit.component.server.tier2.appstore.service.AppStoreService;
import org.orbit.component.server.tier2.appstore.service.impl.AppStoreServiceDatabaseImpl;
import org.orbit.component.server.tier2.appstore.ws.AppStoreWSApplication;
import org.orbit.component.server.tier3.domain.service.DomainMgmtService;
import org.orbit.component.server.tier3.domain.service.impl.DomainMgmtServiceDatabaseImpl;
import org.orbit.component.server.tier3.domain.ws.DomainMgmtWSApplication;
import org.origin.common.util.PropertyUtil;
import org.origin.mgm.client.api.IndexServiceUtil;
import org.origin.mgm.client.loadbalance.IndexProviderLoadBalancer;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {

	protected static BundleContext bundleContext;

	protected static AppStoreServiceDatabaseImpl appStoreService;
	protected static ConfigRegistryServiceDatabaseImpl configRegistryService;
	protected static UserRegistryServiceDatabaseImpl userRegistryService;
	protected static OAuth2ServiceDatabaseImpl oauth2Service;
	protected static DomainMgmtServiceDatabaseImpl domainMgmtService;

	public static BundleContext getBundleContext() {
		return bundleContext;
	}

	public static UserRegistryService getUserRegistryService() {
		return userRegistryService;
	}

	public static OAuth2Service getOAuth2Service() {
		return oauth2Service;
	}

	public static ConfigRegistryService getConfigRegistryService() {
		return configRegistryService;
	}

	public static AppStoreService getAppStoreService() {
		return appStoreService;
	}

	public static DomainMgmtService getDomainManagementService() {
		return domainMgmtService;
	}

	protected IndexProviderLoadBalancer indexProviderLoadBalancer;

	// tier1
	protected UserRegistryWSApplication userRegistryApp;
	protected OAuth2WSApplication oauth2App;
	protected ConfigRegistryWSApplication configRegistryApp;

	// tier2
	protected AppStoreWSApplication appStoreApp;

	// tier3
	protected DomainMgmtWSApplication domainMgmtApp;

	protected boolean runUserRegistry = true;
	protected boolean runOAuth2Service = true;
	protected boolean runConfigRegistry = true;
	protected boolean runAppStore = true;
	protected boolean runDomainMgmtService = true;

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
		// Start services
		// -----------------------------------------------------------------------------
		// 1. Start UserRegistry service
		if (runUserRegistry) {
			UserRegistryServiceDatabaseImpl userRegistryService = new UserRegistryServiceDatabaseImpl();
			userRegistryService.start(bundleContext);
			Activator.userRegistryService = userRegistryService;
		}

		// 2. Start OAuth2 service
		if (runOAuth2Service) {
			OAuth2ServiceDatabaseImpl oauth2Service = new OAuth2ServiceDatabaseImpl();
			oauth2Service.start(bundleContext);
			Activator.oauth2Service = oauth2Service;
		}

		// 3. Start ConfigRegistry service
		if (runConfigRegistry) {
			ConfigRegistryServiceDatabaseImpl configRegistryService = new ConfigRegistryServiceDatabaseImpl();
			configRegistryService.start(bundleContext);
			Activator.configRegistryService = configRegistryService;
		}

		// 4. Start AppStore service
		if (runAppStore) {
			AppStoreServiceDatabaseImpl appStoreService = new AppStoreServiceDatabaseImpl();
			appStoreService.start(bundleContext);
			Activator.appStoreService = appStoreService;
		}

		// 5. Start DomainManagement service
		if (runDomainMgmtService) {
			DomainMgmtServiceDatabaseImpl domainMgmtService = new DomainMgmtServiceDatabaseImpl();
			domainMgmtService.start(bundleContext);
			Activator.domainMgmtService = domainMgmtService;
		}

		// -----------------------------------------------------------------------------
		// Start web applications
		// -----------------------------------------------------------------------------
		// 1. Start UserRegistry web application
		if (runUserRegistry) {
			this.userRegistryApp = new UserRegistryWSApplication();
			this.userRegistryApp.setBundleContext(bundleContext);
			this.userRegistryApp.setContextRoot(userRegistryService.getContextRoot());
			this.userRegistryApp.setIndexProvider(this.indexProviderLoadBalancer.createLoadBalancableIndexProvider());
			this.userRegistryApp.start();
		}

		// 2. Start OAuth2 web application
		if (runOAuth2Service) {
			this.oauth2App = new OAuth2WSApplication();
			this.oauth2App.setBundleContext(bundleContext);
			this.oauth2App.setContextRoot(oauth2App.getContextRoot());
			this.oauth2App.setIndexProvider(this.indexProviderLoadBalancer.createLoadBalancableIndexProvider());
			this.oauth2App.start();
		}

		// 3. Start ConfigRegistry web application
		if (runConfigRegistry) {
			this.configRegistryApp = new ConfigRegistryWSApplication();
			this.configRegistryApp.setBundleContext(bundleContext);
			this.configRegistryApp.setContextRoot(configRegistryService.getContextRoot());
			this.configRegistryApp.setIndexProvider(this.indexProviderLoadBalancer.createLoadBalancableIndexProvider());
			this.configRegistryApp.start();
		}

		// 4. Start AppStore web application
		if (runAppStore) {
			this.appStoreApp = new AppStoreWSApplication();
			this.appStoreApp.setBundleContext(bundleContext);
			this.appStoreApp.setContextRoot(appStoreService.getContextRoot());
			this.appStoreApp.setIndexProvider(this.indexProviderLoadBalancer.createLoadBalancableIndexProvider());
			this.appStoreApp.start();
		}

		// 5. Start DomainManagement web application
		if (runDomainMgmtService) {
			this.domainMgmtApp = new DomainMgmtWSApplication();
			this.domainMgmtApp.setBundleContext(bundleContext);
			this.domainMgmtApp.setContextRoot(domainMgmtApp.getContextRoot());
			this.domainMgmtApp.setIndexProvider(this.indexProviderLoadBalancer.createLoadBalancableIndexProvider());
			this.domainMgmtApp.start();
		}
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		System.out.println(getClass().getName() + ".stop()");

		Activator.bundleContext = null;

		// -----------------------------------------------------------------------------
		// Stop web applications
		// -----------------------------------------------------------------------------
		// 1. Stop UserRegistry web application
		if (runUserRegistry) {
			if (this.userRegistryApp != null) {
				this.userRegistryApp.stop();
				this.userRegistryApp = null;
			}
		}

		// 2. Stop OAuth2 web application
		if (runOAuth2Service) {
			if (this.oauth2App != null) {
				this.oauth2App.stop();
				this.oauth2App = null;
			}
		}

		// 3. Stop ConfigRegistry web application
		if (runConfigRegistry) {
			if (this.configRegistryApp != null) {
				this.configRegistryApp.stop();
				this.configRegistryApp = null;
			}
		}

		// 4. Stop AppStore web application
		if (runAppStore) {
			if (this.appStoreApp != null) {
				this.appStoreApp.stop();
				this.appStoreApp = null;
			}
		}

		// 5. Stop DomainManagement web application
		if (runDomainMgmtService) {
			if (this.domainMgmtApp != null) {
				this.domainMgmtApp.stop();
				this.domainMgmtApp = null;
			}
		}

		// -----------------------------------------------------------------------------
		// Stop services
		// -----------------------------------------------------------------------------
		// 1. Stop UserRegistry service
		if (runUserRegistry) {
			if (Activator.userRegistryService != null) {
				Activator.userRegistryService.stop();
				Activator.userRegistryService = null;
			}
		}

		// 2. Stop OAuth2 service
		if (runOAuth2Service) {
			if (Activator.oauth2Service != null) {
				Activator.oauth2Service.stop();
				Activator.oauth2Service = null;
			}
		}

		// 3. Stop ConfigRegistry service
		if (runConfigRegistry) {
			if (Activator.configRegistryService != null) {
				Activator.configRegistryService.stop();
				Activator.configRegistryService = null;
			}
		}

		// 4. Stop AppStore service
		if (runAppStore) {
			if (Activator.appStoreService != null) {
				Activator.appStoreService.stop();
				Activator.appStoreService = null;
			}
		}

		// 5. Stop DomainManagement service
		if (runDomainMgmtService) {
			if (Activator.domainMgmtService != null) {
				Activator.domainMgmtService.stop();
				Activator.domainMgmtService = null;
			}
		}
	}

	public static void main(String[] args) {
		String host = "";
		try {
			InetAddress address = InetAddress.getLocalHost();
			host = address.getHostAddress();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		System.out.println("host = " + host);
	}

}

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

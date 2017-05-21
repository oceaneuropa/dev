package org.orbit.component.cli;

import java.util.Hashtable;
import java.util.Map;

import org.apache.felix.service.command.Descriptor;
import org.apache.felix.service.command.Parameter;
import org.orbit.component.server.OrbitConstants;
import org.orbit.component.server.tier1.account.service.impl.UserRegistryServiceDatabaseImpl;
import org.orbit.component.server.tier1.config.service.impl.ConfigRegistryServiceDatabaseImpl;
import org.orbit.component.server.tier1.session.service.impl.OAuth2ServiceDatabaseImpl;
import org.orbit.component.server.tier2.appstore.service.impl.AppStoreServiceDatabaseImpl;
import org.orbit.component.server.tier3.domain.service.impl.DomainMgmtServiceDatabaseImpl;
import org.origin.common.osgi.OSGiServiceUtil;
import org.origin.common.util.PropertyUtil;
import org.osgi.framework.BundleContext;

public class OrbitServicesMgmtCommand {

	// service types
	public static final String USER_REGISTRY = "userregistry";
	public static final String OAUTH2 = "oauth2";
	public static final String CONFIGR_EGISTRY = "configregistry";
	public static final String APP_STORE = "appstore";
	public static final String DOMAIN_MGMT = "domainmgmt";

	protected BundleContext bundleContext;

	protected ConfigRegistryServiceDatabaseImpl configRegistryService;
	protected OAuth2ServiceDatabaseImpl oauth2Service;
	protected UserRegistryServiceDatabaseImpl userRegistryService;
	protected AppStoreServiceDatabaseImpl appStoreService;
	protected DomainMgmtServiceDatabaseImpl domainMgmtService;

	/**
	 * 
	 * @param bundleContext
	 */
	public void start(BundleContext bundleContext) {
		System.out.println(getClass().getSimpleName() + ".start()");
		this.bundleContext = bundleContext;

		Hashtable<String, Object> props = new Hashtable<String, Object>();
		props.put("osgi.command.scope", "orbitmgmt");
		props.put("osgi.command.function", new String[] { "startservice", "stopservice" });
		OSGiServiceUtil.register(bundleContext, OrbitServicesMgmtCommand.class.getName(), this, props);
	}

	/**
	 * 
	 * @param bundleContext
	 */
	public void stop(BundleContext bundleContext) {
		System.out.println(getClass().getSimpleName() + ".stop()");

		OSGiServiceUtil.unregister(OrbitServicesMgmtCommand.class.getName(), this);

		this.bundleContext = null;
	}

	/**
	 * Start a service
	 * 
	 * @param service
	 */
	@Descriptor("Start a service")
	public void startservice(@Descriptor("The service to start") @Parameter(names = { "-s", "--service" }, absentValue = "null") String service) {
		System.out.println("starting service: " + service);

		if (service == null || service.isEmpty()) {
			System.out.println("service name is empty.");
			return;
		}

		Map<Object, Object> configProps = new Hashtable<Object, Object>();

		if (USER_REGISTRY.equalsIgnoreCase(service)) {
			boolean startUserRegistry = false;
			PropertyUtil.loadProperty(this.bundleContext, configProps, OrbitConstants.COMPONENT_USER_REGISTRY_NAME);
			if (configProps.containsKey(OrbitConstants.COMPONENT_USER_REGISTRY_NAME)) {
				startUserRegistry = true;
			}
			System.out.println("Start user registry service: " + (startUserRegistry ? "yes" : "no"));
			if (startUserRegistry) {
				startUserRegistryService(this.bundleContext);
			}

		} else if (OAUTH2.equalsIgnoreCase(service)) {
			boolean startOAuth2Service = false;
			PropertyUtil.loadProperty(this.bundleContext, configProps, OrbitConstants.COMPONENT_OAUTH2_NAME);
			if (configProps.containsKey(OrbitConstants.COMPONENT_OAUTH2_NAME)) {
				startOAuth2Service = true;
			}
			System.out.println("Start oauth2 service: " + (startOAuth2Service ? "yes" : "no"));
			if (startOAuth2Service) {
				startOAuth2Service(this.bundleContext);
			}

		} else if (CONFIGR_EGISTRY.equalsIgnoreCase(service)) {
			boolean startConfigRegistry = false;
			PropertyUtil.loadProperty(this.bundleContext, configProps, OrbitConstants.COMPONENT_CONFIG_REGISTRY_NAME);
			if (configProps.containsKey(OrbitConstants.COMPONENT_CONFIG_REGISTRY_NAME)) {
				startConfigRegistry = true;
			}
			System.out.println("Start config registry service: " + (startConfigRegistry ? "yes" : "no"));
			if (startConfigRegistry) {
				startConfigRegistryService(this.bundleContext);
			}

		} else if (APP_STORE.equalsIgnoreCase(service)) {
			boolean startAppStore = false;
			PropertyUtil.loadProperty(this.bundleContext, configProps, OrbitConstants.COMPONENT_APP_STORE_NAME);
			if (configProps.containsKey(OrbitConstants.COMPONENT_APP_STORE_NAME)) {
				startAppStore = true;
			}
			System.out.println("Start app store service: " + (startAppStore ? "yes" : "no"));
			if (startAppStore) {
				startAppStoreService(this.bundleContext);
			}

		} else if (DOMAIN_MGMT.equalsIgnoreCase(service)) {
			boolean startDomainMgmtService = false;
			PropertyUtil.loadProperty(this.bundleContext, configProps, OrbitConstants.COMPONENT_DOMAIN_MANAGEMENT_NAME);
			if (configProps.containsKey(OrbitConstants.COMPONENT_DOMAIN_MANAGEMENT_NAME)) {
				startDomainMgmtService = true;
			}
			System.out.println("Start domain management service: " + (startDomainMgmtService ? "yes" : "no"));
			if (startDomainMgmtService) {
				startDomainMgmtService(this.bundleContext);
			}

		} else {
			System.err.println("###### Unsupported service name: " + service);
		}
	}

	/**
	 * Start a service
	 * 
	 * @param service
	 */
	@Descriptor("Stop a service")
	public void stopservice(@Descriptor("The service to stop") @Parameter(names = { "-s", "--service" }, absentValue = "null") String service) {
		System.out.println("stopping service: " + service);

		if (service == null || service.isEmpty()) {
			System.out.println("service name is empty.");
			return;
		}

		if (USER_REGISTRY.equalsIgnoreCase(service)) {
			stopUserRegistryService();

		} else if (OAUTH2.equalsIgnoreCase(service)) {
			stopOAuth2Service();

		} else if (CONFIGR_EGISTRY.equalsIgnoreCase(service)) {
			stopConfigRegistryService();

		} else if (APP_STORE.equalsIgnoreCase(service)) {
			stopAppStoreService();

		} else if (DOMAIN_MGMT.equalsIgnoreCase(service)) {
			stopDomainMgmtService();

		} else {
			System.err.println("###### Unsupported service name: " + service);
		}
	}

	public void startUserRegistryService(BundleContext bundleContext) {
		UserRegistryServiceDatabaseImpl userRegistryService = new UserRegistryServiceDatabaseImpl();
		userRegistryService.start(bundleContext);
		this.userRegistryService = userRegistryService;
	}

	public void stopUserRegistryService() {
		if (this.userRegistryService != null) {
			this.userRegistryService.stop();
			this.userRegistryService = null;
		}
	}

	public void startOAuth2Service(BundleContext bundleContext) {
		OAuth2ServiceDatabaseImpl oauth2Service = new OAuth2ServiceDatabaseImpl();
		oauth2Service.start(bundleContext);
		this.oauth2Service = oauth2Service;
	}

	public void stopOAuth2Service() {
		if (this.oauth2Service != null) {
			this.oauth2Service.stop();
			this.oauth2Service = null;
		}
	}

	public void startConfigRegistryService(BundleContext bundleContext) {
		ConfigRegistryServiceDatabaseImpl configRegistryService = new ConfigRegistryServiceDatabaseImpl();
		configRegistryService.start(bundleContext);
		this.configRegistryService = configRegistryService;
	}

	public void stopConfigRegistryService() {
		if (this.configRegistryService != null) {
			this.configRegistryService.stop();
			this.configRegistryService = null;
		}
	}

	public void startAppStoreService(BundleContext bundleContext) {
		AppStoreServiceDatabaseImpl appStoreService = new AppStoreServiceDatabaseImpl();
		appStoreService.start(bundleContext);
		this.appStoreService = appStoreService;
	}

	public void stopAppStoreService() {
		if (this.appStoreService != null) {
			this.appStoreService.stop();
			this.appStoreService = null;
		}
	}

	public void startDomainMgmtService(BundleContext bundleContext) {
		DomainMgmtServiceDatabaseImpl domainMgmtService = new DomainMgmtServiceDatabaseImpl();
		domainMgmtService.start(bundleContext);
		this.domainMgmtService = domainMgmtService;
	}

	public void stopDomainMgmtService() {
		if (this.domainMgmtService != null) {
			this.domainMgmtService.stop();
			this.domainMgmtService = null;
		}
	}

}

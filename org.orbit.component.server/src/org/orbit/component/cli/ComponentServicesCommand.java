package org.orbit.component.cli;

import java.util.Hashtable;

import org.apache.felix.service.command.Descriptor;
import org.apache.felix.service.command.Parameter;
import org.orbit.component.server.tier1.account.service.impl.UserRegistryServiceDatabaseImpl;
import org.orbit.component.server.tier1.auth.service.impl.AuthServiceImpl;
import org.orbit.component.server.tier1.config.service.impl.ConfigRegistryServiceDatabaseImpl;
import org.orbit.component.server.tier2.appstore.service.impl.AppStoreServiceDatabaseImpl;
import org.orbit.component.server.tier3.domain.service.impl.DomainManagementServiceDatabaseImpl;
import org.orbit.component.server.tier3.transferagent.service.impl.TransferAgentServiceImpl;
import org.origin.common.osgi.OSGiServiceUtil;
import org.osgi.framework.BundleContext;

public class ComponentServicesCommand {

	// service types
	public static final String USER_REGISTRY = "userregistry";
	// public static final String OAUTH2 = "oauth2";
	public static final String AUTH = "auth";
	public static final String CONFIGR_EGISTRY = "configregistry";
	public static final String APP_STORE = "appstore";
	public static final String DOMAIN = "domain";
	public static final String TRANSFER_AGENT = "transferagent";

	protected BundleContext bundleContext;

	protected ConfigRegistryServiceDatabaseImpl configRegistryService;
	protected AuthServiceImpl authService;
	// protected OAuth2ServiceDatabaseImpl oauth2Service;
	protected UserRegistryServiceDatabaseImpl userRegistryService;
	protected AppStoreServiceDatabaseImpl appStoreService;
	protected DomainManagementServiceDatabaseImpl domainMgmtService;
	protected TransferAgentServiceImpl transferAgentService;

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
		OSGiServiceUtil.register(bundleContext, ComponentServicesCommand.class.getName(), this, props);
	}

	/**
	 * 
	 * @param bundleContext
	 */
	public void stop(BundleContext bundleContext) {
		System.out.println(getClass().getSimpleName() + ".stop()");

		OSGiServiceUtil.unregister(ComponentServicesCommand.class.getName(), this);

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

		if (USER_REGISTRY.equalsIgnoreCase(service)) {
			startUserRegistryService(this.bundleContext);

		} else if (AUTH.equalsIgnoreCase(service)) {
			startAuthService(this.bundleContext);

			// } else if (OAUTH2.equalsIgnoreCase(service)) {
			// startOAuth2Service(this.bundleContext);

		} else if (CONFIGR_EGISTRY.equalsIgnoreCase(service)) {
			startConfigRegistryService(this.bundleContext);

		} else if (APP_STORE.equalsIgnoreCase(service)) {
			startAppStoreService(this.bundleContext);

		} else if (DOMAIN.equalsIgnoreCase(service)) {
			startDomainMgmtService(this.bundleContext);

		} else if (TRANSFER_AGENT.equalsIgnoreCase(service)) {
			startTransferAgentService(this.bundleContext);

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

		if (USER_REGISTRY.equalsIgnoreCase(service)) {
			stopUserRegistryService();

			// } else if (OAUTH2.equalsIgnoreCase(service)) {
			// stopOAuth2Service();

		} else if (AUTH.equalsIgnoreCase(service)) {
			stopAuthService(this.bundleContext);

		} else if (CONFIGR_EGISTRY.equalsIgnoreCase(service)) {
			stopConfigRegistryService();

		} else if (APP_STORE.equalsIgnoreCase(service)) {
			stopAppStoreService();

		} else if (DOMAIN.equalsIgnoreCase(service)) {
			stopDomainMgmtService(this.bundleContext);

		} else if (TRANSFER_AGENT.equalsIgnoreCase(service)) {
			stopTransferAgentService();

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

	public void startAuthService(BundleContext bundleContext) {
		AuthServiceImpl authService = new AuthServiceImpl();
		authService.start(bundleContext);
		this.authService = authService;
	}

	public void stopAuthService(BundleContext bundleContext) {
		if (this.authService != null) {
			this.authService.stop(bundleContext);
			this.authService = null;
		}
	}

	// public void startOAuth2Service(BundleContext bundleContext) {
	// OAuth2ServiceDatabaseImpl oauth2Service = new OAuth2ServiceDatabaseImpl();
	// oauth2Service.start(bundleContext);
	// this.oauth2Service = oauth2Service;
	// }
	//
	// public void stopOAuth2Service() {
	// if (this.oauth2Service != null) {
	// this.oauth2Service.stop();
	// this.oauth2Service = null;
	// }
	// }

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
		DomainManagementServiceDatabaseImpl domainMgmtService = new DomainManagementServiceDatabaseImpl();
		domainMgmtService.start(bundleContext);
		this.domainMgmtService = domainMgmtService;
	}

	public void stopDomainMgmtService(BundleContext bundleContext) {
		if (this.domainMgmtService != null) {
			this.domainMgmtService.stop(bundleContext);
			this.domainMgmtService = null;
		}
	}

	public void startTransferAgentService(BundleContext bundleContext) {
		TransferAgentServiceImpl transferAgentService = new TransferAgentServiceImpl(bundleContext);
		transferAgentService.start();
		this.transferAgentService = transferAgentService;
	}

	public void stopTransferAgentService() {
		if (this.transferAgentService != null) {
			this.transferAgentService.stop();
			this.transferAgentService = null;
		}
	}

}

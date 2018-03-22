package org.orbit.component.runtime.cli;

import java.util.Hashtable;
import java.util.Map;

import org.apache.felix.service.command.Descriptor;
import org.apache.felix.service.command.Parameter;
import org.orbit.component.runtime.common.ws.OrbitConstants;
import org.orbit.component.runtime.tier1.account.service.UserRegistryServiceImpl;
import org.orbit.component.runtime.tier1.auth.service.AuthServiceImpl;
import org.orbit.component.runtime.tier1.config.service.ConfigRegistryServiceDatabaseImpl;
import org.orbit.component.runtime.tier2.appstore.service.AppStoreServiceDatabaseImpl;
import org.orbit.component.runtime.tier3.domainmanagement.service.DomainManagementServiceImpl;
import org.orbit.component.runtime.tier3.nodemanagement.service.NodeManagementServiceImpl;
import org.orbit.component.runtime.tier4.missioncontrol.service.MissionControlServiceImpl;
import org.origin.common.osgi.OSGiServiceUtil;
import org.origin.common.util.PropertyUtil;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServicesCommand {

	protected static Logger LOG = LoggerFactory.getLogger(ServicesCommand.class);

	// service types
	public static final String USER_REGISTRY = "user_registry";
	public static final String AUTH = "auth";
	public static final String CONFIGR_EGISTRY = "config_registry";
	public static final String APP_STORE = "app_store";
	public static final String DOMAIN_MANAGEMENT = "domain_management";
	public static final String NODE_MANAGEMENT = "node_management";
	public static final String MISSION_CONTROL = "mission_control";

	protected BundleContext bundleContext;

	// tier1
	protected boolean autoStartUserRegistryService;
	protected boolean autoStartAuthService;
	protected boolean autoStartConfigRegistryService;

	protected UserRegistryServiceImpl userRegistryService;
	protected AuthServiceImpl authService;
	protected ConfigRegistryServiceDatabaseImpl configRegistryService;

	// tier2
	protected boolean autoStartAppStoreService;
	protected AppStoreServiceDatabaseImpl appStoreService;

	// tier3
	protected boolean autoStartDomainMgmtService;
	protected boolean autoStartTransferAgentService;

	protected DomainManagementServiceImpl domainManagementService;
	protected NodeManagementServiceImpl nodeManagementService;

	// tier4
	protected boolean autoStartMissionControlService;
	protected MissionControlServiceImpl missionControlService;

	/**
	 * 
	 * @param bundleContext
	 */
	public void start(BundleContext bundleContext) {
		LOG.info("start()");

		this.bundleContext = bundleContext;

		Hashtable<String, Object> props = new Hashtable<String, Object>();
		props.put("osgi.command.scope", "orbit");
		props.put("osgi.command.function", new String[] { "startservice", "stopservice" });
		OSGiServiceUtil.register(bundleContext, ServicesCommand.class.getName(), this, props);

		// Get the available components
		Map<Object, Object> properties = new Hashtable<Object, Object>();
		// TASetupUtil.loadConfigIniProperties(bundleContext, configProps);

		PropertyUtil.loadProperty(bundleContext, properties, OrbitConstants.COMPONENT_USER_REGISTRY_AUTOSTART);
		PropertyUtil.loadProperty(bundleContext, properties, OrbitConstants.COMPONENT_AUTH_AUTOSTART);
		PropertyUtil.loadProperty(bundleContext, properties, OrbitConstants.COMPONENT_CONFIG_REGISTRY_AUTOSTART);
		PropertyUtil.loadProperty(bundleContext, properties, OrbitConstants.COMPONENT_APP_STORE_AUTOSTART);
		// PropertyUtil.loadProperty(bundleContext, properties, OrbitConstants.COMPONENT_DOMAIN_MANAGEMENT_AUTOSTART);
		// PropertyUtil.loadProperty(bundleContext, properties, OrbitConstants.COMPONENT_NODE_SERVICE_AUTOSTART);
		PropertyUtil.loadProperty(bundleContext, properties, OrbitConstants.COMPONENT_MISSION_CONTROL_AUTOSTART);

		this.autoStartUserRegistryService = properties.containsKey(OrbitConstants.COMPONENT_USER_REGISTRY_AUTOSTART) ? true : false;
		this.autoStartAuthService = properties.containsKey(OrbitConstants.COMPONENT_AUTH_AUTOSTART) ? true : false;
		this.autoStartConfigRegistryService = properties.containsKey(OrbitConstants.COMPONENT_CONFIG_REGISTRY_AUTOSTART) ? true : false;
		this.autoStartAppStoreService = properties.containsKey(OrbitConstants.COMPONENT_APP_STORE_AUTOSTART) ? true : false;
		// this.autoStartDomainMgmtService = properties.containsKey(OrbitConstants.COMPONENT_DOMAIN_MANAGEMENT_AUTOSTART) ? true : false;
		// this.autoStartTransferAgentService = properties.containsKey(OrbitConstants.COMPONENT_NODE_SERVICE_AUTOSTART) ? true : false;
		this.autoStartMissionControlService = properties.containsKey(OrbitConstants.COMPONENT_MISSION_CONTROL_AUTOSTART) ? true : false;

		LOG.info("autoStartUserRegistryService = " + this.autoStartUserRegistryService);
		LOG.info("autoStartAuthService = " + this.autoStartAuthService);
		LOG.info("autoStartConfigRegistryService = " + this.autoStartConfigRegistryService);
		LOG.info("autoStartAppStoreService = " + this.autoStartAppStoreService);
		LOG.info("autoStartDomainMgmtService = " + this.autoStartDomainMgmtService);
		// LOG.info("autoStartTransferAgentService = " + this.autoStartTransferAgentService);
		LOG.info("autoStartMissionControlService = " + this.autoStartMissionControlService);

		// tier1
		if (autoStartUserRegistryService) {
			startservice(ServicesCommand.USER_REGISTRY);
		}
		if (autoStartAuthService) {
			startservice(ServicesCommand.AUTH);
		}
		if (autoStartConfigRegistryService) {
			startservice(ServicesCommand.CONFIGR_EGISTRY);
		}

		// tier2
		if (autoStartAppStoreService) {
			startservice(ServicesCommand.APP_STORE);
		}

		// tier3
		if (autoStartDomainMgmtService) {
			startservice(ServicesCommand.DOMAIN_MANAGEMENT);
		}
		if (autoStartTransferAgentService) {
			startservice(ServicesCommand.NODE_MANAGEMENT);
		}

		// tier4
		if (autoStartMissionControlService) {
			startservice(ServicesCommand.MISSION_CONTROL);
		}
	}

	/**
	 * 
	 * @param bundleContext
	 */
	public void stop(BundleContext bundleContext) {
		LOG.info("stop()");

		OSGiServiceUtil.unregister(ServicesCommand.class.getName(), this);

		// tier4
		stopservice(ServicesCommand.MISSION_CONTROL);

		// tier3
		stopservice(ServicesCommand.NODE_MANAGEMENT);
		stopservice(ServicesCommand.DOMAIN_MANAGEMENT);

		// tier2
		stopservice(ServicesCommand.APP_STORE);

		// tier1
		stopservice(ServicesCommand.CONFIGR_EGISTRY);
		stopservice(ServicesCommand.AUTH);
		stopservice(ServicesCommand.USER_REGISTRY);

		this.bundleContext = null;
	}

	protected void checkBundleContext() {
		if (this.bundleContext == null) {
			throw new IllegalStateException("bundleContext is null.");
		}
	}

	/**
	 * Start a service
	 * 
	 * @param service
	 */
	@Descriptor("Start a service")
	public void startservice(@Descriptor("The service to start") @Parameter(names = { "-s", "--service" }, absentValue = "null") String service) {
		LOG.info("starting service: " + service);
		checkBundleContext();

		if (USER_REGISTRY.equalsIgnoreCase(service)) {
			startUserRegistryService(this.bundleContext);

		} else if (AUTH.equalsIgnoreCase(service)) {
			startAuthService(this.bundleContext);

		} else if (CONFIGR_EGISTRY.equalsIgnoreCase(service)) {
			startConfigRegistryService(this.bundleContext);

		} else if (APP_STORE.equalsIgnoreCase(service)) {
			startAppStoreService(this.bundleContext);

		} else if (DOMAIN_MANAGEMENT.equalsIgnoreCase(service)) {
			startDomainManagementService(this.bundleContext);

		} else if (NODE_MANAGEMENT.equalsIgnoreCase(service)) {
			startNodeManagementService(this.bundleContext);

		} else if (MISSION_CONTROL.equalsIgnoreCase(service)) {
			startMissionControlService(this.bundleContext);

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
		LOG.info("stopping service: " + service);
		checkBundleContext();

		if (USER_REGISTRY.equalsIgnoreCase(service)) {
			stopUserRegistryService(this.bundleContext);

		} else if (AUTH.equalsIgnoreCase(service)) {
			stopAuthService(this.bundleContext);

		} else if (CONFIGR_EGISTRY.equalsIgnoreCase(service)) {
			stopConfigRegistryService(this.bundleContext);

		} else if (APP_STORE.equalsIgnoreCase(service)) {
			stopAppStoreService(this.bundleContext);

		} else if (DOMAIN_MANAGEMENT.equalsIgnoreCase(service)) {
			stopDomainManagementService(this.bundleContext);

		} else if (NODE_MANAGEMENT.equalsIgnoreCase(service)) {
			stopNodeManagementService(this.bundleContext);

		} else if (MISSION_CONTROL.equalsIgnoreCase(service)) {
			stopMissionControlService(this.bundleContext);

		} else {
			System.err.println("###### Unsupported service name: " + service);
		}
	}

	public void startUserRegistryService(BundleContext bundleContext) {
		UserRegistryServiceImpl userRegistryService = new UserRegistryServiceImpl(null);
		userRegistryService.start(bundleContext);
		this.userRegistryService = userRegistryService;
	}

	public void stopUserRegistryService(BundleContext bundleContext) {
		if (this.userRegistryService != null) {
			this.userRegistryService.stop(bundleContext);
			this.userRegistryService = null;
		}
	}

	public void startAuthService(BundleContext bundleContext) {
		AuthServiceImpl authService = new AuthServiceImpl(null);
		authService.start(bundleContext);
		this.authService = authService;
	}

	public void stopAuthService(BundleContext bundleContext) {
		if (this.authService != null) {
			this.authService.stop(bundleContext);
			this.authService = null;
		}
	}

	public void startConfigRegistryService(BundleContext bundleContext) {
		ConfigRegistryServiceDatabaseImpl configRegistryService = new ConfigRegistryServiceDatabaseImpl(null);
		configRegistryService.start(bundleContext);
		this.configRegistryService = configRegistryService;
	}

	public void stopConfigRegistryService(BundleContext bundleContext) {
		if (this.configRegistryService != null) {
			this.configRegistryService.stop(bundleContext);
			this.configRegistryService = null;
		}
	}

	public void startAppStoreService(BundleContext bundleContext) {
		AppStoreServiceDatabaseImpl appStoreService = new AppStoreServiceDatabaseImpl(null);
		appStoreService.start(bundleContext);
		this.appStoreService = appStoreService;
	}

	public void stopAppStoreService(BundleContext bundleContext) {
		if (this.appStoreService != null) {
			this.appStoreService.stop(bundleContext);
			this.appStoreService = null;
		}
	}

	public void startDomainManagementService(BundleContext bundleContext) {
		DomainManagementServiceImpl domainService = new DomainManagementServiceImpl(null);
		domainService.start(bundleContext);
		this.domainManagementService = domainService;
	}

	public void stopDomainManagementService(BundleContext bundleContext) {
		if (this.domainManagementService != null) {
			this.domainManagementService.stop(bundleContext);
			this.domainManagementService = null;
		}
	}

	public void startNodeManagementService(BundleContext bundleContext) {
		NodeManagementServiceImpl transferAgentService = new NodeManagementServiceImpl(null);
		transferAgentService.start(bundleContext);
		this.nodeManagementService = transferAgentService;
	}

	public void stopNodeManagementService(BundleContext bundleContext) {
		if (this.nodeManagementService != null) {
			this.nodeManagementService.stop(bundleContext);
			this.nodeManagementService = null;
		}
	}

	public void startMissionControlService(BundleContext bundleContext) {
		MissionControlServiceImpl missionControlService = new MissionControlServiceImpl(null);
		missionControlService.start(bundleContext);
		this.missionControlService = missionControlService;
	}

	public void stopMissionControlService(BundleContext bundleContext) {
		if (this.missionControlService != null) {
			this.missionControlService.stop(bundleContext);
			this.missionControlService = null;
		}
	}

}

// public static final String OAUTH2 = "oauth2";

// protected OAuth2ServiceDatabaseImpl oauth2Service;

// } else if (OAUTH2.equalsIgnoreCase(service)) {
// startOAuth2Service(this.bundleContext);

// public void startOAuth2Service(BundleContext bundleContext) {
// OAuth2ServiceDatabaseImpl oauth2Service = new OAuth2ServiceDatabaseImpl();
// oauth2Service.start(bundleContext);
// this.oauth2Service = oauth2Service;
// }
//
// public void stopOAuth2Service(BundleContext bundleContext) {
// if (this.oauth2Service != null) {
// this.oauth2Service.stop();
// this.oauth2Service = null;
// }
// }

// } else if (OAUTH2.equalsIgnoreCase(service)) {
// stopOAuth2Service();

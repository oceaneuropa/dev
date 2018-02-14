/*******************************************************************************
 * Copyright (c) 2017, 2018 OceanEuropa.
 * All rights reserved.
 *
 * Contributors:
 *     OceanEuropa - initial API and implementation
 *******************************************************************************/
package org.orbit.platform.runtime.platform;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import org.orbit.platform.runtime.PlatformConstants;
import org.orbit.platform.runtime.command.service.CommandService;
import org.orbit.platform.runtime.command.service.impl.CommandServiceImpl;
import org.orbit.platform.runtime.programs.ProgramException;
import org.orbit.platform.runtime.programs.ProgramsAndFeatures;
import org.orbit.platform.runtime.programs.ProgramsAndFeaturesImpl;
import org.orbit.platform.sdk.extension.IProgramExtension;
import org.orbit.platform.sdk.extension.IProgramExtensionService;
import org.orbit.platform.sdk.extension.util.ProgramExtensionServiceTracker;
import org.orbit.platform.sdk.relay.WSRelayControl;
import org.orbit.platform.sdk.servicecontrol.ServiceControl;
import org.origin.common.adapter.AdaptorSupport;
import org.origin.common.rest.client.WSClientFactory;
import org.origin.common.rest.client.WSClientFactoryImpl;
import org.origin.common.rest.editpolicy.WSEditPolicies;
import org.origin.common.rest.editpolicy.WSEditPoliciesImpl;
import org.origin.common.util.PropertyUtil;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @see https://en.wikipedia.org/wiki/List_of_nearest_stars_and_brown_dwarfs
 */
public class PlatformImpl implements Platform {

	public static final String PLATFORM__NAME = "Sun";
	public static final String PLATFORM__VERSION = "1.0.0";

	protected static Logger LOG = LoggerFactory.getLogger(PlatformImpl.class);

	protected ProgramExtensionServiceTracker programExtensionServiceTracker;
	protected CommandService commandService;
	protected ProgramsAndFeatures programsAndFreatures;

	protected AdaptorSupport adaptorSupport = new AdaptorSupport();
	protected WSClientFactory wsClientFactory = new WSClientFactoryImpl();
	protected WSEditPolicies wsEditPolicies;
	protected Map<Object, Object> properties = new HashMap<Object, Object>();
	protected ServiceRegistration<?> serviceRegistry;

	public PlatformImpl() {
		this.wsEditPolicies = new WSEditPoliciesImpl();
		this.wsEditPolicies.setService(Platform.class, this);
	}

	/**
	 * 
	 * @param bundleContext
	 * @throws Exception
	 */
	public void start(BundleContext bundleContext) throws Exception {
		// load properties
		Map<Object, Object> configProps = new Hashtable<Object, Object>();
		PropertyUtil.loadProperty(bundleContext, configProps, PlatformConstants.ORBIT_HOST_URL);
		PropertyUtil.loadProperty(bundleContext, configProps, PlatformConstants.PLATFORM_NAME);
		PropertyUtil.loadProperty(bundleContext, configProps, PlatformConstants.PLATFORM_VERSION);
		PropertyUtil.loadProperty(bundleContext, configProps, PlatformConstants.PLATFORM_HOST_URL);
		PropertyUtil.loadProperty(bundleContext, configProps, PlatformConstants.PLATFORM_CONTEXT_ROOT);

		// 1. Start tracking program extension service
		this.programExtensionServiceTracker = new ProgramExtensionServiceTracker();
		this.programExtensionServiceTracker.start(bundleContext);

		// 2. Start command service
		this.commandService = new CommandServiceImpl();
		this.commandService.start();

		// 3. Start programs and features service
		try {
			this.programsAndFreatures = new ProgramsAndFeaturesImpl(bundleContext);
			this.programsAndFreatures.start();
		} catch (ProgramException e) {
			e.printStackTrace();
		}

		// 4. Auto services
		IProgramExtensionService extensionService = getProgramExtensionService();
		if (extensionService != null) {
			startExtensionServices(bundleContext, extensionService);
			startExtensionRelays(bundleContext, extensionService, this.wsClientFactory);
		}

		updateProperties(configProps);

		// Register as Platform service
		Hashtable<String, Object> props = new Hashtable<String, Object>();
		this.serviceRegistry = bundleContext.registerService(Platform.class, this, props);
	}

	/**
	 * 
	 * @param bundleContext
	 * @param extensionService
	 */
	protected void startExtensionServices(BundleContext bundleContext, IProgramExtensionService extensionService) {
		IProgramExtension[] serviceControlExtensions = extensionService.getExtensions(ServiceControl.EXTENSION_TYPE_ID);
		for (IProgramExtension serviceControlExtension : serviceControlExtensions) {
			ServiceControl serviceControl = serviceControlExtension.getAdapter(ServiceControl.class);
			if (serviceControl != null) {
				Map<String, Object> properties = serviceControl.getConfigProperties(bundleContext);
				if (serviceControl.isAutoStart(bundleContext, properties)) {
					serviceControl.start(bundleContext, properties);
				}
			}
		}
	}

	/**
	 * 
	 * @param bundleContext
	 * @param extensionService
	 * @param wsClientFactory
	 */
	protected void startExtensionRelays(BundleContext bundleContext, IProgramExtensionService extensionService, WSClientFactory wsClientFactory) {
		IProgramExtension[] relayControlExtensions = extensionService.getExtensions(WSRelayControl.EXTENSION_TYPE_ID);
		for (IProgramExtension relayControlExtension : relayControlExtensions) {
			String extensionId = relayControlExtension.getId();
			// Note:
			// - Need to find contextRoot and URL list from bundle context from the extensionId.
			// - That means the platform need to understand (or have dependency on --- another way to say) the configuration property names of WS relay. The
			// problem is the configuration property names are different among different WS relays, unless a common format is used for all WS relays, which will
			// require xml based configuration file, instead of property based config.ini file or VM arguments.

			// WSRelayControl relayControl = serviceControlExtension.getAdapter(WSRelayControl.class);
			// if (relayControl != null && relayControl.isAutoStart(bundleContext)) {
			// relayControl.start(bundleContext, wsClientFactory, contextRoot, uriList);
			// }
		}
	}

	/**
	 * 
	 * @param bundleContext
	 * @throws Exception
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		// Stop Platform service
		if (this.serviceRegistry != null) {
			this.serviceRegistry.unregister();
			this.serviceRegistry = null;
		}

		// 1. Stop programs and features service
		if (this.programsAndFreatures != null) {
			try {
				this.programsAndFreatures.stop();
			} catch (ProgramException e) {
				e.printStackTrace();
			}
			this.programsAndFreatures = null;
		}

		// 2. Stop command service
		if (this.commandService != null) {
			this.commandService.stop();
		}

		// 3. Stop tracking program extension service
		if (this.programExtensionServiceTracker != null) {
			this.programExtensionServiceTracker.stop(bundleContext);
			this.programExtensionServiceTracker = null;
		}
	}

	@Override
	public synchronized void updateProperties(Map<Object, Object> properties) {
		LOG.info("updateProperties()");
		if (properties == null) {
			properties = new HashMap<Object, Object>();
		}
		this.properties = properties;
	}

	protected String getProperty(String key) {
		return getProperty(key, String.class);
	}

	@SuppressWarnings("unchecked")
	protected <T> T getProperty(String key, Class<T> valueClass) {
		// Config properties from bundle context or from system/env properties takes precedence over properties defined in config.ini file.
		Object object = this.properties.get(key);
		if (object != null && valueClass.isAssignableFrom(object.getClass())) {
			return (T) object;
		}
		// If config properties cannot be found, read from config.ini file
		if (this.properties != null && String.class.equals(valueClass)) {
			String value = (String) this.properties.get(key.toString());
			if (value != null) {
				return (T) value;
			}
		}
		return null;
	}

	@Override
	public String getName() {
		return PLATFORM__NAME;
	}

	@Override
	public String getVersion() {
		return PLATFORM__VERSION;
	}

	@Override
	public String getHostURL() {
		String hostURL = getProperty(PlatformConstants.PLATFORM_HOST_URL);
		if (hostURL != null) {
			return hostURL;
		}
		String globalHostURL = getProperty(PlatformConstants.ORBIT_HOST_URL);
		if (globalHostURL != null) {
			return globalHostURL;
		}
		return null;
	}

	@Override
	public String getContextRoot() {
		String contextRoot = getProperty(PlatformConstants.PLATFORM_CONTEXT_ROOT);
		return contextRoot;
	}

	@Override
	public String getHome() {
		String home = getProperty(PlatformConstants.PLATFORM_HOME);
		return home;
	}

	@Override
	public WSEditPolicies getEditPolicies() {
		return this.wsEditPolicies;
	}

	@Override
	public IProgramExtensionService getProgramExtensionService() {
		return (this.programExtensionServiceTracker != null) ? this.programExtensionServiceTracker.getService() : null;
	}

	@Override
	public CommandService getCommandService() {
		return this.commandService;
	}

	@Override
	public ProgramsAndFeatures getProgramsAndFeatures() {
		return this.programsAndFreatures;
	}

	@Override
	public <T> void adapt(Class<T> clazz, T object) {
		this.adaptorSupport.adapt(clazz, object);
	}

	@Override
	public <T> T getAdapter(Class<T> adapter) {
		return this.adaptorSupport.getAdapter(adapter);
	}

}

// public static final String PROP_PLATFORM_NAME = "platform.name";
// public static final String PROP_PLATFORM_VERSION = "platform.version";

// 3. Start command and services
// this.platformCommand = new PlatformCommand();
// this.platformCommand.start(bundleContext);
// this.platformCommand.startGAIA();

// 2. Stop command and services
// if (this.platformCommand != null) {
// this.OSCommand.stopGAIA();
// this.platformCommand.stop(bundleContext);
// this.platformCommand = null;
// }

// @Override
// public PlatformCommand getOSCommand() {
// return this.platformCommand;
// }

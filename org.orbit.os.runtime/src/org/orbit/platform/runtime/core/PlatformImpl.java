/*******************************************************************************
 * Copyright (c) 2017, 2018 OceanEuropa.
 * All rights reserved.
 *
 * Contributors:
 *     OceanEuropa - initial API and implementation
 *******************************************************************************/
package org.orbit.platform.runtime.core;

import java.util.Map;

import org.orbit.platform.runtime.cli.PlatformCommand;
import org.orbit.platform.runtime.command.service.CommandService;
import org.orbit.platform.runtime.command.service.impl.CommandServiceImpl;
import org.orbit.platform.runtime.programs.ProgramException;
import org.orbit.platform.runtime.programs.ProgramsAndFeatures;
import org.orbit.platform.runtime.programs.ProgramsAndFeaturesImpl;
import org.orbit.sdk.ServiceControl;
import org.orbit.sdk.WSRelayControl;
import org.orbit.sdk.extension.IProgramExtension;
import org.orbit.sdk.extension.IProgramExtensionService;
import org.orbit.sdk.extension.util.ProgramExtensionServiceTracker;
import org.origin.common.adapter.AdaptorSupport;
import org.origin.common.rest.client.WSClientFactory;
import org.origin.common.rest.client.WSClientFactoryImpl;
import org.osgi.framework.BundleContext;

/**
 * @see https://en.wikipedia.org/wiki/List_of_nearest_stars_and_brown_dwarfs
 */
public class PlatformImpl implements Platform {

	public static final String PROP_PLATFORM_NAME = "platform.name";
	public static final String PROP_PLATFORM_VERSION = "platform.version";

	private static Object lock = new Object[0];
	private static PlatformImpl instance = null;

	public static PlatformImpl getInstance() {
		if (instance == null) {
			synchronized (lock) {
				if (instance == null) {
					instance = new PlatformImpl();
				}
			}
		}
		return instance;
	}

	protected ProgramExtensionServiceTracker programExtensionServiceTracker;
	protected CommandService commandService;
	protected PlatformCommand platformCommand;
	protected ProgramsAndFeatures programsAndFreatures;
	protected AdaptorSupport adaptorSupport = new AdaptorSupport();
	protected WSClientFactory wsClientFactory = new WSClientFactoryImpl();

	@Override
	public String getName() {
		return "Sun";
	}

	@Override
	public String getVersion() {
		return "1.0.0";
	}

	@Override
	public void start(BundleContext bundleContext) throws Exception {
		// 1. Start tracking program extension service
		this.programExtensionServiceTracker = new ProgramExtensionServiceTracker();
		this.programExtensionServiceTracker.start(bundleContext);

		// 2. Start command service
		this.commandService = new CommandServiceImpl();
		this.commandService.start();

		// 3. Start command and services
		this.platformCommand = new PlatformCommand();
		this.platformCommand.start(bundleContext);
		// this.platformCommand.startGAIA();

		// 4. Start programs and features service
		try {
			this.programsAndFreatures = new ProgramsAndFeaturesImpl(bundleContext);
			this.programsAndFreatures.start();
		} catch (ProgramException e) {
			e.printStackTrace();
		}

		// 5. Auto services
		IProgramExtensionService extensionService = getProgramExtensionService();
		if (extensionService != null) {
			startExtensionServices(bundleContext, extensionService);
			startExtensionRelays(bundleContext, extensionService, this.wsClientFactory);
		}
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

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		// Stop running services

		// 1. Stop programs and features service
		if (this.programsAndFreatures != null) {
			try {
				this.programsAndFreatures.stop();
			} catch (ProgramException e) {
				e.printStackTrace();
			}
			this.programsAndFreatures = null;
		}

		// 2. Stop command and services
		if (this.platformCommand != null) {
			// this.OSCommand.stopGAIA();
			this.platformCommand.stop(bundleContext);
			this.platformCommand = null;
		}

		// 2. Stop command service
		if (this.commandService != null) {
			this.commandService.stop();
		}

		// 4. Stop tracking program extension service
		if (this.programExtensionServiceTracker != null) {
			this.programExtensionServiceTracker.stop(bundleContext);
			this.programExtensionServiceTracker = null;
		}
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
	public PlatformCommand getOSCommand() {
		return this.platformCommand;
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

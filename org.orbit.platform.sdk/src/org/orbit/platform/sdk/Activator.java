/*******************************************************************************
 * Copyright (c) 2017, 2018 OceanEuropa.
 * All rights reserved.
 *
 * Contributors:
 *     OceanEuropa - initial API and implementation
 *******************************************************************************/
package org.orbit.platform.sdk;

import org.orbit.platform.sdk.command.CommandExtensionRegistry;
import org.orbit.platform.sdk.command.impl.CommandExtensionRegistryImpl;
import org.orbit.platform.sdk.connector.ConnectorExtensionRegistry;
import org.orbit.platform.sdk.connector.impl.ConnectorExtensionRegistryImpl;
import org.orbit.platform.sdk.ui.IPlatformTracker;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Activator implements BundleActivator {

	protected static Logger LOG = LoggerFactory.getLogger(Activator.class);

	protected static BundleContext bundleContext;
	protected static Activator instance;

	static BundleContext getContext() {
		return bundleContext;
	}

	public static Activator getInstance() {
		return instance;
	}

	protected ConnectorExtensionRegistry connectorExtensionRegistry;
	protected CommandExtensionRegistry commandExtensionRegistry;
	protected IPlatformTracker platformTracker;

	@Override
	public void start(BundleContext bundleContext) throws Exception {
		LOG.info("start()");

		Activator.bundleContext = bundleContext;
		Activator.instance = this;

		// Start tracking IPlatform service
		this.platformTracker = new IPlatformTracker();
		this.platformTracker.start(bundleContext);

		// Handle connector extensions
		ConnectorExtensionRegistryImpl.INSTANCE.start(bundleContext);
		this.connectorExtensionRegistry = ConnectorExtensionRegistryImpl.INSTANCE;

		// Handle command extensions
		CommandExtensionRegistryImpl.INSTANCE.start(bundleContext);
		this.commandExtensionRegistry = CommandExtensionRegistryImpl.INSTANCE;
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		LOG.info("stop()");

		this.connectorExtensionRegistry = null;
		ConnectorExtensionRegistryImpl.INSTANCE.dispose();
		ConnectorExtensionRegistryImpl.INSTANCE.stop(bundleContext);

		this.commandExtensionRegistry = null;
		CommandExtensionRegistryImpl.INSTANCE.dispose();
		CommandExtensionRegistryImpl.INSTANCE.stop(bundleContext);

		// Stop tracking IPlatform service
		if (this.platformTracker != null) {
			this.platformTracker.stop(bundleContext);
			this.platformTracker = null;
		}

		Activator.instance = null;
		Activator.bundleContext = null;
	}

	public IPlatform getPlatform() {
		IPlatform platform = null;
		if (this.platformTracker != null) {
			platform = this.platformTracker.getService();
		}
		return platform;
	}

	public ConnectorExtensionRegistry getConnectorExtensionRegistry() {
		return this.connectorExtensionRegistry;
	}

	public CommandExtensionRegistry getCommandExtensionRegistry() {
		return this.commandExtensionRegistry;
	}

	/**
	 * 
	 * @param object
	 * @return
	 */
	public BundleContext getBundleContext(Object object) {
		Class<?> clazz = object.getClass();
		BundleContext bundleContext = FrameworkUtil.getBundle(clazz).getBundleContext();
		if (bundleContext == null) {
			bundleContext = Activator.bundleContext;
		}
		return bundleContext;
	}

}

// Start program extension service impl
// ExtensionServiceImpl.getInstance().start(bundleContext);

// Stop program extension service impl
// ExtensionServiceImpl.getInstance().stop(bundleContext);

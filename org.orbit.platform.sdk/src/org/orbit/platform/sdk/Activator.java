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
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;

public class Activator implements BundleActivator {

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

	@Override
	public void start(BundleContext bundleContext) throws Exception {
		Activator.bundleContext = bundleContext;
		Activator.instance = this;

		// Handle connector extensions
		ConnectorExtensionRegistryImpl.INSTANCE.start(bundleContext);
		this.connectorExtensionRegistry = ConnectorExtensionRegistryImpl.INSTANCE;

		// Handle command extensions
		CommandExtensionRegistryImpl.INSTANCE.start(bundleContext);
		this.commandExtensionRegistry = CommandExtensionRegistryImpl.INSTANCE;
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		this.connectorExtensionRegistry = null;
		ConnectorExtensionRegistryImpl.INSTANCE.dispose();
		ConnectorExtensionRegistryImpl.INSTANCE.stop(bundleContext);

		this.commandExtensionRegistry = null;
		CommandExtensionRegistryImpl.INSTANCE.dispose();
		CommandExtensionRegistryImpl.INSTANCE.stop(bundleContext);

		Activator.instance = null;
		Activator.bundleContext = null;
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

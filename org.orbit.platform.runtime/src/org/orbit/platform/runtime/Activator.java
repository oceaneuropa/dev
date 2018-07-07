/*******************************************************************************
 * Copyright (c) 2017, 2018 OceanEuropa.
 * All rights reserved.
 *
 * Contributors:
 *     OceanEuropa - initial API and implementation
 *******************************************************************************/
package org.orbit.platform.runtime;

import java.util.Hashtable;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.orbit.platform.runtime.core.Platform;
import org.orbit.platform.runtime.core.PlatformImpl;
import org.orbit.platform.runtime.core.ws.PlatformAdapter;
import org.orbit.platform.runtime.util.PlatformTracker;
import org.origin.common.osgi.AbstractBundleActivator;
import org.origin.common.util.PropertyUtil;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Activator extends AbstractBundleActivator {

	protected static Logger LOG = LoggerFactory.getLogger(Activator.class);

	protected static Activator instance;

	public static Activator getInstance() {
		return instance;
	}

	protected PlatformTracker platformTracker;
	protected PlatformAdapter platformAdapter;
	protected PlatformImpl platform;
	protected ExecutorService executorService;

	@Override
	public void start(BundleContext bundleContext) throws Exception {
		LOG.info("start()");
		super.start(bundleContext);

		this.executorService = Executors.newSingleThreadExecutor();

		// load config properties
		Map<Object, Object> properties = new Hashtable<Object, Object>();
		PropertyUtil.loadProperty(bundleContext, properties, PlatformConstants.ORBIT_INDEX_SERVICE_URL);

		// Start tracking Platform service
		this.platformTracker = new PlatformTracker();
		this.platformTracker.start(bundleContext);

		// Start Platform WS adapter
		this.platformAdapter = new PlatformAdapter(properties);
		this.platformAdapter.start(bundleContext);

		// Start Platform impl
		this.platform = new PlatformImpl();
		this.platform.start(bundleContext);

		Extensions.INSTANCE.start(bundleContext);

		Activator.instance = this;
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		LOG.info("stop()");

		Activator.instance = null;

		Extensions.INSTANCE.stop(bundleContext);

		// Stop Platform impl
		this.platform.stop(bundleContext);

		// Stop Platform WS adapter
		if (this.platformAdapter != null) {
			this.platformAdapter.stop(bundleContext);
			this.platformAdapter = null;
		}

		// Stop tracking Platform service
		if (this.platformTracker != null) {
			this.platformTracker.stop(bundleContext);
			this.platformTracker = null;
		}

		if (this.executorService != null) {
			this.executorService.shutdown();
			this.executorService = null;
		}

		super.stop(bundleContext);
	}

	public Platform getPlatform() {
		return (this.platformTracker != null) ? this.platformTracker.getService() : null;
	}

	public ExecutorService getExecutorService() {
		return this.executorService;
	}

}

// SetupUtil.loadNodeConfigIniProperties(bundleContext, indexProviderProps);

// protected PlatformCommand command;

// Start server CLI
// this.command = new PlatformCommand();
// this.command.start(bundleContext);

// Stop server CLI
// if (this.command != null) {
// this.command.stop(bundleContext);
// this.command = null;
// }

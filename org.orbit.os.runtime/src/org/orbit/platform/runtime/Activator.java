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

import org.orbit.platform.runtime.gaia.service.GAIA;
import org.orbit.platform.runtime.gaia.ws.GaiaAdapter;
import org.orbit.platform.runtime.platform.Platform;
import org.orbit.platform.runtime.platform.PlatformImpl;
import org.orbit.platform.runtime.platform.ws.PlatformAdapter;
import org.orbit.platform.runtime.util.PlatformTracker;
import org.origin.common.util.PropertyUtil;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
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

	protected Extensions extensions;
	protected PlatformTracker platformTracker;
	protected PlatformAdapter platformAdapter;
	protected GaiaAdapter gaiaAdapter;
	protected PlatformImpl platform;

	@Override
	public void start(BundleContext bundleContext) throws Exception {
		LOG.info("start()");

		Activator.bundleContext = bundleContext;
		Activator.instance = this;

		// load config properties
		Map<Object, Object> properties = new Hashtable<Object, Object>();
		PropertyUtil.loadProperty(bundleContext, properties, PlatformConstants.ORBIT_INDEX_SERVICE_URL);

		// Register extensions
		this.extensions = new Extensions();
		this.extensions.start(bundleContext);

		// Start tracking Platform service
		this.platformTracker = new PlatformTracker();
		this.platformTracker.start(bundleContext);

		// Start Platform WS adapter
		this.platformAdapter = new PlatformAdapter(properties);
		this.platformAdapter.start(bundleContext);

		// Start GAIA WS adapter
		this.gaiaAdapter = new GaiaAdapter(properties);
		this.gaiaAdapter.start(bundleContext);

		// Start Platform impl
		this.platform = new PlatformImpl();
		this.platform.start(bundleContext);
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		LOG.info("stop()");

		// Stop Platform impl
		this.platform.stop(bundleContext);

		// Stop GAIA WS adapter
		if (this.gaiaAdapter != null) {
			this.gaiaAdapter.stop(bundleContext);
			this.gaiaAdapter = null;
		}

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

		// Unregister extensions
		if (this.extensions != null) {
			this.extensions.stop(bundleContext);
		}

		Activator.instance = null;
		Activator.bundleContext = null;
	}

	public Platform getPlatform() {
		return (this.platformTracker != null) ? this.platformTracker.getService() : null;
	}

	public GAIA getGAIA() {
		return (this.gaiaAdapter != null) ? this.gaiaAdapter.getService() : null;
	}

}

// SetupUtil.loadNodeConfigIniProperties(bundleContext, indexProviderProps);

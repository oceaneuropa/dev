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

import org.orbit.platform.runtime.core.Platform;
import org.orbit.platform.runtime.core.PlatformImpl;
import org.orbit.platform.runtime.gaia.service.GAIA;
import org.orbit.platform.runtime.gaia.ws.GaiaAdapter;
import org.orbit.platform.runtime.util.OSTracker;
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
	protected OSTracker osTracker;
	protected GaiaAdapter gaiaAdapter;

	@Override
	public void start(BundleContext bundleContext) throws Exception {
		LOG.info("start()");

		Activator.bundleContext = bundleContext;
		Activator.instance = this;

		// Register extensions
		this.extensions = new Extensions();
		this.extensions.start(bundleContext);

		// Start tracking OS service
		this.osTracker = new OSTracker();
		this.osTracker.start(bundleContext);

		Map<Object, Object> properties = new Hashtable<Object, Object>();
		PropertyUtil.loadProperty(bundleContext, properties, OSConstants.ORBIT_INDEX_SERVICE_URL);
		// Start tracking GAIA service (for starting GAIA web service)
		this.gaiaAdapter = new GaiaAdapter(properties);
		this.gaiaAdapter.start(bundleContext);

		// Start OS impl
		PlatformImpl.getInstance().start(bundleContext);
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		LOG.info("stop()");

		// Stop tracking GAIA service
		if (this.gaiaAdapter != null) {
			this.gaiaAdapter.stop(bundleContext);
			this.gaiaAdapter = null;
		}

		// Stop OS impl
		PlatformImpl.getInstance().stop(bundleContext);

		// Stop tracking OS service
		if (this.osTracker != null) {
			this.osTracker.stop(bundleContext);
			this.osTracker = null;
		}

		// Unregister extensions
		if (this.extensions != null) {
			this.extensions.stop(bundleContext);
		}

		Activator.instance = null;
		Activator.bundleContext = null;
	}

	public Platform getOS() {
		return (this.osTracker != null) ? this.osTracker.getService() : null;
	}

	public GAIA getGAIA() {
		return (this.gaiaAdapter != null) ? this.gaiaAdapter.getService() : null;
	}

}

// SetupUtil.loadNodeConfigIniProperties(bundleContext, indexProviderProps);

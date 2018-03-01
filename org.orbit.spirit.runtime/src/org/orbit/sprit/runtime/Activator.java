/*******************************************************************************
 * Copyright (c) 2017, 2018 OceanEuropa.
 * All rights reserved.
 *
 * Contributors:
 *     OceanEuropa - initial API and implementation
 *******************************************************************************/
package org.orbit.sprit.runtime;

import java.util.Hashtable;
import java.util.Map;

import org.orbit.sprit.runtime.gaia.Extensions;
import org.orbit.sprit.runtime.gaia.service.GAIA;
import org.orbit.sprit.runtime.gaia.ws.GaiaAdapter;
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
	protected GaiaAdapter gaiaAdapter;

	@Override
	public void start(BundleContext bundleContext) throws Exception {
		LOG.info("start()");

		Activator.bundleContext = bundleContext;
		Activator.instance = this;

		// load config properties
		Map<Object, Object> properties = new Hashtable<Object, Object>();
		PropertyUtil.loadProperty(bundleContext, properties, Constants.ORBIT_INDEX_SERVICE_URL);

		// Register extensions
		this.extensions = new Extensions();
		this.extensions.start(bundleContext);

		// Start GAIA WS adapter
		this.gaiaAdapter = new GaiaAdapter(properties);
		this.gaiaAdapter.start(bundleContext);
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		LOG.info("stop()");

		// Stop GAIA WS adapter
		if (this.gaiaAdapter != null) {
			this.gaiaAdapter.stop(bundleContext);
			this.gaiaAdapter = null;
		}

		// Unregister extensions
		if (this.extensions != null) {
			this.extensions.stop(bundleContext);
		}

		Activator.instance = null;
		Activator.bundleContext = null;
	}

	public GAIA getGAIA() {
		return (this.gaiaAdapter != null) ? this.gaiaAdapter.getService() : null;
	}

}

/*******************************************************************************
 * Copyright (c) 2017, 2018 OceanEuropa.
 * All rights reserved.
 *
 * Contributors:
 *     OceanEuropa - initial API and implementation
 *******************************************************************************/
package org.orbit.spirit.runtime;

import java.util.Hashtable;
import java.util.Map;

import org.origin.common.osgi.AbstractBundleActivator;
import org.origin.common.util.PropertyUtil;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Activator extends AbstractBundleActivator implements BundleActivator {

	protected static Logger LOG = LoggerFactory.getLogger(Activator.class);

	protected static Activator instance;

	public static Activator getInstance() {
		return instance;
	}

	@Override
	public void start(BundleContext bundleContext) throws Exception {
		LOG.info("start()");
		super.start(bundleContext);
		Activator.instance = this;

		// load config properties
		Map<Object, Object> properties = new Hashtable<Object, Object>();
		PropertyUtil.loadProperty(bundleContext, properties, SpiritConstants.ORBIT_INDEX_SERVICE_URL);

		// Register extensions
		Extensions.INSTANCE.start(bundleContext);

		// Start service adapters
		SpiritServices.getInstance().start(bundleContext);
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		LOG.info("stop()");

		// Stop service adapters
		SpiritServices.getInstance().stop(bundleContext);

		// Unregister extensions
		Extensions.INSTANCE.stop(bundleContext);

		Activator.instance = null;
		super.stop(bundleContext);
	}

}

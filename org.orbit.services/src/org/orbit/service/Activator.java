/*******************************************************************************
 * Copyright (c) 2017, 2018 OceanEuropa.
 * All rights reserved.
 *
 * Contributors:
 *     OceanEuropa - initial API and implementation
 *******************************************************************************/
package org.orbit.service;

import org.orbit.service.websocket.ServerContainerAdapter;
import org.orbit.service.websocket.WebSocketDeployer;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {

	protected static BundleContext context;
	protected static Activator instance;

	public static BundleContext getBundleContext() {
		return context;
	}

	public static Activator getInstance() {
		return instance;
	}

	protected WebSocketDeployer webSocketDeployer;
	protected ServerContainerAdapter webSocketServerContainerAdapter;
	// protected HttpServletDeployer httpServletDeployer;

	@Override
	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
		Activator.instance = this;

		this.webSocketDeployer = new WebSocketDeployer();
		this.webSocketDeployer.start(bundleContext);

		this.webSocketServerContainerAdapter = new ServerContainerAdapter();
		this.webSocketServerContainerAdapter.start(bundleContext);

		// this.httpServletDeployer = new HttpServletDeployer();
		// this.httpServletDeployer.start(bundleContext);
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		// if (this.httpServletDeployer != null) {
		// this.httpServletDeployer.stop(bundleContext);
		// this.httpServletDeployer = null;
		// }

		if (this.webSocketServerContainerAdapter != null) {
			this.webSocketServerContainerAdapter.stop(bundleContext);
			this.webSocketServerContainerAdapter = null;
		}

		if (this.webSocketDeployer != null) {
			this.webSocketDeployer.stop(bundleContext);
			this.webSocketDeployer = null;
		}

		Activator.instance = null;
		Activator.context = null;
	}

}

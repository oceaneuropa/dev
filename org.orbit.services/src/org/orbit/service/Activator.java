package org.orbit.service;

import org.orbit.service.websocket.ServerContainerAdapter;
import org.orbit.service.websocket.WebSocketDeployer;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {

	protected WebSocketDeployer webSocketDeployer;
	protected ServerContainerAdapter webSocketServerContainerAdapter;

	@Override
	public void start(BundleContext bundleContext) throws Exception {
		this.webSocketDeployer = new WebSocketDeployer();
		this.webSocketDeployer.start(bundleContext);

		this.webSocketServerContainerAdapter = new ServerContainerAdapter();
		this.webSocketServerContainerAdapter.start(bundleContext);
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		if (this.webSocketServerContainerAdapter != null) {
			this.webSocketServerContainerAdapter.stop(bundleContext);
			this.webSocketServerContainerAdapter = null;
		}

		if (this.webSocketDeployer != null) {
			this.webSocketDeployer.stop(bundleContext);
			this.webSocketDeployer = null;
		}
	}

}

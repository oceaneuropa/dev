package org.orbit.service;

import org.orbit.service.program.IProgramExtensionService;
import org.orbit.service.program.impl.ProgramExtensionServiceImpl;
import org.orbit.service.program.util.ProgramExtensionServiceTracker;
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

	protected ProgramExtensionServiceTracker programServiceTracker;
	protected WebSocketDeployer webSocketDeployer;
	protected ServerContainerAdapter webSocketServerContainerAdapter;

	@Override
	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
		Activator.instance = this;

		this.programServiceTracker = new ProgramExtensionServiceTracker();
		this.programServiceTracker.start(bundleContext);
		ProgramExtensionServiceImpl.getInstance().start(bundleContext);

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

		ProgramExtensionServiceImpl.getInstance().stop(bundleContext);
		if (this.programServiceTracker != null) {
			this.programServiceTracker.stop(bundleContext);
			this.programServiceTracker = null;
		}

		Activator.instance = null;
		Activator.context = null;
	}

	public IProgramExtensionService getProgramExtensionService() {
		return (this.programServiceTracker != null) ? this.programServiceTracker.getProgramService() : null;
	}

}

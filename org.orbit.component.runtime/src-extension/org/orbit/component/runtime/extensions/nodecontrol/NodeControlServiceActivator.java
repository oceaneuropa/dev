package org.orbit.component.runtime.extensions.nodecontrol;

import java.util.Map;

import org.orbit.component.runtime.tier3.nodecontrol.service.NodeControlService;
import org.orbit.component.runtime.tier3.nodecontrol.service.NodeControlServiceImpl;
import org.orbit.platform.sdk.IPlatformContext;
import org.orbit.platform.sdk.IProcess;
import org.orbit.platform.sdk.extensions.ServiceActivator;
import org.origin.common.rest.util.LifecycleAware;
import org.osgi.framework.BundleContext;

public class NodeControlServiceActivator implements ServiceActivator {

	public static final String ID = "org.orbit.component.runtime.NodeControlServiceActivator";

	public static NodeControlServiceActivator INSTANCE = new NodeControlServiceActivator();

	@Override
	public void start(IPlatformContext context, IProcess process) {
		BundleContext bundleContext = context.getBundleContext();
		Map<Object, Object> properties = context.getProperties();

		// Start service
		NodeControlServiceImpl nodeControl = new NodeControlServiceImpl(properties);
		nodeControl.start(bundleContext);

		process.adapt(NodeControlService.class, nodeControl);
	}

	@Override
	public void stop(IPlatformContext context, IProcess process) {
		BundleContext bundleContext = context.getBundleContext();

		// Stop service
		NodeControlService nodeControl = process.getAdapter(NodeControlService.class);
		if (nodeControl instanceof LifecycleAware) {
			((LifecycleAware) nodeControl).stop(bundleContext);
		}
	}

}

package org.orbit.component.runtime.extensions.serviceactivator;

import java.util.Map;

import org.orbit.component.runtime.tier3.nodemanagement.service.NodeManagementService;
import org.orbit.component.runtime.tier3.nodemanagement.service.NodeManagementServiceImpl;
import org.orbit.platform.sdk.IPlatformContext;
import org.orbit.platform.sdk.IProcess;
import org.orbit.platform.sdk.ServiceActivator;
import org.origin.common.rest.util.LifecycleAware;
import org.osgi.framework.BundleContext;

public class NodeManagementServiceActivator implements ServiceActivator {

	public static final String ID = "org.orbit.component.runtime.NodeManagementServiceActivator";

	public static NodeManagementServiceActivator INSTANCE = new NodeManagementServiceActivator();

	@Override
	public void start(IPlatformContext context, IProcess process) {
		BundleContext bundleContext = context.getBundleContext();
		Map<Object, Object> properties = context.getProperties();

		// Start NodeManagementService
		NodeManagementServiceImpl nodeControlService = new NodeManagementServiceImpl(properties);
		nodeControlService.start(bundleContext);

		process.adapt(NodeManagementService.class, nodeControlService);
	}

	@Override
	public void stop(IPlatformContext context, IProcess process) {
		BundleContext bundleContext = context.getBundleContext();

		// Stop NodeManagementService
		NodeManagementService nodeControlService = process.getAdapter(NodeManagementService.class);
		if (nodeControlService instanceof LifecycleAware) {
			((LifecycleAware) nodeControlService).stop(bundleContext);
		}
	}

}

package org.orbit.component.runtime.extensions.serviceactivator;

import java.util.Map;

import org.orbit.component.runtime.tier3.nodecontrol.service.NodeManagementService;
import org.orbit.component.runtime.tier3.nodecontrol.service.NodeManagementServiceImpl;
import org.orbit.platform.sdk.IPlatformContext;
import org.orbit.platform.sdk.IProcess;
import org.orbit.platform.sdk.ServiceActivator;
import org.osgi.framework.BundleContext;

public class NodeManagementServiceActivator implements ServiceActivator {

	public static final String ID = "component.node_management.service_activator";

	public static NodeManagementServiceActivator INSTANCE = new NodeManagementServiceActivator();

	@Override
	public void start(IPlatformContext context, IProcess process) {
		BundleContext bundleContext = context.getBundleContext();
		Map<Object, Object> properties = context.getProperties();

		// Start NodeManagementService
		NodeManagementServiceImpl nodeManagementService = new NodeManagementServiceImpl(properties);
		nodeManagementService.start(bundleContext);

		process.adapt(NodeManagementService.class, nodeManagementService);
	}

	@Override
	public void stop(IPlatformContext context, IProcess process) {
		BundleContext bundleContext = context.getBundleContext();

		// Stop NodeManagementService
		NodeManagementService nodeManagementService = process.getAdapter(NodeManagementService.class);
		if (nodeManagementService instanceof NodeManagementServiceImpl) {
			((NodeManagementServiceImpl) nodeManagementService).stop(bundleContext);
		}
	}

}

// @Override
// public String getName() {
// return "NodeManagement";
// }

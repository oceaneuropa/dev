package org.orbit.component.runtime.extension.nodecontrol;

import java.util.Map;

import org.orbit.component.runtime.tier3.nodecontrol.service.NodeControlService;
import org.orbit.component.runtime.tier3.nodecontrol.service.NodeControlServiceImpl;
import org.orbit.platform.sdk.IProcess;
import org.orbit.platform.sdk.ProcessContext;
import org.orbit.platform.sdk.ServiceActivator;
import org.origin.common.service.ILifecycle;
import org.osgi.framework.BundleContext;

/**
 * 
 * @author <a href="mailto:yangyang4j@gmail.com">Yang Yang</a>
 *
 */
public class NodeControlServiceActivator implements ServiceActivator {

	public static final String ID = "org.orbit.component.runtime.NodeControlServiceActivator";

	public static NodeControlServiceActivator INSTANCE = new NodeControlServiceActivator();

	@Override
	public void start(ProcessContext context, IProcess process) throws Exception {
		BundleContext bundleContext = context.getBundleContext();
		Map<Object, Object> properties = context.getProperties();

		// Start service
		NodeControlServiceImpl nodeControl = new NodeControlServiceImpl(properties);
		nodeControl.start(bundleContext);

		process.adapt(NodeControlService.class, nodeControl);
	}

	@Override
	public void stop(ProcessContext context, IProcess process) throws Exception {
		BundleContext bundleContext = context.getBundleContext();

		// Stop service
		NodeControlService nodeControl = process.getAdapter(NodeControlService.class);
		if (nodeControl instanceof ILifecycle) {
			((ILifecycle) nodeControl).stop(bundleContext);
		}
	}

}

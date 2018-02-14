package org.orbit.component.runtime.extensions.servicecontrol;

import java.util.Map;

import org.orbit.component.runtime.tier3.nodecontrol.service.NodeControlServiceImpl;
import org.orbit.platform.sdk.servicecontrol.ServiceControlImpl;
import org.osgi.framework.BundleContext;

public class TransferAgentServiceControl extends ServiceControlImpl {

	public static TransferAgentServiceControl INSTANCE = new TransferAgentServiceControl();

	protected NodeControlServiceImpl transferAgentService;

	@Override
	public void start(BundleContext bundleContext, Map<String, Object> properties) {
		NodeControlServiceImpl transferAgentService = new NodeControlServiceImpl();
		transferAgentService.start(bundleContext);
		this.transferAgentService = transferAgentService;
	}

	@Override
	public void stop(BundleContext bundleContext, Map<String, Object> properties) {
		if (this.transferAgentService != null) {
			this.transferAgentService.stop(bundleContext);
			this.transferAgentService = null;
		}
	}

}

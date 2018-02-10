package org.orbit.component.runtime.extensions.servicecontrol;

import java.util.Map;

import org.orbit.component.runtime.tier3.transferagent.service.TransferAgentServiceImpl;
import org.orbit.sdk.ServiceControlImpl;
import org.osgi.framework.BundleContext;

public class TransferAgentServiceControl extends ServiceControlImpl {

	public static TransferAgentServiceControl INSTANCE = new TransferAgentServiceControl();

	protected TransferAgentServiceImpl transferAgentService;

	@Override
	public void start(BundleContext bundleContext, Map<String, Object> properties) {
		TransferAgentServiceImpl transferAgentService = new TransferAgentServiceImpl();
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

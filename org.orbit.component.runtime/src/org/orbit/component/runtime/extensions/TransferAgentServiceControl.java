package org.orbit.component.runtime.extensions;

import org.orbit.component.runtime.tier3.transferagent.service.TransferAgentServiceImpl;
import org.orbit.os.runtime.api.ServiceControl;
import org.osgi.framework.BundleContext;

public class TransferAgentServiceControl implements ServiceControl {

	public static TransferAgentServiceControl INSTANCE = new TransferAgentServiceControl();

	protected TransferAgentServiceImpl transferAgentService;

	@Override
	public void start(BundleContext bundleContext) {
		TransferAgentServiceImpl transferAgentService = new TransferAgentServiceImpl();
		transferAgentService.start(bundleContext);
		this.transferAgentService = transferAgentService;
	}

	@Override
	public void stop(BundleContext bundleContext) {
		if (this.transferAgentService != null) {
			this.transferAgentService.stop(bundleContext);
			this.transferAgentService = null;
		}
	}

}

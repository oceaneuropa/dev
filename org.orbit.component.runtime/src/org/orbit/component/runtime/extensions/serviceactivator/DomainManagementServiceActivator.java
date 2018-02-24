package org.orbit.component.runtime.extensions.serviceactivator;

import java.util.Map;

import org.orbit.component.runtime.tier3.domain.service.DomainManagementService;
import org.orbit.component.runtime.tier3.domain.service.DomainManagementServiceImpl;
import org.orbit.platform.sdk.IPlatformContext;
import org.orbit.platform.sdk.IProcess;
import org.orbit.platform.sdk.ServiceActivatorImpl;
import org.osgi.framework.BundleContext;

public class DomainManagementServiceActivator extends ServiceActivatorImpl {

	public static final String ID = "component.domain_management.service_activator";

	public static DomainManagementServiceActivator INSTANCE = new DomainManagementServiceActivator();

	@Override
	public String getProcessName() {
		return "DomainService";
	}

	@Override
	public void start(IPlatformContext context, IProcess process) {
		BundleContext bundleContext = context.getBundleContext();
		Map<String, Object> properties = context.getProperties();

		// Start DomainManagementService
		DomainManagementServiceImpl domainService = new DomainManagementServiceImpl(properties);
		domainService.start(bundleContext);

		process.adapt(DomainManagementService.class, domainService);
	}

	@Override
	public void stop(IPlatformContext context, IProcess process) {
		BundleContext bundleContext = context.getBundleContext();

		// Stop DomainManagementService
		DomainManagementService domainService = process.getAdapter(DomainManagementService.class);
		if (domainService instanceof DomainManagementServiceImpl) {
			((DomainManagementServiceImpl) domainService).stop(bundleContext);
		}
	}

}

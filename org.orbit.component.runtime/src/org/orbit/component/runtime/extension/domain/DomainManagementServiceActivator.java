package org.orbit.component.runtime.extension.domain;

import java.util.Map;

import org.orbit.component.runtime.tier3.domain.service.DomainManagementService;
import org.orbit.component.runtime.tier3.domain.service.DomainManagementServiceImpl;
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
public class DomainManagementServiceActivator implements ServiceActivator {

	public static final String ID = "org.orbit.component.runtime.DomainManagementServiceActivator";

	public static DomainManagementServiceActivator INSTANCE = new DomainManagementServiceActivator();

	@Override
	public void start(ProcessContext context, IProcess process) throws Exception {
		BundleContext bundleContext = context.getBundleContext();
		Map<Object, Object> properties = context.getProperties();

		// Start DomainManagementService
		DomainManagementServiceImpl domainService = new DomainManagementServiceImpl(properties);
		domainService.start(bundleContext);

		process.adapt(DomainManagementService.class, domainService);
	}

	@Override
	public void stop(ProcessContext context, IProcess process) throws Exception {
		BundleContext bundleContext = context.getBundleContext();

		// Stop DomainManagementService
		DomainManagementService domainService = process.getAdapter(DomainManagementService.class);
		if (domainService instanceof ILifecycle) {
			((ILifecycle) domainService).stop(bundleContext);
		}
	}

}

package org.orbit.infra.runtime.extensions.extensionregistry;

import java.util.Map;

import org.orbit.infra.runtime.extensionregistry.service.ExtensionRegistryService;
import org.orbit.infra.runtime.extensionregistry.service.ExtensionRegistryServiceImpl;
import org.orbit.platform.sdk.IPlatformContext;
import org.orbit.platform.sdk.IProcess;
import org.orbit.platform.sdk.serviceactivator.ServiceActivator;
import org.origin.common.rest.util.LifecycleAware;
import org.osgi.framework.BundleContext;

public class ExtensionRegistryActivator implements ServiceActivator {

	public static final String ID = "org.orbit.infra.runtime.ExtensionRegistryActivator";

	public static ExtensionRegistryActivator INSTANCE = new ExtensionRegistryActivator();

	@Override
	public void start(IPlatformContext context, IProcess process) {
		BundleContext bundleContext = context.getBundleContext();
		Map<Object, Object> properties = context.getProperties();

		// Start ExtensionRegistryService
		ExtensionRegistryServiceImpl service = new ExtensionRegistryServiceImpl(properties);
		service.start(bundleContext);

		process.adapt(ExtensionRegistryService.class, service);
	}

	@Override
	public void stop(IPlatformContext context, IProcess process) {
		BundleContext bundleContext = context.getBundleContext();

		// Stop ExtensionRegistryService
		ExtensionRegistryService service = process.getAdapter(ExtensionRegistryService.class);
		if (service instanceof LifecycleAware) {
			((LifecycleAware) service).stop(bundleContext);
		}
	}

}

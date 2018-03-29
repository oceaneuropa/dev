package org.orbit.infra.runtime.extension.indexes;

import java.util.Map;

import org.orbit.infra.runtime.indexes.service.IndexService;
import org.orbit.infra.runtime.indexes.service.IndexServiceImpl;
import org.orbit.platform.sdk.IPlatformContext;
import org.orbit.platform.sdk.IProcess;
import org.orbit.platform.sdk.extensions.ServiceActivator;
import org.origin.common.rest.util.LifecycleAware;
import org.osgi.framework.BundleContext;

public class IndexServiceActivator implements ServiceActivator {

	public static final String ID = "org.orbit.infra.runtime.IndexServiceActivator";

	public static IndexServiceActivator INSTANCE = new IndexServiceActivator();

	@Override
	public void start(IPlatformContext context, IProcess process) {
		BundleContext bundleContext = context.getBundleContext();
		Map<Object, Object> properties = context.getProperties();

		// Start IndexService
		IndexServiceImpl indexService = new IndexServiceImpl(properties);
		indexService.start(bundleContext);

		process.adapt(IndexService.class, indexService);
	}

	@Override
	public void stop(IPlatformContext context, IProcess process) {
		BundleContext bundleContext = context.getBundleContext();

		// Stop IndexService
		IndexService indexService = process.getAdapter(IndexService.class);
		if (indexService instanceof LifecycleAware) {
			((LifecycleAware) indexService).stop(bundleContext);
		}
	}

}

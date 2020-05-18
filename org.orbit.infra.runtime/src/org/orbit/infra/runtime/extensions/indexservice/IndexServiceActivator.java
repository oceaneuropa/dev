package org.orbit.infra.runtime.extensions.indexservice;

import java.util.Map;

import org.orbit.infra.runtime.indexes.service.IndexService;
import org.orbit.infra.runtime.indexes.service.IndexServiceInMemoryImpl;
import org.orbit.platform.sdk.IProcess;
import org.orbit.platform.sdk.ProcessContext;
import org.orbit.platform.sdk.ServiceActivator;
import org.origin.common.rest.util.LifecycleAware;
import org.osgi.framework.BundleContext;

/**
 * 
 * @author <a href="mailto:yangyang4j@gmail.com">Yang Yang</a>
 *
 */
public class IndexServiceActivator implements ServiceActivator {

	public static final String ID = "org.orbit.infra.runtime.IndexServiceActivator";

	@Override
	public void start(ProcessContext context, IProcess process) throws Exception {
		BundleContext bundleContext = context.getBundleContext();
		Map<Object, Object> properties = context.getProperties();

		// IndexServiceDatabaseImpl indexService = new IndexServiceDatabaseImpl(properties);
		IndexServiceInMemoryImpl indexService = new IndexServiceInMemoryImpl(properties);
		indexService.start(bundleContext);

		process.adapt(IndexService.class, indexService);
	}

	@Override
	public void stop(ProcessContext context, IProcess process) throws Exception {
		BundleContext bundleContext = context.getBundleContext();

		IndexService indexService = process.getAdapter(IndexService.class);
		if (indexService instanceof LifecycleAware) {
			((LifecycleAware) indexService).stop(bundleContext);
		}
	}

}

// public static IndexServiceActivator INSTANCE = new IndexServiceActivator();
// protected static Logger LOG = LoggerFactory.getLogger(IndexServiceActivator.class);

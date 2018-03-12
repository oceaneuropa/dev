package org.orbit.infra.runtime.extensions;

import java.util.Map;

import org.orbit.infra.runtime.indexes.service.IndexService;
import org.orbit.infra.runtime.indexes.service.IndexServiceImpl;
import org.orbit.platform.sdk.IPlatformContext;
import org.orbit.platform.sdk.IProcess;
import org.orbit.platform.sdk.ServiceActivator;
import org.osgi.framework.BundleContext;

public class IndexServiceActivator implements ServiceActivator {

	public static final String ID = "orbit.index_service.service_activator";

	public static IndexServiceActivator INSTANCE = new IndexServiceActivator();

	@Override
	public void start(IPlatformContext context, IProcess process) {
		BundleContext bundleContext = context.getBundleContext();
		Map<String, Object> properties = context.getProperties();

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
		if (indexService instanceof IndexServiceImpl) {
			((IndexServiceImpl) indexService).stop(bundleContext);
		}
	}

}

// protected IParameter[] parameters;

// @Override
// public String getName() {
// return "IndexService";
// }

// public synchronized IParameter[] getParameters() {
// if (this.parameters == null) {
// this.parameters = new IParameter[] { //
// new ParameterImpl("component.index_service.name", "service instance name"), //
// new ParameterImpl("component.index_service.context_root", "web service context root"), //
// new ParameterImpl("component.index_service.jdbc.driver", "JDBC driver"), //
// new ParameterImpl("component.index_service.jdbc.url", "JDBC URL"), //
// new ParameterImpl("component.index_service.jdbc.username", "JDBC username"), //
// new ParameterImpl("component.index_service.jdbc.password", "JDBC password") //
// };
// }
// return this.parameters;
// }

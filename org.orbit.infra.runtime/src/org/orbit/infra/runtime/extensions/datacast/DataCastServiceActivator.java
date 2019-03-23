package org.orbit.infra.runtime.extensions.datacast;

import java.util.Map;

import org.orbit.infra.runtime.datacast.service.DataCastService;
import org.orbit.infra.runtime.datacast.service.impl.DataCastServiceImpl;
import org.orbit.platform.sdk.IProcessContext;
import org.orbit.platform.sdk.IProcess;
import org.orbit.platform.sdk.serviceactivator.ServiceActivator;
import org.origin.common.rest.util.LifecycleAware;
import org.osgi.framework.BundleContext;

public class DataCastServiceActivator implements ServiceActivator {

	public static final String ID = "org.orbit.infra.runtime.DataCastServiceActivator";

	public static DataCastServiceActivator INSTANCE = new DataCastServiceActivator();

	@Override
	public void start(IProcessContext context, IProcess process) throws Exception {
		BundleContext bundleContext = context.getBundleContext();
		Map<Object, Object> properties = context.getProperties();

		// Start DataCastService
		DataCastServiceImpl dataCastService = new DataCastServiceImpl(properties);
		dataCastService.start(bundleContext);

		process.adapt(DataCastService.class, dataCastService);
	}

	@Override
	public void stop(IProcessContext context, IProcess process) throws Exception {
		BundleContext bundleContext = context.getBundleContext();

		// Stop DataCastService
		DataCastService dataCastService = process.getAdapter(DataCastService.class);
		if (dataCastService instanceof LifecycleAware) {
			((LifecycleAware) dataCastService).stop(bundleContext);
		}
	}

}

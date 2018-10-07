package org.orbit.infra.runtime.extensions.datatube;

import java.util.Map;

import org.orbit.infra.runtime.datatube.service.DataTubeService;
import org.orbit.infra.runtime.datatube.service.impl.DataTubeServiceImpl;
import org.orbit.platform.sdk.IPlatformContext;
import org.orbit.platform.sdk.IProcess;
import org.orbit.platform.sdk.serviceactivator.ServiceActivator;
import org.origin.common.rest.util.LifecycleAware;
import org.osgi.framework.BundleContext;

public class DataTubeServiceActivator implements ServiceActivator {

	public static final String ID = "org.orbit.infra.runtime.DataTubeServiceActivator";

	public static DataTubeServiceActivator INSTANCE = new DataTubeServiceActivator();

	@Override
	public void start(IPlatformContext context, IProcess process) throws Exception {
		BundleContext bundleContext = context.getBundleContext();
		Map<Object, Object> properties = context.getProperties();

		// Start DataTubeService
		DataTubeServiceImpl dataTubeService = new DataTubeServiceImpl(properties);
		dataTubeService.start(bundleContext);

		process.adapt(DataTubeService.class, dataTubeService);
	}

	@Override
	public void stop(IPlatformContext context, IProcess process) throws Exception {
		BundleContext bundleContext = context.getBundleContext();

		// Stop DataTubeService
		DataTubeService dataTubeService = process.getAdapter(DataTubeService.class);
		if (dataTubeService instanceof LifecycleAware) {
			((LifecycleAware) dataTubeService).stop(bundleContext);
		}
	}

}

package org.orbit.component.runtime.extensions;

import org.orbit.component.runtime.tier1.config.service.ConfigRegistryServiceDatabaseImpl;
import org.orbit.os.runtime.api.ServiceControl;
import org.osgi.framework.BundleContext;

public class ConfigRegistryServiceControl implements ServiceControl {

	public static ConfigRegistryServiceControl INSTANCE = new ConfigRegistryServiceControl();

	protected ConfigRegistryServiceDatabaseImpl configRegistryService;

	@Override
	public void start(BundleContext bundleContext) {
		ConfigRegistryServiceDatabaseImpl configRegistryService = new ConfigRegistryServiceDatabaseImpl();
		configRegistryService.start(bundleContext);
		this.configRegistryService = configRegistryService;
	}

	@Override
	public void stop(BundleContext bundleContext) {
		if (this.configRegistryService != null) {
			this.configRegistryService.stop();
			this.configRegistryService = null;
		}
	}

}

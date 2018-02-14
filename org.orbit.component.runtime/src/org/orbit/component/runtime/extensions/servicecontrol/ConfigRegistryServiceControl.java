package org.orbit.component.runtime.extensions.servicecontrol;

import java.util.Map;

import org.orbit.component.runtime.tier1.config.service.ConfigRegistryServiceDatabaseImpl;
import org.orbit.platform.sdk.servicecontrol.ServiceControlImpl;
import org.osgi.framework.BundleContext;

public class ConfigRegistryServiceControl extends ServiceControlImpl {

	public static ConfigRegistryServiceControl INSTANCE = new ConfigRegistryServiceControl();

	protected ConfigRegistryServiceDatabaseImpl configRegistryService;

	@Override
	public void start(BundleContext bundleContext, Map<String, Object> properties) {
		ConfigRegistryServiceDatabaseImpl configRegistryService = new ConfigRegistryServiceDatabaseImpl();
		configRegistryService.start(bundleContext);
		this.configRegistryService = configRegistryService;
	}

	@Override
	public void stop(BundleContext bundleContext, Map<String, Object> properties) {
		if (this.configRegistryService != null) {
			this.configRegistryService.stop();
			this.configRegistryService = null;
		}
	}

}

package org.orbit.component.runtime.extensions.servicecontrol;

import java.util.Map;

import org.orbit.component.runtime.tier3.domain.service.DomainServiceDatabaseImpl;
import org.orbit.platform.sdk.servicecontrol.ServiceControlImpl;
import org.osgi.framework.BundleContext;

public class DomainServiceServiceControl extends ServiceControlImpl {

	public static DomainServiceServiceControl INSTANCE = new DomainServiceServiceControl();

	protected DomainServiceDatabaseImpl domainService;

	@Override
	public void start(BundleContext bundleContext, Map<String, Object> properties) {
		DomainServiceDatabaseImpl domainService = new DomainServiceDatabaseImpl();
		domainService.start(bundleContext);
		this.domainService = domainService;
	}

	@Override
	public void stop(BundleContext bundleContext, Map<String, Object> properties) {
		if (this.domainService != null) {
			this.domainService.stop(bundleContext);
			this.domainService = null;
		}
	}

}

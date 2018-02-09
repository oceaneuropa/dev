package org.orbit.component.runtime.extensions;

import org.orbit.component.runtime.tier3.domain.service.DomainServiceDatabaseImpl;
import org.orbit.os.runtime.api.ServiceControl;
import org.osgi.framework.BundleContext;

public class DomainServiceServiceControl implements ServiceControl {

	public static DomainServiceServiceControl INSTANCE = new DomainServiceServiceControl();

	protected DomainServiceDatabaseImpl domainService;

	@Override
	public void start(BundleContext bundleContext) {
		DomainServiceDatabaseImpl domainService = new DomainServiceDatabaseImpl();
		domainService.start(bundleContext);
		this.domainService = domainService;
	}

	@Override
	public void stop(BundleContext bundleContext) {
		if (this.domainService != null) {
			this.domainService.stop(bundleContext);
			this.domainService = null;
		}
	}

}

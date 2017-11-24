package org.origin.common.deploy;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.ws.rs.core.Application;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

public abstract class ApplicationAdapter {

	protected BundleContext bundleContext;
	protected ServiceTracker<Application, Application> applicationServiceTracker;
	protected Map<ServiceReference<Application>, Application> applicationsMap;
	protected boolean debug = true;

	/**
	 * 
	 * @param bundleContext
	 */
	public ApplicationAdapter(BundleContext bundleContext) {
		this.applicationsMap = new LinkedHashMap<ServiceReference<Application>, Application>();
		this.bundleContext = bundleContext;
	}

	public Map<ServiceReference<Application>, Application> getApplicationsMap() {
		return this.applicationsMap;
	}

	/**
	 * Start tracking Application services.
	 * 
	 */
	public void start() {
		this.applicationServiceTracker = new ServiceTracker<Application, Application>(bundleContext, Application.class, new ServiceTrackerCustomizer<Application, Application>() {
			@Override
			public Application addingService(ServiceReference<Application> reference) {
				Application application = bundleContext.getService(reference);

				applicationsMap.put(reference, application);

				deploy(reference, application);

				return application;
			}

			@Override
			public void modifiedService(ServiceReference<Application> reference, Application application) {
				applicationsMap.put(reference, application);

				undeploy(reference, application);
				deploy(reference, application);
			}

			@Override
			public void removedService(ServiceReference<Application> reference, Application application) {
				applicationsMap.remove(reference);

				undeploy(reference, application);
			}
		});
		this.applicationServiceTracker.open();
	}

	/**
	 * Stop tracking Application services.
	 * 
	 */
	public void stop() {
		this.applicationServiceTracker.close();
	}

	/**
	 * 
	 * @param reference
	 * @param application
	 */
	public abstract void deploy(ServiceReference<Application> reference, Application application);

	/**
	 * 
	 * @param reference
	 * @param application
	 */
	public abstract void undeploy(ServiceReference<Application> reference, Application application);

}

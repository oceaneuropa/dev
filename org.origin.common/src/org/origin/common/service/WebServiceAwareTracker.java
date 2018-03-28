package org.origin.common.service;

import java.util.ArrayList;
import java.util.List;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

public class WebServiceAwareTracker {

	protected ServiceTracker<WebServiceAware, WebServiceAware> serviceTracker;
	protected List<WebServiceAware> webServiceAwares = new ArrayList<WebServiceAware>();

	public WebServiceAware[] getWebServiceAwares() {
		return this.webServiceAwares.toArray(new WebServiceAware[this.webServiceAwares.size()]);
	}

	/**
	 * 
	 * @param bundleContext
	 */
	public void start(final BundleContext bundleContext) {
		// clean up data
		this.webServiceAwares.clear();

		// Start tracking WebServiceAware services
		this.serviceTracker = new ServiceTracker<WebServiceAware, WebServiceAware>(bundleContext, WebServiceAware.class, new ServiceTrackerCustomizer<WebServiceAware, WebServiceAware>() {
			@Override
			public WebServiceAware addingService(ServiceReference<WebServiceAware> reference) {
				WebServiceAware webServiceAware = bundleContext.getService(reference);
				if (webServiceAware != null) {
					serviceAdded(bundleContext, reference, webServiceAware);
				}
				return webServiceAware;
			}

			@Override
			public void modifiedService(ServiceReference<WebServiceAware> reference, WebServiceAware webServiceAware) {
			}

			@Override
			public void removedService(ServiceReference<WebServiceAware> reference, WebServiceAware webServiceAware) {
				serviceRemoved(bundleContext, reference, webServiceAware);
			}
		});
		this.serviceTracker.open();
	}

	/**
	 * 
	 * @param bundleContext
	 */
	public void stop(BundleContext bundleContext) {
		// Stop tracking WebServiceAware services
		if (this.serviceTracker != null) {
			this.serviceTracker.close();
			this.serviceTracker = null;
		}

		// clean up data
		this.webServiceAwares.clear();
	}

	/**
	 * 
	 * @param context
	 * @param reference
	 * @param webServiceAware
	 */
	protected void serviceAdded(BundleContext context, ServiceReference<WebServiceAware> reference, WebServiceAware webServiceAware) {
		String name = (String) reference.getProperty(WebServiceAware.PROP_NAME);
		String hostURL = (String) reference.getProperty(WebServiceAware.PROP_HOST_URL);
		String contextRoot = (String) reference.getProperty(WebServiceAware.PROP_CONTEXT_ROOT);
		if (name != null && hostURL != null && contextRoot != null) {
			if (!this.webServiceAwares.contains(webServiceAware)) {
				this.webServiceAwares.add(webServiceAware);
			}
		}
	}

	/**
	 * 
	 * @param context
	 * @param reference
	 * @param webServiceAware
	 */
	protected void serviceRemoved(BundleContext context, ServiceReference<WebServiceAware> reference, WebServiceAware webServiceAware) {
		String name = (String) reference.getProperty(WebServiceAware.PROP_NAME);
		String hostURL = (String) reference.getProperty(WebServiceAware.PROP_HOST_URL);
		String contextRoot = (String) reference.getProperty(WebServiceAware.PROP_CONTEXT_ROOT);
		if (name != null && hostURL != null && contextRoot != null) {
			if (this.webServiceAwares.contains(webServiceAware)) {
				this.webServiceAwares.remove(webServiceAware);
			}
		}
	}

}

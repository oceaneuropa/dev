package org.orbit.infra.runtime.indexes.ws;

import java.util.HashMap;
import java.util.Map;

import org.orbit.infra.runtime.indexes.service.IndexService;
import org.origin.common.rest.server.FeatureConstants;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IndexServiceAdapter {

	protected static Logger LOG = LoggerFactory.getLogger(IndexServiceAdapter.class);

	protected Map<Object, Object> properties;
	protected ServiceTracker<IndexService, IndexService> serviceTracker;
	protected IndexServiceWSApplication webService;
	protected IndexServiceIndexTimer serviceIndexTimer;

	public IndexServiceAdapter(Map<Object, Object> properties) {
		this.properties = checkProperties(properties);
	}

	protected Map<Object, Object> checkProperties(Map<Object, Object> properties) {
		if (properties == null) {
			properties = new HashMap<Object, Object>();
		}
		return properties;
	}

	public IndexService getService() {
		return (this.serviceTracker != null) ? this.serviceTracker.getService() : null;
	}

	public void start(final BundleContext bundleContext) {
		LOG.debug("start()");

		this.serviceTracker = new ServiceTracker<IndexService, IndexService>(bundleContext, IndexService.class, new ServiceTrackerCustomizer<IndexService, IndexService>() {
			@Override
			public IndexService addingService(ServiceReference<IndexService> reference) {
				IndexService service = bundleContext.getService(reference);
				LOG.debug("ServiceTracker IndexService [" + service + "] is added.");

				doStart(bundleContext, service);

				return service;
			}

			@Override
			public void modifiedService(ServiceReference<IndexService> reference, IndexService service) {
				LOG.debug("ServiceTracker IndexService [" + service + "] is modified.");
			}

			@Override
			public void removedService(ServiceReference<IndexService> reference, IndexService service) {
				LOG.debug("ServiceTracker IndexService [" + service + "] is removed.");

				doStop(bundleContext, service);
			}
		});
		this.serviceTracker.open();
	}

	public void stop(final BundleContext bundleContext) {
		LOG.debug("stop()");

		if (this.serviceTracker != null) {
			this.serviceTracker.close();
			this.serviceTracker = null;
		}
	}

	/**
	 * 
	 * @param bundleContext
	 * @param service
	 */
	protected void doStart(BundleContext bundleContext, IndexService service) {
		// 1. start web service
		LOG.debug("start web service");
		this.webService = new IndexServiceWSApplication(service, FeatureConstants.METADATA | FeatureConstants.NAME | FeatureConstants.PING | FeatureConstants.ECHO);
		this.webService.start(bundleContext);

		// 2. start index timer
		LOG.debug("start index timer");
		this.serviceIndexTimer = new IndexServiceIndexTimer(service, service);
		this.serviceIndexTimer.start();
	}

	/**
	 * 
	 * @param bundleContext
	 * @param service
	 */
	protected void doStop(BundleContext bundleContext, IndexService service) {
		// 1. stop index timer
		LOG.debug("stop index timer");
		if (this.serviceIndexTimer != null) {
			this.serviceIndexTimer.stop();
			this.serviceIndexTimer = null;
		}

		// 2. stop web service
		LOG.debug("stop web service");
		if (this.webService != null) {
			this.webService.stop(bundleContext);
			this.webService = null;
		}
	}

}

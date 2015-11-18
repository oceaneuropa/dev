package com.osgi.example1;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.http.HttpService;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

/**
 * -Dorg.osgi.service.http.port=9090
 */
public class Activator implements BundleActivator {

	private static BundleContext context;

	static BundleContext getContext() {
		return context;
	}

	protected ServiceTracker<HttpService, HttpService> httpServiceTracker;

	@Override
	public void start(BundleContext bundleContext) throws Exception {
		Printer.pl("Activator.start()");
		Activator.context = bundleContext;

		this.httpServiceTracker = new ServiceTracker<HttpService, HttpService>(bundleContext, HttpService.class, new ServiceTrackerCustomizer<HttpService, HttpService>() {
			@Override
			public HttpService addingService(ServiceReference<HttpService> reference) {
				Printer.pl("Activator.httpServiceTracker.addingService()");
				HttpService httpService = getContext().getService(reference);
				Printer.pl("\t httpService = " + httpService);
				return httpService;
			}

			@Override
			public void modifiedService(ServiceReference<HttpService> reference, HttpService httpService) {
				Printer.pl("Activator.httpServiceTracker.modifiedService()");
				Printer.pl("\t httpService = " + httpService);
			}

			@Override
			public void removedService(ServiceReference<HttpService> reference, HttpService httpService) {
				Printer.pl("Activator.httpServiceTracker.removedService()");
				Printer.pl("\t httpService = " + httpService);
			}
		});
		this.httpServiceTracker.open();
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		Printer.pl("Activator.stop()");

		this.httpServiceTracker.close();

		Activator.context = null;
	}

}

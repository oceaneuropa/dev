package osgi.node.example;

import java.util.LinkedHashSet;
import java.util.Set;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.http.HttpService;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

/**
 * @see Apache Felix HTTP Service http://felix.apache.org/documentation/subprojects/apache-felix-http-service.html#using-the-whiteboard
 */
public abstract class HttpServiceAdapter {

	protected BundleContext bundleContext;
	protected ServiceTracker<HttpService, HttpService> httpServiceTracker;
	protected Set<HttpService> httpServices;
	protected boolean debug = true;

	/**
	 * 
	 * @param bundleContext
	 */
	public HttpServiceAdapter(BundleContext bundleContext) {
		this.httpServices = new LinkedHashSet<HttpService>();
		this.bundleContext = bundleContext;
	}

	public Set<HttpService> getHttpServices() {
		return this.httpServices;
	}

	public void start() {
		this.httpServiceTracker = new ServiceTracker<HttpService, HttpService>(bundleContext, HttpService.class, new ServiceTrackerCustomizer<HttpService, HttpService>() {
			@Override
			public HttpService addingService(ServiceReference<HttpService> reference) {
				HttpService httpService = bundleContext.getService(reference);
				if (debug) {
					Printer.pl("HttpServiceSupport.httpServiceTracker.addingService()");
					Printer.pl("\t httpService = " + httpService);
				}

				httpServices.add(httpService);

				deploy(reference, httpService);

				return httpService;
			}

			@Override
			public void modifiedService(ServiceReference<HttpService> reference, HttpService httpService) {
				if (debug) {
					Printer.pl("HttpServiceSupport.httpServiceTracker.modifiedService()");
					Printer.pl("\t httpService = " + httpService);
				}

				undeploy(reference, httpService);
				deploy(reference, httpService);
			}

			@Override
			public void removedService(ServiceReference<HttpService> reference, HttpService httpService) {
				if (debug) {
					Printer.pl("HttpServiceSupport.httpServiceTracker.removedService()");
					Printer.pl("\t httpService = " + httpService);
				}

				undeploy(reference, httpService);

				httpServices.remove(httpService);
			}
		});
		this.httpServiceTracker.open();
	}

	public void stop() {
		this.httpServiceTracker.close();
	}

	/**
	 * 
	 * @param reference
	 * @param httpService
	 */
	public abstract void deploy(ServiceReference<HttpService> reference, HttpService httpService);

	/**
	 * 
	 * @param reference
	 * @param httpService
	 */
	public abstract void undeploy(ServiceReference<HttpService> reference, HttpService httpService);

}

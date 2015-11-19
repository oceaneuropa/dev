package com.osgi.example1;

import java.util.concurrent.atomic.AtomicReference;

import org.osgi.service.component.ComponentContext;
import org.osgi.service.event.EventAdmin;
import org.osgi.service.http.HttpService;

/**
 * @see http://stackoverflow.com/questions/15782740/which-one-is-called-first-activate-function-or-bind-function-in-scr-runtime
 * @see https://vaadin.com/wiki/-/wiki/Main/using+Declarative+Services
 */
public class BundleHandler {

	protected ComponentContext context;
	protected AtomicReference<EventAdmin> eventAdminReference = new AtomicReference<EventAdmin>();
	protected AtomicReference<HttpService> httpServiceReference = new AtomicReference<HttpService>();

	public void setEventAdmin(EventAdmin eventAdmin) {
		Printer.pl("BundleHandler.setEventAdmin() eventAdmin = " + eventAdmin);
		this.eventAdminReference.set(eventAdmin);
	}

	public void unsetEventAdmin(EventAdmin eventAdmin) {
		Printer.pl("BundleHandler.unsetEventAdmin()");
		this.eventAdminReference.compareAndSet(eventAdmin, null);
	}

	public void setHttpService(HttpService httpService) {
		Printer.pl("BundleHandler.setHttpService() httpService = " + httpService);
		this.httpServiceReference.set(httpService);
	}

	public void unsetHttpService(HttpService httpService) {
		Printer.pl("BundleHandler.unsetHttpService()");
		this.httpServiceReference.compareAndSet(httpService, null);
	}

	/**
	 * Called by OSGi DS if the component is activated.
	 * 
	 * @param context
	 */
	protected void activate(ComponentContext context) {
		this.context = context;
		Printer.pl("BundleHandler.activate() context = " + context);
	}

	/**
	 * Called by OSGi DS if the component is deactivated.
	 * 
	 * @param context
	 */
	protected void deactivate(ComponentContext context) {
		this.context = null;
		Printer.pl("BundleHandler.deactivate()");
	}

}

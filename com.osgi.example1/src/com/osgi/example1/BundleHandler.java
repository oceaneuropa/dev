package com.osgi.example1;

import java.util.concurrent.atomic.AtomicReference;

import org.osgi.service.component.ComponentContext;
import org.osgi.service.event.EventAdmin;

/**
 * @see http://stackoverflow.com/questions/15782740/which-one-is-called-first-activate-function-or-bind-function-in-scr-runtime
 * @see https://vaadin.com/wiki/-/wiki/Main/using+Declarative+Services
 */
public class BundleHandler {

	protected ComponentContext context;
	protected AtomicReference<EventAdmin> eventAdminReference = new AtomicReference<EventAdmin>();

	/**
	 * Called by OSGi DS if the component is activated.
	 * 
	 * @param context
	 */
	protected void activate(ComponentContext context) {
		this.context = context;
		System.out.println("BundleHandler.activate() context = " + context);
	}

	/**
	 * Called by OSGi DS if the component is deactivated.
	 * 
	 * @param context
	 */
	protected void deactivate(ComponentContext context) {
		this.context = null;
		System.out.println("BundleHandler.deactivate()");
	}

	public void setEventAdmin(EventAdmin eventAdmin) {
		System.out.println("BundleHandler.setEventAdmin() eventAdmin = " + eventAdmin);
		this.eventAdminReference.set(eventAdmin);
	}

	public void unsetEventAdmin(EventAdmin eventAdmin) {
		System.out.println("BundleHandler.unsetEventAdmin()");
		this.eventAdminReference.compareAndSet(eventAdmin, null);
	}

}

package tutorial.example1;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;

/**
 * http://felix.apache.org/documentation/tutorials-examples-and-presentations/apache-felix-osgi-tutorial/apache-felix-tutorial-example-1.html
 * 
 */
public class Activator implements BundleActivator, ServiceListener {

	private static BundleContext context;

	static BundleContext getContext() {
		return context;
	}

	@Override
	public void start(BundleContext context) throws Exception {
		System.out.println("tutorial-example-1: Starting to listen for service events @yangyang4j.");
		Activator.context = context;
		context.addServiceListener(this);
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		context.removeServiceListener(this);
		Activator.context = null;
		System.out.println("tutorial-example-1: Stopped listening for service events @yangyang4j.");
	}

	@Override
	public void serviceChanged(ServiceEvent event) {
		String[] objectClass = (String[]) event.getServiceReference().getProperty("objectClass");
		if (event.getType() == ServiceEvent.REGISTERED) {
			System.out.println("tutorial-example-1: Service of type " + objectClass[0] + " registered @yangyang4j.");
		} else if (event.getType() == ServiceEvent.UNREGISTERING) {
			System.out.println("tutorial-example-1: Service of type " + objectClass[0] + " unregistered @yangyang4j.");
		} else if (event.getType() == ServiceEvent.MODIFIED) {
			System.out.println("tutorial-example-1: Service of type " + objectClass[0] + " modified @yangyang4j.");
		}
	}

}

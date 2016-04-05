package dictionary.demo;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {

	private static BundleContext context;

	static BundleContext getContext() {
		return context;
	}

	@Override
	public void start(BundleContext bundleContext) throws Exception {
		System.out.println("Activator.start()");
		Activator.context = bundleContext;
		DictionaryDemo.main(null);
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		System.out.println("Activator.stop()");
		Activator.context = null;
	}

}

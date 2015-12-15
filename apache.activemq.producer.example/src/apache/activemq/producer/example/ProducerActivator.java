package apache.activemq.producer.example;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class ProducerActivator implements BundleActivator {

	private static BundleContext context;

	static BundleContext getContext() {
		return context;
	}

	protected ExampleProducer producer;

	@Override
	public void start(BundleContext bundleContext) throws Exception {
		ProducerActivator.context = bundleContext;

		this.producer = new ExampleProducer();
		this.producer.start();
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		ProducerActivator.context = null;

		if (this.producer != null) {
			this.producer.stop();
			this.producer = null;
		}
	}

}

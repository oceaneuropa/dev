package apache.activemq.consumer.example;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class ConsumerActivator implements BundleActivator {

	private static BundleContext context;

	static BundleContext getContext() {
		return context;
	}

	protected ExampleConsumer consumer;

	@Override
	public void start(BundleContext bundleContext) throws Exception {
		ConsumerActivator.context = bundleContext;

		this.consumer = new ExampleConsumer();
		this.consumer.start();
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		ConsumerActivator.context = null;

		if (this.consumer != null) {
			this.consumer.stop();
			this.consumer = null;
		}
	}

}

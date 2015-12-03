package osgi.node.example;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Container implements BundleActivator {

	private static BundleContext context;

	static BundleContext getContext() {
		return context;
	}

	protected WSDeployer wsDeployer;
	protected NodeApplication nodeApplication;
	protected boolean debug = true;

	@Override
	public void start(BundleContext bundleContext) throws Exception {
		if (debug) {
			Printer.pl("Container.start()");
		}
		Container.context = bundleContext;

		// 1. Start WebService deployer
		this.wsDeployer = new WSDeployer(bundleContext);
		this.wsDeployer.start();

		// 2. Start NodeApplication web service
		this.nodeApplication = new NodeApplication(bundleContext, "/node");
		this.nodeApplication.start();
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		if (debug) {
			Printer.pl("Container.stop()");
		}

		// 1. Stop NodeApplication web service
		if (this.nodeApplication != null) {
			this.nodeApplication.stop();
			this.nodeApplication = null;
		}

		// 2. Stop WebService deployer
		if (this.wsDeployer != null) {
			this.wsDeployer.stop();
			this.wsDeployer = null;
		}

		Container.context = null;
	}

}

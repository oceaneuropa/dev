package osgi.node.cli.example;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {

	private static BundleContext context;
	protected boolean debug = true;

	static BundleContext getContext() {
		return context;
	}

	protected NodeCommand nodeCommand;

	@Override
	public void start(BundleContext bundleContext) throws Exception {
		if (debug) {
			Printer.pl("(osgi.node.cli.example) Activator.start()");
		}

		Activator.context = bundleContext;

		String host = System.getProperty("node.http.host");
		String portStr = System.getProperty("node.http.port");
		int port = 9090;
		try {
			port = Integer.parseInt(portStr);
		} catch (Exception e) {
			port = 9090;
		}

		if (debug) {
			Printer.pl("host = " + host);
			Printer.pl("port = " + port);
		}

		// start node cli
		this.nodeCommand = new NodeCommand(bundleContext, host, port);
		this.nodeCommand.start();
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		if (debug) {
			Printer.pl("(osgi.node.cli.example) Activator.stop()");
		}

		// stop node cli
		if (this.nodeCommand != null) {
			this.nodeCommand.stop();
			this.nodeCommand = null;
		}

		Activator.context = null;
	}

}

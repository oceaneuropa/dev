package osgi.agent.example;

import org.origin.common.util.Printer;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Agent implements BundleActivator {

	private static BundleContext context;

	static BundleContext getContext() {
		return context;
	}

	protected AgentApplication agentApplication;
	protected boolean debug = true;

	@Override
	public void start(BundleContext bundleContext) throws Exception {
		if (debug) {
			Printer.pl("Agent.start()");
		}
		Agent.context = bundleContext;

		// Start AgentApplication web service
		this.agentApplication = new AgentApplication(bundleContext, "/agent");
		this.agentApplication.start();
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		if (debug) {
			Printer.pl("Agent.stop()");
		}

		if (this.agentApplication != null) {
			this.agentApplication.stop();
			this.agentApplication = null;
		}

		Agent.context = null;
	}

}

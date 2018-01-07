package org.orbit.component.cli;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {

	private static BundleContext context;

	static BundleContext getContext() {
		return context;
	}

	protected ServicesCommand servicesCommand;
	protected AuthCommand authCommand;
	protected UserRegistryCommand userRegistryCommand;
	protected AppStoreCommand appStoreCommand;
	protected DomainServiceCommand domainMgmtCommand;
	protected TransferAgentCommand transferAgentCommand;

	@Override
	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;

		// Start commands
		this.servicesCommand = new ServicesCommand();
		this.servicesCommand.start(bundleContext);

		this.authCommand = new AuthCommand();
		this.authCommand.start(bundleContext);

		this.userRegistryCommand = new UserRegistryCommand();
		this.userRegistryCommand.start(bundleContext);

		this.appStoreCommand = new AppStoreCommand(bundleContext);
		this.appStoreCommand.start();

		this.domainMgmtCommand = new DomainServiceCommand(bundleContext);
		this.domainMgmtCommand.start();

		this.transferAgentCommand = new TransferAgentCommand();
		this.transferAgentCommand.start(bundleContext);
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		// Stop commands
		if (this.servicesCommand != null) {
			this.servicesCommand.stop(bundleContext);
			this.servicesCommand = null;
		}

		if (this.authCommand != null) {
			this.authCommand.stop(bundleContext);
			this.authCommand = null;
		}

		if (this.userRegistryCommand != null) {
			this.userRegistryCommand.stop(bundleContext);
			this.userRegistryCommand = null;
		}

		if (this.appStoreCommand != null) {
			this.appStoreCommand.stop();
			this.appStoreCommand = null;
		}

		if (this.domainMgmtCommand != null) {
			this.domainMgmtCommand.stop();
			this.domainMgmtCommand = null;
		}

		if (this.transferAgentCommand != null) {
			this.transferAgentCommand.stop(bundleContext);
			this.transferAgentCommand = null;
		}

		Activator.context = null;
	}

}

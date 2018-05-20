package org.orbit.component.cli;

import org.osgi.framework.BundleContext;

public class OrbitCLI {

	private static Object lock = new Object[0];
	private static OrbitCLI instance = null;

	public static OrbitCLI getInstance() {
		if (instance == null) {
			synchronized (lock) {
				if (instance == null) {
					instance = new OrbitCLI();
				}
			}
		}
		return instance;
	}

	// services
	protected ServicesCommand servicesCommand;

	// tier1
	protected AuthCommand authCommand;
	protected UserRegistryCommand userRegistryCommand;

	// tier2
	protected AppStoreCommand appStoreCommand;

	// tier3
	protected DomainManagementCommand domainMgmtCommand;
	protected NodeControlCommand transferAgentCommand;
	protected NodeControlCommandGeneric transferAgentCommandGeneric;

	// tier4
	protected MissionControlCommand missionControlCommand;

	public void start(final BundleContext bundleContext) {
		// Start commands
		// services
		this.servicesCommand = new ServicesCommand();
		this.servicesCommand.start(bundleContext);

		// tier1
		this.authCommand = new AuthCommand();
		this.authCommand.start(bundleContext);

		this.userRegistryCommand = new UserRegistryCommand();
		this.userRegistryCommand.start(bundleContext);

		// tier2
		this.appStoreCommand = new AppStoreCommand(bundleContext);
		this.appStoreCommand.start();

		// tier3
		this.domainMgmtCommand = new DomainManagementCommand(bundleContext);
		this.domainMgmtCommand.start();

		this.transferAgentCommand = new NodeControlCommand();
		this.transferAgentCommand.start(bundleContext);

		this.transferAgentCommandGeneric = new NodeControlCommandGeneric();
		this.transferAgentCommandGeneric.start(bundleContext);

		// tier4
		this.missionControlCommand = new MissionControlCommand();
		this.missionControlCommand.start(bundleContext);
	}

	public void stop(BundleContext bundleContext) {
		// Stop commands
		// services
		if (this.servicesCommand != null) {
			this.servicesCommand.stop(bundleContext);
			this.servicesCommand = null;
		}

		// tier1
		if (this.authCommand != null) {
			this.authCommand.stop(bundleContext);
			this.authCommand = null;
		}

		if (this.userRegistryCommand != null) {
			this.userRegistryCommand.stop(bundleContext);
			this.userRegistryCommand = null;
		}

		// tier2
		if (this.appStoreCommand != null) {
			this.appStoreCommand.stop();
			this.appStoreCommand = null;
		}

		// tier3
		if (this.domainMgmtCommand != null) {
			this.domainMgmtCommand.stop();
			this.domainMgmtCommand = null;
		}

		if (this.transferAgentCommand != null) {
			this.transferAgentCommand.stop(bundleContext);
			this.transferAgentCommand = null;
		}

		if (this.transferAgentCommandGeneric != null) {
			this.transferAgentCommandGeneric.stop(bundleContext);
			this.transferAgentCommandGeneric = null;
		}

		// tier4
		if (this.missionControlCommand != null) {
			this.missionControlCommand.stop(bundleContext);
			this.missionControlCommand = null;
		}
	}

}
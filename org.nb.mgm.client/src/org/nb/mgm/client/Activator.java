package org.nb.mgm.client;

import org.nb.home.client.cli.HomeAgentCommand;
import org.nb.home.client.cli.HomeLoginCommand;
import org.nb.mgm.client.cli.HomeCommand;
import org.nb.mgm.client.cli.MachineCommand;
import org.nb.mgm.client.cli.ManagementCommand;
import org.nb.mgm.client.cli.ManagementLoginCommand;
import org.nb.mgm.client.cli.ProjectCommand;
import org.nb.mgm.client.cli.ProjectHomeCommand;
import org.nb.mgm.client.cli.ProjectNodeCommand;
import org.nb.mgm.client.cli.ProjectSoftwareCommand;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {

	private static BundleContext context;

	protected ManagementCommand mgmCommand;
	protected ManagementLoginCommand loginCommand;
	protected MachineCommand machineCommand;
	protected HomeCommand homeCommand;
	protected ProjectCommand projectCommand;
	protected ProjectHomeCommand projectHomeCommand;
	protected ProjectNodeCommand projectNodeCommand;
	protected ProjectSoftwareCommand projectSoftwareCommand;

	protected HomeLoginCommand homeLoginCommand;
	protected HomeAgentCommand homeControlCommand;

	static BundleContext getContext() {
		return context;
	}

	@Override
	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;

		// Start management command
		this.mgmCommand = new ManagementCommand(bundleContext);
		this.mgmCommand.start();

		// Start Login command
		this.loginCommand = new ManagementLoginCommand(bundleContext);
		this.loginCommand.start();

		// Start Machine command
		this.machineCommand = new MachineCommand(bundleContext);
		this.machineCommand.start();

		// Start Home command
		this.homeCommand = new HomeCommand(bundleContext);
		this.homeCommand.start();

		// Start Project command
		this.projectCommand = new ProjectCommand(bundleContext);
		this.projectCommand.start();

		// Start ProjectHome command
		this.projectHomeCommand = new ProjectHomeCommand(bundleContext);
		this.projectHomeCommand.start();

		// Start ProjectNode command
		this.projectNodeCommand = new ProjectNodeCommand(bundleContext);
		this.projectNodeCommand.start();

		// Start ProjectSoftware command
		this.projectSoftwareCommand = new ProjectSoftwareCommand(bundleContext);
		this.projectSoftwareCommand.start();

		// Start HomeLoginCommand
		this.homeLoginCommand = new HomeLoginCommand(bundleContext);
		this.homeLoginCommand.start();

		// Start HomeControlCommand
		this.homeControlCommand = new HomeAgentCommand(bundleContext);
		this.homeControlCommand.start();
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		Activator.context = null;

		// Stop management command
		if (this.mgmCommand != null) {
			this.mgmCommand.stop();
			this.mgmCommand = null;
		}

		// Stop Login command
		if (this.loginCommand != null) {
			this.loginCommand.stop();
			this.loginCommand = null;
		}

		// Stop Machine command
		if (this.machineCommand != null) {
			this.machineCommand.stop();
			this.machineCommand = null;
		}

		// Stop Home command
		if (this.homeCommand != null) {
			this.homeCommand.stop();
			this.homeCommand = null;
		}

		// Stop Project command
		if (this.projectCommand != null) {
			this.projectCommand.stop();
			this.projectCommand = null;
		}

		// Stop ProjectHome command
		if (this.projectHomeCommand != null) {
			this.projectHomeCommand.stop();
			this.projectHomeCommand = null;
		}

		// Stop ProjectNode command
		if (this.projectNodeCommand != null) {
			this.projectNodeCommand.stop();
			this.projectNodeCommand = null;
		}

		// Stop ProjectSoftware command
		if (this.projectSoftwareCommand != null) {
			this.projectSoftwareCommand.stop();
			this.projectSoftwareCommand = null;
		}

		// Stop HomeLoginCommand
		if (this.homeLoginCommand != null) {
			this.homeLoginCommand.stop();
			this.homeLoginCommand = null;
		}

		// Stop HomeControlCommand
		if (this.homeControlCommand != null) {
			this.homeControlCommand.stop();
			this.homeControlCommand = null;
		}
	}

}

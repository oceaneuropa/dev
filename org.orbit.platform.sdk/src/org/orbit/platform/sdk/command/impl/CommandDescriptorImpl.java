package org.orbit.platform.sdk.command.impl;

import org.orbit.platform.sdk.command.CommandDescriptor;
import org.orbit.platform.sdk.command.ICommandActivator;
import org.origin.common.extensions.core.IExtension;

public class CommandDescriptorImpl implements CommandDescriptor {

	protected IExtension extension;
	protected ICommandActivator command;

	/**
	 * 
	 * @param extension
	 */
	public CommandDescriptorImpl(IExtension extension) {
		this.extension = extension;
	}

	@Override
	public synchronized ICommandActivator getCommand() {
		if (this.command == null) {
			this.command = this.extension.getInterface(ICommandActivator.class);
		}
		return this.command;
	}

	@Override
	public void dispose() {
		this.command = null;
	}

}

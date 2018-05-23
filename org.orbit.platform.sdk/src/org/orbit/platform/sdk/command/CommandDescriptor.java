package org.orbit.platform.sdk.command;

public interface CommandDescriptor {

	ICommandActivator getCommand();

	void dispose();

}

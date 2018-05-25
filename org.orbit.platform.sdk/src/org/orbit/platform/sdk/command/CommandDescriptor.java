package org.orbit.platform.sdk.command;

public interface CommandDescriptor {

	CommandActivator getCommand();

	void dispose();

}

package org.orbit.platform.sdk.command;

public interface CommandExtensionRegistry {

	CommandDescriptor[] getDescriptors();

	void dispose();

}

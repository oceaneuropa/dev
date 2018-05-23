package org.orbit.platform.sdk.connector;

public interface ConnectorExtensionRegistry {

	ConnectorDescriptor[] getDescriptors();

	void dispose();

}

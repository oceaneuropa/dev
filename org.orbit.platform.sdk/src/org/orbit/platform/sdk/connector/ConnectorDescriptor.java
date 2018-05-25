package org.orbit.platform.sdk.connector;

public interface ConnectorDescriptor {

	ConnectorActivator getConnector();

	void dispose();

}

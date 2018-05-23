package org.orbit.platform.sdk.connector;

public interface ConnectorDescriptor {

	IConnectorActivator getConnector();

	void dispose();

}

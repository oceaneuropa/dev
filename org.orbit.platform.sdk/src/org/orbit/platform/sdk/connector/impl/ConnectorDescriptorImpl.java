package org.orbit.platform.sdk.connector.impl;

import org.orbit.platform.sdk.connector.ConnectorDescriptor;
import org.orbit.platform.sdk.connector.IConnectorActivator;
import org.origin.common.extensions.core.IExtension;

public class ConnectorDescriptorImpl implements ConnectorDescriptor {

	protected IExtension extension;
	protected IConnectorActivator connector;

	/**
	 * 
	 * @param extension
	 */
	public ConnectorDescriptorImpl(IExtension extension) {
		this.extension = extension;
	}

	@Override
	public synchronized IConnectorActivator getConnector() {
		if (this.connector == null) {
			this.connector = this.extension.getInterface(IConnectorActivator.class);
		}
		return this.connector;
	}

	@Override
	public void dispose() {
		this.connector = null;
	}

}

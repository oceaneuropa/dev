package org.orbit.platform.connector;

import org.orbit.platform.connector.impl.PlatformConnector;
import org.orbit.platform.sdk.connector.IConnectorActivator;
import org.origin.common.extensions.Extension;
import org.origin.common.extensions.InterfaceDescription;
import org.origin.common.extensions.ProgramExtensions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Extensions extends ProgramExtensions {

	protected static Logger LOG = LoggerFactory.getLogger(Extensions.class);

	public static Extensions INSTANCE = new Extensions();

	public Extensions() {
		setBundleId("org.orbit.platform.connector");
	}

	@Override
	public void createExtensions() {
		LOG.debug("createExtensions()");

		createConnectorExtensions();
	}

	protected void createConnectorExtensions() {
		String typeId = IConnectorActivator.TYPE_ID;

		// Platform connector
		Extension extension1 = new Extension(typeId, PlatformConnector.ID, "Platform Connector", "Platform connector description");
		InterfaceDescription desc1 = new InterfaceDescription(IConnectorActivator.class, PlatformConnector.class);
		extension1.addInterface(desc1);
		addExtension(extension1);
	}

}

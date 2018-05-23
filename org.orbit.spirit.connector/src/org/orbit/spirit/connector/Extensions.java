package org.orbit.spirit.connector;

import org.orbit.platform.sdk.connector.IConnectorActivator;
import org.orbit.spirit.connector.gaia.GAIAConnector;
import org.origin.common.extensions.Extension;
import org.origin.common.extensions.InterfaceDescription;
import org.origin.common.extensions.ProgramExtensions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Extensions extends ProgramExtensions {

	protected static Logger LOG = LoggerFactory.getLogger(Extensions.class);

	public static Extensions INSTANCE = new Extensions();

	public Extensions() {
		setBundleId("org.orbit.spirit.connector");
	}

	@Override
	public void createExtensions() {
		LOG.debug("createExtensions()");

		createConnectorExtensions();
	}

	protected void createConnectorExtensions() {
		String typeId = IConnectorActivator.TYPE_ID;

		// GAIA connector
		Extension extension1 = new Extension(typeId, GAIAConnector.ID, "GAIA Connector", "GAIA connector description");
		InterfaceDescription desc1 = new InterfaceDescription(IConnectorActivator.class, GAIAConnector.class);
		extension1.addInterface(desc1);
		addExtension(extension1);
	}

}

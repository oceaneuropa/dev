package org.orbit.infra.connector;

import org.orbit.infra.connector.channel.ChannelsConnector;
import org.orbit.infra.connector.cli.ChannelCommand;
import org.orbit.infra.connector.cli.IndexServiceCommand;
import org.orbit.infra.connector.extensionregistry.ExtensionRegistryConnector;
import org.orbit.infra.connector.indexes.IndexProviderConnector;
import org.orbit.infra.connector.indexes.IndexServiceConnector;
import org.orbit.platform.sdk.command.CommandActivator;
import org.orbit.platform.sdk.connector.ConnectorActivator;
import org.origin.common.extensions.Extension;
import org.origin.common.extensions.InterfaceDescription;
import org.origin.common.extensions.ProgramExtensions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Extensions extends ProgramExtensions {

	protected static Logger LOG = LoggerFactory.getLogger(Extensions.class);

	public static Extensions INSTANCE = new Extensions();

	public Extensions() {
		setBundleId("org.orbit.infra.connector");
	}

	@Override
	public void createExtensions() {
		LOG.debug("createExtensions()");

		createConnectorExtensions();
		createCommandExtensions();
	}

	protected void createConnectorExtensions() {
		String typeId = ConnectorActivator.TYPE_ID;

		// Index Service connector
		Extension extension1 = new Extension(typeId, IndexServiceConnector.ID, "Index Service Connector", "Index Service connector description");
		InterfaceDescription desc1 = new InterfaceDescription(ConnectorActivator.class, IndexServiceConnector.class);
		extension1.addInterface(desc1);
		addExtension(extension1);

		// Index Provider connector
		Extension extension2 = new Extension(typeId, IndexProviderConnector.ID, "Index Provider Connector", "Index Provider connector description");
		InterfaceDescription desc2 = new InterfaceDescription(ConnectorActivator.class, IndexProviderConnector.class);
		extension2.addInterface(desc2);
		addExtension(extension2);

		// Channel connector
		Extension extension3 = new Extension(typeId, ChannelsConnector.ID, "Cannel Connector", "Cannel connector description");
		InterfaceDescription desc3 = new InterfaceDescription(ConnectorActivator.class, ChannelsConnector.class);
		extension3.addInterface(desc3);
		addExtension(extension3);

		// Extension Registry
		Extension extension4 = new Extension(typeId, ExtensionRegistryConnector.ID, "Extension Registry Connector");
		InterfaceDescription desc4 = new InterfaceDescription(ConnectorActivator.class, ExtensionRegistryConnector.class);
		extension4.addInterface(desc4);
		addExtension(extension4);
	}

	protected void createCommandExtensions() {
		String typeId = CommandActivator.TYPE_ID;

		// Index Service command
		Extension extension1 = new Extension(typeId, IndexServiceCommand.ID, "Index Service Command", "Index Service command description");
		InterfaceDescription desc1 = new InterfaceDescription(CommandActivator.class, IndexServiceCommand.class);
		extension1.addInterface(desc1);
		addExtension(extension1);

		// Channel command
		Extension extension2 = new Extension(typeId, ChannelCommand.ID, "Channel Command", "Channel command description");
		InterfaceDescription desc2 = new InterfaceDescription(CommandActivator.class, ChannelCommand.class);
		extension2.addInterface(desc2);
		addExtension(extension2);
	}

}

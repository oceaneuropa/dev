package org.orbit.infra.connector;

import org.orbit.infra.connector.cli.IndexServiceClientCommand;
import org.orbit.infra.connector.configregistry.ConfigRegistryConnector;
import org.orbit.infra.connector.extensionregistry.ExtensionRegistryConnector;
import org.orbit.infra.connector.indexes.IndexProviderConnector;
import org.orbit.infra.connector.indexes.IndexServiceConnector;
import org.orbit.infra.connector.subscription.SubsServerConnector;
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

	@Override
	public void createExtensions() {
		createConnectorExtensions();
		createCommandExtensions();
	}

	protected void createConnectorExtensions() {
		String typeId = ConnectorActivator.EXTENSION_TYPE_ID;

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

		// Extension Registry
		Extension extension3 = new Extension(typeId, ExtensionRegistryConnector.ID, "Extension Registry Connector");
		InterfaceDescription desc3 = new InterfaceDescription(ConnectorActivator.class, ExtensionRegistryConnector.class);
		extension3.addInterface(desc3);
		addExtension(extension3);

		// Config Registry connector
		Extension extension4 = new Extension(typeId, ConfigRegistryConnector.ID, "Config Registry Connector");
		InterfaceDescription desc4 = new InterfaceDescription(ConnectorActivator.class, ConfigRegistryConnector.class);
		extension4.addInterface(desc4);
		addExtension(extension4);

		// SubsServer connector
		Extension extension5 = new Extension(typeId, SubsServerConnector.ID, "Subs Server Connector");
		InterfaceDescription desc5 = new InterfaceDescription(ConnectorActivator.class, SubsServerConnector.class);
		extension5.addInterface(desc5);
		addExtension(extension5);
	}

	protected void createCommandExtensions() {
		String typeId = CommandActivator.EXTENSION_TYPE_ID;

		// Index Service command
		Extension extension1 = new Extension(typeId, IndexServiceClientCommand.ID, "Index Service Command", "Index Service command description");
		InterfaceDescription desc1 = new InterfaceDescription(CommandActivator.class, IndexServiceClientCommand.class);
		extension1.addInterface(desc1);
		addExtension(extension1);
	}

}

package org.orbit.component.connector;

import org.orbit.component.cli.AppStoreCommand;
import org.orbit.component.cli.AuthCommand;
import org.orbit.component.cli.DomainManagementCommand;
import org.orbit.component.cli.MissionControlCommand;
import org.orbit.component.cli.NodeControlCommand;
import org.orbit.component.cli.NodeControlCommandGeneric;
import org.orbit.component.cli.ServicesCommand;
import org.orbit.component.cli.UserRegistryCommand;
import org.orbit.component.connector.tier1.account.UserAccountConnector;
import org.orbit.component.connector.tier1.auth.AuthConnector;
import org.orbit.component.connector.tier1.configregistry.ConfigRegistryConnector;
import org.orbit.component.connector.tier2.appstore.AppStoreConnector;
import org.orbit.component.connector.tier3.domain.DomainManagementConnector;
import org.orbit.component.connector.tier3.nodecontrol.NodeControlConnector;
import org.orbit.component.connector.tier4.mission.MissionControlConnector;
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
		setBundleId("org.orbit.component.connector");
	}

	@Override
	public void createExtensions() {
		LOG.debug("createExtensions()");

		createConnectorExtensions();
		createCommandExtensions();
	}

	protected void createConnectorExtensions() {
		String typeId = ConnectorActivator.TYPE_ID;

		// tier 1
		// User registry connector
		Extension extension1 = new Extension(typeId, UserAccountConnector.ID, "User Registry Connector", "User Registry connector description");
		InterfaceDescription desc1 = new InterfaceDescription(ConnectorActivator.class, UserAccountConnector.class);
		extension1.addInterface(desc1);
		addExtension(extension1);

		// Auth connector
		Extension extension2 = new Extension(typeId, AuthConnector.ID, "Auth Connector", "Auth connector description");
		InterfaceDescription desc2 = new InterfaceDescription(ConnectorActivator.class, AuthConnector.class);
		extension2.addInterface(desc2);
		addExtension(extension2);

		// Config registry connector
		Extension extension3 = new Extension(typeId, ConfigRegistryConnector.ID, "Config Registry Connector", "Config Registry connector description");
		InterfaceDescription desc3 = new InterfaceDescription(ConnectorActivator.class, ConfigRegistryConnector.class);
		extension3.addInterface(desc3);
		addExtension(extension3);

		// tier 2
		// App store connector
		Extension extension4 = new Extension(typeId, AppStoreConnector.ID, "App Store Connector", "App Store connector description");
		InterfaceDescription desc4 = new InterfaceDescription(ConnectorActivator.class, AppStoreConnector.class);
		extension4.addInterface(desc4);
		addExtension(extension4);

		// tier 3
		// Domain management connector
		Extension extension5 = new Extension(typeId, DomainManagementConnector.ID, "Domain Management Connector", "Domain Management connector description");
		InterfaceDescription desc5 = new InterfaceDescription(ConnectorActivator.class, DomainManagementConnector.class);
		extension5.addInterface(desc5);
		addExtension(extension5);

		// Node control connector
		Extension extension6 = new Extension(typeId, NodeControlConnector.ID, "Node Control Connector", "Node Control connector description");
		InterfaceDescription desc6 = new InterfaceDescription(ConnectorActivator.class, NodeControlConnector.class);
		extension6.addInterface(desc6);
		addExtension(extension6);

		// tier 4
		// Mission control connector
		Extension extension7 = new Extension(typeId, MissionControlConnector.ID, "Mission Control Connector", "Mission Control connector description");
		InterfaceDescription desc7 = new InterfaceDescription(ConnectorActivator.class, MissionControlConnector.class);
		extension7.addInterface(desc7);
		addExtension(extension7);
	}

	protected void createCommandExtensions() {
		String typeId = CommandActivator.TYPE_ID;

		// services
		// Services command
		Extension extension1 = new Extension(typeId, ServicesCommand.ID, "Services Command", "Services command description");
		InterfaceDescription desc1 = new InterfaceDescription(CommandActivator.class, ServicesCommand.class);
		extension1.addInterface(desc1);
		addExtension(extension1);

		// tier1
		// Auth command
		Extension extension2 = new Extension(typeId, AuthCommand.ID, "Auth Command", "Auth command description");
		InterfaceDescription desc2 = new InterfaceDescription(CommandActivator.class, AuthCommand.class);
		extension2.addInterface(desc2);
		addExtension(extension2);

		// User Registry command
		Extension extension3 = new Extension(typeId, UserRegistryCommand.ID, "User Registry Command", "User Registry command description");
		InterfaceDescription desc3 = new InterfaceDescription(CommandActivator.class, UserRegistryCommand.class);
		extension3.addInterface(desc3);
		addExtension(extension3);

		// tier2
		// App Store command
		Extension extension4 = new Extension(typeId, AppStoreCommand.ID, "App Store Command", "App Store command description");
		InterfaceDescription desc4 = new InterfaceDescription(CommandActivator.class, AppStoreCommand.class);
		extension4.addInterface(desc4);
		addExtension(extension4);

		// tier3
		// Domain Management command
		Extension extension5 = new Extension(typeId, DomainManagementCommand.ID, "Domain Management Command", "Domain Management command description");
		InterfaceDescription desc5 = new InterfaceDescription(CommandActivator.class, DomainManagementCommand.class);
		extension5.addInterface(desc5);
		addExtension(extension5);

		// Node Control command
		Extension extension6 = new Extension(typeId, NodeControlCommand.ID, "Node Control Command", "Node Control command description");
		InterfaceDescription desc6 = new InterfaceDescription(CommandActivator.class, NodeControlCommand.class);
		extension6.addInterface(desc6);
		addExtension(extension6);

		// Node Control (Generic) command
		Extension extension7 = new Extension(typeId, NodeControlCommandGeneric.ID, "Node Control (Generic) Command", "Node Control (Generic) command description");
		InterfaceDescription desc7 = new InterfaceDescription(CommandActivator.class, NodeControlCommandGeneric.class);
		extension7.addInterface(desc7);
		addExtension(extension7);

		// tier4
		// Mission Control command
		Extension extension8 = new Extension(typeId, MissionControlCommand.ID, "Mission Control Command", "Mission Control command description");
		InterfaceDescription desc8 = new InterfaceDescription(CommandActivator.class, MissionControlCommand.class);
		extension8.addInterface(desc8);
		addExtension(extension8);
	}

}

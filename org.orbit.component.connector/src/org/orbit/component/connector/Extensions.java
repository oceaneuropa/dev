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
import org.orbit.component.connector.tier2.appstore.AppStoreDownloader;
import org.orbit.component.connector.tier3.domain.DomainManagementConnector;
import org.orbit.component.connector.tier3.nodecontrol.NodeControlConnector;
import org.orbit.component.connector.tier4.mission.MissionControlConnector;
import org.orbit.platform.sdk.command.CommandActivator;
import org.orbit.platform.sdk.connector.ConnectorActivator;
import org.orbit.platform.sdk.downloader.DownloadSource;
import org.orbit.platform.sdk.downloader.Downloader;
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
		createDownloaderExtensions();
	}

	protected void createConnectorExtensions() {
		String extensionTypeId = ConnectorActivator.EXTENSION_TYPE_ID;

		// tier 1
		// User registry connector
		Extension extension1 = new Extension(extensionTypeId, UserAccountConnector.ID, "User Registry Connector", "User Registry connector description");
		InterfaceDescription desc1 = new InterfaceDescription(ConnectorActivator.class, UserAccountConnector.class);
		extension1.addInterface(desc1);
		addExtension(extension1);

		// Auth connector
		Extension extension2 = new Extension(extensionTypeId, AuthConnector.ID, "Auth Connector", "Auth connector description");
		InterfaceDescription desc2 = new InterfaceDescription(ConnectorActivator.class, AuthConnector.class);
		extension2.addInterface(desc2);
		addExtension(extension2);

		// Config registry connector
		Extension extension3 = new Extension(extensionTypeId, ConfigRegistryConnector.ID, "Config Registry Connector", "Config Registry connector description");
		InterfaceDescription desc3 = new InterfaceDescription(ConnectorActivator.class, ConfigRegistryConnector.class);
		extension3.addInterface(desc3);
		addExtension(extension3);

		// tier 2
		// App store connector
		Extension extension4 = new Extension(extensionTypeId, AppStoreConnector.ID, "App Store Connector", "App Store connector description");
		InterfaceDescription desc4 = new InterfaceDescription(ConnectorActivator.class, AppStoreConnector.class);
		extension4.addInterface(desc4);
		addExtension(extension4);

		// tier 3
		// Domain management connector
		Extension extension5 = new Extension(extensionTypeId, DomainManagementConnector.ID, "Domain Management Connector", "Domain Management connector description");
		InterfaceDescription desc5 = new InterfaceDescription(ConnectorActivator.class, DomainManagementConnector.class);
		extension5.addInterface(desc5);
		addExtension(extension5);

		// Node control connector
		Extension extension6 = new Extension(extensionTypeId, NodeControlConnector.ID, "Node Control Connector", "Node Control connector description");
		InterfaceDescription desc6 = new InterfaceDescription(ConnectorActivator.class, NodeControlConnector.class);
		extension6.addInterface(desc6);
		addExtension(extension6);

		// tier 4
		// Mission control connector
		Extension extension7 = new Extension(extensionTypeId, MissionControlConnector.ID, "Mission Control Connector", "Mission Control connector description");
		InterfaceDescription desc7 = new InterfaceDescription(ConnectorActivator.class, MissionControlConnector.class);
		extension7.addInterface(desc7);
		addExtension(extension7);
	}

	protected void createCommandExtensions() {
		String extensionTypeId = CommandActivator.EXTENSION_TYPE_ID;

		// services
		// Services command
		Extension extension1 = new Extension(extensionTypeId, ServicesCommand.ID, "Services Command", "Services command description");
		InterfaceDescription desc1 = new InterfaceDescription(CommandActivator.class, ServicesCommand.class);
		extension1.addInterface(desc1);
		addExtension(extension1);

		// tier1
		// Auth command
		Extension extension2 = new Extension(extensionTypeId, AuthCommand.ID, "Auth Command", "Auth command description");
		InterfaceDescription desc2 = new InterfaceDescription(CommandActivator.class, AuthCommand.class);
		extension2.addInterface(desc2);
		addExtension(extension2);

		// User Registry command
		Extension extension3 = new Extension(extensionTypeId, UserRegistryCommand.ID, "User Registry Command", "User Registry command description");
		InterfaceDescription desc3 = new InterfaceDescription(CommandActivator.class, UserRegistryCommand.class);
		extension3.addInterface(desc3);
		addExtension(extension3);

		// tier2
		// App Store command
		Extension extension4 = new Extension(extensionTypeId, AppStoreCommand.ID, "App Store Command", "App Store command description");
		InterfaceDescription desc4 = new InterfaceDescription(CommandActivator.class, AppStoreCommand.class);
		extension4.addInterface(desc4);
		addExtension(extension4);

		// tier3
		// Domain Management command
		Extension extension5 = new Extension(extensionTypeId, DomainManagementCommand.ID, "Domain Management Command", "Domain Management command description");
		InterfaceDescription desc5 = new InterfaceDescription(CommandActivator.class, DomainManagementCommand.class);
		extension5.addInterface(desc5);
		addExtension(extension5);

		// Node Control command
		Extension extension6 = new Extension(extensionTypeId, NodeControlCommand.ID, "Node Control Command", "Node Control command description");
		InterfaceDescription desc6 = new InterfaceDescription(CommandActivator.class, NodeControlCommand.class);
		extension6.addInterface(desc6);
		addExtension(extension6);

		// Node Control (Generic) command
		Extension extension7 = new Extension(extensionTypeId, NodeControlCommandGeneric.ID, "Node Control (Generic) Command", "Node Control (Generic) command description");
		InterfaceDescription desc7 = new InterfaceDescription(CommandActivator.class, NodeControlCommandGeneric.class);
		extension7.addInterface(desc7);
		addExtension(extension7);

		// tier4
		// Mission Control command
		Extension extension8 = new Extension(extensionTypeId, MissionControlCommand.ID, "Mission Control Command", "Mission Control command description");
		InterfaceDescription desc8 = new InterfaceDescription(CommandActivator.class, MissionControlCommand.class);
		extension8.addInterface(desc8);
		addExtension(extension8);
	}

	protected void createDownloaderExtensions() {
		Extension extension1 = new Extension(Downloader.EXTENSION_TYPE_ID, AppStoreDownloader.ID, "AppStore Downloader", "Be able to download apps from app store");
		extension1.setProperty(Downloader.PROP__DOWNLOAD_SOURCE, DownloadSource.Sources.APP_STORE.getId());
		InterfaceDescription desc1 = new InterfaceDescription(Downloader.class, AppStoreDownloader.class);
		extension1.addInterface(desc1);
		addExtension(extension1);
	}

}

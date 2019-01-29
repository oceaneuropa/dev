package org.orbit.component.connector;

import org.orbit.component.cli.AppStoreClientCommand;
import org.orbit.component.cli.AuthClientCommand;
import org.orbit.component.cli.DomainManagementClientCommand;
import org.orbit.component.cli.MissionControlClientCommand;
import org.orbit.component.cli.NodeControlClientCommand;
import org.orbit.component.cli.NodeControlClientCommandGeneric;
import org.orbit.component.cli.ServicesClientCommand;
import org.orbit.component.cli.UserRegistryClientCommand;
import org.orbit.component.connector.tier1.account.UserAccountConnector;
import org.orbit.component.connector.tier1.auth.AuthConnector;
import org.orbit.component.connector.tier1.configregistry.ConfigRegistryConnector;
import org.orbit.component.connector.tier1.identity.IdentityConnector;
import org.orbit.component.connector.tier2.appstore.AppStoreConnector;
import org.orbit.component.connector.tier2.appstore.AppStoreDownloader;
import org.orbit.component.connector.tier3.domain.DomainManagementConnector;
import org.orbit.component.connector.tier3.nodecontrol.NodeControlConnector;
import org.orbit.component.connector.tier4.missioncontrol.MissionControlConnector;
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
		// Identity
		Extension extension11 = new Extension(extensionTypeId, IdentityConnector.ID, "Identity Connector", "Identity connector description");
		InterfaceDescription desc11 = new InterfaceDescription(ConnectorActivator.class, IdentityConnector.class);
		extension11.addInterface(desc11);
		addExtension(extension11);

		// User registry connector
		Extension extension12 = new Extension(extensionTypeId, UserAccountConnector.ID, "User Registry Connector", "User Registry connector description");
		InterfaceDescription desc12 = new InterfaceDescription(ConnectorActivator.class, UserAccountConnector.class);
		extension12.addInterface(desc12);
		addExtension(extension12);

		// Auth connector
		Extension extension13 = new Extension(extensionTypeId, AuthConnector.ID, "Auth Connector", "Auth connector description");
		InterfaceDescription desc13 = new InterfaceDescription(ConnectorActivator.class, AuthConnector.class);
		extension13.addInterface(desc13);
		addExtension(extension13);

		// Config registry connector
		Extension extension14 = new Extension(extensionTypeId, ConfigRegistryConnector.ID, "Config Registry Connector", "Config Registry connector description");
		InterfaceDescription desc14 = new InterfaceDescription(ConnectorActivator.class, ConfigRegistryConnector.class);
		extension14.addInterface(desc14);
		addExtension(extension14);

		// Identity connector
		Extension extension15 = new Extension(extensionTypeId, IdentityConnector.ID, "Identity Connector", "Identity connector description");
		extension15.setProperty(ConnectorActivator.PROP__PROVIDER, "orbit");
		InterfaceDescription desc15 = new InterfaceDescription(ConnectorActivator.class, IdentityConnector.class);
		extension15.addInterface(desc15);
		addExtension(extension15);

		// tier 2
		// App store connector
		Extension extension21 = new Extension(extensionTypeId, AppStoreConnector.ID, "App Store Connector", "App Store connector description");
		InterfaceDescription desc21 = new InterfaceDescription(ConnectorActivator.class, AppStoreConnector.class);
		extension21.addInterface(desc21);
		addExtension(extension21);

		// tier 3
		// Domain service connector
		Extension extension31 = new Extension(extensionTypeId, DomainManagementConnector.ID, "Domain Management Connector", "Domain Management connector description");
		InterfaceDescription desc31 = new InterfaceDescription(ConnectorActivator.class, DomainManagementConnector.class);
		extension31.addInterface(desc31);
		addExtension(extension31);

		// Node control connector
		Extension extension32 = new Extension(extensionTypeId, NodeControlConnector.ID, "Node Control Connector", "Node Control connector description");
		InterfaceDescription desc32 = new InterfaceDescription(ConnectorActivator.class, NodeControlConnector.class);
		extension32.addInterface(desc32);
		addExtension(extension32);

		// tier 4
		// Mission control connector
		Extension extension41 = new Extension(extensionTypeId, MissionControlConnector.ID, "Mission Control Connector", "Mission Control connector description");
		InterfaceDescription desc41 = new InterfaceDescription(ConnectorActivator.class, MissionControlConnector.class);
		extension41.addInterface(desc41);
		addExtension(extension41);
	}

	protected void createCommandExtensions() {
		String extensionTypeId = CommandActivator.EXTENSION_TYPE_ID;

		// services
		// Services command
		Extension extension01 = new Extension(extensionTypeId, ServicesClientCommand.ID, "Services Command", "Services command description");
		InterfaceDescription desc01 = new InterfaceDescription(CommandActivator.class, ServicesClientCommand.class);
		extension01.addInterface(desc01);
		addExtension(extension01);

		// tier1
		// User Registry command
		Extension extension11 = new Extension(extensionTypeId, UserRegistryClientCommand.ID, "User Registry Command", "User Registry command description");
		InterfaceDescription desc11 = new InterfaceDescription(CommandActivator.class, UserRegistryClientCommand.class);
		extension11.addInterface(desc11);
		addExtension(extension11);

		// Auth command
		Extension extension12 = new Extension(extensionTypeId, AuthClientCommand.ID, "Auth Command", "Auth command description");
		InterfaceDescription desc12 = new InterfaceDescription(CommandActivator.class, AuthClientCommand.class);
		extension12.addInterface(desc12);
		addExtension(extension12);

		// tier2
		// App Store command
		Extension extension21 = new Extension(extensionTypeId, AppStoreClientCommand.ID, "App Store Command", "App Store command description");
		InterfaceDescription desc21 = new InterfaceDescription(CommandActivator.class, AppStoreClientCommand.class);
		extension21.addInterface(desc21);
		addExtension(extension21);

		// tier3
		// Domain Management command
		Extension extension31 = new Extension(extensionTypeId, DomainManagementClientCommand.ID, "Domain Management Command", "Domain Management command description");
		InterfaceDescription desc31 = new InterfaceDescription(CommandActivator.class, DomainManagementClientCommand.class);
		extension31.addInterface(desc31);
		addExtension(extension31);

		// Node Control command
		Extension extension32 = new Extension(extensionTypeId, NodeControlClientCommand.ID, "Node Control Command", "Node Control command description");
		InterfaceDescription desc32 = new InterfaceDescription(CommandActivator.class, NodeControlClientCommand.class);
		extension32.addInterface(desc32);
		addExtension(extension32);

		// Node Control (Generic) command
		Extension extension33 = new Extension(extensionTypeId, NodeControlClientCommandGeneric.ID, "Node Control (Generic) Command", "Node Control (Generic) command description");
		InterfaceDescription desc33 = new InterfaceDescription(CommandActivator.class, NodeControlClientCommandGeneric.class);
		extension33.addInterface(desc33);
		addExtension(extension33);

		// tier4
		// Mission Control command
		Extension extension41 = new Extension(extensionTypeId, MissionControlClientCommand.ID, "Mission Control Command", "Mission Control command description");
		InterfaceDescription desc41 = new InterfaceDescription(CommandActivator.class, MissionControlClientCommand.class);
		extension41.addInterface(desc41);
		addExtension(extension41);
	}

	protected void createDownloaderExtensions() {
		Extension extension1 = new Extension(Downloader.EXTENSION_TYPE_ID, AppStoreDownloader.ID, "AppStore Downloader", "Be able to download apps from app store");
		extension1.setProperty(Downloader.PROP__DOWNLOAD_SOURCE, DownloadSource.Sources.APP_STORE.getId());
		InterfaceDescription desc1 = new InterfaceDescription(Downloader.class, AppStoreDownloader.class);
		desc1.setSingleton(true);
		extension1.addInterface(desc1);
		addExtension(extension1);
	}

}

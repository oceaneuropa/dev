package org.orbit.infra.runtime;

import org.orbit.infra.runtime.extensions.ChannelServiceActivator;
import org.orbit.infra.runtime.extensions.ChannelServiceRelayActivator;
import org.orbit.infra.runtime.extensions.IndexServiceActivator;
import org.orbit.infra.runtime.extensions.IndexServiceRelayActivator;
import org.orbit.platform.sdk.ServiceActivator;
import org.orbit.platform.sdk.extension.desc.InterfaceDescription;
import org.orbit.platform.sdk.extension.desc.Parameter;
import org.orbit.platform.sdk.extension.desc.ProgramExtension;
import org.orbit.platform.sdk.extension.desc.ProgramExtensions;

/**
 * Infra extensions
 *
 */
public class Extensions extends ProgramExtensions {

	public static Extensions INSTANCE = new Extensions();

	@Override
	public void createExtensions() {
		createServiceActivatorExtensions();
	}

	protected void createServiceActivatorExtensions() {
		String extensionTypeId = ServiceActivator.TYPE_ID;

		// 1. Index Service
		{
			ProgramExtension extension = new ProgramExtension(extensionTypeId, IndexServiceActivator.ID);
			extension.setName("Index Service Activator");
			extension.setDescription("Index Service activator description");
			InterfaceDescription desc = new InterfaceDescription("IndexService", true, false);
			desc.setParameters( //
					new Parameter("component.index_service.name", "instance name"), //
					new Parameter("component.index_service.context_root", "web service context root"), //
					new Parameter("component.index_service.jdbc.driver", "JDBC driver"), //
					new Parameter("component.index_service.jdbc.url", "JDBC URL"), //
					new Parameter("component.index_service.jdbc.username", "JDBC username"), //
					new Parameter("component.index_service.jdbc.password", "JDBC password") //
			);
			extension.addInterface(ServiceActivator.class, IndexServiceActivator.INSTANCE, desc);
			addExtension(extension);
		}

		// 2. Channel Service
		{
			ProgramExtension extension = new ProgramExtension(extensionTypeId, ChannelServiceActivator.ID);
			extension.setName("Channel Service Activator");
			extension.setDescription("Channel Service activator description");
			InterfaceDescription desc = new InterfaceDescription("ChannelService", true, false);
			desc.setParameters( //
					new Parameter("component.channel.name", "instance name"), //
					new Parameter("component.channel.context_root", "web service context root"), //
					new Parameter("component.channel.http_port", "web socket http port") //
			);
			extension.addInterface(ServiceActivator.class, ChannelServiceActivator.INSTANCE, desc);
			addExtension(extension);
		}

		// 3. Index Service Relay
		{
			ProgramExtension extension = new ProgramExtension(extensionTypeId, IndexServiceRelayActivator.ID);
			extension.setName("Index Service Relay Activator");
			InterfaceDescription desc = new InterfaceDescription("IndexServiceRelay", true, false);
			desc.setParameters( //
					new Parameter(InfraConstants.COMPONENT_INDEX_SERVICE_RELAY_CONTEXT_ROOT, "web service relay context root"), //
					new Parameter(InfraConstants.COMPONENT_INDEX_SERVICE_RELAY_URLS, "target urls") //
			);
			extension.addInterface(ServiceActivator.class, IndexServiceRelayActivator.INSTANCE, desc);
			addExtension(extension);
		}

		// 4. Channel Service Relay
		{
			ProgramExtension extension = new ProgramExtension(extensionTypeId, ChannelServiceRelayActivator.ID);
			extension.setName("Channel Service Relay Activator");
			InterfaceDescription desc = new InterfaceDescription("ChannelServiceRelay", true, false);
			desc.setParameters( //
					new Parameter(InfraConstants.COMPONENT_CHANNEL_RELAY_CONTEXT_ROOT, "web service relay context root"), //
					new Parameter(InfraConstants.COMPONENT_CHANNEL_RELAY_URLS, "target urls") //
			);
			extension.addInterface(ServiceActivator.class, IndexServiceRelayActivator.INSTANCE, desc);
			addExtension(extension);
		}
	}

}

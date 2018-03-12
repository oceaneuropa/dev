package org.orbit.infra.runtime;

import org.orbit.infra.runtime.extensions.ChannelServiceActivator;
import org.orbit.infra.runtime.extensions.IndexServiceActivator;
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
		ProgramExtension indexServiceExtension = new ProgramExtension(extensionTypeId, IndexServiceActivator.ID);
		indexServiceExtension.setName("Index Service Activator");
		indexServiceExtension.setDescription("Index Service activator description");
		InterfaceDescription indexServiceDesc = new InterfaceDescription("IndexService", true, false);
		indexServiceDesc.setParameters( //
				new Parameter("component.index_service.name", "instance name"), //
				new Parameter("component.index_service.context_root", "web service context root"), //
				new Parameter("component.index_service.jdbc.driver", "JDBC driver"), //
				new Parameter("component.index_service.jdbc.url", "JDBC URL"), //
				new Parameter("component.index_service.jdbc.username", "JDBC username"), //
				new Parameter("component.index_service.jdbc.password", "JDBC password"), //
				new Parameter("orbit.host.url", "generic host url", true) //
		);
		indexServiceExtension.addInterface(ServiceActivator.class, IndexServiceActivator.INSTANCE, indexServiceDesc);
		addExtension(indexServiceExtension);

		// 2. Channel Service
		ProgramExtension channelServiceExtension = new ProgramExtension(extensionTypeId, ChannelServiceActivator.ID);
		channelServiceExtension.setName("Channel Service Activator");
		channelServiceExtension.setDescription("Channel Service activator description");
		InterfaceDescription channelServiceDesc = new InterfaceDescription("ChannelService", true, false);
		channelServiceDesc.setParameters( //
				new Parameter("component.channel.name", "instance name"), //
				new Parameter("component.channel.context_root", "web service context root"), //
				new Parameter("component.channel.http_port", "web socket http port"), //
				new Parameter("orbit.host.url", "generic host url", true) //
		);
		channelServiceExtension.addInterface(ServiceActivator.class, ChannelServiceActivator.INSTANCE, channelServiceDesc);
		// test for multi-interfaces in an extension
		// channelServiceExtension.addInterface(IndexServiceActivator.class, IndexServiceActivator.INSTANCE, indexServiceDesc);
		addExtension(channelServiceExtension);
	}

}

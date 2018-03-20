package org.orbit.infra.runtime;

import org.orbit.infra.runtime.extension.ChannelServiceActivator;
import org.orbit.infra.runtime.extension.ChannelServicePropertyTester;
import org.orbit.infra.runtime.extension.ChannelServiceRelayActivator;
import org.orbit.infra.runtime.extension.IndexServiceActivator;
import org.orbit.infra.runtime.extension.IndexServicePropertyTester;
import org.orbit.infra.runtime.extension.IndexServiceRelayActivator;
import org.orbit.platform.sdk.ServiceActivator;
import org.orbit.platform.sdk.condition.ConditionFactory;
import org.orbit.platform.sdk.extension.IPropertyTester;
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
		createPropertyTesterExtensions();
	}

	protected void createServiceActivatorExtensions() {
		String extensionTypeId = ServiceActivator.TYPE_ID;

		// Index Service
		ProgramExtension extension1 = new ProgramExtension(extensionTypeId, IndexServiceActivator.ID);
		extension1.setName("Index Service Activator");
		extension1.setDescription("Index Service activator description");
		InterfaceDescription desc1 = new InterfaceDescription("IndexService", true, false);
		desc1.setParameters( //
				new Parameter("component.index_service.name", "instance name"), //
				new Parameter("component.index_service.context_root", "web service context root"), //
				new Parameter("component.index_service.jdbc.driver", "JDBC driver"), //
				new Parameter("component.index_service.jdbc.url", "JDBC URL"), //
				new Parameter("component.index_service.jdbc.username", "JDBC username"), //
				new Parameter("component.index_service.jdbc.password", "JDBC password") //
		);
		desc1.setTriggerCondition(ConditionFactory.getInstance().newPropertyTesterCondition(IndexServicePropertyTester.ID));
		extension1.addInterface(ServiceActivator.class, IndexServiceActivator.INSTANCE, desc1);

		// Channel Service
		ProgramExtension extension2 = new ProgramExtension(extensionTypeId, ChannelServiceActivator.ID);
		extension2.setName("Channel Service Activator");
		extension2.setDescription("Channel Service activator description");
		InterfaceDescription desc2 = new InterfaceDescription("ChannelService", true, false);
		desc2.setParameters( //
				new Parameter("component.channel.name", "instance name"), //
				new Parameter("component.channel.context_root", "web service context root"), //
				new Parameter("component.channel.http_port", "web socket http port") //
		);
		extension2.addInterface(ServiceActivator.class, ChannelServiceActivator.INSTANCE, desc2);

		// Index Service Relay
		ProgramExtension extension3 = new ProgramExtension(extensionTypeId, IndexServiceRelayActivator.ID);
		extension3.setName("Index Service Relay Activator");
		InterfaceDescription desc3 = new InterfaceDescription("IndexServiceRelay", true, false);
		desc3.setParameters( //
				new Parameter(InfraConstants.COMPONENT_INDEX_SERVICE_RELAY_CONTEXT_ROOT, "web service relay context root"), //
				new Parameter(InfraConstants.COMPONENT_INDEX_SERVICE_RELAY_URLS, "target urls") //
		);
		extension3.addInterface(ServiceActivator.class, IndexServiceRelayActivator.INSTANCE, desc3);

		// Channel Service Relay
		ProgramExtension extension4 = new ProgramExtension(extensionTypeId, ChannelServiceRelayActivator.ID);
		extension4.setName("Channel Service Relay Activator");
		InterfaceDescription desc4 = new InterfaceDescription("ChannelServiceRelay", true, false);
		desc4.setParameters( //
				new Parameter(InfraConstants.COMPONENT_CHANNEL_RELAY_CONTEXT_ROOT, "web service relay context root"), //
				new Parameter(InfraConstants.COMPONENT_CHANNEL_RELAY_URLS, "target urls") //
		);
		extension4.addInterface(ServiceActivator.class, IndexServiceRelayActivator.INSTANCE, desc4);

		addExtension(extension1);
		addExtension(extension2);
		addExtension(extension3);
		addExtension(extension4);
	}

	protected void createPropertyTesterExtensions() {
		String extensionTypeId = IPropertyTester.TYPE_ID;

		// Index Service Property Tester
		ProgramExtension extension1 = new ProgramExtension(extensionTypeId, IndexServicePropertyTester.ID);
		extension1.setName("Index Service Property Tester");
		extension1.addInterface(IPropertyTester.class, IndexServicePropertyTester.INSTANCE);

		// Channel Service Property Tester
		ProgramExtension extension2 = new ProgramExtension(extensionTypeId, ChannelServicePropertyTester.ID);
		extension2.setName("Channel Service Property Tester");
		extension2.addInterface(IPropertyTester.class, ChannelServicePropertyTester.INSTANCE);

		addExtension(extension1);
		addExtension(extension2);
	}

}

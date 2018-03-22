package org.orbit.infra.runtime;

import org.orbit.infra.runtime.extension.ChannelServiceActivator;
import org.orbit.infra.runtime.extension.ChannelServicePropertyTester;
import org.orbit.infra.runtime.extension.ChannelServiceRelayActivator;
import org.orbit.infra.runtime.extension.ChannelServiceRelayPropertyTester;
import org.orbit.infra.runtime.extension.IndexServiceActivator;
import org.orbit.infra.runtime.extension.IndexServicePropertyTester;
import org.orbit.infra.runtime.extension.IndexServiceRelayActivator;
import org.orbit.infra.runtime.extension.IndexServiceRelayPropertyTester;
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

		// ----------------------------------------------------------------------
		// Services
		// ----------------------------------------------------------------------
		// Index Service
		ProgramExtension extension1 = new ProgramExtension(extensionTypeId, IndexServiceActivator.ID, "Index Service Activator", "Index Service activator description");
		InterfaceDescription desc1 = new InterfaceDescription("IndexService", ServiceActivator.class, IndexServiceActivator.class);
		desc1.setParameters( //
				new Parameter("component.index_service.name", "instance name"), //
				new Parameter("component.index_service.context_root", "web service context root"), //
				new Parameter("component.index_service.jdbc.driver", "JDBC driver"), //
				new Parameter("component.index_service.jdbc.url", "JDBC URL"), //
				new Parameter("component.index_service.jdbc.username", "JDBC username"), //
				new Parameter("component.index_service.jdbc.password", "JDBC password") //
		);
		desc1.setTriggerCondition(ConditionFactory.getInstance().newPropertyTesterCondition(IndexServicePropertyTester.ID));
		extension1.addInterface(desc1);
		addExtension(extension1);

		// Channel Service
		ProgramExtension extension2 = new ProgramExtension(extensionTypeId, ChannelServiceActivator.ID, "Channel Service Activator", "Channel Service activator description");
		InterfaceDescription desc2 = new InterfaceDescription("ChannelService", ServiceActivator.class, ChannelServiceActivator.class);
		desc2.setParameters( //
				new Parameter("component.channel.name", "instance name"), //
				new Parameter("component.channel.context_root", "web service context root"), //
				new Parameter("component.channel.http_port", "web socket http port") //
		);
		desc2.setTriggerCondition(ConditionFactory.getInstance().newPropertyTesterCondition(ChannelServicePropertyTester.ID));
		extension2.addInterface(desc2);
		addExtension(extension2);

		// ----------------------------------------------------------------------
		// Service Relays
		// ----------------------------------------------------------------------
		// Index Service Relay
		ProgramExtension extension3 = new ProgramExtension(extensionTypeId, IndexServiceRelayActivator.ID, "Index Service Relay Activator");
		InterfaceDescription desc3 = new InterfaceDescription("IndexServiceRelay", ServiceActivator.class, IndexServiceRelayActivator.class);
		desc3.setParameters( //
				new Parameter(InfraConstants.COMPONENT_INDEX_SERVICE_RELAY_CONTEXT_ROOT, "web service relay context root"), //
				new Parameter(InfraConstants.COMPONENT_INDEX_SERVICE_RELAY_URLS, "target urls") //
		);
		desc3.setTriggerCondition(ConditionFactory.getInstance().newPropertyTesterCondition(IndexServiceRelayPropertyTester.ID));
		extension3.addInterface(desc3);
		addExtension(extension3);

		// Channel Service Relay
		ProgramExtension extension4 = new ProgramExtension(extensionTypeId, ChannelServiceRelayActivator.ID, "Channel Service Relay Activator");
		InterfaceDescription desc4 = new InterfaceDescription("ChannelServiceRelay", ServiceActivator.class, ChannelServiceRelayActivator.class);
		desc4.setParameters( //
				new Parameter(InfraConstants.COMPONENT_CHANNEL_RELAY_CONTEXT_ROOT, "web service relay context root"), //
				new Parameter(InfraConstants.COMPONENT_CHANNEL_RELAY_URLS, "target urls") //
		);
		desc4.setTriggerCondition(ConditionFactory.getInstance().newPropertyTesterCondition(ChannelServiceRelayPropertyTester.ID));
		extension4.addInterface(desc4);
		addExtension(extension4);
	}

	protected void createPropertyTesterExtensions() {
		String extensionTypeId = IPropertyTester.TYPE_ID;

		// ----------------------------------------------------------------------
		// Property Testers
		// ----------------------------------------------------------------------
		// Index Service Property Tester
		ProgramExtension extension1 = new ProgramExtension(extensionTypeId, IndexServicePropertyTester.ID, "Index Service Property Tester");
		InterfaceDescription desc1 = new InterfaceDescription(IPropertyTester.class, IndexServicePropertyTester.class);
		extension1.addInterface(desc1);
		addExtension(extension1);

		// Channel Service Property Tester
		ProgramExtension extension2 = new ProgramExtension(extensionTypeId, ChannelServicePropertyTester.ID, "Channel Service Property Tester");
		InterfaceDescription desc2 = new InterfaceDescription(IPropertyTester.class, ChannelServicePropertyTester.class);
		extension2.addInterface(desc2);
		addExtension(extension2);

		// Index Service Relay Property Tester
		ProgramExtension extension3 = new ProgramExtension(extensionTypeId, IndexServiceRelayPropertyTester.ID, "Index Service Relay Property Tester");
		InterfaceDescription desc3 = new InterfaceDescription(IPropertyTester.class, IndexServiceRelayPropertyTester.class);
		extension3.addInterface(desc3);
		addExtension(extension3);

		// Channel Service Relay Property Tester
		ProgramExtension extension4 = new ProgramExtension(extensionTypeId, ChannelServiceRelayPropertyTester.ID, "Channel Service Relay Property Tester");
		InterfaceDescription desc4 = new InterfaceDescription(IPropertyTester.class, ChannelServiceRelayPropertyTester.class);
		extension4.addInterface(desc4);
		addExtension(extension4);
	}

}

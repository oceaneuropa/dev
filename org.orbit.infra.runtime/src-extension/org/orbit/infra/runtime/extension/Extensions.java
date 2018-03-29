package org.orbit.infra.runtime.extension;

import org.orbit.infra.runtime.InfraConstants;
import org.orbit.infra.runtime.extension.channel.ChannelServiceActivator;
import org.orbit.infra.runtime.extension.channel.ChannelServicePropertyTester;
import org.orbit.infra.runtime.extension.channel.ChannelServiceRelayActivator;
import org.orbit.infra.runtime.extension.channel.ChannelServiceRelayPropertyTester;
import org.orbit.infra.runtime.extension.indexes.IndexServiceActivator;
import org.orbit.infra.runtime.extension.indexes.IndexServicePropertyTester;
import org.orbit.infra.runtime.extension.indexes.IndexServiceRelayActivator;
import org.orbit.infra.runtime.extension.indexes.IndexServiceRelayPropertyTester;
import org.orbit.platform.sdk.extensions.ServiceActivator;
import org.origin.common.extensions.Extension;
import org.origin.common.extensions.InterfaceDescription;
import org.origin.common.extensions.Parameter;
import org.origin.common.extensions.ProgramExtensions;
import org.origin.common.extensions.condition.ConditionFactory;
import org.origin.common.extensions.condition.IPropertyTester;

/**
 * Infra extensions
 *
 */
public class Extensions extends ProgramExtensions {

	public static Extensions INSTANCE = new Extensions();

	public Extensions() {
		setBundleId("org.orbit.infra.runtime");
	}

	@Override
	public void createExtensions() {
		createServiceActivatorExtensions1();
		createServiceActivatorExtensions2();
		createPropertyTesterExtensions1();
		createPropertyTesterExtensions2();
	}

	protected void createServiceActivatorExtensions1() {
		String typeId = ServiceActivator.TYPE_ID;

		// Index Service
		Extension extension1 = new Extension(typeId, IndexServiceActivator.ID, "Index Service Activator", "Index Service activator description");
		InterfaceDescription desc1 = new InterfaceDescription(ServiceActivator.class, IndexServiceActivator.class);
		desc1.setParameters( //
				new Parameter("component.index_service.name", "instance name"), //
				new Parameter("component.index_service.context_root", "web service context root"), //
				new Parameter("component.index_service.jdbc.driver", "JDBC driver"), //
				new Parameter("component.index_service.jdbc.url", "JDBC URL"), //
				new Parameter("component.index_service.jdbc.username", "JDBC username"), //
				new Parameter("component.index_service.jdbc.password", "JDBC password") //
		);
		desc1.setTriggerCondition(ConditionFactory.getInstance().newPropertyTesterCondition(getRealm(), IndexServicePropertyTester.ID));
		extension1.addInterface(desc1);
		addExtension(extension1);

		// Channel Service
		Extension extension2 = new Extension(typeId, ChannelServiceActivator.ID, "Channel Service Activator", "Channel Service activator description");
		InterfaceDescription desc2 = new InterfaceDescription(ServiceActivator.class, ChannelServiceActivator.class);
		desc2.setParameters( //
				new Parameter("component.channel.name", "instance name"), //
				new Parameter("component.channel.context_root", "web service context root"), //
				new Parameter("component.channel.http_port", "web socket http port") //
		);
		desc2.setTriggerCondition(ConditionFactory.getInstance().newPropertyTesterCondition(getRealm(), ChannelServicePropertyTester.ID));
		extension2.addInterface(desc2);
		addExtension(extension2);
	}

	protected void createPropertyTesterExtensions1() {
		String typeId = IPropertyTester.TYPE_ID;

		// Index Service Property Tester
		Extension extension1 = new Extension(typeId, IndexServicePropertyTester.ID);
		InterfaceDescription desc1 = new InterfaceDescription(IPropertyTester.class, IndexServicePropertyTester.class);
		extension1.addInterface(desc1);
		addExtension(extension1);

		// Channel Service Property Tester
		Extension extension2 = new Extension(typeId, ChannelServicePropertyTester.ID);
		InterfaceDescription desc2 = new InterfaceDescription(IPropertyTester.class, ChannelServicePropertyTester.class);
		extension2.addInterface(desc2);
		addExtension(extension2);
	}

	protected void createServiceActivatorExtensions2() {
		String typeId = ServiceActivator.TYPE_ID;

		// Index Service Relay
		Extension extension3 = new Extension(typeId, IndexServiceRelayActivator.ID, "Index Service Relay Activator");
		InterfaceDescription desc3 = new InterfaceDescription(ServiceActivator.class, IndexServiceRelayActivator.class);
		desc3.setParameters( //
				new Parameter(InfraConstants.COMPONENT_INDEX_SERVICE_RELAY_CONTEXT_ROOT, "web service relay context root"), //
				new Parameter(InfraConstants.COMPONENT_INDEX_SERVICE_RELAY_URLS, "target urls") //
		);
		desc3.setTriggerCondition(ConditionFactory.getInstance().newPropertyTesterCondition(getRealm(), IndexServiceRelayPropertyTester.ID));
		extension3.addInterface(desc3);
		addExtension(extension3);

		// Channel Service Relay
		Extension extension4 = new Extension(typeId, ChannelServiceRelayActivator.ID, "Channel Service Relay Activator");
		InterfaceDescription desc4 = new InterfaceDescription(ServiceActivator.class, ChannelServiceRelayActivator.class);
		desc4.setParameters( //
				new Parameter(InfraConstants.COMPONENT_CHANNEL_RELAY_CONTEXT_ROOT, "web service relay context root"), //
				new Parameter(InfraConstants.COMPONENT_CHANNEL_RELAY_URLS, "target urls") //
		);
		desc4.setTriggerCondition(ConditionFactory.getInstance().newPropertyTesterCondition(getRealm(), ChannelServiceRelayPropertyTester.ID));
		extension4.addInterface(desc4);
		addExtension(extension4);
	}

	protected void createPropertyTesterExtensions2() {
		String typeId = IPropertyTester.TYPE_ID;

		// Index Service Relay Property Tester
		Extension extension3 = new Extension(typeId, IndexServiceRelayPropertyTester.ID, "Index Service Relay Property Tester");
		InterfaceDescription desc3 = new InterfaceDescription(IPropertyTester.class, IndexServiceRelayPropertyTester.class);
		extension3.addInterface(desc3);
		addExtension(extension3);

		// Channel Service Relay Property Tester
		Extension extension4 = new Extension(typeId, ChannelServiceRelayPropertyTester.ID, "Channel Service Relay Property Tester");
		InterfaceDescription desc4 = new InterfaceDescription(IPropertyTester.class, ChannelServiceRelayPropertyTester.class);
		extension4.addInterface(desc4);
		addExtension(extension4);
	}

}

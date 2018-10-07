package org.orbit.infra.runtime;

import org.orbit.infra.api.indexes.ServiceIndexTimerFactory;
import org.orbit.infra.runtime.cli.InfraCommand;
import org.orbit.infra.runtime.datacast.ws.DataCastServiceIndexTimerFactory;
import org.orbit.infra.runtime.datatube.ws.DataTubeServiceIndexTimerFactory;
import org.orbit.infra.runtime.extensionregistry.ws.ExtensionRegistryServiceIndexTimerFactory;
import org.orbit.infra.runtime.extensions.datacast.DataCastServiceActivator;
import org.orbit.infra.runtime.extensions.datacast.DataCastServicePropertyTester;
import org.orbit.infra.runtime.extensions.datacast.DataCastServiceRelayActivator;
import org.orbit.infra.runtime.extensions.datacast.DataCastServiceRelayPropertyTester;
import org.orbit.infra.runtime.extensions.datatube.DataTubeServiceActivator;
import org.orbit.infra.runtime.extensions.datatube.DataTubeServicePropertyTester;
import org.orbit.infra.runtime.extensions.datatube.DataTubeServiceRelayActivator;
import org.orbit.infra.runtime.extensions.datatube.DataTubeServiceRelayPropertyTester;
import org.orbit.infra.runtime.extensions.extensionregistry.ExtensionRegistryActivator;
import org.orbit.infra.runtime.extensions.extensionregistry.ExtensionRegistryPropertyTester;
import org.orbit.infra.runtime.extensions.extensionregistry.ExtensionRegistryRelayActivator;
import org.orbit.infra.runtime.extensions.extensionregistry.ExtensionRegistryRelayPropertyTester;
import org.orbit.infra.runtime.extensions.indexservice.IndexServiceActivator;
import org.orbit.infra.runtime.extensions.indexservice.IndexServicePropertyTester;
import org.orbit.infra.runtime.extensions.indexservice.IndexServiceRelayActivator;
import org.orbit.infra.runtime.extensions.indexservice.IndexServiceRelayPropertyTester;
import org.orbit.platform.sdk.command.CommandActivator;
import org.orbit.platform.sdk.serviceactivator.ServiceActivator;
import org.origin.common.extensions.Extension;
import org.origin.common.extensions.InterfaceDescription;
import org.origin.common.extensions.Parameter;
import org.origin.common.extensions.ProgramExtensions;
import org.origin.common.extensions.condition.ConditionFactory;
import org.origin.common.extensions.condition.IPropertyTester;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Infra extensions
 *
 */
public class Extensions extends ProgramExtensions {

	protected static Logger LOG = LoggerFactory.getLogger(Extensions.class);

	public static Extensions INSTANCE = new Extensions();

	public Extensions() {
		setBundleId("org.orbit.infra.runtime");
	}

	@Override
	public void createExtensions() {
		LOG.debug("createExtensions()");

		createServiceActivatorExtensions();
		createPropertyTesterExtensions();
		createIndexProviderExtensions();
		createCommandExtensions();
	}

	protected void createServiceActivatorExtensions() {
		String extensionTypeId = ServiceActivator.EXTENSION_TYPE_ID;

		// Index Service
		Extension extension11 = new Extension(extensionTypeId, IndexServiceActivator.ID, "Index Service Activator", "Index Service activator description");
		InterfaceDescription desc11 = new InterfaceDescription(ServiceActivator.class, IndexServiceActivator.class);
		desc11.setParameters( //
				new Parameter("component.index_service.name", "instance name"), //
				new Parameter("component.index_service.context_root", "web service context root"), //
				new Parameter("component.index_service.jdbc.driver", "JDBC driver"), //
				new Parameter("component.index_service.jdbc.url", "JDBC URL"), //
				new Parameter("component.index_service.jdbc.username", "JDBC username"), //
				new Parameter("component.index_service.jdbc.password", "JDBC password") //
		);
		desc11.setTriggerCondition(ConditionFactory.getInstance().newPropertyTesterCondition(IndexServicePropertyTester.ID));
		extension11.addInterface(desc11);
		addExtension(extension11);

		// ExtensionRegistry Service
		Extension extension12 = new Extension(extensionTypeId, ExtensionRegistryActivator.ID, "Extension Registry Service Activator", "Extension Registry Service activator description");
		InterfaceDescription desc12 = new InterfaceDescription(ServiceActivator.class, ExtensionRegistryActivator.class);
		desc12.setParameters( //
				new Parameter("component.extension_registry.name", "instance name"), //
				new Parameter("component.extension_registry.context_root", "web service context root"), //
				new Parameter("component.extension_registry.jdbc.driver", "JDBC driver"), //
				new Parameter("component.extension_registry.jdbc.url", "JDBC URL"), //
				new Parameter("component.extension_registry.jdbc.username", "JDBC username"), //
				new Parameter("component.extension_registry.jdbc.password", "JDBC password") //
		);
		desc12.setTriggerCondition(ConditionFactory.getInstance().newPropertyTesterCondition(ExtensionRegistryPropertyTester.ID));
		extension12.addInterface(desc12);
		addExtension(extension12);

		// Data Cast Service
		Extension extension13 = new Extension(extensionTypeId, DataCastServiceActivator.ID, "DataCast Service Activator", "DataCast Service activator description");
		InterfaceDescription desc13 = new InterfaceDescription(ServiceActivator.class, DataCastServiceActivator.class);
		desc13.setParameters( //
				new Parameter("infra.data_cast.id", "data tube id"), //
				new Parameter("infra.data_cast.name", "service name"), //
				new Parameter("infra.data_cast.context_root", "web service context root") //
		);
		desc13.setTriggerCondition(ConditionFactory.getInstance().newPropertyTesterCondition(DataCastServicePropertyTester.ID));
		extension13.addInterface(desc13);
		addExtension(extension13);

		// Data Tube Service
		Extension extension14 = new Extension(extensionTypeId, DataTubeServiceActivator.ID, "DataTube Service Activator", "DataTube Service activator description");
		InterfaceDescription desc14 = new InterfaceDescription(ServiceActivator.class, DataTubeServiceActivator.class);
		desc14.setParameters( //
				new Parameter("infra.data_tube.data_cast_id", "data cast id"), //
				new Parameter("infra.data_tube.id", "data tube id"), //
				new Parameter("infra.data_tube.name", "service name"), //
				new Parameter("infra.data_tube.context_root", "web service context root"), //
				new Parameter("infra.data_tube.http_port", "web socket http port") //
		);
		desc14.setTriggerCondition(ConditionFactory.getInstance().newPropertyTesterCondition(DataTubeServicePropertyTester.ID));
		extension14.addInterface(desc14);
		addExtension(extension14);

		// Index Service Relay
		Extension extension21 = new Extension(extensionTypeId, IndexServiceRelayActivator.ID, "Index Service Relay Activator");
		InterfaceDescription desc21 = new InterfaceDescription(ServiceActivator.class, IndexServiceRelayActivator.class);
		desc21.setParameters( //
				new Parameter(InfraConstants.COMPONENT_INDEX_SERVICE_RELAY_CONTEXT_ROOT, "web service relay context root"), //
				new Parameter(InfraConstants.COMPONENT_INDEX_SERVICE_RELAY_URLS, "target urls") //
		);
		desc21.setTriggerCondition(ConditionFactory.getInstance().newPropertyTesterCondition(IndexServiceRelayPropertyTester.ID));
		extension21.addInterface(desc21);
		addExtension(extension21);

		// Extension Registry Service Relay
		Extension extension22 = new Extension(extensionTypeId, ExtensionRegistryRelayActivator.ID, "Extension Registry Service Relay Activator");
		InterfaceDescription desc22 = new InterfaceDescription(ServiceActivator.class, ExtensionRegistryRelayActivator.class);
		desc22.setParameters( //
				new Parameter(InfraConstants.COMPONENT_EXTENSION_REGISTRY_RELAY_CONTEXT_ROOT, "web service relay context root"), //
				new Parameter(InfraConstants.COMPONENT_EXTENSION_REGISTRY_RELAY_URLS, "target urls") //
		);
		desc22.setTriggerCondition(ConditionFactory.getInstance().newPropertyTesterCondition(ExtensionRegistryRelayPropertyTester.ID));
		extension22.addInterface(desc22);
		addExtension(extension22);

		// DataCast Service Relay
		Extension extension23 = new Extension(extensionTypeId, DataCastServiceRelayActivator.ID, "DataCast Service Relay Activator");
		InterfaceDescription desc23 = new InterfaceDescription(ServiceActivator.class, DataCastServiceRelayActivator.class);
		desc23.setParameters( //
				new Parameter(InfraConstants.DATATUBE__RELAY_CONTEXT_ROOT, "web service relay context root"), //
				new Parameter(InfraConstants.DATATUBE__RELAY_URLS, "target urls") //
		);
		desc23.setTriggerCondition(ConditionFactory.getInstance().newPropertyTesterCondition(DataCastServiceRelayPropertyTester.ID));
		extension23.addInterface(desc23);
		addExtension(extension23);

		// DataTube Service Relay
		Extension extension24 = new Extension(extensionTypeId, DataTubeServiceRelayActivator.ID, "DataTube Service Relay Activator");
		InterfaceDescription desc24 = new InterfaceDescription(ServiceActivator.class, DataTubeServiceRelayActivator.class);
		desc24.setParameters( //
				new Parameter(InfraConstants.DATATUBE__RELAY_CONTEXT_ROOT, "web service relay context root"), //
				new Parameter(InfraConstants.DATATUBE__RELAY_URLS, "target urls") //
		);
		desc24.setTriggerCondition(ConditionFactory.getInstance().newPropertyTesterCondition(DataTubeServiceRelayPropertyTester.ID));
		extension24.addInterface(desc24);
		addExtension(extension24);
	}

	protected void createPropertyTesterExtensions() {
		String extensionTypeId = IPropertyTester.EXTENSION_TYPE_ID;

		// Index Service Property Tester
		Extension extension11 = new Extension(extensionTypeId, IndexServicePropertyTester.ID);
		InterfaceDescription desc11 = new InterfaceDescription(IPropertyTester.class, IndexServicePropertyTester.class);
		extension11.addInterface(desc11);
		addExtension(extension11);

		// Extension Registry Service Property Tester
		Extension extension12 = new Extension(extensionTypeId, ExtensionRegistryPropertyTester.ID);
		InterfaceDescription desc12 = new InterfaceDescription(IPropertyTester.class, ExtensionRegistryPropertyTester.class);
		extension12.addInterface(desc12);
		addExtension(extension12);

		// DataCast Service Property Tester
		Extension extension13 = new Extension(extensionTypeId, DataCastServicePropertyTester.ID);
		InterfaceDescription desc13 = new InterfaceDescription(IPropertyTester.class, DataCastServicePropertyTester.class);
		extension13.addInterface(desc13);
		addExtension(extension13);

		// DataTube Service Property Tester
		Extension extension14 = new Extension(extensionTypeId, DataTubeServicePropertyTester.ID);
		InterfaceDescription desc14 = new InterfaceDescription(IPropertyTester.class, DataTubeServicePropertyTester.class);
		extension14.addInterface(desc14);
		addExtension(extension14);

		// Index Service Relay Property Tester
		Extension extension21 = new Extension(extensionTypeId, IndexServiceRelayPropertyTester.ID, "Index Service Relay Property Tester");
		InterfaceDescription desc21 = new InterfaceDescription(IPropertyTester.class, IndexServiceRelayPropertyTester.class);
		extension21.addInterface(desc21);
		addExtension(extension21);

		// Extension Registry Service Relay Property Tester
		Extension extension22 = new Extension(extensionTypeId, ExtensionRegistryRelayPropertyTester.ID, "Extension Registry Service Relay Property Tester");
		InterfaceDescription desc22 = new InterfaceDescription(IPropertyTester.class, ExtensionRegistryRelayPropertyTester.class);
		extension22.addInterface(desc22);
		addExtension(extension22);

		// DataCast Service Relay Property Tester
		Extension extension23 = new Extension(extensionTypeId, DataCastServiceRelayPropertyTester.ID, "DataCast Service Relay Property Tester");
		InterfaceDescription desc23 = new InterfaceDescription(IPropertyTester.class, DataCastServiceRelayPropertyTester.class);
		extension23.addInterface(desc23);
		addExtension(extension23);

		// DataTube Service Relay Property Tester
		Extension extension24 = new Extension(extensionTypeId, DataTubeServiceRelayPropertyTester.ID, "DataTube Service Relay Property Tester");
		InterfaceDescription desc24 = new InterfaceDescription(IPropertyTester.class, DataTubeServiceRelayPropertyTester.class);
		extension24.addInterface(desc24);
		addExtension(extension24);
	}

	protected void createIndexProviderExtensions() {
		String extensionTypeId = ServiceIndexTimerFactory.EXTENSION_TYPE_ID;
		Class<?> factoryClass = ServiceIndexTimerFactory.class;

		Extension extension1 = new Extension(extensionTypeId, InfraConstants.INDEX_SERVICE_INDEXER_ID, "Index Service Index Provider");
		addExtension(extension1);

		Extension extension2 = new Extension(extensionTypeId, InfraConstants.EXTENSION_REGISTRY_INDEXER_ID, "Extension Registry Service Index Provider");
		extension2.addInterface(factoryClass, ExtensionRegistryServiceIndexTimerFactory.class);
		addExtension(extension2);

		Extension extension3 = new Extension(extensionTypeId, InfraConstants.IDX__DATACAST__INDEXER_ID, "DataCast Service Index Provider");
		extension3.addInterface(factoryClass, DataCastServiceIndexTimerFactory.class);
		addExtension(extension3);

		Extension extension4 = new Extension(extensionTypeId, InfraConstants.IDX__DATATUBE__INDEXER_ID, "DataTube Service Index Provider");
		extension4.addInterface(factoryClass, DataTubeServiceIndexTimerFactory.class);
		addExtension(extension4);
	}

	protected void createCommandExtensions() {
		String extensionTypeId = CommandActivator.EXTENSION_TYPE_ID;

		// Infra server side command
		Extension extension1 = new Extension(extensionTypeId, InfraCommand.ID, "Infra server side command", "Infra server side command description");
		InterfaceDescription desc1 = new InterfaceDescription(CommandActivator.class, InfraCommand.class);
		extension1.addInterface(desc1);
		addExtension(extension1);
	}

}

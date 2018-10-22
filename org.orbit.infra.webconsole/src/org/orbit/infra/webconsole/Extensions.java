package org.orbit.infra.webconsole;

import org.orbit.infra.webconsole.extension.WebApplicationActivator;
import org.orbit.infra.webconsole.extension.WebApplicationPropertyTester;
import org.orbit.platform.sdk.serviceactivator.ServiceActivator;
import org.origin.common.extensions.Extension;
import org.origin.common.extensions.InterfaceDescription;
import org.origin.common.extensions.ProgramExtensions;
import org.origin.common.extensions.condition.ConditionFactory;
import org.origin.common.extensions.condition.IPropertyTester;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Extensions extends ProgramExtensions {

	protected static Logger LOG = LoggerFactory.getLogger(Extensions.class);

	public static Extensions INSTANCE = new Extensions();

	public Extensions() {
		setBundleId("org.orbit.infra.webconsole");
	}

	@Override
	public void createExtensions() {
		LOG.debug("createExtensions()");

		createServiceActivatorExtensions();
		createPropertyTesterExtensions();
	}

	protected void createServiceActivatorExtensions() {
		String extensionTypeId = ServiceActivator.EXTENSION_TYPE_ID;

		// Infra Web Application Activator
		Extension extension1 = new Extension(extensionTypeId, WebApplicationActivator.ID, "Infra Web Application Activator");
		InterfaceDescription desc1 = new InterfaceDescription(ServiceActivator.class, WebApplicationActivator.class);
		desc1.setTriggerCondition(ConditionFactory.getInstance().newPropertyTesterCondition(WebApplicationPropertyTester.ID));
		extension1.addInterface(desc1);
		addExtension(extension1);
	}

	protected void createPropertyTesterExtensions() {
		String extensionTypeId = IPropertyTester.EXTENSION_TYPE_ID;

		// Infra Web Application Property Tester
		Extension extension1 = new Extension(extensionTypeId, WebApplicationPropertyTester.ID);
		extension1.addInterface(IPropertyTester.class, WebApplicationPropertyTester.class);
		addExtension(extension1);
	}

}

// createTopMenuExtensions();
// protected void createTopMenuExtensions() {
// String extensionTypeId = DropdownMenuProvider.EXTENSION_TYPE_ID;
// }

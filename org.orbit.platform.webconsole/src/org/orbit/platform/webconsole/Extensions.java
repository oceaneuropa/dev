package org.orbit.platform.webconsole;

import org.orbit.platform.sdk.serviceactivator.ServiceActivator;
import org.orbit.platform.webconsole.extension.PlatformWebApplicationActivator;
import org.orbit.platform.webconsole.extension.PlatformWebApplicationPropertyTester;
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
		setBundleId("org.orbit.platform.webconsole");
	}

	@Override
	public void createExtensions() {
		LOG.debug("createExtensions()");

		createServiceActivatorExtensions();
		createPropertyTesterExtensions();
	}

	protected void createServiceActivatorExtensions() {
		String typeId = ServiceActivator.TYPE_ID;

		// Platform Web Application Activator
		Extension extension1 = new Extension(typeId, PlatformWebApplicationActivator.ID, "Platform Web Application Activator");
		InterfaceDescription desc1 = new InterfaceDescription(ServiceActivator.class, PlatformWebApplicationActivator.class);
		desc1.setTriggerCondition(ConditionFactory.getInstance().newPropertyTesterCondition(PlatformWebApplicationPropertyTester.ID));
		extension1.addInterface(desc1);
		addExtension(extension1);
	}

	protected void createPropertyTesterExtensions() {
		String typeId = IPropertyTester.TYPE_ID;

		// Platform Web Application Property Tester
		Extension extension1 = new Extension(typeId, PlatformWebApplicationPropertyTester.ID);
		extension1.addInterface(IPropertyTester.class, PlatformWebApplicationPropertyTester.class);
		addExtension(extension1);
	}

}

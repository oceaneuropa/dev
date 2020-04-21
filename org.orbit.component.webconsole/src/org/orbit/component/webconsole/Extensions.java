package org.orbit.component.webconsole;

import org.orbit.component.webconsole.servlet.OriginWebApplicationActivator;
import org.orbit.component.webconsole.servlet.OriginWebApplicationPropertyTester;
import org.orbit.component.webconsole.servlet.OrbitHttpContextProvider;
import org.orbit.component.webconsole.servlet.PublicWebApplicationActivator;
import org.orbit.component.webconsole.servlet.PublicWebApplicationPropertyTester;
import org.orbit.component.webconsole.servlet.WebApplicationActivator;
import org.orbit.component.webconsole.servlet.WebApplicationPropertyTester;
import org.orbit.platform.sdk.ServiceActivator;
import org.orbit.platform.sdk.http.HttpContextProvider;
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
		setBundleId("org.orbit.component.webconsole");
	}

	@Override
	public void createExtensions() {
		LOG.debug("createExtensions()");

		createHttpContextProviderExtensions();
		createServiceActivatorExtensions();
		createPropertyTesterExtensions();
	}

	protected void createHttpContextProviderExtensions() {
		String typeId = HttpContextProvider.EXTENSION_TYPE_ID;

		// Extension extension1 = new Extension(typeId, PlatformHttpContextProvider.ID);
		Extension extension1 = new Extension(typeId, OrbitHttpContextProvider.ID);
		extension1.setProperty(HttpContextProvider.PROP__PROVIDER, "orbit");
		// extension1.addInterface(HttpContextProvider.class, PlatformHttpContextProvider.class);
		extension1.addInterface(HttpContextProvider.class, OrbitHttpContextProvider.class);
		addExtension(extension1);
	}

	/**
	 * ServiceActivator extensions
	 */
	protected void createServiceActivatorExtensions() {
		String typeId = ServiceActivator.EXTENSION_TYPE_ID;

		// Main Web Application Activator
		Extension extension1 = new Extension(typeId, OriginWebApplicationActivator.ID, "Main Web Application Activator");
		InterfaceDescription desc1 = new InterfaceDescription(ServiceActivator.class, OriginWebApplicationActivator.class);
		desc1.setTriggerCondition(ConditionFactory.getInstance().newPropertyTesterCondition(OriginWebApplicationPropertyTester.ID));
		extension1.addInterface(desc1);
		addExtension(extension1);

		// Public Web Application Activator
		Extension extension2 = new Extension(typeId, PublicWebApplicationActivator.ID, "Public Web Application Activator");
		InterfaceDescription desc2 = new InterfaceDescription(ServiceActivator.class, PublicWebApplicationActivator.class);
		desc2.setTriggerCondition(ConditionFactory.getInstance().newPropertyTesterCondition(PublicWebApplicationPropertyTester.ID));
		extension2.addInterface(desc2);
		addExtension(extension2);

		// Component Web Application Activator
		Extension extension3 = new Extension(typeId, WebApplicationActivator.ID, "Component Web Application Activator");
		InterfaceDescription desc3 = new InterfaceDescription(ServiceActivator.class, WebApplicationActivator.class);
		desc3.setTriggerCondition(ConditionFactory.getInstance().newPropertyTesterCondition(WebApplicationPropertyTester.ID));
		extension3.addInterface(desc3);
		addExtension(extension3);
	}

	/**
	 * Property Tester extensions
	 */
	protected void createPropertyTesterExtensions() {
		String typeId = IPropertyTester.EXTENSION_TYPE_ID;

		// Main Web Application
		Extension extension1 = new Extension(typeId, OriginWebApplicationPropertyTester.ID);
		extension1.addInterface(IPropertyTester.class, OriginWebApplicationPropertyTester.class);
		addExtension(extension1);

		// Public Web Application
		Extension extension2 = new Extension(typeId, PublicWebApplicationPropertyTester.ID);
		extension2.addInterface(IPropertyTester.class, PublicWebApplicationPropertyTester.class);
		addExtension(extension2);

		// Component Web Application
		Extension extension3 = new Extension(typeId, WebApplicationPropertyTester.ID);
		extension3.addInterface(IPropertyTester.class, WebApplicationPropertyTester.class);
		addExtension(extension3);
	}

}
